package id.timesheet.api.repository;

import id.timesheet.api.entity.TimesheetRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TimesheetRequestRepository extends JpaRepository<TimesheetRequest, String>, JpaSpecificationExecutor<TimesheetRequest> {
}
