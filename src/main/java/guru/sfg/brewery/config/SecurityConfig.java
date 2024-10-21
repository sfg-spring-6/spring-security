package guru.sfg.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests(authorizeRequests -> authorizeRequests.antMatchers("/", "/webjars/**", "/resources/**").permitAll()
                        .antMatchers("/beers/find**", "/beers**").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll())
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("John")
                .password("{noop}Doe")
                .roles("CUSTOMER")
                .and()
                .withUser("user")
                .password("{noop}password")
                .roles("USER");

        auth.inMemoryAuthentication().withUser("Jane").password("{noop}Smith").roles("CUSTOMER");
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {

        /*UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();*/

        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("scott")
                .password("tiger")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }
}
