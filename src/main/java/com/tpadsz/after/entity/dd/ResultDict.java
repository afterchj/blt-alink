package com.tpadsz.after.entity.dd;

public enum ResultDict {

    SUCCESS("000", "成功"), SYSTEM_ERROR("200", "系统错误"), ACCOUNT_NOT_CORRECT("101", "账号或密码不正确！"),
    PASSWORD_NOT_CORRECT("102", "账号密码不正确！"), MOBILE_NOT_EXISTED("103", "该手机号未绑定！"),
    ACCOUNT_IS_DISABLED("104", "token校验失败，该账号已被禁用！"), ACCOUNT_DISABLED("105", "该账号已被禁用！"),
    ADMIN_NOT_ALLOWED("106", "管理员账号不能进入客户端！"), LIGHT_EXISTED("120", "有灯存在，冻结"),
    ID_REPEATED("121", "该账户下的meshId重复"), TOKEN_NOT_SUBMIT("107", "token没有提交"),
    TOKEN_NOT_CORRECT("108", "token失效"), TOKEN_REPLACED("109", "账号在其他设备登陆。"),
    UNAME_REPET("201", "该用户名已存在！"), VERIFY_ERROR("202", "验证码不正确！"),
    MOBILE_REPET("203", "该手机号已绑定！"), PARAMS_BLANK("301", "参数不能够为空"),
    PARAMS_NOT_PARSED("302", "参数解析错误"), MESHID_NOT_NULL("115", "未发现该网络"),
    NO_SCENE("301", "该用户或网络下没有创建场景"), NO_GROUP("302", "该用户或网络下没有创建组"),
    DUPLICATE_NAME("303", "名称重复"), DUPLICATE_GID("304", "该组名已存在"), NO_DEFAULT_PLACE("305", "未发现区域"),
    EXISTED_LIGHTS("307", "存在设备"),DUPLICATE_PLACE_NAME("308","该区域名已存在"),GROUP_NAME_DUPLICATE("309","该组名已存在");

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
