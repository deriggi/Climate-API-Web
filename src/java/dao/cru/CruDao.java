/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dao.cru;

import database.DBUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wb385924
 */
@Deprecated
public class CruDao {
    private static CruDao dao = null;
    public static CruDao getInstance(){
        if(dao == null){
            dao  = new CruDao();
        }
        return dao;
    }

    private CruDao(){
    }

    public ArrayList<HashMap<String, Object>> getCruDataForPoint(int pointId, Date fromDate, Date toDate) {
        Connection c = database.DBUtils.getConnection();
        PreparedStatement ps = null;
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        ResultSet rs = null;
        try {

            ps = c.prepareStatement("select cru_pr_data, cru_pr_date from cru_pr where cru_pr_date >= ? and cru_pr_date < ? and cru_pr_point_id = ? order by cru_pr_date");
            ps.setDate(1, new java.sql.Date(fromDate.getTime()));
            ps.setDate(2, new java.sql.Date(toDate.getTime()));
            ps.setInt(3, pointId);
            rs = ps.executeQuery();
            while (rs.next()) {
                dataMap = new HashMap<String,Object>();
                dataMap.put("d", rs.getDate("cru_pr_date"));
                dataMap.put("v", rs.getDouble("cru_pr_data"));
                list.add(dataMap);
            }


        } catch (SQLException ex) {
            Logger.getLogger(CruDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBUtils.close(c, ps, rs);
        }

        return list;

    }


}
