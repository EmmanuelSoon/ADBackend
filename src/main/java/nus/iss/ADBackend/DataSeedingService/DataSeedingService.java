package nus.iss.ADBackend.DataSeedingService;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import nus.iss.ADBackend.Repo.DietRecordRepository;
import nus.iss.ADBackend.Repo.DishRepository;
import nus.iss.ADBackend.Repo.HealthRecordRepository;
import nus.iss.ADBackend.Repo.IngredientRepository;
import nus.iss.ADBackend.Repo.NutritionRecordRepository;
import nus.iss.ADBackend.Repo.RecipeRepository;
import nus.iss.ADBackend.Repo.UserRepository;
import nus.iss.ADBackend.model.DietRecord;
import nus.iss.ADBackend.model.Dish;
import nus.iss.ADBackend.model.Goal;
import nus.iss.ADBackend.model.HealthRecord;
import nus.iss.ADBackend.model.Ingredient;
import nus.iss.ADBackend.model.MealType;
import nus.iss.ADBackend.model.NutritionRecord;
import nus.iss.ADBackend.model.Procedure;
import nus.iss.ADBackend.model.Recipe;
import nus.iss.ADBackend.model.Role;
import nus.iss.ADBackend.model.User;
import nus.iss.ADBackend.model.WeightedIngredient;

@Component
public class DataSeedingService {

	@Autowired
	DishRepository dRepo;
	@Autowired
	NutritionRecordRepository nRepo;
	@Autowired
	IngredientRepository iRepo;
	@Autowired
	UserRepository uRepo;
	@Autowired
	DietRecordRepository drRepo;
	@Autowired
	HealthRecordRepository hrRepo;
	@Autowired
	RecipeRepository rRepo;

	List<Integer> ingredientIds = getIngredientList();

	private List<Integer> getIngredientList() {
		List<Integer> res = new ArrayList<>();
		for (int i = 1; i <= 28; i++) {
			res.add(i);
		}
		for (int i = 31; i <= 58; i++) {
			res.add(i);
		}
		for (int i = 61; i <= 67; i++) {
			if (i != 65) {
				res.add(i);
			}
		}
		for (int i = 73; i <= 101; i++) {
			res.add(i);
		}
		return res;
	}

	public void seedIngredientFromCSV(String path) throws IOException {
		Reader reader = new FileReader(path);
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(reader);
		List<Ingredient> list = new ArrayList<>();
		for (CSVRecord record : records) {
			NutritionRecord nutritionRecord = new NutritionRecord(Double.parseDouble(record.get(1)),
					Double.parseDouble(record.get(2)), Double.parseDouble(record.get(3)),
					Double.parseDouble(record.get(4)), Double.parseDouble(record.get(5)),
					Double.parseDouble(record.get(6)), Double.parseDouble(record.get(7)),
					Double.parseDouble(record.get(8)), Double.parseDouble(record.get(9)),
					Double.parseDouble(record.get(10)), Double.parseDouble(record.get(11)),
					Double.parseDouble(record.get(12)), Double.parseDouble(record.get(13)),
					Double.parseDouble(record.get(14)), Double.parseDouble(record.get(15)),
					Double.parseDouble(record.get(16)), Double.parseDouble(record.get(17)));
			Ingredient ingredient = new Ingredient(record.get(0));
			ingredient.setNutritionRecord(nutritionRecord);
			ingredient.setCalorie(nutritionRecord.getTotalCalories());
			ingredient.setImage(record.get(18));
			list.add(ingredient);
		}
		iRepo.saveAllAndFlush(list);
	}

	public void seedUserFromCSV(String path) throws IOException {
		Reader reader = new FileReader(path);
		Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(reader);
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		List<User> list = new ArrayList<>();
		for (CSVRecord record : records) {
			User u = new User();
			String name = record.get(0);
			u.setName(name);
			u.setUsername(record.get(1));
			u.setPassword(record.get(2));
			u.setRole(record.get(3).equals("1") ? Role.ADMIN : Role.NORMAL);
			u.setDateofbirth(LocalDate.parse(record.get(4), dateFormatter));
			u.setGender(record.get(5));
			u.setCalorieintake_limit_inkcal(Double.parseDouble(record.get(6)));
			u.setWaterintake_limit_inml(Double.parseDouble(record.get(7)));
			u.setGoal(getRandomGoal());
			list.add(u);
		}
		uRepo.saveAllAndFlush(list);
	}

