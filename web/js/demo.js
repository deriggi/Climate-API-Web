/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var countryCodes = {"VU":"VUT","LV":"LVA","LU":"LUX","LT":"LTU","EC":"ECU","VN":"VNM","LY":"LBY","LS":"LSO","LR":"LBR","DZ":"DZA","VE":"VEN","DM":"DMA","MG":"MDG","DO":"DOM","MK":"MKD","ML":"MLI","DE":"DEU","UZ":"UZB","UY":"URY","MD":"MDA","DK":"DNK","MA":"MAR","DJ":"DJI","MV":"MDV","MU":"MUS","US":"USA","MX":"MEX","MW":"MWI","MZ":"MOZ","MY":"MYS","MN":"MNG","MM":"MMR","MR":"MRT","UG":"UGA","MT":"MLT","UA":"UKR","NG":"NGA","NI":"NIC","ET":"ETH","ES":"ESP","ER":"ERI","NL":"NLD","EG":"EGY","TZ":"TZA","EE":"EST","NA":"NAM","TT":"TTO","TW":"TWN","NE":"NER","GD":"GRD","GE":"GEO","NZ":"NZL","GA":"GAB","GB":"GBR","NU":"NIU","NR":"NRU","NP":"NPL","NO":"NOR","OM":"OMN","FR":"FRA","FJ":"FJI","FI":"FIN","WS":"WSM","GY":"GUY","GW":"GNB","PL":"POL","GT":"GTM","PH":"PHL","GR":"GRC","GQ":"GNQ","PK":"PAK","GN":"GIN","PE":"PER","GM":"GMB","PG":"PNG","PA":"PAN","GH":"GHA","ZA":"ZAF","HN":"HND","HR":"HRV","RO":"ROU","HT":"HTI","HU":"HUN","ZM":"ZMB","ZW":"ZWE","ID":"IDN","IE":"IRL","AT":"AUT","AR":"ARG","IL":"ISR","QA":"QAT","IN":"IND","AU":"AUS","IQ":"IRQ","IR":"IRN","YE":"YEM","AZ":"AZE","IS":"ISL","IT":"ITA","PT":"PRT","PW":"PLW","AG":"ATG","AE":"ARE","AF":"AFG","AL":"ALB","AO":"AGO","PY":"PRY","AM":"ARM","BW":"BWA","TG":"TGO","JP":"JPN","BY":"BLR","TD":"TCD","JO":"JOR","BS":"BHS","TJ":"TJK","JM":"JAM","BR":"BRA","TH":"THA","BT":"BTN","TO":"TON","TN":"TUN","TM":"TKM","CA":"CAN","TR":"TUR","BZ":"BLZ","BF":"BFA","SV":"SLV","BG":"BGR","BH":"BHR","BI":"BDI","SY":"SYR","BB":"BRB","SZ":"SWZ","BD":"BGD","BE":"BEL","BN":"BRN","KI":"KIR","BO":"BOL","KH":"KHM","KG":"KGZ","BJ":"BEN","KE":"KEN","SD":"SDN","SC":"SYC","CY":"CYP","KP":"PRK","SE":"SWE","KR":"KOR","CV":"CPV","SG":"SGP","CU":"CUB","KM":"COM","SI":"SVN","CS":"SCG","KN":"KNA","SL":"SLE","KW":"KWT","SK":"SVK","SN":"SEN","KZ":"KAZ","SO":"SOM","SR":"SUR","CI":"CIV","LA":"LAO","CG":"COG","CH":"CHE","LB":"LBN","RU":"RUS","RW":"RWA","CF":"CAF","CD":"COD","CR":"CRI","CO":"COL","LK":"LKA","CM":"CMR","CN":"CHN","SA":"SAU","CK":"COK","SB":"SLB","CL":"CHL"};
var map;
var brooklyn = new google.maps.LatLng(10, 19);
var geocoder;
var MY_MAPTYPE_ID = 'hiphop';

