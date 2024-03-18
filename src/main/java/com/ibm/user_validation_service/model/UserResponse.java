package com.ibm.user_validation_service.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Setter
@ToString
@Getter
public class UserResponse {
    private UUID uuid;
    private String message;
}
