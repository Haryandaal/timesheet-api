package id.timesheet.api.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeFormRequest {

    private String employeeCode;
    private String fullName;
    private String phoneNumber;
    private String position;
    private String departmentId;
    private String managerId;

    // user account
    private String username;
    private String email;
    private String password;
    private String roleId;
}
