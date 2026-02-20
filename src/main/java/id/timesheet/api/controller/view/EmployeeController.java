package id.timesheet.api.controller.view;

import id.timesheet.api.entity.Employee;
import id.timesheet.api.repository.DepartmentRepository;
import id.timesheet.api.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;


    @GetMapping
    public String index(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "employee/index";
    }

    @GetMapping(path = "layout")
    public String layout(Model model) {
        return "layout/main";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("managers", employeeRepository.findAll());
        return "employee/form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        model.addAttribute("employee", employee);
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("managers", employeeRepository.findAll());

        return "employee/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Employee employee) {

        if (employee.getId() != null  && !employee.getId().isEmpty()) {
            Employee existing = employeeRepository.findById(employee.getId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            existing.setCode(employee.getCode());
            existing.setFullName(employee.getFullName());
            existing.setPhoneNumber(employee.getPhoneNumber());
            existing.setPosition(employee.getPosition());
            existing.setDepartment(employee.getDepartment());
            existing.setManager(employee.getManager());

            employeeRepository.save(existing);
        } else {
            Employee newEmployee = Employee.builder()
                    .code(employee.getCode())
                    .fullName(employee.getFullName())
                    .phoneNumber(employee.getPhoneNumber())
                    .position(employee.getPosition())
                    .department(employee.getDepartment())
                    .manager(employee.getManager())
                    .build();
            employeeRepository.save(newEmployee);
        }

        return "redirect:/employee";
    }

    @PostMapping(path = "/delete/{id}")
    public String delete(@PathVariable String id) {
        employeeRepository.deleteById(id);
        return "redirect:/employee";
    }
}
