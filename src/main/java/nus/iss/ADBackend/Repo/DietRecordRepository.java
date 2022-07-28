package nus.iss.ADBackend.Repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nus.iss.ADBackend.model.DietRecord;

public interface DietRecordRepository extends JpaRepository<DietRecord, Integer> {

    // DietRecord findById(int id);

    // @Query("select fd From FoodDiary fd Where fd.user.id = :userId")
    // List<DietRecord> findByUserId(@Param("userId") int userId);

    // @Transactional
    // void deleteById(int diaryId);


}
