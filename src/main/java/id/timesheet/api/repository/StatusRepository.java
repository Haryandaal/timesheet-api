package id.timesheet.api.repository;

import id.timesheet.api.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, String>, JpaSpecificationExecutor<Status> {
    boolean existsByName(String name);

    Optional<Status> findByName(String name);
}
