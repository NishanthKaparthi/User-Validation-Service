package com.ibm.user_validation_service.controller;

import com.ibm.user_validation_service.dto.UserValidateDto;
import com.ibm.user_validation_service.model.UserResponse;
import com.ibm.user_validation_service.model.UserWrapperResponse;
import com.ibm.user_validation_service.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.ibm.user_validation_service.util.ValidationConstants.DATA_NOT_FOUND;
import static com.ibm.user_validation_service.util.ValidationConstants.INVALID_PASSWORD;

@ExtendWith(MockitoExtension.class)
public class UserValidationControllerTest {
    @InjectMocks
    UserValidationController userValidationController;
    @Mock
    UserService userService;

    /**
     * Test case to verify the behavior when a valid input is provided.
     * Expected outcome: OK (200) status code should be returned
     */
    @Test
    public void testValidate() {
        UserValidateDto userValidateDto = testValidateDto();
        Mockito.when(userService.validateUser(userValidateDto)).thenReturn(new UserWrapperResponse(new UserResponse(), null));

        ResponseEntity result = userValidationController.validate(userValidateDto);

        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    /**
     * Test case to verify the behavior when any condition fails.
     * Expected outcome: INTERNAL_SERVER_ERROR (500) status code should be returned
     */
    @Test
    public void testValidateForError() {
        UserValidateDto userValidateDto = testValidateDto();
        Mockito.when(userService.validateUser(userValidateDto)).thenReturn(new UserWrapperResponse(null, "Error test"));

        ResponseEntity result = userValidationController.validate(userValidateDto);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    /**
     * Test case to verify the behavior when any condition fails.
     * Expected outcome: NOT_FOUND (404) status code should be returned
     */
    @Test
    public void testValidateForNoData() {
        UserValidateDto userValidateDto = testValidateDto();
        Mockito.when(userService.validateUser(userValidateDto)).thenReturn(new UserWrapperResponse(null, DATA_NOT_FOUND));

        ResponseEntity result = userValidationController.validate(userValidateDto);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    /**
     * Test case to verify the behavior when any condition fails.
     * Expected outcome: BAD_REQUEST (400) status code should be returned
     */
    @Test
    public void testValidateForBadRequest() {
        UserValidateDto userValidateDto = testValidateDto();
        Mockito.when(userService.validateUser(userValidateDto)).thenReturn(new UserWrapperResponse(null, INVALID_PASSWORD));

        ResponseEntity result = userValidationController.validate(userValidateDto);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    private UserValidateDto testValidateDto() {
        UserValidateDto userValidateDto = new UserValidateDto();
        userValidateDto.setUsername("TestUser");
        userValidateDto.setPassword("Password123#");
        userValidateDto.setIpAddress("192.168.1.1");
        return userValidateDto;
    }
}
