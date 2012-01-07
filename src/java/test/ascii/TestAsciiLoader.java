/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.ascii;

import ascii.AsciiDataLoader;
import ascii.CacheDataStoreAsciiAction;
import ascii.NetAsciiLoader;
import export.util.FileExportHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import tnccsv.TNCDateFromFileNameExtractor;

/**
 *
 * @author wb385924
 */
public class TestAsciiLoader {

    public static void testDataStoreCache() {
        try {
            long t0 = Calendar.getInstance().getTimeInMillis();


//            String fileUrl = "http://204.236.240.64/afrMENA_countries_monthly_a2b1_midCen/afrMENA_countries_a2b1_monthly_midCen/CAF/cd18/cccma_cgcm3_1.1_a2/map_mean_AR4_Global_Extr_50k_cccma_cgcm3_1.1_a2_cd18_5_2046_2065.asc";
//            String fileUrl = "S:/GLOBAL/ClimatePortal_2/RENDERING/JohnnyDAutoDownload/cd18/map_mean_ensemble_50_AR4_Global_Extr_50k_a1b_cd18_14_2046_2065.asc";

            String topOfTheRoot = "S:/GLOBAL/ClimatePortal_2/RENDERING/JohnnyDAutoDownload/";
            String[] varFolders = new File(topOfTheRoot).list();
            for (String varFolder : varFolders) {
                System.out.println("looking at " + varFolder);
                if(!new File(topOfTheRoot+varFolder).isDirectory()){

                    continue;
                }
                String rootDirectory = topOfTheRoot+varFolder;
                File folder = new File(rootDirectory);
                File[] list = folder.listFiles();

//            URL oracle = new URL(fileUrl);
//            URLConnection yc = oracle.openConnection();

                int count = 0;
                for (File f : list) {
                    if(!f.getName().endsWith(".asc") && !f.getName().endsWith(".ASC")){
                        System.out.println("ignoreing " + f.getName());
                        continue;
                    }
                    CacheDataStoreAsciiAction cacheDataStoreAction = new CacheDataStoreAsciiAction();
                    TNCDateFromFileNameExtractor dateExtractor = new TNCDateFromFileNameExtractor();
                    dateExtractor.extratDateProperties(f.getName());
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR, dateExtractor.getStartYear());
                    c.set(Calendar.MONTH, dateExtractor.getMonth() - 1);
                    c.set(Calendar.DATE, 1);
                    new AsciiDataLoader(cacheDataStoreAction).parseAsciiFile(c.getTime(), new FileInputStream(f), null);

                    

                    String fileName = rootDirectory.substring(rootDirectory.lastIndexOf("/") + 1);
                    if(count++ == 0){
                        FileExportHelper.appendToFile(fileName + ".csv", "file" + "," + cacheDataStoreAction.getCSVHeader());
                    }
                    FileExportHelper.appendToFile(fileName + ".csv", f.getName() + "," + cacheDataStoreAction.toCSV());
//                    FileExportHelper.appendToFile(fileName + ".txt", cacheDataStoreAction.toString());

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(NetAsciiLoader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {
        TestAsciiLoader.testDataStoreCache();


    }
}
