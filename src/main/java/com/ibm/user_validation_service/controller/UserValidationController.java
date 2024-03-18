package com.ibm.user_validation_service.controller;

import com.ibm.user_validation_service.dto.UserValidateDto;
import com.ibm.user_validation_service.model.UserResponse;
import com.ibm.user_validation_service.model.UserWrapperResponse;
import com.ibm.user_validation_service.service.UserService;
import com.ibm.user_validation_service.util.ValidationConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserValidationController has only one POST method, which accepts the JSON body and
 * returns ResponseEntity along with status code.
 * Input JSON example
 * {
 * "username": "John",
 * "password": "Sample$_.#@97",
 * "ipAddress": "192.41.148.220"
 * }
 */

@RestController
@Slf4j
public class UserValidationController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Validate User Registration")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved data",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)))
    @ApiResponse(responseCode = "404", description = "Data Not Found",
            content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class)))
    @PostMapping("/validate")
    public ResponseEntity validate(@RequestBody UserValidateDto userValidateDto) {
        log.info(ValidationConstants.INCOMING_REQUEST, userValidateDto);
        UserWrapperResponse userWrapperResponse = userService.validateUser(userValidateDto);
        log.info(ValidationConstants.OUTGOING_RESPONSE, userWrapperResponse);
        log.info(ValidationConstants.SEPARATOR);

        if (userWrapperResponse.getUserResponse() != null)
            return new ResponseEntity(userWrapperResponse.getUserResponse(), HttpStatus.OK);

        else if (userWrapperResponse.getError().equals(ValidationConstants.DATA_NOT_FOUND))
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(userWrapperResponse.getError());

        else if (ValidationConstants.BAD_REQUEST_LOG.contains(userWrapperResponse.getError()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(userWrapperResponse.getError());

        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(userWrapperResponse.getError());
    }
}
