

//var campus = {
//    "type": "Feature",
//    "properties": {
//        "popupContent": "This is one class of climate data",
//        "style": {
//            weight: 2,
//            color: "#999",
//            opacity: 1,
//            fillColor: "#B0DE5C",
//            fillOpacity: 0.8
//        }
//    },
//    'geometry': { 'type': 'MultiPolygon',coordinates: [[[[-74,7.0000000000000995],[-74,7.5000000000000995],[-74,8.0000000000001],[-73.5,8.0000000000001],[-73.5,7.5000000000000995],[-73.5,7.0000000000000995],[-74,7.0000000000000995]],[[-73.5,9.0000000000001],[-73.5,8.5000000000001],[-74,8.5000000000001],[-74.5,8.5000000000001],[-75,8.5000000000001],[-75,9.0000000000001],[-75,9.5000000000001],[-75,10.0000000000001],[-74.5,10.0000000000001],[-74,10.0000000000001],[-73.5,10.0000000000001],[-73.5,9.5000000000001],[-73.5,9.0000000000001]],[[-68.5,4.0000000000000995],[-68.5,4.5000000000000995],[-69,4.5000000000000995],[-69,5.0000000000000995],[-69.5,5.0000000000000995],[-69.5,5.5000000000000995],[-69,5.5000000000000995],[-69,6.0000000000000995],[-69,6.5000000000000995],[-68.5,6.5000000000000995],[-68,6.5000000000000995],[-67.5,6.5000000000000995],[-67,6.5000000000000995],[-67,6.0000000000000995],[-67.5,6.0000000000000995],[-67.5,5.5000000000000995],[-67.5,5.0000000000000995],[-67.5,4.5000000000000995],[-67.5,4.0000000000000995],[-68,4.0000000000000995],[-68.5,4.0000000000000995]]]]}
//};

function makeLayers(howMany){
    var layers = [];
    var i = 0;
                
    while(i < howMany){
        var layer;
        layer = new L.GeoJSON(null, {
            pointToLayer: function (latlng){
                return new L.CircleMarker(latlng, {
                    radius: 8,
                    fillColor: "#ff7800",
                    color: "#000",
                    weight: 1,
                    opacity: 1,
                    fillOpacity: 0.8
                });
            }
        });
        layer.on("featureparse", function (e) {
            var popupContent = "<p>I started out as a GeoJSON " + e.geometryType + ", but now I'm a Leaflet vector!</p>";
		   
            if (e.properties && e.properties.popupContent) {
                popupContent += e.properties.popupContent;
            }
            e.layer.bindPopup(popupContent);
            if (e.properties && e.properties.style && e.layer.setStyle) {
                e.layer.setStyle(e.properties.style);
            }
        });
        layers[i++] = layer;
                    
    }
                
    return layers;
                
                
}
            
            
var allClasses = [];
allClasses[0] = {
    "type": "Feature",
    "properties": {
        "popupContent": "This is one class of climate data",
        "style": {
            weight: 2,
            color: "#999",
            opacity: 1,
            fillColor: "#B0DE5C",
            fillOpacity: 0.8
        }
    },
   'geometry': { 'type': 'MultiPolygon',coordinates: [[[[-78,0.5000000000000995],[-78,1.0000000000000995],[-77.5,1.0000000000000995],[-77.5,0.5000000000000995],[-78,0.5000000000000995]],[[-74.5,4.0000000000000995],[-74.5,4.5000000000000995],[-74,4.5000000000000995],[-74,4.0000000000000995],[-74.5,4.0000000000000995]],[[-75.5,4.5000000000000995],[-75.5,5.0000000000000995],[-75,5.0000000000000995],[-75,4.5000000000000995],[-75.5,4.5000000000000995]],[[-73,6.0000000000000995],[-73,6.5000000000000995],[-72.5,6.5000000000000995],[-72.5,6.0000000000000995],[-73,6.0000000000000995]],[[-73,7.0000000000000995],[-73,7.5000000000000995],[-72.5,7.5000000000000995],[-72.5,7.0000000000000995],[-73,7.0000000000000995]],[[-74,10.5000000000001],[-74,11.0000000000001],[-73.5,11.0000000000001],[-73.5,10.5000000000001],[-74,10.5000000000001]],[[-74,5.0000000000000995],[-74,5.5000000000000995],[-73.5,5.5000000000000995],[-73.5,5.0000000000000995],[-74,5.0000000000000995]],[[-73.5,5.5000000000000995],[-73.5,6.0000000000000995],[-73,6.0000000000000995],[-73,5.5000000000000995],[-73.5,5.5000000000000995]]]]} 
};

