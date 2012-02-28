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
import dao.GeoDao;
import dao.carbonintensitywri.CarbonIntensityWriDao;
import dao.country.CountryDao;
import dao.droughtaffected.DroughtAffectedDao;
import dao.percapitaco2.PerCapitaCo2Dao;
import dao.totalco2.TotalCo2Dao;
import database.DBUtils;
import domain.CarbonIntensityWri;
import domain.Country;
import domain.DroughtAffected;
import domain.ExtDataHelper;
import domain.PerCaptiaCo2;
import domain.TotalCo2;
import domain.web.ShapeSvg;
import java.sql.Connection;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import service.CountryService;

// The Java class will be hosted at the URI path "/helloworld"
@Path("/v1/c")
public class CountryResource {

    private String numbersPattern = "[0-9]{1,}";
    private Pattern p = null;
    // The Java method will process HTTP GET requests

    
    @GET
    @Path("/search/")
    @Produces("application/json")
    public String searchCountries(@QueryParam("term") String searchTerm) {
        return null;//new Gson().toJson(CountryDao.get().searchCountries(searchTerm));

    }

    @GET
    @Path("/svg/{nameorid}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSvgCountryBoundary(@PathParam("nameorid") String country) {
        if (p == null) {
            p = Pattern.compile(numbersPattern);
        }
        int countryId = -1;
        if (!p.matcher(country).matches()) {

           countryId = CountryService.get().getId(country);
        } else {

            countryId = Integer.parseInt(country);
        }

        return new Gson().toJson(CountryDao.get().getSVGBoundaryForCountry(countryId));

    }

    @GET
    @Path("/svg/")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllCountrySVG() {


        return new Gson().toJson((ArrayList<ShapeSvg>) CountryDao.get().getAllSVGBoundary());

    }

    @GET
    @Path("/kml/{iso3}")
    @Produces(MediaType.APPLICATION_XML)
    public String getKmlCountryBoundary(@PathParam("iso3") String iso3) {

        int countryId = -1;
        if (iso3 != null && iso3.trim().length() == 3) {
            Connection c = DBUtils.getConnection();
            try {
                countryId = CountryService.get().getId(iso3);
                String kml = GeoDao.getGeometryAsKML(c, "boundary", "shape", "area_id", countryId);
                StringBuilder sb = new StringBuilder();

                sb.append("<kml><Document><Placemark>");
                sb.append(kml);
                sb.append("</Placemark></Document></kml>");


                
                return kml;
            } finally {
                DBUtils.close(c);
            }
            
        }
        return "<kml></kml>";


    }
    

    @GET
    @Path("/simplekml/{iso3}")
    @Produces(MediaType.APPLICATION_XML)
    public String getSimpleKmlCountryBoundary(@PathParam("iso3") String iso3) {

        int countryId = -1;
        if (iso3 != null && iso3.trim().length() == 3) {

            Connection c = DBUtils.getConnection();
            try {
                countryId = countryId = CountryService.get().getId(iso3);
                String kml = GeoDao.getGeometryAsKML(c, "boundary", "simple", "area_id", countryId);
                return kml;
            } finally {
                DBUtils.close(c);
            }

        }
        return "<kml></kml>";


    }

    @GET
    @Path("/simplekml/{lon},{lat}")
    @Produces(MediaType.APPLICATION_XML)
    public String getSimpleKmlCountryBoundaryFromPoint(@PathParam("lon") double lon, @PathParam("lat") double lat) {

        String kml = null;
        Connection c = DBUtils.getConnection();
        try {
            GeoDao.getIdOfRegionContainingPoint(c, lat, lon, "boundary", "simple", "area_id");
            Country country = CountryDao.get().getCountryFromPoint(lon, lat);
            kml = GeoDao.getGeometryAsKML(c, "boundary", "simple", "area_id", country.getId());
        } finally {
            DBUtils.close(c);
        }
        if (kml != null) {
            return kml;
        } else {
            return "<kml></kml>";
        }






    }

    @GET
    @Path("/mitigation/")
    @Produces("application/json")
    public List<ExtDataHelper> getMitigation(@QueryParam("countryid") int countryId) {
        // todo add all of these to an country object and return it to the web

//        Country c = CountryDao.get().
        Country c = new Country();
        ExtDataHelper data = new ExtDataHelper();

        List<TotalCo2> totalCo2 = TotalCo2Dao.get().getTotalCo2Data(countryId);
        if (totalCo2.size() > 0) {
            c.setTotalCo2(totalCo2.get(0));

        }

        List<DroughtAffected> droughtAffected = DroughtAffectedDao.get().getDroughtAffectedData(countryId);
        if (droughtAffected.size() > 0) {
            c.setDroughtAffected(droughtAffected.get(0));

        }

        List<PerCaptiaCo2> perCapitaCo2 = PerCapitaCo2Dao.get().getTotalCo2Data(countryId);
        if (perCapitaCo2.size() > 0) {
            c.setPerCapitaCo2(perCapitaCo2.get(0));

        }

        List<CarbonIntensityWri> carbonIntensity = CarbonIntensityWriDao.get().getCarbonIntensityData(countryId);
        if (carbonIntensity.size() > 0) {
            c.setCarbonIntensityWri(carbonIntensity.get(0));

        }

        List<ExtDataHelper> dataHelpers = new ArrayList<ExtDataHelper>();

        data.init(c);
        dataHelpers.add(data);
        //dataHelpers.add(new ExtDataHelper());
        //data.getData().add(new Country());
        return dataHelpers;

    }
}
