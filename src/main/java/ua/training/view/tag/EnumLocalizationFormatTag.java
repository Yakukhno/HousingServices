package ua.training.view.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.StringWriter;

public class EnumLocalizationFormatTag extends SimpleTagSupport {

    private String enumClass;
    private StringWriter sw = new StringWriter();

    public void setEnumClass(String enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public void doTag() throws JspException, IOException {
        getJspBody().invoke(sw);
        getJspContext().getOut().println(formBundleString(sw.toString()));
    }

    private String formBundleString(String string) {
        return enumClass.substring(0, 1).toLowerCase() +
                enumClass.substring(1) + "." + string.trim().toLowerCase();
    }

}
