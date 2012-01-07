/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//var cnrmPaper, gfdlcm20Paper, gfdlcm21Paper, miroc3_2_medres;
var papers = {};
var colors = ["#C6DBEF", "#9ECAE1", "#6BAED6", "#4292C6", "#2171B5" ]


function getSvgs(iso3,month,gcm,scenario, climatestat){
    clearRaphael(gcm)
    $.get("/climateweb/rest/mapstat/country/"+gcm+"/"+scenario+"/"+climatestat+"/mid_century/"+iso3,
        function(data){
            
            var count = 0;
            var canvas = papers[gcm];
            var layers = canvas.set();
            for(var i in data){
                 
                if(data[i] && data[i].svg != null){
                   
                    var raph = canvas.path(data[i].svg);
                    layers.push(raph);
                    raph.attr({
                        'stroke':colors[count]
                    });
                    raph.attr({
                        'fill':colors[count]
                    });
                }
                count++;
            }
            var x = layers.getBBox().x;
            var y = layers.getBBox().y;
            layers.translate((15 - x), 1 - y);
        });
}



function drawMapsFromForm(){
    var iso = $('#countrySelect').val();
    var climVar = $('#varSelect').val();
    var scenario = $('#scenarioSelect').val();

    for(var i in papers){
        getSvgs(iso, 1, i, scenario, climVar);
       
    }
}

function initRaphaels(){

    papers["cnrm_cm3"] = Raphael(document.getElementById("cnrm_cm3"));
    
    papers["gfdl_cm2_0"] = Raphael(document.getElementById("gfdl_cm2_0"));
    
    papers["gfdl_cm2_1"] = Raphael(document.getElementById("gfdl_cm2_1"));

    papers["miroc3_2_medres"] = Raphael(document.getElementById("miroc3_2_medres"));
    

}

function clearRaphaels(){
    for(var i in papers){
        papers[i].clear();
    }
}

function clearRaphael(gcm){
    papers[gcm].clear();
}