allClasses[1] = {
    "type": "Feature",
    "properties": {
        "popupContent": "This is one class of climate data",
        "style": {
            weight: 0,
            color: "#efefef",
            opacity: 1,
            fillColor: "#B0DE5C",
            fillOpacity: 0.8
        }
    },
    'geometry': { 'type': 'MultiPolygon',coordinates: [[[[-77,1.5000000000000995],[-77,2.0000000000000995],[-76.5,2.0000000000000995],[-76.5,1.5000000000000995],[-77,1.5000000000000995]],[[-76,3.5000000000000995],[-76,4.0000000000000995],[-75.5,4.0000000000000995],[-75.5,3.5000000000000995],[-76,3.5000000000000995]],[[-76.5,2.0000000000000995],[-76.5,2.5000000000000995],[-76.5,3.0000000000000995],[-76,3.0000000000000995],[-76,2.5000000000000995],[-76,2.0000000000000995],[-76.5,2.0000000000000995]],[[-74.5,3.5000000000000995],[-74.5,4.0000000000000995],[-74,4.0000000000000995],[-74,3.5000000000000995],[-74.5,3.5000000000000995]],[[-74,4.5000000000000995],[-74,5.0000000000000995],[-73.5,5.0000000000000995],[-73.5,4.5000000000000995],[-74,4.5000000000000995]],[[-73,5.5000000000000995],[-73,6.0000000000000995],[-72.5,6.0000000000000995],[-72.5,5.5000000000000995],[-73,5.5000000000000995]],[[-73,6.5000000000000995],[-73,7.0000000000000995],[-72.5,7.0000000000000995],[-72,7.0000000000000995],[-72,6.5000000000000995],[-72.5,6.5000000000000995],[-73,6.5000000000000995]]]]} 
};


allClasses[2] = {
    "type": "Feature",
    "properties": {
        "popupContent": "This is one class of climate data",
        "style": {
            weight: 0,
            color: "#efefef",
            opacity: 1,
            fillColor: "#B0DE5C",
            fillOpacity: 0.5
        }
    },
    'geometry': { 'type': 'MultiPolygon',coordinates: [[[[-77,2.0000000000000995],[-77,2.5000000000000995],[-76.5,2.5000000000000995],[-76.5,2.0000000000000995],[-77,2.0000000000000995]],[[-77.5,0.5000000000000995],[-77.5,1.0000000000000995],[-78,1.0000000000000995],[-78,1.5000000000000995],[-77.5,1.5000000000000995],[-77,1.5000000000000995],[-77,1.0000000000000995],[-77,0.5000000000000995],[-77.5,0.5000000000000995]],[[-76,3.0000000000000995],[-76,3.5000000000000995],[-75.5,3.5000000000000995],[-75.5,3.0000000000000995],[-76,3.0000000000000995]],[[-75.5,5.0000000000000995],[-75.5,5.5000000000000995],[-75,5.5000000000000995],[-75,5.0000000000000995],[-75.5,5.0000000000000995]],[[-73.5,5.0000000000000995],[-73.5,5.5000000000000995],[-73,5.5000000000000995],[-73,5.0000000000000995],[-73.5,5.0000000000000995]],[[-74.5,4.5000000000000995],[-74.5,5.0000000000000995],[-74,5.0000000000000995],[-74,4.5000000000000995],[-74.5,4.5000000000000995]],[[-74,5.5000000000000995],[-74,6.0000000000000995],[-73.5,6.0000000000000995],[-73.5,5.5000000000000995],[-74,5.5000000000000995]],[[-72.5,6.0000000000000995],[-72.5,6.5000000000000995],[-72,6.5000000000000995],[-72,6.0000000000000995],[-72.5,6.0000000000000995]],[[-76,4.0000000000000995],[-76,4.5000000000000995],[-75.5,4.5000000000000995],[-75.5,4.0000000000000995],[-76,4.0000000000000995]]]]}
};

allClasses[3] = {
    "type": "Feature",
    "properties": {
        "popupContent": "This is one class of climate data",
        "style": {
            weight: 2,
            color: "#efefef",
            opacity: 1,
            fillColor: "#B0DE5C",
            fillOpacity: 0.8
        }
    },
    'geometry': { 'type': 'MultiPolygon',coordinates: [[[[-78.5,0.5000000000000995],[-78.5,1.0000000000000995],[-78,1.0000000000000995],[-78,0.5000000000000995],[-78.5,0.5000000000000995]],[[-77,1.0000000000000995],[-77,1.5000000000000995],[-76.5,1.5000000000000995],[-76.5,1.0000000000000995],[-77,1.0000000000000995]],[[-76.5,1.5000000000000995],[-76.5,2.0000000000000995],[-76,2.0000000000000995],[-76,1.5000000000000995],[-76.5,1.5000000000000995]],[[-73,7.5000000000000995],[-73,8.0000000000001],[-72.5,8.0000000000001],[-72.5,7.5000000000000995],[-73,7.5000000000000995]],[[-73,10.0000000000001],[-73,10.5000000000001],[-72.5,10.5000000000001],[-72.5,10.0000000000001],[-73,10.0000000000001]],[[-76,6.5000000000000995],[-76,7.0000000000000995],[-75.5,7.0000000000000995],[-75.5,6.5000000000000995],[-76,6.5000000000000995]],[[-75.5,5.5000000000000995],[-75.5,6.0000000000000995],[-75.5,6.5000000000000995],[-75,6.5000000000000995],[-75,6.0000000000000995],[-75,5.5000000000000995],[-75.5,5.5000000000000995]]]]}
};

