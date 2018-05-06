package br.com.coutinho.b2w.swapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Spring security configuration class
 * 
 * @author Rafael Coutinho
 *
 */
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Value("${config.users.admin.unm}")
	private String adminName;
	
	@Value("${config.users.admin.pwd}")
	private String adminPwd;
	
	@Value("${config.users.user.unm}")
	private String userName;
	
	@Value("${config.users.user.pwd}")
	private String userPwd;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
				.antMatchers("/b2w-swapi/**").hasAnyRole("ADMIN","USER")
				.and().httpBasic()
				.and()
                .logout().logoutUrl("/b2w-swapi/logout").invalidateHttpSession(true);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(userName).password(userPwd).roles("USER")
		.and().withUser(adminName).password(adminPwd).roles("ADMIN", "USER");
	}

}
