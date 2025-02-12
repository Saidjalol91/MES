package kr.co.goodstream.lotus.admin;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import kr.co.goodstream.lotus.admin.commons.security.handler.LoginFailureHandler;
import kr.co.goodstream.lotus.admin.commons.security.handler.LoginSuccessHandler;
import kr.co.goodstream.lotus.admin.pda.security.filter.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Autowired
	@Qualifier("usersDetailsService")
	private UserDetailsService userDetailsService;

	@Autowired
	private LoginSuccessHandler loginSuccessHandler;

	@Autowired
	private LoginFailureHandler loginFailurHandler;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/admin/**").hasAnyRole("ROLE_ADMIN,ROLE_DEV");
		http.authorizeRequests().antMatchers("/login").permitAll();
		http.authorizeRequests().antMatchers("/updateLoginPassword").permitAll();
		http.authorizeRequests().antMatchers("/skipLoginPassword").permitAll();
		http.authorizeRequests().antMatchers("/file/**").permitAll();
		http.authorizeRequests().antMatchers("/temp/**").permitAll();
		http.csrf().disable();
		http.headers().frameOptions().disable();

		// static resources
		http.authorizeRequests().antMatchers("/resources/**").permitAll();

		http.authorizeRequests().antMatchers("/login").anonymous().anyRequest().authenticated().and().formLogin().loginPage("/login")
				.loginProcessingUrl("/authority").successHandler(loginSuccessHandler).failureHandler(loginFailurHandler).usernameParameter("username")
				.passwordParameter("password").and().logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout").and().rememberMe()
				.tokenValiditySeconds(31536000).userDetailsService(userDetailsService)/*.tokenRepository(tokenRepository())*/;

		http.sessionManagement().invalidSessionUrl("/login");
		http.exceptionHandling().accessDeniedPage("/login");
		
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers("/pda/v1/user/authenticate")
			.antMatchers("/pda/v1/user/revoke")
			.antMatchers("/pda/v1/user/connection_checker");
	}
	
	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public FilterRegistrationBean getSpringSecurityFilterChainBindedToError(
			@Qualifier("springSecurityFilterChain") Filter springSecurityFilterChain) {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(springSecurityFilterChain);
		registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
		return registration;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/*@Bean
	public PersistentTokenRepository tokenRepository() {
		JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
		jdbcTokenRepository.setDataSource(dataSource);
		return jdbcTokenRepository;
	}*/

}
