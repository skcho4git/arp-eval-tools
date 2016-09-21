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
import com.rakuten.gep.merchant.api.client.request.MerchantRequest;
import com.rakuten.gep.merchant.api.client.request.PaymentMethodRequest;
import com.rakuten.gep.merchant.api.client.request.ShopRequest;
import com.rakuten.gep.merchant.api.client.response.GetResponse;
import com.rakuten.gep.merchant.api.client.response.PaymentMethodResponse;
import com.rakuten.gep.merchant.api.client.response.PostResponse;
import com.rakuten.gep.merchant.api.client.response.ShopResponse;
import com.rakuten.gep.merchant.tools.constant.ClientTypeEnum;

/**
 * This is a tool for create Debit Card payment and set Enable/disable setting followed with Credit Card setting
 * 
 * @author ts-wei.gao
 */
public class IndonesiaDebitPaymentExecutor extends AbstractExecutor {

    /**
     * mallId
     */
    private static final String mallId = "id";
    /**
     * Credit Payment id (Kartu Kredit)
     */
    private static final String CREDIT_PYMNT_ID = "b56e8d59-da9d-4a11-adc2-0115ca0f0f4e";
    /**
     * Debit Payment id (Kartu Debit Visa Mandiri)
     */
    private static final String DEBIT_PYMNT_ID = "1bda54a9-374e-494b-807b-311032edf771";
    /**
     * clientId
     */
    private static final String clientId = ClientTypeEnum.RMS_IBS.value();

    public static void main(String[] args) {
        IndonesiaDebitPaymentExecutor paymentExecutor = new IndonesiaDebitPaymentExecutor();
        paymentExecutor.start(args);
    }

    @Override
    protected void start(String[] args) {
        Boolean bFailed = false;
        try {
            System.out.println("###### IndonesiaDebitPaymentExecutor is started in executing ######");
            init();
            execute();
        } catch (Throwable t) {
            bFailed = true;
            if (error.isErrorEnabled()) {
                error.error("{IndonesiaDebitPaymentExecutor is failed in executing}", t);
            }
        } finally {
            System.out.println(bFailed
                                      ? "###### IndonesiaDebitPaymentExecutor is finished in executing with failure ######"
                                      : "###### IndonesiaDebitPaymentExecutor is finished in executing with success ######");
            System.exit(1);
        }
    }

