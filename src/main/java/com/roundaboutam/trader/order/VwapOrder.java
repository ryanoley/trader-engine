package com.roundaboutam.trader.order;

public class VwapOrder extends Order {

	private String startTime = "14:32:00";
	private String endTime = "20:58:00";
	private int participationRate = 12;

	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getParticipationRate() {
		return participationRate;
	}
	public void setParticipationRate(int participationRate) {
		this.participationRate = participationRate;
	}	

}
