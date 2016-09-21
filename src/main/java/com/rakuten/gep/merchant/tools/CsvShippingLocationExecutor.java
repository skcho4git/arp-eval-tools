package com.rakuten.gep.merchant.tools;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.rakuten.gep.merchant.api.client.ShippingResourceClient;
import com.rakuten.gep.merchant.api.client.response.GetResponse;
import com.rakuten.gep.merchant.api.client.response.LocationResponse;
import com.rakuten.gep.merchant.tools.constant.ClientTypeEnum;

/**
 * 
 * @author gaowei
 */
public class CsvShippingLocationExecutor {

    /**
     * clientId
     */
    private static String clientId = ClientTypeEnum.RMS_IBS.value();
    /**
     * mallIds
     */
    private static String mallId_GS = "gs";
    private static String[] mallId_GECP = { "my", "id", "es", "th", "sg" };
    /**
     * titleNameMap
     */
    private static Map<String, String> titleNameMap = new HashMap<String, String>() {

        {
            put("continent", "Continent Name");
            put("country", "Country Name");
            put("state", "State Name");
            put("city", "City Name");
        }
    };
    /**
     * titleCodeMap
     */
    private static Map<String, String> titleCodeMap = new HashMap<String, String>() {

        {
            put("continent", "Continent Code");
            put("country", "Country Code");
            put("state", "State Code");
            put("city", "City Code");
        }
    };

    /**
     * main
     * 
     * @param args
     */
    public static void main(String[] args) {
        CsvShippingLocationExecutor c = new CsvShippingLocationExecutor();
        if (args != null && args.length > 0) {
            mallId_GECP = args;
        }
        c.execute();
    }

