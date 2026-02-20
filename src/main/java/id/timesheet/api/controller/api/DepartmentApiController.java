package id.timesheet.api.controller.api;

import id.timesheet.api.dto.ApiResponse;
import id.timesheet.api.dto.request.DeparmentRequest;
import id.timesheet.api.dto.request.SearchRequest;
import id.timesheet.api.dto.response.DepartmentResponse;
import id.timesheet.api.service.DepartmentService;
import id.timesheet.api.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Department", description = "Department API")
@RestController
@RequestMapping(path = "api/v1/department")
@RequiredArgsConstructor
public class DepartmentApiController {

    private final DepartmentService departmentService;

    @Operation(
            summary = "Create new department"
    )
    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponse>> create(@RequestBody DeparmentRequest request) {
        DepartmentResponse response = departmentService.create(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "department created successfully", response);
    }

    @Operation(
            summary = "Fetch department by id"
    )
    @GetMapping(path = "{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getById(@PathVariable String id) {
        DepartmentResponse response = departmentService.getById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "success fetched department", response);
    }

    @Operation(
            summary = "Fetch departments"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getAll(
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

            Page<DepartmentResponse> responses = departmentService.getAll(pageAndSortRequest);
            return ResponseUtil.buildResponsePage(HttpStatus.OK, "success fetched departments", responses);
        } catch (Exception e) {
            return ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @Operation(
            summary = "Update existing department by id"
    )
    @PutMapping(path = "{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> updateById(@PathVariable String id, DeparmentRequest request) {
        DepartmentResponse response = departmentService.updateById(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "success updated department", response);
    }

    @Operation(
            summary = "Delete department by id (soft delete)"
    )
    @DeleteMapping(path = "{id}")
    public ResponseEntity<ApiResponse<String>> deleteById(@PathVariable String id) {
        departmentService.deleteById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "success deleted department", null);
    }

}
