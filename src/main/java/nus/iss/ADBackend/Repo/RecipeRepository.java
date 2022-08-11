package nus.iss.ADBackend.Repo;

import nus.iss.ADBackend.model.Recipe;
import nus.iss.ADBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    @Query("select r from Recipe r where r.user.id = ?1 and r.dateTime = ?2")
    public Recipe findByUserIdAndAndDateTime(int id, LocalDateTime time);
    @Query("select r from Recipe r where r.user.id = ?1")
    public List<Recipe> findAllByUserId(int userId);
    @Query("select r from Recipe r where r.dish.id = ?1")
    public List<Recipe> findAllByDishId(int dishId);
    @Modifying
    @Query("delete from Recipe r where r.id = ?1 ")
    public void deleteById(int id);
    @Modifying
    @Query("delete from Recipe r where r.user.id = ?1 ")
    public void deleteByUserId(int userId);
    @Modifying
    @Query("delete from Recipe r where r.dish.id = ?1 ")
    public void deleteByDishId(int dishId);
    @Query("select r from Recipe r where r.id = ?1")
    public Recipe findById(int id);
    @Query("select distinct r from Recipe r where lower(r.name) like %?1% or lower(r.searchWords) like %?1%")
    public List<Recipe> searchRecipe(String keyword);
    @Query("select r from Recipe r where r.flagged = true")
    public List<Recipe>findAllFlaggedRecipes();

/*
    @Modifying(flushAutomatically = true)
    @Query("update Recipe r set r.user = ?2 where r.user.id = ?1")
    public void deprecateRecipes(int userId, User defaultUser);
*/

}
