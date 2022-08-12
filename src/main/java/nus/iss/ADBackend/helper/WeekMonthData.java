package nus.iss.ADBackend.helper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class WeekMonthData {

	public String weekMonthRepr;
	public Double cal_intake;
	public Double water_intake;
	public Double user_weight;

	public WeekMonthData() {
	}

	public WeekMonthData(String weekMonthRepr, Double cal_intake, Double water_intake, Double user_weight) {
		this.weekMonthRepr = weekMonthRepr;
		this.cal_intake = cal_intake;
		this.water_intake = water_intake;
		this.user_weight = user_weight;
	}

	public String getWeekMonthRepr() {
		return weekMonthRepr;
	}

	public Double getCal_intake() {
		return cal_intake;
	}

	public Double getWater_intake() {
		return water_intake;
	}

	public Double getUser_weight() {
		return user_weight;
	}
	
	

}
