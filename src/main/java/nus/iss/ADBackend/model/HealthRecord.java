package nus.iss.ADBackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import nus.iss.ADBackend.helper.WeekMonthData;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;


@NamedNativeQuery(name="HealthRecord.getWeeklyFilterRecords",
query="select week(date) as weekMonthRepr, round(avg(cal_intake),2) as cal_intake, round(avg(water_intake),2) as water_intake, round(avg(user_weight),2) as user_weight from health_record where user_id = ?1 group by week(date) order by week(date) desc",
resultSetMapping="Mapping.WeekMonthData")

@NamedNativeQuery(name="HealthRecord.getMonthlyFilterRecords",
query="select month(date) as weekMonthRepr, round(avg(cal_intake),2) as cal_intake, round(avg(water_intake),2) as water_intake, round(avg(user_weight),2) as user_weight from health_record where user_id = ?1 group by month(date) order by month(date) desc",
resultSetMapping="Mapping.WeekMonthData")

@SqlResultSetMapping(name = "Mapping.WeekMonthData",
classes = @ConstructorResult(targetClass = WeekMonthData.class,
                             columns = {@ColumnResult(name = "weekMonthRepr", type = String.class),
                                        @ColumnResult(name = "cal_intake", type = Double.class),
                                        @ColumnResult(name = "water_intake", type = Double.class),
                                        @ColumnResult(name = "user_weight", type = Double.class)}))



@Entity
@Data
@NoArgsConstructor
public class HealthRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate date;
    
    private double userWeight;
    private double userHeight;
    private double calIntake;
    private double waterIntake;

    
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public HealthRecord(User user) {
        this.user = user;
        this.date = LocalDate.now();
    }

    public HealthRecord(User user, LocalDate date) {
        this.user = user;
        this.date = date;
    }

    public HealthRecord(LocalDate date, double userWeight, double userHeight, double calIntake, double waterIntake, User user) {
        this.date = date;
        this.userWeight = userWeight;
        this.userHeight = userHeight;
        this.calIntake = calIntake;
        this.waterIntake = waterIntake;
        this.user = user;
    }
}
