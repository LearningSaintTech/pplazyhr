package admin_user.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import admin_user.service.CustomSuccessHandler;
import admin_user.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    CustomSuccessHandler customSuccessHandler;

    @Autowired
    CustomUserDetailsService customUserDetailsService;
    
//    @Autowired
//    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(c -> c.disable())
            .authorizeHttpRequests(request -> request.requestMatchers("/admin-page")
                .hasAuthority("ADMIN")
                .requestMatchers("/manager-page").hasAuthority("MANAGER")
                .requestMatchers("/user-page").hasAuthority("USER")
                .requestMatchers("/emp/{id}").hasAuthority("USER")
                .requestMatchers("/api/clocking/**").hasAuthority("USER")
                .requestMatchers("/attendance-by-id").hasAuthority("USER")
                .requestMatchers("/employee/**").hasAnyAuthority("ADMIN", "MANAGER")
               // .requestMatchers("/activateUser", "/deactivateUser").hasRole("ADMIN")
                .requestMatchers("/show-emp/**").hasAnyAuthority("ADMIN", "MANAGER")
                .requestMatchers("/add-emp/**").hasAnyAuthority("ADMIN", "MANAGER")
                .requestMatchers("/admin/tickets/**").hasAnyAuthority("ADMIN", "MANAGER")
                .requestMatchers("/attendence").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/add").hasAnyAuthority("ADMIN", "MANAGER")
                .requestMatchers("/registration", "/css/**", "/img/**", "/forgot-password", "/reset-password").permitAll()
                .anyRequest().authenticated())
            .formLogin(form -> form.loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(customSuccessHandler)
                .permitAll())
            .logout(form -> form.invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll());
//            .sessionManagement(session -> session
//                .sessionFixation().migrateSession()
//                .maximumSessions(1)
//                .expiredUrl("/login?expired"));

       // http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
//    }
}
