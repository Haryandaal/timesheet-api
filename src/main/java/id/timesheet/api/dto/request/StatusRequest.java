package id.timesheet.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StatusRequest {

    @NotBlank(message = "status name is required")
    private String name;
}
