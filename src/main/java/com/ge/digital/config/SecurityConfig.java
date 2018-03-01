package com.ge.digital.config;

import com.ge.predix.uaa.token.lib.FastTokenServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import java.util.Collections;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${trustedIssuerId}")
    private String trustedIssuerId;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/api-docs").antMatchers("/api/swagger**");
    }

    /**
     * Configure Spring Security
     *
     * @param http http security instance to be configured
     * @throws Exception if error occurs
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .exceptionHandling()
                .accessDeniedHandler(new OAuth2AccessDeniedHandler())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .anonymous().disable()
                .addFilterAfter(securityTokenFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                .authorizeRequests()
                .antMatchers("/api/**")
                .access("isFullyAuthenticated()");
    }

    /**
     * @return {@link OAuth2AuthenticationProcessingFilter} used by Spring security to filter requests
     */
    private OAuth2AuthenticationProcessingFilter securityTokenFilter() {
        OAuth2AuthenticationManager oauthAuthenticationManager = new OAuth2AuthenticationManager();
        oauthAuthenticationManager.setTokenServices(tokenServices());
        OAuth2AuthenticationProcessingFilter filter = new OAuth2AuthenticationProcessingFilter();
        filter.setAuthenticationManager(oauthAuthenticationManager);
        filter.setAuthenticationEntryPoint(new OAuth2AuthenticationEntryPoint());
        return filter;
    }

    /**
     * @return {@link ResourceServerTokenServices} used by filter to access token service
     */
    @Bean
    @ConfigurationProperties(prefix = "uaa.tokenservices")
    public ResourceServerTokenServices tokenServices() {
        FastTokenServices fastTokenServices = new FastTokenServices();
        fastTokenServices.setTrustedIssuers(Collections.singletonList(trustedIssuerId));
        return fastTokenServices;
    }
}
