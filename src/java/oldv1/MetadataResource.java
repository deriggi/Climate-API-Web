/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oldv1;

/**
 *
 * @author wb385924
 */
import com.google.gson.Gson;
import dao.country.CountryDao;
import dao.metadata.P_AreaMetadataDao;
import domain.Country;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import service.CountryService;
import shapefileloader.gcm.P_GcmConfig;
import shapefileloader.gcm.P_GcmStatsProperties;

// The Java class will be hosted at the URI path "/helloworld"
@Path("/datasets")
public class MetadataResource {

    // The Java method will process HTTP GET requests
    @GET
    @Path("/change/{where : \\w{3}}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getChangeDatasets(@PathParam("where") String where, @Context HttpServletRequest request) {
        List<String> urls = getDataSetsDelegate(where, request, P_GcmStatsProperties.stat_type.anom);
        
        Gson gson = new Gson();
        return gson.toJson(urls);
    }


    private  List<String> getDataSetsDelegate(String where, HttpServletRequest request, P_GcmStatsProperties.stat_type statType){
        P_AreaMetadataDao dao = P_AreaMetadataDao.get();
        int id = CountryService.get().getId(where);
        List<P_GcmConfig> configs = dao.getAreaGcmConfigOptions(id, statType.toString());
        List<String> urls = new ArrayList<String>();
        String preAmble = getPreamble(request,statType);
        String forwardSlash = "/";
        for (P_GcmConfig config : configs) {
            StringBuilder sb = new StringBuilder();
            sb.append(preAmble);
            sb.append(forwardSlash);
            sb.append(config.toUrlPart());
            sb.append(where);
            urls.add(sb.toString());
        }
        return urls;
    }

    @GET
    @Path("/mean/{where : \\w{3}}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMeanDatasets(@PathParam("where") String where, @Context HttpServletRequest request) {
        List<String> urls = getDataSetsDelegate(where, request, P_GcmStatsProperties.stat_type.mean);

        Gson gson = new Gson();
        return gson.toJson(urls);
    }


    @GET
    @Path("/mean/{longitude : -?\\d{1,3}.?\\d*},{latitude : -?\\d{1,3}.?\\d*}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getDatasets(@PathParam("longitude") double longitude, @PathParam("latitude") double latitude,  @Context HttpServletRequest request) {
//        int areaId = GeoDao.getIdOfRegionContainingPoint(c, latitude, longitude, "boundary", "shape", "area_id");
        Country country = CountryDao.get().getCountryFromPoint(longitude, latitude);
        List<String> urls = new ArrayList<String>(0);
        if(country != null){
            urls = getDataSetsDelegate(country.getIso3(), request, P_GcmStatsProperties.stat_type.mean);
        }

        Gson gson = new Gson();
        return gson.toJson(urls);
    }




    private String getPreamble(HttpServletRequest request, P_GcmStatsProperties.stat_type type) {
        String forwardSlash = "/";
        StringBuilder sb = new StringBuilder();
        sb.append(request.getScheme());
        sb.append("://");
        sb.append(request.getServerName());
        sb.append(":");
        sb.append(request.getServerPort());
        sb.append(request.getContextPath());
        sb.append(forwardSlash);
        sb.append(getMonthlyRestPart(type));
        return sb.toString();
    }

    private String getMonthlyRestPart(P_GcmStatsProperties.stat_type statType){
        if(statType.equals(P_GcmStatsProperties.stat_type.anom)){
            return "rest/manom";
        }else if(statType.equals(P_GcmStatsProperties.stat_type.mean)){
            return "rest/mavg";
        }
        
        return  "rest/mavg";
    }
}
