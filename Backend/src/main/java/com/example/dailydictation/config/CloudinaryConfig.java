package com.example.dailydictation.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dc3o5kumf",
                "api_key", "941627642784641",
                "api_secret", "f5hyo9oDf4iM03fehOMrld7ASgg",
                "secure", true
        ));
    }


}
