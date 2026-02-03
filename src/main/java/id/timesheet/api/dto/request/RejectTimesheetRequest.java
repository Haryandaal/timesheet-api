package id.timesheet.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RejectTimesheetRequest {

    @NotBlank(message = "approverId is required")
    private String approverId;

    @NotBlank(message = "rejection reason is required")
    private String reason;
}
