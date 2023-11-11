package com.teamnine.noFreeRider.util;

import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

public class CookieUtil {

    /**
     * 쿠키를 HTTP 응답에 추가합니다
     * @param response : 쿠키를 추가할 응답
     * @param name : 만들 쿠키 이름
     * @param value : 쿠키에 넣을 값
     * @param maxAge : 쿠키의 만료 기간
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * 해당 이름의 쿠키를 삭제합니다
     * @param request : 쿠키를 없앨 요청
     * @param response : 쿠키를 없애고 보낼 응답
     * @param name : 대상 쿠키 이름
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    /**
     * 객체를 직렬화해 쿠키의 값으로 변환
     * @param obj :변환시킬 객체
     * @return : 쿠키 값으로 사용할 value
     */
    public static String serialize(Object obj) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(obj));
    }

    /**
     * 쿠키를 역직렬화해서 객체로 반환
     * @param cookie : 객체를 추출할 쿠키
     * @param cls : 반환 객체
     * @return : 역질렬화 객체
     * @param <T> : 반환 객체 타입
     */
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }

}
