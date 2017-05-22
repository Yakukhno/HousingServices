package ua.training.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ua.training.controller.validator.ApacheDigestPasswordEncoder;
import ua.training.model.service.UserService;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new ApacheDigestPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                    .loginPage("/rest/login")
                    .loginProcessingUrl("/rest/login")
                    .defaultSuccessUrl("/rest/home")
                    .failureUrl("/rest/login")
                    .usernameParameter("email").and()
                .authorizeRequests()
                    .regexMatchers(GET, "/rest/home").permitAll()
                    .regexMatchers(GET, "/rest/task").permitAll()
                    .regexMatchers(POST, "/rest/locale").permitAll()
                    .regexMatchers(GET, "/rest/brigade/\\d+").permitAll()
                    .regexMatchers("/rest/login").anonymous()
                    .regexMatchers(GET, "/rest/new_user").anonymous()
                    .regexMatchers(POST, "/rest/user").anonymous()
                    .regexMatchers(GET, "/rest/user/\\d+").authenticated()
                    .regexMatchers(POST, "/rest/user/\\d+").authenticated()
                    .regexMatchers(POST, "/rest/logout").authenticated()
                    .regexMatchers(POST, "/rest/application").hasRole("TENANT")
                    .regexMatchers(GET, "/rest/user/application").hasRole("TENANT")
                    .regexMatchers(GET, "/rest/new_application").hasRole("TENANT")
                    .regexMatchers(POST, "/rest/application/\\d+/delete").hasRole("TENANT")
                    .regexMatchers(GET, "/rest/application").hasRole("DISPATCHER")
                    .regexMatchers("/rest/new_task").hasRole("DISPATCHER")
                    .regexMatchers(POST, "/rest/task").hasRole("DISPATCHER")
                    .regexMatchers(GET, "/rest/worker").hasRole("DISPATCHER")
                    .regexMatchers(POST, "/rest/worker").hasRole("DISPATCHER")
                    .regexMatchers(GET, "/rest/new_worker").hasRole("DISPATCHER")
                    .anyRequest().denyAll().and()
                .logout()
                    .logoutUrl("/rest/logout")
                    .logoutSuccessUrl("/rest/login").and()
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService((UserDetailsService) userService)
                .passwordEncoder(passwordEncoder());
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
