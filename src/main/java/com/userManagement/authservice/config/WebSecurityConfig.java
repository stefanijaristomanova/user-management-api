package com.userManagement.authservice.config;


import com.userManagement.authservice.auth.JwtOncePerRequestFilter;
import com.userManagement.authservice.auth.AuthenticationEntryPointImpl;
import com.userManagement.authservice.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${authorization.enabled}")
    private boolean authorizationEnabled;

    public static final String[] AUTHORIZED_URL = {
            "/authenticate",
            "/users/registration",
            "/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/actuator/health"};

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationEntryPointImpl authenticationEntryPoint;
    private final JwtOncePerRequestFilter jwtOncePerRequestFilter;

    @Autowired
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, AuthenticationEntryPointImpl authenticationEntryPoint, JwtOncePerRequestFilter jwtOncePerRequestFilter) {
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtOncePerRequestFilter = jwtOncePerRequestFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        String[] authorizedUrls = {"/**"};
        if (authorizationEnabled) {
            authorizedUrls = AUTHORIZED_URL;
        }

        httpSecurity
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(authorizedUrls).permitAll()
                .anyRequest().authenticated().and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity.addFilterBefore(jwtOncePerRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
