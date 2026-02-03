package id.timesheet.api.service;

import id.timesheet.api.dto.request.RoleRequest;
import id.timesheet.api.dto.request.SearchRequest;
import id.timesheet.api.dto.response.RoleResponse;
import org.springframework.data.domain.Page;

public interface RoleService {

    RoleResponse create(RoleRequest request);
    RoleResponse getById(String id);
    Page<RoleResponse> getAll(SearchRequest request);
    RoleResponse updateById(String id, RoleRequest request);
    void deleteById(String id);
}
