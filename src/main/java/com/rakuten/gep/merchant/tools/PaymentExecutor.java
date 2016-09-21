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
import com.rakuten.gep.merchant.api.client.request.PaymentMethodRequest;
import com.rakuten.gep.merchant.api.client.request.ShopRequest;
import com.rakuten.gep.merchant.api.client.response.PaymentMethodResponse;
import com.rakuten.gep.merchant.api.client.response.PostResponse;
import com.rakuten.gep.merchant.tools.constant.ClientTypeEnum;

public class PaymentExecutor extends AbstractExecutor {

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
     * payment method Ids user can use program args to set the payment methodIds he/she want to be inserted
     */
    private List<String> preferedPaymentMethodIds;
    /**
     * payment method list
     */
    private List<String> paymentMethodIdList;
    /**
     * clientId
     */
    private String clientId = ClientTypeEnum.RMS_IBS.value();

    public static void main(String[] args) {
        PaymentExecutor paymentExecutor = new PaymentExecutor();
        paymentExecutor.start(args);
    }

    @Override
    protected void start(String[] args) {
        Boolean bFailed = false;
        if (getParameter(args) == false) {
            return;
        }
        try {
            System.out.println("###### PaymentExecutor is started in executing ######");
            init();
            execute();
        } catch (Throwable t) {
            bFailed = true;
            if (error.isErrorEnabled()) {
                error.error("{PaymentExecutor is failed in executing}", t);
            }
        } finally {
            System.out.println(bFailed ? "###### PaymentExecutor is finished in executing with failure ######"
                                      : "###### PaymentExecutor is finished in executing with success ######");
            System.exit(1);
        }
    }

