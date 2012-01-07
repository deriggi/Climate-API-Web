/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resources.v1.region;

import com.sun.jersey.spi.resource.Singleton;
import dao.ckpregion.CkpRegionDao;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import service.WebService;
import util.RequestUtil;
import service.WebService.Delivery;

/**
 *
 * @author wb385924
 */
@Singleton
@Path("/v1/region/")
public class P_RegionDataResource {

    private static final Logger log = Logger.getLogger(P_RegionDataResource.class.getName());

    // ================= L1 GCM =======================
    @GET
    @Path("/{tempagg}/{gcm:\\w{8,15}}/{scenario:(?i)(20c3m|a2|b1)}/{stat}/{fyear}/{tyear}/{rcode : (?i)\\w{2}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response getGcmScenario(
            @PathParam("tempagg") String tempagg,
            @PathParam("gcm") String gcmName,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("rcode") String rcode) {
        log.info("getting basin single gcm single scenario");
        String mediaType = RequestUtil.getResponseType(rcode);
        String identifier = RequestUtil.getIdentifier(rcode);
        int regionId = CkpRegionDao.get().getRegionId(identifier);
        return WebService.get().getGcmScenario(gcmName, scenarioName, statName, fyear, tyear, regionId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.WEB);
    }

    // ================= L1 GCM download =======================
    @GET
    @Path("/dl/{tempagg}/{gcm:\\w{8,15}}/{scenario:(?i)(20c3m|a2|b1)}/{stat}/{fyear}/{tyear}/{rcode : (?i)\\w{2}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response downloadGcmScenario(
            @PathParam("tempagg") String tempagg,
            @PathParam("gcm") String gcmName,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("rcode") String rcode) {

        String mediaType = RequestUtil.getResponseType(rcode);
        String identifier = RequestUtil.getIdentifier(rcode);
        int regionId = CkpRegionDao.get().getRegionId(identifier);

        return WebService.get().getGcmScenario(gcmName, scenarioName, statName, fyear, tyear, regionId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.DOWNLOAD);
    }

    // ===================== L2 gcm   ============================//
    @GET
    @Path("/{tempagg}/{gcm:\\w{8,15}}/{stat}/{fyear}/{tyear}/{rcode : (?i)\\w{2}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response getGcmData(
            @PathParam("tempagg") String tempagg,
            @PathParam("gcm") String gcmName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("rcode") String rcode) {
        log.info("getting basin single gcm");
        String mediaType = RequestUtil.getResponseType(rcode);
        String identifier = RequestUtil.getIdentifier(rcode);
        int regionId = CkpRegionDao.get().getRegionId(identifier);


        return WebService.get().getSingleGcmData(gcmName, statName, fyear, tyear, regionId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, WebService.Delivery.WEB);//getScenarioDataDelegate(scenarioName, statName, fyear, tyear, iso3, mediaType);
    }

    // ===================== L2 gcm  Download ============================//
    @GET
    @Path("/dl/{tempagg}/{gcm:\\w{8,15}}/{stat}/{fyear}/{tyear}/{rcode : (?i)\\w{2}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response downloadGcmData(
            @PathParam("tempagg") String tempagg,
            @PathParam("gcm") String gcmName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("rcode") String rcode) {

        String mediaType = RequestUtil.getResponseType(rcode);
        String identifier = RequestUtil.getIdentifier(rcode);
        int regionId = CkpRegionDao.get().getRegionId(identifier);


        return WebService.get().getSingleGcmData(gcmName, statName, fyear, tyear, regionId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, WebService.Delivery.DOWNLOAD);//getScenarioDataDelegate(scenarioName, statName, fyear, tyear, iso3, mediaType);
    }

    // ===================== L2 Gcm  ============================//
    @GET
    @Path("/{tempagg}/{scenario:(?i)(20c3m|a2|b1)}/{stat}/{fyear}/{tyear}/{rcode : (?i)\\w{2}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response getScenarioData(
            @PathParam("tempagg") String tempagg,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("rcode") String rcode) {

        log.info("getting basin single scenario");
        String mediaType = RequestUtil.getResponseType(rcode);
        String identifier = RequestUtil.getIdentifier(rcode);

        int regionId = CkpRegionDao.get().getRegionId(identifier);
        return WebService.get().getSingleScenarioData(scenarioName, statName, fyear, tyear, regionId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, WebService.Delivery.WEB);//getScenarioDataDelegate(scenarioName, statName, fyear, tyear, iso3, mediaType);
    }

    // ===================== L2 Gcm  Download ============================//
    @GET
    @Path("/dl/{tempagg}/{scenario:(?i)(20c3m|a2|b1)}/{stat}/{fyear}/{tyear}/{rcode : (?i)\\w{2}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response downloadScenarioData(
            @PathParam("tempagg") String tempagg,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("rcode") String rcode) {

        String mediaType = RequestUtil.getResponseType(rcode);
        String identifier = RequestUtil.getIdentifier(rcode);
        int regionId = CkpRegionDao.get().getRegionId(identifier);

        return WebService.get().getSingleScenarioData(scenarioName, statName, fyear, tyear, regionId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, WebService.Delivery.DOWNLOAD);//getScenarioDataDelegate(scenarioName, statName, fyear, tyear, iso3, mediaType);
    }

