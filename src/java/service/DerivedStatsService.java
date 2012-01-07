/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.country.CountryDao;
import dao.countryboundary.CountryPrecipitationDao;
import dao.deriveddata.DerivedDataDao;
import domain.DerivativeStats;
import domain.web.MinimizedData;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import util.ClimateDateUtils;

/**
 *
 * @author wb385924
 */
public class DerivedStatsService {

    private static DerivedStatsService dss = null;
    private static final Logger log = Logger.getLogger(DerivedStatsService.class.getName());

    public static DerivedStatsService get() {
        if (dss == null) {
            dss = new DerivedStatsService();
        }
        return dss;
    }

    public HashMap<String, String> getCountryProperties(double latitude, double longitude) {
        CountryPrecipitationDao dao = CountryPrecipitationDao.get();
        HashMap<String, String> props = dao.getRegionPropertiesContainingPoint(latitude, longitude);


        return props;
    }

    public int getCountryId(double latitude, double longitude) {
        int id = -1;
        String id_ = "id";
        HashMap<String, String> props = new HashMap<String, String>();
        try {
            props = getCountryProperties(latitude, longitude);
            if (props.get(id_) != null) {
                id = Integer.parseInt(props.get(id_));
            }
        } catch (NumberFormatException nfe) {
            log.warning(nfe.getMessage());
            return id;
        }

        return id;
    }

    


    public List<MinimizedData> getMonthlyCountryData(String gcmName,
            String scenarioName,
            String statName,
            int fyear,
            int tyear,
            double latitude,
            double longitude) {

        Date fromDate = ClimateDateUtils.getFirstDateOfYear(fyear);
        Date toDate = ClimateDateUtils.getLastDateOfYear(tyear);

        int countryId = getCountryId(latitude, longitude);
        DerivativeStats derivStats = DerivativeStats.getInstance();
        DerivativeStats.gcm gcm = derivStats.getGcm(gcmName);
        DerivativeStats.scenario scenario = derivStats.getScenario(scenarioName);
        DerivativeStats.climatestat stat = derivStats.getClimateStat(statName);
        if (gcm == null || scenario == null || stat == null) {
            log.log(Level.WARNING, "something is null{0} {1} {2}", new Object[]{gcm, scenario, stat});
            return null;
        }
        List<MinimizedData> data = DerivedDataDao.get().getDerivedStatByCountryTimePeriod(gcm, stat, scenario, DerivativeStats.stat_type.mean, DerivativeStats.temporal_aggregation.monthly, fromDate, toDate, 1, countryId);

        return data;

    }
}
