package nus.iss.ADBackend.model;

import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.NavigableMap;

@Entity
@Table(name = "UserReward")
@NoArgsConstructor
@Data
public class UserReward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rewardId")
    private Reward reward;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private User user;
    private LocalDateTime awardDateTime;

    public UserReward(User user, Reward reward) {
        super();
        this.user = user;
        this.reward = reward;
        this.awardDateTime = LocalDateTime.now();
    }
}
