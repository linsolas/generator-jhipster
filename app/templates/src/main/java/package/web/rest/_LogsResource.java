package <%=packageName%>.web.rest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.codahale.metrics.annotation.Timed;
import <%=packageName%>.web.rest.dto.LoggerDTO;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
<% if (javaVersion != '8') { %>
import java.util.ArrayList;<% } %>
import java.util.List;<% if (javaVersion == '8') { %>
import java.util.stream.Collectors;<% } %>

/**
 * Controller for view and managing Log Level at runtime.
 */
@RestController
@RequestMapping("/app")
public class LogsResource {

    @RequestMapping(value = "/rest/logs",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public List<LoggerDTO> getList() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();<% if (javaVersion == '8') { %>
	    return context.getLoggerList()
	        .stream()
	        .map(logger -> new LoggerDTO(logger))
	        .collect(Collectors.toList());
        <% } else { %>
        List<LoggerDTO> loggers = new ArrayList<>();
        for (ch.qos.logback.classic.Logger logger : context.getLoggerList()) {
            loggers.add(new LoggerDTO(logger));
        }
        return loggers;<% } %>
    }

    @RequestMapping(value = "/rest/logs",
            method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Timed
    public void changeLevel(@RequestBody LoggerDTO jsonLogger) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        context.getLogger(jsonLogger.getName()).setLevel(Level.valueOf(jsonLogger.getLevel()));
    }
}
