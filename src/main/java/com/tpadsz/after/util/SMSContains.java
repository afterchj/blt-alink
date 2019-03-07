package com.tpadsz.after.util;

/**
 * Created by hongjian.chen on 2018/12/26.
 */
public enum SMSContains {


    TT_LOCK("csms18122512", "3a7844875fdf46a38eb5125c7b1c18ba"), XM_GIFT("csms18122510", "00527ea2b9034ee4a725ee8e8d9579f5"),
    XM_LIGHT("csms18122605", "9814e9e408ca46298a3bd70004f45f25"), XM_LIFE("csms18122513", "c4d29f9796d440ef9e5ea0e66a45f3a5"),
    ALINK_LAIGHT("8819030746012", "e6760f450f7e4867b4e8c8e10888ee13");

    private String sender;
    private String template;

    SMSContains(String sender, String template) {
        this.sender = sender;
        this.template = template;
    }

    public String getSender() {
        return sender;
    }

    public String getTemplate() {
        return template;
    }

//    public static void main(String[] args) {
//        for (SMSContains contains:SMSContains.values()){
//            System.out.println("sender="+contains.getSender()+",template="+contains.getTemplate());
//        }
//    }
}
