package id.timesheet.api.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthResponse {

    private String userId;
    private String username;
    private String email;
    private String roleName;

    private String employeeId;
    private String employeeName;
}