    @Override
    protected void init() throws Throwable {
        super.init();
        System.out.println("###### IndonesiaDebitPaymentExecutor is initialized ######");
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
        String shopId = null;
        try {
            fis = new FileInputStream(properties.getProperty("merchant-tools.input"));
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            fos = new FileOutputStream(properties.getProperty("merchant-tools.output"));
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);
            collectPaymentStatus();
            while (StringUtils.isNotBlank(shopId = br.readLine())) {
                try {
                    // get the shop
                    ShopResponse shopResponse = getShop(shopId);
                    if (shopResponse == null) {
                        continue;
                    }
                    // get the credit payment method
                    ShopPayments shopPayments = getShopCreditPayment(shopId);
                    // for DEV, QA -- start -->>
                    if (shopPayments.getDebitPaymentMethodResponse() != null) {
                        if (shopPayments.getCreditPaymentMethodResponse() != null
                            && shopPayments.getCreditPaymentMethodResponse().getInactiveTime() == null
                            && shopPayments.getDebitPaymentMethodResponse().getInactiveTime() != null) {
                            updateShopPayment(shopId, DEBIT_PYMNT_ID, shopPayments.getDebitPaymentMethodResponse().getShopPaymentMethodId(), "true");
                        }
                        continue;
                    }
                    // for DEV, QA -- end --<<
                    if (shopPayments.getCreditPaymentMethodResponse() == null) {
                        continue;
                    }
                    String isActive = String.valueOf(shopPayments.getCreditPaymentMethodResponse().getInactiveTime() == null);
                    // create the merchant payment method
                    PostResponse postResponse = createMerchantPayment(shopResponse.getMerchantId(), DEBIT_PYMNT_ID);
                    // create the shop payment method
                    createShopPayment(shopId, DEBIT_PYMNT_ID, postResponse.getMerchant().getPaymentMethod().getMerchantPaymentMethodId(), isActive);
                } catch (Throwable t) {
                    bw.write(shopId);
                    bw.newLine();
                    if (error.isErrorEnabled()) {
                        message = "{IndonesiaDebitPaymentExecutor is failed with adding payment to shop : "
                                  + "{shopId=" + shopId + "}}";
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
        List<String> preferedPaymentMethodIds = new ArrayList<String>(2);
        preferedPaymentMethodIds.add(CREDIT_PYMNT_ID);
        preferedPaymentMethodIds.add(DEBIT_PYMNT_ID);
        List<String> paymentMethodIdList = new ArrayList<String>(2);
        List<PaymentMethodResponse> allPayments = paymentResourceClient.collectPaymentStatus(clientId, mallId).getPaymentMethods();
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
            throw new Exception("please confirm paymentMethodId correctly. ");
        }
    }

    /**
     * search shop information
     * 
     * @param shopId
     * @return
     * @throws Throwable
     */
    private ShopResponse getShop(String shopId) throws Throwable {
        System.out.println("### IndonesiaDebitPaymentExecutor is start with adding payment to shop " + shopId + " ###");
        ShopResponse shopResponse = shopResourceClient.getShop(clientId, shopId).getShop();
        if (shopResponse == null) {
            System.out.println("###### the shopId '" + shopId + "' doesn't exist, skip. ######");
            throw new Exception("please input shopId correctly. ");
        }
        if (!mallId.equals(shopResponse.getMallId())) {
            System.out.println("###### the shopId '" + shopId + "' doesn't exist in the Mall '" + mallId
                               + "', skip. ######");
            throw new Exception("please input shopId correctly. ");
        }
        return shopResponse;
    }

    /**
     * create a Payment Method for a shop
     * 
     * @param shopId
     * @param paymentMethodId
     * @param merchantPaymentMethodId
     * @param isActive
     * @throws Throwable
     */
    private void createShopPayment(String shopId,
                                   String paymentMethodId,
                                   String merchantPaymentMethodId,
                                   String isActive) throws Throwable {
        PostResponse postResponse = null;
        System.out.println("###### IndonesiaDebitPaymentExecutor is start with adding payment '" + paymentMethodId
                           + "' to shop '" + shopId + "' ######");
        PaymentMethodRequest pyre = new PaymentMethodRequest();
        pyre.setIsActive(isActive);
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
            String message = "{IndonesiaDebitPaymentExecutor has added payment to shop : " + "{shopId=" + shopId + "},"
                             + "{shopPaymentMethodId="
                             + postResponse.getShop().getPaymentMethod().getShopPaymentMethodId() + "},"
                             + "{merchantPaymentMethodId=" + merchantPaymentMethodId + "}}";
            info.info(message);
        }
    }

    /**
     * update a Payment Method for a shop
     * 
     * @param shopId
     * @param paymentMethodId
     * @param shopPaymentMethodId
     * @param isActive
     * @throws Throwable
     */
    private void updateShopPayment(String shopId, String paymentMethodId, String shopPaymentMethodId, String isActive) throws Throwable {
        PostResponse postResponse = null;
        System.out.println("###### IndonesiaDebitPaymentExecutor is start with updating payment '" + paymentMethodId
                           + "' to shop '" + shopId + "' ######");
        PaymentMethodRequest pyre = new PaymentMethodRequest();
        pyre.setIsActive(isActive);
        ShopRequest request = new ShopRequest();
        request.setShopId(shopId);
        pyre.setShopPaymentMethodId(shopPaymentMethodId);
        request.setPaymentMethod(pyre);
        try {
            postResponse = shopPaymentResourceClient.updatePayment(clientId, request);
        } catch (com.rakuten.gep.merchant.api.client.exception.ResourceClientException ex) {
            System.out.println("Error updating payment '" + paymentMethodId + "' for this shop : '" + shopId + "'. ");
            throw new Exception("Error updating payment '" + paymentMethodId + "' for this shop : '" + shopId + "'. ");
        }
        if (info.isInfoEnabled()) {
            String message = "{IndonesiaDebitPaymentExecutor has updated shop payment  : " + "{shopId=" + shopId + "},"
                             + "{shopPaymentMethodId="
                             + postResponse.getShop().getPaymentMethod().getShopPaymentMethodId() + "}," + "{isActive="
                             + isActive + "}}";
            info.info(message);
        }
    }

    /**
     * get Credit Payment Method for a shop
     * 
     * @param shopId
     * @throws Throwable
     */
    private ShopPayments getShopCreditPayment(String shopId) throws Throwable {
        System.out.println("###### IndonesiaDebitPaymentExecutor is start with getting credit payment for shop '"
                           + shopId + "' ######");
        ShopPayments shopPayments = new ShopPayments();
        GetResponse getResponse = null;
        try {
            getResponse = shopPaymentResourceClient.collectPaymentStatus(clientId, shopId);
        } catch (com.rakuten.gep.merchant.api.client.exception.ResourceClientException ex) {
            System.out.println("Error getting credit payments  for this shop : '" + shopId + "'. ");
            return shopPayments;
        }
        for (PaymentMethodResponse pymntRs : getResponse.getShop().getPaymentMethods()) {
            if ("Kartu Kredit".equalsIgnoreCase(pymntRs.getName())) {
                shopPayments.setCreditPaymentMethodResponse(pymntRs);
            }
            if ("Kartu Debit Visa Mandiri".equalsIgnoreCase(pymntRs.getName())) {
                shopPayments.setDebitPaymentMethodResponse(pymntRs);
            }
        }
        if (shopPayments.getCreditPaymentMethodResponse() == null) {
            System.out.println("Info getting payment '" + CREDIT_PYMNT_ID + "' for this shop : '" + shopId
                               + "'. skip this shop. ");
        }
        return shopPayments;
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
        System.out.println("###### IndonesiaDebitPaymentExecutor is start with adding payment '" + paymentMethodId
                           + "'to merchant '" + merchantId + "' ######");
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
            String message = "{IndonesiaDebitPaymentExecutor has added payment to merchant : " + "{merchantId="
                             + merchantId + "}," + "{merchantPaymentMethodId="
                             + response.getMerchant().getPaymentMethod().getMerchantPaymentMethodId() + "},"
                             + "{paymentMethodId=" + paymentMethodId + "}}";
            info.info(message);
        }
        return response;
    }

    public class ShopPayments {

        private PaymentMethodResponse creditPaymentMethodResponse;
        private PaymentMethodResponse debitPaymentMethodResponse;

        public PaymentMethodResponse getCreditPaymentMethodResponse() {
            return creditPaymentMethodResponse;
        }

        public void setCreditPaymentMethodResponse(PaymentMethodResponse creditPaymentMethodResponse) {
            this.creditPaymentMethodResponse = creditPaymentMethodResponse;
        }

        public PaymentMethodResponse getDebitPaymentMethodResponse() {
            return debitPaymentMethodResponse;
        }

        public void setDebitPaymentMethodResponse(PaymentMethodResponse debitPaymentMethodResponse) {
            this.debitPaymentMethodResponse = debitPaymentMethodResponse;
        }
    }
}
