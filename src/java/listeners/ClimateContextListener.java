package listeners;

import database.DBUtils;
import domain.DerivativeStats;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import service.CountryService;
import shapefileloader.gcm.P_GcmStatsProperties;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Web application lifecycle listener.
 * @author wb385924
 */
public class ClimateContextListener implements ServletContextListener {

    private static Logger log = Logger.getLogger(ClimateContextListener.class.getName());

    public void contextInitialized(ServletContextEvent sce) {
//            prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(DBUtils.propfile));
//            String url = prop.getProperty("dburl");
//            String username = prop.getProperty("user");
//            String password = prop.getProperty("password");
//            DBUtils.get(url, username, password);
            P_GcmStatsProperties.getInstance();
            DerivativeStats.getInstance();
            CountryService.get();
    }

    public void contextDestroyed(ServletContextEvent sce) {
        DBUtils.closeAll();


        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                log.log(Level.INFO, String.format("deregistering jdbc driver: %s", driver));
            } catch (SQLException e) {
                log.log(Level.SEVERE, String.format("Error deregistering driver %s", driver), e);
            }

        }
    }
}
