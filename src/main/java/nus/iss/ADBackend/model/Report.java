package nus.iss.ADBackend.model;

import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String content;
    private LocalDateTime dateTime;
    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;
    @Enumerated(EnumType.STRING)
    private ReportStatus status;
    @ManyToOne
    @JoinColumn(name = "recipeId")
    private Recipe recipe;

    public Report(User user) {
        this.user = user;
    }
}
