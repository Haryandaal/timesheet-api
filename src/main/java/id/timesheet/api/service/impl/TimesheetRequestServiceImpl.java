package id.timesheet.api.service.impl;

import id.timesheet.api.dto.request.RejectTimesheetRequest;
import id.timesheet.api.dto.request.SearchTimesheetRequest;
import id.timesheet.api.dto.request.TimesheetRequestRequest;
import id.timesheet.api.dto.response.TimesheetDetailResponse;
import id.timesheet.api.dto.response.TimesheetRequestResponse;
import id.timesheet.api.entity.*;
import id.timesheet.api.repository.EmployeeRepository;
import id.timesheet.api.repository.StatusRepository;
import id.timesheet.api.repository.TimesheetDetailRepository;
import id.timesheet.api.repository.TimesheetRequestRepository;
import id.timesheet.api.service.TimesheetRequestService;
import id.timesheet.api.specification.TimesheetRequestSpecification;
import id.timesheet.api.util.SortUtil;
import id.timesheet.api.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimesheetRequestServiceImpl implements TimesheetRequestService {
    private final TimesheetRequestRepository timesheetRequestRepository;
    private final TimesheetDetailRepository timesheetDetailRepository;
    private final EmployeeRepository employeeRepository;
    private final StatusRepository statusRepository;
    private final ValidationUtil validationUtil;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public TimesheetRequestResponse create(TimesheetRequestRequest request) {
        validationUtil.validate(request);

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "employee is not found"));

        Status status = statusRepository.findByName("pending".toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "pending status is not found"));

        TimesheetRequest timesheetRequest = TimesheetRequest.builder()
                .employee(employee)
                .requestDate(LocalDateTime.now())
                .status(status)
                .approvedBy(null)
                .approvedAt(null)
                .build();

        timesheetRequestRepository.save(timesheetRequest);

        List<TimesheetDetail> details = request.getTimesheetDetails().stream()
                .map(d -> TimesheetDetail.builder()
                        .employee(employee)
                        .timesheetRequest(timesheetRequest)
                        .workDate(LocalDateTime.parse(d.getWorkDate()))
                        .locationType(d.getLocationType())
                        .hoursSpent(BigDecimal.valueOf(d.getHoursSpent()))
                        .taskDescription(d.getTaskDescription())
                        .build())
                .collect(Collectors.toList());

        timesheetDetailRepository.saveAll(details);

        timesheetRequest.setTimesheetDetails(details);

        return toResponse(timesheetRequest);
    }

    @Transactional(readOnly = true)
    @Override
    public TimesheetRequestResponse getById(String id) {
        TimesheetRequest timesheetRequest = timesheetRequestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "timesheet request is not found"));

        return toResponse(timesheetRequest);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TimesheetDetailResponse> getTimesheetDetails(String timesheetRequestId) {
        TimesheetRequest timesheetRequest = timesheetRequestRepository.findById(timesheetRequestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "timesheet request is not found"));

        return timesheetRequest.getTimesheetDetails().stream()
                .map(this::toDetailResponse)
                .toList();

    }

    @Transactional(readOnly = true)
    @Override
    public Page<TimesheetRequestResponse> getTimesheetRequests(SearchTimesheetRequest request) {
        Sort sort = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Specification<TimesheetRequest> specification = TimesheetRequestSpecification.getSpecification(request);
        Page<TimesheetRequest> timesheetRequests = timesheetRequestRepository.findAll(specification, pageable);
        return timesheetRequests.map(this::toResponse);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void approve(String timesheetRequestId, String approverId) {
        TimesheetRequest timesheetRequest = timesheetRequestRepository.findById(timesheetRequestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "timesheet request is not found"));

        Status appprovedStatus = statusRepository.findByName("Approved".toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "approved status is not found"));

        Employee approver = employeeRepository.findById(approverId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "approver is not found"));

        if ("APPROVED".equalsIgnoreCase(timesheetRequest.getStatus().getName().toLowerCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Timesheet request already approved");
        }

        if (timesheetRequest.getEmployee().getId().equals(approverId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Employee cannot approve their own timesheet");
        }

        timesheetRequest.setStatus(appprovedStatus);
        timesheetRequest.setApprovedBy(approver);
        timesheetRequest.setApprovedAt(LocalDateTime.now());

        timesheetRequestRepository.save(timesheetRequest);
    }

    @Override
    public void reject(String timesheetRequestId, RejectTimesheetRequest rejectTimesheetRequest) {
        TimesheetRequest timesheetRequest = timesheetRequestRepository.findById(timesheetRequestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "timesheet request is not found"));

        Status rejectedStatus = statusRepository.findByName("Rejected".toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "rejected status is not found"));

        Employee approver = employeeRepository.findById(rejectTimesheetRequest.getApproverId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "approver is not found"));

        if (!"PENDING".equalsIgnoreCase(timesheetRequest.getStatus().getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only pending timesheet can be rejected");
        }

        if (timesheetRequest.getEmployee().getId().equals(rejectTimesheetRequest.getApproverId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Employee cannot approve their own timesheet");
        }

        timesheetRequest.setStatus(rejectedStatus);
        timesheetRequest.setApprovedBy(approver);
        timesheetRequest.setApprovedAt(LocalDateTime.now());
        timesheetRequest.setRejectionReason(rejectTimesheetRequest.getReason());

        timesheetRequestRepository.save(timesheetRequest);
    }

    private TimesheetRequestResponse toResponse(TimesheetRequest timesheetRequest) {

        return TimesheetRequestResponse.builder()
                .id(timesheetRequest.getId())
                .employeeId(timesheetRequest.getEmployee().getId())
                .employeeName(timesheetRequest.getEmployee().getFullName())
                .requestDate(timesheetRequest.getRequestDate().toString())
                .statusName(timesheetRequest.getStatus().getName().toUpperCase())
                .approvedBy(
                        timesheetRequest.getApprovedBy() != null
                                ? timesheetRequest.getApprovedBy().getFullName()
                                : null
                )
                .approvedAt(
                        timesheetRequest.getApprovedAt() != null
                                ? timesheetRequest.getApprovedAt().toString()
                                : null
                )
                .timesheetDetails(
                        timesheetRequest.getTimesheetDetails().stream()
                                .map(this::toDetailResponse)
                                .toList()
                )
                .build();
    }

    private TimesheetDetailResponse toDetailResponse(TimesheetDetail detail) {
        return TimesheetDetailResponse.builder()
                .id(detail.getId())
                .workDate(detail.getWorkDate().toString())
                .locationType(detail.getLocationType())
                .hoursSpent(detail.getHoursSpent().toPlainString())
                .taskDescription(detail.getTaskDescription())
                .build();
    }


}
