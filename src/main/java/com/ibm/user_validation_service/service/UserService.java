package com.ibm.user_validation_service.service;

import com.ibm.user_validation_service.config.ApiConfigProperties;
import com.ibm.user_validation_service.dto.UserValidateDto;
import com.ibm.user_validation_service.exception.DataNotFoundException;
import com.ibm.user_validation_service.model.ApiResponse;
import com.ibm.user_validation_service.model.UserResponse;
import com.ibm.user_validation_service.model.UserWrapperResponse;
import com.ibm.user_validation_service.util.PasswordValidation;
import com.ibm.user_validation_service.util.ValidationConstants;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;
import java.util.UUID;

/**
 * UserService class validates the user input against several conditions,
 * Like blank inputs, password eligibility, country restriction.
 * If all the conditions are passed, then the user will be responded with a Welcome message along with their City name.
 * Otherwise, respective error message will be displayed.
 */

@Service
@Slf4j
@NoArgsConstructor
public class UserService {
    @Autowired
    private ApiConfigProperties apiConfigProperties;

    @Autowired
    private RestTemplate restTemplate;

    public UserWrapperResponse validateUser(UserValidateDto userValidateDto) {
        UserWrapperResponse userWrapperResponse;
        try {
            if (areDetailsPresent(userValidateDto)) {
                if (PasswordValidation.validatePassword(userValidateDto.getPassword())) {
                    ApiResponse apiResponse = makeApiCall(userValidateDto);
                    Objects.requireNonNull(apiResponse, ValidationConstants.NULL_API_RESPONSE);
                    log.info("API Response: {}", apiResponse);

                    if (apiResponse.getCountry() != null && apiResponse.getCountry().equalsIgnoreCase(ValidationConstants.COUNTRY)) {
                        userWrapperResponse = new UserWrapperResponse(getUserResponse(userValidateDto, apiResponse), null);
                    } else {
                        //setting the error message, as the user is not from Canada
                        userWrapperResponse = new UserWrapperResponse(null, ValidationConstants.NOT_ELIGIBLE_TO_REGISTER);
                    }

                } else {
                    //setting the error message for invalid password
                    userWrapperResponse = new UserWrapperResponse(null, ValidationConstants.INVALID_PASSWORD);
                }

            } else {
                //setting the error message for empty params
                userWrapperResponse = new UserWrapperResponse(null, ValidationConstants.BLANK_PARAMS);
            }

        } catch (DataNotFoundException e) {
            log.error(e.getMessage());
            userWrapperResponse = new UserWrapperResponse(null, e.getMessage());
        } catch (Exception e) {
            log.error(ValidationConstants.ERROR_TEXT, e.getMessage());
            userWrapperResponse = new UserWrapperResponse(null, "Encountered an exception while processing: " + e.getMessage());
        }
        return userWrapperResponse;
    }

    private UserResponse getUserResponse(UserValidateDto userValidateDto, ApiResponse apiResponse) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUuid(UUID.randomUUID());
        userResponse.setMessage("Welcome " + userValidateDto.getUsername() + "! You are located in " + apiResponse.getCity());
        return userResponse;
    }

    ApiResponse makeApiCall(UserValidateDto userValidateDto) {
        String url = UriComponentsBuilder.fromHttpUrl(apiConfigProperties.getIpApiUrl())
                .path("/json/" + userValidateDto.getIpAddress().trim())
                .queryParam("fields", "status,country,city")
                .toUriString();
        log.info(ValidationConstants.MAKE_API_CALL, url);
        try {
            ResponseEntity<ApiResponse> response = restTemplate.getForEntity(url, ApiResponse.class);
            if (response != null && response.getBody().getStatus().equalsIgnoreCase("success")) {
                return response.getBody();
            } else {
                throw new DataNotFoundException(ValidationConstants.DATA_NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Api call failed due to: {}", e.getMessage());
            throw e;
        }
    }

    private boolean areDetailsPresent(UserValidateDto userValidateDto) {
        return !isBlank(userValidateDto.getUsername()) && !isBlank(userValidateDto.getPassword()) &&
                !isBlank(userValidateDto.getIpAddress());
    }

    private boolean isBlank(String input) {
        return input == null || input.trim().isEmpty();
    }
}