	private Goal getRandomGoal() {
		int num = new Random().nextInt(4);
		if (num == 0)
			return Goal.MUSCLE;
		if (num == 1)
			return Goal.WEIGHTGAIN;
		if (num == 2)
			return Goal.WEIGHTLOSS;
		return Goal.WEIGHTGAIN;
	}

	public void seedHealthAndDietRecordForHenry() {
		Random rdn = new Random();
		User u = uRepo.findByUsername("Henry@gmail.com");
		// List<Ingredient> ingredientList = iRepo.findAll();
		MealType[] meals = new MealType[] { MealType.BREAKFAST, MealType.LUNCH, MealType.DINNER, MealType.EXTRA };
		LocalDate date = LocalDate.now();
		// seed Diet Record
		for (int i = 1; i < 7; i++) {
			LocalDate currDate = date.minusDays(i);
			double totalCals = 0.0;
			for (int j = 0; j < 4; j++) {
				int nums = rdn.nextInt(2) + (rdn.nextInt(100) < 30 ? 1 : 0);
				for (int k = 0; k <= nums; k++) {
					Ingredient ingredient = iRepo.findById(ingredientIds.get(rdn.nextInt(ingredientIds.size()))).get();
					double weight = rdn.nextInt(300) + 100;
					double cals = weight * ingredient.getCalorie() / 100.0;
					totalCals += cals;
					drRepo.saveAndFlush(new DietRecord(currDate, u, ingredient.getName(), meals[j], cals, weight));
				}
			}
			hrRepo.saveAndFlush(new HealthRecord(currDate, 80 + rdn.nextDouble() * (rdn.nextInt(2) == 0 ? 1.0 : -1.0),
					180.0, totalCals, 500 + rdn.nextInt(500), u));
		}
	}

	public void seedDishAndRecipe(String name, double[] weights, String[] Ingredients, String[] procedures,
			String imageName, int portion) {
		String keywords = "";
		for (int i = 0; i < Ingredients.length; i++) {
			if (i != 0) {
				keywords += ",";
			}
			keywords += Ingredients[i];
		}
		List<WeightedIngredient> ingredientList = new ArrayList<>();
		for (int i = 0; i < Ingredients.length; i++) {
			ingredientList.add(new WeightedIngredient(iRepo.findByName(Ingredients[i]), weights[i]));
		}
		List<Procedure> proceduresList = new ArrayList<>();
		Recipe recipe = new Recipe();
		for (int i = 0; i < procedures.length; i++) {
			Procedure p = new Procedure(i, procedures[i], recipe);
			proceduresList.add(p);
		}
		NutritionRecord record = createNutritionRecordByList(ingredientList);

		Dish d = new Dish(name);
		d.setNutritionRecord(record);
		d.setCalorie(record.getTotalCalories());
		d.setIngredients(keywords);
		dRepo.saveAndFlush(d);
		User u = uRepo.findByUsername("Henry@gmail.com");
		recipe.setProcedures(proceduresList);
		recipe.setIngredientList(ingredientList);
		recipe.setUser(u);
		recipe.setDish(d);
		recipe.setDateTime(LocalDateTime.now());
		recipe.setImage(imageName);
		recipe.setNutritionRecord(createNutritionRecordByList(ingredientList));
		recipe.setPortion(portion);
		recipe.setName(name);
		recipe.setSearchWords(keywords);
		rRepo.saveAndFlush(recipe);
	}

