package com.boardgo.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.boardgo.jwt.LoginFilter;

@Configuration
public class SecurityConfig {

	private final AuthenticationConfiguration authenticationConfiguration;

	public SecurityConfig(AuthenticationConfiguration authenticationConfiguration) {
		this.authenticationConfiguration = authenticationConfiguration;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity

			.httpBasic(AbstractHttpConfigurer::disable)

			.csrf(AbstractHttpConfigurer::disable)

			.formLogin(AbstractHttpConfigurer::disable)

			.sessionManagement(sessionManagerConfigurer -> sessionManagerConfigurer
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

			.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration)),
				UsernamePasswordAuthenticationFilter.class)

			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/signup", "/login").permitAll()
				// TODO: 나중에 permitAll 없애기
				.anyRequest().permitAll())
			.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws
		Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
