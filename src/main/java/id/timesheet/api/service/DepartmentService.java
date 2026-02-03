package id.timesheet.api.service;

import id.timesheet.api.dto.request.DeparmentRequest;
import id.timesheet.api.dto.response.DepartmentResponse;
import id.timesheet.api.dto.request.SearchRequest;
import org.springframework.data.domain.Page;

public interface DepartmentService {

    DepartmentResponse create(DeparmentRequest request);
    DepartmentResponse getById(String id);
    Page<DepartmentResponse> getAll(SearchRequest request);
    DepartmentResponse updateById(String id, DeparmentRequest request);
    void deleteById(String id);
}
