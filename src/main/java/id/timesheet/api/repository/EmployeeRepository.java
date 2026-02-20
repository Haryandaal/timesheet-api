package id.timesheet.api.repository;

import id.timesheet.api.dto.response.EmployeeResponse;
import id.timesheet.api.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

    @Query("select new id.timesheet.api.dto.response.EmployeeResponse(" +
            "e.id, e.code, e.fullName, e.phoneNumber, e.position, e.department.name, e.manager.fullName) " +
            "from Employee e join e.department d left join e.manager m where :search is null or e.id like concat('%',:search,'%') " +
            "or e.code like concat('%',:search,'%') or e.fullName like concat('%',:search,'%') or e.position like concat('%',:search,'%') " +
            "or e.phoneNumber like concat('%',:search,'%') or e.department.name like concat('%',:search,'%') or e.manager.fullName like concat('%',:search,'%') ")
    Page<EmployeeResponse> getEmployeePage(@Param("search") String search, Pageable pageable);
}