$(document).ready(function() {
    loadGoogleMap();
    $('.navbutton').button();
    var paper = Raphael(document.getElementById("country1"),500,500);
    var country = paper.path('M 159.44866 -313.05974 l 14.48329 2.29169 17.92223 8.0972 5.9 -2.63887 3.76663 -5.03334 -2.3083 -7.15279 1.67222 -4.35832 4.89166 -3.62499 10.50556 -3.78612 5.18055 -0.11945 9.59167 3.02503 -0.09444 3.24163 2.2111 1.58056 17.22222 2.25833 1.08895 2.60033 -2.89364 2.71821 1.57121 6.3102 -3.17857 6.16199 3.01336 8.98686 0.00563 92.50159 -9.99998 0 0 4.91827 -80.01831 -39.42198 -18.08848 8.05887 -7.08731 -5.41648 -14.97454 -3.36088 -3.78385 -7.4156 -1.81726 0.64229 -5.23452 -3.54663 -5.42339 0.18161 -1.40298 -1.13677 -1.80537 -3.13865 -0.03728 -3.95113 -6.33079 -8.3612 5.14333 -4.51257 -1.29395 -7.06802 1.7865 -5.12972 -1.8261 -15.58271 -3.88848 -7.42553 4.9718 -1.82505 3.37107 -3.82843 0.61619 -3.50569 -1.50124 -4.03116 1.53659 -2.04402 12.36155 -7.1269 0.37264 -7.62608 7.76668 3.42497 10.45832 -0.74721 18.38888 5.23057 2.95278 7.61388 z')
    var basin = paper.path('M 406.1958 154.07114 l -9.20972 11.29832 -6.02429 4.47431 -9.98231 2.32958 -11.14062 7.84058 -1.37501 -1.25835 1.21167 1.89302 -4.92884 5.20159 -7.86038 -5.69128 0.70181 -2.27489 -1.5853 -4.62686 3.63378 -6.45257 -2.43993 -1.98876 1.51657 -4.53384 -2.73077 -2.44128 -4.42028 -1.17202 3.91755 -6.88326 0.42712 -2.6228 -1.54097 -3.25476 2.04435 -1.71606 5.43266 1.538 -2.06327 6.86772 2.39124 1.44528 0.47542 3.70758 2.14984 0.26878 2.48023 -2.17262 5.03385 0.35631 3.37705 -4.33584 9.72702 1.16624 1.83542 -5.13453 8.01708 -4.22457 1.19006 -1.88559 3.57814 -0.71482 0.1126 -1.25629 2.47469 -0.53515 0.09213 -1.61413 2.76256 -0.36607 1.0202 4.91456 -1.16943 1.45833 0.97511 1.17209 0.93076 -0.80687 -0.77258 3.31933 1.7635 -1.07654 0.23656 2.69477 -1.9721 1.70319 0.54986 2.1371 -1.89442 1.02083 1.53889 0.07639 z');
    var raphSet = paper.set();
    raphSet.push(country);
    raphSet.push(basin);
    var x = raphSet.getBBox().x;
    var y = raphSet.getBBox().y;
    //alert(x + ' ' + y);
    raphSet.translate((5-x), 5-y);
});

function getCountryFromClick(latlng){
    geocoder.geocode({
        'latLng': latlng
    }, function(results, status) {
        if (status == google.maps.GeocoderStatus.OK) {
            if (results[results.length-1]) {
                //alert(results[results.length-1].formatted_address + ' ' +  results[results.length-1].address_components[0].short_name);
                var shortName =results[results.length-1].address_components[0].short_name;
                alert(countryCodes[shortName]);
            }
        } else {
            alert("Geocoder failed due to: " + status);
        }
    });
}


function loadGoogleMap() {
    geocoder = new google.maps.Geocoder();
    var stylez = [
    {
        featureType: "road",
        elementType: "all",
        stylers: [
        {
            visibility: "off"
        }
        ]
    },{
        featureType: "all",
        elementType: "labels",
        stylers: [
        {
            visibility: "off"
        }
        ]
    },{
        featureType: "administrative.country",
        elementType: "geometry",
        stylers: [
        {
            visibility: "on"
        },
{
            hue: "#8800ff"
        },
{
            gamma: 1.85
        },
{
            lightness: 28
        }
        ]
    },{
        featureType: "poi",
        elementType: "all",
        stylers: [
        {
            visibility: "off"
        }
        ]
    },{
        featureType: "administrative.province",
        elementType: "geometry",
        stylers: [
        {
            visibility: "off"
        }
        ]
    },{
        featureType: "water",
        elementType: "all",
        stylers: [
        {
            hue: "#00aaff"
        },
{
            gamma: 0.98
        },
{
            lightness: 2
        },
{
            saturation: 39
        }
        ]
    }
    ];

    var mapOptions = {
        zoom: 2,
        center: brooklyn,
        mapTypeControlOptions: {
            mapTypeIds: [google.maps.MapTypeId.SATELLITE, MY_MAPTYPE_ID]
        },
        mapTypeId: MY_MAPTYPE_ID
    };

    map = new google.maps.Map(document.getElementById("map-container"),
        mapOptions);

    var styledMapOptions = {
        name: "blue-water"
    };

    var jayzMapType = new google.maps.StyledMapType(stylez, styledMapOptions);

    map.mapTypes.set(MY_MAPTYPE_ID, jayzMapType);
    google.maps.event.addListener(map, 'click', function(event) {
        getCountryFromClick(event.latLng);
    });

}
function loadCloudmade() {
    var cloudmade = new CM.Tiles.CloudMade.Web({
        key : '2f989835d5c740f2b043c5c61ec7a251',
        styleId : 36374
    });
    var map = new CM.Map('map-container', cloudmade);
    map.setCenter(new CM.LatLng(0.514, -0.137), 3);
}