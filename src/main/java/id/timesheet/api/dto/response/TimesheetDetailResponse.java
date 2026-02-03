package id.timesheet.api.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TimesheetDetailResponse {

    private String id;
    private String workDate;
    private String locationType;
    private String hoursSpent;
    private String taskDescription;
}
