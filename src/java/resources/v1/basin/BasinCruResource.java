/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resources.v1.basin;

import cru.adapter.WebCruAdapter;
import cru.precip.CruDao;
import dao.basin.BasinDao;
import domain.Cru;
import domain.web.V1WebCru;
import export.CruExporter;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import service.WebService;
import util.RequestUtil;

/**
 *
 * @author wb385924
 */
@Path("/v1/basin/cru")
public class BasinCruResource {

    private static Logger log = Logger.getLogger(BasinCruResource.class.getName());

    @GET
//    @Path("/pr/{fyear}-{fmonth}/{tyear}-{tmonth}/{iso3}")
    @Path("/pr/{fyear}-{tyear}/{wbhuc : (?i)\\d{1,6}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response getPrecipData(@PathParam("wbhuc") String wbhuc, @PathParam("fyear") int fyear, @PathParam("tyear") int tyear) {
        String identifier = RequestUtil.getIdentifier(wbhuc);
        String mediatype = RequestUtil.getResponseType(wbhuc);

        BasinDao basinDao = BasinDao.get();

        int basinId = basinDao.getBasinIdFromCode(Integer.parseInt(identifier));

        List<Cru> crupr = CruDao.get().getCru(basinId, CruDao.VAR.pr, fyear, tyear);
        String response = CruExporter.formatCru(mediatype, crupr);
        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/pr/year/{wbhuc : (?i)\\d{1,6}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response getPrecipYearAggregate(@PathParam("wbhuc") String wbhuc) {
        String identifier = RequestUtil.getIdentifier(wbhuc);
        String mediatype = RequestUtil.getResponseType(wbhuc);

        BasinDao basinDao = BasinDao.get();
        int basinId = basinDao.getBasinIdFromCode(Integer.parseInt(identifier));

        List<Cru> crupr = CruDao.get().getCruAggregation(basinId, CruDao.VAR.pr, CruDao.Aggregation.YEAR);

         List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/dl/pr/year/{wbhuc : (?i)\\d{1,6}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response downloadPrecipYearAggregate(@PathParam("wbhuc") String wbhuc) {
        String identifier = RequestUtil.getIdentifier(wbhuc);
        String mediatype = RequestUtil.getResponseType(wbhuc);

        BasinDao basinDao = BasinDao.get();
        int basinId = basinDao.getBasinIdFromCode(Integer.parseInt(identifier));

        List<Cru> crupr = CruDao.get().getCruAggregation(basinId, CruDao.VAR.pr, CruDao.Aggregation.YEAR);

         List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        ResponseBuilder rb = WebService.get().getProperResponse(response, WebService.Delivery.DOWNLOAD, mediatype);
        return rb.build();
    }

    @GET
    @Path("/pr/decade/{wbhuc : (?i)\\d{1,6}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response getPrecipDecadeAggregate(@PathParam("wbhuc") String wbhuc) {
        String identifier = RequestUtil.getIdentifier(wbhuc);
        String mediatype = RequestUtil.getResponseType(wbhuc);

        BasinDao basinDao = BasinDao.get();
        int basinId = basinDao.getBasinIdFromCode(Integer.parseInt(identifier));

        List<Cru> crupr = CruDao.get().getCruAggregation(basinId, CruDao.VAR.pr, CruDao.Aggregation.DECADE);

         List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/dl/pr/decade/{wbhuc : (?i)\\d{1,6}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response downloadPrecipDecadeAggregate(@PathParam("wbhuc") String wbhuc) {
        String identifier = RequestUtil.getIdentifier(wbhuc);
        String mediatype = RequestUtil.getResponseType(wbhuc);

        BasinDao basinDao = BasinDao.get();
        int basinId = basinDao.getBasinIdFromCode(Integer.parseInt(identifier));

        List<Cru> crupr = CruDao.get().getCruAggregation(basinId, CruDao.VAR.pr, CruDao.Aggregation.DECADE);

        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        
        ResponseBuilder rb = WebService.get().getProperResponse(response, WebService.Delivery.DOWNLOAD, mediatype);
        return rb.build();
    }


    @GET
    @Path("/pr/month/{wbhuc: (?i)\\d{1,6}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response getPrecipMonthAggregate(@PathParam("wbhuc") String wbhuc) {
        String identifier = RequestUtil.getIdentifier(wbhuc);
        String mediatype = RequestUtil.getResponseType(wbhuc);

        BasinDao basinDao = BasinDao.get();
        int basinId = basinDao.getBasinIdFromCode(Integer.parseInt(identifier));

        List<Cru> crupr = CruDao.get().getCruAggregation(basinId, CruDao.VAR.pr, CruDao.Aggregation.MONTH);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        return Response.ok(response, mediatype).build();
    }


    @GET
    @Path("/dl/pr/month/{wbhuc: (?i)\\d{1,6}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response downloadPrecipMonthAggregate(@PathParam("wbhuc") String wbhuc) {
        String identifier = RequestUtil.getIdentifier(wbhuc);
        String mediatype = RequestUtil.getResponseType(wbhuc);

        BasinDao basinDao = BasinDao.get();
        int basinId = basinDao.getBasinIdFromCode(Integer.parseInt(identifier));

        List<Cru> crupr = CruDao.get().getCruAggregation(basinId, CruDao.VAR.pr, CruDao.Aggregation.MONTH);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        ResponseBuilder rb = WebService.get().getProperResponse(response, WebService.Delivery.DOWNLOAD, mediatype);
        return rb.build();
    }


    @GET
    @Path("/pr/month/{fyear}-{tyear}/{wbhuc : (?i)\\d{1,6}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response getPrecipMonthYearRange(@PathParam("wbhuc") String wbhuc, @PathParam("fyear") int fyear, @PathParam("tyear") int tyear) {
        String identifier = RequestUtil.getIdentifier(wbhuc);
        String mediatype = RequestUtil.getResponseType(wbhuc);

        BasinDao basinDao = BasinDao.get();
        int basinId = basinDao.getBasinIdFromCode(Integer.parseInt(identifier));

        List<Cru> crupr = CruDao.get().getCruMonthAveragesWithYearRange(basinId, CruDao.VAR.pr, fyear, tyear);
         List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/tas/month/{fyear}-{tyear}/{wbhuc : (?i)\\d{1,6}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response getTempMonthYearRange(@PathParam("wbhuc") String wbhuc, @PathParam("fyear") int fyear, @PathParam("tyear") int tyear) {
        String identifier = RequestUtil.getIdentifier(wbhuc);
        String mediatype = RequestUtil.getResponseType(wbhuc);

        BasinDao basinDao = BasinDao.get();
        int basinId = basinDao.getBasinIdFromCode(Integer.parseInt(identifier));

        List<Cru> crupr = CruDao.get().getCruMonthAveragesWithYearRange(basinId, CruDao.VAR.temp, fyear, tyear);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/tas/{fyear}-{tyear}/{wbhuc : (?i)\\d{1,6}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response getTempData(@PathParam("wbhuc") String wbhuc, @PathParam("fyear") int fyear, @PathParam("tyear") int tyear) {
        String identifier = RequestUtil.getIdentifier(wbhuc);
        String mediatype = RequestUtil.getResponseType(wbhuc);

        BasinDao basinDao = BasinDao.get();
        int basinId = basinDao.getBasinIdFromCode(Integer.parseInt(identifier));

        List<Cru> crupr = CruDao.get().getCru(basinId, CruDao.VAR.temp, fyear, tyear);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        return Response.ok(response, mediatype).build();

    }

    @GET
    @Path("/tas/year/{wbhuc  : (?i)\\d{1,6}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response getTempYearAggregate(@PathParam("wbhuc") String wbhuc) {
        String identifier = RequestUtil.getIdentifier(wbhuc);
        String mediatype = RequestUtil.getResponseType(wbhuc);

        BasinDao basinDao = BasinDao.get();
        int basinId = basinDao.getBasinIdFromCode(Integer.parseInt(identifier));

        List<Cru> crupr = CruDao.get().getCruAggregation(basinId, CruDao.VAR.temp, CruDao.Aggregation.YEAR);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/dl/tas/year/{wbhuc  : (?i)\\d{1,6}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response downloadTempYearAggregate(@PathParam("wbhuc") String wbhuc) {
        String identifier = RequestUtil.getIdentifier(wbhuc);
        String mediatype = RequestUtil.getResponseType(wbhuc);

        BasinDao basinDao = BasinDao.get();
        int basinId = basinDao.getBasinIdFromCode(Integer.parseInt(identifier));

        List<Cru> crupr = CruDao.get().getCruAggregation(basinId, CruDao.VAR.temp, CruDao.Aggregation.YEAR);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);

        ResponseBuilder rb = WebService.get().getProperResponse(response, WebService.Delivery.DOWNLOAD, mediatype);
        return rb.build();
    }

    @GET
    @Path("/dl/tas/decade/{wbhuc : (?i)\\d{1,6}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response downloadTempDecadeAggregate(@PathParam("wbhuc") String wbhuc) {
        String identifier = RequestUtil.getIdentifier(wbhuc);
        String mediatype = RequestUtil.getResponseType(wbhuc);

        BasinDao basinDao = BasinDao.get();
        int basinId = basinDao.getBasinIdFromCode(Integer.parseInt(identifier));


        List<Cru> crupr = CruDao.get().getCruAggregation(basinId, CruDao.VAR.temp, CruDao.Aggregation.DECADE);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);

        ResponseBuilder rb = WebService.get().getProperResponse(response, WebService.Delivery.DOWNLOAD, mediatype);
        return rb.build();
    }


    @GET
    @Path("/tas/decade/{wbhuc : (?i)\\d{1,6}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response getTempDecadeAggregate(@PathParam("wbhuc") String wbhuc) {
        String identifier = RequestUtil.getIdentifier(wbhuc);
        String mediatype = RequestUtil.getResponseType(wbhuc);

        BasinDao basinDao = BasinDao.get();
        int basinId = basinDao.getBasinIdFromCode(Integer.parseInt(identifier));


        List<Cru> crupr = CruDao.get().getCruAggregation(basinId, CruDao.VAR.temp, CruDao.Aggregation.DECADE);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/dl/tas/month/{wbhuc : (?i)\\d{1,6}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response downloadTempMonthAggregate(@PathParam("wbhuc") String wbhuc) {
        String identifier = RequestUtil.getIdentifier(wbhuc);
        String mediatype = RequestUtil.getResponseType(wbhuc);

        BasinDao basinDao = BasinDao.get();
        int basinId = basinDao.getBasinIdFromCode(Integer.parseInt(identifier));

        List<Cru> crupr = CruDao.get().getCruAggregation(basinId, CruDao.VAR.temp, CruDao.Aggregation.MONTH);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        ResponseBuilder rb = WebService.get().getProperResponse(response, WebService.Delivery.DOWNLOAD, mediatype);
        return rb.build();
    }

    @GET
    @Path("/tas/month/{wbhuc : (?i)\\d{1,6}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response getTempMonthAggregate(@PathParam("wbhuc") String wbhuc) {
        String identifier = RequestUtil.getIdentifier(wbhuc);
        String mediatype = RequestUtil.getResponseType(wbhuc);

        BasinDao basinDao = BasinDao.get();
        int basinId = basinDao.getBasinIdFromCode(Integer.parseInt(identifier));

        List<Cru> crupr = CruDao.get().getCruAggregation(basinId, CruDao.VAR.temp, CruDao.Aggregation.MONTH);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        return Response.ok(response, mediatype).build();
    }
//    @Produces(MediaType.APPLICATION_JSON)
//    public String getCruData(@PathParam("fyear") int fyear, @PathParam("fmonth") int fmonth, @PathParam("tyear") int tyear, @PathParam("tmonth") int tmonth, @PathParam("latitude") double latitude, @PathParam("longitude") double longitude) {
//
//        int pointId = GeoDao.getIdOfGeometriesNearPoint(DBUtils.get().getConnection(), latitude, longitude, "cru_pr_point", "geom", "id", 0.5);
//        Gson gson = new Gson();
//        if (pointId != -1) {
//            Calendar fromDate = Calendar.getInstance();
//            fromDate.set(Calendar.YEAR, fyear);
//            fromDate.set(Calendar.MONTH, fmonth - 1);
//            fromDate.set(Calendar.DATE, 1);
//
//            Calendar toDate = Calendar.getInstance();
//            toDate.set(Calendar.YEAR, tyear);
//            toDate.set(Calendar.MONTH, tmonth - 1);
//            toDate.set(Calendar.DATE, 1);
//            log.log(Level.INFO, "looknig for id {0}", pointId);
//
//
//            return gson.toJson(CruDao.getInstance().getCruDataForPoint(pointId, fromDate.getTime(), toDate.getTime()));
//
//        }
//        return gson.toJson(new ArrayList<Object>(0));
//    }
}
