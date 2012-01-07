/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oldv1;

import cru.precip.CruDao;
import domain.Cru;
import export.CruExporter;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import service.CountryService;
import util.RequestUtil;

/**
 *
 * @author wb385924
 */
@Path("/cru")
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
        String response = CruExporter.formatCru(mediatype, crupr);
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
        String response = CruExporter.formatCru(mediatype, crupr);

        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/pr/decade/{iso3 : (?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    public Response getPrecipDecadeAggregate(@PathParam("iso3") String iso3) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(countryId, CruDao.VAR.pr, CruDao.Aggregation.DECADE);
        String response = CruExporter.formatCru(mediatype, crupr);

        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/pr/month/{iso3 : (?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrecipMonthAggregate(@PathParam("iso3") String iso3) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(countryId, CruDao.VAR.pr, CruDao.Aggregation.MONTH);
        String response = CruExporter.formatCru(mediatype, crupr);

        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/pr/month/{fyear}-{tyear}/{iso3: (?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPrecipMonthYearRange(@PathParam("iso3") String iso3, @PathParam("fyear") int fyear, @PathParam("tyear") int tyear) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCruMonthAveragesWithYearRange(countryId, CruDao.VAR.pr, fyear, tyear);
        String response = CruExporter.formatCru(mediatype, crupr);

        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/temp/month/{fyear}-{tyear}/{iso3 : (?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    public Response getTempMonthYearRange(@PathParam("iso3") String iso3, @PathParam("fyear") int fyear, @PathParam("tyear") int tyear) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCruMonthAveragesWithYearRange(countryId, CruDao.VAR.temp, fyear, tyear);
        String response = CruExporter.formatCru(mediatype, crupr);

        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/temp/{fyear}-{tyear}/{iso3 : (?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    public Response getTempData(@PathParam("iso3") String iso3, @PathParam("fyear") int fyear, @PathParam("tyear") int tyear) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCru(countryId, CruDao.VAR.temp, fyear, tyear);
        String response = CruExporter.formatCru(mediatype, crupr);

        return Response.ok(response, mediatype).build();

    }

    @GET
    @Path("/temp/year/{iso3 : (?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    public Response getTempYearAggregate(@PathParam("iso3") String iso3) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(countryId, CruDao.VAR.temp, CruDao.Aggregation.YEAR);
        String response = CruExporter.formatCru(mediatype, crupr);

        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/temp/decade/{iso3 :(?i)\\w{3}(\\.\\w{1,5}|\\.\\w{1,5})?  }")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTempDecadeAggregate(@PathParam("iso3") String iso3) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(countryId, CruDao.VAR.temp, CruDao.Aggregation.DECADE);
        String response = CruExporter.formatCru(mediatype, crupr);

        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/temp/month/{iso3}")
    public Response getTempMonthAggregate(@PathParam("iso3") String iso3) {
        String identifier = RequestUtil.getIdentifier(iso3);
        String mediatype = RequestUtil.getResponseType(iso3);

        CountryService countryService = CountryService.get();
        int countryId = countryService.getId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(countryId, CruDao.VAR.temp, CruDao.Aggregation.MONTH);
        String response = CruExporter.formatCru(mediatype, crupr);

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
