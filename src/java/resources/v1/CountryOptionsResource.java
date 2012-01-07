/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resources.v1;

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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.ResponseConstants;
import service.CountryService;
import service.WebService;
import shapefileloader.gcm.P_GcmConfig;
import shapefileloader.gcm.P_GcmStatsProperties;
import util.RequestUtil;

// The Java class will be hosted at the URI path "/helloworld"
@Path("/v1/options/country/")
public class CountryOptionsResource {

    private List<String> getDataSetsDelegate(String identifier, int areaId, HttpServletRequest request, P_GcmStatsProperties.stat_type statType, boolean isAnnual) {
        P_AreaMetadataDao dao = P_AreaMetadataDao.get();
        P_GcmConfig config = new P_GcmConfig();
        config.setAreaId(areaId);
        config.setStatType(statType);

        List<P_GcmConfig> configs = dao.getOptions(config, isAnnual);

        List<String> urls = new ArrayList<String>();
        String preAmble = getPreamble(request, statType, isAnnual);
        String forwardSlash = "/";
        for (P_GcmConfig c : configs) {
            StringBuilder sb = new StringBuilder();
            sb.append(preAmble);
            sb.append(forwardSlash);
            sb.append(c.toUrlPart());
            sb.append(identifier);
            urls.add(sb.toString());
        }
        return urls;
    }

    @GET
    @Path("/{tempagg}/{where : \\w{3}}")
    public Response getDatasets(@PathParam("where") String where, @PathParam("tempagg") String tempagg, @Context HttpServletRequest request) {
        P_GcmStatsProperties.stat_type statType = WebService.get().getStatType(tempagg);
        if (statType == null) {
            return Response.ok(ResponseConstants.INVALID_PARAMS, MediaType.APPLICATION_JSON).build();
        }
        String mediaType = RequestUtil.getResponseType(where);
        where = RequestUtil.getIdentifier(where);
        int countryId = CountryService.get().getId(where);
        if (countryId == -1) {
            return Response.ok(ResponseConstants.INVALID_ISO, MediaType.APPLICATION_JSON).build();
        }

        List<String> urls = getDataSetsDelegate(where, countryId, request, WebService.get().getStatType(tempagg), WebService.get().isAnnual(tempagg));
        Gson gson = new Gson();
        String formattedData = gson.toJson(urls);
        Response.ResponseBuilder r = null;

        r = Response.ok(formattedData, mediaType);

        return r.build();
    }

    private String getPreamble(HttpServletRequest request, P_GcmStatsProperties.stat_type type, boolean isAnnual) {
        String forwardSlash = "/";
        StringBuilder sb = new StringBuilder();
        sb.append(request.getScheme());
        sb.append("://");
        sb.append(request.getServerName());
        sb.append(":");
        sb.append(request.getServerPort());
        sb.append(request.getContextPath());
        sb.append(forwardSlash);
        if (isAnnual) {
            sb.append(getAnnualRestPart(type));
        } else {
            sb.append(getMonthlyRestPart(type));
        }

        return sb.toString();
    }

    private String getMonthlyRestPart(P_GcmStatsProperties.stat_type statType) {
        if (statType.equals(P_GcmStatsProperties.stat_type.anom)) {
            return "rest/v1/country/manom";
        } else if (statType.equals(P_GcmStatsProperties.stat_type.mean)) {
            return "rest/v1/country/mavg";
        }

        return "rest/v1/country/mavg";
    }

    private String getAnnualRestPart(P_GcmStatsProperties.stat_type statType) {
        if (statType.equals(P_GcmStatsProperties.stat_type.anom)) {
            return "rest/v1/country/annualanom";
        } else if (statType.equals(P_GcmStatsProperties.stat_type.mean)) {
            return "rest/v1/country/annualavg";
        }

        return "rest/v1/country/annualavg";
    }
}
