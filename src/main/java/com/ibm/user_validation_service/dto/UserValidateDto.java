package com.ibm.user_validation_service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserValidateDto {
    private String username;
    private String password;
    private String ipAddress;
}
