package com.tpadsz.after.entity;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-07 17:57
 **/
public class LightExtend {
    private Integer lid;
    private String productId;
    private String lmac;
    private String lname;
    private String x;
    private String y;
    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Integer getLid() {
        return lid;
    }

    public void setLid(Integer lid) {
        this.lid = lid;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

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
        return "LightList{" +
                "lid=" + lid +
                ", productId='" + productId + '\'' +
                ", lmac='" + lmac + '\'' +
                ", lname='" + lname + '\'' +
                ", x='" + x + '\'' +
                ", y='" + y + '\'' +
                '}';
    }
}
