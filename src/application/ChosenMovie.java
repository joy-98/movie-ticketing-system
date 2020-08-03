package application;

import java.time.*;
import java.util.*;
import java.io.*;

public class ChosenMovie extends MovieClass implements Serializable{
	
	//Variables
	private String MovieName = super.getMovieName();
	private String Cinema;
	private LocalDate Date;
	private String Showtime;
	private ArrayList<String> chosenSeats = new ArrayList<String>();

	
	//Default constructor
	public ChosenMovie() {
		
	}
	
	public ChosenMovie(String path, String name, String genre, String language, String duration
			,String cinema, LocalDate date, String time) {
		super(path, name, genre, language, duration);
		Cinema = cinema;
		Date = date;
		Showtime = time;
	}
	
	public ChosenMovie(String movieName, String cinema, LocalDate date, String showtime, ArrayList<String> chosenseats) {
		MovieName = movieName;
		Cinema = cinema;
		Date = date;
		Showtime = showtime;
		chosenSeats = chosenseats;
	}
	
	//to check the movie seats
	public ArrayList<String> checkmovie(String MovieName, String Cinema, LocalDate Date, String Showtime) {
		
		ArrayList<String> arr = new ArrayList<String>();
		
		
		super.load();
		for(int i = 0; i<super.chosenarr.size();i++) {
			if(super.chosenarr.get(i).MovieName.equals(MovieName) && 
				super.chosenarr.get(i).Cinema.equals(Cinema) && 
				super.chosenarr.get(i).Date.equals(Date) && 
				super.chosenarr.get(i).Showtime.equals(Showtime)) {
				
				arr.addAll(super.chosenarr.get(i).chosenSeats);
				
			}
		}
		return arr;
	}

	
	
	public String getCinema() {
		return Cinema;
	}


	public void setCinema(String cinema) {
		Cinema = cinema;
	}


	public LocalDate getDate() {
		return Date;
	}


	public void setDate(LocalDate date) {
		Date = date;
	}


	public String getShowtime() {
		return Showtime;
	}


	public void setShowtime(String showtime) {
		Showtime = showtime;
	}
	
	public ArrayList<String> getSeats() {
		return chosenSeats;
	}
	
	public void setSeats(ArrayList<String> seats) {
		chosenSeats = seats;
	}
	


	
}
