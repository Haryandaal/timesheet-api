package id.timesheet.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterRequest {

    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "email is required")
    @Email
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "role is required")
    private String roleId;

    @NotBlank
    private String employeeCode;

    @NotBlank(message = "full name is required")
    private String fullName;

    @NotBlank(message = "phone number is required")
    private String phoneNumber;

    private String position;

    @NotBlank(message = "department is required")
    private String departmentId;

    private String managerId; // optional
}
