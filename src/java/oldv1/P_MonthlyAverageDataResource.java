/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oldv1;

import util.ResponseConstants;
import com.google.gson.Gson;
import domain.web.GcmDatum;
import export.DataFormatHelper;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import service.CountryService;
import service.DerivedStatsService;
import shapefileloader.ensemble.P_EnsembleConfig;
import shapefileloader.ensemble.P_EnsembleConfigAreaDao;
import shapefileloader.gcm.P_GcmConfig;
import shapefileloader.gcm.P_GcmConfigAreaDao;
import shapefileloader.gcm.P_GcmStatsProperties;
import util.RequestUtil;

/**
 *
 * @author wb385924
 */
@Path("/mavg")
public class P_MonthlyAverageDataResource {

    private static final Logger log = Logger.getLogger(P_MonthlyAverageDataResource.class.getName());

    @GET
    @Path("/{gcm}/{scenario}/{stat}/{fyear}/{tyear}/{longitude},{latitude}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMonthlyData(
            @PathParam("gcm") String gcmName,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("latitude") double latitude,
            @PathParam("longitude") double longitude) {

        int countryId = DerivedStatsService.get().getCountryId(latitude, longitude);
        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();

        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);
        P_GcmStatsProperties.gcm gcm = ds.getGcm(gcmName);
        P_GcmStatsProperties.scenario scenario = ds.getScenario(scenarioName);

        P_GcmConfig config = new P_GcmConfig(P_GcmStatsProperties.stat_type.mean, gcm, scenario, stat, fyear, tyear);
        if (!config.isCompleteIgnoringAreaValueMonth()) {
            return ResponseConstants.INVALID_PARAMS;
        }
        config.setAreaId(countryId);

        HashMap<Integer, Double> data = P_GcmConfigAreaDao.get().getAreaDataForTime(config, false);
        Gson gson = new Gson();
        return gson.toJson(data);

        //return dao.getRainData(id);
    }







    

    // ================= individual record =======================
    @GET
    @Path("/{gcm}/{scenario}/{stat}/{fyear}/{tyear}/{iso3}")
    public Response getMonthlyDataIso(
            @PathParam("gcm") String gcmName,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("iso3") String iso3) {

        if (iso3 == null || iso3.length() < 3) {
            return Response.ok(ResponseConstants.INVALID_ISO, MediaType.APPLICATION_JSON).build();
        }
        String mediaType = RequestUtil.getResponseType(iso3);
        iso3 = RequestUtil.getIdentifier(iso3);

        return getMonthlyDataIsoDelegate(gcmName, scenarioName, statName, fyear, tyear, iso3, mediaType);
    }

    private Response getMonthlyDataIsoDelegate(String gcmName,
            String scenarioName,
            String statName,
            int fyear,
            int tyear,
            String iso3, String format) {
        int countryId = CountryService.get().getId(iso3);
        log.log(Level.INFO, "getting country id is {0}", countryId);
        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);
        P_GcmStatsProperties.gcm gcm = ds.getGcm(gcmName);
        P_GcmStatsProperties.scenario scenario = ds.getScenario(scenarioName);

        //@TODO validate the config here
        P_GcmConfig config = new P_GcmConfig(P_GcmStatsProperties.stat_type.mean, gcm, scenario, stat, fyear, tyear);
        if (!config.isCompleteIgnoringAreaValueMonth()) {
            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
            return Response.ok(errorResponse, format).build();
        }
        config.setAreaId(countryId);
        HashMap<Integer, Double> data = P_GcmConfigAreaDao.get().getAreaDataForTime(config, false);
        String formattedData = DataFormatHelper.formatData(format, data);

        Response.ResponseBuilder builder = Response.ok(formattedData, format);
        return builder.build();
    }















    // ================= individual ensemble =============================
    @GET
    @Path("/ensemble/{scenario}/{stat}/{fyear}/{tyear}/{percentile}/{iso3}")
    public Response getMonthlyEnsemble(
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("percentile") int percentile,
            @PathParam("iso3") String iso3) {

        if (iso3 == null || iso3.length() < 3) {
            return Response.ok(ResponseConstants.INVALID_ISO, MediaType.APPLICATION_JSON).build();
        }
        String mediaType = RequestUtil.getResponseType(iso3);
        iso3 = RequestUtil.getIdentifier(iso3);


        return getMonthlyEnsembleDelegate(scenarioName, statName, fyear, tyear, percentile, iso3, mediaType);
    }

    private Response getMonthlyEnsembleDelegate(String scenarioName, String statName, int fyear, int tyear, int percentile, String iso3, String format) {
        int countryId = CountryService.get().getId(iso3);
        log.log(Level.INFO, "getting country id is {0}", countryId);
        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);
        P_GcmStatsProperties.scenario scenario = ds.getScenario(scenarioName);

        P_EnsembleConfig config = new P_EnsembleConfig(P_GcmStatsProperties.stat_type.mean, scenario, stat, fyear, tyear, percentile);
        if (!config.isCompleteIgnoringAreaValueMonth()) {
            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
            return Response.ok(errorResponse, format).build();
        }
        config.setAreaId(countryId);
        HashMap<Integer, Double> data = P_EnsembleConfigAreaDao.get().getAreaDataForTime(config, false);

        String formattedData = DataFormatHelper.formatMonthlyOrAnnualMap(format, data);
        Response.ResponseBuilder builder = Response.ok(formattedData, format);

        return builder.build();
    }











    // =================  individual gcm with range on start year ============= //
    @GET
    @Path("/range/{gcm}/{scenario}/{stat}/{fyear}/{tyear}/{iso3}")
    public Response getMonthlyStatsRangeCountry(
            @PathParam("gcm") String gcmName,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("iso3") String iso3) {
        if (iso3 == null || iso3.length() < 3) {
            return Response.ok(ResponseConstants.INVALID_ISO, MediaType.APPLICATION_JSON).build();
        }
        String mediaType = RequestUtil.getResponseType(iso3);
        iso3 = RequestUtil.getIdentifier(iso3);

        return getMonthlyStatsRangeCountryDelegate(gcmName, scenarioName, statName, fyear, tyear, iso3, mediaType);
    }

    private Response getMonthlyStatsRangeCountryDelegate(String gcmName, String scenarioName, String statName, int fyear, int tyear, String iso3, String format) {
        int countryId = CountryService.get().getId(iso3);
        log.log(Level.INFO, "getting country id is {0}", countryId);
        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);
        P_GcmStatsProperties.gcm gcm = ds.getGcm(gcmName);
        P_GcmStatsProperties.scenario scenario = ds.getScenario(scenarioName);

        P_GcmConfig config = new P_GcmConfig(P_GcmStatsProperties.stat_type.mean, gcm, scenario, stat, fyear, tyear);
        if (!config.isCompleteIgnoringAreaValueMonth()) {
            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
            return Response.ok(errorResponse, format).build();
        }
        config.setAreaId(countryId);
        TreeMap<Integer, HashMap<Integer, Double>> data = P_GcmConfigAreaDao.get().getAreaDataForStartYearRange(config, fyear, tyear, false);

        String formattedData = DataFormatHelper.formatData(format, data);
        Response.ResponseBuilder builder = Response.ok(formattedData, format);

        return builder.build();
    }









    

    // ============  aggregate across all gcms ==================//
