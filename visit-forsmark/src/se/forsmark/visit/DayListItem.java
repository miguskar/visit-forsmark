package se.forsmark.visit;

public class DayListItem {
	private String start;
	private String seats;
	private String end;
	
	public DayListItem(String start, String seats, String end){
		this.start = start;
		this.seats = seats;
		this.end = end;
	}
	
	public String getStart(){
		return start;
	}
	
	public String getSeats(){
		return seats;
	}
	
	public String getEnd(){
		return end;
	}
}
