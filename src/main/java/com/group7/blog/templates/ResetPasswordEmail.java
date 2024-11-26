package com.group7.blog.templates;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordEmail {
    String recipient;
    String subject;
    String template;
    String username;
    String nameTag;

    public String getTemplate() {
        return """
                Hi ${name}!,
                
                You recently requested to reset the password for your ${nameTag}! account. Click the button below to proceed.
                
                [button]
                
                If you did not request a password reset, please ignore this email or reply to let us know. This password reset link is only valid for the next 30 minutes.
                
                Thanks, the [customer portal] team
                """;
    }
}
