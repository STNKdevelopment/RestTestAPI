package ru.stnk.RestTestAPI.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import ru.stnk.RestTestAPI.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /*@Autowired
    private DataSource dataSource;*/

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private CustomSimpleUrlAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private CustomAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private SpringSessionRememberMeServices rememberMeServices;

    /*@Autowired
    private FindByIndexNameSessionRepository<S> sessionRepository;*/

    /*@Autowired
    private JdbcOperations jdbcOperations;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    private FindByIndexNameSessionRepository sessionRepository = new JdbcOperationsSessionRepository(jdbcOperations, platformTransactionManager);*/

    //private SimpleUrlAuthenticationFailureHandler authenticationFailureHandler = new SimpleUrlAuthenticationFailureHandler();

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        /*auth.jdbcAuthentication().dataSource(dataSource)
                //проверка существования пользователя и его состояние enable_user
                .usersByUsernameQuery("select email,password,enabled from users where email=?")
                //запрос роли пользователя
                .authoritiesByUsernameQuery("select user_email,role_name from user_roles where user_email=?")
                //нужно для проверки пароля
                .passwordEncoder(new BCryptPasswordEncoder());*/

        //auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(new BCryptPasswordEncoder());

        auth.authenticationProvider(authenticationProvider());

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                //.sessionManagement()
                //.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                //.maximumSessions(2).sessionRegistry(sessionRegistry())
                //.sessionFixation().migrateSession()
                //.and()
                //HTTP Basic authentication
                /*.httpBasic()
                .and()*/
                .rememberMe()
                .rememberMeServices(rememberMeServices)
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/reg-start").permitAll()
                .antMatchers("/reg-confirm").permitAll()
                //.antMatchers(HttpMethod.GET, "/add-role").permitAll()
                //.antMatchers(HttpMethod.GET, HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.GET, "/auth").permitAll()
                .antMatchers(HttpMethod.GET, "/currtime").permitAll()
                .antMatchers(HttpMethod.GET, "/quotations").permitAll()
                .antMatchers(HttpMethod.GET, "/coins").permitAll()
                .antMatchers(HttpMethod.GET, "/candles-one").permitAll()
                .antMatchers(HttpMethod.GET, "/candles-five").permitAll()
                .antMatchers(HttpMethod.GET, "/userinfo").authenticated()
                .antMatchers("/logout").authenticated()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                //.usernameParameter("email")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .and()
                .logout()
                //.and()
                //.sessionManagement()
                //.maximumSessions(2)
                //.sessionRegistry(sessionRegistry())
                ;

    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider
                = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsServiceImpl);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authProvider;
    }

    //https://docs.spring.io/spring-session/docs/2.1.6.BUILD-SNAPSHOT/reference/html5/#spring-security-concurrent-sessions
    /*@Bean
    public SpringSessionBackedSessionRegistry<S> sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(this.sessionRepository);
    }*/

    /*
     * Взято отсюда
     * https://alexkosarev.name/2016/05/19/spring-security-token-authentication-part-1/
     */
    /*@Bean
    public RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter() throws Exception {
        RequestHeaderAuthenticationFilter requestHeaderAuthenticationFilter = new RequestHeaderAuthenticationFilter();
        requestHeaderAuthenticationFilter.setPrincipalRequestHeader("X-AUTH-TOKEN");
        requestHeaderAuthenticationFilter.setAuthenticationManager(authenticationManager());
        //requestHeaderAuthenticationFilter.setExceptionIfHeaderMissing(false);

        return requestHeaderAuthenticationFilter;
    }

    @Bean
    public PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider() {
        PreAuthenticatedAuthenticationProvider preAuthenticatedAuthenticationProvider = new PreAuthenticatedAuthenticationProvider();
        preAuthenticatedAuthenticationProvider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(userDetailsService()));

        return preAuthenticatedAuthenticationProvider;
    }*/


    /*@Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }*/

}