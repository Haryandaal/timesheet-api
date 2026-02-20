package id.timesheet.api.controller.api;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import id.timesheet.api.dto.request.SearchRequest;
import id.timesheet.api.dto.request.StatusRequest;
import id.timesheet.api.dto.response.StatusResponse;
import id.timesheet.api.dto.ApiResponse;
import id.timesheet.api.service.StatusService;
import id.timesheet.api.util.ResponseUtil;
import lombok.RequiredArgsConstructor;

@Tag(name = "Status", description = "Status API")
@RestController
@RequestMapping(path = "/api/v1/status")
@RequiredArgsConstructor
public class StatusApiController {

    private final StatusService statusService;

    @Operation(
            summary = "Create new status"
    )
    @PostMapping
    public ResponseEntity<ApiResponse<StatusResponse>> create(@RequestBody StatusRequest request) {
        StatusResponse statusResponse = statusService.create(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "status created successfully", statusResponse);
    }

    @Operation(
            summary = "Fetch status by id"
    )
    @GetMapping(path = "{id}")
    public ResponseEntity<ApiResponse<StatusResponse>> getById(@PathVariable String id) {
        StatusResponse response = statusService.getById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "success fetched status", response);
    }

    @Operation(
            summary = "Fetch statuses"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<StatusResponse>>> getAll(
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "q", required = false) String query) {

        try {
            SearchRequest pageAndSortRequest = SearchRequest.builder()
                    .page(page)
                    .size(size)
                    .sortBy(sortBy)
                    .query(query)
                    .build();

            Page<StatusResponse> responses = statusService.getAll(pageAndSortRequest);
            return ResponseUtil.buildResponsePage(HttpStatus.OK, "success fetched statuses", responses);
        } catch (Exception e) {
            return ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @Operation(
            summary = "Update existing status by id"
    )
    @PutMapping(path = "{id}")
    public ResponseEntity<ApiResponse<StatusResponse>> updateById(@PathVariable String id, StatusRequest request) {
        StatusResponse response = statusService.updateById(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "success updated status", response);
    }

    @Operation(
            summary = "Delete status by id (soft delete)"
    )
    @DeleteMapping(path = "{id}")
    public ResponseEntity<ApiResponse<String>> deleteById(@PathVariable String id) {
        statusService.deleteById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "success deleted status", null);
    }

}
