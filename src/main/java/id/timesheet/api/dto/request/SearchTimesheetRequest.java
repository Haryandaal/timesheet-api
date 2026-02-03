package id.timesheet.api.dto.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class SearchTimesheetRequest extends SearchRequest {

    private String status;
}