//    @GET
//    @Path("/{scenario}/{stat}/{fyear}/{tyear}/{iso3}")
//    public Response getAllMonthlyDataIso(
//            @PathParam("scenario") String scenarioName,
//            @PathParam("stat") String statName,
//            @PathParam("fyear") int fyear,
//            @PathParam("tyear") int tyear,
//            @PathParam("iso3") String iso3) {
//
//        if (iso3 == null || iso3.length() < 3) {
//            return Response.ok(ResponseConstants.INVALID_ISO, MediaType.APPLICATION_JSON).build();
//        }
//        if (iso3 != null && (iso3.contains(ResponseConstants.xml) || iso3.contains(ResponseConstants.XML))) {
//            return getAllMonthlyDataIsoDelegate(scenarioName, statName, fyear, tyear, iso3.substring(0, iso3.indexOf(ResponseConstants.DOT)), MediaType.APPLICATION_XML);
//        }
//
//        return getAllMonthlyDataIsoDelegate(scenarioName, statName, fyear, tyear, iso3, MediaType.APPLICATION_JSON);
//    }
    @GET
    @Path("/{scenario}/{stat}/{fyear}/{tyear}/{iso3}")
    public Response getAllMonthlyDataIso(
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("iso3") String iso3) {

        if (iso3 == null || iso3.length() < 3) {
            return Response.ok(ResponseConstants.INVALID_ISO, MediaType.APPLICATION_JSON).build();
        }
        String mediaType = RequestUtil.getResponseType(iso3);
        iso3 = RequestUtil.getIdentifier(iso3);

        return getAllMonthlyDataIsoDelegate(scenarioName, statName, fyear, tyear, iso3, mediaType);
    }

    private Response getAllMonthlyDataIsoDelegate(
            String scenarioName,
            String statName,
            int fyear,
            int tyear,
            String iso3, String format) {
        int countryId = CountryService.get().getId(iso3);
        log.log(Level.INFO, "getting country id is {0}", countryId);
        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);
        P_GcmStatsProperties.scenario scenario = ds.getScenario(scenarioName);

        //@TODO validate the config here
        P_GcmConfig config = new P_GcmConfig(P_GcmStatsProperties.stat_type.mean, null, scenario, stat, fyear, tyear);
        if (scenario == null || stat == null) {
            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
            return Response.ok(errorResponse, format).build();
        }
        config.setAreaId(countryId);
        List<GcmDatum> gcms = P_GcmConfigAreaDao.get().getAllGcmAreaData(config, false);
        String formattedData = DataFormatHelper.formatGcms(format, gcms);

        Response.ResponseBuilder builder = Response.ok(formattedData, format);
        return builder.build();
    }
}
