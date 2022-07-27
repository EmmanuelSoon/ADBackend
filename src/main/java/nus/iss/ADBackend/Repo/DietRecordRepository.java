package nus.iss.ADBackend.Repo;

import nus.iss.ADBackend.model.DietRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DietRecordRepository extends JpaRepository<DietRecord, Integer> {
}
