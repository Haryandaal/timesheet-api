package id.timesheet.api.service;

import id.timesheet.api.dto.request.RejectTimesheetRequest;
import id.timesheet.api.dto.request.SearchTimesheetRequest;
import id.timesheet.api.dto.request.TimesheetRequestRequest;
import id.timesheet.api.dto.response.TimesheetDetailResponse;
import id.timesheet.api.dto.response.TimesheetRequestResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TimesheetRequestService {

    TimesheetRequestResponse create(TimesheetRequestRequest request);
    TimesheetRequestResponse getById(String id);
    List<TimesheetDetailResponse> getTimesheetDetails(String timesheetRequestId);
    Page<TimesheetRequestResponse> getTimesheetRequests(SearchTimesheetRequest request);
    void approve(String timesheetRequestId, String approverId);
    void reject(String timesheetRequestId, RejectTimesheetRequest rejectTimesheetRequest);

}
