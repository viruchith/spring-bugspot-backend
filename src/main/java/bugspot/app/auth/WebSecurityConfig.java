package bugspot.app.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Autowired
	private JWTAuthenticationFilter jwtAuthenticationFilter;
	
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
        .csrf((csrf) -> csrf.disable()            
        ).authorizeHttpRequests((authorize)->{
			authorize.requestMatchers("/v3/*","/v3/api-docs/*","/*/hello","/*/signup", "/*/login","/swagger-ui/*").permitAll().anyRequest().authenticated();
			
        }).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class).sessionManagement((mgmgt)->{
				mgmgt.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			});

		
		
		
//		http.authorizeRequests((authorize)->{
//			authorize.requestMatchers("/*/hello","/*/signup", "/*/login").permitAll();
//			authorize.anyRequest().authenticated();
//		});
		
		
				
//		http.csrf().disable().authorizeRequests().requestMatchers("/*/hello","/*/signup", "/*/login").permitAll().anyRequest()
//				.authenticated().and().exceptionHandling().and().sessionManagement()
//				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//		http.cors();
		

		return http.build();
	}

}
