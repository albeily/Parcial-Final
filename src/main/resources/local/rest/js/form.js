let lati = 0;
let longi = 0;


function success(pos) {
    let crd = pos.coords;
    lati = crd.latitude;
    longi = crd.longitude;
    console.log(lati)
    if(lati!==0){
        registerForm();
    }
    if (lati === 0) {
        window.alert("Location not retrieved correctly!")
    }

};

function error(err) {
    console.warn('ERROR(' + err.code + '): ' + err.message);
}

function currentPosition() {
    document.getElementById("webcam-switch").checked = false;
    navigator.geolocation.getCurrentPosition(success, error);
}

function formList () {
    document.getElementById("album").innerHTML = "";
    let forms = "";
    const Http = new XMLHttpRequest();
    const url = 'http://albeily.me/api/rest/form';
    Http.open("GET", url);
    Http.setRequestHeader("token", localStorage.getItem("token"));
    Http.send();
    Http.onreadystatechange = function () {
            if (this.readyState === 4 && this.status === 200) {
                forms = JSON.parse(Http.responseText);
                console.log(forms);
                for (let i in forms) {
                    let card = document.createElement("div");
                    card.className = "card mb-4 shadow-sm";
                    card.id = forms[i].id;
                    let img = document.createElement("img");
                    img.id = forms[i].id;
                    img.className = "card-img-top";
                    img.style.maxWidth = "inherit";
                    img.style.maxHeight = "inherit";
                    img.src = forms[i].person.photo.uri;
                    let cardbody = document.createElement("div");
                    cardbody.id = forms[i].id;
                    cardbody.className = "card-body";
                    let cardinfo = document.createElement("p");
                    cardinfo.className = "card-text";
                    let name = document.createElement("p");
                    name.id = forms[i].id;
                    name.textContent = forms[i].person.name + " " + forms[i].person.lastname
                    name.style.fontWeight = "bold";
                    let sector = document.createElement("p");
                    sector.id = forms[i].id;
                    sector.textContent = "Sector: " + forms[i].sector;
                    let education = document.createElement("p");
                    education.id = forms[i].id;
                    education.textContent = "Education: " + forms[i].education;
                    cardinfo.append(name, sector, education);
                    cardbody.appendChild(cardinfo);
                    card.append(img, cardbody);
                    let div = document.createElement("div");
                    div.id = forms[i].id;
                    div.className = "col-md-4";
                    div.appendChild(card);
                    document.getElementById("album").appendChild(div);
                }
            }

        }


    }


function registerForm() {
    const Http = new XMLHttpRequest();
    const url='http://albeily.me/api/rest/form';
    Http.open("POST", url);
    Http.setRequestHeader("token",localStorage.getItem("token"));
    Http.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    let sendform = {
        education: document.getElementById("education").value,
        id: -1,
        sector: document.getElementById("sector").value,
        location: {
            latitude: lati,
            longitude: longi
        },
        person: {
            lastname: document.getElementById("lastname").value,
            name: document.getElementById("name").value,
            photo: {
                uri: picture.toString()
            }
        },
        user: {
            username: "user"
        }
    };

    Http.send(JSON.stringify(sendform));

    Http.onreadystatechange = (e) => {
        console.log(Http.responseText);
    }
    window.confirm("Your form was sent successfully!");
    window.location.reload();
}