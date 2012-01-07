/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oldv1;

/**
 *
 * @author wb385924
 */
import util.ResponseConstants;
import dao.GeoDao;
import dao.basin.BasinDao;
import database.DBUtils;
import domain.Basin;
import java.sql.Connection;
import java.util.List;
import java.util.regex.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

// The Java class will be hosted at the URI path "/helloworld"
@Path("/region")
public class RegionResource {

    private String lettersPattern = "[a-zA-Z]{2}";
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
    @Path("/kml/{code}")
    @Produces(MediaType.APPLICATION_XML)
    public String getKmlCountryBoundary(@PathParam("code") String code) {
        if (code == null || !Pattern.matches(lettersPattern, code)) {
            return ResponseConstants.INVALID_PARAMS;
        }

//        DBUtils db = DBUtils.get();
        Connection c = DBUtils.getConnection();
        String kml;
        try {
            kml = GeoDao.getGeometryAsKML(c, "ckp_region", "geom", "code", code.toUpperCase());
        } finally {
            DBUtils.close(c);
        }
        return kml;


    }
}
