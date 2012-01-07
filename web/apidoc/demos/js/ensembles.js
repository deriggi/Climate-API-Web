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
var paper;
var colors = ["#C6DBEF", "#9ECAE1", "#6BAED6", "#4292C6", "#2171B5" ];

function drawRegionsMap() {
   
    
  

}



function getOldData( scenario, climvar, iso3){
    var requestPath = "/climateweb/rest/ensemble/cd18/a1b/mid_century/"+$('#isoselect').val();
   
    
    $.get(requestPath,function(data){
        if(data){

            paper.clear();
            var layers = paper.set();
            for(var i in data){
//                alert(data[1].svg);
                var ro = paper.path(data[i].svg);
                ro.attr('fill',colors[i]);
                layers.push(ro);
                
            }
            var x = layers.getBBox().x;
            var y = layers.getBBox().y;
            layers.scale(15,15)
            layers.translate((20 - x), 3 - y);
        }
    });
}

$(document).ready(function(){
    paper = Raphael(document.getElementById("testmap"));

    
    $('#showdata').click(function(){
//        var climvar = $('#varSelect').val();
//        var scenario =  $('#scenarioSelect').val();
//        var years = $('#yearsSelect').val();
       
        
//        var yearParts = years.split('-');
           
        getOldData( );
    })
   
})


