package com.rakuten.gep.merchant.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;
import com.rakuten.gep.merchant.api.client.request.MerchantRequest;
import com.rakuten.gep.merchant.api.client.request.ShippingMethodRequest;
import com.rakuten.gep.merchant.api.client.request.ShopRequest;
import com.rakuten.gep.merchant.api.client.response.ShippingMethodResponse;
import com.rakuten.gep.merchant.api.client.response.PostResponse;
import com.rakuten.gep.merchant.tools.constant.ClientTypeEnum;

public class ShippingExecutor extends AbstractExecutor {

    /**
     * mallId
     */
    private String mallId;
    /**
     * isActive
     */
    private Boolean isActive;
    /**
     * isDemo
     */
    private Boolean isDemo;
    /**
     * shipping method Ids user can use program args to set the shipping methodIds he/she want to be inserted
     */
    private List<String> preferedShippingMethodIds;
    /**
     * shipping method list
     */
    private List<String> shippingMethodIdList;
    /**
     * clientId
     */
    private String clientId = ClientTypeEnum.RMS_IBS.value();

    public static void main(String[] args) {
        ShippingExecutor shippingExecutor = new ShippingExecutor();
        shippingExecutor.start(args);
    }

    @Override
    protected void start(String[] args) {
        Boolean bFailed = false;
        if (getParameter(args) == false) {
            return;
        }
        try {
            System.out.println("###### ShippingExecutor is started in executing ######");
            init();
            execute();
        } catch (Throwable t) {
            bFailed = true;
            if (error.isErrorEnabled()) {
                error.error("{ShippingExecutor is failed in executing}", t);
            }
        } finally {
            System.out.println(bFailed ? "###### ShippingExecutor is finished in executing with failure ######"
                                      : "###### ShippingExecutor is finished in executing with success ######");
            System.exit(1);
        }
    }

    /**
     * get mallId, isActive, shippingMethodId from args
     * 
     * @param args
     * @return
     */
    private boolean getParameter(String[] args) {
        if (args == null || args.length < 3) {
            System.out.println("###### please input parameter like: mallId isDemo isActive shippingMethodId ... ######");
            System.out.println("###### for example : es true true d73cbe93-3f40-f848-778c-0e284a0de3fe (when add Correos Standard Delivery for Spain demo shops) ######");
            System.out.println("######          or : es false false 6aac1a96-b71e-3b00-94ff-9fb21733bf5f (when add MRW for Spain all shops) ######");
            System.out.println("###### put --all as shippingMethodId means that add all shipping methods of the mall. ######");
            return false;
        }
        this.mallId = args[0];
        if ("false".equalsIgnoreCase(args[1])) {
            isDemo = Boolean.FALSE;
        }
        if ("true".equalsIgnoreCase(args[1])) {
            isDemo = Boolean.TRUE;
        }
        if ("false".equalsIgnoreCase(args[2])) {
            isActive = Boolean.FALSE;
        }
        if ("true".equalsIgnoreCase(args[2])) {
            isActive = Boolean.TRUE;
        }
        this.preferedShippingMethodIds = new ArrayList<String>();
        for (int i = 2; i < args.length; i++) {
            if (i == 2 && isActive != null) {
                continue;
            }
            preferedShippingMethodIds.add(args[i]);
        }
        if (isActive != null) {
            System.out.println("###### set isActive as " + String.valueOf(isActive.booleanValue()) + " ######");
        } else {
            System.out.println("###### please input the second parameter 'isActive' like true or false ######");
            return false;
        }
        if (CollectionUtils.isEmpty(preferedShippingMethodIds)) {
            System.out.println("###### please input parameter like: mallId isDemo isActive shippingMethodId ... ######");
            System.out.println("###### for example : es true true d73cbe93-3f40-f848-778c-0e284a0de3fe (when add Correos Standard Delivery for Spain demo shops) ######");
            System.out.println("######          or : es false false 6aac1a96-b71e-3b00-94ff-9fb21733bf5f (when add MRW for Spain all shops) ######");
            System.out.println("###### put --all as shippingMethodId means that add all shipping methods of the mall. ######");
            return false;
        }
        return true;
    }

    @Override
    protected void init() throws Throwable {
        super.init();
        System.out.println("###### ShippingExecutor is initialized ######");
    }

