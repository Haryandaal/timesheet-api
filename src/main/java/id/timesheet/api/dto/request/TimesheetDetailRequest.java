package id.timesheet.api.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TimesheetDetailRequest {

    private String workDate;
    private String locationType;
    private Double hoursSpent;
    private String taskDescription;
}
