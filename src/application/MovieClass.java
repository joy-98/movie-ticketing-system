package application;

import java.util.*;
import java.io.*;
import java.time.LocalDate;

public class MovieClass{

	//arraylist to store data from chosenmovie class
	public ArrayList<ChosenMovie> chosenarr = new ArrayList<>();
	//Variables
	private String ImagePath;
	private String MovieName;
	private String Genre;
	private String Language;
	private String Duration;
	
	//load from chosenMovie.dat
	public void load() {
		File chosenf = new File("chosenMovie.dat");
		try {
			chosenf.createNewFile();
		} catch (Exception e) {
		} 
		// Create a file if it doesn't exist
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream(chosenf));
			chosenarr = (ArrayList<ChosenMovie>) ois.readObject();
		} catch (Exception e) {
		}
	}

	//save into chosenMovie.dat
	public void save() {
		File chosenf = new File("chosenMovie.dat");
		try {
			chosenf.createNewFile();
		} catch (Exception e) {
		} 
		// Create a file if it doesn't exist
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(chosenf));
			//writing the into .dat as an array
			oos.writeObject(chosenarr);
		} catch (Exception e) {	
		}
	}
	
	//default constructor
	public MovieClass() {
		
	}
	
	public MovieClass(String path, String name, String genre, String language, String duration){
		
		ImagePath = path;
		MovieName = name;
		Genre = genre;
		Language = language;
		Duration = duration;
		
	}
	
	public void addArr(String movieName, String cinema, LocalDate date, String showtime, ArrayList<String> chosenseats) {
		load();

		ChosenMovie newarr = new ChosenMovie(movieName,cinema,date,showtime,chosenseats);
		chosenarr.add(newarr); // add into to arraylist
		save();
	}
	
	public String getPath() {
		
		return ImagePath;
		
	}
	
	public void setPath(String path) {
		
		ImagePath = path;
		
	}
	
	public String getMovieName() {
		
		return MovieName;
		
	}
	
	public void setMovieName(String name) {
		
		MovieName = name;
		
	}
	
	public String getGenre() {
		
		return Genre;
		
	}
	
	public void setGenre(String genre) {
		
		Genre = genre;
		
	}
	
	public String getLanguage() {
		
		return Language;
		
	}
	
	public void setLanguage(String language) {
		
		Language = language;
		
	}
	
	public String getDuration() {
		
		return Duration;
		
	}
	
	public void setDuration(String duration) {
		
		Duration = duration;
		
	}
	
}
