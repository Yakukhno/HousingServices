package ua.training.configuration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import ua.training.controller.filter.CharsetFilter;
import ua.training.controller.listener.SessionListener;
import ua.training.model.entities.person.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import static ua.training.controller.util.Attributes.DISPATCHER;
import static ua.training.controller.util.Attributes.TENANT;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.setAttribute(TENANT, User.Role.TENANT);
        servletContext.setAttribute(DISPATCHER, User.Role.DISPATCHER);
        servletContext.addListener(new SessionListener());
        servletContext.addFilter("EncodeFilter", CharsetFilter.class)
                .addMappingForUrlPatterns(null, false, "/*");
    }
}
