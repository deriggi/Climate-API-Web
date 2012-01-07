/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var chart = null;
var chosenCountry;

var stats = {}
stats['precip'] = 'precipitation';
stats['cooling days'] = 'cooling_days';
stats['wet days'] = 'wet_days';
stats['max temp'] = 'txx';
stats['min temp'] = 'tnn';
stats['average max temp'] = 'tasmax';
stats['average min temp'] = 'tasmin';


var dates = [];
var months = [];
months.push('Jan');
months.push('Feb');
months.push('Mar');
months.push('Apr');
months.push('May');
months.push('Jun');
months.push('Jul');
months.push('Aug');
months.push('Sep');
months.push('Oct');
months.push('Nov');
months.push('Dec');

for(var year = 1961; year < 2000; year++ ){

    for( var month = 0; month < 12; month++){
        dates.push({
            'year':year,
            'month':months[month],
            'month_num':month+1
        });
    }

}

var countries = [];


//$(function() {
//    $( "#slider-range" ).slider({
//        range: true,
//        min: 0,
//        max: dates.length,
//        //values: [ 75, 300 ],
//        slide: function( event, ui ) {
//            $( "#amount" ).val(  dates[ui.values[ 0 ]].month + ' - ' + dates[ui.values[ 0 ]].year + ' ' + dates[ui.values[ 1 ]].month + ' - ' + dates[ui.values[ 1 ]].year );
//        }
//    });
//
//});



$(document).ready(function(){

    for(var i in dates){
        $('#log').append('<p>' + dates[i].year + ' - ' + dates[i].month);
    }
    getCountries();
    getMonthlyAverage('precipitation', 'MOZ');
//    $('#chartbutton').button().click(function(){
//        getMonthlyAverage($('#variable').val(),$('#countries').val())
//    });

    $('#variable').change(function(){
       chart.destroy();
       chart = null;
        getMonthlyAverage($('#variable').val(),$('#countries').val())
    });

    $('#charttype').change(function(){
       chart.destroy();
       chart = null;
        getMonthlyAverage($('#variable').val(),$('#countries').val())
    });
    setupAutoComplete();
   
// get precip data for one country for 10 years

});

function setupAutoComplete(){

//    for(var i in dataSource){
//        dataSource[i]
//    }
    $( "#countries" ).autocomplete({
        minLength: 0,
        source: "/climateweb/rest/country/search/",
        select:function(event,ui){
            getMonthlyAverage($('#variable').val(),ui.item.value);

            
                        //$( "#yearval" ).val(ui.item.value - 1961);
            //var y = ui.item.value - 1961

        }
    });
}
function getCountries(){

    $.get("/climateweb/rest/country",
        function(data){
            if(data != null){
                countries = data.country;
                //setupAutoComplete(countries)
            }
        }
        );
}

function getMonthlyAverage(variable,iso3){

    $.get("/climateweb/rest/precipitation/monthavg/1961-1/2000-1/"+variable+"/"+iso3,
        function(data){
            if(data != null){
                var prefix = 'm_';
                //                alert(data["m_0"].avg);
                var series = {
                    'name':iso3,
                    data:[]
                }
                for(var i =0; i < 12; i++){
                    var key = prefix + (i);
                    series.data.push(data[key].avg)
                    $('#log').append('<p>' + data[key].avg + '</p>');
                }
                if(chart == null){
                    doChart(series,$('#charttype').val())
                }else{
                    chart.addSeries(series);
                }
                
            }
        }
        );
}
function getStatisticalData(variable, iso3){
    $.get("/climateweb/rest/precipitation/country/1961-1/2000-1/"+variable+".json/"+iso3,
        function(data){
            if(data != null){

                var statData = data;
                for(var i in statData){

                    //                     $('#log').append('<p>' + countries[i].iso3 + '</p>');
                    }
            }
        }
        );
}






function doChart(seriesData, chartType) {
    chart = new Highcharts.Chart({
        chart: {
            renderTo: 'chartcontainer',
            defaultSeriesType: chartType,
            marginRight: 50,
            marginBottom: 25,
            height:320,
            width:600
        },
        credits:{
            enabled:false
        },
        title: {
            text: 'Monthly Average 1961 - 1999',
            x: -20 //center
        },
        subtitle: {
            text: '',
            x: -20
        },
        xAxis: {
            categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
            'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
        },
        yAxis: {
            title: {
                text: ''
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            formatter: function() {
                return '<b>'+ this.series.name +'</b><br/>'+
                this.x +': '+ this.y;
            }
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'middle',
            x: 20,
//            y: 100,
            borderWidth: 0
        },
        series: [seriesData]
    });


}