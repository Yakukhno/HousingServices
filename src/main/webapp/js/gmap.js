var autocomplete;

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

    autocomplete = new google.maps.places.Autocomplete(
        (document.getElementById('address')),
        {types: ['geocode']});
    autocomplete.bindTo('bounds', map);
    autocomplete.addListener('place_changed', function() {
        marker.setVisible(false);
        var place = autocomplete.getPlace();
        if (!place.geometry) {
            // User entered the name of a Place that was not suggested and
            // pressed the Enter key, or the Place Details request failed.
            window.alert("No details available for input: '" + place.name + "'");
            return;
        }

        // If the place has a geometry, then present it on a map.
        if (place.geometry.viewport) {
            map.fitBounds(place.geometry.viewport);
        } else {
            map.setCenter(place.geometry.location);
            map.setZoom(17);  // Why 17? Because it looks good.
        }
        marker.setPosition(place.geometry.location);
        marker.setVisible(true);
    });
}