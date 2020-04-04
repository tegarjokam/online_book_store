package com.tegar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Value("${security.oauth2.client.id}")
	private String clientId;
	
	@Value("${security.oauth2.client.client-id}")
	private String clientSecret;
	
	@Value("${security.oauth2.client.access-token-validity-seconds}")
	private int accessToken;
	
	@Value("${security.oauth2.client.refresh-token-validity-seconds}")
	private int refreshToken;
	
	@Value("${security.oauth2.client.authorized-grant-types}")
	private String[] authorizedGrantTypes;
	
	@Value("${security.oauth2.client.scope}")
	private String[] scopes;
	
	@Value("${security.oauth2.client.resource.id}")
	private String resourceId;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient(clientId)
				.secret(passwordEncoder.encode(clientSecret))
				.accessTokenValiditySeconds(accessToken)
				.refreshTokenValiditySeconds(refreshToken)  // 12 hours
				.authorizedGrantTypes(authorizedGrantTypes)  // 30 days
				.scopes(scopes)
				.resourceIds(resourceId);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.accessTokenConverter(accessTokenConverter())
					.userDetailsService(userDetailsService)
					.authenticationManager(authenticationManager);
	}
	
	@Bean
	private JwtAccessTokenConverter accessTokenConverter() {
		return new JwtAccessTokenConverter();
	}
}
