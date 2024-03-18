package com.ibm.user_validation_service.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
public class ApiResponse {
    public String status;
    public String country;
    public String city;
}
