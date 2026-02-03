package id.timesheet.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DeparmentRequest {

    @NotBlank(message = "department name is required")
    private String name;
}
