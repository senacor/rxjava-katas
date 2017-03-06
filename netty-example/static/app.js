var socket = null;

$(function () {
    // prevent form submit
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    // connect buttons
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendName();
    });
    setConnected(false);
});

function openWebSocket(url) {
    if (!window.WebSocket) {
        window.WebSocket = window.MozWebSocket;
    }
    if (!window.WebSocket) {
        appendLog("Your browser does not support Web Socket!");
        throw new Error("Your browser does not support Web Socket!", "web_socket_unsupported");
    }
    return new WebSocket(url);
}

function connect() {
    socket = openWebSocket($("#url").val());

    socket.onopen = function () {
        setConnected(true);
        appendLog('Connected!');
    };

    socket.onmessage = function (e) {
        appendLog(e.data);
    };

    socket.onerror = function (error) {
        appendLog("WebSocket Error: " + error);
    };
}

function disconnect() {
    if (socket != null) {
        socket.close();
        socket = null;
        appendLog("Disconnected!");
    }
    setConnected(false);
}

function sendName() {
    socket.send($("#name").val());
}

function appendLog(message) {
    $("#log").append("<tr><td>" + message + "</td></tr>");
}

function setConnected(connected) {
    if (connected) {
        $("#connect").hide();
        $("#disconnect").show();
        $("#sendMsgRow").show();
    } else {
        $("#connect").show();
        $("#disconnect").hide();
        $("#sendMsgRow").hide();
    }
}


