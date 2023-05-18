import React, { useEffect, useRef, useState } from 'react';
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend } from 'chart.js';
import { Line } from 'react-chartjs-2';
import SockJSClient from 'react-stomp';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Storage } from 'react-jhipster';
import { convertTimestampFromServer } from 'app/shared/util/date-utils';

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

interface IDcuRealtimeData {
  voltage: number;
  current: number;
  timestamp: string;
}

function updateChart(chart: ChartJS, updatedData: IDcuRealtimeData) {
  chart.data.labels.push(updatedData.timestamp);
  chart.data.labels.shift();
  chart.data.datasets[0].data.push(updatedData.voltage);
  chart.data.datasets[0].data.shift();
  chart.data.datasets[1].data.push(updatedData.current);
  chart.data.datasets[1].data.shift();
  // eslint-disable-next-line no-console
  console.log(chart.data);
  chart.update('none');
}

export const ManagerDcuRealtime = () => {
  const [message, setMessage] = useState(null);
  const [loading, setLoading] = useState(true);
  const dcuEntity = useAppSelector(state => state.dcu.entity);

  const chartRef = useRef(null);

  const options = {
    responsive: true,
    maintainRatioAspect: true,
    plugins: {
      legend: {
        position: 'top' as const,
      },
      title: {
        display: true,
        text: 'DCU Realtime Report',
      },
    },
  };

  const data = {
    labels: new Array<string>(100).fill(''),
    datasets: [
      {
        label: 'Voltage',
        data: [],
        borderColor: 'rgb(255, 99, 132)',
        backgroundColor: 'rgba(255, 99, 132, 0.5)',
      },
      {
        label: 'Current',
        data: [],
        borderColor: 'rgb(53, 162, 235)',
        backgroundColor: 'rgba(53, 162, 235, 0.5)',
      },
    ],
  };

  const loc = window.location;
  const baseHref = document.querySelector('base').getAttribute('href').replace(/\/$/, '');

  let url = '//' + loc.host + baseHref + '/websocket/dcus';
  const authToken = Storage.local.get('jhi-authenticationToken') || Storage.session.get('jhi-authenticationToken');
  if (authToken) {
    url += '?access_token=' + authToken;
  }

  const onConnected = () => {
    setLoading(false);
  };

  const onMessageReceived = (msg: any) => {
    // eslint-disable-next-line no-console
    console.log(msg);
    setMessage(msg);
  };

  useEffect(() => {
    const chart = chartRef.current;

    if (message != null) {
      updateChart(chart, {
        voltage: message.voltage,
        current: message.current,
        timestamp: convertTimestampFromServer(message.key.timestamp),
      });
    }
  }, [message]);

  return (
    <>
      <SockJSClient
        url={url}
        topics={['/topic/dcus/' + dcuEntity.id]}
        onConnect={onConnected}
        onDisconnect={() => setLoading(true)}
        onMessage={msg => onMessageReceived(msg)}
        debug={true}
      />
      <div style={{ height: '500px !important', width: '100%', position: 'relative' }}>
        {!loading ? <Line ref={chartRef} options={options} data={data} /> : null}
      </div>
    </>
  );
};
