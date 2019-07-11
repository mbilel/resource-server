package com.example.demo;

import io.micrometer.core.instrument.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.*;

import java.io.IOException;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${rest.oauth2.resource.token-info-uri}")
    private String tokenUrl;

    @Value("${rest.oauth2.resource.jwt.key-value}")
    private String keyValue;

    @Value("${rest.oauth2.resource.mode}")
    private String mode;

    @Value("${rest.oauth2.resource.key-file}")
    private String publickeyFile;


    @Override
    public void configure(final ResourceServerSecurityConfigurer config) {
        if(mode.equals(TokenCheckEnum.WS.getValue())){
            config.tokenServices(remotetokenServices());
        }else if(mode.equals(TokenCheckEnum.REMOTE_KEY.getValue())){
            config.tokenServices(tokenServices());
        }else{
            config.tokenServices(tokenServices());
        }

    }

    public RemoteTokenServices remotetokenServices() {

        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        StringBuilder serverUrlBuilder = new StringBuilder();
        remoteTokenServices.setCheckTokenEndpointUrl(serverUrlBuilder.append(tokenUrl).toString());
        remoteTokenServices.setClientId("client");
        remoteTokenServices.setClientSecret("password");
        return remoteTokenServices;

    }

    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }


    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        String publicKey;

        if(mode.equals(TokenCheckEnum.LOCAL_KEY_FILE.getValue())){
            Resource resource = new ClassPathResource(publickeyFile);

            try {
                publicKey = IOUtils.toString(resource.getInputStream());
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            publicKey = keyValue;
        }

        converter.setVerifierKey(publicKey);
        return converter;
    }
}

