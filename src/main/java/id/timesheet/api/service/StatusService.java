package id.timesheet.api.service;

import id.timesheet.api.dto.request.SearchRequest;
import id.timesheet.api.dto.request.StatusRequest;
import id.timesheet.api.dto.response.StatusResponse;
import org.springframework.data.domain.Page;

public interface StatusService {

    StatusResponse create(StatusRequest request);
    StatusResponse getById(String id);
    Page<StatusResponse> getAll(SearchRequest request);
    StatusResponse updateById(String id, StatusRequest request);
    void deleteById(String id);
}
