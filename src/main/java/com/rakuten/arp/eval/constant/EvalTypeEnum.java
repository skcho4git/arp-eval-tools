package com.rakuten.arp.eval.constant;

/**
 * eval type enum
 * 
 * @author skcho
 * @version 1.0
 * 
 */
public enum EvalTypeEnum {
	

	/**
	 * CPU evaluation
	 */
	CPU("cpu"),	
	
	/**
	 * Memory evaluation
	 */
	MEMORY("memory"),	
	
	/**
	 * Disk read operate evaluation
	 */
	READ("read"),
	
	/**
	 * Disk write operate evaluation
	 */
	WRITE("write"),
	
	/**
	 * Network I/O evaluation
	 */
	NETWORK("network");
   

    private final String value;

    public String value() {
        return value;
    }

    private EvalTypeEnum(String value) {
        this.value = value;
    }

    public static boolean isExist(String value) {
        for (EvalTypeEnum d : values()) {
            if (d.value().equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static EvalTypeEnum convertFrom(String value) {
        for (EvalTypeEnum e : values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        return null;
    }
}