package nus.iss.ADBackend;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import nus.iss.ADBackend.Repo.*;
import nus.iss.ADBackend.model.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserLogCRUDTest {
    
    @Autowired
	UserRepository uRepo;

	@Autowired
	IngredientRepository iRepo;

	@Autowired
	DietRecordRepository fdRepo;

	@Autowired
	HealthRecordRepository hsRepo;

	@Autowired
	RewardRepository rRepo;

	@Autowired
	UserRewardRepository urRepo;

    @Test
	@Order(1)
	void createUserTest() {
		User user1 = new User("Henry", "user1", "pass1", Role.NORMAL);
		uRepo.saveAndFlush(user1);
		User found = uRepo.findById(user1.getId());
		Assertions.assertNotNull(found);

	}

	@Test
	@Order(2)
	void updateUserTest() {
		User user2 = new User("Henrietta", "user2", "pass2", Role.NORMAL);
		Ingredient celery = new Ingredient("Celery", 100.0);
		iRepo.saveAndFlush(celery);
		List<Ingredient> myDislikes = user2.getDislike();
		myDislikes.add(celery);
		uRepo.saveAndFlush(user2);
		User found = uRepo.findById(user2.getId());
		Assertions.assertEquals(found.getDislike().get(0).getName(), "Celery");
	}

	// //im not sure how to check the User dislikes table??

	@Test
	@Order(3)
	void createDietRecordTest(){
		User user1 = new User("Henry", "user1", "pass1", Role.NORMAL);
		uRepo.saveAndFlush(user1);
		System.out.println("before:" + user1);
		User usGot = uRepo.findByName("Henry");
		System.out.println("After:" + usGot);
		DietRecord fd1 = new DietRecord(usGot, "Chicken Rice", MealType.DINNER, 400.0, 250.0);
		fdRepo.saveAndFlush(fd1);
		List<DietRecord> userFd = fdRepo.findByUserId(usGot.getId());

		Assertions.assertFalse(userFd.isEmpty());

	}

	@Test
	@Order(4)
	void updateDietRecordTest(){
		User user1 = new User("Henry", "user1", "pass1", Role.NORMAL);
		uRepo.saveAndFlush(user1);
		DietRecord fd1 = new DietRecord(user1, "Chicken Rice", MealType.DINNER, 400.0, 250.0);
		fdRepo.saveAndFlush(fd1);
		DietRecord fd2 = fdRepo.findById(fd1.getId());
		Assertions.assertNotNull(fd2);
		fd2.setMealType(MealType.EXTRA);
		fdRepo.saveAndFlush(fd2);
		DietRecord fd3 = fdRepo.findById(fd1.getId());
		Assertions.assertEquals(MealType.EXTRA, fd3.getMealType());
	}

	@Test
	@Order(5)
	void deleteDietRecordTest(){
		User user1 = new User("Henry", "user1", "pass1", Role.NORMAL);
		uRepo.saveAndFlush(user1);
		DietRecord fd1 = new DietRecord(user1, "Chicken Rice", MealType.DINNER, 400.0, 250.0);
		fdRepo.saveAndFlush(fd1);
		DietRecord fd2 = fdRepo.findById(fd1.getId());
		Assertions.assertNotNull(fd2);
		fdRepo.deleteById(fd2.getId());
		DietRecord fd3 = fdRepo.findById(fd2.getId());
		Assertions.assertNull(fd3);
	}

	@Test
	@Order(6)
	void createHealthRecordTest(){
		User user2 = new User("Henrietta", "user2", "pass2", Role.NORMAL);
		uRepo.saveAndFlush(user2);
		HealthRecord hs1 = new HealthRecord(user2);
		HealthRecord hs2 = new HealthRecord(user2, LocalDate.now().plusDays(1));
		List<HealthRecord> myList = Arrays.asList(hs1, hs2);
		hsRepo.saveAllAndFlush(myList);
		List<HealthRecord> foundList = hsRepo.findByUserId(user2.getId());
		Assertions.assertEquals(2, foundList.size());
	}

	@Test
	@Order(7)
	void updateHealthRecordTest(){
		User user2 = new User("Henrietta", "user2", "pass2", Role.NORMAL);
		uRepo.saveAndFlush(user2);
		HealthRecord hs1 = new HealthRecord(user2);
		hsRepo.saveAndFlush(hs1);
		HealthRecord hs2 = hsRepo.findById(hs1.getId());
		hs2.setCalIntake(1500.0);
		hsRepo.saveAndFlush(hs2);
		HealthRecord hs3 = hsRepo.findById(hs1.getId());
		Assertions.assertEquals(1500.0, hs3.getCalIntake());
		
	}

	@Test
	@Order(8)
	void deleteHealthRecordTest(){
		User user2 = new User("Henrietta", "user2", "pass2", Role.NORMAL);
		uRepo.saveAndFlush(user2);
		HealthRecord hs1 = new HealthRecord(user2);
		hsRepo.saveAndFlush(hs1);
		HealthRecord hs2 = hsRepo.findById(hs1.getId());
		hsRepo.deleteById(hs2.getId());
		HealthRecord hs3 = hsRepo.findById(hs2.getId());
		Assertions.assertNull(hs3);

	}

	@Test
	@Order(9)
	void deleteUserTest(){
		User user1 = new User("Henry", "user1", "pass1", Role.NORMAL);
		uRepo.saveAndFlush(user1);
		DietRecord fd1 = new DietRecord(user1, "Chicken Rice", MealType.DINNER, 400.0, 250.0);
		fdRepo.saveAndFlush(fd1);
		HealthRecord hs1 = new HealthRecord(user1);
		hsRepo.saveAndFlush(hs1);
		User userFound = uRepo.findById(user1.getId());
		Assertions.assertNotNull(userFound);

		hsRepo.delete(hs1);
		fdRepo.delete(fd1);
		uRepo.delete(userFound);

		DietRecord fdFound = fdRepo.findById(fd1.getId());
		Assertions.assertNull(fdFound);

		HealthRecord hsFound = hsRepo.findById(hs1.getId());
		Assertions.assertNull(hsFound);

		User userNull = uRepo.findById(user1.getId());
		Assertions.assertNull(userNull);


	}

	@Test
	@Order(10)
	void CreateUserRewardTest(){
		User user1 = new User("Henry", "user1", "pass1", Role.NORMAL);
		Reward logOneWeek = new Reward("Three days in a row!", "You've logged your calories 3 days in a row, keep it up!");
		uRepo.saveAndFlush(user1);
		rRepo.saveAndFlush(logOneWeek);
		urRepo.saveAndFlush(new UserReward(user1, logOneWeek));
		List<UserReward> urList = urRepo.findByUserId(user1.getId());
		Assertions.assertFalse(urList.isEmpty());

	}

	@Test
	@Order(11)
	void deleteUserRewardTest(){
		User user1 = new User("Henry", "user1", "pass1", Role.NORMAL);
		User user2 = new User("Henrietta", "user2", "pass2", Role.NORMAL);
		Reward logOneWeek = new Reward("Three days in a row!", "You've logged your calories 3 days in a row, keep it up!");
		uRepo.saveAndFlush(user1);
		uRepo.saveAndFlush(user2);
		rRepo.saveAndFlush(logOneWeek);
		urRepo.saveAndFlush(new UserReward(user1, logOneWeek));
		urRepo.saveAndFlush(new UserReward(user2, logOneWeek));
		urRepo.deleteByRewardId(logOneWeek.getId());
		rRepo.deleteById(logOneWeek.getId());
		List<UserReward> urList = urRepo.findByRewardId(logOneWeek.getId());
		System.out.println(urList);
		Assertions.assertTrue(urList.isEmpty());
		
	}
}
