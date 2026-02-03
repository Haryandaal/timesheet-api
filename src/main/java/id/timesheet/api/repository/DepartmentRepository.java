package id.timesheet.api.repository;

import id.timesheet.api.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DepartmentRepository extends JpaRepository<Department, String>, JpaSpecificationExecutor<Department> {
    boolean existsByName(String name);
}
