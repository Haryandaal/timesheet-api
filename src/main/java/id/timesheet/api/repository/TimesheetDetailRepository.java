package id.timesheet.api.repository;

import id.timesheet.api.entity.TimesheetDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TimesheetDetailRepository extends JpaRepository<TimesheetDetail, String>, JpaSpecificationExecutor<TimesheetDetail> {
}
