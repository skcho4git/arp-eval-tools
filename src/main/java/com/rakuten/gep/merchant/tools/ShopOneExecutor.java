/*
 * To change this license header, choose License Headers in Project Properties. To change this template file,
 * choose Tools | Templates and open the template in the editor.
 */
package com.rakuten.gep.merchant.tools;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import com.rakuten.gep.merchant.api.client.response.GetResponse;
import com.rakuten.gep.merchant.api.client.response.ShopResponse;
import com.rakuten.gep.merchant.tools.constant.ClientTypeEnum;

/**
 * 
 * @author gaowei
 */
public class ShopOneExecutor extends AbstractExecutor {

    /**
     * shopId
     */
    private String[] shopIds = null;
    /**
     * mallId
     */
    private String mallId = null;
    /**
     * shopUrl
     */
    private String[] shopUrls = null;
    /**
     * clientId
     */
    private String clientId = ClientTypeEnum.RMS_IBS.value();

    public static void main(String[] args) {
        ShopOneExecutor shopExecutor = new ShopOneExecutor();
        shopExecutor.start(args);
        try {
            shopExecutor.init();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private boolean getParameter(String[] args) {
        if (args == null || args.length < 1) {
            System.out.println("###### please input parameter like: shopId1 shopId2  ... ######");
            System.out.println("######                     or like: mallId shopUrl1 shopUrl2  ... ######");
            System.out.println("###### for example : 00391690-9451-11e3-8913-005056a57ee8 b5af9e40-9465-11e3-8913-005056a57ee8 ######");
            System.out.println("######          or : gs _ao_test0321-1-1848 aj_stg ######");
            return false;
        }
        if (args[0].length() == 36) {
            shopIds = new String[args.length];
            for (int i = 0; i < args.length; i++) {
                shopIds[i] = args[i];
            }
        } else {
            mallId = args[0];
            shopUrls = new String[args.length - 1];
            for (int i = 0; i < shopUrls.length; i++) {
                shopUrls[i] = args[i + 1];
            }
        }
        return true;
    }

    @Override
    protected void start(String[] args) {
        Boolean bFailed = false;
        if (getParameter(args) == false) {
            return;
        }
        try {
            System.out.println("###### ShopOneExecutor is started in executing ######");
            init();
            execute();
        } catch (Throwable t) {
            bFailed = true;
            if (error.isErrorEnabled()) {
                error.error("{ShopOneExecutor is failed in executing}", t);
            }
        } finally {
            System.out.println(bFailed ? "###### ShopOneExecutor is finished in executing with failure ######"
                                      : "###### ShopOneExecutor is finished in executing with success ######");
            exit(bFailed);
        }
    }

    @Override
    protected void execute() throws Throwable {
        PrintWriter shopListPw = null;
        try {
            String shopListOutputFile = properties.getProperty("all-shop-list.output");
            String shopId, shopUrl, merchantId;
            shopListPw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(shopListOutputFile))));
            if (shopIds != null) {
                for (int i = 0; i < shopIds.length; i++) {
                    shopId = shopIds[i];
                    ShopResponse shop1 = getShop(clientId, shopId);
                    merchantId = shop1.getMerchantId();
                    shopListPw.println(shopId + "," + merchantId);
                }
            } else {
                for (int i = 0; i < shopUrls.length; i++) {
                    shopUrl = shopUrls[i];
                    GetResponse getResponse = findShop(clientId, mallId, shopUrl);
                    merchantId = getResponse.getMerchant().getMerchantId();
                    shopId = getResponse.getShop().getShopId();
                    shopListPw.println(shopId + "," + merchantId);
                }
            }
            shopListPw.flush();
        } catch (Throwable t) {
            throw t;
        } finally {
            if (shopListPw != null) {
                shopListPw.close();
            }
        }
    }

    protected ShopResponse getShop(String clientId, String shopId) throws Throwable {
        System.out.println("### Start get shop for shopId: " + shopId + " ###");
        System.out.println(shopResourceClient);
        GetResponse getResponse = shopResourceClient.getShop(clientId, shopId);
        return getResponse.getShop();
    }

    protected GetResponse findShop(String clientId, String mallId, String shopUrl) throws Throwable {
        System.out.println("### Start find shop for shopUrl: " + shopUrl + " ###");
        System.out.println(shopResourceClient);
        GetResponse getResponse = shopResourceClient.findShop(clientId, mallId, shopUrl);
        return getResponse;
    }
}
