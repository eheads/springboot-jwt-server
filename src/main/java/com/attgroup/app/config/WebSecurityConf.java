package com.attgroup.app.config;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.attgroup.app.security.AuthenticationEntryPointJwtHandler;
import com.attgroup.app.security.CustomAuthenticationProvider;
import com.attgroup.app.security.LoginAccessSuccessHandler;
import com.attgroup.app.security.jwt.AuthTokenFilter;
import com.attgroup.app.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConf extends WebSecurityConfigurerAdapter{

	@Autowired
	UserService userService;

	@Autowired
	private AuthenticationEntryPointJwtHandler authenticationEntryPointJwtHandler;

	@Autowired
	private CustomAuthenticationProvider customAuthenticationProvider;

	@Bean
	public AuthTokenFilter authTokenFilter() {
		return new AuthTokenFilter();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		//authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder());
		authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManager();
	}

	@Bean
	public AuthenticationSuccessHandler myAuthenticationSuccessHandler(){
		return new LoginAccessSuccessHandler();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.cors().and().csrf().disable()
		.authorizeRequests()
		.antMatchers("/").permitAll()
		.antMatchers("/api/auth/register").permitAll().antMatchers("/api/auth/authenticate").permitAll()
		.antMatchers("/api/auth/welcome").authenticated().and()
		//.anyRequest().authenticated()
		.exceptionHandling().authenticationEntryPoint(authenticationEntryPointJwtHandler).and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.formLogin().loginPage("/login").successHandler(myAuthenticationSuccessHandler()).permitAll()
		;

		httpSecurity.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}