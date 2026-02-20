package id.timesheet.api.controller.api;

import id.timesheet.api.dto.response.EmployeeResponse;
import id.timesheet.api.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/employee")
@RequiredArgsConstructor
public class EmployeeApiController {

    private final EmployeeRepository employeeRepository;

    @GetMapping
    public Map<String, Object> getAllEmployee(@RequestParam Map<String, String> requestParams) {
        Integer draw = Integer.parseInt(requestParams.get("draw"));
        Integer startPage = Integer.parseInt(requestParams.get("start")); // 1
        Integer length = Integer.parseInt(requestParams.get("length")); // total row'

        String searchValue = requestParams.get("search[value]");

        Page<EmployeeResponse> employees = employeeRepository.getEmployeePage(searchValue, PageRequest.of(startPage / length, length));

        Map<String, Object> response = new HashMap<>();
        response.put("draw", draw);
        response.put("recordsTotal", employees.getTotalElements());
        response.put("recordsFiltered", employees.getTotalElements());
        response.put("data", employees.getContent());

        return response;
    }
}
