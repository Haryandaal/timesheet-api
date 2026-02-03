package id.timesheet.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_tr_timesheet_detail")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class TimesheetDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "timesheet_request_id", nullable = false)
    private TimesheetRequest timesheetRequest;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "work_date", nullable = false)
    private LocalDateTime workDate;

    @Column(name = "location_type", length = 100)
    private String locationType;

    @Column(name = "hours_spent", precision = 4, scale = 2)
    private BigDecimal hoursSpent;

    @Column(name = "task_description", columnDefinition = "TEXT", nullable = false)
    private String taskDescription;
}
