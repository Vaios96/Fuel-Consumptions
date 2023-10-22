package chicko.project.fuelconsumptions.config;


import chicko.project.fuelconsumptions.service.IUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;


@Configuration
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(IUserService userService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());

        return auth;
    }

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.   authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers(("/")).permitAll()
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/processRegistration").permitAll()
                                .requestMatchers(HttpMethod.GET, "/fuel-consumptions/**").hasAuthority("VISITOR")
                                .requestMatchers(HttpMethod.POST, "/fuel-consumptions/**").hasAuthority("VISITOR")
                                .requestMatchers(HttpMethod.PUT, "/fuel-consumptions/**").hasAuthority("VISITOR")
                                .requestMatchers(HttpMethod.DELETE, "/fuel-consumptions/**").hasAuthority("VISITOR")
                                .requestMatchers(HttpMethod.GET, "/manufacturers/list").hasAuthority("VISITOR")
                                .requestMatchers(HttpMethod.GET,"manufacturers/add").hasAuthority("EMPLOYEE")
                                .requestMatchers(HttpMethod.POST,"manufacturers/save").hasAuthority("EMPLOYEE")
                                .requestMatchers(HttpMethod.GET,"manufacturers/update").hasAuthority("EMPLOYEE")
                                .requestMatchers(HttpMethod.POST,"manufacturers/processUpdate").hasAuthority("EMPLOYEE")
                                .requestMatchers(HttpMethod.GET,"manufacturers/delete").hasAuthority("EMPLOYEE")
                                .requestMatchers(HttpMethod.GET, "/models/list").hasAuthority("VISITOR")
                                .requestMatchers(HttpMethod.GET,"models/add").hasAuthority("EMPLOYEE")
                                .requestMatchers(HttpMethod.POST,"models/save").hasAuthority("EMPLOYEE")
                                .requestMatchers(HttpMethod.GET,"models/update").hasAuthority("EMPLOYEE")
                                .requestMatchers(HttpMethod.POST,"models/processUpdate").hasAuthority("EMPLOYEE")
                                .requestMatchers(HttpMethod.GET,"models/delete").hasAuthority("EMPLOYEE")
                                .requestMatchers(HttpMethod.GET,"roles/list").hasAuthority("EMPLOYEE")
                                .requestMatchers(HttpMethod.POST,"roles/addEmployee").hasAuthority("EMPLOYEE")
                                .requestMatchers(HttpMethod.GET,"user/change-password").hasAuthority("VISITOR")
                                .requestMatchers(HttpMethod.POST,"user/processUpdate").hasAuthority("VISITOR")
                                .anyRequest().authenticated())
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/access-denied/"))
                .formLogin(form ->

                        form.loginPage("/login")
                            .loginProcessingUrl("/authenticateTheUser")
                            .defaultSuccessUrl("/fuel-consumptions/list")
                            .permitAll()
                )
                .logout(logout -> logout.permitAll());


        return http.build();
    }
}
