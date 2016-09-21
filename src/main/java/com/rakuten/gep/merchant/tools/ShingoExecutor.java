package com.rakuten.gep.merchant.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import org.apache.commons.lang.StringUtils;
import com.rakuten.gep.merchant.tools.constant.ClientTypeEnum;
import com.rakuten.gep.merchant.tools.message.Merchant;

public class ShingoExecutor extends AbstractExecutor {

    public static void main(String[] args) {
        ShingoExecutor shingo = new ShingoExecutor();
        shingo.start(args);
    }

    @Override
    protected void start(String[] args) {
        Boolean bFailed = false;
        try {
            /*
             * if(info.isInfoEnabled()){ info.info("###### meghan is started in executing ######"); }
             */
            System.out.println("###### shingo is started in executing ######");
            init();
            execute();
        } catch (Throwable t) {
            bFailed = true;
            if (error.isErrorEnabled()) {
                error.error("{shingo is failed in executing}", t);
            }
        } finally {
            System.out.println(bFailed ? "###### shingo is finished in executing with failure ######"
                                      : "###### shingo is finished in executing with success######");
            System.exit(1);
        }
    }

    @Override
    protected void init() throws Throwable {
        super.init();
        System.out.println("###### shingo is initialized ######");
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
            while (StringUtils.isNotBlank(shopId = br.readLine())) {
                try {
                    sendMessage(ClientTypeEnum.RMS_IBS.value(), shopId);
                } catch (Throwable t) {
                    bw.write(shopId);
                    bw.newLine();
                    if (error.isErrorEnabled()) {
                        message = "{shingo is failed with sending message to rabbitMQ : " + "{shopId=" + shopId + "}}";
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

    private void sendMessage(String clientId, String shopId) throws Throwable {
        String message = null, merchantId = null;
        merchantId = shopResourceClient.getShop(clientId, shopId).getShop().getMerchantId();
        System.out.println("### shingo is start with sending message to rabbitMQ " + shopId + " ###");
        amqpTemplate.convertAndSend(properties.getProperty("rabbitmq.routing.key.name"), getMessage(merchantId, shopId));
        if (info.isInfoEnabled()) {
            message = "{shingo has sent message to rabbitMQ : " + "{action="
                      + properties.getProperty("rabbitmq.message.action01") + "}," + "{merchantId=" + merchantId + "},"
                      + "{shopId=" + shopId + "}}";
            info.info(message);
        }
    }

    /**
     * 
     * @param merchantId
     * @param shopId
     * @return
     */
    private Merchant getMessage(String merchantId, String shopId) {
        Merchant merchant = new Merchant();
        // merchant.setAction("headChange");
        merchant.setAction(properties.getProperty("rabbitmq.message.action01"));
        merchant.setMerchantId(merchantId);
        merchant.setShopId(shopId);
        return merchant;
    }
}
