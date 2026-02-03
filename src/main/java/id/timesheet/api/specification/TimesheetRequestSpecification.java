package id.timesheet.api.specification;

import id.timesheet.api.dto.request.SearchTimesheetRequest;
import id.timesheet.api.entity.Employee;
import id.timesheet.api.entity.Status;
import id.timesheet.api.entity.TimesheetRequest;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TimesheetRequestSpecification {

    public static Specification<TimesheetRequest> getSpecification(SearchTimesheetRequest request) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // join
            Join<TimesheetRequest, Employee> employeeJoin = root.join("employee");
            Join<TimesheetRequest, Status> statusJoin = root.join("status", JoinType.LEFT);

            // search keyword
            if (StringUtils.hasText(request.getQuery())) {
                String keyword = "%" + request.getQuery().toLowerCase() + "%";

                predicates.add(
                        cb.or(
                                cb.like(cb.lower(employeeJoin.get("fullName")), keyword),
                                cb.like(cb.lower(employeeJoin.get("code")), keyword)
                        )
                );
            }

            // filter status
            if (StringUtils.hasText(request.getStatus())) {
                predicates.add(
                        cb.equal(cb.lower(statusJoin.get("name")), request.getStatus().toLowerCase())
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
