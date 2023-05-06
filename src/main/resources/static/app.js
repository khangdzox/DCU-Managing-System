var stompClient = null;
var userName = '';

function setConnected(connected) {
  $('#dcu-id').prop('readonly', connected);
  $('#connect').prop('disabled', connected);
  $('#disconnect').prop('disabled', !connected);
}

function connect() {
  if ($('#dcu-id').val() === '') {
    return;
  }
  $.post({
    contentType: 'application/json',
    url: '/api/authenticate',
    data: JSON.stringify({ username: 'admin', password: 'admin', rememberMe: true }),
  }).done(function (data) {
    var socket = new SockJS('/websocket/dcus?access_token=' + data.id_token);
    stompClient = Stomp.over(socket);
    stompClient.connect(
      {},
      function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/dcus/' + $('#dcu-id').val(), function (rawMessage) {
          message = JSON.parse(rawMessage.body);
          showMessage(message);
        });
        setConnected(true);
      },
      function (err) {
        alert('Error: ' + err);
        setConnected(false);
      }
    );
  });
}

function disconnect() {
  if (stompClient !== null) {
    setConnected(false);
    stompClient.disconnect();
  }
  console.log('Disconnected');
}

function showMessage(message) {
  $('#response').append(
    '<tr><td>{<br>' +
      '&emsp;key = {<br>' +
      '&emsp;&emsp;dcuId = ' +
      message.key.dcuId +
      ',<br>' +
      "&emsp;&emsp;timestamp = '" +
      message.key.timestamp +
      "',<br>" +
      '&emsp;&emsp;id = ' +
      message.key.id +
      '<br>' +
      '&emsp;},<br>' +
      '&emsp;current = ' +
      message.current +
      ',<br>' +
      '&emsp;voltage = ' +
      message.voltage +
      ',<br>' +
      '}</td></tr>'
  );
  $('.scrollable').animate({ scrollTop: $('.scrollable')[0].scrollHeight }, 'slow');
}

$(function () {
  $('form').on('submit', function (e) {
    e.preventDefault();
  });
  $('#connect').click(function () {
    connect();
  });
  $('#disconnect').click(function () {
    disconnect();
  });
});
