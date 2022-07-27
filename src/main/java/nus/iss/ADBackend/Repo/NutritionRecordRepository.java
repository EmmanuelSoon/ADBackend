package nus.iss.ADBackend.Repo;

import nus.iss.ADBackend.model.NutritionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface NutritionRecordRepository extends JpaRepository<NutritionRecord, Integer> {

    @Query("select nr from NutritionRecord nr where nr.id = ?1")
    public NutritionRecord findById(int id);
}
