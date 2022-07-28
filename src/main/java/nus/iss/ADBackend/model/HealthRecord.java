package nus.iss.ADBackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;
@Entity
@Data
@NoArgsConstructor
public class HealthRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @FutureOrPresent
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
}
