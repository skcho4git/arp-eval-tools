package com.rakuten.arp.eval.entrypoint;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


import com.rakuten.arp.eval.constant.EvalTypeEnum;
import com.rakuten.arp.eval.Evaluator;
import com.rakuten.arp.eval.impl.CPUEvaluator;
import com.rakuten.arp.eval.impl.MemoryEvaluator;
import com.rakuten.arp.eval.impl.NetworkEvaluator;
import com.rakuten.arp.eval.impl.ReadEvaluator;
import com.rakuten.arp.eval.impl.WriteEvaluator;

/**
 * 
 * @author skcho
 *
 */

public class EvalExecutor  {
	
	
	/**
     * slf4j info logger
     */
    private Logger info = LoggerFactory.getLogger(EvalExecutor.class);
    /**
     * slf4j warn logger
     */
    private Logger warn = LoggerFactory.getLogger("log.warn");
    /**
     * slf4j error logger
     */
    private Logger error = LoggerFactory.getLogger("log.error");     
    
    private Evaluator cpuEvaluator;    
    
    private Evaluator memoryEvaluator;    
    
    private Evaluator readEvaluator;
    
    private Evaluator writeEvaluator;
    
    private Evaluator networkEvaluator;
	
    /**
	 * @return the cpuEvaluator
	 */
	public Evaluator getCpuEvaluator() {
		return cpuEvaluator;
	}


	/**
	 * @param cpuEvaluator the cpuEvaluator to set
	 */
	public void setCpuEvaluator(Evaluator cpuEvaluator) {
		this.cpuEvaluator = cpuEvaluator;
	}


	/**
	 * @return the memoryEvaluator
	 */
	public Evaluator getMemoryEvaluator() {
		return memoryEvaluator;
	}


	/**
	 * @param memoryEvaluator the memoryEvaluator to set
	 */
	public void setMemoryEvaluator(Evaluator memoryEvaluator) {
		this.memoryEvaluator = memoryEvaluator;
	}


	/**
	 * @return the readEvaluator
	 */
	public Evaluator getReadEvaluator() {
		return readEvaluator;
	}


	/**
	 * @param readEvaluator the readEvaluator to set
	 */
	public void setReadEvaluator(Evaluator readEvaluator) {
		this.readEvaluator = readEvaluator;
	}


	/**
	 * @return the writeEvaluator
	 */
	public Evaluator getWriteEvaluator() {
		return writeEvaluator;
	}


	/**
	 * @param writeEvaluator the writeEvaluator to set
	 */
	public void setWriteEvaluator(Evaluator writeEvaluator) {
		this.writeEvaluator = writeEvaluator;
	}


	/**
	 * @return the networkEvaluator
	 */
	public Evaluator getNetworkEvaluator() {
		return networkEvaluator;
	}


	/**
	 * @param networkEvaluator the networkEvaluator to set
	 */
	public void setNetworkEvaluator(Evaluator networkEvaluator) {
		this.networkEvaluator = networkEvaluator;
	}


	public static void main(String[] args){
		
		
		new EvalExecutor().execute(args);
		
	}
    

    private void execute(String[] args){
    	
    	EvalTypeEnum evalType = null;
		int threadCount = 0;
		
		try{
			
			    List<Optional<String>> argList = Arrays.stream(Optional.ofNullable(args).orElseThrow(() -> error()))
                                                       .map(arg -> Optional.ofNullable(arg))
                                                       .collect(Collectors.toList());
			    
			    
			    if(argList.size() < 2){

			    	throw error();
			    	
			    }
			    
			    
			    evalType = argList.get(0)
			    		          .map(arg -> EvalTypeEnum.convertFrom(arg))
			    		          .orElseThrow(() -> error());
			    
			    
			    threadCount = argList.get(1)
			    		             .map(arg -> Integer.valueOf(arg))
			    		             .orElseThrow(() -> error())
			    		             .intValue();
			    
			    
			    init();
			    
			    
			    switch(evalType){
			    
			        case CPU:
			        	getCpuEvaluator().invoke(threadCount);
				        break;
				    case MEMORY:
				    	getMemoryEvaluator().invoke(threadCount);
				        break;
				    case READ:
				    	getReadEvaluator().invoke(threadCount);
				        break;
				    case WRITE:
				    	getWriteEvaluator().invoke(threadCount);
				        break;
				    case NETWORK:
				        getNetworkEvaluator().invoke(threadCount);
				        break;

			    }
			    
			    
		}catch(Exception e){
			
			error.error("{EvalExecutor failed to execute}", e);
			
		}
    	
    	
    }
    
    private void init() throws Exception{
    	
    	ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[] {"classpath:/applicationContext.xml" });
    	
    	setCpuEvaluator((CPUEvaluator)applicationContext.getBean("cpuEvaluator"));
    	setMemoryEvaluator((MemoryEvaluator)applicationContext.getBean("memoryEvaluator"));
    	setReadEvaluator((ReadEvaluator)applicationContext.getBean("readEvaluator"));
    	setWriteEvaluator((WriteEvaluator)applicationContext.getBean("writeEvaluator"));
    	setNetworkEvaluator((NetworkEvaluator)applicationContext.getBean("networkEvaluator"));
    	
    }
	
	private IllegalStateException error(){
		
		return new IllegalStateException("Usage: eval.sh (cpu|memory|read|write|network) (${#thread})");
		
	}
    
}
