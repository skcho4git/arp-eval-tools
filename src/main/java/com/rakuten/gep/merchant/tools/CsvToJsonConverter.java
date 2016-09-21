package com.rakuten.gep.merchant.tools;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;
import com.rakuten.gep.merchant.tools.response.ShippingMethodResponseV2.RateScheduleResponse.City;
import com.rakuten.gep.merchant.tools.response.ShippingMethodResponseV2.RateScheduleResponse.DestinationResponse;
import com.rakuten.gep.merchant.tools.response.ShippingMethodResponseV2.RateScheduleResponse.FeeResponse;
import com.rakuten.gep.merchant.tools.response.ShippingMethodResponseV2.RateScheduleResponse.Name;
import com.rakuten.gep.merchant.tools.response.ShippingMethodResponseV2.RateScheduleResponse.State;
import com.rakuten.gep.merchant.tools.response.ShippingMethodResponseV2.RateScheduleResponse.WarehouseResponse;

/**
 * 
 * CsvToJsonConverter
 * 
 */
public class CsvToJsonConverter {

    // input csv file path
    private String inCsvPath;
    // input states csv file path
    private String inStatesCsvPath;
    // input cities csv file path
    private String inCitiesCsvPath;
    // output json file path
    private String outJsonPath;
    // the length of fees array
    private final int FEES_ARRAY_LEN = 2;
    // save the statecodes
    private Map<String, String> statesMap = new HashMap<String, String>();
    // save the citycodes
    private Map<String, String> citiesMap = new HashMap<String, String>();

    public CsvToJsonConverter(String csvPath, String statesCsvPath, String citiesCsvPath) {
        String basePath = this.getClass().getClassLoader().getResource("").getPath();
        this.inCsvPath = basePath + "CsvToJsonConverter/input_csv/" + csvPath;
        this.inStatesCsvPath = basePath + "CsvToJsonConverter/input_csv/" + statesCsvPath;
        this.inCitiesCsvPath = basePath + "CsvToJsonConverter/input_csv/" + citiesCsvPath;
        this.outJsonPath = basePath + "CsvToJsonConverter/output_json/";
    }

