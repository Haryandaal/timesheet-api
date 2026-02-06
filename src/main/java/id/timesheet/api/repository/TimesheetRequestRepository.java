package id.timesheet.api.repository;

import id.timesheet.api.entity.TimesheetRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TimesheetRequestRepository extends JpaRepository<TimesheetRequest, String>, JpaSpecificationExecutor<TimesheetRequest> {

    @Query("SELECT tr FROM TimesheetRequest tr WHERE tr.employee.id = :employeeId")
    Page<TimesheetRequest> findAllByEmployeeId(
            @Param("employeeId") String employeeId,
            Specification<TimesheetRequest> spec,
            Pageable pageable
    );
}
