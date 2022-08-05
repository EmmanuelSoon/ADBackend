package nus.iss.ADBackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Users")
@NoArgsConstructor
@Data
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String username;
	private String password;
	@Enumerated(EnumType.STRING)
	private Role role;
	@Enumerated(EnumType.STRING)
	private Goal goal;
	private LocalDate dateofbirth;
	@Size(max = 1, min = 1)
	private String gender;
	private Double calorieintake_limit_inkcal;
	private Double waterintake_limit_inml;

	@OneToMany
	private List<Ingredient> dislike = new ArrayList<>();

	public User(String name, String username, String password, Role role, LocalDate dateofbirth, String gender,
			Double calorieintake_limit_inkcal, Double waterintake_limit_inml) {
		this.name = name;
		this.username = username;
		this.password = password;
		this.role = role;
		this.dateofbirth = dateofbirth;
		this.gender = gender;
		this.calorieintake_limit_inkcal = calorieintake_limit_inkcal;
		this.waterintake_limit_inml = waterintake_limit_inml;
	}

	public User(String name, String username, String password, Role role) {
		this.name = name;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
    public User(String username, String password, LocalDate dob, String gender, Goal goal){
        this.username = username;
        this.password = password;
        this.dateofbirth = dob;
        this.gender = gender;
        this.goal = goal;
    }
}
