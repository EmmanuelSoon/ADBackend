package nus.iss.ADBackend.model;

import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    private ReportCategory category;

    @OneToMany(cascade=CascadeType.ALL)
    private List<ReportAction> actionsTaken = new ArrayList<>();

    public Report(User user) {
        this.user = user;
    }

    public Report(String content, User user, Recipe recipe, ReportCategory category) {
        this.content = content;
        this.dateTime = LocalDateTime.now();
        this.user = user;
        this.status = ReportStatus.SUBMITTED;
        this.recipe = recipe;
        this.category = category;
    }
}
