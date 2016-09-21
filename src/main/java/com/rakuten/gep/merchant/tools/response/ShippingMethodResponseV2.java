package com.rakuten.gep.merchant.tools.response;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;

/**
 * shipping method response
 * 
 * @author vuanhai
 * @version 1.0
 */
@XmlType(// name="shippingMethod",
propOrder = { "shippingMethodId", "merchantShippingMethodId", "shopShippingMethodId", "warehouseId", "currencyCode",
             "name", "description", "isDefault", "inactiveTime", "rateSchedule", "liveStartTime", "liveEndTime" })
@XmlAccessorType(XmlAccessType.FIELD)
public class ShippingMethodResponseV2 implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3956171267183991643L;
    /**
     * shipping method id
     */
    @XmlElement(type = String.class)
    private String shippingMethodId;
    /**
     * merchant shipping method id
     */
    @XmlElement(type = String.class)
    private String merchantShippingMethodId;
    /**
     * shop shipping method id
     */
    @XmlElement(type = String.class)
    private String shopShippingMethodId;
    /**
     * warehouse Id
     */
    @XmlElement(type = String.class)
    private String warehouseId;
    /**
     * currency code
     */
    @XmlElement(type = String.class)
    private String currencyCode;
    /**
     * name
     */
    @XmlElement(type = String.class)
    private String name;
    /**
     * description
     */
    @XmlElement(type = String.class)
    private String description;
    /**
     * default flg
     */
    @XmlElement(type = Boolean.class)
    private Boolean isDefault;
    /**
     * inactive time
     */
    @XmlElement(type = String.class)
    private Timestamp inactiveTime;
    @XmlElement(type = RateScheduleResponse.class)
    private RateScheduleResponse rateSchedule;
    /**
     * live start time
     */
    @XmlElement(type = String.class)
    private Date liveStartTime;
    /**
     * live end time
     */
    @XmlElement(type = String.class)
    private Date liveEndTime;

    /**
     * 
     * rate schedule response
     * 
     * @author sangki.cho
     * @version 1.0
     */
    /**
     * @return the shippingMethodId
     */
    public String getShippingMethodId() {
        return shippingMethodId;
    }

    /**
     * @param shippingMethodId the shippingMethodId to set
     */
    public void setShippingMethodId(String shippingMethodId) {
        this.shippingMethodId = shippingMethodId;
    }

    /**
     * @return the merchantShippingMethodId
     */
    public String getMerchantShippingMethodId() {
        return merchantShippingMethodId;
    }

    /**
     * @param merchantShippingMethodId the merchantShippingMethodId to set
     */
    public void setMerchantShippingMethodId(String merchantShippingMethodId) {
        this.merchantShippingMethodId = merchantShippingMethodId;
    }

    /**
     * @return the shopShippingMethodId
     */
    public String getShopShippingMethodId() {
        return shopShippingMethodId;
    }

    /**
     * @param shopShippingMethodId the shopShippingMethodId to set
     */
    public void setShopShippingMethodId(String shopShippingMethodId) {
        this.shopShippingMethodId = shopShippingMethodId;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return warehouseId
     */
    public String getWarehouseId() {
        return warehouseId;
    }

    /**
     * @param warehouseId
     */
    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    /**
     * @return the currencyCode
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * @param currencyCode the currencyCode to set
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the isDefault
     */
    public Boolean getIsDefault() {
        return isDefault;
    }

    /**
     * @param isDefault the isDefault to set
     */
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    /**
     * @return the inactiveTime
     */
    public Timestamp getInactiveTime() {
        return inactiveTime;
    }

    /**
     * @param inactiveTime the inactiveTime to set
     */
    public void setInactiveTime(Timestamp inactiveTime) {
        this.inactiveTime = inactiveTime;
    }

    /**
     * @return the rateSchedule
     */
    public RateScheduleResponse getRateSchedule() {
        return rateSchedule;
    }

    /**
     * @param rateSchedule the rateSchedule to set
     */
    public void setRateSchedule(RateScheduleResponse rateSchedule) {
        this.rateSchedule = rateSchedule;
    }

    /**
     * @return the liveStartTime
     */
    public Date getLiveStartTime() {
        return liveStartTime;
    }

    /**
     * @param liveStartTime the liveStartTime to set
     */
    public void setLiveStartTime(Date liveStartTime) {
        this.liveStartTime = liveStartTime;
    }

    /**
     * @return the liveEndTime
     */
    public Date getLiveEndTime() {
        return liveEndTime;
    }

    /**
     * @param liveEndTime the liveEndTime to set
     */
    public void setLiveEndTime(Date liveEndTime) {
        this.liveEndTime = liveEndTime;
    }

    @XmlType(// name="rateSchedule",
    propOrder = { "rateType", "multiProductShippingType", "unit", "destinations", "destinationsV2", "warehouses",
                 "fees" })
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class RateScheduleResponse implements Serializable {

        /**
		 * 
		 */
        private static final long serialVersionUID = -855562132081232631L;
        /**
         * rate type
         */
        @XmlElement(type = String.class)
        private String rateType;
        /**
         * multi product shipping type
         */
        @XmlElement(type = MultiProductShippingTypeResponse.class)
        private MultiProductShippingTypeResponse multiProductShippingType;
        /**
         * unit
         */
        @XmlElement(type = UnitResponse.class)
        private UnitResponse unit;
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
         * @return the rateType
         */
        public String getRateType() {
            return rateType;
        }

        /**
         * @param rateType the rateType to set
         */
        public void setRateType(String rateType) {
            this.rateType = rateType;
        }

        /**
         * @return the multiProductShippingType
         */
        public MultiProductShippingTypeResponse getMultiProductShippingType() {
            return multiProductShippingType;
        }

        /**
         * @param multiProductShippingType the multiProductShippingType to set
         */
        public void setMultiProductShippingType(MultiProductShippingTypeResponse multiProductShippingType) {
            this.multiProductShippingType = multiProductShippingType;
        }

        /**
         * @return the unit
         */
        public UnitResponse getUnit() {
            return unit;
        }

        /**
         * @param unit the unit to set
         */
        public void setUnit(UnitResponse unit) {
            this.unit = unit;
        }

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
         * @return the warehouses
         */
        public List<WarehouseResponse> getWarehouses() {
            return warehouses;
        }

        /**
         * @param warehouses the warehouses to set
         */
        public void setWarehouses(List<WarehouseResponse> warehouses) {
            this.warehouses = warehouses;
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

        /**
         * multi product shipping type response
         * 
         * @author skcho
         * 
         */
        @XmlType(propOrder = { "name", "shippingCalculationMethodId" })
        @XmlAccessorType(XmlAccessType.FIELD)
        public static class MultiProductShippingTypeResponse implements Serializable {

            /**
			 * 
			 */
            private static final long serialVersionUID = 892361827529883769L;
            /**
             * multi product shipping type name
             */
            @XmlElement(type = String.class)
            private String name;
            /**
             * shipping calculation method id
             */
            @XmlElement(type = String.class)
            private String shippingCalculationMethodId;

            /**
             * @return the name
             */
            public String getName() {
                return name;
            }

            /**
             * @param name the name to set
             */
            public void setName(String name) {
                this.name = name;
            }

            /**
             * @return the shippingCalculationMethodId
             */
            public String getShippingCalculationMethodId() {
                return shippingCalculationMethodId;
            }

            /**
             * @param shippingCalculationMethodId the shippingCalculationMethodId to set
             */
            public void setShippingCalculationMethodId(String shippingCalculationMethodId) {
                this.shippingCalculationMethodId = shippingCalculationMethodId;
            }
        }

        /**
         * 
         * destination response
         * 
         * @author sangki.cho
         * @version 1.0
         */
        @XmlType(// name="destination",
        propOrder = { "name", "states" })
        @XmlAccessorType(XmlAccessType.FIELD)
        public static class DestinationResponse implements Serializable {

            /**
			 * 
			 */
            private static final long serialVersionUID = -1440835826741229424L;
            @XmlElement(type = String.class)
            private String name;
            @XmlElement(type = State.class)
            private List<State> states;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<State> getStates() {
                return states;
            }

            public void setStates(List<State> states) {
                this.states = states;
            }
        }

        /**
         * 
         * warehouse response
         * 
         * @author sangki.cho
         * @version 1.0
         */
        @XmlType(// name="departure",
        propOrder = { "name", "warehouseId", "fees", "addition" })
        @XmlAccessorType(XmlAccessType.FIELD)
        public static class WarehouseResponse implements Serializable {

            /**
			 * 
			 */
            private static final long serialVersionUID = 1186765142449463747L;
            /**
             * name
             */
            @XmlElement(type = String.class)
            private String name;
            /**
             * warehouse id
             */
            @XmlElement(type = String.class)
            private String warehouseId;
            /**
             * fees
             */
            @XmlElement(type = FeeResponse.class)
            private List<FeeResponse> fees;
            /**
             * addition
             */
            @XmlElement(type = AdditionResponse.class)
            private AdditionResponse addition;

            /**
             * @return the name
             */
            public String getName() {
                return name;
            }

            /**
             * @param name the name to set
             */
            public void setName(String name) {
                this.name = name;
            }

            /**
             * @return the warehouseId
             */
            public String getWarehouseId() {
                return warehouseId;
            }

            /**
             * @param warehouseId the warehouseId to set
             */
            public void setWarehouseId(String warehouseId) {
                this.warehouseId = warehouseId;
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

            /**
             * @return the addition
             */
            public AdditionResponse getAddition() {
                return addition;
            }

            /**
             * @param addition the addition to set
             */
            public void setAddition(AdditionResponse addition) {
                this.addition = addition;
            }

            /**
             * addition response
             * 
             * @author skcho
             * 
             */
            @XmlType
            // (name="addition")
            @XmlAccessorType(XmlAccessType.FIELD)
            public static class AdditionResponse extends HashMap<String, ArrayList<String>> {

                /**
				 * 
				 */
                private static final long serialVersionUID = 7997425102499963648L;

                public AdditionResponse() {
                    super();
                }

                public AdditionResponse(String key, ArrayList<String> value) {
                    super(1);
                    this.put(key, value);
                }
            }
        }

        /**
         * 
         * fee response
         * 
         * @author sangki.cho
         * @version 1.0
         */
        @XmlType
        // (name="fee")
        @XmlAccessorType(XmlAccessType.FIELD)
        public static class FeeResponse extends HashMap<String, ArrayList<String>> {

            /**
			 * 
			 */
            private static final long serialVersionUID = 5160472333363221897L;

            /**
             * serialVersionUID
             */
            // //private static final long serialVersionUID = -6417029489742586195L;
            public FeeResponse() {
                super();
            }

            public FeeResponse(String key, ArrayList<String> value) {
                super(1);
                this.put(key, value);
            }
        }

        /**
         * 
         * destination response
         * 
         * @author vuanhai
         * @version 1.0
         */
        @XmlType(propOrder = { "stateCode", "name", "cities" })
        @XmlAccessorType(XmlAccessType.FIELD)
        public static class State implements Serializable {

            /**
			 * 
			 */
            private static final long serialVersionUID = 7932125297795109490L;
            /**
             * serialVersionUID
             */
            // //private static final long serialVersionUID = -4867332723905609067L;
            @XmlElement(type = Name.class)
            private Name name;
            @XmlElement(type = String.class)
            private String stateCode;
            @XmlElement(type = City.class)
            private List<City> cities;

            public Name getName() {
                return name;
            }

            public void setName(Name name) {
                this.name = name;
            }

            public String getStateCode() {
                return stateCode;
            }

            public void setStateCode(String stateCode) {
                this.stateCode = stateCode;
            }

            public List<City> getCities() {
                return cities;
            }

            public void setCities(List<City> cities) {
                this.cities = cities;
            }
        }

        /**
         * 
         * destination response
         * 
         * @author vuanhai
         * @version 1.0
         */
        @XmlType(// name="destination",
        propOrder = { "cityCode", "name" })
        @XmlAccessorType(XmlAccessType.FIELD)
        public static class City implements Serializable {

            /**
             * serialVersionUID
             */
            private static final long serialVersionUID = 5708656081679876754L;
            @XmlElement(type = Name.class)
            private Name name;
            @XmlElement(type = String.class)
            private String cityCode;

            public Name getName() {
                return name;
            }

            public void setName(Name name) {
                this.name = name;
            }

            public String getCityCode() {
                return cityCode;
            }

            public void setCityCode(String cityCode) {
                this.cityCode = cityCode;
            }
        }

        @XmlType(propOrder = { "name", "states" })
        @XmlAccessorType(XmlAccessType.FIELD)
        public static class DestinationV2Response implements Serializable {

            /**
             * serialVersionUID
             */
            // //private static final long serialVersionUID = -2551934299777170323L;
            @XmlElement(type = Name.class)
            private Name name;
            @XmlElement(type = State.class)
            private List<State> states;

            public Name getName() {
                return name;
            }

            public void setName(Name name) {
                this.name = name;
            }

            public List<State> getStates() {
                return states;
            }

            public void setStates(List<State> states) {
                this.states = states;
            }
        }

        @XmlType(propOrder = { "value" })
        @XmlAccessorType(XmlAccessType.FIELD)
        public static class Name implements Serializable {

            /**
             * serialVersionUID
             */
            private static final long serialVersionUID = -4855907883860203876L;
            @XmlElement(type = String.class)
            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
