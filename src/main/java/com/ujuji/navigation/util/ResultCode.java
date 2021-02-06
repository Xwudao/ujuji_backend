package com.ujuji.navigation.util;

//状态码
public enum ResultCode {
    /* 成功状态码 */
    SUCCESS(1000, "成功"),

    /* 参数错误：10001-19999 */
    PARAM_IS_INVALID(10001, "参数无效"),
    PARAM_IS_BLANK(10002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(10003, "参数类型错误"),
    PARAM_NOT_COMPLETE(10004, "参数缺失"),

    /* 用户错误：20001-29999*/
    USER_NOT_LOGGED_IN(20001, "用户未登录"),
    USER_LOGIN_ERROR(20002, "账号不存在或密码错误"),
    USER_ACCOUNT_FORBIDDEN(20003, "账号已被禁用"),
    USER_NOT_EXIST(20004, "用户不存在"),
    USER_HAS_EXISTED(20005, "用户已存在"),
    USER_PASSWORD_ERROR(20006, "用户密码错误"),
    USER_PASSWORD_MODIFY_SUCCESS(20007, "修改密码成功"),
    USER_PASSWORD_MODIFY_FAIL(20008, "修改密码失败"),
    USER_LOGIN_SUCCESS(20009, "登录成功"),
    USER_LOGIN_FAIL(20010, "登录失败，请检查用户名和密码"),
    USER_REGISTER_SUCCESS(20011, "注册成功"),
    USER_REGISTER_FAIL(20012, "注册失败"),
    USER_OLD_PASS_ERROR(20013, "旧密码错误"),
    EMAIL_EXISTED(20014, "邮箱已存在"),
    VERIFY_CODE_ERROR(20015, "验证码错误"),
    VERIFY_CODE_EXPIRED(20016, "验证码过期请重新验证"),
    PLEASE_VERIFY_CODE(20017, "请输入验证码"),
    USERNAME_EMAIL_NOT_MATCH(20018, "请确认用户名和邮箱匹配"),
    FIND_PASS_EMAIL_SEND_SUCCESS(20019, "重置密码邮件验证码已发送成功"),
    GENERATE_TOKEN_SUCCESS(20020, "生成access_token成功"),
    GENERATE_TOKEN_FAIL(20021, "生成access_token失败"),


    /*业务：30001-39999*/
    SERVICE_QUERY_FAIL(30001, "查询失败"),
    SERVICE_INSERT_SUCCESS(30002, "添加成功"),
    SERVICE_INSERT_FAIL(30003, "添加失败"),
    SERVICE_DELETE_SUCCESS(30004, "删除成功"),
    SERVICE_DELETE_FAIL(30005, "删除失败"),
    SERVICE_UPDATE_SUCCESS(30006, "修改成功"),
    SERVICE_UPDATE_FAIL(30007, "修改失败"),

    USER_HAS_NOTICE(31001, "用户已存在一个公告,不能再次创建"),
    USER_HAS_BOX(31002, "存在相同的一个盒子，不能创建"),
    PLEASE_INPUT_USER_ID(31003, "抱歉，请输入用户ID"),
    NOT_HAS_PERMISSION_OPERATOR(31004, "您没有操作此项操作的权限"),
    PLEASE_INPUT_ID(31005, "请输入ID"),
    BOX_NOT_EXISTED(31006, "盒子不存在，请检查"),
    LINK_NOT_EXISTED(31007, "链接不存在，请检查"),
    BOX_NOT_FOUND(31008, "未找到该导航"),
    SELECT_ONE_FAIL(31009, "查询单条记录失败"),
    SUFFIX_NOT_AVAILABLE(31010, "此后缀(标签)不可用，请更换"),
    NAME_EXISTED(31011, "名称已存在"),
    SUFFIX_NOT_EXISTED(31012, "此后缀不存在"),
    BOX_PWD_ERROR(31013, "盒子密码错误"),
    SUFFIX_LENGTH_ERROR(31014, "站点后缀长度小于规定"),
    LEAVE_MSG_NOT_EXISTED(31015, "留言不存在"),
    SET_MSG_FIXED_SUCCESS(31016, "留言置/取顶成功"),
    SET_MSG_FIXED_FAIL(31017, "留言置/取顶失败"),
    REPLY_MSG_SUCCESS(31018, "回复留言成功"),
    REPLY_MSG_FAIL(31019, "回复留言失败"),
    ITEM_NOT_EXISTS(31020, "该条目不存在"),
    SET_MSG_READ_FAIL(31021, "设置留言已读失败"),
    SET_MSG_READ_SUCCESS(31022, "设置留言已读成功"),
    GENERATE_VERIFY_CODE_FAIL(31023, "生成验证码失败"),
    UNKNOWN_ERROR(31024, "未知错误"),
    MSG_TIME_TOO_SHORT(31025, "距离上次留言时间过短，暂不可留言"),
    SEND_FORGOT_PASS_TIME_TOO_SHORT(31026, "验证码已经发送过了，请检查您的垃圾箱"),
    IMPORTED_BOX_HAS_EXISTED(31027, "用户已存在相同标题的一个，请删除后导入！"),
    INSERT_BOX_FAILED(31028, "创建盒子失败，此次导入失败"),
    IMPORT_FAILED(31029, "导入失败，请收好重试"),
    TOKEN_EMPTY(31030, "AccessToken错误"),


    /* 权限错误：70001-79999 */
    PERMISSION_NO_ACCESS(70001, "无访问权限"),
    PERMISSION_NO_OPERATION(70002, "无操作权限"),
    PERMISSION_NOT_MATCH_OPERATION(70003, "无操作权限"),
    VISIT_TOO_OFTEN(70004, "操作太频繁了，请稍后重试"),
    ACCESS_TOKEN_ERROR(70005, "AccessToken无效");

    private final Integer code;
    private final String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}