    /**
     * execute
     */
    public void execute() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {
                                                                                                 "classpath:/applicationContext-resourceClient.xml",
                                                                                                 "classpath:/applicationContext-service.xml" });
        ShippingResourceClient shippingResourceClient = (ShippingResourceClient) applicationContext.getBean("shippingResourceClient");
        PrintWriter printWriter1 = null;
        PrintWriter printWriter2 = null;
        try {
            String basePath = this.getClass().getClassLoader().getResource("").getPath();
            GetResponse gsRs = shippingResourceClient.collectShippingLocation(clientId, mallId_GS);
            Map<String, String> countryNameMap = generateCountryNameMap(gsRs);
            List<List<String>> csvDatas1 = getCsvDatas(gsRs);
            printWriter1 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(basePath
                                                                                                          + "\\gs.csv"))));
            printCsvDataWithTitle(printWriter1, csvDatas1);
            printWriter2 = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(basePath
                                                                                                          + "\\gecp.csv"))));
            printCsvTitle(printWriter2);
            for (int i = 0; i < mallId_GECP.length; i++) {
                String mallId = mallId_GECP[i];
                if (mallId_GS.equals(mallId)) {
                    continue;
                }
                String countryCode = mallId.toUpperCase();
                String countryName = countryNameMap.get(countryCode);
                GetResponse rs = shippingResourceClient.collectShippingLocation(clientId, mallId);
                List<List<String>> csvDatas2 = getCsvDatas(rs);
                printCsvDataWithCountry(printWriter2, countryCode, countryName, csvDatas2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (printWriter1 != null) {
                printWriter1.close();
            }
            if (printWriter2 != null) {
                printWriter2.close();
            }
        }
    }

    /**
     * generate an countryCode - countryName map
     * 
     * @param rs
     * @return Map<String, String>
     */
    private Map<String, String> generateCountryNameMap(GetResponse rs) {
        Map<String, String> result = new HashMap<String, String>();
        for (LocationResponse location : rs.getLocations()) {
            addCountryFromLocation(location, result);
        }
        return result;
    }

    /**
     * add location (countryCode - countryName) to Map
     * 
     * @param location
     * @param result
     */
    private void addCountryFromLocation(LocationResponse location, Map<String, String> result) {
        // if it is country , then add itself
        if ("country".equalsIgnoreCase(location.getType())) {
            result.put(location.getCode(), location.getName().get("value"));
        }
        // add child location data
        for (LocationResponse childLocation : location.getLocations()) {
            addCountryFromLocation(childLocation, result);
        }
    }

    /**
     * get csv data from response
     * 
     * @param rs
     * @return List<List<String>>
     */
    private List<List<String>> getCsvDatas(GetResponse rs) {
        List<String> titles = new ArrayList<String>();
        List<List<String>> datas = new ArrayList<List<String>>();
        for (LocationResponse location : rs.getLocations()) {
            makeCsv(location, titles, datas);
        }
        List<List<String>> result = new ArrayList<List<String>>();
        result.add(titles);
        result.addAll(datas);
        return result;
    }

    /**
     * make csv title and data according to location
     * 
     * @param location
     * @param titles
     * @param datas
     */
    private void makeCsv(LocationResponse location, List<String> titles, List<List<String>> datas) {
        // edit title
        String type = location.getType();
        if (!titles.contains(titleNameMap.get(type))) {
            titles.add(titleNameMap.get(type));
            titles.add(titleCodeMap.get(type));
        }
        // edit data
        List<String> data = new ArrayList<String>();
        int colIndex = titles.indexOf(titleNameMap.get(type));
        // edit data (copy previous line data data)
        if (colIndex >= 2) {
            List<String> predata = datas.get(datas.size() - 1);
            for (int i = 0; i < colIndex; i++) {
                data.add(predata.get(i));
            }
        }
        // edit data (add self data)
        data.add(location.getName().get("value"));
        data.add(location.getCode());
        datas.add(data);
        // add child location data
        for (LocationResponse childLocation : location.getLocations()) {
            makeCsv(childLocation, titles, datas);
        }
    }

    /**
     * print fix title
     * 
     * @param printWriter
     */
    private void printCsvTitle(PrintWriter printWriter) {
        StringBuilder sb = null;
        sb = new StringBuilder();
        sb.append("\"Country Name\",");
        sb.append("\"Country Code\",");
        sb.append("\"State Name\",");
        sb.append("\"State Code\",");
        sb.append("\"City Name\",");
        sb.append("\"City Code\"");
        printWriter.println(sb.toString());
    }

    /**
     * print csv title and data
     * 
     * @param printWriter
     * @param csvDatas
     */
    private void printCsvDataWithTitle(PrintWriter printWriter, List<List<String>> csvDatas) {
        StringBuilder sb = null;
        try {
            sb = new StringBuilder();
            List<String> titles = csvDatas.get(0);
            boolean additional = false;
            for (String title : titles) {
                if (additional)
                    sb.append(",");
                sb.append("\"" + title + "\"");
                additional = true;
            }
            printWriter.println(sb.toString());
            int titleLength = titles.size();
            for (int i = 1; i < csvDatas.size(); i++) {
                sb = new StringBuilder();
                List<String> rowData = csvDatas.get(i);
                while (rowData.size() < titleLength) {
                    rowData.add("");
                }
                additional = false;
                for (String coldata : rowData) {
                    if (additional)
                        sb.append(",");
                    sb.append("\"" + coldata + "\"");
                    additional = true;
                }
                printWriter.println(sb.toString());
            }
            printWriter.flush();
        } catch (Throwable t) {
            System.out.println(t.getMessage());
            throw t;
        }
    }

    /**
     * print csv data with countryCode and countryName
     * 
     * @param printWriter
     * @param countryCode
     * @param countryName
     * @param csvDatas
     */
    private void printCsvDataWithCountry(PrintWriter printWriter,
                                         String countryCode,
                                         String countryName,
                                         List<List<String>> csvDatas) {
        StringBuilder sb = null;
        try {
            for (int i = 1; i < csvDatas.size(); i++) {
                sb = new StringBuilder();
                List<String> rowData = csvDatas.get(i);
                while (rowData.size() < 4) {
                    rowData.add("");
                }
                sb.append("\"" + countryName + "\"");
                sb.append(",\"" + countryCode + "\"");
                for (String coldata : rowData) {
                    sb.append(",\"" + coldata + "\"");
                }
                printWriter.println(sb.toString());
            }
            printWriter.flush();
        } catch (Throwable t) {
            System.out.println(t.getMessage());
            throw t;
        }
    }
}