    // =================  L2 ensemble =============================
    @GET
    @Path("/{tempagg}/ensemble/{scenario:(?i)(20c3m|a2|b1)}/{percentile}/{stat}/{fyear}/{tyear}/{rcode : (?i)\\w{2}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response getEnsembleScenario(
            @PathParam("tempagg") String tempagg,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("percentile") int percentile,
            @PathParam("rcode") String rcode) {
        log.info("getting basin scenario percentile");
        String mediaType = RequestUtil.getResponseType(rcode);
        String identifier = RequestUtil.getIdentifier(rcode);
        int regionId = CkpRegionDao.get().getRegionId(identifier);

        return WebService.get().getEnsembleScenario(scenarioName, statName, fyear, tyear, percentile, regionId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.WEB);
    }

    // =================  L2 ensemble =============================
    @GET
    @Path("/dl/{tempagg}/ensemble/{scenario:(?i)(20c3m|a2|b1)}/{percentile}/{stat}/{fyear}/{tyear}/{rcode : (?i)\\w{2}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response downloadEnsembleScenario(
            @PathParam("tempagg") String tempagg,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("percentile") int percentile,
            @PathParam("rcode") String rcode) {

        String mediaType = RequestUtil.getResponseType(rcode);
        String identifier = RequestUtil.getIdentifier(rcode);
        int regionId = CkpRegionDao.get().getRegionId(identifier);

        return WebService.get().getEnsembleScenario(scenarioName, statName, fyear, tyear, percentile, regionId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.DOWNLOAD);
    }

    // ===============  gcm projections  ==================
    @GET
    @Path("/{tempagg}/{stat}/projection/{rcode : (?i)\\w{2}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response getGcmProjections(
            @PathParam("tempagg") String tempagg,
            @PathParam("stat") String statName,
            @PathParam("rcode") String rcode) {
        log.info("getting basin all projections");
        String mediaType = RequestUtil.getResponseType(rcode);
        String identifier = RequestUtil.getIdentifier(rcode);
        int regionId = CkpRegionDao.get().getRegionId(identifier);

        return WebService.get().getGcmProjections(statName, regionId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.WEB);
    }

    // ===============  ensemble projections  ==================
    @GET
    @Path("/{tempagg}/ensemble/{stat}/projection/{rcode : (?i)\\w{2}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response getEnsembleProjections(
            @PathParam("tempagg") String tempagg,
            @PathParam("stat") String statName,
            @PathParam("rcode") String rcode) {
        log.info("getting basin projections");
        String mediaType = RequestUtil.getResponseType(rcode);
        String identifier = RequestUtil.getIdentifier(rcode);
        int regionId = CkpRegionDao.get().getRegionId(identifier);



        return WebService.get().getEnsembleProjections(statName, regionId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.WEB);
    }

    // =============== download ensemble projections  ==================
    @GET
    @Path("/dl/{tempagg}/ensemble/{stat}/projection/{rcode : (?i)\\w{2}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response downloadEnsembleProjections(
            @PathParam("tempagg") String tempagg,
            @PathParam("stat") String statName,
            @PathParam("rcode") String rcode) {
        log.info("getting basin  projections");
        String mediaType = RequestUtil.getResponseType(rcode);
        String identifier = RequestUtil.getIdentifier(rcode);
        int regionId = CkpRegionDao.get().getRegionId(identifier);


        return WebService.get().getEnsembleProjections(statName, regionId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.DOWNLOAD);
    }

    // =================  L3 ensemble  =============================
    @GET
    @Path("/{tempagg}/ensemble/{stat}/{fyear}/{tyear}/{rcode : (?i)\\w{2}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response getAllScenarioEnsemble(
            @PathParam("tempagg") String tempagg,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("rcode") String rcode) {
        log.info("getting basin all scenario all percnetile");
        String mediaType = RequestUtil.getResponseType(rcode);
        String identifier = RequestUtil.getIdentifier(rcode);
         int regionId = CkpRegionDao.get().getRegionId(identifier);
        return WebService.get().getAllScenarioEnsemble(statName, fyear, tyear, regionId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.WEB);
    }

    // =================  L3 ensemble  download  =============================
    @GET
    @Path("/dl/{tempagg}/ensemble/{stat}/{fyear}/{tyear}/{rcode : (?i)\\w{2}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response downloadAllScenarioEnsemble(
            @PathParam("tempagg") String tempagg,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("rcode") String rcode) {

        String mediaType = RequestUtil.getResponseType(rcode);
        String identifier = RequestUtil.getIdentifier(rcode);
        int regionId = CkpRegionDao.get().getRegionId(identifier);

        return WebService.get().getAllScenarioEnsemble(statName, fyear, tyear, regionId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.DOWNLOAD);

    }

