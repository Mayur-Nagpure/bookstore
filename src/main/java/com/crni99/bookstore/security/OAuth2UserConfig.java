package com.crni99.bookstore.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.user.*;
import java.util.*;

@Configuration
public class OAuth2UserConfig {

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        return userRequest -> {
            OAuth2User user = new DefaultOAuth2UserService().loadUser(userRequest);
            Map<String, Object> attributes = user.getAttributes();
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

            // By default, assign as USER
            authorities.add(new SimpleGrantedAuthority("USER"));

            String principalKey = attributes.get("email") != null ? "email" : "login";
            return new DefaultOAuth2User(authorities, attributes, principalKey);

        };
    }
}

