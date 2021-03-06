/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

if(typeof String.prototype.trim !== 'function') {
    String.prototype.trim = function() {
        return this.replace(/^\s+|\s+$/g, '');
    }
}


var chosenIso = 'MOZ';
function drawRegionsMap() {
   
    
  

//    var geochart = new google.visualization.GeoChart(
//        document.getElementById('map_canvas'));
//    geochart.draw(dataTable, {
//        width: 556,
//        height: 347,
//        colors:['#9ECAE1','#C6DBEF']
//    });
    

//    google.visualization.events.addListener(geochart, 'select', function(
//        eventData) {
//        var selection = geochart.getSelection();
//
//        var message = ''
//        for (var i = 0; i < selection.length; i++) {
//            var item = selection[i];
//            if (item.row != null && item.column != null) {
//                message += '{row:' + item.row + ',column:' + item.column + '}';
//            } else if (item.row != null) {
//                message += '{row:' + item.row + '}';
//
//            } else if (item.column != null) {
//                message += '{column:' + item.column + '}';
//
//            }
//            if(item.row != null){
//                chosenIso = dataTable.getValue(item.row,1);
//
//                var countryName = dataTable.getValue(item.row,0)
//                $('#chosenIso').text(countryName)
//                $('#titlecountry').fadeOut('medium',function(){
//                    $(this).text(": "  + countryName)
//                    $(this).fadeIn('medium');
//
//                })
//                $('#configcountry').fadeOut('medium',function(){
//                    $(this).text(countryName)
//                    $(this).fadeIn('medium');
//                })
//
//
//
//            }
//
//
//        }
//
//    });
//
//
//}
}

function getCountries(){
    dataTable = new google.visualization.DataTable();
    dataTable.addColumn('string', 'Country');
    //    dataTable.addColumn('number', 'Population');
    dataTable.addColumn('string', 'ISO3');

    
    $.get('/climateweb/rest/country',
        function(data){
            dataTable.addRows(data.length*2);
            //            dataTable.setValue(0, 0, 'Germany');
            //            dataTable.setValue(0, 1, 1);
            var counter = 0;
            for(var i in data){
                if(i>=240)break;
                dataTable.setValue(counter, 0, data[i].name);
                //                dataTable.setValue(counter, 1, 1);
                dataTable.setValue(counter, 1, data[i].iso3);
                counter++;
            }
            drawRegionsMap();
        });

    getOldData("mavg","csiro_mk3_5", 'b1', 'tas', "2020", "2039", 'MOZ');
}

function getOldData(stattype,gcm, scenario, climvar, fyear,tyear,iso3){
    var requestPath = "/climateweb/rest/v1/country/"+stattype+"/"+gcm+"/"+scenario+"/"+climvar+"/"+fyear+"/"+tyear+"/"+iso3;
    //    $('#requestpath').text(requestPath);
    
    $.get(requestPath,function(data){
        $("#chartsrow").show();
        $("#tablerow").show();
        var tbody = $('#databody')
        var tbl = $('#tbl')
        $(tbody).children().remove();
        var headerRow = createHeadingFromArray(data[0].monthVals, data[0].percentile);
        $(tbl).children('thead').remove();
        $('#per10').children('img').remove();
        $('#per50').children('img').remove();
        $('#per90').children('img').remove();
        $(tbl).children('thead').remove();
        $(tbl).append(headerRow)
        //        $('#per90').append(createSingleChart(data));
        var maxval = getMax(data)
        for (var i= 0; i<data.length; i++){
            if(data[i].percentile){
                var per = data[i].percentile;
                $('#per'+per).append(createRadarChart(data[i], maxval));
            }
            
            if(data[i].monthVals){
                var dataArray = data[i].monthVals;
                var percentile;
                if(data[i].percentile){
                    percentile = data[i].percentile;
                }
                var row = createTableRowFromArray(dataArray, percentile);
                $(tbody).append(row);
            }
        }

    });
}



function createRadarChart(dataObject, maxval){

    var base = 'https://chart.googleapis.com/chart?cht=rs&chs=200x200&';
    var end = '&chxt=x&chxl=0:|Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec';
    //    var end = '&chxt=x&chxl=0:|0|1|2|3|4|5|6|7|8|9|10|11';
    //   'chd=t:10,20,30,40,50';
    var database = 'chd=t:';
    var dataArray = dataObject.monthVals;
    for(var i = 0; i < dataArray.length; i++){
        database += (dataArray[i].toFixed(3)/maxval)*100
        if(i < dataArray.length -1){
            database += ',';
        }

    }
    var element = document.createElement('img');

    var path  =  base + database + end;
    element.setAttribute('src', path);
    return element;

}

function sortNumbers(a,b)
{
    return a - b;
}

function getMax(dataObjects){

    for(var j = 0; j < dataObjects.length; j++){
        if(dataObjects[j].percentile == 90){
            var mvals = dataObjects[j].monthVals;
            var copy = mvals.slice(0);
            copy.sort(sortNumbers);
            var max = copy[copy.length-1];
            return max;
        }
    }
    return 300;
}

function createSingleChart(dataObjects){

    var base = 'https://chart.googleapis.com/chart?cht=rs&chs=200x200&';
    var end = '&chxt=x&chxl=0:|Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec';
    //   'chd=t:10,20,30,40,50';
    var database = 'chd=t:';
    for(var j = 0; j < dataObjects.length; j++){
        database += getObjectSeries(dataObjects[j]);
        if(j < dataObjects.length - 1){
            database+='|'
        }
    }
    var element = document.createElement('img');

    var path  =  base + database + end;
    element.setAttribute('src', path);
    return element;

}

function getObjectSeries(dataObject){
    var database = '';
    var dataArray = dataObject.monthVals;
    for(var i = 0; i < dataArray.length; i++){
        database += dataArray[i].toFixed(3)
        if(i < dataArray.length -1){
            database += ',';
        }

    }
    return database;

}

function createHeadingFromArray(dataArray, percentile){
    var thead = document.createElement("thead")
    thead.appendChild(document.createElement('tr'));
    if(percentile){
        var ptd = document.createElement("th")
        var ptext = document.createTextNode('percentile');
        ptd.appendChild(ptext);
        thead.appendChild(ptd);
    }
    var headerTable = null;
    if(dataArray.length == 12){
        headerTable = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec']
    }else{
        headerTable = ['Annual'];
    }
    for(var i=0; i  < headerTable.length; i++){
        var td = document.createElement("th")
        var text = document.createTextNode(headerTable[i]);
        td.appendChild(text);
        thead.appendChild(td);
    }
    return thead;
}


function createTableRowFromArray(dataArray, percentile){
    var tr = document.createElement("tr")

    if(percentile){
        var ptd = document.createElement("td")
        var ptext = document.createTextNode(percentile);
        ptd.appendChild(ptext);
        tr.appendChild(ptd);
    }
    for(var i=0; i  < dataArray.length; i++){
        var td = document.createElement("td")
        var text = document.createTextNode(dataArray[i].toFixed(3));
        td.appendChild(text);
        tr.appendChild(td);
    }
    return tr;
}

function downloadGcmData(stattype,gcm, scenario, climvar, fyear,tyear,iso3){
    var requestPath = "/climateweb/rest/v1/country/dl/"+stattype+"/"+gcm+"/"+scenario+"/"+climvar+"/"+fyear+"/"+tyear+"/"+iso3+".csv";
    window.location = requestPath;
//    $('#requestpath').text(requestPath);

//    $.get(requestPath,function(data){
//
//
//    });
}

$(document).ready(function(){

    assignYearsChangeHandler();
    $('#showdata').click(function(){
       
        var climvar = $('#varSelect').val();
        var scenario = getScenario();
        var years = $('#yearsSelect').val();
        var stattype = $('#stattype').val();
        
        var yearParts = years.split('-');
           
        getOldData(stattype,'ensemble', scenario, climvar, yearParts[0].trim(), yearParts[1].trim(), chosenIso);
    });
    $('#downloaddata').click(function(){

        var climvar = $('#varSelect').val();
        var scenario = getScenario();
        var years = $('#yearsSelect').val();
        var stattype = $('#stattype').val();

        var yearParts = years.split('-');

        downloadGcmData( stattype,'ensemble', scenario, climvar, yearParts[0].trim(), yearParts[1].trim(), chosenIso);
    })
    testCountriesSvg();
})

function getScenario(){
    var years = $('#yearsSelect').val();
    var yearParts = years.split('-');
    if(yearParts[0]<2000){
        return '20c3m'
    }
    return $('#scenarioSelect').val();
}

function assignYearsChangeHandler(){
    $('#yearsSelect').change(function(){
        var years = $('#yearsSelect').val();
        var yearParts = years.split('-');
        if(yearParts[0]<2000){
            $('#scenarioSelect').slideUp(200);
            $('#scenariolabel').hide();
            $('#meantype').attr('selected','selected');
            $('#anomtype').removeAttr('selected')

        }else{
            $('#scenarioSelect').slideDown(200);
            $('#scenariolabel').show();
        }
    });
}

