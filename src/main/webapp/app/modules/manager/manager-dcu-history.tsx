import React, { useEffect, useRef, useState } from 'react';
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend } from 'chart.js';
import { Line } from 'react-chartjs-2';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Storage, Translate } from 'react-jhipster';

import { getEntitiesInParent } from 'app/entities/record/record.reducer';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Button, Row } from 'reactstrap';
import { convertTimestampFromServer } from 'app/shared/util/date-utils';
import { useParams } from 'react-router-dom';

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

interface IDcuChartData {
  timestamp: Array<string>;
  voltage: Array<number>;
  current: Array<number>;
}

function updateChart(chart: ChartJS, updatedData: IDcuChartData) {
  chart.data.labels = updatedData.timestamp;
  chart.data.datasets[0].data = updatedData.voltage;
  chart.data.datasets[1].data = updatedData.current;
  // eslint-disable-next-line no-console
  console.log(chart.data);
  chart.update();
}

export const ManagerDcuHistory = () => {
  const dispatch = useAppDispatch();

  const recordList = useAppSelector(state => state.record.entities);
  const loading = useAppSelector(state => state.record.loading);

  const { did: id } = useParams<'did'>();

  useEffect(() => {
    dispatch(getEntitiesInParent({ id, limit: 100 }));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntitiesInParent({ id, limit: 100 }));
  };

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
        text: 'DCU History',
      },
    },
  };

  const labels = recordList
    .map(record => record.key.timestamp)
    .slice(0, 100)
    .reverse()
    .map(dtime => convertTimestampFromServer(dtime));

  const data = {
    labels,
    datasets: [
      {
        label: 'Voltage',
        data: recordList
          .map(record => record.voltage)
          .slice(0, 100)
          .reverse(),
        borderColor: 'rgb(255, 99, 132)',
        backgroundColor: 'rgba(255, 99, 132, 0.5)',
      },
      {
        label: 'Current',
        data: recordList
          .map(record => record.current)
          .slice(0, 100)
          .reverse(),
        borderColor: 'rgb(53, 162, 235)',
        backgroundColor: 'rgba(53, 162, 235, 0.5)',
      },
    ],
  };

  useEffect(() => {
    const chart = chartRef.current;

    const timestamp: Array<string> = recordList
      .map(record => record.key.timestamp)
      .slice(0, 100)
      .reverse()
      .map(dtime => convertTimestampFromServer(dtime));
    const voltage: Array<number> = recordList
      .map(record => record.voltage)
      .slice(0, 100)
      .reverse();
    const current: Array<number> = recordList
      .map(record => record.current)
      .slice(0, 100)
      .reverse();

    updateChart(chart, { timestamp, voltage, current });
  }, [recordList]);

  return (
    <>
      <div className="d-flex justify-content-end">
        <Button className="me-2" color="info" onClick={handleSyncList}>
          <FontAwesomeIcon icon="sync" spin={loading} /> Refresh Chart
        </Button>
      </div>
      <div style={{ height: '500px !important', width: '100%', position: 'relative' }}>
        {!loading ? <Line ref={chartRef} options={options} data={data} /> : null}
      </div>
    </>
  );
};

export default ManagerDcuHistory;
