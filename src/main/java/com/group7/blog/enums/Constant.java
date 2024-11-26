package com.group7.blog.enums;


import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

public class Constant {

    @NonFinal
    @Value("${server.domain-url}")
    public static String domain = "http://localhost:4200";

    public static final String FOLDER_NAME = "blog";

    public static final String resetPasswordUrl = domain + "/users/resetPassword?token=";
}
