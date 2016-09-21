package com.rakuten.arp.eval.entrypoint;

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
    private Logger warn = LoggerFactory.getLogger("log.warn");
    /**
     * slf4j error logger
     */
    protected Logger error = LoggerFactory.getLogger("log.error");    
    /**
     * properties
     */
    protected Properties properties;

    /**
     *
     */
    protected abstract void invoke(String[] args);

    /**
     * 
     * @throws Throwable
     */
    protected void init() throws Throwable {
        
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
