package id.timesheet.api.service;

import id.timesheet.api.entity.UserAccount;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserAccount getOne(String id);
    UserAccount getOneByEmail(String email);
}
