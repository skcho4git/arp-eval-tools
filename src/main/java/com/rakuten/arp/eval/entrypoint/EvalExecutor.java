package com.rakuten.arp.eval.entrypoint;

import com.rakuten.arp.eval.constant.EvalTypeEnum;
import com.rakuten.gep.merchant.tools.constant.ClientTypeEnum;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 
 * @author skcho
 *
 */

public class EvalExecutor extends AbstractExecutor {	
	
	
	public static void main(String[] args){
		
		
		/*Optional<EvalTypeEnum> evalType = Optional.ofNullable(EvalTypeEnum.convertFrom(Optional.ofNullable(args)
				                                                                               .orElseThrow(() -> error())[0]));*/
		
		
		Optional.ofNullable(args).orElseThrow(() -> error()).length == 2 ? 
		
		
		EvalTypeEnum evalType = null;
		int threadCount = 0;
		
        
	    Arrays.stream(Optional.ofNullable(args).orElseThrow(() -> error()))
	          .
       
		
		
		switch(evalType.orElseThrow(() -> error())){
		
		    case CPU:
			    break;
		    case MEMORY:
		    	//TODO
		    	break;
		    case READ:
		    	//TODO:
		    	break;
		    case WRITE:
		    	//TODO:
		    	break;
		    case NETWORK:
		    	//TODO:
		    	break;	
			
		}
		
		
		
		
		
	}
	
	private static IllegalStateException error(){
		
		return new IllegalStateException("Usage: ping [-aAbBdDfhLnOqrRUvV] [-c count] [-i interval] [-I interface]");
		
	}
    /**
     * mallId
     */
    private String mallId = "es";
    /**
     * isTest
     */
    private String isDemo = null;
    /**
     * clientId
     */
    private String clientId = ClientTypeEnum.RMS_IBS.value();

    /*public static void main(String[] args) {
        EvalExecutor shopExecutor = new EvalExecutor();
        shopExecutor.start(args);
        try {
            shopExecutor.init();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }*/

    private boolean getParameter(String[] args) {
        if (args == null || args.length < 1) {
            System.out.println("###### please input parameter like: mallId isDemo ... ######");
            System.out.println("###### for example : es false (when get all shop list for Spain) ######");
            System.out.println("######          or : id true (when get demo shop list for Indonesia) ######");
            return false;
        }
        this.mallId = args[0];
        if (args.length >= 2 && "true".equalsIgnoreCase(args[1])) {
            this.isDemo = "true";
        }
        
        return true;
    }

    @Override
    protected void invoke(String[] args) {
        Boolean bFailed = false;
        if (getParameter(args) == false) {
            return;
        }
        try {
            System.out.println("###### ShopExecutor is started in executing ######");
            init();
            execute();
        } catch (Throwable t) {
            bFailed = true;
            if (error.isErrorEnabled()) {
                error.error("{ShopExecutor is failed in executing}", t);
            }
        } finally {
            System.out.println(bFailed ? "###### ShopExecutor is finished in executing with failure ######"
                                      : "###### ShopExecutor is finished in executing with success ######");
            exit(bFailed);
        }
    }

    @Override
    protected void execute() throws Throwable {
        PrintWriter shopListPw = null;
        try {
            String shopListOutputFile = properties.getProperty("all-shop-list.output");
            ;
            if ("true".equals(this.isDemo)) {
                shopListOutputFile = properties.getProperty("demo-shop-list.output");
            }
            shopListPw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(shopListOutputFile))));
            List<ShopResponse> shops1 = getShops(clientId, mallId);
            for (ShopResponse shop : shops1) {
                shopListPw.println(shop.getShopId() + "," + shop.getMerchantId());
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

    protected List<ShopResponse> getShops(String clientId, String mallId) throws Throwable {
        System.out.println("### Start get shop list for  mallId: " + mallId + " isDemo: " + String.valueOf(this.isDemo)
                           + " ###");
        System.out.println(shopResourceClient);
        List<String> status = new ArrayList<String>(2);
        status.add("Staging");
        status.add("Live");
        GetResponse shopResponseList = shopResourceClient.collectShopStatus(clientId, mallId, null, null, status, this.isDemo, "true");
        return shopResponseList.getShops();
    }
}
