/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputservlet;

import asciipng.GeometryBuilder;
import com.google.gson.Gson;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import cru.precip.GeometryCruCsvWriterRunner;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import shapefileloader.userdefinedregions.UserDefinedRegionService;

/**
 *
 * @author wb385924
 */
public class UserDefinedRegionServlet extends HttpServlet {

    private static int lameCounter = 0;
    
    private static final Logger log = Logger.getLogger(UserDefinedRegionServlet.class.getName());
    
    private static final String starterFile = "C:\\Users\\Johnny\\Dropbox\\CRU\\tmp\\tmp\\cru_ts_3_10.1901.2009.tmp_1961_1.asc";
    private static final  String cruDirectory = "C:\\Users\\Johnny\\Dropbox\\CRU\\tmp\\tmp\\";
//    private static final String outputDirectory = "C:\\Users\\Johnny\\webappmonthlyCRUoutput\\";
    
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        response.setHeader("Content-Type", getServletContext().getMimeType("yourregion.png"));
//        response.setHeader("Content-Disposition", "attachment;filename=\"yourregion.png\"");

        String userRegion = request.getParameter("region");

        UserDefinedRegionService service = UserDefinedRegionService.get();
        double[][] points = service.convertPointStringToArray(userRegion); // should be five points
        
        List<Point> pointList = service.convertArrayToGeometry(points);
        Polygon poly = GeometryBuilder.createPolygonFromCoordList(pointList);

        HashMap<String, Geometry> geomMap = new HashMap<String, Geometry>();
        String projectName=  "lameName_" + lameCounter++;
        geomMap.put(projectName, poly);

        String outuptDirectory = new File(getServletContext().getRealPath("/webappoutput/" + projectName)).getAbsolutePath() + "\\";
        
        GeometryCruCsvWriterRunner.doTask(starterFile, cruDirectory, outuptDirectory, geomMap, projectName);
        
        
        Writer outputWriter = response.getWriter();
        response.setContentType("application/json");
        HashMap<String,String> quickObject = new HashMap<String,String>();
        quickObject.put("path", outuptDirectory);
        quickObject.put("cachekey", projectName);
        
        outputWriter.write(new Gson().toJson(quickObject));
        
        outputWriter.flush();
        outputWriter.close();

        
        //        
//        UserDefinedRegionShapeFileFilter shapeFileFilter = new UserDefinedRegionShapeFileFilter();
////        List<GridCell> cells = shapeFileFilter.readShapeFileForAnnualData("/data/GCM_2deg/GCM/GCM_long_clim_monthly.shp/GCM_long_clim_monthly.shp/bccr_bcm2_0/pcmdi_long_clim_annual.bccr_bcm2_0.pr_20c3m.1920-1939.shp", poly);
//        List<GridCell> cells = shapeFileFilter.readShapeFileForAnnualData("C:\\Users\\wb385924\\OLD_CLIMATE_DATA\\GCM\\GCM_long_clim_annual.shp\\GCM_long_clim_annual.shp\\bccr_bcm2_0\\pcmdi_long_clim_annual.bccr_bcm2_0.pr_20c3m.1920-1939.shp", poly);
////        new File()
//        new CellMapMaker().drawToStream(cells, response.getOutputStream(), poly);
//        String rootPath = getServletContext().getRealPath("/userimages/");




    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