allClasses[4] = {
    "type": "Feature",
    "properties": {
        "popupContent": "This is one class of climate data",
        "style": {
            weight: 2,
            color: "#999",
            opacity: 1,
            fillColor: "#B0DE5C",
            fillOpacity: 0.8
        }
    },
    'geometry': { 'type': 'MultiPolygon',coordinates: [[[[-77.5,2.0000000000000995],[-77.5,2.5000000000000995],[-77,2.5000000000000995],[-77,2.0000000000000995],[-77.5,2.0000000000000995]],[[-77,2.5000000000000995],[-77,3.0000000000000995],[-76.5,3.0000000000000995],[-76.5,2.5000000000000995],[-77,2.5000000000000995]],[[-74,4.0000000000000995],[-74,4.5000000000000995],[-73.5,4.5000000000000995],[-73.5,4.0000000000000995],[-74,4.0000000000000995]],[[-73.5,10.5000000000001],[-73.5,11.0000000000001],[-73,11.0000000000001],[-73,10.5000000000001],[-73.5,10.5000000000001]],[[-72.5,7.0000000000000995],[-72.5,7.5000000000000995],[-72.5,8.0000000000001],[-72,8.0000000000001],[-72,7.5000000000000995],[-72,7.0000000000000995],[-72.5,7.0000000000000995]],[[-73.5,6.0000000000000995],[-73.5,6.5000000000000995],[-73.5,7.0000000000000995],[-73,7.0000000000000995],[-73,6.5000000000000995],[-73,6.0000000000000995],[-73.5,6.0000000000000995]],[[-74.5,5.0000000000000995],[-74.5,5.5000000000000995],[-74,5.5000000000000995],[-74,5.0000000000000995],[-74.5,5.0000000000000995]],[[-76.5,3.0000000000000995],[-76.5,3.5000000000000995],[-76.5,4.0000000000000995],[-76,4.0000000000000995],[-76,3.5000000000000995],[-76,3.0000000000000995],[-76.5,3.0000000000000995]],[[-76.5,4.5000000000000995],[-76.5,5.0000000000000995],[-76,5.0000000000000995],[-75.5,5.0000000000000995],[-75.5,4.5000000000000995],[-76,4.5000000000000995],[-76.5,4.5000000000000995]],[[-75.5,6.5000000000000995],[-75.5,7.0000000000000995],[-75,7.0000000000000995],[-75,6.5000000000000995],[-75.5,6.5000000000000995]],[[-76.5,6.0000000000000995],[-76.5,6.5000000000000995],[-76.5,7.0000000000000995],[-76,7.0000000000000995],[-76,6.5000000000000995],[-75.5,6.5000000000000995],[-75.5,6.0000000000000995],[-76,6.0000000000000995],[-76.5,6.0000000000000995]],[[-76,7.0000000000000995],[-76,7.5000000000000995],[-75.5,7.5000000000000995],[-75.5,7.0000000000000995],[-76,7.0000000000000995]],[[-75.5,2.0000000000000995],[-75.5,2.5000000000000995],[-75,2.5000000000000995],[-75,2.0000000000000995],[-75.5,2.0000000000000995]],[[-76,2.5000000000000995],[-76,3.0000000000000995],[-75.5,3.0000000000000995],[-75.5,2.5000000000000995],[-76,2.5000000000000995]],[[-75,2.5000000000000995],[-75,3.0000000000000995],[-74.5,3.0000000000000995],[-74.5,2.5000000000000995],[-75,2.5000000000000995]]]]}
};