	public NutritionRecord createNutritionRecordByList(List<WeightedIngredient> ingredientList) {
		NutritionRecord nutritionRecord = new NutritionRecord();
		double ServingSize = 0.0;
		for (WeightedIngredient weightedIngredient : ingredientList) {
			NutritionRecord currRecord = weightedIngredient.getIngredient().getNutritionRecord();
			double weight = weightedIngredient.getWeight();
			if (currRecord.getTotalCalories() > 0) {
				nutritionRecord.setTotalCalories(
						nutritionRecord.getTotalCalories() + currRecord.getTotalCalories() * weight / 100.0);
			}
			if (currRecord.getCarbs() > 0) {
				nutritionRecord.setCarbs(nutritionRecord.getCarbs() + currRecord.getCarbs() * weight / 100.0);
			}
			if (currRecord.getProteins() > 0) {
				nutritionRecord.setProteins(nutritionRecord.getProteins() + currRecord.getProteins() * weight / 100.0);
			}
			if (currRecord.getFats() > 0) {
				nutritionRecord.setFats(nutritionRecord.getFats() + currRecord.getFats() * weight / 100.0);
			}
			if (currRecord.getSugar() > 0) {
				nutritionRecord.setSugar(nutritionRecord.getSugar() + currRecord.getSugar() * weight / 100.0);
			}
			if (currRecord.getSodium() > 0) {
				nutritionRecord.setSodium(nutritionRecord.getSodium() + currRecord.getSodium() * weight / 100.0);
			}
			if (currRecord.getCalcium() > 0) {
				nutritionRecord.setCalcium(nutritionRecord.getCalcium() + currRecord.getCalcium() * weight / 100.0);
			}
			if (currRecord.getIron() > 0) {
				nutritionRecord.setIron(nutritionRecord.getIron() + currRecord.getIron() * weight / 100.0);
			}
			if (currRecord.getVitaminA() > 0) {
				nutritionRecord.setVitaminA(nutritionRecord.getVitaminA() + currRecord.getVitaminA() * weight / 100.0);
			}
			if (currRecord.getVitaminB12() > 0) {
				nutritionRecord
						.setVitaminB12(nutritionRecord.getVitaminB12() + currRecord.getVitaminB12() * weight / 100.0);
			}
			if (currRecord.getVitaminB6() > 0) {
				nutritionRecord
						.setVitaminB6(nutritionRecord.getVitaminB6() + currRecord.getVitaminB6() * weight / 100.0);
			}
			if (currRecord.getVitaminC() > 0) {
				nutritionRecord.setVitaminC(nutritionRecord.getVitaminC() + currRecord.getVitaminC() * weight / 100.0);
			}
			if (currRecord.getVitaminE() > 0) {
				nutritionRecord.setVitaminE(nutritionRecord.getVitaminE() + currRecord.getVitaminE() * weight / 100.0);
			}
			if (currRecord.getVitaminK() > 0) {
				nutritionRecord.setVitaminK(nutritionRecord.getVitaminK() + currRecord.getVitaminK() * weight / 100.0);
			}
			if (currRecord.getFiber() > 0) {
				nutritionRecord.setFiber(nutritionRecord.getFiber() + currRecord.getFiber() * weight / 100.0);
			}
			if (currRecord.getCholesterol() > 0) {
				nutritionRecord.setCholesterol(
						nutritionRecord.getCholesterol() + currRecord.getCholesterol() * weight / 100.0);
			}
			if (currRecord.getWater() > 0) {
				nutritionRecord.setWater(nutritionRecord.getWater() + currRecord.getWater() * weight / 100.0);
			}
			ServingSize += weight;
		}
		nutritionRecord.setServingSize(ServingSize);
		return nutritionRecord;
	}

