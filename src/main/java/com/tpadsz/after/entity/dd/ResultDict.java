package com.tpadsz.after.entity.dd;

public enum ResultDict {

    SUCCESS("000", "成功"), SYSTEM_ERROR("200", "系统错误"), ACCOUNT_NOT_CORRECT("101", "账号密码不正确！"), PASSWORD_NOT_CORRECT("102", "账号密码不正确！"),MOBILE_NOT_EXISTED("103", "该手机号未绑定！"),LIGHT_EXISTED("120", "有灯存在，冻结"),
    UNAME_REPET("201", "该用户名已存在！"),VERIFY_ERROR("202", "验证码不正确！"),MOBILE_REPET("203", "该手机号已绑定！"),PARAMS_BLANK("301", "参数不能够为空"), PARAMS_NOT_PARSED("302", "参数解析错误"),MESHID_NOT_NULL("115","无此meshId"),
    NO_SCENE("301", "该用户或网络下没有创建场景"), NO_GROUP("302", "该用户或网络下没有创建组");

    ResultDict(String code, String value) {
        this.value = value;
        this.code = code;
    }

    private String value;
    private String code;

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

}