allClasses[5] = {
    "type": "Feature",
    "properties": {
        "popupContent": "This is one class of climate data",
        "style": {
            weight: 2,
            color: "#999",
            opacity: 1,
            fillColor: "#B0DE5C",
            fillOpacity: 0.8
        }
    },
    'geometry': { 'type': 'MultiPolygon',coordinates: [[[[-74,7.0000000000000995],[-74,7.5000000000000995],[-74,8.0000000000001],[-73.5,8.0000000000001],[-73.5,7.5000000000000995],[-73.5,7.0000000000000995],[-74,7.0000000000000995]],[[-73.5,9.5000000000001],[-73.5,9.0000000000001],[-73.5,8.5000000000001],[-74,8.5000000000001],[-74.5,8.5000000000001],[-75,8.5000000000001],[-75,9.0000000000001],[-75,9.5000000000001],[-75,10.0000000000001],[-74.5,10.0000000000001],[-74,10.0000000000001],[-73.5,10.0000000000001],[-73.5,9.5000000000001]],[[-68.5,4.0000000000000995],[-68.5,4.5000000000000995],[-69,4.5000000000000995],[-69,5.0000000000000995],[-69.5,5.0000000000000995],[-69.5,5.5000000000000995],[-69,5.5000000000000995],[-69,6.0000000000000995],[-69,6.5000000000000995],[-68.5,6.5000000000000995],[-68,6.5000000000000995],[-67.5,6.5000000000000995],[-67,6.5000000000000995],[-67,6.0000000000000995],[-67.5,6.0000000000000995],[-67.5,5.5000000000000995],[-67.5,5.0000000000000995],[-67.5,4.5000000000000995],[-67.5,4.0000000000000995],[-68,4.0000000000000995],[-68.5,4.0000000000000995]]]]}
};


//
allClasses[6] = {
    "type": "Feature",
    "properties": {
        "popupContent": "This is one class of climate data",
        "style": {
            weight: 2,
            color: "#999",
            opacity: 1,
            fillColor: "#B0DE5C",
            fillOpacity: 0.8
        }
    },
    'geometry': { 'type': 'MultiPolygon',coordinates: [[[[-77.5,1.5000000000000995],[-77.5,2.0000000000000995],[-77,2.0000000000000995],[-77,1.5000000000000995],[-77.5,1.5000000000000995]],[[-77,3.0000000000000995],[-77,3.5000000000000995],[-76.5,3.5000000000000995],[-76.5,3.0000000000000995],[-77,3.0000000000000995]],[[-72.5,5.5000000000000995],[-72.5,6.0000000000000995],[-72,6.0000000000000995],[-72,5.5000000000000995],[-72.5,5.5000000000000995]],[[-73.5,4.5000000000000995],[-73.5,5.0000000000000995],[-73,5.0000000000000995],[-73,4.5000000000000995],[-73.5,4.5000000000000995]],[[-73,5.0000000000000995],[-73,5.5000000000000995],[-72.5,5.5000000000000995],[-72.5,5.0000000000000995],[-73,5.0000000000000995]],[[-73.5,7.5000000000000995],[-73.5,8.0000000000001],[-73.5,8.5000000000001],[-73,8.5000000000001],[-73,8.0000000000001],[-73,7.5000000000000995],[-73.5,7.5000000000000995]],[[-75,3.0000000000000995],[-75,3.5000000000000995],[-75,4.0000000000000995],[-74.5,4.0000000000000995],[-74.5,3.5000000000000995],[-74.5,3.0000000000000995],[-75,3.0000000000000995]],[[-76.5,4.0000000000000995],[-76.5,4.5000000000000995],[-76,4.5000000000000995],[-76,4.0000000000000995],[-76.5,4.0000000000000995]],[[-76.5,7.0000000000000995],[-76.5,7.5000000000000995],[-76,7.5000000000000995],[-76,7.0000000000000995],[-76.5,7.0000000000000995]],[[-76.5,5.0000000000000995],[-76.5,5.5000000000000995],[-76.5,6.0000000000000995],[-76,6.0000000000000995],[-75.5,6.0000000000000995],[-75.5,5.5000000000000995],[-75.5,5.0000000000000995],[-76,5.0000000000000995],[-76.5,5.0000000000000995]],[[-76,1.5000000000000995],[-76,2.0000000000000995],[-76,2.5000000000000995],[-75.5,2.5000000000000995],[-75.5,2.0000000000000995],[-75.5,1.5000000000000995],[-76,1.5000000000000995]],[[-75.5,4.0000000000000995],[-75.5,4.5000000000000995],[-75,4.5000000000000995],[-75,4.0000000000000995],[-75.5,4.0000000000000995]]]]}
};

