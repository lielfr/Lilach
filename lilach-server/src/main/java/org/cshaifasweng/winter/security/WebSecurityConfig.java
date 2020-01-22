package org.cshaifasweng.winter.security;

import org.cshaifasweng.winter.da.UserRepository;
import org.cshaifasweng.winter.services.UserDetailsService;
import org.cshaifasweng.winter.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;

    private UserService userService;

    private final DataSource dataSource;

    private final WebApplicationContext applicationContext;

    private final UserRepository userRepository;

    private final LilachLogoutHandler logoutHandler;

    public WebSecurityConfig(DataSource dataSource, WebApplicationContext applicationContext, UserRepository userRepository, LilachLogoutHandler logoutHandler) {
        this.dataSource = dataSource;
        this.applicationContext = applicationContext;
        this.userRepository = userRepository;
        this.logoutHandler = logoutHandler;
    }

    @PostConstruct
    public void completeSetup() {
        userDetailsService = applicationContext.getBean(UserDetailsService.class);
        userService = applicationContext.getBean(UserService.class);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.PUT, "/catalog/**")
                .hasAuthority(SecurityConstants.PRIVILEGE_CATALOG_EDIT)
                .antMatchers(HttpMethod.POST, "/catalog")
                .hasAuthority(SecurityConstants.PRIVILEGE_CATALOG_EDIT)
                .antMatchers(HttpMethod.GET, "/catalog", SecurityConstants.AUTH_LOGIN_URL)
                .permitAll()
                .antMatchers(HttpMethod.GET, "/store/**/catalog")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/customer")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/complaint")
                .hasAuthority(SecurityConstants.PRIVILEGE_COMPLAINT_FILE)
                .antMatchers(HttpMethod.PUT, "/complaint/**")
                .hasAuthority(SecurityConstants.PRIVILEGE_COMPLAINT_HANDLE)
                .antMatchers(HttpMethod.GET, "/complaint")
                .hasAuthority(SecurityConstants.PRIVILEGE_COMPLAINT_HANDLE)
                .anyRequest().authenticated()
                .and()
                .addFilter(new AuthenticationFilter(authenticationManager(), userRepository, userService))
                .addFilter(new AuthorizationFilter(authenticationManager()))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .logout(httpSecurityLogoutConfigurer -> {
                    httpSecurityLogoutConfigurer.clearAuthentication(false);
                    httpSecurityLogoutConfigurer.logoutUrl(SecurityConstants.AUTH_LOGOUT_URL);
                    httpSecurityLogoutConfigurer.logoutSuccessHandler(logoutHandler);
                });
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());

        return source;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
