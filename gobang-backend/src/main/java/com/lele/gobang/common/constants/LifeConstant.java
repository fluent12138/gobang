package com.lele.gobang.common.constants;

import java.math.BigDecimal;

/**
 * @author lele
 * @create 2023-01-06 12:16
 */
public class LifeConstant {
    public static final Integer DEFAULT_LIFE = 99;
    public static final Integer DEFAULT_ACCURATE = 2;
    public static final Integer DEFAULT_TENSION = 0;
    public static final Integer DEFAULT_LOVING = 0;
    public static final BigDecimal DEFAULT_LOVING_START = new BigDecimal(1647619200000L);
    public static final BigDecimal DEFAULT_BIRTHDAY = new BigDecimal(1011456000000L);

    //acwing
    public static final String APPID = "4500";
    public static final String APPSECRET = "abb015a0376746edaf206bbf06b413df";
    public static final String ACAPP_REDIRECT_URI = "https://app4362.acapp.acwing.com.cn/api/life/acwing/acapp/receive_code/";
    public static final String APPLY_ACWING_ACCESS_TOKEN_URL = "https://www.acwing.com/third_party/api/oauth2/access_token/";
    public static final String APPLY_ACWING_USERINFO_URL = "https://www.acwing.com/third_party/api/meta/identity/getinfo/";
}