function testSvg(){
    //    var testPath1 = "M 142.82 -54.3 l 1.92 5.65 -2.65 2.75 z M 124.6 -73.73 l 1.74 1.35 -3.92 -0.6 z M 139.19 -76.07 l 6.19 0.56 -8.52 0.16 z M 101.48 -76.45 l 12.41 0.61 -8.68 3.08 70.91 2.88 3.88 0.91 0 3.91 -5.55 0.38 4.65 2.4 -15.46 2.24 -0.29 3.85 -6.69 5.32 0.1 -6.86 8.87 -4.7 -23.38 3.33 -7.09 4.25 6.26 1.57 -1.24 4.84 -9.48 6.16 4.04 -5.97 -3.75 0.58 -7.38 -5.86 -5.74 4.02 -18.95 -2.62 -11.51 3.06 -18.46 -6.36 -7.94 1.5 0.37 3.16 -13.9 0.37 0.09 9.21 -10.95 -3.92 3.51 -4.47 -8.36 -2.51 -4.44 -5.48 4.23 -5.32 -1.47 -6.76 10.88 1.95 -9.13 0.56 5.56 3.35 6.75 -2.07 -0.86 -2.81 25.12 0.46 -1.82 -2.82 4.94 -1.86 2.09 4.44 -4.68 1.66 3.03 0.59 2.64 -2.55 4.4 1.2 -5.29 -1.6 1.08 -3.66 8.24 2.77 -2.55 -3.51 z M 53.2 -73.06 l 4.43 2.33 -6.21 -1 z M 102.97 -79.33 l 2.44 0.76 -6.07 0.55 z M -173.47 -64.39 l -6.53 -0.67 0 -3.91 10.29 2.85 z M 97.61 -80.17 l 2.33 1.2 -7.09 -0.6 z M 95.96 -81.22 l 2.01 0.51 -6.54 0.4 z";
    var testPath1 = " M 35.49 21.69 l -0.05 0.1 0.05 -0.26 z M 40.44 10.48 l 0.18 0.36 -0.23 0.48 0.09 1.19 0.17 0.25 -0.23 0.21 0.18 0 -0.07 0.54 0.12 0.51 -0.12 0.15 0.19 0.03 -0.09 0.28 0.17 -0.08 0.04 0.29 -0.14 0.38 -0.19 0.1 0.17 0.07 -0.11 0.24 -0.88 1.04 -0.6 0.45 -1.22 0.39 -0.89 0.62 -0.14 -0.13 0.1 0.23 -0.69 0.78 -0.15 -0.08 -1.21 1.05 -0.26 -0.24 0.15 0.21 -0.11 0.57 0.44 0.54 0.19 1.47 0.09 0.05 0.08 -0.36 0.06 0.14 0.05 0.69 -0.26 0.99 0.18 -0.11 -0.03 0.31 -0.48 0.55 -2.13 0.88 -0.29 0.43 0.26 0.32 0.1 -0.2 -0.06 0.76 -0.76 -0.01 -0.03 -0.68 -0.18 -0.34 0.1 -1.35 -0.14 -0.51 -0.33 -0.47 -0.25 -1.06 1.19 -1.07 -0.13 -0.21 0.16 -0.22 -0.02 -0.32 0.16 -0.04 0.35 -0.61 0.04 -0.16 -0.27 -0.31 0.1 -0.36 -0.19 -0.16 0.22 -0.17 -0.03 -0.25 0.18 -0.18 -0.13 -0.37 0.1 -0.62 -0.18 -0.44 0.12 -0.21 -1.07 -0.3 -0.63 -0.39 -0.85 -0.01 -0.21 -1.02 3.01 -0.97 0.41 0.53 0.75 -0.15 0.16 0.22 0.05 0.64 -0.34 0.64 0.2 0.39 0.69 0.56 -0.06 0.29 0.2 0.01 -0.01 -0.43 -0.14 -0.15 0.27 -0.42 0.4 -0.11 -0.01 -0.83 0.12 -0.29 -0.04 -0.23 -0.78 -0.98 -0.53 -0.35 -0.2 -1.15 0.25 -0.61 0.95 0.03 0.26 -0.19 0.36 0.29 0.37 0.03 0.27 -0.17 0.65 0.15 0.32 -0.16 0.14 -0.27 0.56 0.13 0.41 -0.25 0.36 -0 z";
    var testPath2 = " M 29 17 L 29 15 29 13 31 13 33 13 33 11 35 11 37 11 39 11 39 9 41 9 41 11 41 13 41 15 41 17 41 19 39 19 37 19 37 21 37 23 37 25 35 25 35 27 33 27 31 27 31 25 31 23 31 21 31 19 31 17 Z";
    //
    //    var testPath3 = "  M8 64 L8 56 L16 56 L16 64 L8 64 Z";
    //    var testPath3 = "M781.68,324.4l-2.31,8.68l-12.53,4.23l-3.75-4.4l-1.82,0.5l3.4,13.12l5.09,0.57l6.79,2.57v2.57l3.11-0.57l4.53-6.27v-5.13l2.55-5.13l2.83,0.57l-3.4-7.13l-0.52-4.59L781.68,324.4L781.68,324.4M722.48,317.57l-0.28,2.28l6.79,11.41h1.98l14.15,23.67l5.66,0.57l2.83-8.27l-4.53-2.85l-0.85-4.56L722.48,317.57L722.48,317.57M789.53,349.11l2.26,2.77l-1.47,4.16v0.79h3.34l1.18-10.4l1.08,0.3l1.96,9.5l1.87,0.5l1.77-4.06l-1.77-6.14l-1.47-2.67l4.62-3.37l-1.08-1.49l-4.42,2.87h-1.18l-2.16-3.17l0.69-1.39l3.64-1.78l5.5,1.68l1.67-0.1l4.13-3.86l-1.67-1.68l-3.83,2.97h-2.46l-3.73-1.78l-2.65,0.1l-2.95,4.75l-1.87,8.22L789.53,349.11L789.53,349.11M814.19,330.5l-1.87,4.55l2.95,3.86h0.98l1.28-2.57l0.69-0.89l-1.28-1.39l-1.87-0.69L814.19,330.5L814.19,330.5M819.99,345.45l-4.03,0.89l-1.18,1.29l0.98,1.68l2.65-0.99l1.67-0.99l2.46,1.98l1.08-0.89l-1.96-2.38L819.99,345.45L819.99,345.45M753.17,358.32l-2.75,1.88l0.59,1.58l8.75,1.98l4.42,0.79l1.87,1.98l5.01,0.4l2.36,1.98l2.16-0.5l1.97-1.78l-3.64-1.68l-3.14-2.67l-8.16-1.98L753.17,358.32L753.17,358.32M781.77,366.93l-2.16,1.19l1.28,1.39l3.14-1.19L781.77,366.93L781.77,366.93M785.5,366.04l0.39,1.88l2.26,0.59l0.88-1.09l-0.98-1.49L785.5,366.04L785.5,366.04M790.91,370.99l-2.75,0.4l2.46,2.08h1.96L790.91,370.99L790.91,370.99M791.69,367.72l-0.59,1.19l4.42,0.69l3.44-1.98l-1.96-0.59l-3.14,0.89l-1.18-0.99L791.69,367.72L791.69,367.72M831.93,339.34l-4.17,0.47l-2.68,1.96l1.11,2.24l4.54,0.84v0.84l-2.87,2.33l1.39,4.85l1.39,0.09l1.2-4.76h2.22l0.93,4.66l10.83,8.96l0.28,7l3.7,4.01l1.67-0.09l0.37-24.72l-6.29-4.38l-5.93,4.01l-2.13,1.31l-3.52-2.24l-0.09-7.09L831.93,339.34L831.93,339.34z"
    var paths = [];
    var colors = ["#BDD7E7", "#6BAED6", "#2171B5" ]
    paths[0] = testPath1;
    paths[1] = testPath2;
    //    paths[2] = testPath3;

    var paper = Raphael(document.getElementById("testmap"));
    var layers = paper.set();
    for (var p in paths){
        var raphObj = paper.path(paths[p]);
        raphObj.attr({
            'stroke':colors[p]
        });
        //        raphObj.attr({
        //            'fill':colors[p]
        //            });
        layers.push(raphObj);
    }
    var x = layers.getBBox().x;
    var y = layers.getBBox().y;
    layers.scale(2.1,2.1,x/2.0,y/2.0);
    layers.translate((15 - x), 1 - y);
   
}

