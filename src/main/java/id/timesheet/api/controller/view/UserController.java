package id.timesheet.api.controller.view;

import id.timesheet.api.entity.UserAccount;
import id.timesheet.api.repository.EmployeeRepository;
import id.timesheet.api.repository.RoleRepository;
import id.timesheet.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "user-account/index";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("user", new UserAccount());
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("employees", employeeRepository.findAll());
        return "user-account/form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        UserAccount userAccount = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserAccount not found"));

        model.addAttribute("user", userAccount);
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("employees", employeeRepository.findAll());

        return "user-account/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute UserAccount user) {

        if (user.getId() != null  && !user.getId().isEmpty()) {
            UserAccount existing = userRepository.findById(user.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            existing.setRole(user.getRole());
            existing.setEmail(user.getEmail());
            existing.setUsername(user.getUsernameField());

            userRepository.save(existing);
        } else {
            UserAccount newUsers = UserAccount.builder()
                    .employee(user.getEmployee())
                    .role(user.getRole())
                    .username(user.getUsernameField())
                    .email(user.getEmail())
                    .password(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()))
                    .build();
            userRepository.save(newUsers);
        }

        return "redirect:/user";
    }

    @GetMapping(path = "/delete/{id}")
    public String delete(@PathVariable String id) {
        userRepository.deleteById(id);
        return "redirect:/user";
    }
}
