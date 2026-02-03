package id.timesheet.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TimesheetRequestRequest {

    @NotBlank(message = "employeeId is required")
    private String employeeId;

    @NotEmpty(message = "timesheet detail cannot be empty")
    private List<TimesheetDetailRequest> timesheetDetails;
}
