/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package resources.disputedborder;

/**
 *
 * @author wb385924
 */
import dao.basin.BasinDao;
import dao.disputedborder.DisputedBorderDao;
import domain.Basin;
import java.sql.Connection;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import service.CountryService;

// The Java class will be hosted at the URI path "/helloworld"
@Path("/disputed")
public class DisputedBorderResource {


    
    @GET
    @Path("/kml/{iso3: \\w{3}}")
    @Produces(MediaType.APPLICATION_XML)
    public String getDisputedBordersForCountry(@PathParam("iso3") String iso3) {
       int id = CountryService.get().getId(iso3);
       String kml = DisputedBorderDao.get().getDisputedBorderKMLForCountry(id);

        return kml;



    }



    @GET
    @Path("/kml/")
    @Produces(MediaType.APPLICATION_XML)
    public String getDisputedBorders(@PathParam("iso3") String iso3) {
       String kml = DisputedBorderDao.get().getDisputedBorder();

        return kml;



    }

   
}
