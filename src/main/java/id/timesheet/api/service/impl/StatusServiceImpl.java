package id.timesheet.api.service.impl;

import id.timesheet.api.dto.request.SearchRequest;
import id.timesheet.api.dto.request.StatusRequest;
import id.timesheet.api.dto.response.StatusResponse;
import id.timesheet.api.entity.Status;
import id.timesheet.api.repository.StatusRepository;
import id.timesheet.api.service.StatusService;
import id.timesheet.api.specification.StatusSpecification;
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
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StatusResponse create(StatusRequest request) {
        validationUtil.validate(request);

        if (statusRepository.existsByName(request.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "status name is already exist");
        }

        Status status = Status.builder()
                .name(request.getName())
                .build();

        Status saved = statusRepository.saveAndFlush(status);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public StatusResponse getById(String id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "status is not found"));
        return toResponse(status);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<StatusResponse> getAll(SearchRequest request) {
        Sort sort = SortUtil.parseSort(request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        Specification<Status> specification = StatusSpecification.getSpecification(request);
        Page<Status> page = statusRepository.findAll(specification, pageable);
        return page.map(this::toResponse);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public StatusResponse updateById(String id, StatusRequest request) {
        validationUtil.validate(request);

        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "status is not found"));

        status.setName(request.getName());
        status.setUpdatedAt(LocalDateTime.now());

        Status saved = statusRepository.save(status);
        return toResponse(saved);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "status is not found"));

        status.setDeletedAt(LocalDateTime.now());
        statusRepository.save(status);
    }

    private StatusResponse toResponse(Status status) {
        return StatusResponse.builder()
                .id(status.getId())
                .name(status.getName())
                .build();
    }
}
