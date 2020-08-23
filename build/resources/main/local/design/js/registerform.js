let lati = 0;
let longi = 0;
let myPicture;


let options = {
    enableHighAccuracy: true,
    timeout: 0,
    maximumAge: 0
};

function success(pos) {
    let crd = pos.coords;
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
    console.log("mypicture: " + myPicture.toString())
    document.getElementById("webcam-switch").checked = false;
    navigator.geolocation.getCurrentPosition(success, error);
}

function takePhoto() {

    beforeTakePhoto();
    myPicture = webcam.snap();
    console.log(myPicture.toString());
    afterTakePhoto();
    document.getElementById("webcam-switch").checked = false;
    let camApp = document.getElementById("webcam-app")
    let image = new Image();
    image.src = myPicture;
    image.onload = function () {
        camApp.style.backgroundImage = "url(" + this.src + ")";
    };
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
        },
        photo: {
            uri: myPicture.toString()
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
        document.getElementById("webcam-app").style.backgroundImage = "url(" + "/images/picture.png" + ")";
        document.getElementById("webcam-switch").checked = false;
        document.getElementById("webcam-switch").hidden = false;
        window.location.reload();
    };
}