    /**
     * @param args
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    public static void main(String args[]) throws JsonGenerationException, JsonMappingException, IOException {
        CsvToJsonConverter c = new CsvToJsonConverter("pre-defined.csv", "States.csv", "Cities.csv");
        c.convert();
    }

    /**
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    public void convert() throws JsonGenerationException, JsonMappingException, IOException {
        long start = System.currentTimeMillis();
        readeSatesCsv();
        System.out.println("the size of stateCode is " + statesMap.size());
        readeCitiesCsv();
        System.out.println("the size of cityCode is " + citiesMap.size());
        readeCsv();
        System.out.println("convert csv to json successfully!...  total time is "
                           + (System.currentTimeMillis() - start) / 1000 + " s");
    }

    /**
     * readeSatesCsv
     */
    private void readeSatesCsv() {
        try {
            ArrayList<String[]> csvList = new ArrayList<String[]>();
            // code for reading
            CsvListReader reader = new CsvListReader(new FileReader(inStatesCsvPath), CsvPreference.STANDARD_PREFERENCE);
            // if need header , don't write this
            // reader.readHeaders();
            // read by line, except header data
            List<String> customerList;
            while ((customerList = reader.read()) != null) {
                csvList.add((String[]) customerList.toArray(new String[0]));
            }
            reader.close();
            for (int row = 0; row < csvList.size(); row++) {
                statesMap.put(csvList.get(row)[1], csvList.get(row)[2]);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    /**
     * readeCitiesCsv
     */
    private void readeCitiesCsv() {
        try {
            ArrayList<String[]> csvList = new ArrayList<String[]>();
            // code for reading
            CsvListReader reader = new CsvListReader(new FileReader(inCitiesCsvPath), CsvPreference.STANDARD_PREFERENCE);
            // if need header , don't write this
            // reader.readHeaders();
            // read by line, except header data
            List<String> customerList;
            while ((customerList = reader.read()) != null) {
                csvList.add((String[]) customerList.toArray(new String[0]));
            }
            reader.close();
            for (int row = 0; row < csvList.size(); row++) {
                citiesMap.put(csvList.get(row)[2], csvList.get(row)[3]);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    /**
     * readeCsv
     */
    private void readeCsv() {
        try {
            ArrayList<String[]> csvList = new ArrayList<String[]>();
            // code for reading
            CsvListReader reader = new CsvListReader(new FileReader(inCsvPath), CsvPreference.STANDARD_PREFERENCE);
            // if need header , don't need this
            // reader.readHeaders();
            // read by line, except header data
            List<String> customerList;
            while ((customerList = reader.read()) != null) {
                csvList.add((String[]) customerList.toArray(new String[0]));
            }
            reader.close();
            Map<String, String> warehousesMap = new HashMap<String, String>();
            warehousesMap.put(csvList.get(0)[1], "");
            // save a data for a json file
            List<Map<String, Object>> oneJsonDestData = new ArrayList<Map<String, Object>>();
            List<String[]> oneJsonFeeData = new ArrayList<String[]>();
            for (int row = 0; row < csvList.size(); row++) {
                String name = csvList.get(row)[1];
                // save a row data
                Map<String, Object> oneRowMap = new HashMap<String, Object>();
                oneRowMap.put("name", name);
                oneRowMap.put("province", csvList.get(row)[3]);
                oneRowMap.put("city", csvList.get(row)[5]);
                // For First 1.09 kg : Rp
                String value1 = csvList.get(row)[6];
                // For per additional 1.0 kg : Rp
                String value2 = csvList.get(row)[7];
                if (!warehousesMap.containsKey(name)) {
                    writeJson(oneJsonDestData, oneJsonFeeData);
                    warehousesMap.put(name, "");
                    oneJsonDestData = new ArrayList<Map<String, Object>>();
                    oneJsonFeeData = new ArrayList<String[]>();
                }
                oneJsonDestData.add(oneRowMap);
                oneJsonFeeData.add(new String[] { value1, value2 });
                if (row == csvList.size() - 1) {
                    writeJson(oneJsonDestData, oneJsonFeeData);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    /**
     * writeJson
     * 
     * @param destData
     * @param feeData
     */
    private void writeJson(List<Map<String, Object>> destData, List<String[]> feeData) {
        ObjectMapper mapper = new ObjectMapper();
        DestsAndFeesResponse response = new DestsAndFeesResponse();
        List<DestinationResponse> destinations = new ArrayList<DestinationResponse>();
        String fileName = (String) destData.get(0).get("name");
        String cityCode = "";
        for (int i = 0; i < destData.size(); i++) {
            Map<String, Object> oneRowMap = destData.get(i);
            DestinationResponse destinationResponse = new DestinationResponse();
            destinationResponse.setName((String) oneRowMap.get("city"));
            List<State> states = new ArrayList<State>();
            State s = new State();
            Name n = new Name();
            n.setValue((String) oneRowMap.get("province"));
            s.setName(n);
            s.setStateCode(statesMap.get(n.getValue()));
            List<City> cities = new ArrayList<City>();
            City c = new City();
            n = new Name();
            n.setValue((String) oneRowMap.get("city"));
            c.setName(n);
            cityCode = citiesMap.get(n.getValue());
            c.setCityCode((null != cityCode && 0 < cityCode.length()) ? cityCode : "#N/A");
            cities.add(c);
            s.setCities(cities);
            states.add(s);
            destinationResponse.setStates(states);
            destinations.add(destinationResponse);
        }
        response.setDestinations(destinations);
        response.setFees(getFees(feeData));
        try {
            mapper.writeValue(new File(outJsonPath + fileName.replaceAll("/", "_") + ".json"), response);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * getFees
     * 
     * @param value
     */
    private List<FeeResponse> getFees(List<String[]> value) {
        String value1;
        String value2;
        List<FeeResponse> fees = new ArrayList<FeeResponse>();
        Integer first = 0;
        Integer perAdd = 0;
        FeeResponse feeResponse = null;
        for (int i = 1; i <= FEES_ARRAY_LEN; i++) {
            ArrayList<String> list = new ArrayList<String>();
            for (int j = 0; j < value.size(); j++) {
                String[] feeArr = value.get(j);
                value1 = feeArr[0].replaceAll(",", "").trim();
                value2 = feeArr[1].replaceAll(",", "").trim();
                first = Integer.parseInt(value1);
                perAdd = Integer.parseInt(value2);
                list.add(String.valueOf(first + (i - 1) * perAdd));
            }
            feeResponse = new FeeResponse(String.valueOf(i) + ".09", list);
            fees.add(feeResponse);
        }
        return fees;
    }

    @XmlType(// name="DestsAndFeesResponse",
    propOrder = { "destinations", "fees" })
    @XmlAccessorType(XmlAccessType.FIELD)
    public class DestsAndFeesResponse {

        /**
         * destinations
         */
        @XmlElement(type = DestinationResponse.class)
        private List<DestinationResponse> destinations;
        /**
         * warehouses
         */
        @XmlElement(type = WarehouseResponse.class)
        private List<WarehouseResponse> warehouses;
        /**
         * fees
         */
        @XmlElement(type = FeeResponse.class)
        private List<FeeResponse> fees;

        /**
         * @return the destinations
         */
        public List<DestinationResponse> getDestinations() {
            return destinations;
        }

        /**
         * @param destinations the destinations to set
         */
        public void setDestinations(List<DestinationResponse> destinations) {
            this.destinations = destinations;
        }

        /**
         * @return the fees
         */
        public List<FeeResponse> getFees() {
            return fees;
        }

        /**
         * @param fees the fees to set
         */
        public void setFees(List<FeeResponse> fees) {
            this.fees = fees;
        }
    }
}
