package se.forsmark.visit;

public class DayListItem {
	private String start;
	private String seats;
	private String end;
	
	public DayListItem(String st, String se, String e){
		start = st;
		seats = se;
		end = e;
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
