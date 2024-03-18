package com.ibm.user_validation_service.service;

import com.ibm.user_validation_service.config.ApiConfigProperties;
import com.ibm.user_validation_service.dto.UserValidateDto;
import com.ibm.user_validation_service.model.ApiResponse;
import com.ibm.user_validation_service.model.UserResponse;
import com.ibm.user_validation_service.model.UserWrapperResponse;
import com.ibm.user_validation_service.util.ValidationConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static com.ibm.user_validation_service.util.ValidationConstants.BLANK_PARAMS;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private ApiConfigProperties config;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UserResponse userResponse;

    /**
     * Test case to verify the behavior when the Data is not found from api
     * Expected outcome: No Data Found message should be returned
     */
    @Test
    public void testValidUserForNoDataException() {
        UserValidateDto userValidateDto = new UserValidateDto();
        userValidateDto.setUsername("TestUser");
        userValidateDto.setPassword("Password123#");
        userValidateDto.setIpAddress("192.168.1.1");

        Mockito.when(config.getIpApiUrl()).thenReturn("http://example.com");
        UserWrapperResponse userWrapperResponse = userService.validateUser(userValidateDto);
        Assertions.assertEquals(userWrapperResponse.getError(), ValidationConstants.DATA_NOT_FOUND);
    }

    /**
     * Test case to verify the behavior when the valid input is provided.
     * Steps:
     * 1. Create UserValidateDto with correct values
     * 2. Create ApiResponse with appropriate values
     * 3. Assert the message field with expected message
     */
    @Test
    public void testValidUser() {
        UserValidateDto userValidateDto = new UserValidateDto();
        userValidateDto.setUsername("TestUser");
        userValidateDto.setPassword("Password123#");
        userValidateDto.setIpAddress("192.168.1.1");

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus("Success");
        apiResponse.setCountry("Canada");
        apiResponse.setCity("Halifax");
        ResponseEntity<ApiResponse> apiResponseEntity = new ResponseEntity<>(apiResponse, HttpStatus.OK);

        Mockito.when(config.getIpApiUrl()).thenReturn("http://example.com");
        Mockito.when(restTemplate.getForEntity("http://example.com/json/192.168.1.1?fields=status,country,city", ApiResponse.class)).thenReturn(apiResponseEntity);
        UserWrapperResponse userWrapperResponse = userService.validateUser(userValidateDto);
        Assertions.assertEquals("Welcome TestUser! You are located in Halifax", userWrapperResponse.getUserResponse().getMessage());
    }

    /**
     * Test case to verify the behavior when the empty input is provided.
     * Expected outcome: Blank error message should be returned
     */
    @Test
    public void testValidUserForEmptyInput() {
        UserValidateDto userValidateDto = new UserValidateDto();
        UserWrapperResponse userWrapperResponse = userService.validateUser(userValidateDto);
        Assertions.assertEquals(BLANK_PARAMS, userWrapperResponse.getError());
    }
}