    // =================  ensemble single scenario all percentile =============================
    @GET
    @Path("/{tempagg}/ensemble/{scenario:(?i)(20c3m|a2|b1)}/{stat}/{fyear}/{tyear}/{rcode : (?i)\\w{2}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response getSingleScenarioAllPercentile(
            @PathParam("tempagg") String tempagg,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("rcode") String rcode) {
        log.info("getting basin single scenario all percentile");
        String mediaType = RequestUtil.getResponseType(rcode);
        String identifier = RequestUtil.getIdentifier(rcode);
        int regionId = CkpRegionDao.get().getRegionId(identifier);

        return WebService.get().getSingleScenarioAllPercentile(scenarioName, statName, fyear, tyear, regionId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.WEB);
    }

    // =================  download ensemble single scenario all percentile =============================
    @GET
    @Path("/dl/{tempagg}/ensemble/{scenario:(?i)(20c3m|a2|b1)}/{stat}/{fyear}/{tyear}/{rcode : (?i)\\w{2}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response downloadSingleScenarioAllPercentile(
            @PathParam("tempagg") String tempagg,
            @PathParam("scenario") String scenarioName,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("rcode") String rcode) {

        String mediaType = RequestUtil.getResponseType(rcode);
        String identifier = RequestUtil.getIdentifier(rcode);
        int regionId = CkpRegionDao.get().getRegionId(identifier);

        return WebService.get().getSingleScenarioAllPercentile(scenarioName, statName, fyear, tyear, regionId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.DOWNLOAD);
    }

    // =================  get ensemble single percentile all scenario =============================
    @GET
    @Path("/{tempagg}/ensemble/{percentile:\\d{2}}/{stat}/{fyear}/{tyear}/{rcode : (?i)\\w{2}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response getSinglePercentileAllScenario(
            @PathParam("tempagg") String tempagg,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("percentile") int percentile,
            @PathParam("rcode") String rcode) {
        log.info("getting basin ensemble single percentile all scenario");
        String mediaType = RequestUtil.getResponseType(rcode);
        String identifier = RequestUtil.getIdentifier(rcode);
        int regionId = CkpRegionDao.get().getRegionId(identifier);


        return WebService.get().getSinglePercentileAllScenario(percentile, statName, fyear, tyear, regionId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.WEB);
    }

    // =================  download ensemble single percentile all scenario =============================
    @GET
    @Path("/dl/{tempagg}/ensemble/{percentile:\\d{2}}/{stat}/{fyear}/{tyear}/{rcode : (?i)\\w{2}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response downloadSinglePercentileAllScenario(
            @PathParam("tempagg") String tempagg,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("percentile") int percentile,
            @PathParam("rcode") String rcode) {

        String mediaType = RequestUtil.getResponseType(rcode);
        String identifier = RequestUtil.getIdentifier(rcode);
        int regionId = CkpRegionDao.get().getRegionId(identifier);

        return WebService.get().getSinglePercentileAllScenario(percentile, statName, fyear, tyear, regionId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.DOWNLOAD);
    }

    // ===============  L3  Gcm  ==================
    @GET
    @Path("/{tempagg}/{stat}/{fyear}/{tyear}/{rcode : (?i)\\w{2}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response getAllGcmAllScenarioAllGcmMonthlyDataIso(
            @PathParam("tempagg") String tempagg,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("rcode") String rcode) {
        log.info("getting basin all gcm all scenarios");
        String mediaType = RequestUtil.getResponseType(rcode);
        String identifier = RequestUtil.getIdentifier(rcode);
        int regionId = CkpRegionDao.get().getRegionId(identifier);


        return WebService.get().getAllGcmAllScenario(statName, fyear, tyear, regionId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.WEB);
    }

    // ========  L3 Gcm Download  ===================  //
    @GET
    @Path("/dl/{tempagg}/{stat}/{fyear}/{tyear}/{rcode : (?i)\\w{2}(\\.\\w{3,5}|\\.\\w{3,5})?}")
    public Response downloadAllGcmAllScenarioAllGcmMonthlyDataIso(
            @PathParam("tempagg") String tempagg,
            @PathParam("stat") String statName,
            @PathParam("fyear") int fyear,
            @PathParam("tyear") int tyear,
            @PathParam("rcode") String rcode) {

        String mediaType = RequestUtil.getResponseType(rcode);
        String identifier = RequestUtil.getIdentifier(rcode);
        int regionId = CkpRegionDao.get().getRegionId(identifier);


        Response r = WebService.get().getAllGcmAllScenario(statName, fyear, tyear, regionId, WebService.get().getStatType(tempagg), WebService.get().geTempAgg(tempagg), mediaType, Delivery.DOWNLOAD);
        return r;
    }
}
