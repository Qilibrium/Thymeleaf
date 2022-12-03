import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@WebServlet(value = "/time")
public class ThymeleafZoneId extends HttpServlet {
    static final String FORMAT_DATA_TIME = "dd.MM.yyyy HH:mm:ss";
    private TemplateEngine engine;

    @Override
    public void init() {
        engine = new TemplateEngine();

        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix("/templates/");
                //("C:\\Users\\Admin\\IdeaProjects\\JavaDeveloperGOIT\\ServletZoneId\\templates\\");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");

        Context simpleContext = new Context(req.getLocale(),
                Map.of("time", parseTime(req)));

        engine.process("test", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
    private String parseTime(HttpServletRequest request) {
        String timezone = request.getParameter("timezone");

        if(request.getParameterMap().containsKey("timezone")){
            String zoneTimePlus = timezone.replace(" ", "+");
            ZoneId zoneId = ZoneId.of(zoneTimePlus);
            ZonedDateTime zdt = ZonedDateTime.now(zoneId);
            String timeZoneUtc = zdt.format(DateTimeFormatter.ofPattern(FORMAT_DATA_TIME));
            return timeZoneUtc+" "+zoneTimePlus;

        }
        ZoneId zoneId = ZoneId.of("UTC");
        ZonedDateTime zdt = ZonedDateTime.now(zoneId);
        String utc = zdt.format(DateTimeFormatter.ofPattern(FORMAT_DATA_TIME));
        return utc + " UTC";
    }
}
