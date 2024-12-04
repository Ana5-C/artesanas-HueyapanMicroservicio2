package artesanas.artesanas;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    /*
     * @Bean
     * public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     * http.csrf(AbstractHttpConfigurer::disable)
     * .authorizeHttpRequests(
     * auth -> auth.requestMatchers("/signin", "/signup").permitAll()
     * .requestMatchers("/customers/**", "/payments/**",
     * "/shoppings/**","carts/**","/shippings/**","/addresses/**",
     * "/productsCarts/**").hasRole("ADMIN")
     * .requestMatchers("/").hasRole("USER")
     * .anyRequest().authenticated())
     * .formLogin(withDefaults())
     * .rememberMe(withDefaults())
     * .logout(logout -> logout.logoutUrl("/signout").permitAll());
     * return http.build();
     * }
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Deshabilitar CSRF si no es necesario
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/signin", "/signup").permitAll() // Permitir acceso sin autenticación
                        .requestMatchers("/customers/**", "/payments/**", "/shoppings/**", "/carts/**", "/shippings/**",
                                "/addresses/**", "/productsCarts/**")
                        .hasRole("ADMIN")
                        .requestMatchers("/doc/swagger-ui/**").authenticated() // Requerir autenticación para Swagger
                        .requestMatchers("/").authenticated() // Requerir autenticación para la raíz
                        .anyRequest().authenticated() // Todas las demás rutas requieren autenticación
                )
                .formLogin(form -> form
                        .loginPage("/signin") // Página de inicio de sesión personalizada
                        .defaultSuccessUrl("/", true) // Redirigir a la raíz después de autenticarse
                )
                .logout(logout -> logout
                        .logoutUrl("/signout") // URL para cerrar sesión
                        .logoutSuccessUrl("/signin") // Redirigir a la página de inicio de sesión tras cerrar sesión
                        .permitAll())
                .rememberMe(withDefaults()); // Configuración de "recordar usuario"

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var userDetailsManager = new InMemoryUserDetailsManager();
        userDetailsManager.createUser(User.withUsername("userOne")
                .password(passwordEncoder().encode("userOne"))
                .roles("USER")
                .build());
        userDetailsManager.createUser(User.withUsername("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build());
        return userDetailsManager;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