allClasses[7] = {
    "type": "Feature",
    "properties": {
        "popupContent": "This is one class of climate data",
        "style": {
            weight: 2,
            color: "#999",
            opacity: 1,
            fillColor: "#B0DE5C",
            fillOpacity: 0.8
        }
    },
    'geometry': { 'type': 'MultiPolygon',coordinates: [[[[-77.5,0.0000000000000995],[-77.5,0.5000000000000995],[-77,0.5000000000000995],[-77,0.0000000000000995],[-77.5,0.0000000000000995]],[[-76.5,1.0000000000000995],[-76.5,1.5000000000000995],[-76,1.5000000000000995],[-76,1.0000000000000995],[-76.5,1.0000000000000995]],[[-78.5,1.0000000000000995],[-78.5,1.5000000000000995],[-78,1.5000000000000995],[-78,1.0000000000000995],[-78.5,1.0000000000000995]],[[-78,1.5000000000000995],[-78,2.0000000000000995],[-77.5,2.0000000000000995],[-77.5,1.5000000000000995],[-78,1.5000000000000995]],[[-77,3.5000000000000995],[-77,4.0000000000000995],[-76.5,4.0000000000000995],[-76.5,3.5000000000000995],[-77,3.5000000000000995]],[[-76,7.5000000000000995],[-76,8.0000000000001],[-75.5,8.0000000000001],[-75.5,7.5000000000000995],[-76,7.5000000000000995]],[[-74.5,3.0000000000000995],[-74.5,3.5000000000000995],[-74,3.5000000000000995],[-74,3.0000000000000995],[-74.5,3.0000000000000995]],[[-74.5,7.5000000000000995],[-74.5,8.0000000000001],[-74,8.0000000000001],[-74,7.5000000000000995],[-74.5,7.5000000000000995]],[[-74,6.0000000000000995],[-74,6.5000000000000995],[-73.5,6.5000000000000995],[-73.5,6.0000000000000995],[-74,6.0000000000000995]],[[-73.5,8.5000000000001],[-73.5,9.0000000000001],[-73.5,9.5000000000001],[-73,9.5000000000001],[-73,9.0000000000001],[-73,8.5000000000001],[-73.5,8.5000000000001]],[[-73,10.5000000000001],[-73,11.0000000000001],[-72.5,11.0000000000001],[-72.5,10.5000000000001],[-73,10.5000000000001]],[[-74,11.0000000000001],[-74,11.5000000000001],[-73.5,11.5000000000001],[-73.5,11.0000000000001],[-74,11.0000000000001]],[[-75.5,2.5000000000000995],[-75.5,3.0000000000000995],[-75,3.0000000000000995],[-75,2.5000000000000995],[-75.5,2.5000000000000995]],[[-75.5,7.0000000000000995],[-75.5,7.5000000000000995],[-75,7.5000000000000995],[-75,7.0000000000000995],[-75.5,7.0000000000000995]],[[-75,6.5000000000000995],[-75,7.0000000000000995],[-74.5,7.0000000000000995],[-74.5,6.5000000000000995],[-75,6.5000000000000995]]]]}
};




