/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resources;

import com.google.gson.Gson;
import domain.DerivativeStats;
import domain.web.ShapeSvg;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import service.CountryService;
import svg.SVGMapService;


/**
 *
 * @author wb385924
 */
@Path("/mapstat")
public class DerivedMapStatResource {

    private static final Logger log = Logger.getLogger(DerivedMapStatResource.class.getName());
    
    private SimpleDateFormat sdf = new SimpleDateFormat();

   
    @GET
    @Path("/country/{gcm}/{scenario}/{stat}/{timePeriod}/{iso3}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMonthlyStats(
            @PathParam("gcm") String gcmName,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("iso3") String iso3
            ) {
        DerivativeStats dstats = DerivativeStats.getInstance();
        DerivativeStats.gcm gcm = dstats.getGcm(gcmName);
        DerivativeStats.scenario scenario = dstats.getScenario(scenarioName);
        DerivativeStats.climatestat stat = dstats.getClimateStat(statName);

        int areaId = CountryService.get().getId(iso3);
        List<ShapeSvg> classes = SVGMapService.get().getSVGMapAllGCMS(6,areaId, DerivativeStats.time_period.mid_century, DerivativeStats.stat_type.mean, stat, DerivativeStats.temporal_aggregation.monthly, scenario, gcm, 1, 1);
        Collections.sort(classes);
        
        Gson gson = new Gson();
        return gson.toJson(classes);
        
        //return dao.getRainData(id);
    }


}
