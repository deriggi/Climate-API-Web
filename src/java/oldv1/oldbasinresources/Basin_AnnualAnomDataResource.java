/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oldv1.oldbasinresources;

/**
 *
 * @author wb385924
 */
import dao.basin.BasinDao;
import domain.Basin;
import domain.web.GcmDatum;
import export.DataFormatHelper;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.ResponseConstants;
import shapefileloader.ensemble.P_EnsembleConfig;
import shapefileloader.ensemble.P_EnsembleConfigAreaDao;
import shapefileloader.gcm.P_GcmConfig;
import shapefileloader.gcm.P_GcmConfigAreaDao;
import shapefileloader.gcm.P_GcmStatsProperties;
import util.RequestUtil;

// The Java class will be hosted at the URI path "/helloworld"
@Path("/basin/annualanom")
public class Basin_AnnualAnomDataResource {

    private Pattern p = null;
    private Logger log = Logger.getLogger(Basin_AnnualAnomDataResource.class.getName());

    // The Java method will process HTTP GET requests
    @GET
    // The Java method will produce content identified by the MIME Media
    // type "text/plain"
    @Produces(MediaType.APPLICATION_JSON)
    public List<Basin> getBasins() {

        return BasinDao.get().getBasins();
    }

    @GET
    @Path("/{gcm}/{scenario}/{stat}/{fyear}/{tyear}/{wbhuc : \\d+ }")
    public Response getMonthlyDataBasin(
            @PathParam("gcm") String gcmName,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("wbhuc") int wbhuc) {

        return getAnnualDataBasinDelegate(gcmName, scenarioName, statName, fyear, tyear, wbhuc, MediaType.APPLICATION_JSON);
    }

    private Response getAnnualDataBasinDelegate(String gcmName,
            String scenarioName,
            String statName,
            int fyear,
            int tyear,
            int wbhuc, String format) {
        int basinId = BasinDao.get().getBasinIdFromCode(wbhuc);
        log.log(Level.INFO, "getting basin id is {0}", basinId);
        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);
        P_GcmStatsProperties.gcm gcm = ds.getGcm(gcmName);
        P_GcmStatsProperties.scenario scenario = ds.getScenario(scenarioName);

        //@TODO validate the config here
        P_GcmConfig config = new P_GcmConfig(P_GcmStatsProperties.stat_type.anom, gcm, scenario, stat, fyear, tyear);
        if (!config.isCompleteIgnoringAreaValueMonth()) {
            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
            return Response.ok(errorResponse, format).build();
        }
        config.setAreaId(basinId);
        HashMap<Integer, Double> data = P_GcmConfigAreaDao.get().getAreaDataForTime(config, true);
        String formattedData = DataFormatHelper.formatData(format, data);

        Response.ResponseBuilder builder = Response.ok(formattedData, format);
        return builder.build();
    }

    // ============  aggregate across all gcms ==================//
    @GET
    @Path("/{scenario}/{stat}/{fyear}/{tyear}/{wbhuc : (?i)\\d{1,6}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response getAllMonthlyDataIso(
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("wbhuc") String wbhuc) {

        String mediaType = RequestUtil.getResponseType(wbhuc);
        String identifier = RequestUtil.getIdentifier(wbhuc);


        return getAllMonthlyDataBasinDelegate(scenarioName, statName, fyear, tyear, Integer.parseInt(identifier), mediaType);
    }

    private Response getAllMonthlyDataBasinDelegate(
            String scenarioName,
            String statName,
            int fyear,
            int tyear,
            int wbhuc, String format) {
        int basinId = BasinDao.get().getBasinIdFromCode(wbhuc);
        log.log(Level.INFO, "getting basin id is {0}", basinId);
        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);
        P_GcmStatsProperties.scenario scenario = ds.getScenario(scenarioName);

        //@TODO validate the config here
        P_GcmConfig config = new P_GcmConfig(P_GcmStatsProperties.stat_type.anom, null, scenario, stat, fyear, tyear);
        if (scenario == null || stat == null) {
            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
            return Response.ok(errorResponse, format).build();
        }
        config.setAreaId(basinId);
        List<GcmDatum> configs = P_GcmConfigAreaDao.get().getAllGcmAreaData(config, true);
        String formattedData = DataFormatHelper.formatGcms(format, configs);

        Response.ResponseBuilder builder = Response.ok(formattedData, format);
        return builder.build();
    }

    // ===   ensemble ==
    @GET
    @Path("/ensemble/{scenario}/{stat}/{fyear}/{tyear}/{percentile}/{wbhuc : (?i)\\d{1,6}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnnualEnsembleStatInIsoCountry(
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("percentile") int percentile,
            @PathParam("wbhuc") String wbhuc) {

        String mediaType = RequestUtil.getResponseType(wbhuc);
        String identifier = RequestUtil.getIdentifier(wbhuc);

        return getAnnualEnsembleDelegate(scenarioName, statName, fyear, tyear, percentile, Integer.parseInt(identifier), mediaType);

        //return dao.getRainData(id);
    }

    private Response getAnnualEnsembleDelegate(String scenarioName, String statName, int fyear, int tyear, int percentile, int wbhuc, String format) {
        int basinId = BasinDao.get().getBasinIdFromCode(wbhuc);
        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);
        P_GcmStatsProperties.scenario scenario = ds.getScenario(scenarioName);

        P_EnsembleConfig config = new P_EnsembleConfig(P_GcmStatsProperties.stat_type.anom, scenario, stat, fyear, tyear, percentile);
        if (!config.isCompleteIgnoringAreaValueMonth()) {
            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
            return Response.ok(errorResponse, format).build();
        }
        config.setAreaId(basinId);
        HashMap<Integer, Double> data = P_EnsembleConfigAreaDao.get().getAreaDataForTime(config, true);

        String formattedData = DataFormatHelper.formatMonthlyOrAnnualMap(format, data);
        Response.ResponseBuilder builder = Response.ok(formattedData, format);

        return builder.build();
    }
}
