/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mathutil;

import domain.web.MinimizedData;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import sdnis.wb.util.BasicAverager;

/**
 *
 * @author wb385924
 */
public class DataAverager {

    private static final Logger log = Logger.getLogger(DataAverager.class.getName());
    
    public HashMap<Integer,BasicAverager> deriveMonthlyStats(List<MinimizedData> data){
        HashMap<Integer,BasicAverager> averagerMap = new HashMap<Integer,BasicAverager>();
        if(data == null){
            return averagerMap;
        }
        for(MinimizedData minData : data){
            int monthKey = getMonthFromDate(minData.getDate());
            if(!averagerMap.containsKey(monthKey)){
                averagerMap.put(monthKey, new BasicAverager());
            }
            averagerMap.get(monthKey).update(minData.getValue());
        }
        return averagerMap;
    }

    public HashMap<Integer,Double> deriveMonthlyAverage(List<MinimizedData> data){

        HashMap<Integer,BasicAverager> statsMap = deriveMonthlyStats(data);
        Set<Integer> keys = statsMap.keySet();
        HashMap<Integer,Double> averages = new HashMap<Integer,Double>();
        
        for(Integer k:keys){
            averages.put(k, statsMap.get(k).getAvg());
        }

        return averages;
    }

    private int getMonthFromDate(Date date){
        if(date == null){
            log.warning("date param is null in averager");
            return -1;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        
        return c.get(Calendar.MONTH);
    }

}
