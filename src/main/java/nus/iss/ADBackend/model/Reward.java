package nus.iss.ADBackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Reward")
@Data
@NoArgsConstructor
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;

    public Reward(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }
}
