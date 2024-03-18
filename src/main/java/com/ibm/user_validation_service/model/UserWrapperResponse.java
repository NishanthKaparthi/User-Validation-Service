package com.ibm.user_validation_service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class UserWrapperResponse {
    private UserResponse userResponse;
    private String error;
}
