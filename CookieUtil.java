package com.mbsl.velocity.risk.auth;

import java.io.FileInputStream;
import java.util.Properties;
import org.springframework.web.util.WebUtils;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {
    public static void create(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, String name, String value, Boolean secure, Integer maxAge, String domain) {
        Cookie cookie = new Cookie(name, value);
        cookie.setSecure(secure);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        cookie.setDomain(httpServletRequest.getServerName());
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
    }

    public static void clear(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setDomain(httpServletRequest.getServerName());
        httpServletResponse.addCookie(cookie);
    }

    public static String getValue(HttpServletRequest httpServletRequest, String name) {
        Cookie cookie = WebUtils.getCookie(httpServletRequest, name);
        return cookie != null ? cookie.getValue() : null;
    }
    
    private static String getProptValue(String option) {
        String value = null;
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("/usr/local/Velocity/config/as400.properties"));
            value = prop.getProperty(option);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}

