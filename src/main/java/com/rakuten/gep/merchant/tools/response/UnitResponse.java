package com.rakuten.gep.merchant.tools.response;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 
 * unit response
 * 
 * @author sangki.cho
 * @version 1.0
 */
// @XmlRootElement
@XmlType(// name="unit",
propOrder = { "unitId", "unitCode", "name" })
@XmlAccessorType(XmlAccessType.FIELD)
public class UnitResponse implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1731512243924911768L;
    @XmlElement(type = String.class)
    private String unitId;
    @XmlElement(type = String.class)
    private String unitCode;
    @XmlElement(type = String.class)
    private String name;

    /**
     * @return the unitId
     */
    public String getUnitId() {
        return unitId;
    }

    /**
     * @return the unitCode
     */
    public String getUnitCode() {
        return unitCode;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param unitId the unitId to set
     */
    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    /**
     * @param unitCode the unitCode to set
     */
    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
