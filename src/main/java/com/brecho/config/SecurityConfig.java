package com.brecho.config;

import com.brecho.model.Usuario;
import com.brecho.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioRepository userRepository;

    public SecurityConfig(UsuarioRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilite CSRF para APIs REST se você não estiver usando tokens CSRF
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/usuarios/registrar").permitAll() // Permite acesso público ao endpoint de registro
                        .anyRequest().authenticated() // Todas as outras requisições exigem autenticação
                )
                .httpBasic(org.springframework.security.config.Customizer.withDefaults()); // Autenticação básica (para testes)
        // .formLogin(Customizer.withDefaults()); // Ou autenticação via formulário

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Usuario user = userRepository.findByNome(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

            // Converte os papéis (roles) do seu modelo para GrantedAuthority
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getNome())
                    .password(user.getSenha())
                    .authorities(user.getRoles().stream()
                            .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role.getNome()))
                            .collect(Collectors.toList()))
                    .build();
        };
    }
}
