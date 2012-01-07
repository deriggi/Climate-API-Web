/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package export;



import database.DBUtils;
import export.util.FileExportHelper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wb385924
 */
public class BasinSVGExporter {

    private static Logger log = Logger.getLogger(BasinSVGExporter.class.getName());
    
    private final String GET_SCALED_BASIN_SVG = "select basin_name, basin_will_id,country_iso_3,   st_assvg( st_scale(basin_geom,10.0,10.0) ,1,5 ) , country_iso_3 from basin inner join country on basin_country_id = country_id where basin_id = ?";


    private final String GET_SIMPLE_SCALED_BASIN_SVG = "select basin_name, basin_will_id, country_iso_3, st_assvg(  st_scale(st_simplify(basin_geom,0.09),10.0,10.0) ,1,5 ), country_iso_3 from basin inner join country on basin_country_id = country_id where basin_id = ?";
    private final String GET_BASIN_IDS_WHERE_COUNTRY_BOUNDARIES_EXIST = "select  basin_id from basin inner join country_boundary on basin_country_id = country_boundary_country_id inner join country on basin_country_id = country_id ";

    public static void main(String[] args){
        new BasinSVGExporter().iterateThrough();
    }
    /**
     * The goal is to create a json entity like { iso3 : MOZ, basinSVG:asdfasdfasdf}
     */
    public void iterateThrough() {
        Connection c = DBUtils.getConnection();
        PreparedStatement ps = null;
        int basinId=0;
        try {

            ps = c.prepareStatement(GET_BASIN_IDS_WHERE_COUNTRY_BOUNDARIES_EXIST);
            ResultSet rs = ps.executeQuery();

            int count = 0;
            while(rs.next()){
                basinId = rs.getInt(1);
                String line = getSimpleSVG(basinId);
                if(line == null){
                    line = getScaledSVG(basinId);
                }

                if(line != null){
                    FileExportHelper.appendToFile("basinswithnames.txt",line);
                    count++;
                }else{
                    System.out.println("file is null");
                }
                System.out.println("total file count is " + count++);
                
                //log.log(Level.INFO, "we got {0} and {1} ",new String[]{basinId,svg});
            }


        } catch (SQLException ex) {
            log.log(Level.SEVERE,"error exporting for " + basinId, ex);
        } finally{
            DBUtils.close(c, ps, null);
        }
    }

    private String getSimpleSVG(int id){
        Connection c = DBUtils.getConnection();
        PreparedStatement ps = null;
        try {

            ps = c.prepareStatement(GET_SIMPLE_SCALED_BASIN_SVG);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            StringBuilder sb = new StringBuilder();
            if(rs.next()){
                String basinName = rs.getString(1);
                String will_id = rs.getString(2);
                String iso3 = rs.getString(3);
                String svg = rs.getString(4);

                sb.append(will_id);
                sb.append("|");
                sb.append(basinName);
                sb.append("|");
                sb.append(iso3);
                sb.append("|");
                sb.append(svg);
                
                log.log(Level.FINE, "we got {0} and {1} ",new String[]{iso3,svg});
                return sb.toString();
            }


        } catch (SQLException ex) {
            log.log(Level.SEVERE,"error exporting for " + id, ex);
        } finally{
            DBUtils.close(c, ps, null);
        }
        return null;
    }

    private String getScaledSVG(int id){
        Connection c = DBUtils.getConnection();
        PreparedStatement ps = null;
        StringBuilder sb = new StringBuilder();
        try {

            ps = c.prepareStatement(GET_SCALED_BASIN_SVG);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String basinName = rs.getString(1);
                String will_id = rs.getString(2);
                String iso3 = rs.getString(3);
                String svg = rs.getString(4);

                sb.append(will_id);
                sb.append("|");
                sb.append(basinName);
                sb.append("|");
                sb.append(iso3);
                sb.append("|");
                sb.append(svg);

                log.log(Level.FINE, "we got {0} and {1} ",new String[]{iso3,svg});
                log.log(Level.INFO, "success {0} ",id);
                return sb.toString();
            }


        } catch (SQLException ex) {
            log.log(Level.SEVERE,"error exporting for " + id, ex);
        } finally{
            DBUtils.close(c, ps, null);
        }
        return null;
    }

    private void appendToBarSeparatedFile(String barSeparated) {
        FileWriter fw = null;
        try {


            File f = new File("outfile.txt");
             fw = new FileWriter(f, true);
            fw.append(barSeparated);
            fw.append(System.getProperty("line.separator"));



        } catch (IOException ex) {
            log.log(Level.SEVERE, "error writing ", ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                log.log(Level.SEVERE, "error writing ", ex);
            }
        }


    }
}
