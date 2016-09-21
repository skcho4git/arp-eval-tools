package com.rakuten.gep.merchant.tools;

import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.rakuten.gep.merchant.api.client.MerchantResourceClient;
import com.rakuten.gep.merchant.api.client.PaymentResourceClient;
import com.rakuten.gep.merchant.api.client.ShippingResourceClient;
import com.rakuten.gep.merchant.api.client.ShopResourceClient;

public abstract class AbstractExecutor {

    /**
     * slf4j info logger
     */
    protected Logger info = LoggerFactory.getLogger(AbstractExecutor.class);
    /**
     * slf4j warn logger
     */
    // private Logger warn = LoggerFactory.getLogger("log.warn");
    /**
     * slf4j error logger
     */
    protected Logger error = LoggerFactory.getLogger("log.error");
    /**
     * merchant resource client
     */
    protected MerchantResourceClient merchantResourceClient;
    /**
     * shop resource client
     */
    protected ShopResourceClient shopResourceClient;
    /**
     * shipping resource client
     */
    protected ShippingResourceClient shippingResourceClient;
    /**
     * merchant shipping resource client
     */
    protected ShippingResourceClient merchantShippingResourceClient;
    /**
     * shop shipping resource client
     */
    protected ShippingResourceClient shopShippingResourceClient;
    /**
     * payment resource client
     */
    protected PaymentResourceClient paymentResourceClient;
    /**
     * merchant payment resource client
     */
    protected PaymentResourceClient merchantPaymentResourceClient;
    /**
     * shop payment resource client
     */
    protected PaymentResourceClient shopPaymentResourceClient;
    /**
     * advanced message queue protocol template
     */
    protected AmqpTemplate amqpTemplate;
    /**
     * properties
     */
    protected Properties properties;

    /**
     *
     */
    protected abstract void start(String[] args);

    /**
     * 
     * @throws Throwable
     */
    protected void init() throws Throwable {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {
                                                                                                 "classpath:/applicationContext-resourceClient.xml",
                                                                                                 "classpath:/applicationContext-service.xml" });
        merchantResourceClient = (MerchantResourceClient) applicationContext.getBean("merchantResourceClient");
        shopResourceClient = (ShopResourceClient) applicationContext.getBean("shopResourceClient");
        shippingResourceClient = (ShippingResourceClient) applicationContext.getBean("shippingResourceClient");
        merchantShippingResourceClient = (ShippingResourceClient) applicationContext.getBean("merchantResourceClient");
        shopShippingResourceClient = (ShippingResourceClient) applicationContext.getBean("shopResourceClient");
        paymentResourceClient = (PaymentResourceClient) applicationContext.getBean("paymentResourceClient");
        merchantPaymentResourceClient = (PaymentResourceClient) applicationContext.getBean("merchantResourceClient");
        shopPaymentResourceClient = (PaymentResourceClient) applicationContext.getBean("shopResourceClient");
        amqpTemplate = (AmqpTemplate) applicationContext.getBean("amqpTemplate");
        properties = (Properties) applicationContext.getBean("properties");
        /*
         * if(info.isInfoEnabled()){ //info.info("### meghan is initialized ###"); }
         */
    }

    /**
     * 
     * @throws Throwable
     */
    protected abstract void execute() throws Throwable;

    protected void exit(boolean bFailed) {
        if (bFailed) {
            System.exit(1);
        } else {
            System.exit(0);
        }
    }
}
