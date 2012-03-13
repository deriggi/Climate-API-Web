/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resources.projectstatus;

import com.google.gson.Gson;
import cru.precip.moneymaker.StatusCache;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Johnny
 */
@Path("/projectstatus/")
public class ProjectStatusResource {
    
    @GET
    @Path("/percentcomplete/{cachekey}/")
    @Produces(MediaType.APPLICATION_JSON)
    public String getProjectPercentComplete(@PathParam("cachekey") String cacheKey){
        float percent = StatusCache.getPercentComplete(cacheKey);
        
        percent = Math.round(percent*10000)/10000.0f;
        
        return new Gson().toJson(percent*100);
    }
    
    @GET
    @Path("/lastfile/{cachekey}/")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLastFileProcessed(@PathParam("cachekey") String cacheKey){
        return new Gson().toJson(StatusCache.getLastFile(cacheKey));
    }
    
    @GET
    @Path("/restingplace/{cachekey}/")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFinalRestingPlace(@PathParam("cachekey") String cacheKey){
        return new Gson().toJson(StatusCache.getFinalRestingPlace(cacheKey));
    }
  
    
    public static void main(String[] args){
        float p = .0134f;
        System.out.println(Math.round(p*1000)/1000.0);
    }
}
