let locations = [];

function createLocations(forms) {
    let location = [];
    let text = "";

    locations = [];

    for (let i in forms) {
        text = forms[i].id + " " + forms[i].person.name + " " + forms[i].person.lastname;
        location = [text, forms[i].location.latitude, forms[i].location.longitude];
        locations.push(location);
    }

    initMap();
}

function initMap(listener) {
    var position = {
        lat: 18.950000,
        lng: -70.000000
    };

    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 8,
        center: position
    });

    for (let count = 0; count < locations.length; count++) {

        var marker = new google.maps.Marker({
            position: new google.maps.LatLng(locations[count][1], locations[count][2]),
            map: map
        });

        marker.info = new google.maps.InfoWindow({
            content: locations [count][0]
        });

        marker.addListener('click', function () {
            var marker_map = this.getMap();
            this.info.open(marker_map, this);
        });
    }
}