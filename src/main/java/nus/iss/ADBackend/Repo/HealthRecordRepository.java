package nus.iss.ADBackend.Repo;

import nus.iss.ADBackend.model.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, Integer> {
}
