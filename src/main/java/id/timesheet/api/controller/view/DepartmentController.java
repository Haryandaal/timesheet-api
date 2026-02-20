package id.timesheet.api.controller.view;

import id.timesheet.api.dto.response.DepartmentResponse;
import id.timesheet.api.entity.Department;
import id.timesheet.api.repository.DepartmentRepository;
import id.timesheet.api.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(path = "department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;
    private final DepartmentRepository departmentRepository;

    @GetMapping
    public String index(Model model) {
        List<Department> departmentList = departmentRepository.findAll();
        model.addAttribute("departments", departmentList);
        return "department/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("department", new Department());
        return "department/form";
    }

    @GetMapping(path = "/edit/{id}")
    public String form(Model model, @PathVariable(required = false) String id) {
        DepartmentResponse response = departmentService.getById(id);
        model.addAttribute("department", response);
        return "department/form";
    }

    @PostMapping(path = "/save")
    public String save(@ModelAttribute Department department) {

        if (department.getId() != null && !department.getId().isEmpty()) {
            Department existing = departmentRepository.findById(department.getId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));

            existing.setName(department.getName());

            departmentRepository.save(existing); // UPDATE
        } else {
            Department newDepartment = Department.builder()
                    .name(department.getName())
                    .build();
            departmentRepository.save(newDepartment); // CREATE
        }

        return "redirect:/department";
    }

    @GetMapping(path = "/delete/{id}")
    public String delete(@PathVariable String id) {
        departmentRepository.deleteById(id);
        return "redirect:/department";
    }

}
