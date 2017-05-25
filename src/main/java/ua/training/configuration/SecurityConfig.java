package ua.training.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.training.controller.validator.ApacheDigestPasswordEncoder;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static ua.training.controller.Attributes.EMAIL;
import static ua.training.controller.NewRoutes.*;
import static ua.training.controller.Roles.DISPATCHER;
import static ua.training.controller.Roles.TENANT;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private String[] permitAllRoutesGET;
    private String[] permitAllRoutesPOST;
    private String[] anonymousRoutesGET;
    private String[] anonymousRoutesPOST;
    private String[] authenticatedRoutesGET;
    private String[] authenticatedRoutesPOST;
    private String[] tenantRoutesGET;
    private String[] tenantRoutesPOST;
    private String[] dispatcherRoutesGET;
    private String[] dispatcherRoutesPOST;

    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new ApacheDigestPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        initRoutes();
        http
                .formLogin()
                    .loginPage(LOGIN_ROUTE)
                    .loginProcessingUrl(LOGIN_ROUTE)
                    .defaultSuccessUrl(HOME_ROUTE)
                    .failureUrl(LOGIN_ROUTE)
                    .usernameParameter(EMAIL).and()
                .authorizeRequests()
                    .regexMatchers(GET, permitAllRoutesGET).permitAll()
                    .regexMatchers(POST, permitAllRoutesPOST).permitAll()
                    .regexMatchers(GET, anonymousRoutesGET).anonymous()
                    .regexMatchers(POST, anonymousRoutesPOST).anonymous()
                    .regexMatchers(GET, authenticatedRoutesGET).authenticated()
                    .regexMatchers(POST, authenticatedRoutesPOST).authenticated()
                    .regexMatchers(GET, tenantRoutesGET).hasRole(TENANT)
                    .regexMatchers(POST, tenantRoutesPOST).hasRole(TENANT)
                    .regexMatchers(GET, dispatcherRoutesGET).hasRole(DISPATCHER)
                    .regexMatchers(POST, dispatcherRoutesPOST).hasRole(DISPATCHER)
                    .anyRequest().denyAll().and()
                .logout()
                    .logoutUrl(LOGOUT_ROUTE)
                    .logoutSuccessUrl(LOGIN_ROUTE).and()
                .csrf().disable();
    }

    private void initRoutes() {
        permitAllRoutesGET = new String[]{ROOT_ROUTE, HOME_ROUTE,
                TASK_ROUTE, BRIGADE_WITH_ID_ROUTE};
        permitAllRoutesPOST = new String[]{LOCALE_ROUTE};
        anonymousRoutesGET = new String[]{LOGIN_ROUTE, NEW_USER_ROUTE};
        anonymousRoutesPOST = new String[]{LOGIN_ROUTE, USER_ROUTE};
        authenticatedRoutesGET = new String[]{USER_WITH_ID_ROUTE};
        authenticatedRoutesPOST = new String[]{USER_WITH_ID_ROUTE, LOGOUT_ROUTE};
        tenantRoutesGET = new String[]{USER_APPLICATIONS_ROUTE, NEW_APPLICATION_ROUTE};
        tenantRoutesPOST = new String[]{APPLICATION_ROUTE, DELETE_APPLICATION_ROUTE};
        dispatcherRoutesGET = new String[]{APPLICATION_ROUTE, WORKER_ROUTE,
                NEW_WORKER_ROUTE, NEW_TASK_ROUTE};
        dispatcherRoutesPOST = new String[]{WORKER_ROUTE, TASK_ROUTE, NEW_TASK_ROUTE};
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/fonts/**",
                "/WEB-INF/**", "/favicon.ico", "/welcome.png");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
