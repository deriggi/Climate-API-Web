/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.thoughtworks.xstream.XStream;
import domain.web.SimpleClimateData;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import shapefileloader.gcm.P_GcmConfig;
import shapefileloader.gcm.P_GcmConfigAreaDao;
import shapefileloader.gcm.P_GcmConfigDao;
import shapefileloader.gcm.P_GcmStatsProperties;
import shapefileloader.gcm.P_GcmStatsProperties.stat_type;
import shapefileloader.gcm.P_GcmStatsProperties.scenario;


/**
 * needs to be updated to handle generic p_configs
 * @author wb385924
 */

public class ClimateExchangeService {

    private static ClimateExchangeService cis = new ClimateExchangeService();

    public static ClimateExchangeService get() {
        return cis;
    }

    public String toXML(List<SimpleClimateData> data) {
        XStream xstream = new XStream();
        return xstream.toXML(data);
    }

    public void sendData(List<P_GcmConfig> configs) throws Exception{
        String xml = new XStream().toXML(configs);
	URL url = new URL("http://64.95.129.89:8080/climateweb/testpost");
	URLConnection connection = url.openConnection();
	connection.setDoOutput(true);
	OutputStreamWriter out = new OutputStreamWriter(
                              connection.getOutputStream());
	out.write("string=" + xml);
	out.close();
	BufferedReader in = new BufferedReader(
				new InputStreamReader(
				connection.getInputStream()));
	String decodedString;
	while ((decodedString = in.readLine()) != null) {
	    System.out.println(decodedString);
	}
	in.close();
    }



    public synchronized void saveConfigs(String xml) {
        List<P_GcmConfig> confs =  (List<P_GcmConfig>) new XStream().fromXML(xml);
        if(confs == null){
            return;
        }
        P_GcmConfigDao dao = P_GcmConfigDao.get();
        P_GcmConfigAreaDao areaDao = P_GcmConfigAreaDao.get();
        
        CountryService cs = CountryService.get();
        P_GcmStatsProperties ds = P_GcmStatsProperties.getInstance();
        for(P_GcmConfig conf: confs){
            conf.setAreaId(cs.getId(conf.getIso3()));
            ds.reset(conf);
            int id = dao.getConfigId(conf);
            areaDao.insertAreaValue(id, conf.getAreaId(), conf.getValue());

//            System.out.println("server received month of " + conf.getMonth());
            
        }

//        dao.saveMonthlyData(confs.get(0));
    }

    

    public static void main(String[] args) {
        try {
            P_GcmConfig config = new P_GcmConfig(stat_type.mean, P_GcmStatsProperties.gcm.cnrm_cm3, scenario.a1b, P_GcmStatsProperties.precipstat.pr, 1920, 1939);
            config.setIso3("MOZ");
            config.setMonth(5);
            config.setValue(12.3);
            List<P_GcmConfig> configs = new ArrayList<P_GcmConfig>();
            configs.add(config);
            ClimateExchangeService.get().sendData(configs);
            //        SimpleClimateData scd = new SimpleClimateData("COD", "bccr", "pr", "mean", "a2", 1920, 1939, 1, 12.0f);
            //        List<SimpleClimateData> scds = new ArrayList<SimpleClimateData>();
            //        scds.add(scd);
            //
            //        String xml = new XStream().toXML(scds);
        } catch (Exception ex) {
            Logger.getLogger(ClimateExchangeService.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}
