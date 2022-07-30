package nus.iss.ADBackend;


import nus.iss.ADBackend.Repo.*;
import nus.iss.ADBackend.Service.RewardService;
import nus.iss.ADBackend.Service.UserService;
import nus.iss.ADBackend.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.w3c.dom.ls.LSInput;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@Import({UserService.class, RewardService.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CRUDTest {

    @Autowired
    DishRepository dRepo;
    @Autowired
    NutritionRecordRepository nRepo;
    @Autowired
    UserRepository uRepo;
    @Autowired
    RecipeRepository rRepo;
    @Autowired
    CommentRepository cRepo;
    @Autowired
    IngredientRepository iRepo;
    @Autowired
    HealthRecordRepository hrRepo;
    @Autowired
    DietRecordRepository drRepo;
    @Autowired
    UserService uService;
    @Autowired
    ReportRepository rpRepo;
    @Autowired
    RewardService rwService;

    public Dish createDishWithNutritionInfo() {
        NutritionRecord n = new NutritionRecord(10.0);
        Dish dish = new Dish("MALA", "path", 100.0, n);
        return dish;
    }
    @Test
    @Order(1)
    public void DishCreateTest() {
        //Create Test
        Dish dish = createDishWithNutritionInfo();
        dRepo.saveAndFlush(dish);
        Dish d = dRepo.findByName("MALA");
        Assertions.assertNotNull(d);
        System.out.println(d.getCalorie());
        NutritionRecord nr = d.getNutritionRecord();
        Assertions.assertNotNull(nr);
        //Update the Value
        d.setName("FISH");
        dRepo.save(d);
        Assertions.assertNull(dRepo.findByName("MALA"));
        Assertions.assertNotNull(dRepo.findByName("FISH"));
        Assertions.assertEquals(dRepo.findByName("FISH").getNutritionRecord().getTotalCalories(), 10.0);
    }
    @Test
    @Order(2)
    public void DishDeleteTest() {
        //Delete Test
        Dish dish = createDishWithNutritionInfo();
        dRepo.saveAndFlush(dish);
        Dish d = dRepo.findByName("MALA");
        int nId = d.getNutritionRecord().getId();
        Assertions.assertEquals(nRepo.findById(nId).getTotalCalories(), 10.0);
        dRepo.delete(d);
        Assertions.assertNull(dRepo.findByName("MALA"));
        Assertions.assertNull(nRepo.findById(nId));
    }

    private User createUserByName(String name, Role role) {
        User u = new User(name, "test-username", "test-password", role);
        uRepo.save(u);
        return uRepo.findByName(name);
    }

    private Recipe createRecipeByUserAndDish(User user, Dish dish, LocalDateTime time, String stpes) {
        Recipe r = new Recipe("imagetestpath", user, dish, time, stpes);
        rRepo.saveAndFlush(r);
        return rRepo.findByUserIdAndAndDateTime(user.getId(), time);
    }

    @Test
    @Order(3)
    public void recipeCreationAndDeleteTest() {
        dRepo.saveAndFlush(createDishWithNutritionInfo());
        Dish d = dRepo.findByName("MALA");
        User u1 = createUserByName("test-staff", Role.NORMAL);
        User u2 = createUserByName("test-admin", Role.ADMIN);
        //public Recipe(String image, User user, Dish dish, LocalDateTime dateTime, String procedures)
        LocalDateTime t1 = LocalDateTime.of(2020, 12, 31, 11, 59, 59);
        LocalDateTime t2 = LocalDateTime.of(2021, 12, 31, 11, 59, 59);
        Recipe r1 = createRecipeByUserAndDish(u1, d, t1, "11111");
        Recipe r2 = createRecipeByUserAndDish(u2, d, t2, "22222");
        Assertions.assertNotNull(rRepo.findByUserIdAndAndDateTime(u1.getId(), t1));
        Assertions.assertNotNull(rRepo.findByUserIdAndAndDateTime(u2.getId(), t2));
        Assertions.assertNull(rRepo.findByUserIdAndAndDateTime(u1.getId(), t2));
        Assertions.assertEquals(2, rRepo.findAllByDishId(d.getId()).size());
        /*
        List<Recipe> rList = rRepo.findAllByDishId(d.getId());
        for (Recipe r: rList)
        {
            System.out.println(r);
        }*/
        rRepo.deleteByUserId(u1.getId());
        Assertions.assertEquals(1, rRepo.findAllByDishId(d.getId()).size());
        rRepo.deleteByDishId(d.getId());
        Assertions.assertEquals(0, rRepo.findAllByDishId(d.getId()).size());
        Assertions.assertNull(rRepo.findByUserIdAndAndDateTime(u1.getId(), t1));
        Assertions.assertNull(rRepo.findByUserIdAndAndDateTime(u2.getId(), t2));
        Assertions.assertNotNull(dRepo.findByName("MALA"));
    }
    //public Comment(double rating, String content, User user, LocalDateTime dateTime, Recipe recipe)
    private Comment createComment(double rating, String content, User user, LocalDateTime dateTime, Recipe recipe) {
        Comment c = new Comment(rating, content, user, dateTime, recipe);
        cRepo.saveAndFlush(c);
        return cRepo.findByUserIdAndDateTime(user.getId(), dateTime);
    }

    @Test
    @Order(4)
    public void CommentCreateAndDeleteTest() {
        dRepo.saveAndFlush(createDishWithNutritionInfo());
        Dish d = dRepo.findByName("MALA");
        User u1 = createUserByName("test-staff", Role.NORMAL);
        User u2 = createUserByName("test-admin", Role.ADMIN);
        LocalDateTime t1 = LocalDateTime.of(2020, 12, 31, 11, 59, 59);
        LocalDateTime t2 = LocalDateTime.of(2021, 12, 31, 11, 59, 59);
        Recipe r1 = createRecipeByUserAndDish(u1, d, t1, "11111");
        Comment c1 = createComment(5.0, "nice", u1, t1, r1);
        Comment c2 = createComment(1.0, "nice", u2, t2, r1);
        List<Comment> commentList = cRepo.findAllByRecipeId(r1.getId());
        Assertions.assertEquals(2, commentList.size());
        for (Comment c : commentList) {
            System.out.println(c);
        }
        //delete dish and it associate nutritution info, recipe and comment
        //when perform the parent delet operation we shall delete the children first and then followed by
        cRepo.deleteAllByRecipeId(r1.getId());//delete comment
        rRepo.deleteByDishId(d.getId());
        dRepo.delete(d);
        Assertions.assertNull(rRepo.findByUserIdAndAndDateTime(r1.getUser().getId(), r1.getDateTime()));
        Assertions.assertEquals(0, cRepo.findAllByRecipeId(r1.getId()).size());
        Assertions.assertNull(dRepo.findByName("MALA"));
    }

    private void createIntegredient() {
        iRepo.saveAndFlush(new Ingredient("apple", 10.0));
        iRepo.saveAndFlush(new Ingredient("fired-chicken", 20.0));
    }
    @Test
    @Order(5)
    public void WeightIngredientTest() {
        dRepo.saveAndFlush(createDishWithNutritionInfo());
        Dish d = dRepo.findByName("MALA");
        User u1 = createUserByName("test-staff", Role.NORMAL);
        LocalDateTime t1 = LocalDateTime.of(2020, 12, 31, 11, 59, 59);
        Recipe r1 = createRecipeByUserAndDish(u1, d, t1, "11111");
        createIntegredient();
        r1.getIngredientList().add(new WeightedIngredient(iRepo.findByName("apple"), 200));
        r1.getIngredientList().add(new WeightedIngredient(iRepo.findByName("fired-chicken"), 100));
        rRepo.saveAndFlush(r1);
        Recipe got = rRepo.findByUserIdAndAndDateTime(u1.getId(), t1);
        Assertions.assertNotNull(got);
        System.out.println(got);
        Assertions.assertEquals(2, got.getIngredientList().size());
        Assertions.assertEquals(4000.0, got.getTotalCalories());
        rRepo.deleteById(got.getId());
        Assertions.assertNull(rRepo.findByUserIdAndAndDateTime(r1.getUser().getId(), r1.getDateTime()));
    }
    @Test
    @Order(6)
    public void UserTest() {
        createIntegredient();
        User u1 = createUserByName("test-staff", Role.NORMAL);
        Assertions.assertEquals(0, u1.getDislike().size());
        u1.getDislike().add(iRepo.findByName("apple"));
        u1.getDislike().add(iRepo.findByName("fired-chicken"));
        uRepo.saveAndFlush(u1);
        System.out.println(uRepo.findByName("test-staff"));
        Assertions.assertEquals(2, uRepo.findByName("test-staff").getDislike().size());
        u1 = uRepo.findByName("test-staff");
        u1.getDislike().remove(0);
        uRepo.saveAndFlush(u1);
        System.out.println(uRepo.findByName("test-staff"));
        Assertions.assertEquals(1, uRepo.findByName("test-staff").getDislike().size());
        uRepo.delete(uRepo.findByName("test-staff"));
        Assertions.assertNull(uRepo.findByName("test-staff"));
    }

    @Test
    @Order(7)
    public void DeleteAndDepecateTest() {
        User u1 = new User("test1", "deleted-user", "test", Role.NORMAL);
        User u2 = new User("test2", "henry", "test", Role.NORMAL);
        User u3 = new User("test3", "user3", "test", Role.NORMAL);
        uRepo.saveAndFlush(u1);
        uRepo.saveAndFlush(u2);
        uRepo.saveAndFlush(u3);
        dRepo.saveAndFlush(createDishWithNutritionInfo());
        Dish d = dRepo.findByName("MALA");
        LocalDateTime t1 = LocalDateTime.of(2020, 12, 31, 11, 59, 59);
        Recipe r1 = createRecipeByUserAndDish(u2, d, t1, "11111");
        Recipe r2 = createRecipeByUserAndDish(u3, d, t1.plusMonths(1), "22222222");
        Comment c1 = createComment(5.0, "nice", u2, t1, r1);
        Comment c2 = createComment(1.0, "bad", u3, t1.plusMonths(1), r1);
        Comment c3 = createComment(5.0, "nice", u2, t1.plusMonths(2), r2);
        HealthRecord hr = new HealthRecord(u2);
        hrRepo.saveAndFlush(hr);
        DietRecord dr = new DietRecord(u2);
        drRepo.saveAndFlush(dr);
        Assertions.assertNotNull(uRepo.findByUsername("deleted-user"));
        Assertions.assertNotNull(uRepo.findByUsername("henry"));
        Assertions.assertNotNull(uRepo.findByUsername("user3"));
        Assertions.assertEquals(2, cRepo.findAllByUserId(u2.getId()).size());
        List<Comment> commentList1 = cRepo.findAllByUserId(u2.getId());
        System.out.println(u2.getId());
        for (Comment c : commentList1) {
            System.out.println(c.getUser());
        }
        Report r = new Report(u2);
        rpRepo.saveAndFlush(r);
        Assertions.assertNotNull(rpRepo.findById(r.getId()));
        Assertions.assertEquals(1, cRepo.findAllByUserId(u3.getId()).size());
        Assertions.assertEquals(0, cRepo.findAllByUserId(u1.getId()).size());
        Assertions.assertEquals(1, hrRepo.findByUserId(u2.getId()).size());
        Assertions.assertEquals(1, drRepo.findByUserId(u2.getId()).size());
        //proceed delete operation
        uService.deleteUserById(u2.getId());
        Assertions.assertNotNull(uRepo.findByUsername("deleted-user"));
        Assertions.assertNull(uRepo.findByUsername("henry"));
        Assertions.assertNotNull(uRepo.findByUsername("user3"));
        Assertions.assertEquals(0, cRepo.findAllByUserId(u2.getId()).size());
        Assertions.assertEquals(1, cRepo.findAllByUserId(u3.getId()).size());
        Assertions.assertEquals(2, cRepo.findAllByUserId(u1.getId()).size());
        System.out.println(u1.getId());
        List<Comment> commentList = cRepo.findAllByUserId(u1.getId());
        for (Comment c : commentList) {
            System.out.println(c.getUser());
        }
        Assertions.assertEquals(0, hrRepo.findByUserId(u2.getId()).size());
        Assertions.assertEquals(0, drRepo.findByUserId(u2.getId()).size());
    }

    @Test
    @Order(8)
    public void UserRewardTest(){
        Reward r1 = new Reward("name1", "desc1");
        rwService.createReward(r1);
        Reward r2 = new Reward("name2", "desc2");
        rwService.createReward(r2);
        Assertions.assertNotNull(rwService.findByName("name1"));
        Assertions.assertNotNull(rwService.findByName("name2"));
        User u = uService.createUser("test2", "henry", "test");
        Assertions.assertNotNull(uService.findUserByUserNameAndPassword("henry", "test"));
        rwService.AddRewardToUser(u, r1);
        rwService.AddRewardToUser(u, r2);
        Assertions.assertEquals(2, rwService.getRewardsByUserId(u.getId()).size());
        List<User> uList = rwService.findByRewardId(r1.getId());
        Assertions.assertEquals(1, uList.size());
        System.out.println(uList.get(0));
        uService.deleteUserById(u.getId());
        Assertions.assertNotNull(rwService.findByName("name1"));
        Assertions.assertNotNull(rwService.findByName("name2"));
        Assertions.assertEquals(0, rwService.getRewardsByUserId(u.getId()).size());
        Assertions.assertEquals(0, rwService.findByRewardId(r1.getId()).size());
    }
}
