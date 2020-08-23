var webSocket;
var timeout = 5000;

setInterval(reconnect, timeout);

$(document).ready(function () {
    connect();
});

function send() {
    var data = dataBase.result.transaction(["forms"]);
    var forms = data.objectStore("forms");
    var allRecords = forms.getAll();

    if (window.navigator.onLine) {
        allRecords.onsuccess = function () {
            webSocket.send(JSON.stringify(allRecords.result));
            cleanDatabase();
            formList();
        };
    } else if (!window.navigator.onLine) {
        window.alert("Sorry, you have no connection. Please, try again!");
        formList();
    }

}

function connect() {
    webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/");

    webSocket.onopen = function (e) {
        console.log("Connected");
    };

    webSocket.onclose = function (e) {
        console.log("Disconnected");
    };
}

function reconnect() {
    if (!webSocket || webSocket.readyState == 3) {
        connect();
    }
}

function cleanDatabase() {
    var data = dataBase.result.transaction(["forms"], ["readwrite"]);
    var objectStore = data.objectStore("forms");
    var objectStoreRequest = objectStore.clear();

    objectStoreRequest.onsuccess = function (event) {
        formList();
    };
}