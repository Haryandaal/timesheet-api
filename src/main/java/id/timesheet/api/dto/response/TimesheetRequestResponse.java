package id.timesheet.api.dto.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TimesheetRequestResponse {

    private String id;
    private String employeeId;
    private String employeeName;
    private String requestDate;
    private String statusName;
    private String approvedBy;
    private String approvedAt;
    private List<TimesheetDetailResponse> timesheetDetails;
}
