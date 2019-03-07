package com.tpadsz.after.entity.dd;

public enum ResultDict {

    SUCCESS("000", "成功"), SYSTEM_ERROR("200", "系统错误"), ACCOUNT_NOT_CORRECT("101", "登陆账号不正确"),PASSWORD_NOT_CORRECT("102", "登陆账号密码不正确"),
    UNAME_REPET("201", "用户名已存在"), VERIFY_ERROR("202", "验证码不正确"),
    PARAMS_BLANK("301", "参数不能够为空"), PARAMS_NOT_PARSED("302", "参数解析错误");

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
