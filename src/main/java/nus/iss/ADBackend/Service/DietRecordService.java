package nus.iss.ADBackend.Service;

import net.bytebuddy.asm.Advice;
import nus.iss.ADBackend.Repo.DietRecordRepository;
import nus.iss.ADBackend.model.DietRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class DietRecordService {

    @Autowired
    DietRecordRepository drRepo;

    void createDietRecord(DietRecord dr) {
        drRepo.saveAndFlush(dr);
    }
    boolean saveDietRecord(DietRecord dr) {
        if (drRepo.findById(dr.getId()) != null) {
            drRepo.saveAndFlush(dr);
            return true;
        }
        return false;
    }

    List<DietRecord> findByUserId(int userId) {
        return drRepo.findByUserId(userId);
    }

    List<DietRecord> findByUserIdAndDate(int userId, LocalDate date) {
        return drRepo.findByUserIdAndDate(userId, date);
    }

    @Transactional
    boolean deleteDietRecordById(int id) {
        if (drRepo.findById(id) != null) {
            drRepo.deleteById(id);
            return true;
        }
        return false;
    }

    double getTotalCaloriesByUserIdAndDate(int userId, LocalDate date) {
        List<DietRecord> dietRecords= findByUserIdAndDate(userId, date);
        double res = 0.0;
        for (DietRecord record : dietRecords) {
            res += record.getCalorie();
        }
        return res;
    }
}
