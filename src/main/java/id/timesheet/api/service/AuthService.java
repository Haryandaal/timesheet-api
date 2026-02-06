package id.timesheet.api.service;

import id.timesheet.api.dto.response.AuthResponse;
import id.timesheet.api.dto.request.LoginRequest;
import id.timesheet.api.dto.request.RegisterRequest;
import id.timesheet.api.dto.response.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
