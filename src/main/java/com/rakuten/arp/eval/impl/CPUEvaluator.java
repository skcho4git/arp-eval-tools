package com.rakuten.arp.eval.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.rakuten.arp.eval.Evaluator;

/**
 * 
 * @author skcho
 *
 */
public class CPUEvaluator implements Evaluator {
	

	@Override
	@SuppressWarnings("unchecked")
	public void invoke(final int threadCount){		
		
		final long start = System.currentTimeMillis();		
		
		CompletableFuture<Void>[] futureArray = new CompletableFuture[threadCount];
		//ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		
		List<ExecutorService> executorList = new ArrayList<>(threadCount);
		ExecutorService executor = null;
		
		for(int i = 0 ; i < threadCount ; i++){			
			
			futureArray[i] = CompletableFuture.runAsync(() -> calcMandelbrot(),(executor = Executors.newSingleThreadExecutor()));
			executorList.add(executor);
		}		
				
		CompletableFuture.allOf(futureArray)
		                 .thenRun(() -> System.out.println("Elapsed Time: " + (System.currentTimeMillis() - start) + "ms"))
		                 .thenRun(() -> {
		                	 
		                	 executorList.stream()
		                	             .forEach(ExecutorService::shutdown);
		                	 
		                 });
	    	
		
	}
	
	@Override
	public void init() throws Exception{
		
		//TODO	
		
	}
	
	private void calcMandelbrot(){
		//TODO		
		long start = System.currentTimeMillis();

		   // 初期値を変化させて，マンデルブロの漸化式を計算する
	       for (double x = -2.0; x <= 0.6; x += 0.001) {
	           for (double y = -1.2; y <= 1.2; y += 0.001) {
	               Point c = new Point(x, y, 0);
	               Point z = new Point(0.0, 0.0, 0);
	               
	               while (true) {
	                   z = calcNextPoint(z, c);
	                   if (z.length() > 100000 || z.number > 0x200) {
	                       break;
	                   }
	               }
	               
	               c.number = z.number;
	               
	           }
	       }
	       
	       long end = System.currentTimeMillis();	       
	       System.out.println(Thread.currentThread().getId() + "," + (end - start) + "ms");		
		
		
	}
	
	private Point calcNextPoint(Point z, Point c) {
		
		
		// マンデルブロ漸化式の計算
		double x = Math.pow(z.x, 2.0) - Math.pow(z.y, 2.0) + c.x;
		//double x = z.x * z.x - z.y * z.y + c.x;
		double y = 2 * z.x * z.y + c.y;       
	       
		z.x = x;
		z.y = y;
	    z.number++;	       
	    return z;
        
   }
   
   private class Point {
	   
	    public double x;
	    public double y;

	    public int number;

	    public Point(double x, double y, int number) {
	        this.x = x;
	        this.y = y;
	        this.number = number;
	    }

	    public double length() {
	        return x * x + y * y;
	    }
	}
	
	

}
