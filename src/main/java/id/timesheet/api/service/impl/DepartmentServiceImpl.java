package id.timesheet.api.service.impl;

import id.timesheet.api.dto.request.DeparmentRequest;
import id.timesheet.api.dto.response.DepartmentResponse;
import id.timesheet.api.dto.request.SearchRequest;
import id.timesheet.api.entity.Department;
import id.timesheet.api.repository.DepartmentRepository;
import id.timesheet.api.service.DepartmentService;
import id.timesheet.api.specification.DepartmentSpecification;
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

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public DepartmentResponse create(DeparmentRequest request) {
        validationUtil.validate(request);

        if (departmentRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "department name is already exist");
        }

        Department department = Department.builder()
                .name(request.getName())
                .build();

        Department saved = departmentRepository.saveAndFlush(department);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public DepartmentResponse getById(String id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "department is not found"));
        return toResponse(department);
    }

    @Override
    public Page<DepartmentResponse> getAll(SearchRequest request) {
        Sort sort = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Specification<Department> specification = DepartmentSpecification.getSpecification(request);
        Page<Department> departments = departmentRepository.findAll(specification, pageable);
        return departments.map(this::toResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public DepartmentResponse updateById(String id, DeparmentRequest request) {
        validationUtil.validate(request);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "department is not found"));

        department.setName(request.getName());
        department.setUpdatedAt(LocalDateTime.now());

        Department saved = departmentRepository.save(department);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public void deleteById(String id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "department is not found"));

        department.setDeletedAt(LocalDateTime.now());
        departmentRepository.save(department);
    }

    private DepartmentResponse toResponse(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .build();
    }
}
