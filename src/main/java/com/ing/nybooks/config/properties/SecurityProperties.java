package com.ing.nybooks.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration properties for security settings.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    /**
     * List of user credentials and roles.
     */
    private List<UserDetails> credentials;

    /**
     * Inner class to represent user details including username, password, and roles.
     */
    @Getter
    @Setter
    public static class UserDetails {

        /**
         * Username for authentication.
         */
        private String username;

        /**
         * Password associated with the username.
         */
        private String password;

        /**
         * Comma-separated roles assigned to the user.
         */
        private String roles;
    }
}
