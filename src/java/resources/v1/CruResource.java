/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resources.v1;

import cru.adapter.WebCruAdapter;
import cru.precip.CruDao;
import domain.Cru;
import domain.web.V1WebCru;
import export.CruExporter;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import service.CountryService;
import service.WebService;
import util.RequestUtil;

/**
 *
 * @author wb385924
 */
@Path("v1/country/cru")
public class CruResource {

    private static Logger log = Logger.getLogger(CruResource.class.getName());

    @GET
//    @Path("/pr/{fyear}-{fmonth}/{tyear}-{tmonth}/{iso3}")
    @Path("/pr/{fyear}-{tyear}/{iso3 : (?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    public Response getPrecipData(@PathParam("iso3") String iso3, @PathParam("fyear") int fyear, @PathParam("tyear") int tyear) {


        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);


        List<Cru> crupr = CruDao.get().getCru(countryId, CruDao.VAR.pr, fyear, tyear);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        return Response.ok(response, mediatype).build();

    }

    @GET
    @Path("/pr/year/{iso3: (?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    public Response getPrecipYearAggregate(@PathParam("iso3") String iso3) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(countryId, CruDao.VAR.pr, CruDao.Aggregation.YEAR);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);

        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/dl/pr/year/{iso3: (?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    public Response downloadPrecipYearAggregate(@PathParam("iso3") String iso3) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(countryId, CruDao.VAR.pr, CruDao.Aggregation.YEAR);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);

        ResponseBuilder rb = WebService.get().getProperResponse(response, WebService.Delivery.DOWNLOAD, mediatype);
        return rb.build();
    }

    @GET
    @Path("/pr/decade/{iso3 : (?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    public Response getPrecipDecadeAggregate(@PathParam("iso3") String iso3) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(countryId, CruDao.VAR.pr, CruDao.Aggregation.DECADE);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);

        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/dl/pr/decade/{iso3 : (?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    public Response downloadPrecipDecadeAggregate(@PathParam("iso3") String iso3) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(countryId, CruDao.VAR.pr, CruDao.Aggregation.DECADE);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);

        ResponseBuilder rb = WebService.get().getProperResponse(response, WebService.Delivery.DOWNLOAD, mediatype);
        return rb.build();
    }

    @GET
    @Path("/pr/month/{iso3 : (?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    public Response getPrecipMonthAggregate(@PathParam("iso3") String iso3) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(countryId, CruDao.VAR.pr, CruDao.Aggregation.MONTH);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);

        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/dl/pr/month/{iso3 : (?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    public Response downloadPrecipMonthAggregate(@PathParam("iso3") String iso3) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(countryId, CruDao.VAR.pr, CruDao.Aggregation.MONTH);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);

        ResponseBuilder rb = WebService.get().getProperResponse(response, WebService.Delivery.DOWNLOAD, mediatype);
        return rb.build();
    }

    @GET
    @Path("/pr/month/{fyear}-{tyear}/{iso3: (?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    public Response getPrecipMonthYearRange(@PathParam("iso3") String iso3, @PathParam("fyear") int fyear, @PathParam("tyear") int tyear) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCruMonthAveragesWithYearRange(countryId, CruDao.VAR.pr, fyear, tyear);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);

        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/dl/pr/month/{fyear}-{tyear}/{iso3: (?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    public Response downloadPrecipMonthYearRange(@PathParam("iso3") String iso3, @PathParam("fyear") int fyear, @PathParam("tyear") int tyear) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCruMonthAveragesWithYearRange(countryId, CruDao.VAR.pr, fyear, tyear);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);

        ResponseBuilder rb = WebService.get().getProperResponse(response, WebService.Delivery.DOWNLOAD, mediatype);
        return rb.build();
    }

    @GET
    @Path("/tas/month/{fyear}-{tyear}/{iso3 : (?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    public Response getTempMonthYearRange(@PathParam("iso3") String iso3, @PathParam("fyear") int fyear, @PathParam("tyear") int tyear) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCruMonthAveragesWithYearRange(countryId, CruDao.VAR.temp, fyear, tyear);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);

        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/tas/{fyear}-{tyear}/{iso3 : (?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    public Response getTempData(@PathParam("iso3") String iso3, @PathParam("fyear") int fyear, @PathParam("tyear") int tyear) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCru(countryId, CruDao.VAR.temp, fyear, tyear);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        return Response.ok(response, mediatype).build();

    }

    @GET
    @Path("/tas/year/{iso3 : (?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    public Response getTempYearAggregate(@PathParam("iso3") String iso3) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(countryId, CruDao.VAR.temp, CruDao.Aggregation.YEAR);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);

        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/dl/tas/year/{iso3 : (?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    public Response downloadTempYearAggregate(@PathParam("iso3") String iso3) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(countryId, CruDao.VAR.temp, CruDao.Aggregation.YEAR);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        ResponseBuilder rb = WebService.get().getProperResponse(response, WebService.Delivery.DOWNLOAD, mediatype);
        return rb.build();
    }

    @GET
    @Path("/tas/decade/{iso3 :(?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    public Response getTempDecadeAggregate(@PathParam("iso3") String iso3) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(countryId, CruDao.VAR.temp, CruDao.Aggregation.DECADE);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);

        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/dl/tas/decade/{iso3 :(?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    public Response downloadTempDecadeAggregate(@PathParam("iso3") String iso3) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(countryId, CruDao.VAR.temp, CruDao.Aggregation.DECADE);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        ResponseBuilder rb = WebService.get().getProperResponse(response, WebService.Delivery.DOWNLOAD, mediatype);
        return rb.build();
    }

    @GET
    @Path("/tas/month/{iso3:(?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    public Response getTempMonthAggregate(@PathParam("iso3") String iso3) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(countryId, CruDao.VAR.temp, CruDao.Aggregation.MONTH);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);

        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/dl/tas/month/{iso3:(?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    public Response downloadTempMonthAggregate(@PathParam("iso3") String iso3) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(countryId, CruDao.VAR.temp, CruDao.Aggregation.MONTH);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        ResponseBuilder rb = WebService.get().getProperResponse(response, WebService.Delivery.DOWNLOAD, mediatype);
        return rb.build();
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
