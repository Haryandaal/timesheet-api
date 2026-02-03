package id.timesheet.api.service;

import id.timesheet.api.dto.request.EmployeeRequest;
import id.timesheet.api.dto.response.EmployeeResponse;
import id.timesheet.api.dto.request.SearchRequest;
import org.springframework.data.domain.Page;

public interface EmployeeService {

    EmployeeResponse create(EmployeeRequest request);
    EmployeeResponse getById(String id);
    Page<EmployeeResponse> getAll(SearchRequest request);
    EmployeeResponse updateById(String id, EmployeeRequest request);
    void deleteById(String id);
}
