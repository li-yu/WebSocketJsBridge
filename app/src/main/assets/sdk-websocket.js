var ws = new WebSocket("ws://localhost:8124/jssdk");

ws.onerror = function(event) {
    alert("JSSDK 连接异常,请检查！");
};

ws.onopen = function (evt) {
    console.log("Connection open ...");

};

ws.onmessage = function (evt) {
    console.log("Received Message: " + evt.data);
    if (typeof event.data == 'string') {
        document.getElementById("javaMsg").value = evt.data;
    }

};

ws.onclose = function (evt) {
    console.log("Connection closed.");
};

function sendMsgToJava(){
    if (ws.readyState == 1) {
        ws.send('hello,I am from JS.');
    }
}