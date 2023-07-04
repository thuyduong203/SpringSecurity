package com.example.springsecuritytd3.configuration;

import com.example.springsecuritytd3.utils.RSAKeyProperties;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    private final RSAKeyProperties keys;

    public SecurityConfiguration(RSAKeyProperties keys) {
        this.keys = keys;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService detailsService) {
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(detailsService);
        daoProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/auth/**", "/oauth2/**", "/hi").permitAll();
                    auth.requestMatchers("/admin/**").hasRole("ADMIN");
                    auth.requestMatchers("/user/**").hasAnyRole("ADMIN", "USER");
                    auth.anyRequest().authenticated();
                });

        http.oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter());
        http.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
//        http
//                .oauth2Login()
//                .loginPage("/login")
//                .defaultSuccessUrl("/success")
//                .and();
////                .logout()
////                .logoutUrl("/logout")
////                .logoutSuccessUrl("/login");
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        //dung publicKey de cau hinh doi tuong jwtDecoder
        return NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build();
//JwtDecoder: Giao diện JwtDecoder được sử dụng để giải mã (decode) JWT
// và trích xuất thông tin từ JWT đã được ký và mã hóa.
// Nó đảm nhận nhiệm vụ xác minh tính hợp lệ của JWT và
// trích xuất các thành phần của JWT như header, payload và signature.
// JwtDecoder được sử dụng trong quá trình xác thực và ủy quyền
// để kiểm tra tính hợp lệ của JWT và lấy thông tin người dùng từ JWT.
        //
        //khi giải mã JWT, chỉ cần sử dụng khóa công khai để xác minh tính hợp lệ của JWT.
        // Khóa công khai được sử dụng để xem xét chữ ký của JWT và đảm bảo rằng
        // nó không bị thay đổi từ khi nó được mã hóa.
        // Thông tin người dùng và các thông tin khác được chứa trong payload
        // của JWT có thể được trích xuất bằng cách sử dụng khóa công khai mà không cần sử dụng khóa riêng.
        //
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        //JWK (JSON Web Key)
        //tao ra jwt tu cap khoa RSA
        JWK jwk = new RSAKey.Builder(keys.getPublicKey()).privateKey(keys.getPrivateKey()).build();
        // tu jwt => jwtDecode:  & jwtEncoder
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
        //JwtEncoder: Giao diện JwtEncoder được sử dụng để mã hóa (encode)
        // thông tin người dùng thành JWT.
        // Nó nhận thông tin người dùng, như thông tin xác thực, quyền hạn,
        // và thông tin khác, và tạo ra một chuỗi JWT đã được mã hóa.
        // JwtEncoder được sử dụng trong quá trình tạo JWT cho việc xác thực và ủy quyền.
        //
        //Khi mã hóa JWT, việc sử dụng cả cặp khóa công khai (public key) và khóa riêng (private key)
        // là cần thiết. Trong trường hợp của JwtEncoder, cặp khóa RSA được sử dụng để tạo chữ ký cho JWT.
        // Điều này đảm bảo tính toàn vẹn của JWT và nguồn gốc của nó,
        // giúp người nhận có thể xác minh tính hợp lệ của JWT bằng cách sử dụng khóa công khai.
        //Private key được sử dụng trong quá trình mã hóa (encoding) của JWT để tạo chữ ký số (digital signature).
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtConverter;
        //JwtAuthenticationConverter: Đối tượng JwtAuthenticationConverter được sử dụng để
        // chuyển đổi JWT thành đối tượng Authentication trong quá trình xác thực.
        //Trong quá trình xác thực, thông tin quyền (authorities) từ JWT
        // sẽ được chuyển đổi thành danh sách quyền (granted authorities)
        // trong đối tượng Authentication.
        // Điều này cho phép chúng ta sử dụng thông tin quyền từ JWT để kiểm soát quyền truy cập trong ứng dụng.
    }

}
