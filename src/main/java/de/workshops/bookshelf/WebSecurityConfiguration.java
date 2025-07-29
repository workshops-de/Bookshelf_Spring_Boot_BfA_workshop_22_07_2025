package de.workshops.bookshelf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class WebSecurityConfiguration {
//  // see https://docs.spring.io/spring-security/reference/5.8/migration/servlet/exploits.html,
//  // https://docs.spring.io/spring-security/reference/5.8/migration/servlet/exploits.html#_i_am_using_angularjs_or_another_javascript_framework,
//  // and https://github.com/spring-projects/spring-security/issues/12915#issuecomment-1482669321
//  CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
//  // Please note: This disables CSRF BREACH protection.
//  // https://blog.gypsyengineer.com/en/security/csrf-tokens-in-spring-and-the-breach-attack.html
//  // https://security.stackexchange.com/questions/43669/with-breach-attack-is-session-based-csrf-token-still-secure
//  // See this more elaborate implementation for both use cases (JavaScript and default login form) to work
//  // and still including CSRF BREACH protection: https://github.com/spring-projects/spring-security/issues/12915#issuecomment-1482931700
//  CsrfTokenRequestAttributeHandler delegate = new CsrfTokenRequestAttributeHandler();

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(authorize ->
            authorize
                .requestMatchers(
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/webjars/swagger-ui/**",
                    "/h2-console/**"
                ).permitAll()
                .anyRequest().authenticated()
        )
        .httpBasic(withDefaults())
        .formLogin(withDefaults())
        .csrf(csrf -> csrf.disable())
//        .csrf(csrf -> csrf.ignoringRequestMatchers(
//                AntPathRequestMatcher.antMatcher("/h2-console/**")
//            )
//            .csrfTokenRepository(tokenRepository)
//            .csrfTokenRequestHandler(delegate::handle) )
//        .headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        .build();
  }

  @Bean
  UserDetailsService userDetailsService(JdbcTemplate jdbcTemplate) {
    return username -> {
      String sql = "SELECT * FROM bookshelf_user WHERE username = ?";

      return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new User(
          rs.getString("username"),
          rs.getString("password"),
          List.of(new SimpleGrantedAuthority(rs.getString("role")))
      ), username);
    };
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
