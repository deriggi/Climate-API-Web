/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


var poly, map;
var markers = [];
var path = new google.maps.MVCArray;
var cacheKey = null;
var outputPath = null;

function initialize() {
    var uluru = new google.maps.LatLng(-3, 29);

    map = new google.maps.Map(document.getElementById("testmap"), {
        zoom: 3,
        center: uluru,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    });

    poly = new google.maps.Polygon({
        strokeWeight: 3,
        fillColor: '#5555FF'
    });
    poly.setMap(map);
    poly.setPaths(new google.maps.MVCArray([path]));

    google.maps.event.addListener(map, 'click', addPoint);
}
// set interval
var tid = null;
function checkPercentComplete() {
    makePercentCompleteRequest(cacheKey);
}
function abortTimer() { // to be called when you want to stop the timer
    clearInterval(tid);
}


// call get

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
    var pathArray = path.getArray();
    var coordsString= '';

    for(var i in pathArray){
        coordsString += pathArray[i].lng() + ',' + pathArray[i].lat();
        if(i < pathArray.length - 1){
            coordsString += '|';
        }
    }
    coordsString+= '|' + pathArray[0].lng() + ',' + pathArray[0].lat();
    return coordsString;
}


function makeIntersectionRequest(){
    var boundary = createRequestString();
    $.get('/climateweb/userregion',{
        region:boundary
    }, function(data){
        outputPath = data.path;
        cacheKey =   data.cachekey;
        setInterval(checkPercentComplete, 5000);
    });
}

function makePercentCompleteRequest(cacheKey){
    if(cacheKey != null){
        $.get('/climateweb/rest/projectstatus/percentcomplete/' + cacheKey,function(data){
            $('#statustrail').text( data )
            if(data >= 100){
                // done so get the file written location
                makeFinalRestingPlaceRequest(cacheKey);
            }
        });
    }
}

function makeFinalRestingPlaceRequest(cacheKey){
    if(cacheKey != null){
        $.get('/climateweb/rest/projectstatus/restingplace/' + cacheKey,function(data){
            alert(data);
        });
    }
}


// ================================================================================

// on ready

// ================================================================================


$(document).ready(function(){

    $('#showdata').click(function(){
        makeIntersectionRequest();
    })
    
});
