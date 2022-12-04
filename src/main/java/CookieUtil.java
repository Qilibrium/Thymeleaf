import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CookieUtil {

    public static String getCookie(String key, HttpServletRequest req) {
        return getCookies(req).get(key);
    }

    public static void addCookie(String cookieKey, String cookieValue, HttpServletResponse resp) {
        Cookie cookie = new Cookie(cookieKey, cookieValue);
        resp.addCookie(cookie);
    }

    public static Map<String, String> getCookies(HttpServletRequest req) {
        Map<String, String> cookies = new HashMap<>();

        String cookie = req.getHeader("Cookie");
        if (cookie == null) {
            return Collections.emptyMap();
        }

        String[] separateCookies = cookie.split(";");
        for (String pair : separateCookies) {
            String[] keyValue = pair.split("=");
            cookies.put(keyValue[0], keyValue[1]);
        }
        return cookies;
    }
}
