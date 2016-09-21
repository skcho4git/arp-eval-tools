package com.rakuten.gep.merchant.tools.message;

public class Merchant {

    /**
     * action
     */
    private String action;
    /**
     * merchant id
     */
    private String merchantId;
    /**
     * shop id
     */
    private String shopId;

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return the merchantId
     */
    public String getMerchantId() {
        return merchantId;
    }

    /**
     * @param merchantId the merchantId to set
     */
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    /**
     * @return the shopId
     */
    public String getShopId() {
        return shopId;
    }

    /**
     * @param shopId the shopId to set
     */
    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
