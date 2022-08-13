package nus.iss.ADBackend.helper;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nus.iss.ADBackend.model.DietRecord;
import nus.iss.ADBackend.model.HealthRecord;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCombinedData {
	private List<WeekMonthData> weekList;
	private List<WeekMonthData> monthList;
    private List<HealthRecord> myHrList;
    private List<DietRecord> myDietRecord;

}
