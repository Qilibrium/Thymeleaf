import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@WebServlet(value = "/time")
public class ThymeleafZoneId extends HttpServlet {
    private final String FORMAT_DATA_TIME = "dd.MM.yyyy HH:mm:ss";
    private final String TIMEZONE_COOKIE_KEY = "lastTimezone";
    private TemplateEngine engine;

    private Properties properties = PropertiesUtil.loadProperties("application.properties");

    @Override
    public void init() {
        engine = new TemplateEngine();

        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(properties.getProperty("prefix"));
        resolver.setSuffix(properties.getProperty("suffix"));
        resolver.setTemplateMode(properties.getProperty("template"));
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");

        String timezone = req.getParameter("timezone");
        if (timezone != null) {
            CookieUtil.addCookie(TIMEZONE_COOKIE_KEY, timezone, resp);
        }

        String outputTime = parseTime(req, timezone);

        Context simpleContext = new Context(req.getLocale(),
                Map.of("time", outputTime));
        engine.process("test", simpleContext, resp.getWriter());

        resp.getWriter().close();
    }

    private String parseTime(HttpServletRequest req, String timezone) {
        String lastTimezone = CookieUtil.getCookie("lastTimezone", req);

        if (lastTimezone != null && timezone == null) {
            timezone = lastTimezone;
        } else if (timezone == null) {
            timezone = "UTC";
        }

        ZoneId zoneId = ZoneId.of(timezone);
        ZonedDateTime zdt = ZonedDateTime.now(zoneId);
        String outputTime = zdt.format(DateTimeFormatter.ofPattern(FORMAT_DATA_TIME));
        return outputTime + " " + timezone;
    }
}
