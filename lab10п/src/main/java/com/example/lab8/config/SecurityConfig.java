package com.example.lab8.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Включает поддержку @PreAuthorize и других аннотаций
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Отключаем CSRF, так как используем JWT (обычно для stateless API)
                .csrf(AbstractHttpConfigurer::disable)
                // Настраиваем правила авторизации запросов
                .authorizeHttpRequests(auth -> auth
                        // Эндпоинты Keycloak (если бы они были здесь) и другие публичные эндпоинты
                        // .requestMatchers("/public/**").permitAll() // Пример
                        // Требуем роли для защищенных эндпоинтов
                        .requestMatchers("/api/orders/**").hasAnyRole("USER", "ADMIN") // Используем роли из Keycloak
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // Используем роли из Keycloak
                        // Все остальные запросы требуют аутентификации
                        .anyRequest().authenticated()
                )
                // Конфигурируем сервер ресурсов OAuth2 для работы с JWT
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                // Указываем кастомный конвертер для извлечения ролей
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )
                // Устанавливаем политику управления сессиями на STATELESS, так как используем токены
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }

    // Бин для конвертации JWT в объект Authentication с правильными ролями
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        // Указываем Spring Security, как извлечь роли из JWT
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            // Смотрим в стандартный claim 'realm_access' -> 'roles'
            Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
            if (realmAccess == null || realmAccess.get("roles") == null) {
                return List.of(); // Возвращаем пустой список, если ролей нет
            }
            Collection<String> roles = (Collection<String>) realmAccess.get("roles");
            // Преобразуем строки ролей в GrantedAuthority (добавляя префикс ROLE_, если он нужен)
            return roles.stream()
                    // Убери .toUpperCase(), если твои роли в Keycloak уже ROLE_USER, ROLE_ADMIN
                    // Добавь префикс "ROLE_", если его нет в Keycloak, а @PreAuthorize его ожидает
                    // .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .map(SimpleGrantedAuthority::new) // Если роли уже ROLE_USER, ROLE_ADMIN
                    .collect(Collectors.toList());
        });
        // Опционально: если нужно извлекать имя пользователя из другого claim'а (по умолчанию 'sub')
        // jwtConverter.setPrincipalClaimName("preferred_username");
        return jwtConverter;
    }

    // Бин PasswordEncoder больше не нужен здесь, удали его из AppConfig
}