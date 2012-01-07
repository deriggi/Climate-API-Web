/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oldv1;

/**
 *
 * @author wb385924
 */
import dao.GeoDao;
import dao.basin.BasinDao;
import dao.countryboundary.CountryPrecipitationDao;
import database.DBUtils;
import domain.Basin;
import domain.web.ShapeSvg;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

// The Java class will be hosted at the URI path "/helloworld"
@Path("/basin")
public class BasinResource {

    private String numbersPattern = "[0-9]{1,}";
    private Pattern p = null;

    // The Java method will process HTTP GET requests
    @GET
    // The Java method will produce content identified by the MIME Media
    // type "text/plain"
    @Produces(MediaType.APPLICATION_JSON)
    public List<Basin> getBasins() {

        return BasinDao.get().getBasins();
    }

    @GET
    @Path("/ids/{iso3}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getBasinIds(@PathParam("iso3") String iso3) {
        return null;



    }

    @GET
    @Path("/contains/{longitude},{latitude}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Basin> getContainingBasin(@PathParam("latitude") double latitude, @PathParam("longitude") double longitude) {
        BasinDao dao = BasinDao.get();

//        List<Basin> basins = dao.getBasinsContainingPoint(latitude, longitude);
        ;

        return null;
    }

    @GET
    @Path("/svg/{longitude},{latitude}")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<ShapeSvg> getBasinSvgs(@PathParam("latitude") double latitude, @PathParam("longitude") double longitude) {
        CountryPrecipitationDao dao = CountryPrecipitationDao.get();
        HashMap<String, String> props = dao.getRegionPropertiesContainingPoint(latitude, longitude);

        int countryId = -1;

        if (props.get("id") != null) {
            countryId = Integer.parseInt(props.get("id"));
        }

        return null;//(ArrayList<ShapeSvg>) BasinDao.get().getSVGBasinsForCountry(countryId);

    }

    @GET
    @Path("/svg/{nameorid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBasinSvgsFromCountryName(@PathParam("nameorid") String country, @Context HttpServletRequest request) {
//        int countryId = Integer.parseInt(country);

        ResponseBuilder builder = Response.ok("<res>" + request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + "</res>", MediaType.APPLICATION_XML);

        return builder.build();// (ArrayList<ShapeSvg>) BasinDao.get().getSVGBasinsForCountry(countryId);

    }

    @GET
    @Path("/kml/{wbhuc}")
    @Produces(MediaType.APPLICATION_XML)
    public String getKmlCountryBoundary(@PathParam("wbhuc") int wbhuc) {

        int basinId = -1;
        Connection c = DBUtils.getConnection();
        try {
            basinId = GeoDao.getEntityId(c, "basin", "code", wbhuc);
            String kml = GeoDao.getGeometryAsKML(c, "basin", "geom", "id", basinId);
            return kml;
        } finally {
            DBUtils.close(c);
        }




    }
}
