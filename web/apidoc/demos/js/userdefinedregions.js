/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


var poly, map;
var markers = [];
var path = new google.maps.MVCArray;

function initialize() {
    var uluru = new google.maps.LatLng(-25.344, 131.036);

    map = new google.maps.Map(document.getElementById("testmap"), {
        zoom: 14,
        center: uluru,
        mapTypeId: google.maps.MapTypeId.SATELLITE
    });

    poly = new google.maps.Polygon({
        strokeWeight: 3,
        fillColor: '#5555FF'
    });
    poly.setMap(map);
    poly.setPaths(new google.maps.MVCArray([path]));

    google.maps.event.addListener(map, 'click', addPoint);
}

function addPoint(event) {
    path.insertAt(path.length, event.latLng);

    var marker = new google.maps.Marker({
        position: event.latLng,
        map: map,
        draggable: true
    });
    markers.push(marker);
    marker.setTitle("#" + path.length);

    google.maps.event.addListener(marker, 'click', function() {
        marker.setMap(null);
        for (var i = 0, I = markers.length; i < I && markers[i] != marker; ++i);
        markers.splice(i, 1);
        path.removeAt(i);
        
    }
    );

    google.maps.event.addListener(marker, 'dragend', function() {
        for (var i = 0, I = markers.length; i < I && markers[i] != marker; ++i);
        path.setAt(i, marker.getPosition());
    //        $('#pointlog').clear();
        
    }
    );

    $('#pointlog').empty();
    for(var p = 0; p < path.length; p++){
        $('#pointlog').append(path.getAt(p).lng() + ' ' + path.getAt(p).lat())

    }
}

function createRequestString(){
    var pathArray = path;
    var coordsString;
    for(var i in pathArray){
        coordsString += pathArray.getAt(i).lng() + ',' + pathArray.getAt(i).lat();
        if(i < pathArray.length - 1){
            coordsString += '|';
        }
    }
    return coordsString;
}