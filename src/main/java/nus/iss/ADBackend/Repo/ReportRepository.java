package nus.iss.ADBackend.Repo;

import nus.iss.ADBackend.model.Recipe;
import nus.iss.ADBackend.model.Report;
import nus.iss.ADBackend.model.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    @Query("select r from Report r where r.user.id = ?1")
    List<Report> findReportByUserId(int userId);
    @Query("select r from Report r where r.status = ?1 order by r.dateTime")
    List<Report> findReportByReportStatus(ReportStatus status);
    @Query("select r from Report r where r.recipe.id = ?1")
    List<Report> findReportByRecipeId(int recipeId);
    @Modifying
    @Query("delete from Report r where r.user.id = ?1")
    void deleteByUserId(int userId);
    @Modifying
    @Query("delete from Report r where r.recipe.id = ?1")
    void deleteAllByRecipeId(int id);
    @Modifying
    void deleteById(int id);
    Report findById(int id);
}
