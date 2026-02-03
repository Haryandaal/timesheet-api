package id.timesheet.api.repository;

import id.timesheet.api.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserAccount, String>, JpaSpecificationExecutor<UserAccount> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<UserAccount> findByEmail(String email);

    boolean existsByEmployee_Id(String employeeId);
}
