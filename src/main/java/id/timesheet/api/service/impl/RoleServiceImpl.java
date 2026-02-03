package id.timesheet.api.service.impl;

import id.timesheet.api.dto.request.RoleRequest;
import id.timesheet.api.dto.response.RoleResponse;
import id.timesheet.api.dto.request.SearchRequest;
import id.timesheet.api.entity.Role;
import id.timesheet.api.repository.RoleRepository;
import id.timesheet.api.service.RoleService;
import id.timesheet.api.specification.RoleSpecification;
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
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RoleResponse create(RoleRequest request) {
        validationUtil.validate(request);

        if (roleRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "role name is already exist");
        }

        Role role = Role.builder()
                .name(request.getName())
                .level(request.getLevel())
                .build();

        Role saved = roleRepository.saveAndFlush(role);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public RoleResponse getById(String id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "status is not found"));
        return toResponse(role);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RoleResponse> getAll(SearchRequest request) {
        Sort sort = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Specification<Role> specification = RoleSpecification.getSpecification(request);
        Page<Role> page = roleRepository.findAll(specification, pageable);
        return page.map(this::toResponse);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RoleResponse updateById(String id, RoleRequest request) {
        validationUtil.validate(request);

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "status is not found"));

        role.setName(request.getName());
        role.setUpdatedAt(LocalDateTime.now());

        Role saved = roleRepository.save(role);
        return toResponse(saved);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "status is not found"));

        role.setDeletedAt(LocalDateTime.now());
        roleRepository.save(role);
    }

    private RoleResponse toResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .level(role.getLevel())
                .build();
    }
}
