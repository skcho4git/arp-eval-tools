package com.rakuten.arp.eval;

/**
 * 
 * @author skcho
 *
 */
public interface Evaluator{
	
	public void invoke(final int threadCount);
	
	public void init() throws Exception;
	
}

