package nus.iss.ADBackend.Service;

import nus.iss.ADBackend.Repo.HealthRecordRepository;
import nus.iss.ADBackend.Repo.UserRepository;
import nus.iss.ADBackend.model.HealthRecord;
import nus.iss.ADBackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HealthRecordService {

    @Autowired
    HealthRecordRepository hrRepo;
    @Autowired
    UserRepository uRepo;

    public void createHealthRecord(HealthRecord hr) {
        hrRepo.saveAndFlush(hr);
    }
    
    public boolean saveHealthRecord(HealthRecord hr) {
        if (hrRepo.findById(hr.getId()) != null) {
            hrRepo.saveAndFlush(hr);
            return true;
        }
        return false;
    }
    
    public HealthRecord findHealthRecordByUserIdAndDate(int userId, LocalDate date) {
        return hrRepo.findByUserIdAndAndDate(userId, date);
    }
    
    //should only have 1 HR per day?
    public List<HealthRecord> findHealthRecordListByUserIdAndDate(int userId, LocalDate date) {
        return hrRepo.findListByUserIdAndAndDate(userId, date);
    }
    
    public HealthRecord createHealthRecordIfAbsent(int userId, LocalDate date) {
        if (findHealthRecordByUserIdAndDate(userId, date) != null) {
            return findHealthRecordByUserIdAndDate(userId, date);
        }
        User u = uRepo.findById(userId);
        if (u == null) {
            //user not exist
            return null;
        }
        HealthRecord hr = new HealthRecord(u, date);
        //hr.setUserWeight(-1.0);
        hrRepo.saveAndFlush(hr);
        return hr;
    }

    public List<HealthRecord> findAllHealthRecordsByUserId(int userId) {
        return hrRepo.findByUserId(userId);
    }

    public HealthRecord findHealthRecordById(int hrId){
        return hrRepo.findById(hrId);
    }

    public void updateUserWeight(int userId, double weight, LocalDate date){
        HealthRecord hr = findHealthRecordByUserIdAndDate(userId, date);
        if (hr != null) {
            hr.setUserWeight(weight);
            hrRepo.saveAndFlush(hr);
        }
//        else{
//            // throw the hr not found exception...
//        }
    }

}
