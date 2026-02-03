package id.timesheet.api.dto;

import id.timesheet.api.dto.response.PagingResponse;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ApiResponse<T> {
    private Integer status;
    private String message;
    private T data;
    private PagingResponse paging;
}
