package nus.iss.ADBackend.Repo;

import nus.iss.ADBackend.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DishRepository extends JpaRepository<Dish, Integer> {
    @Query("select d from Dish d where d.name = ?1")
    public Dish findByName(String name);
}
