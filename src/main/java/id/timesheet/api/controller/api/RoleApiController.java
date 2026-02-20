package id.timesheet.api.controller.api;

import id.timesheet.api.dto.*;
import id.timesheet.api.dto.request.RoleRequest;
import id.timesheet.api.dto.request.SearchRequest;
import id.timesheet.api.dto.response.RoleResponse;
import id.timesheet.api.service.RoleService;
import id.timesheet.api.util.ResponseUtil;
import id.timesheet.api.util.UtilResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Role", description = "Role API")
@RestController
@RequestMapping(path = "/api/v1/role")
@RequiredArgsConstructor
public class RoleApiController {

    private final RoleService roleService;

    @Operation(
            summary = "Create new role"
    )
    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> create(@RequestBody RoleRequest request) {
        RoleResponse response = roleService.create(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "role is created successfully", response);
    }

    @Operation(
            summary = "Fetch role by id"
    )
    @GetMapping(path = "{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> getById(@PathVariable String id) {
        RoleResponse response = roleService.getById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "success fetched role", response);
    }

    @GetMapping(path = "{id}/test")
    public ResponseEntity<Object> getByIdd(@PathVariable String id) {
        RoleResponse response = roleService.getById(id);
        return UtilResponse.generate(HttpStatus.OK, "success fetched role", response);
    }

    @Operation(
            summary = "Fetch roles"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAll(
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

            Page<RoleResponse> responses = roleService.getAll(pageAndSortRequest);
            return ResponseUtil.buildResponsePage(HttpStatus.OK, "success fetched roles", responses);
        } catch (Exception e) {
            return ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @Operation(
            summary = "Update existing role by id"
    )
    @PutMapping(path = "{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> updateById(@PathVariable String id, RoleRequest request) {
        RoleResponse response = roleService.updateById(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "success updated role", response);
    }

    @Operation(
            summary = "Delete role by id (soft delete)"
    )
    @DeleteMapping(path = "{id}")
    public ResponseEntity<ApiResponse<String>> deleteById(@PathVariable String id) {
        roleService.deleteById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "success deleted role", null);
    }
}
