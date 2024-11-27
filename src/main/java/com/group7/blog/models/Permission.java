package com.group7.blog.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class Permission {
    @Id
    @GeneratedValue
    Integer id;
    String codeName;
}
