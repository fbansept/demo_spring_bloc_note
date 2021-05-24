package edu.fbansept.demospringblocnote.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    final UserDetailsServiceCustom userDetailsService;

    final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(edu.fbansept.demospringblocnote.security.UserDetailsServiceCustom userDetailsService, edu.fbansept.demospringblocnote.security.JwtRequestFilter jwtRequestFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .cors().configurationSource(httpServletRequest -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    corsConfiguration.applyPermitDefaultValues();
                    corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
                    corsConfiguration.setAllowedHeaders(
                            Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept", "Authorization","Access-Control-Allow-Origin"));
                    return corsConfiguration;
                })

                .and().csrf().disable()
                .authorizeRequests()

                    .antMatchers("/test/**").permitAll()
                    .antMatchers("/authentification").permitAll()
                    .antMatchers("/inscription").permitAll()
                    .antMatchers("/admin/**").hasRole("ADMINISTRATEUR")
                    .antMatchers("/user/**").hasAnyRole("ADMINISTRATEUR","UTILISATEUR")

                    /*.antMatchers("/demo/test/**").permitAll()
                    .antMatchers("/demo/authentification").permitAll()
                    .antMatchers("/demo/inscription").permitAll()
                    .antMatchers("/demo/admin/**").hasRole("ADMINISTRATEUR")
                    .antMatchers("/demo/user/**").hasAnyRole("ADMINISTRATEUR","UTILISATEUR")*/
                    .anyRequest().authenticated()
                .and().exceptionHandling()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and().addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
