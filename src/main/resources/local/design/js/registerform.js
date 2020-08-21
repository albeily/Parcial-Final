var lati = 0;
var longi = 0;
var options = {
    enableHighAccuracy: true,
    timeout: 0,
    maximumAge: 0
};

function success(pos) {
    var crd = pos.coords;
    lati = crd.latitude;
    longi = crd.longitude;

    if (lati === 0 && longi === 0) {
        document.getElementById("saveModalBody").innerHTML = '';
        document.getElementById("saveModalBody").innerHTML = '<p>Location has not been retrieved. Do you still want to save this form?</p>';
    }
};

function error(err) {
    console.warn('ERROR(' + err.code + '): ' + err.message);
}

function currentPosition() {
    navigator.geolocation.getCurrentPosition(success, error);
}

function registerForm() {
    var dbActive = dataBase.result;
    var transaction = dbActive.transaction(["forms"], "readwrite");
    var forms = transaction.objectStore("forms");

    var request = forms.put({
        person: {
            name: document.querySelector("#name").value,
            lastname: document.querySelector("#lastname").value
        },
        sector: document.querySelector("#sector").value,
        education: document.querySelector("#education").value,
        location: {
            latitude: lati,
            longitude: longi
        },
        user: {
            username: localStorage.getItem("UID").toString()
        }
    });

    transaction.onerror = function (e) {
        alert(request.error.name + " " + request.error.message);
    };

    request.onerror = function (e) {
        var message = "Error: " + e.target.errorCode;
        console.error(message);
        alert(message)
    };

    request.onsuccess = function (e) {
        console.log("Data acquired:");
        document.querySelector("#name").value = "";
        document.querySelector("#lastname").value = "";
        document.querySelector("#sector").value = "";
        document.querySelector("#education").value = "";
    };
}