    @Override
    protected void execute() throws Throwable {
        String message = null;
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        String csvline = null;
        String shopId = null;
        String merchantId = null;
        try {
            String inputFile = isDemo ? properties.getProperty("demo-shop-list.output")
                                     : properties.getProperty("all-shop-list.output");
            fis = new FileInputStream(inputFile);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            fos = new FileOutputStream(properties.getProperty("merchant-tools.output"));
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);
            collectShippingStatus();
            while (StringUtils.isNotBlank(csvline = br.readLine())) {
                String[] csvitems = csvline.split(",");
                shopId = csvitems[0];
                merchantId = csvitems[1];
                try {
                    for (String shippingMethodId : shippingMethodIdList) {
                        PostResponse postResponse = createMerchantShipping(merchantId, shippingMethodId);
                        createShopShipping(shopId, shippingMethodId, postResponse.getMerchant().getShippingMethod().getMerchantShippingMethodId());
                    }
                } catch (Throwable t) {
                    bw.write(csvline);
                    bw.newLine();
                    if (error.isErrorEnabled()) {
                        message = "{ShippingExecutor is failed with adding shipping to shop : " + "{shopId=" + shopId
                                  + "}}";
                        // warn.warn(message);
                        error.error(message, t);
                    }
                }
            }
        } catch (Throwable t) {
            throw t;
        } finally {
            if (br != null) {
                br.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (fis != null) {
                fis.close();
            }
            if (bw != null) {
                bw.close();
            }
            if (osw != null) {
                osw.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * search mall shipping method.
     * 
     * @throws Throwable
     */
    private void collectShippingStatus() throws Throwable {
        shippingMethodIdList = new ArrayList<>();
        List<ShippingMethodResponse> allShippings = shippingResourceClient.collectShippingStatus(this.clientId, this.mallId).getShippingMethods();
        // if use --all as a parameter shippingMethodId, then add all shippingMethod
        if (preferedShippingMethodIds.size() >= 1 && "--all".equals(preferedShippingMethodIds.get(0))) {
            for (ShippingMethodResponse shipping : allShippings) {
                shippingMethodIdList.add(shipping.getShippingMethodId());
            }
            return;
        }
        // otherwise, add the shippingMethods according to parameter
        for (ShippingMethodResponse shipping : allShippings) {
            if (preferedShippingMethodIds.contains(shipping.getShippingMethodId()))
                shippingMethodIdList.add(shipping.getShippingMethodId());
        }
        // if the parameter shippingMethodId doesn't exist in the mall, then end.
        if (shippingMethodIdList.size() != preferedShippingMethodIds.size()) {
            for (String preferedShippingMethodId : preferedShippingMethodIds) {
                if (!shippingMethodIdList.contains(preferedShippingMethodId)) {
                    System.out.println("###### the input shippingMethodId '" + preferedShippingMethodId
                                       + "' doesn't exist in the Mall '" + mallId + "'######");
                }
            }
            throw new Exception("please input shippingMethodId correctly. ");
        }
    }

    /**
     * create a Shipping Method for a shop
     * 
     * @param shopId
     * @param shippingMethodId
     * @param merchantShippingMethodId
     * @throws Throwable
     */
    private void createShopShipping(String shopId, String shippingMethodId, String merchantShippingMethodId) throws Throwable {
        PostResponse postResponse = null;
        System.out.println("###### ShippingExecutor is start with adding shipping '" + shippingMethodId + "' to shop '"
                           + shopId + "' ######");
        ShippingMethodRequest pyre = new ShippingMethodRequest();
        pyre.setIsActive(String.valueOf(isActive.booleanValue()));
        ShopRequest request = new ShopRequest();
        request.setShopId(shopId);
        pyre.setMerchantShippingMethodId(merchantShippingMethodId);
        request.setShippingMethod(pyre);
        try {
            postResponse = shopShippingResourceClient.createShipping(clientId, request);
        } catch (com.rakuten.gep.merchant.api.client.exception.ResourceClientException ex) {
            System.out.println("Error creating shipping '" + shippingMethodId + "' for this shop : '" + shopId + "'. ");
            throw new Exception("Error creating shipping '" + shippingMethodId + "' for this shop : '" + shopId + "'. ");
        }
        if (info.isInfoEnabled()) {
            String message = "{ShippingExecutor has added shipping to shop : " + "{shopId=" + shopId + "},"
                             + "{shopShippingMethodId="
                             + postResponse.getShop().getShippingMethod().getShopShippingMethodId() + "},"
                             + "{merchantShippingMethodId=" + merchantShippingMethodId + "}}";
            info.info(message);
        }
    }

    /**
     * create a Shipping Method for a merchant
     * 
     * @param merchantId
     * @param shippingMethodId
     * @return
     * @throws Throwable
     */
    private PostResponse createMerchantShipping(String merchantId, String shippingMethodId) throws Throwable {
        PostResponse response = null;
        System.out.println("###### ShippingExecutor is start with adding shipping '" + shippingMethodId
                           + "'to merchant '" + merchantId + "' ######");
        MerchantRequest request = new MerchantRequest();
        request.setShippingMethod(new ShippingMethodRequest());
        request.setMerchantId(merchantId);
        request.getShippingMethod().setShippingMethodId(shippingMethodId);
        try {
            response = merchantShippingResourceClient.createShipping(clientId, request);
        } catch (com.rakuten.gep.merchant.api.client.exception.ResourceClientException ex) {
            System.out.println("Error creating shipping '" + shippingMethodId + "' for this merchant : '" + merchantId
                               + "'. ");
            throw new Exception("Error creating shipping '" + shippingMethodId + "' for this merchant : '" + merchantId
                                + "'. ");
        }
        if (info.isInfoEnabled()) {
            String message = "{ShippingExecutor has added shipping to merchant : " + "{merchantId=" + merchantId + "},"
                             + "{merchantShippingMethodId="
                             + response.getMerchant().getShippingMethod().getMerchantShippingMethodId() + "},"
                             + "{shippingMethodId=" + shippingMethodId + "}}";
            info.info(message);
        }
        return response;
    }
}
