package se.forsmark.visit.Booking;

public class DayListItem {
	private String start;
	private String seats;
	private String end;
	private int id;
	
	public DayListItem(int id,String start, String seats, String end){
		this.start = start;
		this.seats = seats;
		this.end = end;
		this.id = id;
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
	
	public int getId(){
		return id;
	}

}