allClasses[8] = {
    "type": "Feature",
    "properties": {
        "popupContent": "This is one class of climate data",
        "style": {
            weight: 2,
            color: "#999",
            opacity: 1,
            fillColor: "#B0DE5C",
            fillOpacity: 0.8
        }
    },
    'geometry': { 'type': 'MultiPolygon',coordinates: [[[[-76,1.0000000000000995],[-76,1.5000000000000995],[-75.5,1.5000000000000995],[-75.5,1.0000000000000995],[-76,1.0000000000000995]],[[-79,1.0000000000000995],[-79,1.5000000000000995],[-78.5,1.5000000000000995],[-78.5,1.0000000000000995],[-79,1.0000000000000995]],[[-79,2.0000000000000995],[-79,2.5000000000000995],[-78.5,2.5000000000000995],[-78.5,2.0000000000000995],[-79,2.0000000000000995]],[[-77,0.5000000000000995],[-77,1.0000000000000995],[-76.5,1.0000000000000995],[-76.5,0.5000000000000995],[-77,0.5000000000000995]],[[-77.5,2.5000000000000995],[-77.5,3.0000000000000995],[-77,3.0000000000000995],[-77,2.5000000000000995],[-77.5,2.5000000000000995]],[[-77.5,4.0000000000000995],[-77.5,4.5000000000000995],[-77,4.5000000000000995],[-76.5,4.5000000000000995],[-76.5,4.0000000000000995],[-77,4.0000000000000995],[-77.5,4.0000000000000995]],[[-74,2.0000000000000995],[-74,2.5000000000000995],[-74,3.0000000000000995],[-73.5,3.0000000000000995],[-73.5,2.5000000000000995],[-73.5,2.0000000000000995],[-74,2.0000000000000995]],[[-71.5,1.0000000000000995],[-71.5,1.5000000000000995],[-71,1.5000000000000995],[-71,1.0000000000000995],[-71.5,1.0000000000000995]],[[-72,1.5000000000000995],[-72,2.0000000000000995],[-71.5,2.0000000000000995],[-71.5,1.5000000000000995],[-72,1.5000000000000995]],[[-73.5,1.0000000000000995],[-73.5,1.5000000000000995],[-73,1.5000000000000995],[-72.5,1.5000000000000995],[-72.5,1.0000000000000995],[-73,1.0000000000000995],[-73.5,1.0000000000000995]],[[-74,3.5000000000000995],[-74,4.0000000000000995],[-73.5,4.0000000000000995],[-73.5,3.5000000000000995],[-74,3.5000000000000995]],[[-75.5,1.5000000000000995],[-75.5,2.0000000000000995],[-75,2.0000000000000995],[-75,1.5000000000000995],[-75.5,1.5000000000000995]],[[-75,5.0000000000000995],[-75,5.5000000000000995],[-74.5,5.5000000000000995],[-74.5,5.0000000000000995],[-74.5,4.5000000000000995],[-75,4.5000000000000995],[-75,5.0000000000000995]],[[-75.5,3.0000000000000995],[-75.5,3.5000000000000995],[-75.5,4.0000000000000995],[-75,4.0000000000000995],[-75,3.5000000000000995],[-75,3.0000000000000995],[-75.5,3.0000000000000995]],[[-75,2.0000000000000995],[-75,2.5000000000000995],[-74.5,2.5000000000000995],[-74.5,2.0000000000000995],[-75,2.0000000000000995]],[[-74.5,11.0000000000001],[-74.5,11.5000000000001],[-74,11.5000000000001],[-74,11.0000000000001],[-74.5,11.0000000000001]],[[-73.5,11.0000000000001],[-73.5,11.5000000000001],[-73,11.5000000000001],[-72.5,11.5000000000001],[-72,11.5000000000001],[-72,11.0000000000001],[-72.5,11.0000000000001],[-73,11.0000000000001],[-73.5,11.0000000000001]],[[-78,7.0000000000000995],[-78,7.5000000000000995],[-78,8.0000000000001],[-77.5,8.0000000000001],[-77.5,8.5000000000001],[-77.5,9.0000000000001],[-77,9.0000000000001],[-77,8.5000000000001],[-77,8.0000000000001],[-77,7.5000000000000995],[-77.5,7.5000000000000995],[-77.5,7.0000000000000995],[-78,7.0000000000000995]],[[-77.5,5.0000000000000995],[-77.5,5.5000000000000995],[-77.5,6.0000000000000995],[-77.5,6.5000000000000995],[-77.5,7.0000000000000995],[-77,7.0000000000000995],[-76.5,7.0000000000000995],[-76.5,6.5000000000000995],[-76.5,6.0000000000000995],[-77,6.0000000000000995],[-77,5.5000000000000995],[-77,5.0000000000000995],[-77.5,5.0000000000000995]],[[-73,8.0000000000001],[-73,8.5000000000001],[-72.5,8.5000000000001],[-72,8.5000000000001],[-72,8.0000000000001],[-72.5,8.0000000000001],[-73,8.0000000000001]],[[-73.5,9.5000000000001],[-73.5,10.0000000000001],[-74,10.0000000000001],[-74,10.5000000000001],[-73.5,10.5000000000001],[-73,10.5000000000001],[-73,10.0000000000001],[-72.5,10.0000000000001],[-72.5,9.5000000000001],[-73,9.5000000000001],[-73.5,9.5000000000001]],[[-72,6.5000000000000995],[-72,7.0000000000000995],[-71.5,7.0000000000000995],[-71.5,6.5000000000000995],[-72,6.5000000000000995]],[[-74.5,5.5000000000000995],[-74.5,6.0000000000000995],[-74,6.0000000000000995],[-74,5.5000000000000995],[-74.5,5.5000000000000995]],[[-75,7.0000000000000995],[-75,7.5000000000000995],[-74.5,7.5000000000000995],[-74,7.5000000000000995],[-74,7.0000000000000995],[-74.5,7.0000000000000995],[-75,7.0000000000000995]],[[-75,6.0000000000000995],[-75,6.5000000000000995],[-74.5,6.5000000000000995],[-74.5,6.0000000000000995],[-75,6.0000000000000995]],[[-76.5,7.5000000000000995],[-76.5,8.0000000000001],[-76,8.0000000000001],[-76,7.5000000000000995],[-76.5,7.5000000000000995]],[[-73.5,7.0000000000000995],[-73.5,7.5000000000000995],[-73,7.5000000000000995],[-73,7.0000000000000995],[-73.5,7.0000000000000995]]]]}
};

