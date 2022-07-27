package nus.iss.ADBackend.Repo;

import nus.iss.ADBackend.model.Recipe;
import nus.iss.ADBackend.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ReportRepository extends JpaRepository<Report, Integer> {

}
