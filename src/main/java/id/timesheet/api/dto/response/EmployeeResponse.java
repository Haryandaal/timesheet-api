package id.timesheet.api.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmployeeResponse {

    private String id;
    private String code;
    private String fullName;
    private String phoneNumber;
    private String position;
    private String  departmentName;
    private String managerName;
}