	public void seedDishAndRecipe() {
		String[] Ingredients1 = new String[] { "Spaghetti", "Garlic", "Oil", "Chilli", "Salt", "Coriander Leaf" };
		double[] weights1 = new double[] { 300, 30, 20, 5, 4, 16 };
		String[] procedures1 = new String[] {
				"Bring a large pot of lightly salted water to a boil. Cook spaghetti in the boiling water, stirring occasionally until cooked through but firm to the bite, about 12 minutes. Drain and transfer to a pasta bowl.",
				"Combine garlic and olive oil in a cold skillet. Cook over medium heat to slowly toast garlic, about 10 minutes. Reduce heat to medium-low when olive oil begins to bubble. Cook and stir until garlic is golden brown, after 5 minutes. Remove from heat.",
				"Stir red pepper flakes, black pepper, and salt into the pasta. Pour in olive oil and garlic, and sprinkle on Italian parsley and half of the Parmigiano-Reggiano cheese; stir until combined.",
				"Serve pasta topped with the remaining Parmigiano-Reggiano cheese." };
		String name1 = "Spaghetti Aglio e Olio";

		String[] Ingredients2 = new String[] { "Raw Chicken Breast", "Noodles", "Egg", "Xiao Bai Cai", "Salt",
				"Water" };
		double[] weights2 = new double[] { 400, 240, 80, 100, 4, 400 };
		String[] procedures2 = new String[] {
				"Season the chicken with salt and ground black pepper on both sides of the chicken.",
				"Turn on the Saute mode on the Instant Pot. As soon as the pot is fully heated and hot, add the oil. Pan sear the chicken until both sides turn brown.",
				"Add the chicken broth, water, and the white parts of the scallions. Cover the pot and select Manual and set to High pressure for 10 minutes.",
				"In a small pot, add cold water. The water level must be deep enough to cover all eggs. When the water boils, add the eggs and cook for 7 minutes. When your eggs are done cooking, plunge them into ice water and leave to sit for 5 minutes. Carefully peel off the shells, while the eggs are still in the water. Make sure you remove the membrane covering the egg white. Slice the eggs into halves. Set aside.",
				"When the Instant Pot beeps, turn to Quick Release. When the valve drops, remove the lid carefully. Discard the white parts of the scallions and scoop out the chicken. Let cool and shred the chicken using two forks.",
				"Turn to Saute mode and as soon as the soup boils, add the chicken and ramen noodles. Make sure you push the ramen noodles down so they submerge in the soup.",
				"As soon as the ramen noodles becomes soft (no longer in a block), add the bok choy and soy sauce. Stir the ramen around. Cancel Saute mode and remove the pot. DO NOT OVERCOOK the ramen as the noodles will continue to cook even after you remove the pot.",
				"Divide the ramen noodles into four bowls. Top with ramen eggs, chopped scallions, sesame seeds (if using), and dashes of chili oil. Serve the ramen immediately, do not keep in the pot as they will turn soggy." };
		String name2 = "Instant Pot Ramen";

		String[] Ingredients3 = new String[] { "Rice", "Luncheon Meat", "Scallion Spring Onion", "Egg",
				"Iceberg Lettuce", "Salt", "Light Soya Sauce", "Vegetable Oil" };
		double[] weights3 = new double[] { 200, 50, 10, 50, 100, 2, 8, 5 };
		String[] procedures3 = new String[] {
				"To prepare the day-old cooked rice, remove it from the refrigerator and heat it in the microwave until it's room temperature or slightly warm. If you are using 2 servings of freshly cooked rice, transfer the hot rice to a baking sheet. Spread it out and let it cool completely. This will remove the moisture from the rice.",
				"Cut the white part of the green onion into rounds and the green part diagonally. Keep the white and green parts separate and set aside. Reserve a few green slices for the garnish.",
				"Dice the ham into square pieces ½ inch (1.3 cm) wide.", "Crack and whisk the egg in a bowl.",
				"Make sure all the ingredients are prepped and ready to go, as this dish cooks quickly. Heat the wok (or a large frying pan) on medium-high heat. Once it’s hot, add half the oil and swirl it around to make sure it coats the entire surface of the wok. Add the beaten egg to the hot wok. After a few seconds, the egg will start to float on top of the oil.",
				"With the blunt end of a spatula, swirl the loosely set egg around the pan to keep it fluffy while continuing to cook it. When the bottom of the egg is set but the top is still a bit runny and not quite fully cooked, transfer it to a plate. We do not want to overcook the egg at this stage.",
				"Add the other half of the oil to the pan. Then, add the ham and white part of the chopped green onion. Stir-fry and coat well with the oil.",
				"Add the cooked and cooled rice to the wok. With the spatula, use a slicing motion to separate the rice clumps without mashing or breaking the rice grains. Combine it with the ham mixture and continue to toss the rice so it is coated with oil and develops a nice char.",
				"Add the cooked egg back to the wok and break it into smaller pieces while you combine it with the rice. If some of the rice sticks to the wok (it happens when there's not enough oil), you can scrape it off easily if you're using a well-seasoned or nonstick wok. This creates a nice charred flavor. You may need to add more oil if you're using a stainless steel pan, as the rice tends to stick more.",
				"Season the rice with salt and white pepper.",
				"Add the soy sauce and toss to distribute it throughout the rice. The key action here is to continuously toss the fried rice in the air to keep it from clumping instead of leaving it a sticky mass clustered at the bottom of the wok. Turning and tossing the rice in the air also helps develop a smoky flavor.",
				"Add the green part of the chopped green onion. Taste the fried rice and adjust the seasoning if needed. After tossing the rice a few more times, transfer it to a plate." };
		String name3 = "Japanese Fried Rice";
		String[] Ingredients4 = new String[] { "Broccoli", "Oil", "Garlic", "Salt", "Light Soya Sauce", "Sugar",
				"Oyster Sauce" };
		double[] weights4 = new double[] { 600, 4, 20, 4, 2, 2, 2 };
		String[] procedures4 = new String[] {
				"Bring enough water to a boil in a large pot, add pinch of salt and ¼ teaspoon sesame oil and then cook broccoli for around 1 minute. Transfer out. (No need to drain)",
				"Heat cooking oil in wok and fry chopped garlic until aroma over slow fire, then turn up the fire and slide broccoli in.",
				"Add salt and oyster sauce, mix well. And serve warm." };
		String name4 = "Garlic Broccoli Stir Fry";

		String[] Ingredients5 = new String[] { "Japanese Cucumber", "Salt", "Vinegar", "Sugar", "Light Soya Sauce" };
		double[] weights5 = new double[] { 400, 2, 40, 20, 2 };
		String[] procedures5 = new String[] { "Gather all the ingredients.",
				"In a bowl, combine the rice vinegar, sugar, salt, and soy sauce. Whisk well together. If the vinegar tastes too strong, you can dilute it with a very small amount of dashi (Japanese soup stock) or water.",
				"Soak the dried wakame seaweed in water and let it rehydrate for 10 minutes.",
				"Peel the skin of the cucumbers, leaving some skin on to create stripes. Then, slice them thinly into rounds.",
				"Sprinkle the salt on the slices and gently massage it in. Set aside for 5 minutes. The salt helps draw out the moisture from the cucumbers (so it does not dilute the salad dressing after mixing).",
				"Squeeze out the liquid from the rehydrated wakame seaweed and cucumbers. Add them to the bowl with the dressing and toss it all together. ",
				"Keep in an airtight container and store in the refrigerator for up to 2-3 days. However, it's possible that the cucumber releases more moisture and the sauce may get diluted. Enjoy it soon!" };
		String name5 = "Japanese Cucumber Salad";
		seedDishAndRecipe(name1, weights1, Ingredients1, procedures1, "/asset/images/recipe1.jpg", 3);
		seedDishAndRecipe(name2, weights2, Ingredients2, procedures2, "/asset/images/recipe2.jpg", 3);
		seedDishAndRecipe(name3, weights3, Ingredients3, procedures3, "/asset/images/recipe3.jpg", 2);
		seedDishAndRecipe(name4, weights4, Ingredients4, procedures4, "/asset/images/recipe4.jpg", 4);
		seedDishAndRecipe(name5, weights5, Ingredients5, procedures5, "/asset/images/recipe5.jpg", 4);
	}

	public void launchSeeding() throws IOException {
		if (!uRepo.existsBy()) {
			seedUserFromCSV("./src/main/resources/data/userdata.csv");
			seedIngredientFromCSV("./src/main/resources/data/data.csv");
			seedHealthAndDietRecordForHenry();
			seedDishAndRecipe();
		}
	}
}
