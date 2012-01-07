/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oldv1;

import util.ResponseConstants;
import cache.GcmCache;
import com.google.gson.Gson;
import domain.web.GcmDatum;
import export.DataFormatHelper;
import java.text.SimpleDateFormat;
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
@Path("/annualanom")
public class P_AnnualAnomDataResource {

    private static final Logger log = Logger.getLogger(P_AnnualAnomDataResource.class.getName());

    @GET
    @Path("/{gcm}/{scenario}/{stat}/{fyear}/{tyear}/{longitude},{latitude}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMonthlyDerivedStatInContainingCountry(
            @PathParam("gcm") String gcmName,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("latitude") double latitude,
            @PathParam("longitude") double longitude) {
        log.fine("trying to retrieve derived data");
        int countryId = DerivedStatsService.get().getCountryId(latitude, longitude);
        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();

        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);
        P_GcmStatsProperties.gcm gcm = ds.getGcm(gcmName);
        P_GcmStatsProperties.scenario scenario = ds.getScenario(scenarioName);

        P_GcmConfig config = new P_GcmConfig(P_GcmStatsProperties.stat_type.anom, gcm, scenario, stat, fyear, tyear);
        config.setAreaId(countryId);

        HashMap<Integer, Double> data = P_GcmConfigAreaDao.get().getAreaDataForTime(config, true);
        Gson gson = new Gson();
        return gson.toJson(data);

        //return dao.getRainData(id);
    }

    @GET
    @Path("/{gcm}/{scenario}/{stat}/{fyear}/{tyear}/{iso3}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMonthlyDerivedStatInIsoCountry(
            @PathParam("gcm") String gcmName,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("iso3") String iso3) {
        log.fine("trying to retrieve derived data");
        int countryId = CountryService.get().getId(iso3);
        log.log(Level.INFO, "getting country id is {0}", countryId);
        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);
        P_GcmStatsProperties.gcm gcm = ds.getGcm(gcmName);
        P_GcmStatsProperties.scenario scenario = ds.getScenario(scenarioName);

        P_GcmConfig config = new P_GcmConfig(P_GcmStatsProperties.stat_type.anom, gcm, scenario, stat, fyear, tyear);
          if (!config.isCompleteIgnoringAreaValueMonth()) {
            String errorResponse = DataFormatHelper.formatData(MediaType.APPLICATION_JSON, ResponseConstants.INVALID_PARAMS);
            return errorResponse;
        }

        config.setAreaId(countryId);
        HashMap<Integer, Double> data = P_GcmConfigAreaDao.get().getAreaDataForTime(config, true);
        Gson gson = new Gson();
        return gson.toJson(data);

        //return dao.getRainData(id);
    }

    // ===   ensemble ==
    @GET
    @Path("/ensemble/{scenario}/{stat}/{fyear}/{tyear}/{percentile}/{iso3}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAnnualEnsembleStatInIsoCountry(
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("percentile") int percentile,
            @PathParam("iso3") String iso3) {

        String mediaType = RequestUtil.getResponseType(iso3);
        iso3 = RequestUtil.getIdentifier(iso3);

        return getAnnualEnsembleDelegate(scenarioName, statName, fyear, tyear, percentile, iso3, mediaType);

        //return dao.getRainData(id);
    }

    private Response getAnnualEnsembleDelegate(String scenarioName, String statName, int fyear, int tyear, int percentile, String iso3, String format) {
        int countryId = CountryService.get().getId(iso3);
        log.log(Level.INFO, "getting country id is {0}", countryId);
        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);
        P_GcmStatsProperties.scenario scenario = ds.getScenario(scenarioName);

        P_EnsembleConfig config = new P_EnsembleConfig(P_GcmStatsProperties.stat_type.anom, scenario, stat, fyear, tyear, percentile);
        if (!config.isCompleteIgnoringAreaValueMonth()) {
            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
            return Response.ok(errorResponse, format).build();
        }
        config.setAreaId(countryId);
        HashMap<Integer, Double> data = P_EnsembleConfigAreaDao.get().getAreaDataForTime(config, true);

        String formattedData = DataFormatHelper.formatMonthlyOrAnnualMap(format, data);
        Response.ResponseBuilder builder = Response.ok(formattedData, format);

        return builder.build();
    }

    @GET
    @Path("/range/{gcm}/{scenario}/{stat}/{fyear}/{tyear}/{iso3}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getMonthlyStatsRangeCountry(
            @PathParam("gcm") String gcmName,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("iso3") String iso3) {
        log.fine("trying to retrieve derived data");
        int countryId = CountryService.get().getId(iso3);
        log.log(Level.INFO, "getting country id is {0}", countryId);
        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        P_GcmStatsProperties.climatestat stat = ds.getClimateStat(statName);
        P_GcmStatsProperties.gcm gcm = ds.getGcm(gcmName);
        P_GcmStatsProperties.scenario scenario = ds.getScenario(scenarioName);

        P_GcmConfig config = new P_GcmConfig(P_GcmStatsProperties.stat_type.anom, gcm, scenario, stat, fyear, tyear);
        config.setAreaId(countryId);
        TreeMap<Integer, HashMap<Integer, Double>> data = P_GcmConfigAreaDao.get().getAreaDataForStartYearRange(config, fyear, tyear, true);
        Gson gson = new Gson();
        return gson.toJson(data);

        //return dao.getRainData(id);
    }

    // ====     aggregate   ========
    @GET
    @Path("/{scenario}/{stat}/{fyear}/{tyear}/{iso3}")
    public Response getAllDataIso(
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

        return getAlDataIsoDelegate(scenarioName, statName, fyear, tyear, iso3, mediaType);
    }

    private Response getAlDataIsoDelegate(
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
        P_GcmConfig config = new P_GcmConfig(P_GcmStatsProperties.stat_type.anom, null, scenario, stat, fyear, tyear);
        if (scenario == null || stat == null) {
            String errorResponse = DataFormatHelper.formatData(format, ResponseConstants.INVALID_PARAMS);
            return Response.ok(errorResponse, format).build();
        }
        config.setAreaId(countryId);
        List<GcmDatum> configs = P_GcmConfigAreaDao.get().getAllGcmAreaData(config, true);
        String formattedData = DataFormatHelper.formatGcms(format, configs);

        Response.ResponseBuilder builder = Response.ok(formattedData, format);
        return builder.build();
    }
}
