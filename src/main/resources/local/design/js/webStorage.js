window.addEventListener('storage', persistence);

function cookie() {
    //Creating item in localStorage
    localStorage.setItem("UID", getCookie("UID"));
    localStorage.setItem("UPA", getCookie("UPA"));
}

function persistence(e) {
    console.log("Event: " + e)
    console.log('key: ' + e.key);
    console.log('old: ' + e.oldValue);
    console.log('new: ' + e.newValue);
    console.log('url: ' + e.url);
    console.log('storage: ' + e.storageArea);
}

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');

    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];

        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }

        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }

    return "";
}

