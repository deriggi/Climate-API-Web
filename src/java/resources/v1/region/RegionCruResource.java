/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resources.v1.region;

import cru.adapter.WebCruAdapter;
import cru.precip.CruDao;
import dao.ckpregion.CkpRegionDao;
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
@Path("/v1/region/cru")
public class RegionCruResource {

    private static Logger log = Logger.getLogger(RegionCruResource.class.getName());

    @GET
    @Path("/pr/{fyear}-{tyear}/{rcode : (?i)\\w{2}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response getPrecipData(@PathParam("rcode") String rcode, @PathParam("fyear") int fyear, @PathParam("tyear") int tyear) {
        String identifier = RequestUtil.getIdentifier(rcode);
        String mediatype = RequestUtil.getResponseType(rcode);

        int regionId = CkpRegionDao.get().getRegionId(identifier);

        List<Cru> crupr = CruDao.get().getCru(regionId, CruDao.VAR.pr, fyear, tyear);
        String response = CruExporter.formatCru(mediatype, crupr);
        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/pr/year/{rcode : (?i)\\w{2}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response getPrecipYearAggregate(@PathParam("rcode") String rcode) {
        String identifier = RequestUtil.getIdentifier(rcode);
        String mediatype = RequestUtil.getResponseType(rcode);

        int regionId = CkpRegionDao.get().getRegionId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(regionId, CruDao.VAR.pr, CruDao.Aggregation.YEAR);

        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/dl/pr/year/{rcode : (?i)\\w{2}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response downloadPrecipYearAggregate(@PathParam("rcode") String rcode) {
        String identifier = RequestUtil.getIdentifier(rcode);
        String mediatype = RequestUtil.getResponseType(rcode);

        int regionId = CkpRegionDao.get().getRegionId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(regionId, CruDao.VAR.pr, CruDao.Aggregation.YEAR);

        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        ResponseBuilder rb = WebService.get().getProperResponse(response, WebService.Delivery.DOWNLOAD, mediatype);
        return rb.build();
    }

    @GET
    @Path("/pr/decade/{rcode : (?i)\\w{2}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response getPrecipDecadeAggregate(@PathParam("rcode") String rcode) {
        String identifier = RequestUtil.getIdentifier(rcode);
        String mediatype = RequestUtil.getResponseType(rcode);

        int regionId = CkpRegionDao.get().getRegionId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(regionId, CruDao.VAR.pr, CruDao.Aggregation.DECADE);

        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/dl/pr/decade/{rcode : (?i)\\w{2}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response downloadPrecipDecadeAggregate(@PathParam("rcode") String rcode) {
        String identifier = RequestUtil.getIdentifier(rcode);
        String mediatype = RequestUtil.getResponseType(rcode);

        int regionId = CkpRegionDao.get().getRegionId(identifier);
        List<Cru> crupr = CruDao.get().getCruAggregation(regionId, CruDao.VAR.pr, CruDao.Aggregation.DECADE);

        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);

        ResponseBuilder rb = WebService.get().getProperResponse(response, WebService.Delivery.DOWNLOAD, mediatype);
        return rb.build();
    }

    @GET
    @Path("/pr/month/{rcode: (?i)\\w{2}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response getPrecipMonthAggregate(@PathParam("rcode") String rcode) {
        String identifier = RequestUtil.getIdentifier(rcode);
        String mediatype = RequestUtil.getResponseType(rcode);

        int regionId = CkpRegionDao.get().getRegionId(identifier);
        List<Cru> crupr = CruDao.get().getCruAggregation(regionId, CruDao.VAR.pr, CruDao.Aggregation.MONTH);

        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/dl/pr/month/{rcode: (?i)\\w{2}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response downloadPrecipMonthAggregate(@PathParam("rcode") String rcode) {
        String identifier = RequestUtil.getIdentifier(rcode);
        String mediatype = RequestUtil.getResponseType(rcode);

        int regionId = CkpRegionDao.get().getRegionId(identifier);
        List<Cru> crupr = CruDao.get().getCruAggregation(regionId, CruDao.VAR.pr, CruDao.Aggregation.MONTH);

        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        ResponseBuilder rb = WebService.get().getProperResponse(response, WebService.Delivery.DOWNLOAD, mediatype);
        return rb.build();
    }

     @GET
    @Path("/tas/{fyear}-{tyear}/{rcode : (?i)\\w{2}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response getTempData(@PathParam("rcode") String rcode, @PathParam("fyear") int fyear, @PathParam("tyear") int tyear) {
        String identifier = RequestUtil.getIdentifier(rcode);
        String mediatype = RequestUtil.getResponseType(rcode);

        int regionId = CkpRegionDao.get().getRegionId(identifier);

        List<Cru> crupr = CruDao.get().getCru(regionId, CruDao.VAR.temp, fyear, tyear);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        return Response.ok(response, mediatype).build();

    }

    @GET
    @Path("/tas/year/{rcode  : (?i)\\w{2}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response getTempYearAggregate(@PathParam("rcode") String rcode) {
        String identifier = RequestUtil.getIdentifier(rcode);
        String mediatype = RequestUtil.getResponseType(rcode);

        int regionId = CkpRegionDao.get().getRegionId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(regionId, CruDao.VAR.temp, CruDao.Aggregation.YEAR);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/dl/tas/year/{rcode  : (?i)\\w{2}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response downloadTempYearAggregate(@PathParam("rcode") String rcode) {
        String identifier = RequestUtil.getIdentifier(rcode);
        String mediatype = RequestUtil.getResponseType(rcode);

        int regionId = CkpRegionDao.get().getRegionId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(regionId, CruDao.VAR.temp, CruDao.Aggregation.YEAR);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);

        ResponseBuilder rb = WebService.get().getProperResponse(response, WebService.Delivery.DOWNLOAD, mediatype);
        return rb.build();
    }

    @GET
    @Path("/dl/tas/decade/{rcode : (?i)\\w{2}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response downloadTempDecadeAggregate(@PathParam("rcode") String rcode) {
        String identifier = RequestUtil.getIdentifier(rcode);
        String mediatype = RequestUtil.getResponseType(rcode);

        int regionId = CkpRegionDao.get().getRegionId(identifier);


        List<Cru> crupr = CruDao.get().getCruAggregation(regionId, CruDao.VAR.temp, CruDao.Aggregation.DECADE);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);

        ResponseBuilder rb = WebService.get().getProperResponse(response, WebService.Delivery.DOWNLOAD, mediatype);
        return rb.build();
    }


    @GET
    @Path("/tas/decade/{rcode : (?i)\\w{2}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response getTempDecadeAggregate(@PathParam("rcode") String rcode) {
        String identifier = RequestUtil.getIdentifier(rcode);
        String mediatype = RequestUtil.getResponseType(rcode);

        int regionId = CkpRegionDao.get().getRegionId(identifier);


        List<Cru> crupr = CruDao.get().getCruAggregation(regionId, CruDao.VAR.temp, CruDao.Aggregation.DECADE);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        return Response.ok(response, mediatype).build();
    }

    @GET
    @Path("/dl/tas/month/{rcode : (?i)\\w{2}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response downloadTempMonthAggregate(@PathParam("rcode") String rcode) {
        String identifier = RequestUtil.getIdentifier(rcode);
        String mediatype = RequestUtil.getResponseType(rcode);

        int regionId = CkpRegionDao.get().getRegionId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(regionId, CruDao.VAR.temp, CruDao.Aggregation.MONTH);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        ResponseBuilder rb = WebService.get().getProperResponse(response, WebService.Delivery.DOWNLOAD, mediatype);
        return rb.build();
    }

    @GET
    @Path("/tas/month/{rcode : (?i)\\w{2}(\\.\\w{1,5}|\\.\\w{1,5})?}")
    public Response getTempMonthAggregate(@PathParam("rcode") String rcode) {
        String identifier = RequestUtil.getIdentifier(rcode);
        String mediatype = RequestUtil.getResponseType(rcode);

        int regionId = CkpRegionDao.get().getRegionId(identifier);

        List<Cru> crupr = CruDao.get().getCruAggregation(regionId, CruDao.VAR.temp, CruDao.Aggregation.MONTH);
        List<V1WebCru> webCrus = new WebCruAdapter().eatCrus(crupr);
        String response = CruExporter.formatCru(mediatype, webCrus);
        return Response.ok(response, mediatype).build();
    }
}
