package com.rakuten.gep.merchant.tools.constant;

/**
 * client type enum
 * 
 * @author skcho
 * @version 1.0
 * 
 */
public enum ClientTypeEnum {
    /**
     * RMS UI Application (Merchant Login, Mainmenu, Page Design etc)
     */
    RMS_UI("rms-ui"),
    /**
     * RMS UI Application for IBS (Account Open Tool etc)
     */
    RMS_IBS("rms-ibs"),
    /**
     * Mall Page Application (Shop Top Page, Shop Summary, Item Page)
     */
    MALL_PAGE("mall-page");

    private final String value;

    public String value() {
        return value;
    }

    private ClientTypeEnum(String value) {
        this.value = value;
    }

    public static boolean isExist(String value) {
        for (ClientTypeEnum d : values()) {
            if (d.value().equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static ClientTypeEnum convertFrom(String value) {
        for (ClientTypeEnum e : values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        return null;
    }
}