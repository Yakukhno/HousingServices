function initMap() {
    var myLatLng = {lat: 50.450030, lng: 30.524448};

    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 10,
        center: myLatLng
    });

    var marker = new google.maps.Marker({
        position: myLatLng,
        map: map,
        draggable: true
    });

    var geocoder = new google.maps.Geocoder();

    marker.addListener('dragend', function () {
        geocoder.geocode({'location': marker.getPosition()}, function (results, status) {
            if (status === 'OK') {
                if (results[0]) {
                    document.getElementById('address').setAttribute('value', results[0].formatted_address);
                } else {
                    console.log("No result!");
                }
            } else {
                console.log("error!");
            }
        });
    })
}