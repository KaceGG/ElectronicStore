package javaweb.ElectronicStore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javaweb.ElectronicStore.api.security.jwt.AuthEntryPointJwt;
import javaweb.ElectronicStore.api.security.jwt.AuthTokenFilter;
import javaweb.ElectronicStore.oauth2.CustomerOAuth2UserService;
import javaweb.ElectronicStore.oauth2.OAuth2LoginSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	@Autowired
	private CustomerOAuth2UserService oAuth2UserService;
	
	@Autowired
	private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
	
	@Autowired
	public CustomAuthSucessHandler sucessHandler;
	
//	@Autowired
//	private CustomerOAuth2UserService customerOAuth2UserService;
//	
	 @Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
//
//	@Bean
//	public UserDetailsService getDetailsService() {
//		return new CustomUserDetailsService();
//	}
//	@Bean
//	public DaoAuthenticationProvider getAuthenticationProvider() {
//		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//		daoAuthenticationProvider.setUserDetailsService(getDetailsService());
//		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());	
//		return daoAuthenticationProvider;
//	}
	
	  @Autowired CustomUserDetailsService userDetailsService;

	  @Autowired private AuthEntryPointJwt unauthorizedHandler;

	  @Bean
	  public AuthTokenFilter authenticationJwtTokenFilter() {
	    return new AuthTokenFilter();
	  }
	  
	  @Bean
	  public DaoAuthenticationProvider authenticationProvider() {
	      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	       
	      authProvider.setUserDetailsService(userDetailsService);
	      authProvider.setPasswordEncoder(passwordEncoder());
	   
	      return authProvider;
	  }
	  
	  @Bean
	  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
	    return authConfig.getAuthenticationManager();
	  }	
	  
	  @Bean
	  @Order(1)
	  public SecurityFilterChain filterChainApis(HttpSecurity http) throws Exception {
	    http
	    	.cors(Customizer.withDefaults())
	    	.csrf(csrf -> csrf.disable())
	        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .securityMatcher(AntPathRequestMatcher.antMatcher("/api/**"))
	        .authorizeHttpRequests(auth -> 
		        auth.requestMatchers("/api/auth/**").permitAll()
        		.anyRequest().authenticated()
	        )
	        .authenticationProvider(authenticationProvider())
	        .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	    
	    return http.build();
	  }
	  
		@Bean
		@Order(2)
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .csrf(csrf -> csrf.disable())
	            .authorizeHttpRequests(auth -> {
	            	auth.requestMatchers("/user/**").hasRole("USER");
	            	auth.requestMatchers("/admin/**").hasRole("ADMIN");
	            	auth.requestMatchers("/auth/**").permitAll();
	            	auth.requestMatchers("/**").permitAll();
	                auth.anyRequest().authenticated();
	            })
	                
	            .formLogin(login -> login
	                    .loginPage("/signin")
	                    .loginProcessingUrl("/userLogin")
	                    .successHandler(sucessHandler)
	                    .permitAll()
	                )
	            .oauth2Login(oauth2Login -> oauth2Login
	                    .loginPage("/signin")
	                    .userInfoEndpoint(userInfo -> userInfo
	                        .userService(oAuth2UserService)
	                    )
	                    .successHandler(oAuth2LoginSuccessHandler)
	            );
	        return http.build();
	    }
}