    /**
     * get mallId, isActive, paymentMethodId from args
     * 
     * @param args
     * @return
     */
    private boolean getParameter(String[] args) {
        if (args == null || args.length < 3) {
            System.out.println("###### please input parameter like: mallId isDemo isActive paymentMethodId ... ######");
            System.out.println("###### for example : es false true c1b12f39-64cf-e5ee-9940-3c34aa1ae212 (when add PayPal for Spain all shop) ######");
            System.out.println("######          or : id true false 1bda54a9-374e-494b-807b-311032edf771 (when add Kartu Debit Visa Mandiri for Indonesia demo shop) ######");
            System.out.println("###### put --all as paymentMethodId means that add all payment methods of the mall. ######");
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
        this.preferedPaymentMethodIds = new ArrayList<String>();
        for (int i = 2; i < args.length; i++) {
            if (i == 2 && isActive != null) {
                continue;
            }
            preferedPaymentMethodIds.add(args[i]);
        }
        if (isActive != null) {
            System.out.println("###### set isActive as " + String.valueOf(isActive.booleanValue()) + " ######");
        } else {
            System.out.println("###### please input the second parameter isActive like true or false ######");
            return false;
        }
        if (CollectionUtils.isEmpty(preferedPaymentMethodIds)) {
            System.out.println("###### please input parameter like: mallId isDemo isActive paymentMethodId ... ######");
            System.out.println("###### for example : es false true c1b12f39-64cf-e5ee-9940-3c34aa1ae212 (when add PayPal for Spain all shop) ######");
            System.out.println("######          or : id true false 1bda54a9-374e-494b-807b-311032edf771 (when add Kartu Debit Visa Mandiri for Indonesia demo shop) ######");
            System.out.println("###### put --all as paymentMethodId means that add all payment methods of the mall. ######");
            return false;
        }
        return true;
    }

    @Override
    protected void init() throws Throwable {
        super.init();
        System.out.println("###### PaymentExecutor is initialized ######");
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
            collectPaymentStatus();
            while (StringUtils.isNotBlank(csvline = br.readLine())) {
                String[] csvitems = csvline.split(",");
                shopId = csvitems[0];
                merchantId = csvitems[1];
                try {
                    for (String paymentMethodId : paymentMethodIdList) {
                        PostResponse postResponse = createMerchantPayment(merchantId, paymentMethodId);
                        createShopPayment(shopId, paymentMethodId, postResponse.getMerchant().getPaymentMethod().getMerchantPaymentMethodId());
                    }
                } catch (Throwable t) {
                    bw.write(csvline);
                    bw.newLine();
                    if (error.isErrorEnabled()) {
                        message = "{PaymentExecutor is failed with adding payment to shop : " + "{shopId=" + shopId
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
     * search mall payment method.
     * 
     * @throws Throwable
     */
    private void collectPaymentStatus() throws Throwable {
        paymentMethodIdList = new ArrayList<>();
        List<PaymentMethodResponse> allPayments = paymentResourceClient.collectPaymentStatus(this.clientId, this.mallId).getPaymentMethods();
        // if use --all as a parameter paymentMethodId, then add all paymentMethod
        if (preferedPaymentMethodIds.size() >= 1 && "--all".equals(preferedPaymentMethodIds.get(0))) {
            for (PaymentMethodResponse payment : allPayments) {
                paymentMethodIdList.add(payment.getPaymentMethodId());
            }
            return;
        }
        // otherwise, use the preferred paymentMethods according to parameter
        for (PaymentMethodResponse payment : allPayments) {
            if (preferedPaymentMethodIds.contains(payment.getPaymentMethodId()))
                paymentMethodIdList.add(payment.getPaymentMethodId());
        }
        // if the parameter paymentMethodId doesn't exist in the mall, then end.
        if (paymentMethodIdList.size() != preferedPaymentMethodIds.size()) {
            for (String preferedPaymentMethodId : preferedPaymentMethodIds) {
                if (!paymentMethodIdList.contains(preferedPaymentMethodId)) {
                    System.out.println("###### the input paymentMethodId '" + preferedPaymentMethodId
                                       + "' doesn't exist in the Mall '" + mallId + "'######");
                }
            }
            throw new Exception("please input paymentMethodId correctly. ");
        }
    }

    /**
     * create a Payment Method for a shop
     * 
     * @param shopId
     * @param paymentMethodId
     * @param merchantPaymentMethodId
     * @throws Throwable
     */
    private void createShopPayment(String shopId, String paymentMethodId, String merchantPaymentMethodId) throws Throwable {
        PostResponse postResponse = null;
        System.out.println("###### PaymentExecutor is start with adding payment '" + paymentMethodId + "' to shop '"
                           + shopId + "' ######");
        PaymentMethodRequest pyre = new PaymentMethodRequest();
        pyre.setIsActive(String.valueOf(isActive.booleanValue()));
        ShopRequest request = new ShopRequest();
        request.setShopId(shopId);
        pyre.setMerchantPaymentMethodId(merchantPaymentMethodId);
        request.setPaymentMethod(pyre);
        try {
            postResponse = shopPaymentResourceClient.createPayment(clientId, request);
        } catch (com.rakuten.gep.merchant.api.client.exception.ResourceClientException ex) {
            System.out.println("Error creating payment '" + paymentMethodId + "' for this shop : '" + shopId + "'. ");
            throw new Exception("Error creating payment '" + paymentMethodId + "' for this shop : '" + shopId + "'. ");
        }
        if (info.isInfoEnabled()) {
            String message = "{PaymentExecutor has added payment to shop : " + "{shopId=" + shopId + "},"
                             + "{shopPaymentMethodId="
                             + postResponse.getShop().getPaymentMethod().getShopPaymentMethodId() + "},"
                             + "{merchantPaymentMethodId=" + merchantPaymentMethodId + "}}";
            info.info(message);
        }
    }

    /**
     * create a Payment Method for a merchant
     * 
     * @param merchantId
     * @param paymentMethodId
     * @return
     * @throws Throwable
     */
    private PostResponse createMerchantPayment(String merchantId, String paymentMethodId) throws Throwable {
        PostResponse response = null;
        System.out.println("###### PaymentExecutor is start with adding payment '" + paymentMethodId + "'to merchant '"
                           + merchantId + "' ######");
        MerchantRequest request = new MerchantRequest();
        request.setPaymentMethod(new PaymentMethodRequest());
        request.setMerchantId(merchantId);
        request.getPaymentMethod().setPaymentMethodId(paymentMethodId);
        try {
            response = merchantPaymentResourceClient.createPayment(clientId, request);
        } catch (com.rakuten.gep.merchant.api.client.exception.ResourceClientException ex) {
            System.out.println("Error creating payment '" + paymentMethodId + "' for this merchant : '" + merchantId
                               + "'. ");
            throw new Exception("Error creating payment '" + paymentMethodId + "' for this merchant : '" + merchantId
                                + "'. ");
        }
        if (info.isInfoEnabled()) {
            String message = "{PaymentExecutor has added payment to merchant : " + "{merchantId=" + merchantId + "},"
                             + "{merchantPaymentMethodId="
                             + response.getMerchant().getPaymentMethod().getMerchantPaymentMethodId() + "},"
                             + "{paymentMethodId=" + paymentMethodId + "}}";
            info.info(message);
        }
        return response;
    }
}
