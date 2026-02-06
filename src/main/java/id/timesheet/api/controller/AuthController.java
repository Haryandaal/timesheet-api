package id.timesheet.api.controller;

import id.timesheet.api.dto.ApiResponse;
import id.timesheet.api.dto.response.AuthResponse;
import id.timesheet.api.dto.request.LoginRequest;
import id.timesheet.api.dto.request.RegisterRequest;
import id.timesheet.api.dto.response.RegisterResponse;
import id.timesheet.api.service.AuthService;
import id.timesheet.api.util.ResponseUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Authentication API")
@RestController
@RequestMapping(path = "api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(path = "register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "successfully registered employee", response);
    }

    @PostMapping(path = "login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "successfully logged in", response);
    }
}
