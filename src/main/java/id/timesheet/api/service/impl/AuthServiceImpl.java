package id.timesheet.api.service.impl;

import id.timesheet.api.dto.response.AuthResponse;
import id.timesheet.api.dto.request.LoginRequest;
import id.timesheet.api.dto.request.RegisterRequest;
import id.timesheet.api.entity.Department;
import id.timesheet.api.entity.Employee;
import id.timesheet.api.entity.Role;
import id.timesheet.api.entity.UserAccount;
import id.timesheet.api.repository.DepartmentRepository;
import id.timesheet.api.repository.EmployeeRepository;
import id.timesheet.api.repository.RoleRepository;
import id.timesheet.api.repository.UserRepository;
import id.timesheet.api.service.AuthService;
import id.timesheet.api.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userAccountRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final ValidationUtil validationUtil;


    @Override
    public AuthResponse register(RegisterRequest request) {
        validationUtil.validate(request);

        // Validate unique user
        if (userAccountRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already used");
        }

        if (userAccountRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already used");
        }

        // Get department
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department not found"));

        // Get manager (optional)
        Employee manager = null;
        if (request.getManagerId() != null) {
            manager = employeeRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Manager not found"));
        }

        // Create employee
        Employee employee = Employee.builder()
                .code(request.getEmployeeCode())
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .position(request.getPosition())
                .department(department)
                .manager(manager)
                .build();

        employeeRepository.save(employee);

        // Get role
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found"));

        // Create user account
        UserAccount user = UserAccount.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(hashPassword(request.getPassword()))
                .role(role)
                .employee(employee)
                .build();

        userAccountRepository.save(user);

        return toAuthResponse(user);
    }

    @Transactional(readOnly = true)
    @Override
    public AuthResponse login(LoginRequest request) {
        UserAccount user = userAccountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

        if (!matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        return toAuthResponse(user);
    }

    private String hashPassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    private boolean matches(String raw, String hashed) {
        return BCrypt.checkpw(raw, hashed);
    }

    private AuthResponse toAuthResponse(UserAccount user) {
        return AuthResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roleName(user.getRole().getName())
                .employeeId(user.getEmployee().getId())
                .employeeName(user.getEmployee().getFullName())
                .build();
    }
}
