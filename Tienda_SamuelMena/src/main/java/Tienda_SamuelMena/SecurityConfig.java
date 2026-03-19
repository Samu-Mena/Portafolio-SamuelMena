/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tienda_SamuelMena;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String[] PUBLIC_URLS = {
        "/", "/index", "/fav/**", "/carrito/**", "/consultas/**", "/registro/**",
        "/js/**", "/webjars/**", "/login", "/acceso_denegado"
    };

    public static final String[] ADMIN_URLS = {
        "/producto/nuevo", "/producto/guardar", "/producto/modificar/**", "/producto/eliminar/**",
        "/categoria/nuevo", "/categoria/guardar", "/categoria/modificar/**", "/categoria/eliminar/**",
        "/usuario/nuevo", "/usuario/guardar", "/usuario/modificar/**", "/usuario/eliminar/**"
    };

    public static final String[] ADMIN_OR_VENDEDOR_URLS = {
        "/producto/listado", "/categoria/listado", "/usuario/listado"
    };

    public static final String[] USUARIO_URLS = {
        "/facturar/carrito"
    };
/*
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                .requestMatchers(PUBLIC_URLS).permitAll()
                .requestMatchers(ADMIN_URLS).hasRole("ADMIN")
                .requestMatchers(ADMIN_OR_VENDEDOR_URLS).hasAnyRole("ADMIN", "VENDEDOR")
                .requestMatchers(USUARIO_URLS).hasRole("USUARIO")
                .anyRequest().authenticated()
        )
                .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
                )
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                )
                .exceptionHandling(exceptions -> exceptions
                .accessDeniedPage("/acceso_denegado")
                )
                .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                );

        return http.build();
    }
*/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
/*
    @Bean
    public UserDetailsService users(PasswordEncoder passwordEncoder) {

        UserDetails admin = User.builder()
                .username("juan")
                .password(passwordEncoder.encode("123"))
                .roles("ADMIN")
                .build();

        UserDetails sales = User.builder()
                .username("rebeca")
                .password(passwordEncoder.encode("456"))
                .roles("VENDEDOR")
                .build();

        UserDetails user = User.builder()
                .username("pedro")
                .password(passwordEncoder.encode("789"))
                .roles("USUARIO")
                .build();

        return new InMemoryUserDetailsManager(admin, sales, user);
    }
*/

    @Autowired
public void configureGlobal(AuthenticationManagerBuilder build,
    @Lazy PasswordEncoder passwordEncoder,
    @Lazy UserDetailsService userDetailsService) throws Exception {
    build.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
}


}
