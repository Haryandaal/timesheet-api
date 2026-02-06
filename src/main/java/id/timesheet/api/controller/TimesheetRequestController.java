package id.timesheet.api.controller;

import id.timesheet.api.dto.*;
import id.timesheet.api.dto.request.RejectTimesheetRequest;
import id.timesheet.api.dto.request.SearchTimesheetRequest;
import id.timesheet.api.dto.request.TimesheetRequestRequest;
import id.timesheet.api.dto.response.TimesheetDetailResponse;
import id.timesheet.api.dto.response.TimesheetRequestResponse;
import id.timesheet.api.service.TimesheetRequestService;
import id.timesheet.api.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Timesheet Request", description = "Timesheet Request API")
@RestController
@RequestMapping(path = "api/v1/timesheet-request")
@RequiredArgsConstructor
public class TimesheetRequestController {

    private final TimesheetRequestService timesheetRequestService;

    @Operation(
            summary = "Create new timesheet request with the details"
    )
    @PostMapping
    public ResponseEntity<ApiResponse<TimesheetRequestResponse>> create(@RequestBody TimesheetRequestRequest request) {
        TimesheetRequestResponse response = timesheetRequestService.create(request);
        return ResponseUtil.buildResponse(HttpStatus.CREATED, "timesheet request created", response);
    }

    @Operation(
            summary = "Fetch timesheet request by id"
    )
    @GetMapping(path = "{id}")
    public ResponseEntity<ApiResponse<TimesheetRequestResponse>> getById(@PathVariable String id) {
        TimesheetRequestResponse response = timesheetRequestService.getById(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "fetched timesheet request", response);
    }

    @Operation(
            summary = "Fetch timesheets request"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<TimesheetRequestResponse>>> getAll(
            @RequestParam(name = "page", defaultValue = "1", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(name = "status", required = false) String status
    ) {
        try {
            SearchTimesheetRequest request = SearchTimesheetRequest.builder()
                    .page(page)
                    .size(size)
                    .sortBy(sortBy)
                    .query(query)
                    .status(status)
                    .build();

            Page<TimesheetRequestResponse> responses =
                    timesheetRequestService.getTimesheetRequests(request);
            return ResponseUtil.buildResponsePage(HttpStatus.OK, "success fetched timesheet requests", responses);

        } catch (Exception e) {
            return ResponseUtil.buildResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }
    }

    @Operation(
            summary = "Fetch timesheet details by timesheet request id"
    )
    @GetMapping(path = "{id}/details")
    public ResponseEntity<ApiResponse<List<TimesheetDetailResponse>>> getTimesheetDetails(@PathVariable String id) {
        List<TimesheetDetailResponse> detailResponses = timesheetRequestService.getTimesheetDetails(id);
        return ResponseUtil.buildResponse(HttpStatus.OK, "fetched timesheet details", detailResponses);
    }

    @PreAuthorize("hasRole('Manager')")
    @Operation(
            summary = "Approve timesheet request (for manager)"
    )
    @PatchMapping(path = "{id}/approve")
    public ResponseEntity<ApiResponse<Void>> approve(@PathVariable String id, @RequestParam String approverId) {
        timesheetRequestService.approve(id, approverId);
        return ResponseUtil.buildResponse(HttpStatus.OK, "timesheet request approved", null);
    }

    @PreAuthorize("hasRole('Manager')")
    @Operation(
            summary = "Reject timesheet request with reason (for manager)"
    )
    @PatchMapping(path = "{id}/reject")
    public ResponseEntity<ApiResponse<Void>> reject(@PathVariable String id, @RequestBody RejectTimesheetRequest request) {
        timesheetRequestService.reject(id, request);
        return ResponseUtil.buildResponse(HttpStatus.OK, "timesheet request rejected with reason", null);
    }
}
