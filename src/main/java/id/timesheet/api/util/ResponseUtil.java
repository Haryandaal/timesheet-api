package id.timesheet.api.util;

import id.timesheet.api.dto.response.PagingResponse;
import id.timesheet.api.dto.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseUtil {
    public static <T> ResponseEntity<ApiResponse<T>> buildResponse(HttpStatus httpStatus, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>(httpStatus.value(), message, data,  null);
        return ResponseEntity.status(httpStatus).body(response);
    }

    public static <T> ResponseEntity<ApiResponse<List<T>>> buildResponsePage(HttpStatus httpStatus, String message, Page<T> page) {
        PagingResponse pagingResponse = PagingResponse.builder()
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .page(page.getPageable().getPageNumber() + 1)
                .size(page.getSize())
                .build();
        ApiResponse<List<T>> response = new ApiResponse<>(
                httpStatus.value(),
                message,
                page.getContent(),
                pagingResponse
        );
        return ResponseEntity.status(httpStatus).body(response);
    }
}
