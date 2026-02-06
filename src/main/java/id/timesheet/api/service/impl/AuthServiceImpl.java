package id.timesheet.api.service.impl;

import id.timesheet.api.dto.request.LoginRequest;
import id.timesheet.api.dto.request.RegisterRequest;
import id.timesheet.api.dto.response.AuthResponse;
import id.timesheet.api.dto.response.RegisterResponse;
import id.timesheet.api.entity.Department;
import id.timesheet.api.entity.Employee;
import id.timesheet.api.entity.Role;
import id.timesheet.api.entity.UserAccount;
import id.timesheet.api.repository.DepartmentRepository;
import id.timesheet.api.repository.EmployeeRepository;
import id.timesheet.api.repository.RoleRepository;
import id.timesheet.api.repository.UserRepository;
import id.timesheet.api.service.AuthService;
import id.timesheet.api.service.JwtService;
import id.timesheet.api.service.UserService;
import id.timesheet.api.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userAccountRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ValidationUtil validationUtil;


    @Override
    public RegisterResponse register(RegisterRequest request) {
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

    @Override
    public AuthResponse login(LoginRequest request) {

        UserAccount userAccount = userService.getOneByEmail(request.getEmail());
        if (userAccount == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid email");
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAccount.getEmail(), request.getPassword()));
        if (!authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad Credentials");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtService.generateAccessToken(userAccount, 30L);
        return AuthResponse.builder()
                .id(userAccount.getId())
                .accessToken(accessToken)
                .role(userAccount.getRole().getName())
                .build();
    }

    private String hashPassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    private boolean matches(String raw, String hashed) {
        return BCrypt.checkpw(raw, hashed);
    }

    private RegisterResponse toAuthResponse(UserAccount user) {
        return RegisterResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roleName(user.getRole().getName())
                .employeeId(user.getEmployee().getId())
                .employeeName(user.getEmployee().getFullName())
                .build();
    }
}
