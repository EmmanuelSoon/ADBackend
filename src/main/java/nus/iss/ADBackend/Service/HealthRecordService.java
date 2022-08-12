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
        setLatestWeight(hr);
  
        hrRepo.saveAndFlush(hr);
        return hr;
    }
    
    public void setLatestWeight(HealthRecord hr) {

    	LocalDate date = hr.getDate();
    	LocalDate dateLimit = hr.getDate().minusDays(11);
    	
    	//try finding last 10 day weight
    	try {
    		while(hr.getUserWeight()==0) {
    			if(date.equals(dateLimit)) {
    				return;
    			}
    			else {
    				date = date.minusDays(1);
            		HealthRecord healthRecord = findHealthRecordByUserIdAndDate(hr.getUser().getId(), date);
            		if(healthRecord != null) {
            			hr.setUserWeight(healthRecord.getUserWeight());
            		}	
    			}
    		}
    	}catch(NullPointerException e) {
    		System.out.println(e.getMessage());
    	}
    }

    
//    public double getLatestWeight(User user, LocalDate date) {
//
//    	HealthRecord healthRecord = new HealthRecord(user, date);
//
//    	double weight = healthRecord.getUserWeight();
//    	LocalDate dateLimit = date.minusDays(7);
//    	
//    	while(weight == 0) {
//    		if(!date.equals(dateLimit)) {
//    			date = date.minusDays(1);
//        		healthRecord = findHealthRecordByUserIdAndDate(user.getId(), date);
//        		if(healthRecord != null) {
//        			weight = healthRecord.getUserWeight();
//        		}	
//    		}   		
//    	}
//    	return weight;
//    }

    public List<HealthRecord> findAllHealthRecordsByUserId(int userId) {
        return hrRepo.findByUserId(userId);
    }

    public HealthRecord findHealthRecordById(int hrId){
        return hrRepo.findById(hrId);
    }

    public void updateUserWeight(int userId, double weight, LocalDate date){
        HealthRecord hr = createHealthRecordIfAbsent(userId, date);
        if (hr != null) {
            hr.setUserWeight(weight);
            hrRepo.saveAndFlush(hr);
        }
//        else{
//            // throw the hr not found exception...
//        }
    }
    
    public HealthRecord findTopOneUserHealthRecord (int userId)
    {
    	return hrRepo.findTopOneUserHealthRecord(userId);
    }

}