var countries = [];
countries.push({
    "iso3":"AFG",
    "min":0.0,
    "max":0.0,
    "svg":"M 67.78 -37.19 l 3.18 -1.29 0.72 1.8 3.24 -0.56 -3.3 0.78 -0.54 2.4 -4.82 4.2 -5.39 -0.01 0.98 -1.16 -1.27 -2.04 0.71 -2.56 1.43 0.37 2.97 -2.27 z",
    "name":"Afghanistan"
});
countries.push({
    "iso3":"ALB",
    "min":0.0,
    "max":0.0,
    "svg":"M 20.07 -42.56 l 0.98 1.88 -0.86 1.03 -0.91 -0.78 z",
    "name":"Albania"
});
countries.push({
    "iso3":"DZA",
    "min":0.0,
    "max":0.0,
    "svg":"M -2.21 -35.09 l 3.39 -1.43 7.44 -0.43 -1.13 3.05 2.35 4.73 -0.44 3.01 2.59 2.63 -8.65 4.55 -12 -8.31 1.02 -2.1 6.46 -2.73 z",
    "name":"Algeria"
});
countries.push({
    "iso3":"AGO",
    "min":0.0,
    "max":0.0,
    "svg":"M 23.99 10.87 l 0.03 2.13 -2.02 0.01 -0 3.17 1.48 1.45 -11.72 -0.37 2.1 -6.25 -1.57 -4.94 4.23 -0.19 1.12 2.22 4.15 -0.82 0.48 3.93 z",
    "name":"Angola"
});
countries.push({
    "iso3":"ARG",
    "min":0.0,
    "max":0.0,
    "svg":"M -68.64 54.79 l 0.02 -2.15 3.48 2.01 z M -62.64 22.24 l 4.88 2.93 -0.84 2.15 2.87 0.12 1.87 -1.75 0.02 1.49 -4.05 3.36 -0.58 4 1.81 2.36 -1.64 1.58 -4.08 0.32 -0 2.1 -2.74 -0.06 1.55 1.77 -1.37 0.05 -0.66 2.36 -1.97 0.98 1.79 1.97 -3.21 2.04 -0.61 1.61 1.23 0.7 -3.53 -0.33 -1.67 -2.45 2.47 -5.01 -1.02 -2.25 2.31 -8.05 -0.21 -4.94 3.29 -7.06 z",
    "name":"Argentina"
});
countries.push({
    "iso3":"ARM",
    "min":0.0,
    "max":0.0,
    "svg":"M 45.02 -41.3 l 1.52 2.42 -2.89 -1.25 -0.19 -0.99 z",
    "name":"Armenia"
});
countries.push({
    "iso3":"AUS",
    "min":0.0,
    "max":0.0,
    "svg":"M 142.02 12.07 l 0.5 -1.36 3.76 8.18 2.49 1.35 4.41 5.72 -0.13 5.09 -3.08 6.49 -3.58 1.63 -1.48 -1.28 -3.35 0.55 -2.21 -3.04 -1.26 0.24 -0 -1.48 -1.26 1.12 0.98 -2.68 -1.86 2.44 -1.77 -2.52 -3.04 -1.01 -13.21 3.65 -2.93 -0.86 0.73 -2.39 -2.52 -5.63 1 0.05 -0.19 -4.45 7 -2.25 1.89 -3.18 0.66 1.18 -0 -1.43 1.32 0.24 -0.44 -0.93 1.56 -1.55 1.41 0.03 0.6 1.54 1.71 -0.32 0.85 -2.78 2.17 -0.27 -0.76 -1.01 4.58 0.81 -1.11 3 5.04 2.71 z M 145.08 40.81 l 3.14 0.04 -0.23 2.38 -1.96 0.27 z",
    "name":"Australia"
});
countries.push({
    "iso3":"AUT",
    "min":0.0,
    "max":0.0,
    "svg":"M 16.95 -48.62 l -0.44 1.61 -1.96 0.6 -5.01 -0.87 z",
    "name":"Austria"
});
countries.push({
    "iso3":"AZE",
    "min":0.0,
    "max":0.0,
    "svg":"M 46.45 -41.9 l 3.92 1.63 -1.49 1.82 -0.9 -1.27 -1.44 0.84 -1.52 -2.42 1.49 0.25 z",
    "name":"Azerbaijan"
});
countries.push({
    "iso3":"BGD",
    "min":0.0,
    "max":0.0,
    "svg":"M 89.06 -22.12 l -0.65 -4.51 4.08 1.71 -1.33 1.19 1.13 0.03 0.04 2.95 -1.62 -2.8 -0.44 1.7 z",
    "name":"Bangladesh"
});
countries.push({
    "iso3":"BLR",
    "min":0.0,
    "max":0.0,
    "svg":"M 28.17 -56.15 l 2.76 0.55 1.81 2.14 -1.47 0.44 0.52 0.92 -8.22 0.57 -0.06 -2.41 z",
    "name":"Belarus"
});
countries.push({
    "iso3":"BEL",
    "min":0.0,
    "max":0.0,
    "svg":"M 4.24 -51.35 l 1.61 0.2 0.02 1.58 -3.32 -1.52 z",
    "name":"Belgium"
});
countries.push({
    "iso3":"BEN",
    "min":0.0,
    "max":0.0,
    "svg":"M 3.6 -11.69 l -0.88 5.33 -1.08 0.15 -0.83 -4.49 1.59 -1.53 z",
    "name":"Benin"
});
countries.push({
    "iso3":"BTN",
    "min":0.0,
    "max":0.0,
    "svg":"M 88.92 -27.32 l 1.1 -1 2.09 1.39 z",
    "name":"Bhutan"
});
countries.push({
    "iso3":"BOL",
    "min":0.0,
    "max":0.0,
    "svg":"M -69.5 17.51 l 0.83 -5 -0.89 -1.55 4.13 -1.27 0.43 2.31 4.53 1.81 0.32 2.46 1.83 0.02 0.81 1.92 -0.59 1.95 -3.63 -0.5 -0.9 2.59 -1.69 0.63 -1.88 -1.09 -1.36 1.12 z",
    "name":"Bolivia"
});
countries.push({
    "iso3":"BIH",
    "min":0.0,
    "max":0.0,
    "svg":"M 19.04 -44.86 l -0.58 2.3 -2.71 -2.25 z",
    "name":"Bosnia and Herzegovina"
});
countries.push({
    "iso3":"BWA",
    "min":0.0,
    "max":0.0,
    "svg":"M 29.37 22.19 l -3.96 3.54 -2.4 -0.43 -2.37 1.53 -0.64 -4.82 1 -0.01 0 -3.68 4.26 -0.52 z",
    "name":"Botswana"
});
countries.push({
    "iso3":"BRA",
    "min":0.0,
    "max":0.0,
    "svg":"M -51.68 -4.03 l 1.78 2.86 -2.8 2.78 1.85 -0.69 -0.59 1.36 2.17 -0.56 -0.21 0.85 2.06 -1.98 2.57 0.85 0.07 1.87 0.72 -0.89 4.06 0.44 4.58 2.37 0.6 2.72 -4.09 4.77 -0.23 4.98 -1.86 4.32 -7.73 3.42 -0.04 3.07 -3.31 3.68 1.5 -1.71 -0.71 -0.45 -2.12 3.73 -0.48 -1.77 -3.73 -1.79 3.96 -3.93 -2.2 -3.96 -2.14 -0.2 -0.34 -5.81 -1.83 -0.02 -0.32 -2.46 -4.53 -1.81 -0.4 -2.3 -5.22 1.32 0.12 -1.58 -1.67 0.57 -1.82 -2.45 1.15 -2.43 2.9 -0.86 0.1 -5.97 2.42 -0.43 1.94 1.48 2.13 -1.6 -1.42 -2.03 1.9 0.73 2.19 -1.66 1.88 4.03 2.85 -1.35 3.01 0.36 z M -49.71 0.23 l 1.34 0.06 -0.48 1.17 -1.73 0.34 -0.08 -1.51 z",
    "name":"Brazil"
});
countries.push({
    "iso3":"BGR",
    "min":0.0,
    "max":0.0,
    "svg":"M 28.58 -43.75 l -0.57 1.77 -1.87 0.63 -3.2 0.01 -0.57 -0.98 0.31 -1.9 z",
    "name":"Bulgaria"
});
countries.push({
    "iso3":"BFA",
    "min":0.0,
    "max":0.0,
    "svg":"M 2.4 -11.9 l -5.23 0.89 0.08 1.61 -2.76 -1.03 1.28 -2.81 3.51 -1.84 z",
    "name":"Burkina Faso"
});
countries.push({
    "iso3":"BDI",
    "min":0.0,
    "max":0.0,
    "svg":"M 30.57 2.4 l -1.15 2.05 -0.36 -1.84 z",
    "name":"Burundi"
});
countries.push({
    "iso3":"KHM",
    "min":0.0,
    "max":0.0,
    "svg":"M 107.55 -14.71 l -1.34 3.94 -3.07 -0.11 -0.17 -3.32 z",
    "name":"Cambodia"
});
countries.push({
    "iso3":"CMR",
    "min":0.0,
    "max":0.0,
    "svg":"M 15.5 -7.53 l -1.08 1.49 1.74 4.32 -6.33 -0.53 -0.97 -3.58 2.69 -0.82 3.09 -4.91 -0.57 -1.51 1.6 3.09 -1.72 0.34 z",
    "name":"Cameroon"
});
countries.push({
    "iso3":"CAN",
    "min":0.0,
    "max":0.0,
    "svg":"M -134.24 -69.58 l 0.01 0.88 -1.77 -0.25 z M -72.34 -83.1 l 11.26 0.78 -8.22 0.61 2.67 0.2 -3.59 0.34 5.76 -0.31 -13.61 2.13 3.61 0.29 -4.44 -0 4.16 0.36 -3.97 1.39 -3.24 -0.37 4.14 0.9 -3.27 0.66 -8.62 -0.44 2.93 -0.61 -1.33 -0.65 5.74 -0.25 -5.21 -0.07 0.68 -0.59 5.37 -0.31 -3.26 0.01 1.38 -0.02 -3.14 -1.25 6.62 0.65 -3.3 -0.67 6.43 -1.12 -8.3 0.93 -1.67 -0.1 4.37 -0.57 -5.23 0.55 -1.86 -0.28 4.72 -0.37 -7.22 -0.38 z M -132.82 -54.12 l 1.15 -0.02 -0.3 0.87 z M -116.35 -77.54 l 0.96 0.23 -1.71 1.01 -5.94 0.21 z M -95.74 -77.07 l 16.4 2.17 -12.21 0.25 -1.53 -1.71 -3.88 -0.37 z M -116.92 -75.77 l 2.18 -0.74 5.84 1.04 -1.5 -0.91 1.61 -0.47 3.39 1.21 -7.62 1.25 -1.43 -0.27 3.53 -0.57 -6.76 -0.01 2.68 -0.45 z M -99.98 -76.62 l 2.29 0.14 0.4 1.08 -5.59 -0.21 3.43 -0.36 -1.53 -0.53 z M -95.41 -77.76 l 2.3 0.1 -3.22 0.06 z M -101.71 -77.9 l 0.78 0.16 -1.6 -0.1 z M -131.76 -53.2 l 0.73 1.02 -1.52 -0.92 z M -118.32 -75.57 l -1.09 -0.03 1.94 -0.48 z M -94.36 -75.59 l 0.9 0.88 -3.15 -0.28 z M -120.15 -74.27 l 4.83 0.8 -7.48 2.39 -3.19 -0.89 2.21 -1.79 -0.98 -0.58 z M -92.64 -74.1 l 2.44 0.21 -5.01 1.91 -0.47 -1.73 z M -99.81 -73.9 l 2.84 0.16 -1.48 0.87 1.92 1 -2.2 0.6 -4.03 -1.49 2.34 0.02 0.64 -0.46 -1.84 -0.28 z M -89.96 -72.08 l 1.68 -1.49 3.25 -0.23 -1.7 1.08 1.9 1.44 -1.98 0.29 2.01 0.07 -1.24 -1.09 1.88 -0.01 -1.52 -0.87 1.73 0.15 -1.49 -0.37 3.9 -0.59 1.29 0.99 -0.72 0.84 5.75 -0.61 -1.12 0.61 5.74 0.84 -0.93 1.03 3.21 -0.54 -2.15 0.72 2.66 -0.42 0.67 0.53 -2.9 0.19 3.24 0.19 -2.6 0.47 8.13 2.24 -2.28 1.74 -1.95 -0.86 1.14 -0.59 -4.48 0.15 4.34 2.55 -0.68 1.08 -3.78 -1.2 3.01 1.85 -2.61 -0.34 -4.75 -2.41 -4.88 0.09 4.68 -0.9 -0.97 -0.66 2.21 -1.11 -1.5 -1.44 -2.87 -0 1.04 -0.53 -3.43 -1.46 0.23 0.79 -2.96 -0.23 0.8 0.41 -6.97 -0.53 -1.63 -0.85 2.54 0.1 z M -96.77 -78.68 l 1.88 0.58 -3.52 -0.39 z M -80.15 -73.7 l 4.09 0.79 -3.56 0.14 -1.25 -0.57 z M -105.1 -73.74 l -0.16 0.89 -1.77 -0.64 z M -113.14 -70.61 l -4.41 0.01 -0.86 -0.4 3.34 -0.52 -4.07 -0.25 4.57 -1.6 2.9 1.1 2 -0.65 1.91 1.31 -0.54 -1.54 1.43 -0.16 2.28 2.25 3.71 1.27 -12.39 1.34 -4.17 -1.53 5.98 -0.3 z M -103.59 -79.33 l 4.65 1.27 -6.68 -1.11 z M -85.5 -65.8 l 5.33 2.03 -7.01 0.18 z M -60.45 -46.86 l -0.7 1.16 1.31 -0.24 -1.5 0.36 z M -55.39 -47.59 l -4.01 -0.31 3.5 -3.73 -0.95 2.08 3.34 0.27 -0.33 1.58 1.22 0.19 -0.95 0.89 -0.62 -1.23 -1.77 0.93 z M -92.5 -63.82 l 1.87 0.76 -2.99 1.12 -1.2 1.98 0.46 1.74 1.17 -0.54 0.33 1.85 4.05 0.08 3.4 1.83 3.11 -0.15 1.3 4.12 2.16 -0.13 -0.92 -3.49 3.23 -1.67 -2.04 -2.31 1.81 -1.53 -1.42 -0.63 0.67 -1.77 7.99 1.5 -1.43 1 1.35 0.23 -0.22 1.24 1.46 -0.18 -1.01 1 3.3 -0.55 1.59 -1.96 3.79 5.29 3.33 0.42 -3.06 1.31 2.94 -0.93 1.78 2.08 -4.32 1.86 -6.44 -0.02 -3.23 2.13 -1.36 -0.3 1.31 0.34 -1.57 1.37 3.77 -2.12 3.3 -0.05 -2.62 0.91 5.87 2.73 -4.51 1.81 -0.71 -0.95 2.83 -0.94 -1.38 -0.73 -2.59 0.94 -0.46 -1.92 -1.44 -0.4 -1.64 2.23 -4.12 0.26 -7.43 3.31 -0.12 -3.68 -5.82 -2.96 -6.78 -1.06 -27.94 0.38 -1.71 -1.92 -2.98 -0.25 1.17 -0.51 -1.26 0.01 0.9 -1.17 -1.42 0.54 -0.21 -1.74 -1.87 -0.33 1 -1.11 -5.62 -4.15 -2.37 0.71 -3.52 -1.4 -0.01 -9.34 6.69 0.96 4.9 -1.43 -3.51 1.41 4.92 -1.9 2.58 1.28 0.98 -0.84 10.37 1.68 -1.04 0.67 5.03 -0.21 2.85 1.66 -0.66 -1.74 2.24 -0.55 -3.16 0.37 2.61 -0.67 3.96 1.22 6.27 -0.53 0.33 1.59 2.28 -2.71 -3.16 -0.75 1.56 -1.85 3.46 1.82 -1.41 0.48 2.61 0.23 -1.12 0.1 1.17 1.11 2.22 -0.58 0.53 1.7 2.98 -1.9 -1.02 -0.84 4.26 0.74 -1.33 0.62 1.13 1.5 -3.43 -0.06 1.25 0.85 -3.07 -0.32 0.86 0.36 -1.5 0.85 -4.03 -0.63 4.49 0.81 z M -127.23 -50.64 l 3.93 2.22 -5.12 -2.36 z M -93.91 -81.04 l 9 1.77 -7.15 1.06 -2.23 -0.77 3.92 -0.26 -6.43 -0.84 2.42 0.1 -2.29 -0.36 z M -75.58 -68.3 l 0.38 0.86 -2.05 -0.01 z M -97.36 -69.69 l 2.16 0.84 -4.38 -0.17 z",
    "name":"Canada"
});
countries.push({
    "iso3":"CAF",
    "min":0.0,
    "max":0.0,
    "svg":"M 22.87 -10.92 l 0.66 2.21 3.93 3.69 -5.11 0.89 -2.93 -1 -3.21 2.91 -1.67 -3.07 0.7 -1.94 z",
    "name":"Central African Republic"
});
countries.push({
    "iso3":"TCD",
    "min":0.0,
    "max":0.0,
    "svg":"M 24 -19.5 l -0.01 3.8 -2.17 2.91 1.04 1.87 -4.28 2.88 -3.09 0.51 -1.54 -2.12 1.72 -0.34 -2.21 -4.44 2.03 -2.49 0.51 -3.43 -1 -2.65 1 -0.45 z",
    "name":"Chad"
});
countries.push({
    "iso3":"CHL",
    "min":0.0,
    "max":0.0,
    "svg":"M -68.4 54.96 l 0.35 0.76 -1.98 -0.55 z M -69.15 52.68 l 0.51 2.2 -3.33 -0.24 2.97 -0.18 -1.49 -1.24 z M -69.5 17.51 l 2.5 5.5 -3.53 8.18 0.71 3.05 -2.31 8.05 1.02 2.25 -2.47 5.01 1.67 2.45 3.47 0.33 -2.38 0.4 -0.47 1.15 -1.17 -0.48 1.29 -0.59 -2.13 0.35 1.75 -0.6 -1.91 0.44 -0.28 -0.96 1.26 -0.25 -1.08 0.27 -0.03 -1.65 -1.12 -0.2 0.98 -0.45 -0.94 -1.73 1.43 -0.03 -2.49 -1.28 1.35 -0.93 0.52 0.8 1.23 -2.12 0.3 -3.04 -1.68 -0.47 0.32 -3.62 2.02 -3.68 1.25 -15.31 z",
    "name":"Chile"
});
countries.push({
    "iso3":"CHN",
    "min":0.0,
    "max":0.0,
    "svg":"M 110.72 -20.07 l -0.67 1.68 -1.36 -0.12 z M 124.34 -53.26 l 6.62 5.55 3.81 0.01 -1.69 2.61 -2.12 0.25 -0.35 2.41 -0.7 -0.58 -8.72 4.29 1.11 -1.79 -0.93 -0.43 -3.62 1.83 1.49 1.96 3.33 -0.25 -3.38 2.51 2.71 3.12 -2.28 -0.43 2.26 1.18 -1.72 0.81 1.95 0.3 -0.46 1.55 -5.16 5.41 -2.8 -0.21 -3.29 1.78 -0.12 1.13 -0.71 -1.48 -2.21 0.12 -2 -1.72 -3.67 0.85 0.09 1.33 -1.58 -0.29 -1.31 -2.72 -1.35 0.21 1.16 -3.58 -2.61 -1.93 -7.17 2.14 -7.8 -2.7 -2.72 -2.52 1.12 -0.14 -0.61 -1.29 1.36 -1.58 -4.09 -0.28 -2.47 -3.04 1.15 -1.66 5.41 -1.54 -0.4 -2.85 2.7 -0.23 0.47 -2.07 2.49 0.14 1.35 -2.05 3.48 1.48 0.55 2.37 4.51 0.96 0.97 1.56 8.62 1.15 4.51 -0.87 2.44 -1.24 -0.09 -1.36 8.03 -1.63 -1.36 -1.32 -2.99 -0.15 1.16 -1.69 2.61 -0.27 1.51 -3.17 z",
    "name":"China"
});
countries.push({
    "iso3":"COL",
    "min":0.0,
    "max":0.0,
    "svg":"M -77.37 -8.67 l 0.61 0.76 1.49 -2.88 3.74 -1.65 -1.85 3.27 0.91 1.67 5.02 1.3 0.58 4.97 -0.55 -0.92 -2.42 0.43 -0.11 5.95 -0.34 -1.74 -2.59 0.01 -1.89 -2.3 -4.28 -1.84 2.02 -2.29 z",
    "name":"Colombia"
});
countries.push({
    "iso3":"COG",
    "min":0.0,
    "max":0.0,
    "svg":"M 18.62 -3.48 l -2.73 7.42 -3.89 1.07 -0.43 -2.68 2.85 -0.45 0.06 -2.8 -1.19 -1.25 2.78 0.51 0.57 -1.87 z",
    "name":"Congo"
});
countries.push({
    "iso3":"COD",
    "min":0.0,
    "max":0.0,
    "svg":"M 30.86 -3.49 l 0.44 1.37 -2.45 4.65 1.92 5.67 -2.4 1.09 1.43 4.17 -2.59 -1.87 -4.96 -0.37 -0.48 -3.93 -4.15 0.82 -1.05 -2.2 -4.34 -0.09 3.66 -1.87 3.53 -9.07 2.93 1 4.74 -1.08 z",
    "name":"Congo, Democratic Republic of"
});
countries.push({
    "iso3":"CRI",
    "min":0.0,
    "max":0.0,
    "svg":"M -83.65 -10.92 l 0.75 2.9 -2.95 -2.22 0.24 -0.97 z",
    "name":"Costa Rica"
});
countries.push({
    "iso3":"CIV",
    "min":0.0,
    "max":0.0,
    "svg":"M -5.52 -10.44 l 2.85 0.97 -0.06 4.35 -4.79 0.76 -1.08 -2.16 0.46 -3.48 z",
    "name":"Cote d\u0027Ivoire"
});
countries.push({
    "iso3":"HRV",
    "min":0.0,
    "max":0.0,
    "svg":"M 18.82 -45.91 l 0.22 1.05 -3.25 -0.31 1.79 2.23 -4.07 -2.56 2.79 -1.03 z",
    "name":"Croatia"
});
countries.push({
    "iso3":"CUB",
    "min":0.0,
    "max":0.0,
    "svg":"M -75.14 -19.96 l -2.59 0.13 0.49 -0.83 -4.65 -2.02 -3.07 0.82 4.32 -1.24 6.5 2.88 z",
    "name":"Cuba"
});
countries.push({
    "iso3":"CYP",
    "min":0.0,
    "max":0.0,
    "svg":"M 33.27 -34.71 l -1 -0.37 2.32 -0.61 z",
    "name":"Cyprus"
});
countries.push({
    "iso3":"CZE",
    "min":0.0,
    "max":0.0,
    "svg":"M 18.85 -49.52 l -4.57 0.93 -2.18 -1.74 3.08 -0.69 z",
    "name":"Czech Republic"
});
countries.push({
    "iso3":"DNK",
    "min":0.0,
    "max":0.0,
    "svg":"M 12.57 -55.99 l -0.5 1.03 -1.19 -0.77 z M 8.66 -54.91 l -0.47 -1.78 2.77 0.25 z",
    "name":"Denmark"
});
countries.push({
    "iso3":"DJI",
    "min":0.0,
    "max":0.0,
    "svg":"M 43.12 -12.71 l -0.18 1.71 -1.14 0.02 z",
    "name":"Djibouti"
});
countries.push({
    "iso3":"DOM",
    "min":0.0,
    "max":0.0,
    "svg":"M -71.77 -18.04 l 0.17 -1.87 3.27 1.29 z",
    "name":"Dominican Republic"
});
countries.push({
    "iso3":"ECU",
    "min":0.0,
    "max":0.0,
    "svg":"M -75.29 0.12 l -3.75 4.88 -1.43 -0.55 0.41 -5.27 1.24 -0.61 z",
    "name":"Ecuador"
});
countries.push({
    "iso3":"EGY",
    "min":0.0,
    "max":0.0,
    "svg":"M 34.27 -31.22 l -0.01 3.49 -1.91 -1.87 4.56 7.59 -11.89 0.01 0.07 -9.59 z",
    "name":"Egypt, Arab Republic of"
});
countries.push({
    "iso3":"SLV",
    "min":0.0,
    "max":0.0,
    "svg":"M -89.35 -14.43 l 1.55 1.17 -2.3 -0.48 z",
    "name":"El Salvador"
});
countries.push({
    "iso3":"GNQ",
    "min":0.0,
    "max":0.0,
    "svg":"M 11.34 -2.17 l 0.01 1.17 -1.99 -0.15 0.45 -1.19 z",
    "name":"Equatorial Guinea"
});
countries.push({
    "iso3":"ERI",
    "min":0.0,
    "max":0.0,
    "svg":"M 36.54 -14.26 l 0.45 -2.8 1.61 -0.93 1.12 2.91 3.4 2.38 -2.96 -1.76 z",
    "name":"Eritrea"
});
countries.push({
    "iso3":"EST",
    "min":0.0,
    "max":0.0,
    "svg":"M 24.31 -57.87 l -0.81 -1.35 4.69 -0.14 -0.82 1.83 z",
    "name":"Estonia"
});
countries.push({
    "iso3":"ETH",
    "min":0.0,
    "max":0.0,
    "svg":"M 42.4 -12.47 l -0.61 1.46 1.15 0.01 0.5 1.58 4.55 1.41 -3.04 3.1 -6.83 1.29 -5.11 -4.23 3.54 -6.44 3.67 -0.15 z",
    "name":"Ethiopia"
});
countries.push({
    "iso3":"FJI",
    "min":0.0,
    "max":0.0,
    "svg":"M 178.28 17.39 l 0.38 0.7 -1.36 -0.01 z",
    "name":"Fiji"
});
countries.push({
    "iso3":"FIN",
    "min":0.0,
    "max":0.0,
    "svg":"M 20.58 -69.06 l 4.35 0.48 1.54 -1.36 2.63 0.23 -0.64 1.17 1.57 0.84 -0.95 0.79 0.92 3.17 1.58 0.83 -3.84 2.4 -6.29 -0.05 0.04 -2.64 3.95 -1.75 -1.76 -1.31 -0.02 -1.68 z",
    "name":"Finland"
});
countries.push({
    "iso3":"ESH",
    "min":0.0,
    "max":0.0,
    "svg":"M -8.67 -27.67 l -0 1.67 -3.33 0 -1 4.66 -4.06 0.57 3.88 -6.9 z",
    "name":"Former Spanish Sahara"
});
countries.push({
    "iso3":"FRA",
    "min":0.0,
    "max":0.0,
    "svg":"M 2.54 -51.09 l 5.68 2.13 -2.26 2.76 0.83 -0.22 0.91 2.35 -5.17 1.74 -3.97 -0.72 0.32 -3.27 -3.66 -2.19 3.41 -0.13 -0.57 -1.08 2.37 0.27 z M 9.45 -42.68 l -0.26 1.32 -0.61 -1.02 z",
    "name":"France"
});
countries.push({
    "iso3":"GUF",
    "min":0.0,
    "max":0.0,
    "svg":"M -54.17 -5.35 l 2.4 0.81 -1.19 2.36 -1.65 -0.15 z",
    "name":"French Guiana (Fr.)"
});
countries.push({
    "iso3":"GAB",
    "min":0.0,
    "max":0.0,
    "svg":"M 13.29 -2.16 l 1.19 1.25 -0.06 2.8 -2.85 0.45 -0.43 1.59 -2.44 -3.34 0.9 -1.6 1.75 0.02 0.01 -1.3 z",
    "name":"Gabon"
});
countries.push({
    "iso3":"GEO",
    "min":0.0,
    "max":0.0,
    "svg":"M 40 -43.38 l 5.16 0.68 1.53 1.49 -5.17 -0.31 z",
    "name":"Georgia"
});
countries.push({
    "iso3":"DEU",
    "min":0.0,
    "max":0.0,
    "svg":"M 10.98 -54.38 l 3.3 0.68 0.69 2.65 -2.87 0.73 1.71 1.74 -0.79 1.11 -5.39 -0.09 0.6 -1.4 -1.86 -0.49 -0.4 -2.35 1.27 -1.86 2.6 0.13 -1.44 -1.51 z",
    "name":"Germany"
});
countries.push({
    "iso3":"GHA",
    "min":0.0,
    "max":0.0,
    "svg":"M -0.15 -11.14 l 1.35 5.04 -4.3 1.02 0.27 -5.92 z",
    "name":"Ghana"
});
countries.push({
    "iso3":"GRC",
    "min":0.0,
    "max":0.0,
    "svg":"M 26.36 -41.71 l -0.27 0.97 -2.37 -0.01 0.27 0.79 -1.4 -0.51 0.76 1.28 -0.82 0.32 1.57 1.09 -1.36 0.21 0.48 1.12 -1.5 -0.38 -0.32 -1.39 1.84 0.06 -2.93 -1.17 0.69 -1.53 z",
    "name":"Greece"
});
countries.push({
    "iso3":"GRL",
    "min":0.0,
    "max":0.0,
    "svg":"M -39.64 -83.26 l 1 0.14 -2.03 -0.16 z M -41.75 -83.12 l 20.43 0.51 -11.78 0.84 11.08 -0.16 -2.49 1.39 12.35 -1.06 -9.09 1.03 5.13 0.06 -4.44 0.4 3.1 0.05 -4.59 2.37 3.73 0.88 -4.43 0.1 3.4 1.3 -3.09 0.24 3.45 0.68 -3.5 0.17 1.98 0.86 -7.23 0.32 2.68 0.05 -2.34 0.24 5.49 1.1 0.42 1.2 -7.16 -1.58 3.23 0.77 -2.99 0.37 -0.8 0.58 2.88 0.01 -2.22 0.33 6.46 -0.09 -7.3 1.94 -3.1 -0.42 0.34 0.77 -2.59 1.51 -5.38 0.77 -1.47 1.3 1.05 0.56 -2.63 0.94 0.98 0.38 -1.45 1.26 0.86 0.44 -1.45 0.09 1.11 0.49 -1.52 0.12 -1.16 -1.35 -2.45 0.52 -2.83 -2.37 0.78 -0.22 -1.38 -0.61 1.61 -0.19 -1.7 -0.01 2.16 -0.15 -1.39 -0.86 -1.02 1.01 0.75 -0.81 -1.31 -0.3 2.02 -0.39 -2.92 -0.26 3.47 -1 -3.62 0.83 1.39 -0.69 -1.58 -0.34 3.47 -0.01 -3.45 -0.23 3.86 -0.28 -3.79 0.16 3.59 -0.4 -3.24 -0.39 3.17 -0.63 -0.9 -0.24 0.9 -0.82 -4.41 -0.63 4.08 0.31 -2.44 -1.08 1.34 -0.29 -4.26 0.03 1.61 -0.8 -4.12 -3.24 -10.08 -0.37 -1.13 -0.29 1.65 -0.31 -3.39 -0.38 5.32 -0.44 -7 -0.67 7.08 -0.94 2.19 -1.04 -3.7 -0.18 6.68 -1.56 21.05 -0.51 -7.13 -0.57 z M -23.61 -72.83 l 1.68 0.43 -2.54 -0.43 z M -24.36 -73.41 l 1.42 0.28 -2.77 -0.05 z M -52.69 -69.92 l 0.86 0.28 -3.15 -0.06 z M -46 -82.64 l 1.58 0.27 -3.33 -0.26 z M -20.93 -74.42 l 0.81 0.22 -1.86 -0.02 z",
    "name":"Greenland (Den.)"
});
countries.push({
    "iso3":"GTM",
    "min":0.0,
    "max":0.0,
    "svg":"M -88.91 -15.89 l -1.19 2.15 -2.15 -0.8 0.52 -1.52 1.29 -0.01 -0.54 -1.73 1.84 0 z",
    "name":"Guatemala"
});
countries.push({
    "iso3":"GIN",
    "min":0.0,
    "max":0.0,
    "svg":"M -11.37 -12.41 l 2.42 0.05 0.73 4.81 -2.48 -0.75 -0.52 -1.7 -2.08 0.97 -1.78 -1.81 1.36 -1.84 z",
    "name":"Guinea"
});
countries.push({
    "iso3":"GNB",
    "min":0.0,
    "max":0.0,
    "svg":"M -13.71 -12.68 l -1.37 1.75 0.08 -1.05 -1.71 -0.35 z",
    "name":"Guinea-Bissau"
});
countries.push({
    "iso3":"GUY",
    "min":0.0,
    "max":0.0,
    "svg":"M -59.99 -8.54 l 2.8 2.4 -0.88 1.98 1.6 2.21 -3.28 0.08 0.07 -2.52 -1.72 -1.55 z",
    "name":"Guyana"
});
countries.push({
    "iso3":"HTI",
    "min":0.0,
    "max":0.0,
    "svg":"M -71.77 -18.04 l -2.7 -0.41 2.12 -0.09 -0.44 -1.41 1.05 0.26 z",
    "name":"Haiti"
});
countries.push({
    "iso3":"HND",
    "min":0.0,
    "max":0.0,
    "svg":"M -88.21 -15.72 l 5.08 0.73 -4.17 2.01 -2.05 -1.45 z",
    "name":"Honduras"
});
countries.push({
    "iso3":"HUN",
    "min":0.0,
    "max":0.0,
    "svg":"M 22.89 -47.95 l -4.46 2.2 -2.32 -1.12 1.07 -1.15 z",
    "name":"Hungary"
});
countries.push({
    "iso3":"ISL",
    "min":0.0,
    "max":0.0,
    "svg":"M -22.02 -64.42 l -2.03 -0.47 2.36 -0.56 -2.84 -0.05 8.51 -1.04 2.53 1.47 -5.21 1.68 -4.01 -0.43 z",
    "name":"Iceland"
});
countries.push({
    "iso3":"IND",
    "min":0.0,
    "max":0.0,
    "svg":"M 77.82 -35.5 l 1.19 1.21 -0.24 2.98 2.26 1.1 -0.97 1.38 1.84 0.98 5.37 1.48 1.35 -1.74 1.02 1.4 6.23 -1.58 1.28 1.16 -2.01 0.51 -2.54 4.63 -0.32 -1.73 -1.12 0.08 1.25 -1.39 -4 -1.6 0.67 5 -1.18 -0.8 -1.48 2.44 -6.14 4.29 -0.42 5.41 -2.32 2.21 -4.09 -7.99 -0.53 -6.21 -2.09 1.58 -1.88 -1.59 1.56 -0.81 -2.37 -0.51 2.96 -0.81 -1.52 -2.76 2.31 -0.78 3.48 -4.25 -1.44 -2.42 z",
    "name":"India"
});
countries.push({
    "iso3":"IDN",
    "min":0.0,
    "max":0.0,
    "svg":"M 117.97 8.75 l 1.22 -0.05 -2.44 0.28 z M 106.93 6.09 l 5.63 0.82 2.06 1.83 -9.41 -1.97 z M 127.92 3.56 l 1.61 -0.78 1.35 0.81 z M 134.15 1.93 l 1.19 1.46 2.53 -1.92 3.14 1.14 0 6.52 -0.86 -1.24 -1.24 0.41 -0.85 -2.89 -4.24 -2.45 -0.93 1.13 -0.94 -1.3 1.98 -0.68 -2.97 -0.7 1.75 -1.04 z M 122.36 3.22 l 0.54 1.17 -1.2 0.45 -0.68 -2.16 -0.82 0.29 0.26 2.66 -1 -0.06 -0.71 -2.79 0.87 -2.77 1.2 -1.32 4.3 -0.35 -0.81 1.28 -4.2 0.22 0.55 1.57 2.73 -0.74 -2.11 1.13 z M 127.9 0.46 l 0.15 -2.65 -0.42 1.27 1.08 -0.65 0.2 1.37 z M 117.59 -4.17 l 1.42 3.19 -1.12 -0.13 -1.92 4.72 -5.73 -0.62 -0.9 -4.87 1.22 1.03 4 -0.58 1.31 -2.93 z M 104.49 1.64 l 0.04 1.13 1.52 0.26 -0.33 2.87 -4.1 -2.65 -6.39 -8.82 2.28 0.32 4.92 5 1.3 -0.05 z",
    "name":"Indonesia"
});
countries.push({
    "iso3":"IRN",
    "min":0.0,
    "max":0.0,
    "svg":"M 46.54 -38.88 l 1.44 -0.84 1.12 2.07 2.78 1.06 4.87 -1.7 4.4 1.64 -0.57 3.58 1.27 2.04 -0.98 1.16 2.32 3.22 -1.74 1.56 -3.31 -0.47 -1.32 -1.58 -3.06 0.41 -3.69 -3.49 -1.58 0.22 -3.07 -3.99 0.94 -1.84 -2.31 -3.57 z",
    "name":"Iran"
});
countries.push({
    "iso3":"IRQ",
    "min":0.0,
    "max":0.0,
    "svg":"M 40.41 -31.95 l -1.62 -1.43 2.21 -1.04 0.4 -2.11 3.34 -0.65 1.6 1.36 -0.94 1.84 3.16 4.04 -3.84 0.74 z",
    "name":"Iraq"
});
countries.push({
    "iso3":"IRL",
    "min":0.0,
    "max":0.0,
    "svg":"M -9.87 -54.23 l 2.94 -1.01 -1.24 0.77 2.04 0.43 -0.2 1.81 -3.81 0.63 1.31 -1.07 -1.11 0.11 0.99 -0.59 z",
    "name":"Ireland"
});
countries.push({
    "iso3":"ISR",
    "min":0.0,
    "max":0.0,
    "svg":"M 35.62 -33.25 l -0.72 3.76 -0.64 -1.73 z",
    "name":"Israel"
});
countries.push({
    "iso3":"ITA",
    "min":0.0,
    "max":0.0,
    "svg":"M 9.51 -40.92 l 0.05 1.77 -1.16 0.19 -0.22 -1.96 z M 13.72 -46.53 l -1.35 2.28 6.14 4.11 -1.59 -0.32 -0.86 2.53 -0.39 -2.11 -9.04 -5.08 1.74 -1.34 z M 15.53 -38.14 l -0.45 1.49 -2.65 -1.15 z",
    "name":"Italy"
});
countries.push({
    "iso3":"JPN",
    "min":0.0,
    "max":0.0,
    "svg":"M 131.21 -33.6 l 0.78 0.76 -1.32 1.83 -1.08 -2.36 z M 134.22 -34.34 l -0.04 1.1 -2.17 -0.1 z M 143.73 -44.12 l 2.09 0.75 -2.57 1.44 -2.77 -0.65 0.72 0.78 -1.16 0.35 1.66 -3.95 z M 139.87 -39.89 l 1.05 -1.64 1.15 1.98 -1.74 4.42 -3.48 0.05 -1.08 1.62 -0.44 -1.26 -4.45 0.78 2.21 -1.65 2.98 -0.07 0.68 -1.69 2.67 -0.81 z",
    "name":"Japan"
});
countries.push({
    "iso3":"JOR",
    "min":0.0,
    "max":0.0,
    "svg":"M 35.55 -32.39 l 3.24 -0.98 0.51 1.14 -2.3 0.73 1 1 -1.93 1.32 -1.11 -0.17 z",
    "name":"Jordan"
});
countries.push({
    "iso3":"KAZ",
    "min":0.0,
    "max":0.0,
    "svg":"M 65.24 -54.34 l 3.71 -1.1 4.49 2.01 3.37 -1.01 3.25 3.68 3.41 -0.22 3.88 1.9 -1.83 2.03 -2.49 -0.14 -0.47 2.07 -2.7 0.23 0.41 2.69 -6.23 -0.97 -2.78 0.43 -2.8 2.16 -3.55 -3.14 -2.91 0.25 -3.45 -2.09 -2.57 0.57 0 3.67 -1.83 -1.01 -1.73 0.6 0.29 -0.97 -2.49 -1.86 2.99 -0.76 -0.17 -1.55 -3.84 0.54 -2.72 -2.07 0.82 -1.88 1.47 0.36 1.98 -1.83 3.75 1.24 6.88 -0.26 -1.4 -1.17 2.11 -1.04 -1.1 -0.95 z",
    "name":"Kazakhstan"
});
countries.push({
    "iso3":"KEN",
    "min":0.0,
    "max":0.0,
    "svg":"M 35.94 -4.62 l 3.58 1.21 2.39 -0.57 -0.91 1.14 0.57 4.51 -2.16 2.96 -5.48 -3.63 1.09 -2.9 -1.01 -2.33 z",
    "name":"Kenya"
});
countries.push({
    "iso3":"PRK",
    "min":0.0,
    "max":0.0,
    "svg":"M 130.6 -42.42 l -3.09 2.68 0.71 1.36 -3.55 0.26 0.98 -0.51 -1.1 -1.62 5.35 -2.76 z",
    "name":"Korea, Democratic Peoples Republic of"
});
countries.push({
    "iso3":"KOR",
    "min":0.0,
    "max":0.0,
    "svg":"M 128.36 -38.63 l 1.1 3.08 -2.91 1.24 -0.02 -3.46 z",
    "name":"Korea, Republic of"
});
countries.push({
    "iso3":"KWT",
    "min":0.0,
    "max":0.0,
    "svg":"M 47.94 -30.02 l 0.47 1.47 -1.87 -0.56 z",
    "name":"Kuwait"
});
countries.push({
    "iso3":"KGZ",
    "min":0.0,
    "max":0.0,
    "svg":"M 80.23 -42.2 l -6.39 2.72 -4.59 -0.28 3.9 -1.04 -2.97 -0.74 1.24 -1.26 z",
    "name":"Kyrgyz Republic"
});
countries.push({
    "iso3":"LAO",
    "min":0.0,
    "max":0.0,
    "svg":"M 101.15 -21.57 l 0.97 -0.86 2.53 1.78 -0.76 1.34 3.58 3.23 0.01 1.44 -2.19 0.46 0.35 -1.49 -1.66 -2.66 -3.05 0.76 0.36 -2 -1.19 -0.78 z",
    "name":"Lao, People\u0027s Democratic Republic of"
});
countries.push({
    "iso3":"LVA",
    "min":0.0,
    "max":0.0,
    "svg":"M 27.37 -57.54 l 0.23 1.74 -6.63 -0.45 1.51 -1.5 z",
    "name":"Latvia"
});
countries.push({
    "iso3":"LBN",
    "min":0.0,
    "max":0.0,
    "svg":"M 35.97 -34.65 l 0.65 0.44 -1.52 1.11 z",
    "name":"Lebanon"
});
countries.push({
    "iso3":"LSO",
    "min":0.0,
    "max":0.0,
    "svg":"M 28.58 28.61 l 0.88 0.74 -1.72 1.25 -0.72 -0.97 z",
    "name":"Lesotho"
});
countries.push({
    "iso3":"LBR",
    "min":0.0,
    "max":0.0,
    "svg":"M -8.47 -7.56 l 0.94 3.21 -3.97 -2.57 1.32 -1.59 z",
    "name":"Liberia"
});
countries.push({
    "iso3":"LBY",
    "min":0.0,
    "max":0.0,
    "svg":"M 11.53 -33.17 l 7.48 2.9 2.63 -2.67 3.41 1.01 -0.03 11.93 -14.75 -4.61 -0.94 -5.52 z",
    "name":"Libya"
});
countries.push({
    "iso3":"LTU",
    "min":0.0,
    "max":0.0,
    "svg":"M 26.61 -55.67 l -0.83 1.51 -2.19 0.23 -2.54 -2.15 z",
    "name":"Lithuania"
});
countries.push({
    "iso3":"MKD",
    "min":0.0,
    "max":0.0,
    "svg":"M 20.59 -41.88 l 2.44 0.16 -2.05 0.87 z",
    "name":"Macedonia, FYR"
});
countries.push({
    "iso3":"MDG",
    "min":0.0,
    "max":0.0,
    "svg":"M 44.47 19.88 l -0.03 -3.67 3.55 -1.44 1.28 -2.82 1.21 3.46 -3.35 9.52 -1.6 0.64 -1.5 -0.56 -0.79 -2.72 z",
    "name":"Madagascar"
});
countries.push({
    "iso3":"MWI",
    "min":0.0,
    "max":0.0,
    "svg":"M 34.97 11.57 l 0.32 5.56 -0.77 -2.56 -1.84 -0.96 1.02 -3.05 -0.76 -1.16 1.39 0.34 z",
    "name":"Malawi"
});
countries.push({
    "iso3":"MYS",
    "min":0.0,
    "max":0.0,
    "svg":"M 102.1 -6.24 l 2.18 4.87 -3.57 -2.52 -0.51 -2.81 0.95 1.06 z M 111.57 -1 l -2.01 -0.91 1.82 0.56 0.07 -1.35 2.54 -1.9 1.37 0.28 1.4 -2.7 2.51 1.64 -3.39 1.01 -1.3 2.92 z",
    "name":"Malaysia"
});
countries.push({
    "iso3":"MLI",
    "min":0.0,
    "max":0.0,
    "svg":"M 4.25 -19.15 l -0.73 3.79 -7.48 1.86 -2.04 3.31 -1.97 0.03 -1.18 -2.32 -2.34 0.31 -0.22 -3.37 6.22 0.05 -1.08 -9.5 1.77 -0 z",
    "name":"Mali"
});
countries.push({
    "iso3":"MRT",
    "min":0.0,
    "max":0.0,
    "svg":"M -4.81 -25 l -1.77 0 1.08 9.5 -6 -0.14 -0.56 0.92 -2.28 -1.91 -2.18 0.58 -0.43 -5.28 3.95 -0 1 -4.66 3.33 -0 0 -1.29 z",
    "name":"Mauritania"
});
countries.push({
    "iso3":"MEX",
    "min":0.0,
    "max":0.0,
    "svg":"M -97.14 -25.97 l -0.53 4.29 3.13 3.51 3.06 -0.29 1.37 -2.71 3.29 -0.25 -1.03 3.22 -3.13 0.37 0.54 1.73 -1.29 0.01 -0.49 1.52 -2.15 -1.73 -3.39 0.33 -7.23 -3.38 -0.84 -3.32 -7.27 -8.57 -1.94 -0.73 5.62 8.5 -0.59 0.58 -4.99 -4.84 0.93 -0.8 -3.06 -4.02 10.73 0.79 3.02 2.72 1.97 -0.75 2.3 3.34 z",
    "name":"Mexico"
});
countries.push({
    "iso3":"MDA",
    "min":0.0,
    "max":0.0,
    "svg":"M 28.21 -45.45 l -1.51 -2.89 3.24 1.52 z",
    "name":"Moldova"
});
countries.push({
    "iso3":"MNG",
    "min":0.0,
    "max":0.0,
    "svg":"M 87.84 -49.17 l 4.48 -1.64 5.03 1.08 1.58 -2.41 9.64 2.81 7.68 -0.69 -0.65 2.11 2.94 -0.08 1.39 1.28 -7.95 1.62 -1.54 2.31 -5.43 1.19 -8.62 -1.15 -0.97 -1.56 -4.51 -0.96 0.02 -1.7 z",
    "name":"Mongolia"
});
countries.push({
    "iso3":"MNE",
    "min":0.0,
    "max":0.0,
    "svg":"M 19.23 -43.51 l 1.15 0.67 -1.01 0.99 -0.91 -0.67 z",
    "name":"Montenegro"
});
countries.push({
    "iso3":"MAR",
    "min":0.0,
    "max":0.0,
    "svg":"M -5.4 -35.92 l 3.64 1.16 0.58 2.64 -2.63 0.41 -4.85 2.99 0 1.04 -4.51 -0 2.95 -1.65 0.95 -3.23 z",
    "name":"Morocco"
});
countries.push({
    "iso3":"MOZ",
    "min":0.0,
    "max":0.0,
    "svg":"M 40.44 10.48 l 0.14 5.02 -5.95 4.12 0.87 4.48 -3.36 2.74 -0.84 -4.42 1.72 -2.47 -0.04 -3.23 -2.77 -1.73 4.17 -0.59 0.91 2.74 0.63 -2.24 -1.3 -3.31 z",
    "name":"Mozambique"
});
countries.push({
    "iso3":"MMR",
    "min":0.0,
    "max":0.0,
    "svg":"M 97.05 -27.75 l 0.66 -0.77 1.03 1.16 -1.18 3.43 1.34 -0.23 0.28 2 2 0.52 -3.81 3.06 2.3 6.75 -1.11 1.84 -0.81 -6.57 -0.86 -0.89 -1.45 1.72 -1.18 -0.23 -0.27 -3.5 -1.78 -1.76 2.94 -5.4 z",
    "name":"Myanmar"
});
countries.push({
    "iso3":"NAM",
    "min":0.0,
    "max":0.0,
    "svg":"M 25.26 17.8 l -4.27 0.52 -1 10.1 -3.64 0.15 -4.6 -11.31 z",
    "name":"Namibia"
});
countries.push({
    "iso3":"NPL",
    "min":0.0,
    "max":0.0,
    "svg":"M 81.03 -30.2 l 7.16 2.36 -0.1 1.42 -7.7 -2.19 z",
    "name":"Nepal"
});
countries.push({
    "iso3":"NLD",
    "min":0.0,
    "max":0.0,
    "svg":"M 4.25 -51.38 l -0.81 -0.15 2.43 -0.98 -1.14 -0.45 2.47 -0.29 -1.2 2.49 z",
    "name":"Netherlands, The"
});
countries.push({
    "iso3":"NZL",
    "min":0.0,
    "max":0.0,
    "svg":"M 173.77 39.38 l 1.12 -2.32 -2.15 -2.62 3.26 3.2 2.56 0.06 -3.24 3.92 z M 167.08 45.32 l 5.55 -4.81 1.69 0.49 -1.23 2.85 -3.39 2.7 -2.98 -0.35 z",
    "name":"New Zealand"
});
countries.push({
    "iso3":"NIC",
    "min":0.0,
    "max":0.0,
    "svg":"M -83.13 -14.99 l -0.55 4.21 -4.01 -2.13 z",
    "name":"Nicaragua"
});
countries.push({
    "iso3":"NER",
    "min":0.0,
    "max":0.0,
    "svg":"M 15 -23 l 1 2.65 -0.51 3.43 -1.86 3.2 -3.99 0.92 -4.75 -0.98 -1.28 2.09 -2.62 -1.35 -0.75 -1.95 3.65 -0.71 0.36 -3.44 7.74 -4.38 z",
    "name":"Niger"
});
countries.push({
    "iso3":"NGA",
    "min":0.0,
    "max":0.0,
    "svg":"M 14.07 -13.08 l 0.57 1.51 -2.78 4.49 -2.07 0.28 -1.5 2.25 -2.24 0.26 -1.52 -2.01 -1.81 -0.07 1.76 -7.32 z",
    "name":"Nigeria"
});
countries.push({
    "iso3":"NOR",
    "min":0.0,
    "max":0.0,
    "svg":"M 25.36 -70.61 l 5.72 0.32 -2.47 0.12 2.34 0.47 -1.89 0.66 -1.46 -1.04 -2.66 1.49 -3.61 -0.75 -4.59 1.43 -2.22 1.78 -0.52 2.11 -2.05 0.74 0.92 1.91 -1.23 2.46 -0.94 -1 -2.56 1.8 -2.62 -0.57 0.96 -0.89 -1.29 0.05 1.92 -0.99 -1.35 0.51 -0.73 -1.05 2.55 -0.43 -2.45 0.33 1.65 -0.73 -1.68 -0.31 5.83 -1.28 0.58 -0.55 -1.94 0.24 8.71 -5.72 z",
    "name":"Norway"
});
countries.push({
    "iso3":"OMN",
    "min":0.0,
    "max":0.0,
    "svg":"M 51.99 -19 l 3.01 -0.93 0.93 -5.04 3.91 2.49 -2.03 3.52 -2.77 1.96 -1.92 0.37 z",
    "name":"Oman"
});
countries.push({
    "iso3":"PAK",
    "min":0.0,
    "max":0.0,
    "svg":"M 74.57 -37.03 l 3.26 1.53 -3.89 0.87 1.44 2.42 -3.48 4.25 -2.38 0.96 1.59 2.58 -2.95 0.73 -1.79 -1.93 -4.64 0.56 1.61 -2.09 -2.46 -2.71 5.39 0.01 4.82 -4.2 0.16 -2.08 z",
    "name":"Pakistan"
});
countries.push({
    "iso3":"PAN",
    "min":0.0,
    "max":0.0,
    "svg":"M -82.56 -9.56 l 4.01 0.13 1.34 1.49 -0.69 0.7 -1.1 -1.9 -1.84 1.93 -2.05 -0.82 z",
    "name":"Panama"
});
countries.push({
    "iso3":"PNG",
    "min":0.0,
    "max":0.0,
    "svg":"M 152.24 4.21 l -1.77 2.07 -2.14 -0.58 z M 141.01 6.33 l 0.04 -3.74 6.44 3.39 -0.3 1.49 3.7 2.77 -2.93 -0.08 -3.43 -2.64 -2.38 0.72 1.23 0.79 -2.25 0.22 z",
    "name":"Papua New Guinea"
});
countries.push({
    "iso3":"PRY",
    "min":0.0,
    "max":0.0,
    "svg":"M -58.16 20.17 l 0.17 1.92 2.14 0.2 0.44 1.67 1.17 0.1 -1.49 3.38 -2.87 -0.12 0.84 -2.15 -4.88 -2.93 0.9 -2.59 z",
    "name":"Paraguay"
});
countries.push({
    "iso3":"PER",
    "min":0.0,
    "max":0.0,
    "svg":"M -69.96 4.24 l -2.9 0.89 -1.15 2.43 1.82 2.45 1.67 -0.57 -0.12 1.58 1.96 1.49 -0.15 3.83 -1.58 2.02 -5.53 -3.7 -5.42 -9.96 1.01 -1.31 1.3 1.62 3.83 -4.96 2.32 2.47 2.59 -0.01 z",
    "name":"Peru"
});
countries.push({
    "iso3":"PHL",
    "min":0.0,
    "max":0.0,
    "svg":"M 120.61 -14.52 l -0.86 -1.44 0.82 -2.53 1.65 -0.03 -0.49 4.35 2.19 0.38 0.17 1.24 z M 125.18 -12.56 l 0.57 1.55 -1.49 -1.54 z M 124.77 -8.97 l 0.73 -0.82 0.97 1.57 -1.06 2.66 -1.73 -2.25 -1.76 0.82 z M 123.21 -9.98 l -0.19 0.95 0.18 -1.97 z M 122.23 -11.8 l 0.89 0.63 -1.18 0.75 z",
    "name":"Philippines"
});
countries.push({
    "iso3":"POL",
    "min":0.0,
    "max":0.0,
    "svg":"M 19.63 -54.46 l 3.86 0.32 0.63 3.57 -1.23 1.56 -8.06 -1.86 -0.6 -3.06 z",
    "name":"Poland"
});
countries.push({
    "iso3":"PRT",
    "min":0.0,
    "max":0.0,
    "svg":"M -8.75 -41.95 l 2.55 0.37 -1.71 4.57 -1.09 -0.02 -0.49 -1.68 z",
    "name":"Portugal"
});
countries.push({
    "iso3":"ROM",
    "min":0.0,
    "max":0.0,
    "svg":"M 26.63 -48.26 l 1.54 2.79 1.48 0.14 -1.07 1.58 -5.69 -0.09 -2.63 -2.28 2.89 -1.99 z",
    "name":"Romania"
});
countries.push({
    "iso3":"RUS",
    "min":0.0,
    "max":0.0,
    "svg":"M 57.8 -81.53 l 0.77 0.12 -1.83 -0.04 z M 128.14 -72.58 l 1.42 0.36 -2.91 -0.21 z M 142.15 -73.89 l 1.35 0.66 -3.85 -0.17 z M 142.82 -54.3 l 1.92 5.65 -1.71 -0.51 0.57 2.77 -1.51 0.49 -0.45 -6.42 z M -178.57 -71.56 l 1.13 0.33 -2.56 0.23 z M 124.6 -73.73 l 1.8 0.22 -0.05 1.13 -3.92 -0.6 z M 147.02 -75.33 l 3.93 0.2 -4.88 -0.08 z M 20.98 -55.28 l 1.8 0.91 -2.99 -0.07 z M 139.19 -76.07 l 6.19 0.56 -8.52 0.16 z M 59.46 -70.28 l 1.09 0.47 -2.14 -0.45 z M 55.22 -74.17 l 5.82 -2.1 7.89 -0.51 -10.74 2.21 -1.44 1.33 -3.12 -0.51 z M 127.3 -73.52 l 1.82 0.42 -2.82 0.59 z M 101.48 -76.45 l 2.82 -1.28 3.2 0.81 -1.11 0.41 7.5 0.67 -8.68 3.08 5.7 -0.93 -0.71 -0.33 2.69 0.06 1.16 1.37 -0.57 -0.9 5.16 -0.07 8.58 2.18 -0.5 -0.99 0.94 0.04 3.47 1.62 1.59 -1.21 5.27 0.83 1.95 -0.38 -0.85 -0.75 1.51 -0.66 3.75 0.25 2.58 0.34 -2 0.61 3.35 -0.63 3.96 1.44 6.93 0.03 1.76 2.33 1.41 -1.14 8.29 0.91 -0.16 -1.38 5.67 0.24 3.88 0.91 0 3.91 -5.55 0.38 3.84 0.33 0.82 2.06 -2.13 -0.57 -6.73 2.95 -6.6 -0.14 -1.7 1.98 1.27 0.23 0.14 1.64 -6.69 5.32 -1.11 -4.42 1.22 -2.43 8.87 -4.7 -2.37 -0.1 -3.12 1.95 0.22 -1.36 -2.87 0.14 -3.26 1.92 0.51 0.75 -12.5 0.01 -7.09 4.25 6.26 1.57 -1.24 4.84 -5.04 4.95 -4.43 1.21 0.26 -2.54 2.16 -0.29 1.62 -3.14 -3.75 0.58 -0.32 -1.17 -3.16 -0.95 -1.42 -2.95 -2.48 -0.79 -3.58 0.78 0.68 0.79 -2.84 2.46 -3.55 -0.76 -5.76 0.95 -9.64 -2.81 -1.58 2.41 -5.03 -1.08 -4.91 1.74 -3.94 -1.91 -3.41 0.22 -3.25 -3.68 -3.37 1.01 -4.49 -2.01 -7.94 1.5 1.1 0.95 -2.11 1.04 1.38 1.17 -6.86 0.25 -3.75 -1.24 -1.98 1.83 -1.3 -0.48 -0.99 2 2.8 2.16 -2.62 1.74 1.9 2.68 -1.01 0.62 -10.95 -3.92 1.94 -0.92 -0.83 -0.62 1.54 -0.35 -1.06 -0.32 1.56 -0.5 0.36 -1.76 -4.54 -0.77 -1.77 -1.99 -2.05 0.26 -0.52 -0.92 1.47 -0.44 -5.39 -4.13 0.66 -2.17 2.24 -0.21 -2.44 -0.58 3.77 -2.36 -1.58 -0.83 -0.92 -3.17 0.95 -0.79 -1.57 -0.84 1.65 -1.13 10.88 1.95 0.23 0.88 -2.61 0.79 -6.75 -1.1 5.56 3.35 -0.98 -1.13 4.06 0.4 -0.75 -1.02 2.42 -0.97 2 0.65 -0.86 -2.81 3.22 0.55 -1.61 0.77 1.47 0.62 7.39 -2.23 0.78 -0.03 -1.35 0.73 6.63 -0.1 1.02 -1.49 7.57 1.64 0.78 -0.73 -2.42 -0.62 -0.17 -1.46 2.71 -1.9 2.23 0.04 1.26 0.2 -1.02 1.25 1.84 3 -2.1 1.82 -2.58 -0.16 3.03 0.59 2.74 -1.47 -0.1 -1.08 4.4 1.2 -1.57 -0.19 0.18 -1.15 -3.9 -0.27 0.57 -1.48 -1.3 -0.76 1.81 -1.42 0.44 1.47 1.65 0.29 2.19 0.06 -3.01 -0.92 7.16 0.21 -1.18 1.16 0.99 0.5 0.55 -1.56 -3.11 -1.95 6.53 -0.3 -1.26 0.4 1 0.48 -0.94 -0.46 1.82 -0.44 -1.88 -0.74 1.99 -0.39 -0.79 -0.12 11.81 -1.12 1.39 1.1 -1.35 -1.34 z M 53.2 -73.06 l 3.26 -0.16 -1.25 1.29 2.41 1.2 -6.21 -1 z M 102.97 -79.33 l 2.44 0.76 -6.07 0.55 z M -173.47 -64.39 l -5.09 -1.12 0.03 -0.89 -1.47 1.33 0 -3.91 4.54 1.27 1 1.4 0.79 -0.83 3.97 1.01 -3.1 0.45 0.44 1.25 z M 97.61 -80.17 l 2.4 0.34 -0.97 0.54 0.9 0.33 -7.09 -0.6 z M 57.97 -80.49 l 1.31 0.16 -2.33 -0.14 z M 47.9 -80.8 l 0.86 0.18 -3.9 0.01 z M 50.13 -80.85 l 1.61 0.13 -5.12 0.42 z M 95.96 -81.22 l 2.01 0.51 -6.54 0.4 z",
    "name":"Russia"
});
countries.push({
    "iso3":"RWA",
    "min":0.0,
    "max":0.0,
    "svg":"M 30.48 1.06 l 0.35 1.29 -1.93 0.31 z",
    "name":"Rwanda"
});
countries.push({
    "iso3":"SAU",
    "min":0.0,
    "max":0.0,
    "svg":"M 43.19 -16.68 l -8.62 -11.41 0.39 -1.26 3.04 -1.14 -1 -1 3.41 -0.44 4.31 2.75 3.7 0.66 4.16 5.58 3.09 0.97 -0.68 2.06 -5.87 1.29 -1.94 1.69 -3.74 -0.59 z",
    "name":"Saudi Arabia"
});
countries.push({
    "iso3":"SEN",
    "min":0.0,
    "max":0.0,
    "svg":"M -12.24 -14.76 l 0.87 2.36 -5.34 0.09 2.91 -1.12 -3.73 -1.31 2.55 -1.94 z",
    "name":"Senegal"
});
countries.push({
    "iso3":"SRB",
    "min":0.0,
    "max":0.0,
    "svg":"M 21.59 -42.26 l -2.35 -1.21 -0.42 -2.45 3.95 1.36 0.23 1.42 z",
    "name":"Serbia"
});
countries.push({
    "iso3":"SLE",
    "min":0.0,
    "max":0.0,
    "svg":"M -10.27 -8.49 l -1.23 1.56 -1.8 -2.11 2.08 -0.97 z",
    "name":"Sierra Leone"
});
countries.push({
    "iso3":"SVK",
    "min":0.0,
    "max":0.0,
    "svg":"M 22.56 -49.08 l -3.89 1.32 -1.82 -0.6 1.72 -1.13 z",
    "name":"Slovak Republic"
});
countries.push({
    "iso3":"SVN",
    "min":0.0,
    "max":0.0,
    "svg":"M 16.61 -46.48 l -1.43 1.05 -1.79 -0.87 z",
    "name":"Slovenia"
});
countries.push({
    "iso3":"SOM",
    "min":0.0,
    "max":0.0,
    "svg":"M 43.25 -11.47 l 1.69 1.05 5.83 -1.56 0.64 1.53 -3.46 5.99 -6.39 6.14 -0.57 -4.51 7 -5.17 -4.55 -1.41 z",
    "name":"Somalia"
});
countries.push({
    "iso3":"ZAF",
    "min":0.0,
    "max":0.0,
    "svg":"M 31.3 22.41 l 0.71 3.2 -1.19 1.19 2.05 0.2 -2.87 4.3 -4.3 2.73 -7.28 0.29 -1.94 -5.71 3.51 -0.19 0 -3.65 0.64 2.06 1.03 0.04 1.35 -1.56 2.49 0.38 3.52 -3.45 z M 28.58 28.61 l -1.56 1.02 0.72 0.97 1.72 -1.25 z",
    "name":"South Africa"
});
countries.push({
    "iso3":"ESP",
    "min":0.0,
    "max":0.0,
    "svg":"M 1.45 -42.6 l 1.76 0.71 -2.5 1.1 -1.43 3.19 -4.89 1.6 -1.91 -1.55 1.33 -4.03 -2.68 -0.3 -0.42 -1.04 1.44 -0.84 z",
    "name":"Spain"
});
countries.push({
    "iso3":"LKA",
    "min":0.0,
    "max":0.0,
    "svg":"M 80.27 -9.77 l 1.61 2.49 -1.29 1.37 z",
    "name":"Sri Lanka"
});
countries.push({
    "iso3":"SDN",
    "min":0.0,
    "max":0.0,
    "svg":"M 36.89 -22 l 1.72 3.99 -1.61 0.95 -0.85 4.35 -3.15 4.79 2.95 3.3 -3.75 1.11 -3.99 -0.84 -4.67 -4.36 -1.7 -4.09 2.17 -2.91 0.01 -4.3 0.99 -0 0 -2 z",
    "name":"Sudan"
});
countries.push({
    "iso3":"SUR",
    "min":0.0,
    "max":0.0,
    "svg":"M -57.25 -5.49 l 3.22 -0.33 -0.4 3.38 -2.26 0.41 -1.39 -2.13 z",
    "name":"Suriname"
});
countries.push({
    "iso3":"SJM",
    "min":0.0,
    "max":0.0,
    "svg":"M 17.73 -79.55 l 3.56 0.89 -4.63 2.1 -2.4 -0.86 2.46 -1.24 -3.63 0.44 -2.08 -1.21 z M 20.71 -80.29 l 6.3 0.21 -3.27 0.91 -5.82 -0.94 z",
    "name":"Svalbard (Nor.)"
});
countries.push({
    "iso3":"SWZ",
    "min":0.0,
    "max":0.0,
    "svg":"M 32.13 26.84 l -1.33 -0.37 0.62 -0.74 z",
    "name":"Swaziland"
});
countries.push({
    "iso3":"SWE",
    "min":0.0,
    "max":0.0,
    "svg":"M 24.17 -65.81 l -2.4 0.1 -0.99 1.85 -3.07 0.87 -0.55 2.05 1.92 1.2 -3.04 0.25 2.61 0.17 -2.45 0.69 -0.33 2.54 -2.88 0.69 -1.87 -3.6 1.74 -2.36 -0.92 -1.91 2.21 -0.91 0.36 -1.94 6.07 -2.94 3.08 1.11 z",
    "name":"Sweden"
});
countries.push({
    "iso3":"CHE",
    "min":0.0,
    "max":0.0,
    "svg":"M 9.57 -47.54 l 0.57 1.31 -4.17 0.02 1.02 -1.29 z",
    "name":"Switzerland"
});
countries.push({
    "iso3":"SYR",
    "min":0.0,
    "max":0.0,
    "svg":"M 42.36 -37.11 l -1.35 2.69 -5.35 1.73 1.01 -4.15 z",
    "name":"Syrian Arab Republic"
});
countries.push({
    "iso3":"TWN",
    "min":0.0,
    "max":0.0,
    "svg":"M 121.73 -25.14 l -1.02 3.21 -0.6 -1.7 z",
    "name":"Taiwan"
});
countries.push({
    "iso3":"TJK",
    "min":0.0,
    "max":0.0,
    "svg":"M 73.66 -39.45 l 1.44 2.13 -3.42 0.65 -0.72 -1.8 -3.19 1.36 -0.33 -2.37 1.96 -1.32 1.59 0.57 -1.66 0.71 z",
    "name":"Tajikistan"
});
countries.push({
    "iso3":"TZA",
    "min":0.0,
    "max":0.0,
    "svg":"M 33.92 1 l 5.28 3.67 0.82 6.11 -5.05 0.79 -0.64 -1.83 -3.35 -1.19 -1.27 -1.96 0.76 -5.51 z",
    "name":"Tanzania"
});
countries.push({
    "iso3":"THA",
    "min":0.0,
    "max":0.0,
    "svg":"M 105.21 -14.35 l -2.83 0.78 0.54 1.94 -2.96 -1.66 -0.81 3.53 2.95 3.53 -0.95 0.6 -2.94 -2.89 1.46 -3.3 -1.79 -7.74 2.47 -0.81 0.82 2.92 2.82 -0.87 1.66 2.66 z",
    "name":"Thailand"
});
countries.push({
    "iso3":"TGO",
    "min":0.0,
    "max":0.0,
    "svg":"M 0.92 -11 l 0.28 4.9 -1.35 -5.04 z",
    "name":"Togo"
});
countries.push({
    "iso3":"TUN",
    "min":0.0,
    "max":0.0,
    "svg":"M 8.62 -36.94 l 2.48 0.04 -1.1 2.74 1.57 1.7 -2.04 2.24 -2.04 -3.65 z",
    "name":"Tunisia"
});
countries.push({
    "iso3":"TUR",
    "min":0.0,
    "max":0.0,
    "svg":"M 28.01 -41.98 l 1.02 0.93 -2.87 1 0.22 -1.77 z M 43.46 -41.11 l 1.36 1.47 -0.03 2.5 -8.13 0.32 -0.5 1.02 -0.14 -1.1 -3.24 0.89 -5.4 -0.66 0.95 -0.35 -1.07 0.07 -0.98 -1.3 0.68 -1.29 -0.88 0.07 7.27 -2.54 z",
    "name":"Turkey"
});
countries.push({
    "iso3":"TKM",
    "min":0.0,
    "max":0.0,
    "svg":"M 56 -41.33 l 2.61 -1.47 7.92 5.43 -4.23 2.22 -5.1 -3.14 -3.3 0.93 -1.18 -3.1 2.01 -0.66 -2.29 -0.64 1.73 -0.6 z",
    "name":"Turkmenistan"
});
countries.push({
    "iso3":"UGA",
    "min":0.0,
    "max":0.0,
    "svg":"M 34 -4.22 l 1.01 2.33 -1.09 2.9 -4.32 0.38 1.36 -5.06 z",
    "name":"Uganda"
});
countries.push({
    "iso3":"UKR",
    "min":0.0,
    "max":0.0,
    "svg":"M 31.78 -52.11 l 2.05 -0.26 1.77 1.99 4.54 0.77 -0.34 1.73 -6.13 1.65 2.97 0.84 -2.71 1 -1.45 -1.01 1.13 -0.75 -1.82 -0.14 0.85 -0.36 -0.89 -0.61 -3.05 2.03 0.28 -1.25 1.15 0.06 -0.98 -1.58 -6.99 -0.43 1.96 -2.16 -0.46 -1.07 6.9 0.38 z",
    "name":"Ukraine"
});
countries.push({
    "iso3":"ARE",
    "min":0.0,
    "max":0.0,
    "svg":"M 56.08 -26.07 l -0.88 3.36 -3.62 -1.41 2.8 -0.13 z",
    "name":"United Arab Emirates"
});
countries.push({
    "iso3":"GBR",
    "min":0.0,
    "max":0.0,
    "svg":"M -6.27 -54.1 l -1.9 -0.36 2.03 -0.76 0.71 0.73 z M -5.07 -58.26 l 3.21 0.64 -1.86 1.59 5.4 3.28 -1.29 1.3 1 0.29 -7.11 1.09 3.34 -1.7 -2.87 0.03 2.54 -1.62 -0.32 -1.62 -2.14 0.08 0.25 -1.38 -0.87 0.96 z",
    "name":"United Kingdom"
});
countries.push({
    "iso3":"USA",
    "min":0.0,
    "max":0.0,
    "svg":"M -123.9 -46.44 l -0.81 -1.95 1.96 0.24 -0.13 1.09 0.12 -1.94 27.61 -0.38 11.03 2.85 1.58 1.17 -0.15 3.67 7.7 -3.3 4.12 -0.26 1.64 -2.23 1.44 0.4 0.82 2.24 -3.2 1.05 -0.87 1.48 1.1 0.63 -4.02 0.37 -2.01 4.15 0.13 -2.42 -0.48 1.52 -0.75 -0.86 0.79 1.82 -0.96 -0.21 1.7 1.49 -1.21 -0.43 1.03 0.41 -1.33 0.29 0.55 0.79 -4.33 2.22 -0.7 1.67 1.51 4.22 -1.06 1.52 -2.92 -4.98 -6.23 -0.28 0.83 1.45 -5.66 -0.79 -2.71 2.27 0.35 1.61 -3.99 -3.93 -1.97 0.75 -3.02 -2.72 -10.73 -0.79 -3.48 -2.02 -1.88 -2.97 1.06 -0.48 -2.27 -0.92 z M -153.12 -57.95 l 0.96 0.34 -2.65 0.32 z M -161.64 -70.25 l 5.04 -1.1 15.59 1.71 0.01 9.34 3.52 1.4 2 -0.9 5.48 4.52 -2.17 -0.3 -3.18 -3.88 0.26 1.23 -1.98 -0.83 0.41 0.85 -2.84 -1.82 -8.22 -1.24 -0.72 1.33 -3.54 0.67 2.55 -2.23 -3.66 1.21 -1.18 1.17 1 0.27 -5.24 2.87 -4.85 1.18 5.95 -2.68 0.62 -1.66 -5.38 0.5 0.29 -2.05 -2.19 0.88 -1.36 -0.73 1.87 -0.34 -2.64 -0.69 1.8 -1.62 3.25 -0.3 0.37 -1.21 -5.34 0.14 -2.01 -1.07 7.89 -0.75 -6.59 -1.95 z M -155.01 -19.33 l -0.85 0.3 -0.01 -1.23 z M -135.74 -58.26 l 0.89 0.8 -1.55 -0.36 z",
    "name":"United States"
});
countries.push({
    "iso3":"URY",
    "min":0.0,
    "max":0.0,
    "svg":"M -57.61 30.18 l 4.51 2.54 -0.74 1.7 -4.56 -0.49 z",
    "name":"Uruguay"
});
countries.push({
    "iso3":"UZB",
    "min":0.0,
    "max":0.0,
    "svg":"M 70.97 -42.25 l -0.79 0.71 2.99 0.71 -3.8 0.05 -1.92 1.3 0.94 1.29 -1.14 1.01 -8.63 -5.61 -2.61 1.47 -0 -3.67 2.57 -0.57 3.45 2.09 2.91 -0.25 3.52 3.14 z",
    "name":"Uzbekistan"
});
countries.push({
    "iso3":"VEN",
    "min":0.0,
    "max":0.0,
    "svg":"M -71.32 -11.85 l -0.27 2.81 1.58 -3.16 1.9 1.71 6.23 -0.24 -1.14 0.63 3.21 1.81 -1.59 2.34 0.8 1.02 -2.29 1.36 -1.9 -0.73 1.38 2.14 -2.94 1.38 -1.49 -2.06 0.37 -3.37 -4.54 -0.82 -1.39 -2.16 z",
    "name":"Venezuela"
});
countries.push({
    "iso3":"VNM",
    "min":0.0,
    "max":0.0,
    "svg":"M 102.14 -22.4 l 3.22 -0.93 2.71 1.84 -2.45 2.51 3.22 3.56 0.44 3.53 -4.41 3.33 -0.42 -1.86 3.1 -1.94 0.15 -2.91 -3.82 -4.04 0.76 -1.34 z",
    "name":"Vietnam"
});
countries.push({
    "iso3":"YEM",
    "min":0.0,
    "max":0.0,
    "svg":"M 50.12 -18.97 l 1.88 -0.04 1.1 2.36 -0.91 1.04 -8.73 2.92 -0.2 -4.73 3.93 0.47 z",
    "name":"Yemen, Republic of"
});
countries.push({
    "iso3":"ZMB",
    "min":0.0,
    "max":0.0,
    "svg":"M 32.94 9.41 l 0.28 4.61 -6.52 4.06 -4.56 -1.59 -0.13 -3.48 2.02 -0.01 -0.03 -2.13 5.82 2.58 -1.44 -1.93 0.54 -3.04 z",
    "name":"Zambia"
});
countries.push({
    "iso3":"ZWE",
    "min":0.0,
    "max":0.0,
    "svg":"M 30.42 15.63 l 2.57 1.08 0.04 3.23 -1.72 2.47 -3.28 -0.85 -2.78 -3.66 z",
    "name":"Zimbabwe"
});




