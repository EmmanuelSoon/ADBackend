package nus.iss.ADBackend.Repo;

import nus.iss.ADBackend.model.HealthRecord;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, Integer> {

    @Query("Select hr from HealthRecord hr where hr.user.id = ?1 order by hr.date desc")
    List<HealthRecord> findByUserId(int userId);
    HealthRecord findById(int Id);
    @Transactional
    void deleteById(int Id);
    @Modifying
    @Query("delete from HealthRecord hr where hr.user.id = ?1")
    void deleteByUserId(int userId);
    @Query("Select hr from HealthRecord hr where hr.user.id = ?1 and hr.date = ?2")
    HealthRecord findByUserIdAndAndDate(int id, LocalDate date);
    
    @Query("Select hr from HealthRecord hr where hr.user.id = ?1 and hr.date = ?2")
    List<HealthRecord> findListByUserIdAndAndDate(int id, LocalDate date);
    
    @Query(value = "select  * from dietdata.health_record where user_id = 1 ORDER BY date desc LIMIT 1", nativeQuery = true )
    HealthRecord findTopOneUserHealthRecord (int userId);
}