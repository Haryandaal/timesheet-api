package id.timesheet.api.service;

import id.timesheet.api.entity.UserAccount;
import jakarta.servlet.http.HttpServletRequest;

public interface JwtService {

    String generateAccessToken(UserAccount userAccount, Long accessTokenExpiry);
    String getUserId(String token);
    String extractTokenFromRequest(HttpServletRequest request);
    boolean validateToken(String token);
}