var raphs = {};
var clicked = null;
var layers;
function testCountriesSvg(){

    var paths = [];
    //    var colors = ["#BDD7E7", "#6BAED6", "#2171B5" ]


    var paper = Raphael(document.getElementById("testmap"));
    layers = paper.set();
    for (var p in countries){
        var raphObj = paper.path(countries[p].svg);
        raphs[countries[p].iso3] = raphObj;
        raphObj.attr({
            'cursor':'pointer',
            'stroke':"#bababa",
            'stroke-width':1.0,
            'fill':'#efefef',
            'title': countries[p].name
        });
        
        addClickEvent(countries[p].iso3);
        //addOutEvent(countries[p].iso3);
        //addOverEvent(countries[p].iso3);
        
        //        raphObj.attr({
        //            'fill':colors[p]
        //
        //                        });
        layers.push(raphObj);
    }
    var x = layers.getBBox().x;
    var y = layers.getBBox().y;
    layers.scale(1.45,1.45,x/2.0,y/2.0);
    layers.translate((-35 - x), -4 - y);

}




function addOverEvent( code){
    raphs[code].mouseover(function(event){
        this.toFront();
        this.attr({
            'fill':'#efefef'

        });
        
    });
}

function addClickEvent( code){
    raphs[code].click(function(event){
        if(clicked != null){
            raphs[clicked].attr({
                'stroke':'#bababa',
                'stroke-width':1,
                'fill':'#efefef'
            });
        }
        //        var x = layers.getBBox().x
        //        var y = layers.getBBox().y
        //        x = x-((x-event.x)/2);
        //        y = y+((y-event.y)/2);

        //        layers.scale(1.1,1.1,x ,y);
        chosenIso = code;
        this.toFront();
        this.attr({
            'stroke':"#808080",
            'stroke-width':2,
            'fill':'#adadad'
        });

        clicked = code;
        

    });
}

function addOutEvent(code){

    raphs[code].mouseout(function(event){        
        this.attr({
            'stroke': '#303030'
        });
        this.attr({
            'stroke-width': 1
        });
     
    });
}