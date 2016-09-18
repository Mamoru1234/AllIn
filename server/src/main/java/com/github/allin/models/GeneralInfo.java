package com.github.allin.models;

import lombok.Builder;
import lombok.Data;

/**
 */
@Builder
@Data
public class GeneralInfo {
    private String firstName;
    private String surName;
    private Gender gender;
    private String country;
    public enum Gender {
        MALE, FEMALE, UNDEF
    }
}