allClasses[9] = {
    "type": "Feature",
    "properties": {
        "popupContent": "This is one class of climate data",
        "style": {
            weight: 2,
            color: "#999",
            opacity: 1,
            fillColor: "#B0DE5C",
            fillOpacity: 0.8
        }
    },
    'geometry': { 'type': 'MultiPolygon',coordinates: [[[[-67.5,5.5000000000000995],[-67.5,6.0000000000000995],[-67,6.0000000000000995],[-67,5.5000000000000995],[-67.5,5.5000000000000995]],[[-79,1.5000000000000995],[-79,2.0000000000000995],[-78.5,2.0000000000000995],[-78.5,2.5000000000000995],[-78,2.5000000000000995],[-78,3.0000000000000995],[-77.5,3.0000000000000995],[-77.5,2.5000000000000995],[-77.5,2.0000000000000995],[-78,2.0000000000000995],[-78,1.5000000000000995],[-78.5,1.5000000000000995],[-79,1.5000000000000995]],[[-82,12.5000000000001],[-82,13.0000000000001],[-81.5,13.0000000000001],[-81.5,12.5000000000001],[-82,12.5000000000001]],[[-81.5,13.0000000000001],[-81.5,13.5000000000001],[-81,13.5000000000001],[-81,13.0000000000001],[-81.5,13.0000000000001]],[[-77.5,3.0000000000000995],[-77.5,3.5000000000000995],[-77.5,4.0000000000000995],[-77,4.0000000000000995],[-77,3.5000000000000995],[-77,3.0000000000000995],[-77.5,3.0000000000000995]],[[-77.5,4.5000000000000995],[-77.5,5.0000000000000995],[-77,5.0000000000000995],[-77,5.5000000000000995],[-77,6.0000000000000995],[-76.5,6.0000000000000995],[-76.5,5.5000000000000995],[-76.5,5.0000000000000995],[-76.5,4.5000000000000995],[-77,4.5000000000000995],[-77.5,4.5000000000000995]],[[-78,6.5000000000000995],[-78,7.0000000000000995],[-77.5,7.0000000000000995],[-77.5,6.5000000000000995],[-78,6.5000000000000995]],[[-77.5,7.0000000000000995],[-77.5,7.5000000000000995],[-77,7.5000000000000995],[-77,8.0000000000001],[-77,8.5000000000001],[-77,9.0000000000001],[-76.5,9.0000000000001],[-76.5,9.5000000000001],[-76,9.5000000000001],[-76,10.0000000000001],[-76,10.5000000000001],[-75.5,10.5000000000001],[-75.5,11.0000000000001],[-75,11.0000000000001],[-74.5,11.0000000000001],[-74,11.0000000000001],[-74,10.5000000000001],[-74,10.0000000000001],[-74.5,10.0000000000001],[-75,10.0000000000001],[-75,9.5000000000001],[-75,9.0000000000001],[-75,8.5000000000001],[-74.5,8.5000000000001],[-74,8.5000000000001],[-73.5,8.5000000000001],[-73.5,8.0000000000001],[-74,8.0000000000001],[-74.5,8.0000000000001],[-74.5,7.5000000000000995],[-75,7.5000000000000995],[-75.5,7.5000000000000995],[-75.5,8.0000000000001],[-76,8.0000000000001],[-76.5,8.0000000000001],[-76.5,7.5000000000000995],[-76.5,7.0000000000000995],[-77,7.0000000000000995],[-77.5,7.0000000000000995]],[[-78.5,8.0000000000001],[-78.5,8.5000000000001],[-78,8.5000000000001],[-78,8.0000000000001],[-78.5,8.0000000000001]],[[-75,4.0000000000000995],[-75,4.5000000000000995],[-74.5,4.5000000000000995],[-74.5,4.0000000000000995],[-75,4.0000000000000995]],[[-75,5.5000000000000995],[-75,6.0000000000000995],[-74.5,6.0000000000000995],[-74.5,5.5000000000000995],[-75,5.5000000000000995]],[[-74.5,6.0000000000000995],[-74.5,6.5000000000000995],[-74.5,7.0000000000000995],[-74,7.0000000000000995],[-73.5,7.0000000000000995],[-73.5,6.5000000000000995],[-74,6.5000000000000995],[-74,6.0000000000000995],[-74.5,6.0000000000000995]],[[-72.5,5.0000000000000995],[-72.5,5.5000000000000995],[-72,5.5000000000000995],[-72,6.0000000000000995],[-72,6.5000000000000995],[-71.5,6.5000000000000995],[-71.5,7.0000000000000995],[-72,7.0000000000000995],[-72,7.5000000000000995],[-71.5,7.5000000000000995],[-71,7.5000000000000995],[-70.5,7.5000000000000995],[-70,7.5000000000000995],[-70,7.0000000000000995],[-69.5,7.0000000000000995],[-69.5,6.5000000000000995],[-69,6.5000000000000995],[-69,6.0000000000000995],[-69,5.5000000000000995],[-69.5,5.5000000000000995],[-69.5,5.0000000000000995],[-69,5.0000000000000995],[-69,4.5000000000000995],[-68.5,4.5000000000000995],[-68.5,4.0000000000000995],[-68,4.0000000000000995],[-67.5,4.0000000000000995],[-67,4.0000000000000995],[-67,3.5000000000000995],[-67,3.0000000000000995],[-67,2.5000000000000995],[-67,2.0000000000000995],[-66.5,2.0000000000000995],[-66.5,1.5000000000000995],[-66.5,1.0000000000000995],[-67,1.0000000000000995],[-67.5,1.0000000000000995],[-67.5,1.5000000000000995],[-68,1.5000000000000995],[-68.5,1.5000000000000995],[-69,1.5000000000000995],[-69,1.0000000000000995],[-69,0.5000000000000995],[-69.5,0.5000000000000995],[-70,0.5000000000000995],[-70,0.0000000000000995],[-69.5,0.0000000000000995],[-69.5,-0.4999999999999005],[-69,-0.4999999999999005],[-69,-0.9999999999999005],[-69,-1.4999999999999005],[-69,-1.9999999999999005],[-69.5,-1.9999999999999005],[-69.5,-2.4999999999999005],[-69.5,-2.9999999999999005],[-69.5,-3.4999999999999005],[-69.5,-3.9999999999999005],[-69.5,-4.4999999999999005],[-70,-4.4999999999999005],[-70.5,-4.4999999999999005],[-70.5,-3.9999999999999005],[-71,-3.9999999999999005],[-71,-3.4999999999999005],[-71,-2.9999999999999005],[-70.5,-2.9999999999999005],[-70.5,-2.4999999999999005],[-71,-2.4999999999999005],[-71.5,-2.4999999999999005],[-72,-2.4999999999999005],[-72.5,-2.4999999999999005],[-73,-2.4999999999999005],[-73.5,-2.4999999999999005],[-73.5,-1.9999999999999005],[-74,-1.9999999999999005],[-74,-1.4999999999999005],[-74.5,-1.4999999999999005],[-74.5,-0.9999999999999005],[-74.5,-0.4999999999999005],[-75,-0.4999999999999005],[-75.5,-0.4999999999999005],[-76,-0.4999999999999005],[-76,0.0000000000000995],[-76.5,0.0000000000000995],[-77,0.0000000000000995],[-77,0.5000000000000995],[-76.5,0.5000000000000995],[-76.5,1.0000000000000995],[-76,1.0000000000000995],[-75.5,1.0000000000000995],[-75.5,1.5000000000000995],[-75,1.5000000000000995],[-75,2.0000000000000995],[-74.5,2.0000000000000995],[-74.5,2.5000000000000995],[-74.5,3.0000000000000995],[-74,3.0000000000000995],[-74,3.5000000000000995],[-73.5,3.5000000000000995],[-73.5,4.0000000000000995],[-73.5,4.5000000000000995],[-73,4.5000000000000995],[-73,5.0000000000000995],[-72.5,5.0000000000000995]],[[-74,3.0000000000000995],[-74,2.5000000000000995],[-74,2.0000000000000995],[-73.5,2.0000000000000995],[-73.5,2.5000000000000995],[-73.5,3.0000000000000995],[-74,3.0000000000000995]],[[-72,2.0000000000000995],[-72,1.5000000000000995],[-71.5,1.5000000000000995],[-71.5,2.0000000000000995],[-72,2.0000000000000995]],[[-71.5,1.5000000000000995],[-71.5,1.0000000000000995],[-71,1.0000000000000995],[-71,1.5000000000000995],[-71.5,1.5000000000000995]],[[-72.5,1.0000000000000995],[-72.5,1.5000000000000995],[-73,1.5000000000000995],[-73.5,1.5000000000000995],[-73.5,1.0000000000000995],[-73,1.0000000000000995],[-72.5,1.0000000000000995]],[[-73,8.5000000000001],[-73,9.0000000000001],[-73,9.5000000000001],[-72.5,9.5000000000001],[-72.5,9.0000000000001],[-72.5,8.5000000000001],[-73,8.5000000000001]],[[-73,11.5000000000001],[-73,12.0000000000001],[-72.5,12.0000000000001],[-72.5,12.5000000000001],[-72,12.5000000000001],[-71.5,12.5000000000001],[-71,12.5000000000001],[-71,12.0000000000001],[-71,11.5000000000001],[-71.5,11.5000000000001],[-72,11.5000000000001],[-72.5,11.5000000000001],[-73,11.5000000000001]]]]} 
};
//
