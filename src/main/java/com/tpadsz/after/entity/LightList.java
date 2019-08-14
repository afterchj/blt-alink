package com.tpadsz.after.entity;

/**
 * @program: blt-alink
 * @description:
 * @author: Mr.Ma
 * @create: 2019-03-07 17:57
 **/
public class LightList {
    private Integer id;
    private Integer lid;
    private String productId;
    private String lmac;
    private String lname;
    private Integer gid;
//    private Integer typeId;
    private String x;
    private String y;
//    private String tname;
//    private String tnum;
    private Integer groupId;
    private Integer mid;
//    private String productName;
//    private String irrEff;
//    private String power;
//    private String voltage;
//    private String current;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    private Integer pid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getLid() {
        return lid;
    }

    public void setLid(Integer lid) {
        this.lid = lid;
    }

//    public String getProductName() {
//        return productName;
//    }
//
//    public void setProductName(String productName) {
//        this.productName = productName;
//    }
//
//    public String getIrrEff() {
//        return irrEff;
//    }
//
//    public void setIrrEff(String irrEff) {
//        this.irrEff = irrEff;
//    }
//
//    public String getPower() {
//        return power;
//    }
//
//    public void setPower(String power) {
//        this.power = power;
//    }
//
//    public String getVoltage() {
//        return voltage;
//    }
//
//    public void setVoltage(String voltage) {
//        this.voltage = voltage;
//    }
//
//    public String getCurrent() {
//        return current;
//    }
//
//    public void setCurrent(String current) {
//        this.current = current;
//    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

//    public String getTname() {
//        return tname;
//    }
//
//    public void setTname(String tname) {
//        this.tname = tname;
//    }

//    public String getTnum() {
//        return tnum;
//    }
//
//    public void setTnum(String tnum) {
//        this.tnum = tnum;
//    }

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

    public void setGid(Integer gid) {
        this.gid = gid;
    }

//    public Integer getTypeId() {
//        return typeId;
//    }
//
//    public void setTypeId(Integer typeId) {
//        this.typeId = typeId;
//    }

    public Integer getGid() {
        return gid;
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
                ", gid=" + gid +
//                ", typeId=" + typeId +
                ", x='" + x + '\'' +
                ", y='" + y + '\'' +
//                ", tname='" + tname + '\'' +
//                ", tnum='" + tnum + '\'' +
                ", groupId=" + groupId +
                ", mid=" + mid +
//                ", productName='" + productName + '\'' +
//                ", irrEff='" + irrEff + '\'' +
//                ", power='" + power + '\'' +
//                ", voltage='" + voltage + '\'' +
//                ", current='" + current + '\'' +
                '}';
    }
}
