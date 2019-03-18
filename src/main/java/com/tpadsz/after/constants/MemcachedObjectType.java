package com.tpadsz.after.constants;

public enum MemcachedObjectType {

    //===========================日志key前缀==============================

//    COUNTER_MEMBER("uic_counter_member:", 60 * 60 * 24),
//    
//    COUNTER_WORK("uic_counter_work:", 60 * 60 * 24),
    
    COUNTER_SERIALNO("uic_counter_serialno:", 0), 
    
    CACHE_MESSAGE_VERIFICATION("uic_cache_message_verify_%s", 5 * 60),
    
    CACHE_TOKEN("uic_cache_token:", 3 * 24 * 3600);


    //=================================================================

    private String prefix;
    private int expiredTime;

    MemcachedObjectType(String prefix, int expiredTime) {
        this.prefix = prefix;
        this.expiredTime = expiredTime;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getExpiredTime() {
        return expiredTime;
    }
}
