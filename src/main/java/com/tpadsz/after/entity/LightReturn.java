package com.tpadsz.after.entity;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-04-10 16:55
 **/
public class LightReturn {

    private String productId;
    private String lmac;
    private String lname;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getLmac() {
        return lmac;
    }

    public void setLmac(String lmac) {
        this.lmac = lmac;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    @Override
    public String toString() {
        return "LightReturn{" +
                "productId='" + productId + '\'' +
                ", lmac='" + lmac + '\'' +
                ", lname='" + lname + '\'' +
                '}';
    }
}
