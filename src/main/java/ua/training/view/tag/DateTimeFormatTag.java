package ua.training.view.tag;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class DateTimeFormatTag extends SimpleTagSupport {

    private static final String DATE_FORMAT_PATTERN = "dd.MM.yyyy HH:mm";

    private StringWriter sw = new StringWriter();

    @Override
    public void doTag() throws JspException, IOException {
        getJspBody().invoke(sw);
        LocalDateTime localDateTime = LocalDateTime.parse(sw.toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN);
        String str = localDateTime.format(formatter);
        getJspContext().getOut().println(str);
    }
}
