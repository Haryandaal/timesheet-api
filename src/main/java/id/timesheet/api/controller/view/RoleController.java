package id.timesheet.api.controller.view;

import id.timesheet.api.dto.response.RoleResponse;
import id.timesheet.api.entity.Department;
import id.timesheet.api.entity.Role;
import id.timesheet.api.repository.RoleRepository;
import id.timesheet.api.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(path = "role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleRepository roleRepository;
    private final RoleService roleService;

    @GetMapping
    public String index(Model model) {
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        return "role/index";
    }

    @GetMapping("create")
    public String create(Model model) {
        model.addAttribute("role", new Role());
        return "role/form";
    }

    @GetMapping(path = "edit/{id}")
    public String form(Model model, @PathVariable(required = false) String id) {
        RoleResponse response = roleService.getById(id);
        model.addAttribute("role", response);
        return "role/form";
    }

    @PostMapping(path = "save")
    public String save(Role role) {

        if (role.getId() != null  && !role.getId().isEmpty()) {
            Role existing = roleRepository.findById(role.getId())
                    .orElseThrow(() -> new RuntimeException("role not found"));

            existing.setName(role.getName());
            existing.setLevel(role.getLevel());

            roleRepository.save(existing); // UPDATE
        } else {
            Role newRole = Role.builder()
                    .name(role.getName())
                    .level(role.getLevel()).build();
            roleRepository.save(newRole); // CREATE
        }

        return "redirect:/role";
    }

    @GetMapping(path = "delete/{id}")
    public String delete(@PathVariable String id) {
        roleRepository.deleteById(id);
        return "redirect:/role";
    }
}
