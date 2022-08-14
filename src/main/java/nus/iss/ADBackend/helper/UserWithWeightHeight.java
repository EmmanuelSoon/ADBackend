package nus.iss.ADBackend.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nus.iss.ADBackend.model.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWithWeightHeight {

	private User user;
	private Double userWeight;
	private Double userHeight;

}
