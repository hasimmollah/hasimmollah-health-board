
package com.hasim.healthboard.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.csrf.CsrfFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class DashboardSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    /*	http
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint)
        .and()
        .authorizeRequests()
        .antMatchers("*").permitAll()
        .antMatchers("/health-board-websocket/**").permitAll();*/

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        /*.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint)
        .and()*/
        .authorizeRequests().anyRequest().permitAll()
       /* .antMatchers("*").permitAll()
        .antMatchers("/health-board-websocket/**").permitAll()*/
       // .antMatchers(SecurityParams.PUBLIC_ROUTES).permitAll()
        /*.antMatchers("/health-board-websocket/**").permitAll()*/.and()
        .headers()

        .httpStrictTransportSecurity().includeSubDomains(true)
            .maxAgeInSeconds(31536000).and().contentTypeOptions().and().frameOptions()
            ;
        http.csrf().disable();
        http.addFilterAfter(new CSRFFilter(), CsrfFilter.class);
    }

}
