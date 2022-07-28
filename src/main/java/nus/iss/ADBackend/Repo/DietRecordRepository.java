package nus.iss.ADBackend.Repo;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nus.iss.ADBackend.model.DietRecord;

public interface DietRecordRepository extends JpaRepository<DietRecord, Integer> {

    DietRecord findById(int id);

    @Query("select fd From DietRecord fd Where fd.user.id = :userId")
    List<DietRecord> findByUserId(@Param("userId") int userId);
    @Transactional
    void deleteById(int diaryId);
    @Modifying
    @Query("delete from DietRecord dr where dr.user.id = ?1")
    void deleteByUserId(int userId);
    @Query("select fd From DietRecord fd Where fd.user.id = ?1 and fd.date = ?2")
    List<DietRecord> findByUserIdAndDate(int userId, LocalDate date);


}
