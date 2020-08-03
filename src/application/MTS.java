package application;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.application.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCombination;


public class MTS extends Application{
	
	
	//Global Variable for every methods to excess
	private int bookingId = (int) (Math.random()*999999+0);
	private String usernameGlobal;
	private BorderPane homepagemainbp;
	private Button backbtn;
	
	//For Final Amount of Cost (From Booking() to Payment())
	private Text Tsubtotal = new Text(" 0.0");
	private Text Esubtotal = new Text(" 0.0");
	private Text totalCost = new Text("0.0");
	
	
	//For Implementing different Movie Time 
	private Text MovieName1 = new Text();
	private Text MovieName2 = new Text();
	private Text MovieName3 = new Text();
	private Text MovieName4 = new Text();
	private Text MovieName5 = new Text();
	private Text MovieName6 = new Text();
	private Text MovieName7 = new Text();
	
	//For Limiting the Number of Choosing seats
	private Text adultQuantity = new Text("0");
	private Text kidQuantity = new Text("0");
	private Text twinQuantity = new Text("0");
	//For Recommendation Page (Cinema Preferable)
	ComboBox<String> usercinemacb = new ComboBox<String>();
	
	
	//Calling the Model Class
	ChosenMovie chosenmovie = new ChosenMovie();
	
	
	//getting the email key-ed in by the user from the log in page
	public void setUsernameGlobal(String usernameGlobal) {
		this.usernameGlobal = usernameGlobal;
	}
	
	//Start Method
	public void start(Stage stage) {

		try {
			//calling login page
			Parent login = FXMLLoader.load(getClass().getResource("/application/Login.fxml"));
			
			//Scene
			Scene scene = new Scene(login);
			stage.setScene(scene);
			stage.setFullScreen(true);
			//disabling exit full screen hint message
			stage.setFullScreenExitHint("");
			//disabling esc key from exiting full screen
			stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	//Main Method
	public static void main(String[] args) {
		launch(args);
	}

	//Home Page
	public BorderPane homepage() {
		//GridPane to align the buttons
		GridPane gp = new GridPane();
		gp.setHgap(5);
		gp.setPadding(new Insets(15,15,15,15));
		
		//Movies Button
		Button moviebtn = new Button("Movies");
		gp.add(moviebtn,0,0);
		
		//Cinemas Button
		Button cinemasbtn = new Button("Cinemas");
		gp.add(cinemasbtn,1,0);
		
		//User Profile
		Button profilebtn = new Button("User Profile");
		gp.add(profilebtn,2,0);
		
		//Contact Us
		Button contactbtn = new Button("Contact Us");
		gp.add(contactbtn,3,0);
		
		//Log Out
		Button logoutbtn = new Button("Logout");
		gp.add(logoutbtn,4,0);
		
		//Back Button
		backbtn = new Button("Back");
		gp.add(backbtn,5,0);
		gp.setMargin(backbtn, new Insets(0,0,0,1150));
		
		//Pane for styling
		Pane p = new Pane();
		p.getChildren().add(gp);
		p.getStyleClass().add("MTSHomepane");
		
		
		//BorderPane for align
		homepagemainbp = new BorderPane();
		homepagemainbp.setTop(p);
		//homepagemainbp.setCenter(choosingShowtime());
		//temp
		homepagemainbp.setCenter(movies());
		
		// Bottom
		HBox bottom = new HBox();
		homepagemainbp.setBottom(bottom);
		bottom.setId("bottom");
		bottom.setPrefHeight(90);
		Text nothing = new Text("");
		bottom.getChildren().add(nothing);
		
		
		//Movies Button
		moviebtn.setOnAction(e->{
			homepagemainbp.setCenter(movies());
		});
		
		//Cinema Button 
		cinemasbtn.setOnAction(e->{
			homepagemainbp.setCenter(cinemas());
		});
		
		//User Profile Button
		profilebtn.setOnAction(e->{
			
			int index = UserManager.usermanager.getUserIndex(usernameGlobal);

			homepagemainbp.setCenter(userprofile(UserManager.usermanager.getUser(index)));
		});
		
		//Contact Us Button
		contactbtn.setOnAction(e->{
			homepagemainbp.setCenter(contactUs());
		});
		
		
		//Logout Button
		logoutbtn.setOnAction(e->{
			Alert logoutConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
			logoutConfirmation.setHeaderText("Are you sure do you want to Log Out?");
			logoutConfirmation.setContentText("Click OK to Log Out.");
			
			Optional<ButtonType> result = logoutConfirmation.showAndWait();
			if(result.get() == ButtonType.OK) {

				try {
					//calling login page
					Parent login = FXMLLoader.load(getClass().getResource("/application/Login.fxml"));
					Scene scene = new Scene(login);
					Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
					stage.setScene(scene);
					stage.setFullScreen(true);
					//disabling exit full screen hint message
					stage.setFullScreenExitHint("");
					//disabling esc key from exiting full screen
					stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
					stage.show();

				} catch (IOException ioe) {
					ioe.printStackTrace();
				}	
			}
		});
		
		return homepagemainbp;
	}
	
	//Movie Page
	public TabPane movies() {
		//Set backbtn to be invisible
		backbtn.setVisible(false);
		
		//Recommendation Page
		Hyperlink recPageLink = new Hyperlink("Can't decide?\nClick here for letting us to recommend you a movie.");
		recPageLink.setId("rechyperlink");
		recPageLink.setOnAction(e->{
			homepagemainbp.setCenter(recommendation());
		});
		recPageLink.setPadding(new Insets(0,0,10,0));
		
		//Movies
		
		//====================Movie Pic, Name & Desc================================
		
		//====================Movie 1 (A Simple Favour)=============================
		
		//HBox per line for VBox
		HBox movie1 = new HBox(20);
		
		//Image for movie
		Image NS1 = new Image(getClass().getResourceAsStream("/application/images/A Simple Favour.jpg"));
		ImageView Image1 = new ImageView();
		Image1.setImage(NS1);
		Image1.setFitWidth(220);
		Image1.setFitHeight(280);
		Image1.setSmooth(true);
		Image1.setCache(true);
		
		
		//VBox for Movie Name & Desc
		VBox movieDesc = new VBox(20);
		
		
		//Name for Movie
		MovieName1.setText("A Simple Favour");
		MovieName1.setFont(Font.font(null, FontWeight.BOLD, 35));
		MovieName1.setFill(Color.FIREBRICK);
		Hyperlink movielink1 = new Hyperlink();
		movielink1.setGraphic(MovieName1);
		movielink1.setOnAction(e->{
			try {
				homepagemainbp.setCenter(movieNS1());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		
		//Description for movie
		Text Desc1 = new Text();
		Desc1.setText("A SIMPLE FAVOR, directed by Paul Feig, centers around Stephanie (Anna Kendrick), "
				+ "a mommy vlogger who seeks to uncover the truth behind her best friend Emily's "
				+ "(Blake Lively) sudden disappearance from their small town.");
		Desc1.setFont(Font.font (null, 20));
		Desc1.setWrappingWidth(700);
		Desc1.setTextAlignment(TextAlignment.JUSTIFY);
		
		
		//Getting children per VBox for Movie Name & Desc
		movieDesc.getChildren().addAll(movielink1, Desc1);
		
		//Getting children per HBox
		movie1.getChildren().addAll(Image1, movieDesc);

		//====================Movie 2(Crazy Rich Asians)=============================
		
		//HBox per line for VBox
		HBox movie2 = new HBox(20);
				
		//Image for movie
		ImageView Image2 = new ImageView();
		Image NS2 = new Image(getClass().getResourceAsStream("/application/images/CRA.png"));
		Image2.setImage(NS2);
		Image2.setFitWidth(220);
		Image2.setFitHeight(280);
		Image2.setSmooth(true);
		Image2.setCache(true);
				
		//VBox for Movie Name & Desc
		VBox movie2Desc = new VBox(20);
				
				
		//Name for Movie
		MovieName2.setText("Crazy Rich Asians");
		MovieName2.setFont(Font.font(null, FontWeight.BOLD, 35));
		MovieName2.setFill(Color.FIREBRICK);
		Hyperlink movielink2 = new Hyperlink();
		movielink2.setGraphic(MovieName2);
		movielink2.setOnAction(e->{
			try {
				homepagemainbp.setCenter(movieNS2());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
				
		//Description for movie
		Text Desc2 = new Text();
		Desc2.setText("Three wealthy Chinese families prepare for the wedding of the year.");
		Desc2.setFont(Font.font (null, 20));
		Desc2.setWrappingWidth(700);
		Desc2.setTextAlignment(TextAlignment.JUSTIFY);
				
				
		//Getting children per VBox for Movie Name & Desc
		movie2Desc.getChildren().addAll(movielink2, Desc2);
				
		//Getting children per HBox
		movie2.getChildren().addAll(Image2, movie2Desc);


		//====================Movie 3(First Man)=============================
		
		//HBox per line for VBox
		HBox movie3 = new HBox(20);
				
		//Image for movie
		ImageView Image3 = new ImageView();
		Image NS3 = new Image(getClass().getResourceAsStream("/application/images/First Man.jpg"));
		Image3.setImage(NS3);
		Image3.setFitWidth(220);
		Image3.setFitHeight(280);
		Image3.setSmooth(true);
		Image3.setCache(true);
				
		//VBox for Movie Name & Desc
		VBox movie3Desc = new VBox(20);
				
				
		//Name for Movie
		MovieName3.setText("First Man");
		MovieName3.setFont(Font.font(null, FontWeight.BOLD, 35));
		MovieName3.setFill(Color.FIREBRICK);
		Hyperlink movielink3 = new Hyperlink();
		movielink3.setGraphic(MovieName3);
		movielink3.setOnAction(e->{
			try {
				homepagemainbp.setCenter(movieNS3());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		//Description for movie
		Text Desc3 = new Text();
		Desc3.setText("A look at the life of the astronaut, Neil Armstrong, and the legendary space mission"
				+ "that led him to become the first man to walk on the Moon on July 20, 1969.");
		Desc3.setFont(Font.font (null, 20));
		Desc3.setWrappingWidth(700);
		Desc3.setTextAlignment(TextAlignment.JUSTIFY);
				
				
		//Getting children per VBox for Movie Name & Desc
		movie3Desc.getChildren().addAll(movielink3, Desc3);
				
		//Getting children per HBox
		movie3.getChildren().addAll(Image3, movie3Desc);

		
		
		//====================Movie 4(Golden Job)=============================
		
		//HBox per line for VBox
		HBox movie4 = new HBox(20);
				
		//Image for movie
		ImageView Image4 = new ImageView();
		Image NS4 = new Image(getClass().getResourceAsStream("/application/images/Golden Job.jpg"));
		Image4.setImage(NS4);
		Image4.setFitWidth(220);
		Image4.setFitHeight(280);
		Image4.setSmooth(true);
		Image4.setCache(true);
				
		//VBox for Movie Name & Desc
		VBox movie4Desc = new VBox(20);
				
				
		//Name for Movie
		MovieName4.setText("Golden Job");
		MovieName4.setFont(Font.font(null, FontWeight.BOLD, 35));
		MovieName4.setFill(Color.FIREBRICK);
		Hyperlink movielink4 = new Hyperlink();
		movielink4.setGraphic(MovieName4);
		movielink4.setOnAction(e->{
			try {
				homepagemainbp.setCenter(movieNS4());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		//Description for movie
		Text Desc4 = new Text();
		Desc4.setText("Five con artists are planning to retire soon. However, while they are throwing an innocent"
				+ "bachelor party, they unwittingly get caught up in a heist planned by"
				+ "an international drug lord.");
		Desc4.setFont(Font.font (null, 20));
		Desc4.setWrappingWidth(700);
		Desc4.setTextAlignment(TextAlignment.JUSTIFY);
				
				
		//Getting children per VBox for Movie Name & Desc
		movie4Desc.getChildren().addAll(movielink4, Desc4);
				
		//Getting children per HBox
		movie4.getChildren().addAll(Image4, movie4Desc);

		
		//====================Movie 5(Malicious)=============================
		
		//HBox per line for VBox
		HBox movie5 = new HBox(20);
				
		//Image for movie
		ImageView Image5 = new ImageView();
		Image NS5 = new Image(getClass().getResourceAsStream("/application/images/Malicious.jpg"));
		Image5.setImage(NS5);
		Image5.setFitWidth(220);
		Image5.setFitHeight(280);
		Image5.setSmooth(true);
		Image5.setCache(true);
				
		//VBox for Movie Name & Desc
		VBox movie5Desc = new VBox(20);
				
				
		//Name for Movie
		MovieName5.setText("Malicious");
		MovieName5.setFont(Font.font(null, FontWeight.BOLD, 35));
		MovieName5.setFill(Color.FIREBRICK);
		Hyperlink movielink5 = new Hyperlink();
		movielink5.setGraphic(MovieName5);
		movielink5.setOnAction(e->{
			try {
				homepagemainbp.setCenter(movieNS5());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		//Description for movie
		Text Desc5 = new Text();
		Desc5.setText("A couple in an unstable relationship become stalked by an unseen adversary who threatens "
				+ "the normalcy of their home life. As tensions and fears begin to escalate, the line becomes "
				+ "blurred on whether the true danger remains on the outside of their home, or on the inside.");
		Desc5.setFont(Font.font (null, 20));
		Desc5.setWrappingWidth(700);
		Desc5.setTextAlignment(TextAlignment.JUSTIFY);
				
				
		//Getting children per VBox for Movie Name & Desc
		movie5Desc.getChildren().addAll(movielink5, Desc5);
				
		//Getting children per HBox
		movie5.getChildren().addAll(Image5, movie5Desc);

		
		//====================Movie 6(Paskal)=============================
		
		//HBox per line for VBox
		HBox movie6 = new HBox(20);
				
		//Image for movie
		ImageView Image6 = new ImageView();
		Image NS6 = new Image(getClass().getResourceAsStream("/application/images/Paskal.jpg"));
		Image6.setImage(NS6);
		Image6.setFitWidth(220);
		Image6.setFitHeight(280);
		Image6.setSmooth(true);
		Image6.setCache(true);
				
		//VBox for Movie Name & Desc
		VBox movie6Desc = new VBox(20);
				
				
		//Name for Movie
		MovieName6.setText("Paskal");
		MovieName6.setFont(Font.font(null, FontWeight.BOLD, 35));
		MovieName6.setFill(Color.FIREBRICK);
		Hyperlink movielink6 = new Hyperlink();
		movielink6.setGraphic(MovieName6);
		movielink6.setOnAction(e->{
			try {
				homepagemainbp.setCenter(movieNS6());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
				
		//Description for movie
		Text Desc6 = new Text();
		Desc6.setText("PASKAL, or Pasukan Khas Laut, is an elite unit in the Royal Malaysian Navy. The"
				+ "movie follows the true events of PASKAL's Lieutenant Commander Arman Anwar and"
				+ "his team's mission to rescue a tanker, MV Bunga Laurel, that"
				+ "was hijacked by Somalian Pirates in 2011.");
		Desc6.setFont(Font.font (null, 20));
		Desc6.setWrappingWidth(700);
		Desc6.setTextAlignment(TextAlignment.JUSTIFY);
				
				
		//Getting children per VBox for Movie Name & Desc
		movie6Desc.getChildren().addAll(movielink6, Desc6);
				
		//Getting children per HBox
		movie6.getChildren().addAll(Image6, movie6Desc);

		
		//====================Movie 7(Venom)=============================
		
		//HBox per line for VBox
		HBox movie7 = new HBox(20);
				
		//Image for movie
		ImageView Image7 = new ImageView();
		Image NS7 = new Image(getClass().getResourceAsStream("/application/images/Venom.jpg"));
		Image7.setImage(NS7);
		Image7.setFitWidth(220);
		Image7.setFitHeight(280);
		Image7.setSmooth(true);
		Image7.setCache(true);
				
		//VBox for Movie Name & Desc
		VBox movie7Desc = new VBox(20);
				
				
		//Name for Movie
		MovieName7.setText("Venom");
		MovieName7.setFont(Font.font(null, FontWeight.BOLD, 35));
		MovieName7.setFill(Color.FIREBRICK);
		Hyperlink movielink7 = new Hyperlink();
		movielink7.setGraphic(MovieName7);
		movielink7.setOnAction(e->{
			try {
				homepagemainbp.setCenter(movieNS7());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		//Description for movie
		Text Desc7 = new Text();
		Desc7.setText("One of Marvel's most enigmatic, complex and badass characters comes to the big"
				+ "screen, starring Academy Award nominated actor Tom Hardy as the"
				+ "lethal protector Venom.");
		Desc7.setFont(Font.font (null, 20));
		Desc7.setWrappingWidth(700);
		Desc7.setTextAlignment(TextAlignment.JUSTIFY);
				
				
		//Getting children per VBox for Movie Name & Desc
		movie7Desc.getChildren().addAll(movielink7, Desc7);
				
		//Getting children per HBox
		movie7.getChildren().addAll(Image7, movie7Desc);

		
		//====================Creating the layout==============================
		
		//Ultimate VBox for All Movies
		VBox vb = new VBox(8);
		vb.getChildren().addAll(recPageLink, movie1, movie2, movie3, movie4, movie5, movie6, movie7);
		vb.setSpacing(10);
		vb.setPadding(new Insets(20,0,0,0));
		
		
		//ScrollPane for VBox
		ScrollPane sp = new ScrollPane();
		sp.setContent(vb);
		
		//TabPane
		TabPane tp = new TabPane();
		tp.setId("tabpane");
		Tab nowshowingtab = new Tab();
		nowshowingtab.setText("Now Showing");
		nowshowingtab.setClosable(false);
		Tab comingsoontab = new Tab();
		comingsoontab.setText("Coming Soon");
		comingsoontab.setClosable(false);
		tp.getTabs().add(nowshowingtab);
		tp.getTabs().add(comingsoontab);
		nowshowingtab.setContent(sp);
		comingsoontab.setContent(comingSoonMovies());
		
				
		return tp;
	}
	
	//Coming Soon Tab
	public ScrollPane comingSoonMovies() {
		
		//====================Movie Pic, Name & Desc================================
		
		//====================Movie 1 (Bohemian Rhapsody)=============================
		
		//HBox per line for VBox
		HBox movie1 = new HBox(20);
		
		//Image for movie
		Image CS1 = new Image(getClass().getResourceAsStream("/application/images/Bohemian Rhapsody.jpg"));
		ImageView Image1 = new ImageView();
		Image1.setImage(CS1);
		Image1.setFitWidth(220);
		Image1.setFitHeight(280);
		Image1.setSmooth(true);
		Image1.setCache(true);
		
		
		//VBox for Movie Name & Desc
		VBox movieDesc = new VBox(20);
		
		
		//Name for Movie
		Text Name1 = new Text();
		Name1.setText("Bohemian Rhapsody");
		Name1.setFont(Font.font(null, FontWeight.BOLD, 35));
		Name1.setFill(Color.FIREBRICK);
		Hyperlink movielink1 = new Hyperlink();
		movielink1.setGraphic(Name1);
		movielink1.setOnAction(e->{
			try {
				homepagemainbp.setCenter(movieCS1());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		
		//Description for movie
		Text Desc1 = new Text();
		Desc1.setText("Bohemian Rhapsody is a foot-stomping celebration of Queen, their music "
				+ "and their extraordinary lead singer Freddie Mercury.");
		Desc1.setFont(Font.font (null, 20));
		Desc1.setWrappingWidth(700);
		Desc1.setTextAlignment(TextAlignment.JUSTIFY);
		
		
		//Getting children per VBox for Movie Name & Desc
		movieDesc.getChildren().addAll(movielink1, Desc1);
		
		//Getting children per HBox
		movie1.getChildren().addAll(Image1, movieDesc);

		//====================Movie 2(Fantastic Beasts 2)=============================
		
		//HBox per line for VBox
		HBox movie2 = new HBox(20);
				
		//Image for movie
		ImageView Image2 = new ImageView();
		Image CS2 = new Image(getClass().getResourceAsStream("/application/images/Fantastic Beasts 2.jpg"));
		Image2.setImage(CS2);
		Image2.setFitWidth(220);
		Image2.setFitHeight(280);
		Image2.setSmooth(true);
		Image2.setCache(true);
				
		//VBox for Movie Name & Desc
		VBox movie2Desc = new VBox(20);
				
				
		//Name for Movie
		Text Name2 = new Text();
		Name2.setText("Fantastic Beasts 2");
		Name2.setFont(Font.font(null, FontWeight.BOLD, 35));
		Name2.setFill(Color.FIREBRICK);
		Hyperlink movielink2 = new Hyperlink();
		movielink2.setGraphic(Name2);
		movielink2.setOnAction(e->{
			try {
				homepagemainbp.setCenter(movieCS2());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
				
		//Description for movie
		Text Desc2 = new Text();
		Desc2.setText("Fantastic Beasts: The Crimes of Grindelwald is the second of five all"
				+ " new adventures in J.K. Rowling's Wizarding World.");
		Desc2.setFont(Font.font (null, 20));
		Desc2.setWrappingWidth(700);
		Desc2.setTextAlignment(TextAlignment.JUSTIFY);
				
				
		//Getting children per VBox for Movie Name & Desc
		movie2Desc.getChildren().addAll(movielink2, Desc2);
				
		//Getting children per HBox
		movie2.getChildren().addAll(Image2, movie2Desc);

		
		//====================Movie 3(The Grinch)=============================
		
		//HBox per line for VBox
		HBox movie3 = new HBox(20);
						
		//Image for movie
		ImageView Image3 = new ImageView();
		Image CS3 = new Image(getClass().getResourceAsStream("/application/images/The Grinch.jpg"));
		Image3.setImage(CS3);
		Image3.setFitWidth(220);
		Image3.setFitHeight(280);
		Image3.setSmooth(true);
		Image3.setCache(true);
						
		//VBox for Movie Name & Desc
		VBox movie3Desc = new VBox(20);
						
						
		//Name for Movie
		Text Name3 = new Text();
		Name3.setText("The Grinch");
		Name3.setFont(Font.font(null, FontWeight.BOLD, 35));
		Name3.setFill(Color.FIREBRICK);
		Hyperlink movielink3 = new Hyperlink();
		movielink3.setGraphic(Name3);
		movielink3.setOnAction(e->{
			try {
				homepagemainbp.setCenter(movieCS3());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
						
		//Description for movie
		Text Desc3 = new Text();
		Desc3.setText("A grumpy Grinch plots to ruin Christmas for the village of Whoville.");
		Desc3.setFont(Font.font (null, 20));
		Desc3.setWrappingWidth(700);
		Desc3.setTextAlignment(TextAlignment.JUSTIFY);
						
						
		//Getting children per VBox for Movie Name & Desc
		movie3Desc.getChildren().addAll(movielink3, Desc3);
						
		//Getting children per HBox
		movie3.getChildren().addAll(Image3, movie3Desc);


		
		//====================Creating the layout==============================
		
		//Ultimate VBox for All Movies
		VBox vb = new VBox(8);
		vb.getChildren().addAll(movie1, movie2, movie3);
		vb.setSpacing(10);
		vb.setPadding(new Insets(20,0,0,0));
		
		
		//ScrollPane for VBox
		ScrollPane sp = new ScrollPane();
		sp.setContent(vb);
		
		return sp;
	}
	
	//Choosing Seats Page
	public BorderPane choosingSeats() {
		//setting back button to be visible
		backbtn.setVisible(true);
		//Back Button
		backbtn.setOnAction(e->{
			homepagemainbp.setCenter(booking());
		});
		
		
		//Proceed Button
		Button proceedbtn = new Button("Proceed");
		proceedbtn.setPrefSize(150, 70);
		proceedbtn.setDisable(true);
		proceedbtn.setOnAction(e->{
			homepagemainbp.setCenter(payment());
		});

	
		
		//Movie Description
		//GridPane for Movie Description
		GridPane gp = new GridPane();
		gp.setVgap(5);
		gp.setHgap(10);
		
		//Movie Poster
		ImageView movieposteriv = new ImageView();
		Image movieposterimg = new Image(getClass().getResourceAsStream(chosenmovie.getPath()));
		movieposteriv.setImage(movieposterimg);
		movieposteriv.setFitHeight(350);
		movieposteriv.setPreserveRatio(true);
		gp.add(movieposteriv, 0, 0);
		gp.setRowSpan(movieposteriv, 10);
		
		
		Label movienamelbl1 = new Label("Movie: ");
		Label movienamelbl2 = new Label(chosenmovie.getMovieName());
		gp.add(movienamelbl1, 1, 0);
		gp.add(movienamelbl2, 2, 0);
		gp.setColumnSpan(movienamelbl2, 2);
		
		
		//Cinema
		Label moviecinemalbl1 = new Label("Cinema: ");
		Label moviecinemalbl2 = new Label(chosenmovie.getCinema());
		gp.add(moviecinemalbl1, 1, 1);
		gp.add(moviecinemalbl2, 2, 1);
		gp.setColumnSpan(moviecinemalbl2, 2);
		
		//Date
		Label movietimelbl1 = new Label("Date: ");
		Label movietimelbl2 = new Label(chosenmovie.getShowtime());
		gp.add(movietimelbl1, 1, 2);
		gp.add(movietimelbl2, 2, 2);
		gp.setColumnSpan(movietimelbl2, 2);
		
		//Time
		Label moviedatelbl1 = new Label("Time: ");
		//convert LocalDate to String
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String convertedDate = chosenmovie.getDate().format(formatter);
		Label moviedatelbl2 = new Label(convertedDate);
		gp.add(moviedatelbl1, 1, 3);
		gp.add(moviedatelbl2, 2, 3);
		gp.setColumnSpan(moviedatelbl2, 2);
		
		
		//VBox for seats description align
		VBox seatsvb = new VBox();
		seatsvb.setSpacing(5);
		seatsvb.setPadding(new Insets(15,15,15,15));
		
		//GridPane for legends
		GridPane gp1 = new GridPane();
		gp1.setVgap(5);
		gp1.setHgap(5);
		gp1.setPadding(new Insets(0,0,15,0));
		seatsvb.getChildren().add(gp1);
		
		
		//Unavailable
		Image unavimg = new Image(getClass().getResourceAsStream("/application/images/seatunavailable.png"));
		ImageView unaviv = new ImageView();
		unaviv.setImage(unavimg);
		unaviv.setFitHeight(50);
		unaviv.setPreserveRatio(true);
		Text unavtxt = new Text("Unavailable");
		unavtxt.getStyleClass().add("seatslabel");
		gp1.add(unaviv,0,0);
		gp1.add(unavtxt,1,0);
		
		//Available
		Image avimg = new Image(getClass().getResourceAsStream("/application/images/seat.png"));
		ImageView aviv = new ImageView();
		aviv.setImage(avimg);
		aviv.setFitHeight(50);
		aviv.setPreserveRatio(true);	
		Text avtxt = new Text("Available");
		avtxt.getStyleClass().add("seatslabel");
		gp1.add(aviv, 0, 1);
		gp1.add(avtxt, 1, 1);
		
		Image scimg = new Image(getClass().getResourceAsStream("/application/images/seatselected.png"));
		ImageView sciv = new ImageView();
		sciv.setImage(scimg);
		sciv.setFitHeight(50);
		sciv.setPreserveRatio(true);
		Text sctxt = new Text("Selected");
		sctxt.getStyleClass().add("seatslabel");
		gp1.add(sciv, 0, 2);
		gp1.add(sctxt, 1, 2);
		
		//Seats Selected
		Label selectedseatslbl1 = new Label("Selected Seats: ");
		Label selectedseatslbl2 = new Label();
		selectedseatslbl2.setWrapText(true);
		selectedseatslbl2.setPrefWidth(200);
		selectedseatslbl2.setTextAlignment(TextAlignment.JUSTIFY);
		selectedseatslbl1.getStyleClass().add("seatslabel");
		selectedseatslbl2.getStyleClass().add("seatslabel");
		seatsvb.getChildren().add(selectedseatslbl1);
		seatsvb.getChildren().add(selectedseatslbl2);
		
		
		//show screen
		VBox vb = new VBox();
		vb.setSpacing(20);
		Label screen = new Label("Screen");
		screen.getStyleClass().add("screen");
		vb.getChildren().add(screen);
		
		
		//choose seats
		HBox[] hb = new HBox[5];
		int seatscount = 0;
		ToggleButton[] seat = new ToggleButton[40];
		
		//Selected seats array
		ArrayList<String> seatsarr = new ArrayList<String>();
		ArrayList<String> normalseatsarr = new ArrayList<String>();
		ArrayList<String> coupleseatsarr = new ArrayList<String>();
		
		//Get the quantity of people
		int adultquan = Integer.parseInt(adultQuantity.getText());
		int kidquan = Integer.parseInt(kidQuantity.getText());
		int adultAndKidquan = adultquan + kidquan;
		int twinquan = Integer.parseInt(twinQuantity.getText());
		int totalquan = adultAndKidquan + twinquan;
		
		for(int j=0; j<hb.length;j++) {
			hb[j] = new HBox();
			hb[j].setSpacing(10);
			hb[j].setAlignment(Pos.CENTER);
			for(int i=0; i<8;i++) {
				//create toggle button
				seat[seatscount] = new ToggleButton();
				//setting numbers into toggle button
				seat[seatscount].setText(Integer.toString(seatscount+1));
				hb[j].getChildren().add(seat[seatscount]);
				
				//event handler for each seats
				int tmpseatscount = seatscount;
				seat[seatscount].setOnAction(e->{
					String seatsnum = seat[tmpseatscount].getText();
					if(seat[tmpseatscount].isSelected()) {
						normalseatsarr.add(seatsnum);
						seatsarr.add(seatsnum);
						//limit the number of the seats to be chosen according to the amount from booking()
						if(normalseatsarr.size()>adultAndKidquan) {
							seat[tmpseatscount].setSelected(false);
							normalseatsarr.remove(seatsnum);
							seatsarr.remove(seatsnum);				
						}
					}
					if(!seat[tmpseatscount].isSelected()) {
						normalseatsarr.remove(seatsnum);
						seatsarr.remove(seatsnum);
					}
					selectedseatslbl2.setText(seatsarr.toString());
					
					//check if the user has chosen the seats
					if(seatsarr.size() >= totalquan) {
						proceedbtn.setDisable(false);
					} else {
						proceedbtn.setDisable(true);
					}
					
				});
				//check if any seats for the movie, date, time was booked
				if(chosenmovie.checkmovie(chosenmovie.getMovieName(), chosenmovie.getCinema(), 
						chosenmovie.getDate(), chosenmovie.getShowtime()).contains(seat[seatscount].getText())) {
					//making the seats to red
					seat[seatscount].setDisable(true);
				}
				seatscount++;
				
				
				
			}
			vb.getChildren().add(hb[j]);
		}
		
		
		
		//Couple Seats
		HBox couplehb = new HBox();
		couplehb.setSpacing(20);
		couplehb.setAlignment(Pos.CENTER);
		ToggleButton[] coupleseat = new ToggleButton[5];
		for(int i=0; i<5; i++) {
			coupleseat[i] = new ToggleButton();
			coupleseat[i].setText(Integer.toString(seatscount+1));
			coupleseat[i].getStyleClass().add("coupleToggleButton");
			couplehb.getChildren().add(coupleseat[i]);
			//event handler for each seats
			int tmpseatscount = i;
			coupleseat[i].setOnAction(e->{
				String seatsnum = coupleseat[tmpseatscount].getText();
				if(coupleseat[tmpseatscount].isSelected()) {
					coupleseatsarr.add(seatsnum);
					seatsarr.add(seatsnum);
					//limit the number of the seats to be chosen according to the amount from booking()
					if(coupleseatsarr.size()>twinquan) {
						coupleseat[tmpseatscount].setSelected(false);
						coupleseatsarr.remove(seatsnum);
						seatsarr.remove(seatsnum);
					}
				}
				if(!coupleseat[tmpseatscount].isSelected()) {
					coupleseatsarr.remove(seatsnum);
					seatsarr.remove(seatsnum);
				}
				
				selectedseatslbl2.setText(seatsarr.toString());
				
				//check if the user has chosen the seats
				if(seatsarr.size() >= totalquan) {
					proceedbtn.setDisable(false);
				} else {
					proceedbtn.setDisable(true);
				}
				
			});
			//check if any seats for the movie, date, time was booked
			if(chosenmovie.checkmovie(chosenmovie.getMovieName(), chosenmovie.getCinema(), 
					chosenmovie.getDate(), chosenmovie.getShowtime()).contains(coupleseat[i].getText())) {
				//making the seats to red
				coupleseat[i].setDisable(true);
			}
			seatscount++;
		}

		chosenmovie.setSeats(seatsarr);
		vb.getChildren().add(couplehb);
		vb.setAlignment(Pos.CENTER);
		
				
	
		//BorderPane for alignment
		BorderPane bp = new BorderPane();
		bp.setTop(gp);
		bp.setRight(seatsvb);
		bp.setCenter(vb);
		bp.setBottom(proceedbtn);
		bp.setAlignment(proceedbtn, Pos.BOTTOM_RIGHT);
		bp.setPadding(new Insets(20,20,20,20));
		
		
		return bp;
	}

	//Choosing Cinema, Date, Showtime Page
	public BorderPane choosingShowtime(){
		//setting back button to be visible
		backbtn.setVisible(true);
		//Back Button
		backbtn.setOnAction(e->{
			homepagemainbp.setCenter(movies());
		});
				
		//BorderPane for align
		BorderPane bp = new BorderPane();
		bp.setPadding(new Insets(20,20,20,20));
		
		//Proceed Button
		Button proceedbtn = new Button("Proceed");
		proceedbtn.setPrefSize(150, 70);
		proceedbtn.setDisable(true);
		bp.setBottom(proceedbtn);
		bp.setAlignment(proceedbtn, Pos.BOTTOM_RIGHT);
		proceedbtn.setOnAction(e->{
			homepagemainbp.setCenter(booking());
		});
		
		//HBoxPane for Movie Description
		HBox hb = new HBox();
		hb.setPadding(new Insets(20,20,20,20));
		bp.setTop(hb);
		
		//Movie Poster
		ImageView movieposteriv = new ImageView();
		Image movieposterimg = new Image(getClass().getResourceAsStream(chosenmovie.getPath()));
		movieposteriv.setImage(movieposterimg);
		movieposteriv.setFitHeight(500);
		movieposteriv.setPreserveRatio(true);
		hb.getChildren().add(movieposteriv);
		
		
		//VBox for descriptions
		GridPane gp = new GridPane();
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setPadding(new Insets(10,10,10,10));
		hb.getChildren().add(gp);
		
		//Movie title
		Label movienamelbl1 = new Label("Movie: ");
		Label movienamelbl2 = new Label(chosenmovie.getMovieName());
		gp.add(movienamelbl1,0,0);
		gp.add(movienamelbl2,1,0);
		
		//Genre
		Label moviegenrelbl1 = new Label("Genre: ");
		Label moviegenrelbl2 = new Label(chosenmovie.getGenre());
		gp.add(moviegenrelbl1,0,1);
		gp.add(moviegenrelbl2,1,1);
		
		//Language
		Label movielanglbl1 = new Label("Language: ");
		Label movielanglbl2 = new Label(chosenmovie.getLanguage());
		gp.add(movielanglbl1,0,2);
		gp.add(movielanglbl2,1,2);
		
		//Duration
		Label moviedurationlbl1 = new Label("Duration: ");
		Label moviedurationlbl2 = new Label(chosenmovie.getDuration());
		gp.add(moviedurationlbl1,0,3);
		gp.add(moviedurationlbl2,1,3);	
		
		//GridPane for User to Choose
		GridPane gp2 = new GridPane();
		gp2.setHgap(20);
		gp2.setVgap(100);
		gp2.setAlignment(Pos.CENTER);
		bp.setCenter(gp2);
		
		
		//Cinema
		Label usercinemalbl = new Label("Cinema");
		usercinemalbl.setId("showtimelbl");
		usercinemacb.setId("showtimecb");
		usercinemacb.getItems().clear();
		usercinemacb.getItems().addAll("One Utama", "Suria KLCC" , "Mid Valley" , "Sunway Pyramid");
		gp2.add(usercinemalbl,0,0);
		gp2.add(usercinemacb, 1, 0);
		
		//Date
		Label userdatelbl = new Label("Date");
		userdatelbl.setId("showtimelbl");
		ComboBox<LocalDate> datescb = new ComboBox<LocalDate>();
		datescb.setId("showtimecb");
		//get Today's date
		LocalDate now = LocalDate.now();
		//get tmr's date
		LocalDate tmr = now.plusDays(1);
		//get the day after's date
		LocalDate dayafter = tmr.plusDays(1);
		datescb.getItems().addAll(now,tmr,dayafter);
		gp2.add(userdatelbl, 2, 0);
		gp2.add(datescb,3,0);
		
		
		//Showtime
		Label usershowtimelbl = new Label("Showtime");
		usershowtimelbl.setId("showtimelbl");
		ComboBox<String> usershowtimecb = new ComboBox<String>();
		usershowtimecb.setId("showtimecb");
		
		//setting time each movie
		if (chosenmovie.getMovieName().equals(MovieName1.getText())) {
			usershowtimecb.getItems().clear();
			usershowtimecb.getItems().addAll("12:15", "17:00" , "21:45");
		}
		if (chosenmovie.getMovieName().equals(MovieName2.getText())) {
			usershowtimecb.getItems().clear();
			usershowtimecb.getItems().addAll("10:00", "15:45" , "18:00");
		}
		if (chosenmovie.getMovieName().equals(MovieName3.getText())) {
			usershowtimecb.getItems().clear();
			usershowtimecb.getItems().addAll("9:40", "13:00" , "19:20");
		}
		if (chosenmovie.getMovieName().equals(MovieName4.getText())) {
			usershowtimecb.getItems().clear();
			usershowtimecb.getItems().addAll("14:55", "16:30" , "21:40");
		}
		if (chosenmovie.getMovieName().equals(MovieName5.getText())) {
			usershowtimecb.getItems().clear();
			usershowtimecb.getItems().addAll("13:25", "22:00" , "23:30");
		}
		if (chosenmovie.getMovieName().equals(MovieName6.getText())) {
			usershowtimecb.getItems().clear();
			usershowtimecb.getItems().addAll("10:30", "14:30" , "20:45");
		}
		if (chosenmovie.getMovieName().equals(MovieName7.getText())) {
			usershowtimecb.getItems().clear();
			usershowtimecb.getItems().addAll("11:00", "14:00", "18:30", "22:30");
		}
		

		
		gp2.add(usershowtimelbl, 4, 0);
		gp2.add(usershowtimecb, 5, 0);
			

		//event handler
		usercinemacb.setOnAction(e->{
			if(!usercinemacb.getSelectionModel().isEmpty() && !datescb.getSelectionModel().isEmpty() && !usershowtimecb.getSelectionModel().isEmpty()) {
				proceedbtn.setDisable(false);
			}
			chosenmovie.setCinema(usercinemacb.getValue());
		});

		datescb.setOnAction(e->{
			if(!usercinemacb.getSelectionModel().isEmpty() && !datescb.getSelectionModel().isEmpty() && !usershowtimecb.getSelectionModel().isEmpty()) {
				proceedbtn.setDisable(false);
			}
			chosenmovie.setDate(datescb.getValue());
		});
		
		usershowtimecb.setOnAction(e->{
			if(!usercinemacb.getSelectionModel().isEmpty() && !datescb.getSelectionModel().isEmpty() && !usershowtimecb.getSelectionModel().isEmpty()) {
				proceedbtn.setDisable(false);
			}
			chosenmovie.setShowtime(usershowtimecb.getValue());
		});
	 
		return bp;
	}
	
	//Recommendation Page
	public BorderPane recommendation() {
		//setting back button to be invisible
		backbtn.setVisible(true);
		backbtn.setOnAction(e->{
			homepagemainbp.setCenter(movies());
		});

		//BorderPane for align
		BorderPane main = new BorderPane();	
			
		// Questions(Left Side)
		GridPane questionPage = new GridPane();
		main.setLeft(questionPage);
		questionPage.setId("questionPage");
		questionPage.setPadding(new Insets(50,0,50,50));

		// First VBox(Q1,Q2,Q3) ________________________________________________________________________________________
		VBox firstColQ = new VBox();
		questionPage.add(firstColQ, 0, 1);
		firstColQ.setId("firstColQ");
		firstColQ.setPrefSize(600, 800);
		firstColQ.setAlignment(Pos.TOP_CENTER);
		firstColQ.setPadding(new Insets(0,50,50,50));
		
		// Question1
		GridPane Q1 = new GridPane();
		firstColQ.getChildren().add(Q1);
		Q1.setPadding(new Insets(50,0,0,0));
		Text question1 = new Text("1. What is your prefered Duration?");
		question1.setId("question");
		FlowPane flowRadio = new FlowPane();
		ToggleGroup group = new ToggleGroup();
		RadioButton L2 = new RadioButton("<2 hours");
		RadioButton R2 = new RadioButton("2 hours>");
		flowRadio.getChildren().addAll(L2,R2);
		flowRadio.setHgap(15);
		L2.setToggleGroup(group);
		L2.setId("question");
		R2.setToggleGroup(group);
		R2.setId("question");
		Q1.add(question1, 0, 0);
		Q1.add(flowRadio, 0, 1);
		Q1.setVgap(10);
		Q1.setPadding(new Insets(100,0,0,0));
		Q1.setMargin(flowRadio, new Insets(0,0,0,25));
		
		// Question 2
		GridPane Q2 = new GridPane();
		Text question2 = new Text("2. Who did you come with?");
		question2.setId("question");
		firstColQ.getChildren().add(Q2);
		ComboBox<String> comboQ2 = new ComboBox<>();
		comboQ2.getItems().addAll("With Children","Without Children");
		comboQ2.setValue("WithChildren");
		Q2.add(question2, 0, 0);
		Q2.add(comboQ2, 0, 1);
		Q2.setVgap(10);
		Q2.setPadding(new Insets(100,0,0,0));
		Q2.setMargin(comboQ2, new Insets(0,0,0,25));
		
		// Question 3
		GridPane Q3 = new GridPane();
		Text question3 = new Text("3. How many person is watching?");
		question3.setId("question");
		firstColQ.getChildren().add(Q3);
		ComboBox<String> comboQ3 = new ComboBox<>();
		comboQ3.getItems().addAll("1","2","3","4","5","6");
		comboQ3.setValue("1");
		//to booking()
		comboQ3.setOnAction(e->{
			adultQuantity.setText(comboQ3.getValue());
		});
		Q3.add(question3, 0, 0);
		Q3.add(comboQ3, 0, 1);
		Q3.setVgap(10);
		Q3.setPadding(new Insets(100,0,0,0));
		Q3.setMargin(comboQ3, new Insets(0,0,0,25));
		 //End firstCol________________________________________________________________________________________________________
		
		
		// Second VBox (Q4,Q5,Q6)______________________________________________________________________________________________
		VBox secondColQ = new VBox();
		questionPage.add(secondColQ, 1, 1);
		secondColQ.setId("secondColQ");
		secondColQ.setPrefSize(600, 800);
		secondColQ.setAlignment(Pos.TOP_CENTER);
		
		GridPane Q4 = new GridPane();
		Text question4 = new Text("4. Which cinema do you prefer?");
		question4.setId("question");
		secondColQ.getChildren().add(Q4);
		ComboBox<String> comboQ4 = new ComboBox<>();
		comboQ4.getItems().addAll("KLCC","Sunway Pyramid","One Utama","Mid Valley");
		comboQ4.setValue("KLCC");
		comboQ4.setOnAction(e->{
			usercinemacb.setValue(comboQ4.getValue());
			chosenmovie.setCinema(comboQ4.getValue());
		});
		Q4.add(question4, 0, 0);
		Q4.add(comboQ4, 0, 1);
		Q4.setVgap(10);
		Q4.setPadding(new Insets(100,0,0,0));
		Q4.setMargin(comboQ4, new Insets(0,0,0,25));
		
		GridPane Q5 = new GridPane();
		Text question5 = new Text("5. Language of movies?");
		question5.setId("question");
		secondColQ.getChildren().add(Q5);
		FlowPane flowLanguage = new FlowPane();
		CheckBox chinese = new CheckBox("Chinese");
		CheckBox english= new CheckBox("English");
		CheckBox malay = new CheckBox("Malay");
		Q5.add(question5, 0, 0);
		Q5.add(flowLanguage, 0, 1);
		Q5.setVgap(10);
		Q5.setPadding(new Insets(100,0,0,0));
		Q5.setMargin(flowLanguage, new Insets(0,0,0,25));
		flowLanguage.getChildren().addAll(chinese,english,malay);
		flowLanguage.setHgap(10);
		chinese.setId("question");
		english.setId("question");
		malay.setId("question");
		
		
		GridPane Q6 = new GridPane();
		Text question6 = new Text("6. What genre of movies do you prefer?");
		question6.setId("question");
		secondColQ.getChildren().add(Q6);
		ComboBox<String> comboQ6 = new ComboBox<>();
		comboQ6.getItems().addAll("Comedy","Biography","Horror","Crime","Action");
		comboQ6.setValue("Comedy");
		Q6.add(question6, 0, 0);
		Q6.add(comboQ6, 0, 1);
		Q6.setVgap(10);
		Q6.setPadding(new Insets(100,0,0,0));
		Q6.setMargin(comboQ6, new Insets(0,0,0,25));
		
		//End secondCol________________________________________________________________________________________________________
		
		
		// Center Button
		VBox forButton = new VBox();
		forButton.setSpacing(5);
		Button All = new Button("  All  ");
		Button filter = new Button("Filter");
		filter.setId("filter");
		All.setId("filter");
		main.setCenter(forButton);
		forButton.setId("forButton");
		forButton.getChildren().addAll(All,filter);
		forButton.setAlignment(Pos.BOTTOM_CENTER);;
		forButton.setPadding(new Insets(0,0,70,0));
		
		
		// Right Part (Movie Details) with Scroll Pane
		VBox movieRight = new VBox();
		Text movieTxt = new Text("Movies");
		movieRight.getChildren().add(movieTxt);
		movieRight.setId("movieRight");
		movieTxt.setId("movieTxt");
		movieRight.setPadding(new Insets(15,0,0,30));
		movieRight.setPrefWidth(550);	
		
		GridPane MovieLayout = new GridPane();
		main.setRight(movieRight);
		movieRight.getChildren().add(MovieLayout);
		//MovieLayout.setPadding(new Insets(25,50,25,50));
		MovieLayout.setVgap(5);
		MovieLayout.setHgap(15);

		// Movie List
		Hyperlink cra = new Hyperlink();
		Image CRA = new Image(getClass().getResourceAsStream("/application/images/CRA.png"));
		ImageView ViewCRA = new ImageView();	
		Text Cratxt = new Text("Crazy Rich Asians");
		cra.setGraphic(ViewCRA);
		ViewCRA.setImage(CRA);
		ViewCRA.setFitHeight(300);
		ViewCRA.setFitWidth(230);
		MovieLayout.add(cra, 0, 0);
		MovieLayout.add(Cratxt, 0, 1);
		cra.setOnAction(e->{
			try {
				homepagemainbp.setCenter(movieNS2());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		Hyperlink golden = new Hyperlink();
		Image GOLDEN = new Image(getClass().getResourceAsStream("/application/images/Golden Job.jpg"));
		ImageView ViewGOLDEN = new ImageView();
		Text Goldentxt = new Text("Golden Job");
		golden.setGraphic(ViewGOLDEN);
		ViewGOLDEN.setImage(GOLDEN);
		ViewGOLDEN.setFitHeight(300);
		ViewGOLDEN.setFitWidth(230);
		MovieLayout.add(golden, 2, 0);
		MovieLayout.add(Goldentxt, 2, 1);
		golden.setOnAction(e->{
			try {
				homepagemainbp.setCenter(movieNS4());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		Hyperlink pask = new Hyperlink();
		Image paskimg = new Image(getClass().getResourceAsStream("/application/images/Paskal.jpg"));
		ImageView Viewpask = new ImageView();
		Text pasktxt = new Text("Paskal");
		pask.setGraphic(Viewpask);
		Viewpask.setImage(paskimg);
		Viewpask.setFitHeight(300);
		Viewpask.setFitWidth(230);
		MovieLayout.add(pask, 0, 3);
		MovieLayout.add(pasktxt, 0, 4);
		pask.setOnAction(e->{
			try {
				homepagemainbp.setCenter(movieNS6());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		Hyperlink first= new Hyperlink();
		Image FIRST = new Image(getClass().getResourceAsStream("/application/images/First Man.jpg"));
		ImageView ViewFIRST = new ImageView();	
		Text Firsttxt = new Text("First Man");
		first.setGraphic(ViewFIRST);
		ViewFIRST.setImage(FIRST);
		ViewFIRST.setFitHeight(300);
		ViewFIRST.setFitWidth(230);
		MovieLayout.add(first, 2, 3);
		MovieLayout.add(Firsttxt, 2, 4);
		first.setOnAction(e->{
			try {
				homepagemainbp.setCenter(movieNS3());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		Hyperlink malicious = new Hyperlink();
		Image MALICIOUS = new Image(getClass().getResourceAsStream("/application/images/Malicious.jpg"));
		ImageView ViewMALICIOUS = new ImageView();
		Text Malicioustxt = new Text("Malicious");
		malicious.setGraphic(ViewMALICIOUS);
		ViewMALICIOUS.setImage(MALICIOUS);
		ViewMALICIOUS.setFitHeight(300);
		ViewMALICIOUS.setFitWidth(230);
		MovieLayout.add(malicious, 0, 6);
		MovieLayout.add(Malicioustxt, 0, 7);
		malicious.setOnAction(e->{
			try {
				homepagemainbp.setCenter(movieNS5());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		Hyperlink simple = new Hyperlink();
		Image Simple = new Image(getClass().getResourceAsStream("/application/images/A Simple Favour.jpg"));
		ImageView ViewSimple = new ImageView();
		Text Simpletxt = new Text("A Simple Favor");
		simple.setGraphic(ViewSimple);
		ViewSimple.setImage(Simple);
		ViewSimple.setFitHeight(300);
		ViewSimple.setFitWidth(230);
		MovieLayout.add(simple, 2, 6);
		MovieLayout.add(Simpletxt, 2, 7);
		simple.setOnAction(e->{
			try {
				homepagemainbp.setCenter(movieNS1());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		Hyperlink venom = new Hyperlink();
		Image Venom = new Image(getClass().getResourceAsStream("/application/images/Venom.jpg"));
		ImageView ViewVenom = new ImageView();
		Text Venomtxt = new Text("Venom");
		venom.setGraphic(ViewVenom);
		ViewVenom.setImage(Venom);
		ViewVenom.setFitHeight(300);
		ViewVenom.setFitWidth(230);
		MovieLayout.add(venom, 0, 9);
		MovieLayout.add(Venomtxt, 0, 10);
		venom.setOnAction(e->{
			try {
				homepagemainbp.setCenter(movieNS7());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		
		// Right Part (Movie Details) with ScrollPane
		ScrollPane scrollMovie = new ScrollPane();
		main.setRight(scrollMovie);
		scrollMovie.setContent(movieRight);
		scrollMovie.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollMovie.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		
		
		// Add Movie into Map to filter Movies
		List<Hyperlink> ChineseMovie = new ArrayList<Hyperlink>();
		ChineseMovie.add(cra);
		ChineseMovie.add(golden);
		
		List<Hyperlink> EnglishMovie = new ArrayList<Hyperlink>();
		EnglishMovie.add(first);
		EnglishMovie.add(malicious);
		EnglishMovie.add(simple);
		EnglishMovie.add(venom);
		
		List<Hyperlink> MalayMovie = new ArrayList<Hyperlink>();
		MalayMovie.add(pask);
		
		List<Hyperlink> Action = new ArrayList<Hyperlink>();
		Action.add(venom);
		Action.add(golden);
		Action.add(pask);
		
		List<Hyperlink> Comedy = new ArrayList<Hyperlink>();
		Comedy.add(cra);
			
		List<Hyperlink> Biography = new ArrayList<Hyperlink>();
		Biography.add(first);
		
		List<Hyperlink> Horror = new ArrayList<Hyperlink>();
		Horror.add(venom);
		Horror.add(malicious);
		
		List<Hyperlink> Crime = new ArrayList<Hyperlink>();
		Crime.add(simple);
	
		List<Hyperlink> WithChildren = new ArrayList<Hyperlink>();
		WithChildren.add(cra);
		WithChildren.add(pask);
		WithChildren.add(venom);
		WithChildren.add(first);
		
		List<Hyperlink> WithoutChildren = new ArrayList<Hyperlink>();
		WithoutChildren.add(golden);
		WithoutChildren.add(simple);
		WithoutChildren.add(malicious);
		WithoutChildren.add(cra);
		WithoutChildren.add(pask);
		WithoutChildren.add(venom);
		WithoutChildren.add(first);
		
		List<Hyperlink> MoreThan2 = new ArrayList<Hyperlink>();
		MoreThan2.add(cra);
		MoreThan2.add(venom);
		MoreThan2.add(first);
		
		List<Hyperlink> LessThan2 = new ArrayList<Hyperlink>();
		LessThan2.add(simple);
		LessThan2.add(golden);
		LessThan2.add(malicious);
		LessThan2.add(pask);
		
		// Text
		List<Text> ChineseMovietxt = new ArrayList<Text>();
		ChineseMovietxt.add(Cratxt);
		ChineseMovietxt.add(Goldentxt);
				
		List<Text> EnglishMovietxt = new ArrayList<Text>();
		EnglishMovietxt.add(Firsttxt);
		EnglishMovietxt.add(Malicioustxt);
		EnglishMovietxt.add(Simpletxt);
		EnglishMovietxt.add(Venomtxt);
		
		List<Text> MalayMovietxt = new ArrayList<Text>();
		MalayMovietxt.add(pasktxt);
		
		List<Text> Actiontxt = new ArrayList<Text>();
		Actiontxt.add(Venomtxt);
		Actiontxt.add(Goldentxt);
		Actiontxt.add(pasktxt);
						
		List<Text> Comedytxt = new ArrayList<Text>();
		Comedytxt.add(Cratxt);
						
		List<Text> Biographytxt = new ArrayList<Text>();
		Biographytxt.add(Firsttxt);
						
		List<Text> Horrortxt = new ArrayList<Text>();
		Horrortxt.add(Venomtxt);
		Horrortxt.add(Malicioustxt);
						
		List<Text> Crimetxt = new ArrayList<Text>();
		Crimetxt.add(Simpletxt);
					
		List<Text> WithChildrentxt = new ArrayList<Text>();
		WithChildrentxt.add(Cratxt);
		WithChildrentxt.add(pasktxt);
		WithChildrentxt.add(Venomtxt);
		WithChildrentxt.add(Firsttxt);
						
		List<Text> WithoutChildrentxt = new ArrayList<Text>();
		WithoutChildrentxt.add(Goldentxt);
		WithoutChildrentxt.add(Simpletxt);
		WithoutChildrentxt.add(Malicioustxt);
		WithoutChildrentxt.add(Cratxt);
		WithoutChildrentxt.add(pasktxt);
		WithoutChildrentxt.add(Venomtxt);
		WithoutChildrentxt.add(Firsttxt);

		List<Text> MoreThan2txt = new ArrayList<Text>();
		MoreThan2txt.add(Cratxt);
		MoreThan2txt.add(Venomtxt);
		MoreThan2txt.add(Firsttxt);
		
		List<Text> LessThan2txt = new ArrayList<Text>();
		LessThan2txt.add(Simpletxt);
		LessThan2txt.add(Goldentxt);
		LessThan2txt.add(Malicioustxt);
		LessThan2txt.add(pasktxt);
		
					
		filter.setOnMouseClicked(e->{
			
			MovieLayout.getChildren().clear();
			List<Hyperlink> filteredMovie = new ArrayList<Hyperlink>();
			filteredMovie.clear();
			List<Text> filteredMovietxt = new ArrayList<Text>();
			filteredMovietxt.clear();
			
			if(chinese.isSelected() && english.isSelected() && malay.isSelected()) {
				filteredMovie.addAll(ChineseMovie);
				filteredMovie.addAll(EnglishMovie);
				filteredMovie.addAll(MalayMovie);
				filteredMovietxt.addAll(ChineseMovietxt);
				filteredMovietxt.addAll(EnglishMovietxt);
				filteredMovietxt.addAll(MalayMovietxt);
				if(comboQ6.getValue() == "Action") {
					filteredMovie.addAll(Action);
					filteredMovietxt.addAll(Actiontxt);
				}
				else if(comboQ6.getValue() == "Comedy") {
					filteredMovie.addAll(Comedy);
					filteredMovietxt.addAll(Comedytxt);
				}
				else if(comboQ6.getValue() == "Biography") {
					
					filteredMovie.addAll(Biography);	
					filteredMovietxt.addAll(Biographytxt);	
				}
				else if(comboQ6.getValue() == "Horror") {
					
					filteredMovie.addAll(Horror);	
					filteredMovietxt.addAll(Horrortxt);	
				}
				else if(comboQ6.getValue() == "Crime") {
					
					filteredMovie.addAll(Crime);	
					filteredMovietxt.addAll(Crimetxt);	
				}
				else {
					
					filteredMovie.clear();
					filteredMovietxt.clear();
				}
				
			}
			else if(chinese.isSelected() && english.isSelected()) {
				filteredMovie.addAll(ChineseMovie);
				filteredMovie.addAll(EnglishMovie);
				filteredMovietxt.addAll(ChineseMovietxt);
				filteredMovietxt.addAll(EnglishMovietxt);
				if(comboQ6.getValue() == "Action") {
					filteredMovie.addAll(Action);
					filteredMovietxt.addAll(Actiontxt);
				}
				else if(comboQ6.getValue() == "Comedy") {
					filteredMovie.addAll(Comedy);
					filteredMovietxt.addAll(Comedytxt);
				}
				else if(comboQ6.getValue() == "Biography") {
					
					filteredMovie.addAll(Biography);	
					filteredMovietxt.addAll(Biographytxt);	
				}
				else if(comboQ6.getValue() == "Horror") {
					
					filteredMovie.addAll(Horror);	
					filteredMovietxt.addAll(Horrortxt);	
				}
				else if(comboQ6.getValue() == "Crime") {
					
					filteredMovie.addAll(Crime);	
					filteredMovietxt.addAll(Crimetxt);	
				}
				else {
					
					filteredMovie.clear();
					filteredMovietxt.clear();
				}
				
			}
			else if(chinese.isSelected() && malay.isSelected()) {
				filteredMovie.addAll(ChineseMovie);
				filteredMovie.addAll(MalayMovie);
				filteredMovietxt.addAll(ChineseMovietxt);
				filteredMovietxt.addAll(MalayMovietxt);
				if(comboQ6.getValue() == "Action") {
					filteredMovie.addAll(Action);
					filteredMovietxt.addAll(Actiontxt);
				}
				else if(comboQ6.getValue() == "Comedy") {
					filteredMovie.addAll(Comedy);
					filteredMovietxt.addAll(Comedytxt);
				}
				else if(comboQ6.getValue() == "Biography") {
					
					filteredMovie.addAll(Biography);	
					filteredMovietxt.addAll(Biographytxt);	
				}
				else if(comboQ6.getValue() == "Horror") {
					
					filteredMovie.addAll(Horror);	
					filteredMovietxt.addAll(Horrortxt);	
				}
				else if(comboQ6.getValue() == "Crime") {
					
					filteredMovie.addAll(Crime);	
					filteredMovietxt.addAll(Crimetxt);	
				}
				else {
					
					filteredMovie.clear();
					filteredMovietxt.clear();
				}
				
			}
			else if(english.isSelected() && malay.isSelected()) {
				filteredMovie.addAll(EnglishMovie);
				filteredMovie.addAll(MalayMovie);
				filteredMovietxt.addAll(EnglishMovietxt);
				filteredMovietxt.addAll(MalayMovietxt);
				if(comboQ6.getValue() == "Action") {
					filteredMovie.addAll(Action);
					filteredMovietxt.addAll(Actiontxt);
				}
				else if(comboQ6.getValue() == "Comedy") {
					filteredMovie.addAll(Comedy);
					filteredMovietxt.addAll(Comedytxt);
				}
				else if(comboQ6.getValue() == "Biography") {
					
					filteredMovie.addAll(Biography);	
					filteredMovietxt.addAll(Biographytxt);	
				}
				else if(comboQ6.getValue() == "Horror") {
					
					filteredMovie.addAll(Horror);	
					filteredMovietxt.addAll(Horrortxt);	
				}
				else if(comboQ6.getValue() == "Crime") {
					
					filteredMovie.addAll(Crime);	
					filteredMovietxt.addAll(Crimetxt);	
				}
				else {
					
					filteredMovie.clear();
					filteredMovietxt.clear();
				}
				
			}
			else if(chinese.isSelected()) {
				
				filteredMovie.addAll(ChineseMovie);
				filteredMovietxt.addAll(ChineseMovietxt);
				
				if(comboQ6.getValue() == "Action") {
					filteredMovie.addAll(Action);
					filteredMovietxt.addAll(Actiontxt);
				}
				else if(comboQ6.getValue() == "Comedy") {
					filteredMovie.addAll(Comedy);	
					filteredMovietxt.addAll(Comedytxt);
				}
				else {
					
					filteredMovie.clear();
					filteredMovietxt.clear();
				}	
			}
			else if(english.isSelected()) {
				
				filteredMovie.addAll(EnglishMovie);
				filteredMovietxt.addAll(EnglishMovietxt);
				if(comboQ6.getValue() == "Action") {
					filteredMovie.addAll(Action);
					filteredMovietxt.addAll(Actiontxt);
				}
				else if(comboQ6.getValue() == "Comedy") {
					filteredMovie.addAll(Comedy);
					filteredMovietxt.addAll(Comedytxt);
				}
				else if(comboQ6.getValue() == "Biography") {
					
					filteredMovie.addAll(Biography);	
					filteredMovietxt.addAll(Biographytxt);	
				}
				else if(comboQ6.getValue() == "Horror") {
					
					filteredMovie.addAll(Horror);	
					filteredMovietxt.addAll(Horrortxt);	
				}
				else if(comboQ6.getValue() == "Crime") {
					
					filteredMovie.addAll(Crime);	
					filteredMovietxt.addAll(Crimetxt);	
				}
				else {
					
					filteredMovie.clear();
					filteredMovietxt.clear();
				}
				
			}
			else if(malay.isSelected()) {
				
				filteredMovie.addAll(MalayMovie);
				filteredMovietxt.addAll(MalayMovietxt);
				
				if(comboQ6.getValue() == "Action") {
					filteredMovie.addAll(Action);
					filteredMovietxt.addAll(Actiontxt);
				}
				else {
					
					filteredMovie.clear();
					filteredMovietxt.clear();
				}	
			}
			else {
				filteredMovie.addAll(ChineseMovie);
				filteredMovie.addAll(EnglishMovie);
				filteredMovie.addAll(MalayMovie);
				filteredMovietxt.addAll(ChineseMovietxt);
				filteredMovietxt.addAll(EnglishMovietxt);
				filteredMovietxt.addAll(MalayMovietxt);
				if(comboQ6.getValue() == "Action") {
					filteredMovie.addAll(Action);
					filteredMovietxt.addAll(Actiontxt);
				}
				else if(comboQ6.getValue() == "Comedy") {
					filteredMovie.addAll(Comedy);
					filteredMovietxt.addAll(Comedytxt);
				}
				else if(comboQ6.getValue() == "Biography") {
					
					filteredMovie.addAll(Biography);	
					filteredMovietxt.addAll(Biographytxt);	
				}
				else if(comboQ6.getValue() == "Horror") {
					
					filteredMovie.addAll(Horror);	
					filteredMovietxt.addAll(Horrortxt);	
				}
				else if(comboQ6.getValue() == "Crime") {
					
					filteredMovie.addAll(Crime);	
					filteredMovietxt.addAll(Crimetxt);	
				}
				else {
					
					filteredMovie.clear();
					filteredMovietxt.clear();
				}
		
			}
		
			
			// Copy the duplicated movie to show with set
			List<Hyperlink> checkDuplicated = new ArrayList<Hyperlink>();
			List<Hyperlink> remainDuplicated = new ArrayList<Hyperlink>();
			checkDuplicated.clear();
			remainDuplicated.clear();
			for(Hyperlink a : filteredMovie) {
				
				if(checkDuplicated.contains(a)) {
					if(!remainDuplicated.contains(a))
					remainDuplicated.add(a);
					
				}
				else {
					
					checkDuplicated.add(a);
					
				}
				
			}
			
			filteredMovie.clear();
			filteredMovie.addAll(remainDuplicated);

			
			List<Text> checkDuplicatedtxt = new ArrayList<Text>();
			List<Text> remainDuplicatedtxt = new ArrayList<Text>();
			checkDuplicatedtxt.clear();
			remainDuplicatedtxt.clear();
			for(Text a : filteredMovietxt) {
				
				if(checkDuplicatedtxt.contains(a)) {
					if(!remainDuplicatedtxt.contains(a))
					remainDuplicatedtxt.add(a);
					
				}
				else {
					
					checkDuplicatedtxt.add(a);
					
				}
				
			}
			
			filteredMovietxt.clear();
			filteredMovietxt.addAll(remainDuplicatedtxt);
			
			
			if(comboQ2.getValue() == "With Children") {
				
				filteredMovie.retainAll(WithChildren);
				filteredMovietxt.retainAll(WithChildrentxt);
			}
			else if(comboQ2.getValue() == "Without Children") {
				
				filteredMovie.retainAll(WithoutChildren);
				filteredMovietxt.retainAll(WithoutChildrentxt);
				
			}
			
			if(L2.isSelected()) {
				
				filteredMovie.retainAll(LessThan2);
				filteredMovietxt.retainAll(LessThan2txt);
			}
			else if(R2.isSelected()) {
				
				filteredMovie.retainAll(MoreThan2);
				filteredMovietxt.retainAll(MoreThan2txt);
				
			}
			
			if(!filteredMovie.isEmpty()) {
				int indexOfImage = -1, indexOfTxt = -1;
				double length = Math.ceil(filteredMovie.size()/2);
				for(int row = 0 ; row < length+filteredMovie.size(); row++) {
					
					for(int col = 0 ; col < 2; col++) {
						indexOfImage++;
						indexOfTxt++;
						if(indexOfImage == filteredMovie.size()){
							break;
						}
						MovieLayout.add(filteredMovie.get(indexOfImage), col, row);
						MovieLayout.add(filteredMovietxt.get(indexOfTxt), col, row+1);
						
					}
					row+=2;
				}
			}
			
		});
		
		All.setOnAction(e->{
			
			MovieLayout.getChildren().clear();
			
			List<Hyperlink> all = new ArrayList<Hyperlink>();
			all.addAll(ChineseMovie);
			all.addAll(EnglishMovie);
			all.addAll(MalayMovie);
			List<Text> alltxt = new ArrayList<Text>();
			alltxt.addAll(ChineseMovietxt);
			alltxt.addAll(EnglishMovietxt);
			alltxt.addAll(MalayMovietxt);
			
			int indexOfImage = -1, indexOfTxt = -1;
			double length = Math.ceil(all.size()/2);
			for(int row = 0 ; row <= length+all.size(); row++) {
				
				for(int col = 0 ; col < 2; col++) {
					
					indexOfImage++;
					indexOfTxt++;
					if(indexOfImage == all.size()) {
						break;
					}
					MovieLayout.add(all.get(indexOfImage), col, row);
					MovieLayout.add(alltxt.get(indexOfTxt), col, row+1);

				}
				row += 2;
			}
			
			
		});
		
		
		
		return main;
	}
	
	//Booking Page
	public BorderPane booking(){
		//setting back button to be visible
		backbtn.setVisible(true);
		//Back Button
		backbtn.setOnAction(e->{
			homepagemainbp.setCenter(choosingShowtime());
		});

		
		BorderPane main = new BorderPane();
		
		GridPane center = new GridPane();
		main.setCenter(center);
		center.setPadding(new Insets(40,100,50,100));
		
		// in GridPane (0, 1) - Movie Details
		FlowPane firstQuater = new FlowPane();
		center.add(firstQuater, 0, 1);
		firstQuater.setHgap(10);
		firstQuater.setPrefSize(800, 400);
		firstQuater.setPadding(new Insets(50,0,0,50));
		
		// Movie Image
		Image movieposter = new Image(getClass().getResourceAsStream(chosenmovie.getPath()));
		ImageView Viewposter = new ImageView();
		Viewposter.setImage(movieposter);
		Viewposter.setFitHeight(300);
		Viewposter.setFitWidth(230);
		firstQuater.getChildren().add(Viewposter);
		
		
		//Movie Details
		GridPane DetailsLayout = new GridPane();
		firstQuater.getChildren().add(DetailsLayout);
		DetailsLayout.setVgap(10);
		DetailsLayout.setPadding(new Insets(25,15,15,15));
		
		Label movie = new Label("Movie : ");
		Text movieName = new Text(chosenmovie.getMovieName());
		movie.setId("MovieText");
		movieName.setId("MovieText");
		DetailsLayout.add(movie, 0, 0);
		DetailsLayout.add(movieName, 1, 0);
		
		Label cinema = new Label("Cinema : ");
		Text cinemaName = new Text(chosenmovie.getCinema());
		cinema.setId("MovieText");
		cinemaName.setId("MovieText");
		DetailsLayout.add(cinema, 0, 1);
		DetailsLayout.add(cinemaName, 1, 1);
		
		Label date = new Label("Date : ");
		//convert LocalDate to String
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String convertedDate = chosenmovie.getDate().format(formatter);
		Text dateTime = new Text(convertedDate);
		
		date.setId("MovieText");
		dateTime.setId("MovieText");
		DetailsLayout.add(date, 0, 2);
		DetailsLayout.add(dateTime, 1, 2);
		
		Label show = new Label("Showtime : ");
		Text showTime = new Text(chosenmovie.getShowtime());
		show.setId("MovieText");
		showTime.setId("MovieText");
		DetailsLayout.add(show, 0, 3);
		DetailsLayout.add(showTime, 1, 3);
		
		Label booking = new Label("Booking ID : ");
		Text bookingId = new Text(Integer.toString(this.bookingId));
		booking.setId("MovieText");
		bookingId.setId("MovieText");
		DetailsLayout.add(booking, 0, 4);
		DetailsLayout.add(bookingId, 1, 4);
		
		//eCombo Details at GridPane(1,0)
		VBox secondQuater = new VBox();
		center.add(secondQuater, 1, 1);
		secondQuater.setPrefSize(950,400);
		secondQuater.setId("secondQuater");
		
		// Title
		GridPane eComboTitle = new GridPane();
		secondQuater.getChildren().add(eComboTitle);
		eComboTitle.setId("eComboTitle");
		eComboTitle.setHgap(150);
		eComboTitle.setPadding(new Insets(10,20,10,30));
		Text eComboType = new Text("e-Combo Type(s)");
		Text eComboPrice = new Text("Item Price (RM)");
		Text eComboQuantity = new Text("Quantity");
		eComboType.setId("eComboTitleD");
		eComboPrice.setId("eComboTitleD");
		eComboQuantity.setId("eComboTitleD");
		eComboTitle.setMargin(eComboPrice, new Insets(0,0,0,50));
		eComboTitle.add(eComboType, 0, 0);
		eComboTitle.add(eComboPrice, 1, 0);
		eComboTitle.add(eComboQuantity, 2, 0);
		
		// Details
		FlowPane eComboDetails = new FlowPane();
		secondQuater.getChildren().add(eComboDetails);
		eComboDetails.setHgap(10);
		
		// e-Combo Types Names
		GridPane Etypes = new GridPane();
		eComboDetails.getChildren().add(Etypes);
		Etypes.setPadding(new Insets(10,30,10,30));
		Etypes.setVgap(5);
		
		Text combo1 = new Text("e-Combo1 ");
		Text KingSpade = new Text("1 x King's Spade Popcorn");
		Text KingDrink = new Text("1 x Soft Drink (LARGE)");
		Etypes.add(combo1, 0, 0);
		Etypes.add(KingSpade, 1, 0);
		Etypes.add(KingDrink, 1, 1);
		combo1.setId("combotxt");
		Etypes.setMargin(combo1, new Insets(25,0,0,0));
		Etypes.setMargin(KingSpade, new Insets(25,0,0,0));
		KingSpade.setId("combotxt");
		KingDrink.setId("combotxt");
		
		Text combo2 = new Text("e-Combo2 ");
		Text QueenSpade = new Text("1 x Queen's Spade Popcorn");
		Text QueenDrink = new Text("1 x Soft Drink (REGULAR)");
		Etypes.add(combo2, 0, 2);
		Etypes.add(QueenSpade, 1, 2);
		Etypes.add(QueenDrink, 1, 3);
		Etypes.setMargin(combo2, new Insets(40,0,0,0));
		Etypes.setMargin(QueenSpade, new Insets(40,0,0,0));
		combo2.setId("combotxt");
		QueenSpade.setId("combotxt");
		QueenDrink.setId("combotxt");
		
		Text combo3 = new Text("e-Combo3 ");
		Text JackSpade = new Text("1 x Jack's Spade Popcorn");
		Text JackDrink = new Text("1 x Mineral Water");
		Etypes.add(combo3, 0, 4);
		Etypes.add(JackSpade, 1, 4);
		Etypes.add(JackDrink, 1, 5);
		Etypes.setMargin(combo3, new Insets(40,0,0,0));
		Etypes.setMargin(JackSpade, new Insets(40,0,0,0));
		combo3.setId("combotxt");
		JackSpade.setId("combotxt");
		JackDrink.setId("combotxt");
		
		// eCombo Price
		GridPane Eprice = new GridPane();
		eComboDetails.getChildren().add(Eprice);
		Eprice.setPadding(new Insets(50,30,10,50));
		Eprice.setVgap(40);
		
		Text Combo1Price = new Text("12.0");
		Text Combo2Price = new Text("10.0");
		Text Combo3Price = new Text(" 8.0");
		Eprice.add(Combo1Price, 0, 0);
		Eprice.add(Combo2Price, 0, 2);
		Eprice.add(Combo3Price, 0, 4);
		Combo1Price.setId("combotxt");
		Combo2Price.setId("combotxt");
		Combo3Price.setId("combotxt");
		
		//eCombo Quantity
		GridPane Equantity = new GridPane();
		eComboDetails.getChildren().add(Equantity);
		Equantity.setPadding(new Insets(45,30,10,150));
		Equantity.setVgap(70);
		Equantity.setHgap(10);
		
		Button Combo1Plus = new Button(">");
		Button Combo1Minus = new Button("<");
		Text Combo1Quantity = new Text("0");
		Combo1Quantity.setId("combotxt");
		Combo1Plus.setId("operator");
		Combo1Minus.setId("operator");
		Equantity.add(Combo1Plus, 2, 0);
		Equantity.add(Combo1Quantity, 1, 0);
		Equantity.add(Combo1Minus, 0, 0);
		
		Button Combo2Plus = new Button(">");
		Button Combo2Minus = new Button("<");
		Text Combo2Quantity = new Text("0");
		Combo2Quantity.setId("combotxt");
		Combo2Plus.setId("operator");
		Combo2Minus.setId("operator");
		Equantity.add(Combo2Plus, 2, 1);
		Equantity.add(Combo2Quantity, 1, 1);
		Equantity.add(Combo2Minus, 0, 1);
		
		Button Combo3Plus = new Button(">");
		Button Combo3Minus = new Button("<");
		Text Combo3Quantity = new Text("0");
		Combo3Quantity.setId("combotxt");
		Combo3Plus.setId("operator");
		Combo3Minus.setId("operator");
		Equantity.add(Combo3Plus, 2, 2);
		Equantity.add(Combo3Quantity, 1, 2);
		Equantity.add(Combo3Minus, 0, 2);
		
		//Ticket Details at GridPane(0,1)
		VBox thirdQuater = new VBox();
		center.add(thirdQuater, 0, 2);
		thirdQuater.setId("thirdQuater");
		
		// Ticket Title
		FlowPane ticketTitle = new FlowPane();
		thirdQuater.getChildren().add(ticketTitle);
		ticketTitle.setId("ticketTitle");
		ticketTitle.setPadding(new Insets(10,30,10,30));
		ticketTitle.setHgap(120);
		Text ticketType = new Text("Ticket Type");
		Text ticketPrice = new Text("Ticket Price (RM)");
		Text ticketQuantity = new Text("Quantity");
		ticketType.setId("ticketTitleD");
		ticketPrice.setId("ticketTitleD");
		ticketQuantity.setId("ticketTitleD");
		ticketTitle.getChildren().addAll(ticketType,ticketPrice,ticketQuantity);
		
		//Ticket Details
		FlowPane ticketDetails = new FlowPane();
		thirdQuater.getChildren().add(ticketDetails);
		ticketDetails.setHgap(205);
		
		
		// Ticket Types Names
		GridPane Ttypes = new GridPane();
		ticketDetails.getChildren().add(Ttypes);
		Ttypes.setVgap(40);
		Ttypes.setPadding(new Insets(30,0,30,40));

		Text adult = new Text("Adult ");
		Ttypes.add(adult, 0, 0);
		adult.setId("tickettxt");
		Text kid = new Text("Children ");
		Ttypes.add(kid, 0, 1);
		kid.setId("tickettxt");
		Text twin = new Text("Twin ");
		Ttypes.add(twin, 0, 2);
		twin.setId("tickettxt");

		
		// Ticket Price
		GridPane Tprice = new GridPane();
		ticketDetails.getChildren().add(Tprice);
		Tprice.setVgap(20);
		Tprice.setPadding(new Insets(30,0,30,0));
		
		Text adultPrice = new Text("16.5");
		Text kidPrice = new Text("10.0");
		Text twinPrice = new Text("36.0");
		adultPrice.setId("tickettxt");
		kidPrice.setId("tickettxt");
		twinPrice.setId("tickettxt");
		Tprice.add(adultPrice, 0, 0);
		Tprice.add(kidPrice, 0, 2);
		Tprice.add(twinPrice, 0, 4);
		
		//Ticket Quantity
		GridPane Tquantity = new GridPane();
		ticketDetails.getChildren().add(Tquantity);
		Tquantity.setHgap(10);
		Tquantity.setVgap(29);
		Tquantity.setPadding(new Insets(25,0,30,0));
		
		Button adultPlus = new Button(">");
		Button adultMinus = new Button("<");
		//Global Variable
		adultQuantity.setId("tickettxt");
		adultPlus.setId("operator");
		adultMinus.setId("operator");
		Tquantity.add(adultPlus, 2, 0);
		Tquantity.add(adultQuantity, 1, 0);
		Tquantity.add(adultMinus, 0, 0);
		
		Button kidPlus = new Button(">");
		Button kidMinus = new Button("<");
		if(chosenmovie.getMovieName().equalsIgnoreCase("a simple favour") || chosenmovie.getMovieName().equalsIgnoreCase("golden job")
				|| chosenmovie.getMovieName().equalsIgnoreCase("malicious")) {
			kidPlus.setDisable(true);
			kidMinus.setDisable(true);
		} else {
			kidPlus.setDisable(false);
			kidMinus.setDisable(false);
		}
		//Global Variable
		kidQuantity.setId("tickettxt");
		kidPlus.setId("operator");
		kidMinus.setId("operator");
		Tquantity.add(kidPlus, 2, 1);
		Tquantity.add(kidQuantity, 1, 1);
		Tquantity.add(kidMinus, 0, 1);
		
		Button twinPlus = new Button(">");
		Button twinMinus = new Button("<");
		//Global Variable
		twinQuantity.setId("tickettxt");
		twinPlus.setId("operator");
		twinMinus.setId("operator");
		Tquantity.add(twinPlus, 2, 2);
		Tquantity.add(twinQuantity, 1, 2);
		Tquantity.add(twinMinus, 0, 2);
		
		// Grand total in GrinPane(1,2)
		FlowPane fourthQuater = new FlowPane();
		center.add(fourthQuater, 1, 2);
		fourthQuater.setPrefSize(700, 300);
		
		VBox fourthLeft = new VBox();
		fourthQuater.getChildren().add(fourthLeft);
		fourthLeft.setPrefSize(650,300);
		fourthLeft.setPadding(new Insets(10,0,0,30));
		Text orderTitle = new Text("Your Order");
		orderTitle.setId("yourOrder");
		fourthLeft.getChildren().add(orderTitle);
		
		// Ticket total
		FlowPane TicketandCombo = new FlowPane();
		TicketandCombo.setHgap(55);
		VBox ticketTotal = new VBox();
		fourthLeft.getChildren().add(TicketandCombo);
		TicketandCombo.getChildren().add(ticketTotal);
		ticketTotal.setPrefHeight(220);
		ticketTotal.setPadding(new Insets(20,0,0,140));
		Text ticket = new Text("Ticket(s)                   ");
		ticket.setId("subtotal");
		ticket.setUnderline(true);
		ticketTotal.getChildren().add(ticket);
		FlowPane adultA = new FlowPane();
		Text AticketNo = new Text("0");
		Text AticketX = new Text("x");
		Text AticketA = new Text("Adult");
		Text AticketC = new Text(" 0.0");
		double AticketCsubtotal = Double.parseDouble(adultQuantity.getText())*Double.parseDouble(adultPrice.getText());
		AticketC.setText(Double.toString(AticketCsubtotal));
		AticketNo.setId("subtotal");
		AticketX.setId("subtotal");
		AticketA.setId("subtotal");
		AticketC.setId("subtotal");
		adultA.setPrefWidth(100);
		adultA.setHgap(22);
		adultA.getChildren().addAll(AticketNo,AticketX,AticketA,AticketC);
		ticketTotal.getChildren().add(adultA);
		FlowPane adultC = new FlowPane();
		Text CticketNo = new Text("0");
		Text CticketX = new Text("x");
		Text CticketA = new Text("Children");
		Text CticketC = new Text(" 0.0");
		CticketNo.setId("subtotal");
		CticketX.setId("subtotal");
		CticketA.setId("subtotal");
		CticketC.setId("subtotal");
		adultC.setPrefWidth(100);
		adultC.setHgap(13);
		adultC.getChildren().addAll(CticketNo,CticketX,CticketA,CticketC);
		ticketTotal.getChildren().add(adultC);
		FlowPane adultT = new FlowPane();
		Text TticketNo = new Text("0");
		Text TticketX = new Text("x");
		Text TticketA = new Text("Twin");
		Text TticketC = new Text(" 0.0");
		TticketNo.setId("subtotal");
		TticketX.setId("subtotal");
		TticketA.setId("subtotal");
		TticketC.setId("subtotal");
		adultT.setPrefWidth(100);
		adultT.setHgap(23);
		adultT.getChildren().addAll(TticketNo,TticketX,TticketA,TticketC);
		ticketTotal.getChildren().add(adultT);
		
		// e-Combo total
		VBox eComboTotal = new VBox();
		TicketandCombo.getChildren().add(eComboTotal);
		eComboTotal.setPrefHeight(220);
		eComboTotal.setPadding(new Insets(20,0,0,0));
		Text eCombo = new Text("e-Combo(s)                  ");
		eCombo.setId("subtotal");
		eCombo.setUnderline(true);
		eComboTotal.getChildren().add(eCombo);
		FlowPane Ecombo1 = new FlowPane();
		Text Ecombo1No = new Text("0");
		Text Ecombo1X = new Text("x");
		Text Ecombo1A = new Text("e-Combo1");
		Text Ecombo1C = new Text(" 0.0");
		Ecombo1No.setId("subtotal");
		Ecombo1X.setId("subtotal");
		Ecombo1A.setId("subtotal");
		Ecombo1C.setId("subtotal");
		Ecombo1.setPrefWidth(100);
		Ecombo1.setHgap(10);
		Ecombo1.getChildren().addAll(Ecombo1No,Ecombo1X,Ecombo1A,Ecombo1C);
		eComboTotal.getChildren().add(Ecombo1);
		FlowPane Ecombo2 = new FlowPane();
		Text Ecombo2No = new Text("0");
		Text Ecombo2X = new Text("x");
		Text Ecombo2A = new Text("e-Combo2");
		Text Ecombo2C = new Text(" 0.0");
		Ecombo2No.setId("subtotal");
		Ecombo2X.setId("subtotal");
		Ecombo2A.setId("subtotal");
		Ecombo2C.setId("subtotal");
		Ecombo2.setPrefWidth(100);
		Ecombo2.setHgap(10);
		Ecombo2.getChildren().addAll(Ecombo2No,Ecombo2X,Ecombo2A,Ecombo2C);
		eComboTotal.getChildren().add(Ecombo2);
		FlowPane Ecombo3 = new FlowPane();
		Text Ecombo3No = new Text("0");
		Text Ecombo3X = new Text("x");
		Text Ecombo3A = new Text("e-Combo3");
		Text Ecombo3C = new Text(" 0.0");
		Ecombo3No.setId("subtotal");
		Ecombo3X.setId("subtotal");
		Ecombo3A.setId("subtotal");
		Ecombo3C.setId("subtotal");
		Ecombo3.setPrefWidth(100);
		Ecombo3.setHgap(10);
		Ecombo3.getChildren().addAll(Ecombo3No,Ecombo3X,Ecombo3A,Ecombo3C);
		eComboTotal.getChildren().add(Ecombo3);
		
		GridPane sub = new GridPane();
		sub.setHgap(120);
		sub.setId("sublayout");
		sub.setPadding(new Insets(5,0,5,20));
		fourthLeft.getChildren().add(sub);
		Text subtxt = new Text("Subtotal(RM)");
		subtxt.setId("sub");
		Tsubtotal.setId("sub");
		Esubtotal.setId("sub");
		sub.add(subtxt, 0, 0);
		sub.add(Tsubtotal, 1, 0);
		Tsubtotal.setText(Double.toString(AticketCsubtotal));
		sub.add(Esubtotal, 2, 0);
		sub.setMargin(Esubtotal, new Insets(0,0,0,90));
		
		// Grand total
		GridPane grandlayout = new GridPane();
		GridPane grandTotal = new GridPane();
		grandlayout.add(grandTotal, 0, 0);
		grandlayout.setPadding(new Insets(30,10,10,30));
		grandlayout.setPrefWidth(285);
		fourthQuater.getChildren().add(grandlayout);
		grandTotal.setId("grandLayout");
		grandTotal.setPrefSize(230,230);
		Text total = new Text("Grand Total\n      (RM) :");
		total.setId("grandtxt");
		totalCost.setId("grandtxt1");
		totalCost.setText(Double.toString(AticketCsubtotal));
		grandTotal.add(total, 0, 0);
		grandTotal.add(totalCost, 0, 1);
		grandTotal.setMargin(total, new Insets(35,10,10,33));
		grandTotal.setMargin(totalCost, new Insets(27,0,0,75));
		

		// Ticket handler_________________________________________________________________________________________________________________
		adultPlus.setOnMouseClicked(e->{
			
			int quantity = Integer.parseInt(adultQuantity.getText());
			double cost = Double.parseDouble(adultPrice.getText());
			double Asubtotal = Double.parseDouble(Tsubtotal.getText());
			double grand = Double.parseDouble(totalCost.getText());
			double subtotal = Double.parseDouble(AticketC.getText());
			if(quantity < 15) {
				quantity += 1;
				subtotal += cost ;
				Asubtotal += cost;
				grand += cost;
			}
			adultQuantity.setText(quantity+"");
			AticketNo.setText(quantity+"");
			AticketC.setText(subtotal+"");
			Tsubtotal.setText(Asubtotal+"");
			totalCost.setText(grand + "");
			
		
		});
		
		kidPlus.setOnMouseClicked(e->{
			
			int quantity = Integer.parseInt(kidQuantity.getText());
			double cost = Double.parseDouble(kidPrice.getText());
			double Csubtotal = Double.parseDouble(Tsubtotal.getText());
			double grand = Double.parseDouble(totalCost.getText());
			double subtotal = Double.parseDouble(CticketC.getText());;
			if(quantity < 15) {
				quantity += 1;
				subtotal += cost;
				Csubtotal += cost;
				grand += cost;
			}
			kidQuantity.setText(quantity+"");
			CticketNo.setText(quantity+"");
			CticketC.setText(subtotal+"");
			Tsubtotal.setText(Csubtotal+"");
			totalCost.setText(grand + "");
			
		});
		
		twinPlus.setOnMouseClicked(e->{
			
			int quantity = Integer.parseInt(twinQuantity.getText());
			double cost = Double.parseDouble(twinPrice.getText());
			double Asubtotal = Double.parseDouble(Tsubtotal.getText());
			double grand = Double.parseDouble(totalCost.getText());
			double subtotal = Double.parseDouble(TticketC.getText());
			if(quantity < 5) {
				quantity += 1;
				subtotal += cost;
				Asubtotal += cost;
				grand += cost;
			}
			twinQuantity.setText(quantity+"");
			TticketNo.setText(quantity+"");
			TticketC.setText(subtotal+"");
			Tsubtotal.setText(Asubtotal+"");
			totalCost.setText(grand + "");
			
		});
		
		adultMinus.setOnMouseClicked(e->{
			
			int quantity = Integer.parseInt(adultQuantity.getText());
			double cost = Double.parseDouble(adultPrice.getText());
			double Asubtotal = Double.parseDouble(Tsubtotal.getText());
			double grand = Double.parseDouble(totalCost.getText());
			double subtotal = Double.parseDouble(AticketC.getText());
			if(quantity > 0) {
				quantity -= 1;
				subtotal -= cost;
				Asubtotal -= cost;
				grand -= cost;
				if(quantity == 0) {
					
					subtotal = 0.00;
					
				}
				adultQuantity.setText(quantity+"");
				AticketNo.setText(quantity+"");
				AticketC.setText(subtotal+"");
				Tsubtotal.setText(Asubtotal+"");
				totalCost.setText(grand + "");
			}
		
		});
		
		kidMinus.setOnMouseClicked(e->{
			
			int quantity = Integer.parseInt(kidQuantity.getText());
			double cost = Double.parseDouble(kidPrice.getText());
			double Asubtotal = Double.parseDouble(Tsubtotal.getText());
			double grand = Double.parseDouble(totalCost.getText());
			double subtotal = Double.parseDouble(CticketC.getText());
			if(quantity > 0) {
				quantity -= 1;
				subtotal -= cost;
				Asubtotal -= cost;
				grand -= cost;
				if(quantity == 0) {
					
					subtotal = 0.00;
					
				}
				kidQuantity.setText(quantity+"");
				CticketNo.setText(quantity+"");
				CticketC.setText(subtotal+"");
				Tsubtotal.setText(Asubtotal+"");
				totalCost.setText(grand + "");
			}
		
		});
		
		twinMinus.setOnMouseClicked(e->{
			
			int quantity = Integer.parseInt(twinQuantity.getText());
			double cost = Double.parseDouble(twinPrice.getText());
			double Asubtotal = Double.parseDouble(Tsubtotal.getText());
			double grand = Double.parseDouble(totalCost.getText());
			double subtotal = Double.parseDouble(TticketC.getText());
			if(quantity > 0) {
				quantity -= 1;
				subtotal -= cost;
				Asubtotal -= cost;
				grand -= cost;
				if(quantity == 0) {
					
					subtotal = 0.00;
					
				}
				twinQuantity.setText(quantity+"");
				TticketNo.setText(quantity+"");
				TticketC.setText(subtotal+"");
				Tsubtotal.setText(Asubtotal+"");
				totalCost.setText(grand + "");
			}
		
		});
		// Ticket handler_________________________________________________________________________________________________________________
		
		// eCombo Handler_________________________________________________________________________________________________________________
		Combo1Plus.setOnMouseClicked(e->{
			
			int quantity = Integer.parseInt(Combo1Quantity.getText());
			double cost = Double.parseDouble(Combo1Price.getText());
			double Asubtotal = Double.parseDouble(Esubtotal.getText());
			double grand = Double.parseDouble(totalCost.getText());
			double subtotal;
			quantity += 1;
			subtotal = cost * quantity;
			Asubtotal += cost;
			grand += cost;
			Combo1Quantity.setText(quantity+"");
			Ecombo1No.setText(quantity+"");
			Ecombo1C.setText(subtotal+"");
			Esubtotal.setText(Asubtotal+"");
			totalCost.setText(grand + "");
			
		
		});
		
		Combo2Plus.setOnMouseClicked(e->{
			
			int quantity = Integer.parseInt(Combo2Quantity.getText());
			double cost = Double.parseDouble(Combo2Price.getText());
			double Csubtotal = Double.parseDouble(Esubtotal.getText());
			double grand = Double.parseDouble(totalCost.getText());
			double subtotal;
			quantity += 1;
			subtotal = cost * quantity;
			Csubtotal += cost;
			grand += cost;
			Combo2Quantity.setText(quantity+"");
			Ecombo2No.setText(quantity+"");
			Ecombo2C.setText(subtotal+"");
			Esubtotal.setText(Csubtotal+"");
			totalCost.setText(grand + "");
			
		});
		
		Combo3Plus.setOnMouseClicked(e->{
			
			int quantity = Integer.parseInt(Combo3Quantity.getText());
			double cost = Double.parseDouble(Combo3Price.getText());
			double Asubtotal = Double.parseDouble(Esubtotal.getText());
			double grand = Double.parseDouble(totalCost.getText());
			double subtotal;
			quantity += 1;
			subtotal = cost * quantity;
			Asubtotal += cost;
			grand += cost;
			Combo3Quantity.setText(quantity+"");
			Ecombo3No.setText(quantity+"");
			Ecombo3C.setText(subtotal+"");
			Esubtotal.setText(Asubtotal+"");
			totalCost.setText(grand + "");
			
		});
		
		Combo1Minus.setOnMouseClicked(e->{
			
			int quantity = Integer.parseInt(Combo1Quantity.getText());
			double cost = Double.parseDouble(Combo1Price.getText());
			double Asubtotal = Double.parseDouble(Esubtotal.getText());
			double grand = Double.parseDouble(totalCost.getText());
			double subtotal = Double.parseDouble(Ecombo1C.getText());
			if(quantity > 0) {
				quantity -= 1;
				subtotal -= cost;
				Asubtotal -= cost;
				grand -= cost;
				if(quantity == 0) {
					
					subtotal = 0.00;
					
				}
				Combo1Quantity.setText(quantity+"");
				Ecombo1No.setText(quantity+"");
				Ecombo1C.setText(subtotal+"");
				Esubtotal.setText(Asubtotal+"");
				totalCost.setText(grand + "");
			}
		
		});
		
		Combo2Minus.setOnMouseClicked(e->{
			
			int quantity = Integer.parseInt(Combo2Quantity.getText());
			double cost = Double.parseDouble(Combo2Price.getText());
			double Asubtotal = Double.parseDouble(Esubtotal.getText());
			double grand = Double.parseDouble(totalCost.getText());
			double subtotal = Double.parseDouble(Ecombo2C.getText());
			if(quantity > 0) {
				quantity -= 1;
				subtotal -= cost;
				Asubtotal -= cost;
				grand -= cost;
				if(quantity == 0) {
					
					subtotal = 0.00;
					
				}
				Combo2Quantity.setText(quantity+"");
				Ecombo2No.setText(quantity+"");
				Ecombo2C.setText(subtotal+"");
				Esubtotal.setText(Asubtotal+"");
				totalCost.setText(grand + "");
			}
		
		});
		
		Combo3Minus.setOnMouseClicked(e->{
			
			int quantity = Integer.parseInt(Combo3Quantity.getText());
			double cost = Double.parseDouble(Combo3Price.getText());
			double Asubtotal = Double.parseDouble(Esubtotal.getText());
			double grand = Double.parseDouble(totalCost.getText());
			double subtotal = Double.parseDouble(Ecombo3C.getText());
			if(quantity > 0) {
				quantity -= 1;
				subtotal -= cost;
				Asubtotal -= cost;
				grand -= cost;
				if(quantity == 0) {
					
					subtotal = 0.00;
					
				}
				Combo3Quantity.setText(quantity+"");
				Ecombo3No.setText(quantity+"");
				Ecombo3C.setText(subtotal+"");
				Esubtotal.setText(Asubtotal+"");
				totalCost.setText(grand + "");
			}
		
		});
		
		
		//Pane for button padding
		HBox proceedbtnpane = new HBox();
		main.setBottom(proceedbtnpane);
		proceedbtnpane.setAlignment(Pos.BASELINE_RIGHT);
		proceedbtnpane.setPadding(new Insets(0,20,20,0));
		
		//Proceed Button
		Button proceedbtn = new Button("Proceed");
		proceedbtn.setPrefSize(150, 70);
		proceedbtnpane.getChildren().add(proceedbtn);
		proceedbtn.setOnAction(e->{
			homepagemainbp.setCenter(choosingSeats());
		});

		
	
		return main;
	}
 
	//Cinema Details Page
	public ScrollPane cinemas() {
		//setting back button to be invisible
		backbtn.setVisible(false);
		//GridPane
		GridPane gp = new GridPane();
		gp.setHgap(5);
		gp.setVgap(5);
		gp.setPadding(new Insets(30,30,30,30));
		
		
		//Mid Valley
		ImageView cinema1iv = new ImageView();
		Image cinema1img = new Image(getClass().getResourceAsStream("/application/images/midvalley.jpg"));
		cinema1iv.setImage(cinema1img);
		cinema1iv.setFitHeight(300);
		cinema1iv.setPreserveRatio(true);
		//VBox for description
		VBox vb1 = new VBox();
		vb1.setSpacing(10);	
		Label namecinema1 = new Label("Mid Valley");
		namecinema1.getStyleClass().add("CinemaName");
		Label addresscinema1 = new Label("Mid Valley Megamall, LOT F-049&050,1ST, Mid Valley City, \n58000 Kuala Lumpur");
		Label contactcinema1 = new Label("(+603)66741017");
		vb1.getChildren().addAll(addresscinema1,contactcinema1);
		HBox hb1 = new HBox();
		hb1.setSpacing(10);
		hb1.getChildren().addAll(cinema1iv,vb1);
		gp.add(namecinema1, 0, 0);
		gp.add(hb1,0,1);
		gp.setMargin(hb1, new Insets(0,0,30,0));
		
		
		//Sunway Pyramid
		ImageView cinema2iv = new ImageView();
		Image cinema2img = new Image(getClass().getResourceAsStream("/application/images/sunway.jpg"));
		cinema2iv.setImage(cinema2img);
		cinema2iv.setFitHeight(300);
		cinema2iv.setPreserveRatio(true);
		//VBox for description
		VBox vb2 = new VBox();
		vb2.setSpacing(10);	
		Label namecinema2 = new Label("Sunway Pyramid");
		namecinema2.getStyleClass().add("CinemaName");
		Label addresscinema2 = new Label("3, Jalan PJS 11/15, Bandar Sunway, \n47500 Subang Jaya, Selangor");
		Label contactcinema2 = new Label("(+603)42450088");
		vb2.getChildren().addAll(addresscinema2,contactcinema2);
		HBox hb2 = new HBox();
		hb2.setSpacing(10);
		hb2.getChildren().addAll(cinema2iv,vb2);
		gp.add(namecinema2, 0, 3);
		gp.add(hb2,0,4);
		gp.setMargin(hb2, new Insets(0,0,30,0));
		
		
		//1U
		ImageView cinema3iv = new ImageView();
		Image cinema3img = new Image(getClass().getResourceAsStream("/application/images/1u.jpg"));
		cinema3iv.setImage(cinema3img);
		cinema3iv.setFitHeight(300);
		cinema3iv.setPreserveRatio(true);
		//VBox for description
		VBox vb3 = new VBox();
		vb3.setSpacing(10);	
		Label namecinema3 = new Label("One Utama");
		namecinema3.getStyleClass().add("CinemaName");
		Label addresscinema3 = new Label("1, Lebuh Bandar Utama, Bandar Utama, \n47800 Petaling Jaya, Selangor");
		Label contactcinema3 = new Label("(+603)76936124");
		vb3.getChildren().addAll(addresscinema3,contactcinema3);
		HBox hb3 = new HBox();
		hb3.setSpacing(10);
		hb3.getChildren().addAll(cinema3iv,vb3);
		gp.add(namecinema3, 0, 5);
		gp.add(hb3,0,6);
		gp.setMargin(hb3, new Insets(0,0,30,0));
		
		
		//KLCC
		ImageView cinema4iv = new ImageView();
		Image cinema4img = new Image(getClass().getResourceAsStream("/application/images/klcc.jpg"));
		cinema4iv.setImage(cinema4img);
		cinema4iv.setFitHeight(300);
		cinema4iv.setPreserveRatio(true);
		//VBox for description
		VBox vb4 = new VBox();
		vb4.setSpacing(10);	
		Label namecinema4 = new Label("KLCC");
		namecinema4.getStyleClass().add("CinemaName");
		Label addresscinema4 = new Label("KLCC, Lot 241 Level 2 Suria, Kuala Lumpur City Centre, \n50088 Kuala Lumpur, Wilayah Persekutuan Kuala Lumpur");
		Label contactcinema4 = new Label("(+603)74818033");
		vb4.getChildren().addAll(addresscinema4,contactcinema4);
		HBox hb4 = new HBox();
		hb4.setSpacing(10);
		hb4.getChildren().addAll(cinema4iv,vb4);
		gp.add(namecinema4, 0, 7);
		gp.add(hb4,0,8);
		gp.setMargin(hb4, new Insets(0,0,30,0));
		
		ScrollPane scrollMovie = new ScrollPane();
		scrollMovie.setContent(gp);
		scrollMovie.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scrollMovie.setHbarPolicy(ScrollBarPolicy.NEVER);

		return scrollMovie;
	}
	
	//Contact Us Page
	public HBox contactUs() {
		//setting back button to be invisible
		backbtn.setVisible(false);
		//HBox
		HBox hb = new HBox();
		hb.setSpacing(20);
		
		
		//Logo
		Image logoimg = new Image(getClass().getResourceAsStream("/application/images/spade.png"));
		ImageView logoiv= new ImageView();
		logoiv.setImage(logoimg);
		logoiv.setFitHeight(200);
		logoiv.setPreserveRatio(true);
		hb.getChildren().add(logoiv);
		
		//GridPane
		GridPane gp = new GridPane();
		gp.setHgap(5);
		gp.setVgap(5);
		gp.setAlignment(Pos.CENTER);
		hb.getChildren().add(gp);
		
		//Email
		Label emaillbl1 = new Label("Email: ");
		Label emaillbl2 = new Label("spades.theatre@gmail.com.my");
		gp.add(emaillbl1,0,0);
		gp.add(emaillbl2, 1, 0);
		
		//Facebook Page
		Label fblbl1 = new Label("Facebook Page: ");
		Label fblbl2 = new Label("www.facebook.com/spades.21");
		gp.add(fblbl1,0,1);
		gp.add(fblbl2, 1, 1);
		
		//Instagram Page 
		Label iglbl1 = new Label("Instagram Page: ");
		Label iglbl2 = new Label("www.instagram.com/spades.theatre_my");
		gp.add(iglbl1,0,2);
		gp.add(iglbl2, 1, 2);
		
		
		//Phone Contacts For all branches
		VBox vb = new VBox();
		vb.setPadding(new Insets(100,0,0,0));
		vb.setSpacing(10);
		
		//Contact Title
		Label titlelbl = new Label("Contacts For Branches");
		titlelbl.getStyleClass().add("CinemaName");
		vb.getChildren().add(titlelbl);
		
		//GridPane 
		GridPane gp1 = new GridPane();
		gp1.setVgap(5);
		gp1.setHgap(5);
		vb.getChildren().add(gp1);
		
		//Sunway
		Label sunwaylbl1 = new Label("Sunway: ");
		Label sunwaylbl2 = new Label("(+603)42450088");
		gp1.add(sunwaylbl1, 0, 0);
		gp1.add(sunwaylbl2, 1, 0);
		
		//Mid Valley
		Label mvlbl1 = new Label("Mid Valley: ");
		Label mvlbl2 = new Label("(+603)66741017");
		gp1.add(mvlbl1, 0, 1);
		gp1.add(mvlbl2, 1, 1);
		
		//1U
		Label oulbl1 = new Label("One Utama: ");
		Label oulbl2 = new Label("(+603)76936124");
		gp1.add(oulbl1, 0, 2);
		gp1.add(oulbl2, 1, 2);
		
		//KLCC
		Label klcclbl1 = new Label("Suria KLCC: ");
		Label klcclbl2 = new Label("(+603)74818033");
		gp1.add(klcclbl1, 0, 3);
		gp1.add(klcclbl2, 1, 3);
		
		
		//VBox to align
		VBox mainvb = new VBox();
		mainvb.getChildren().addAll(hb,vb);
		mainvb.setAlignment(Pos.CENTER);
		
		//HBox to align
		HBox mainhb = new HBox();
		mainhb.getChildren().add(mainvb);
		mainhb.setAlignment(Pos.CENTER);
		
		return mainhb;
	}
	
	//Payment Page
	public BorderPane payment() {
		//setting back button to be visible
		backbtn.setVisible(true);
		//Back Button
		backbtn.setOnAction(e->{
			homepagemainbp.setCenter(choosingSeats());
		});

		//BorderPane for align
		BorderPane bp = new BorderPane();
		bp.setPadding(new Insets(20,20,20,20));
		
		//GridPane 
		GridPane gp = new GridPane();
		gp.setPadding(new Insets(20,20,20,20));
		gp.setHgap(10);
		gp.setVgap(10);
		gp.setAlignment(Pos.CENTER);
		bp.setCenter(gp);
		
		//Title
		Label titlelbl = new Label("Payment Information");
		titlelbl.setId("paymentTitle");
		bp.setTop(titlelbl);
		
		//Add Total Amount at Left
		GridPane amountgp = new GridPane();
		amountgp.setHgap(10);
		amountgp.setVgap(10);
		amountgp.setPadding(new Insets(100,0,0,0));
		bp.setLeft(amountgp);
		
		Label eSublbl = new Label("E-Combo Total: ");
		Label eSubAmountlbl = new Label();
		eSubAmountlbl.setText("RM"+Esubtotal.getText());
		amountgp.add(eSublbl, 0, 0);
		amountgp.add(eSubAmountlbl, 1, 0);
		
		Label tSublbl = new Label("Tickets Total: ");
		Label tSubAmountlbl = new Label();
		tSubAmountlbl.setText("RM"+Tsubtotal.getText());
		amountgp.add(tSublbl, 0, 1);
		amountgp.add(tSubAmountlbl, 1, 1);
		
		Label grandTotallbl = new Label("Grand Total: ");
		Label grandTotalAmountlbl = new Label();
		grandTotallbl.setFont(Font.font(null,FontWeight.BOLD,27));
		grandTotalAmountlbl.setFont(Font.font(null,FontWeight.BOLD,27));
		grandTotalAmountlbl.setText("RM"+totalCost.getText());
		amountgp.add(grandTotallbl, 0, 2);
		amountgp.add(grandTotalAmountlbl, 1, 2);
		
		
		//RadioButton Credit/Debit
		RadioButton creditrb = new RadioButton("Credit Card");
		RadioButton debitrb = new RadioButton("Debit Card");
		creditrb.setFont(Font.font(null,20));
		debitrb.setFont(Font.font(null,20));
		gp.add(creditrb, 0, 0);
		gp.add(debitrb, 1, 0);
		
		ToggleGroup tg = new ToggleGroup();
		creditrb.setToggleGroup(tg);
		debitrb.setToggleGroup(tg);
		tg.selectToggle(creditrb);
		
		//Name
		Label namelbl = new Label("Name On Card: ");
		TextField nametf = new TextField();
		nametf.setId("paymentTF");
		gp.add(namelbl, 0, 1);
		gp.add(nametf, 1, 1);
		
		//Card
		Label cardlbl = new Label("Card Number: ");
		TextField cardtf = new TextField();
		cardtf.setId("paymentTF");
		gp.add(cardlbl, 0, 2);
		gp.add(cardtf, 1, 2);
		
		//Exp Date
		Label explbl = new Label("Expiry Date: ");
		TextField exptf = new TextField();
		exptf.setId("paymentTF");
		gp.add(explbl, 0, 3);
		gp.add(exptf, 1, 3);
		
		//CVV
		Label cvvlbl = new Label("CVV: ");
		PasswordField cvvtf = new PasswordField();
		cvvtf.setId("paymentTF");
		gp.add(cvvlbl, 0, 4);
		gp.add(cvvtf, 1, 4);
		
		//Message Label
		Label msglbl = new Label();
		msglbl.setId("messagelbl");
		gp.add(msglbl, 0, 5);
		gp.setColumnSpan(msglbl, 2);
		
		//Proceed Button
		Button proceedbtn = new Button("Proceed with Payment");
		proceedbtn.setPrefSize(300, 70);
		bp.setBottom(proceedbtn);
		bp.setAlignment(proceedbtn, Pos.BOTTOM_RIGHT);
		proceedbtn.setOnAction(e->{
			
			if(nametf.getText().isEmpty() || cardtf.getText().isEmpty() || exptf.getText().isEmpty() || cvvtf.getText().isEmpty()) {
				msglbl.setText("Please fill up all fields");
			}
			//16 digits for card number  
			else if (!cardtf.getText().matches("\\d{16}")) {
				msglbl.setText("Please enter a valid Card Number!");
			}
			//2 digits / 2 digits format for expiry date
			else if(!exptf.getText().matches("\\d{2}/\\d{2}")){
				msglbl.setText("Please enter a valid Expiry Date!");
			}
			//3 digits for CVV
			else if(!cvvtf.getText().matches("\\d{3}")) {
				msglbl.setText("Please enter a valid CVV!");
			} 
			else {
				//shows payment complete
				Alert paymentComplete = new Alert(Alert.AlertType.INFORMATION);
				paymentComplete.setTitle("Payment Completion");
				paymentComplete.setHeaderText("Payment Completed!");
				paymentComplete.setContentText("Click OK to retrieve back to Movies.");
				paymentComplete.showAndWait();
				
				String movieName = chosenmovie.getMovieName();
				String cinema = chosenmovie.getCinema();
				LocalDate date = chosenmovie.getDate();
				String showtime = chosenmovie.getShowtime();
				ArrayList<String> chosenseats = chosenmovie.getSeats();
				
				chosenmovie.addArr(movieName,cinema,date,showtime,chosenseats);
				
				adultQuantity.setText("0");
				kidQuantity.setText("0");
				twinQuantity.setText("0");
				
				
				//then retrieve back to movies page
				homepagemainbp.setCenter(movies());
			}

		});
		
		return bp;
	}

	//User Profile Page
	public FlowPane userprofile(User user) {
		
		ImageView image=new ImageView();           
	    Image image1 = new Image(getClass().getResourceAsStream("/application/images/user.jpg"));
		image.setImage(image1);		

	    Label lbl1=new Label("Email: ");
		Label lbl2=new Label("Account No: ");
		Label lbl3=new Label("Profile Name: ");
		Label lbl4=new Label("IC: ");
		Label lbl5=new Label("Phone Number: ");
		Label lbl6=new Label("Date of Birth: ");
		Label lbl7=new Label("Gender: ");
		Label lbl8=new Label("Address: ");
		Label lbl9=new Label("State: ");
		Label lbl10=new Label("Postal Code: ");
		Label lbl11=new Label("Country: ");
		
		Label lbl15 = new Label("Old Password: ");
		Label lbl12=new Label("New Password: ");
		Label lbl13=new Label("Confrim Password: ");
		ToggleButton lbl14 = new ToggleButton("Change Password");
        lbl14.setId("changePass");

		Label infolbl1=new Label(user.email);
		Label infolbl2=new Label(Integer.toString(user.id));
		Label infolbl3=new Label(user.getName());
		Label infolbl4=new Label(user.getIc());
		Label infolbl5=new Label(user.getContact());
		Label infolbl6=new Label(user.getDob());
		Label infolbl7=new Label(user.getGender());
		Label infolbl8=new Label(user.getAddress());
		Label infolbl9=new Label(user.getState());
		Label infolbl10=new Label(user.getPostcode());
		Label infolbl11=new Label(user.getCountry());
		Label infolbl12= new Label(user.getPassword());

	    Button btn1=new Button("Edit Profile"); 
	    Button btn2=new Button("Update Profile"); 

	    GridPane gp=new GridPane();
		gp.setVgap(10);
		gp.setHgap(20);
		
	    gp.add(lbl1,0,0);	 gp.add(infolbl1,1,0);
	    gp.add(lbl2,0,1);	 gp.add(infolbl2,1,1);
	    gp.add(lbl3,0,2);	 gp.add(infolbl3,1,2);
	    gp.add(lbl4,0,3);	 gp.add(infolbl4,1,3);
	    gp.add(lbl5,0,4);	 gp.add(infolbl5,1,4);
	    gp.add(lbl6,0,5);	 gp.add(infolbl6,1,5);
	    gp.add(lbl7,0,6);    gp.add(infolbl7,1,6);
	    gp.add(lbl8,0,7);    gp.add(infolbl8,1,7);
	    gp.add(lbl9,0,8);    gp.add(infolbl9,1,8);
	    gp.add(lbl10,0,9);   gp.add(infolbl10,1,9);
	    gp.add(lbl11,0,10);  gp.add(infolbl11,1,10);
	    gp.add(btn1,1,11);	
	    
	    Label tf1=new Label(infolbl1.getText()); 
		Label tf2=new Label(infolbl2.getText());  
		TextField tf3=new TextField(infolbl3.getText());  
		tf3.setId("textfieldinfo");
	    TextField tf4=new TextField(infolbl4.getText());  
	    tf4.setId("textfieldinfo");
		TextField tf5=new TextField(infolbl5.getText());  
		tf5.setId("textfieldinfo");
		DatePicker tf6=new DatePicker(); 
		tf6.setId("textfieldinfo");
		DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
		tf6.setValue(LocalDate.parse(infolbl6.getText(),formatter));
		
		ComboBox<String> comboBox = new ComboBox<String>();
		comboBox.getItems().addAll("Male","Female");
		comboBox.setValue(user.getGender());
		comboBox.setPrefWidth(300.0);
		
		TextField tf8=new TextField(infolbl8.getText());  
		tf8.setId("textfieldinfo");
		TextField tf9=new TextField(infolbl9.getText());   
		tf9.setId("textfieldinfo");
	    TextField tf10=new TextField(infolbl10.getText()); 
	    tf10.setId("textfieldinfo");
	    TextField tf11=new TextField(infolbl11.getText()); 
	    tf11.setId("textfieldinfo");
	    PasswordField tf12= new PasswordField(); 
	    tf12.setId("textfieldinfo");
	    PasswordField tf13=new PasswordField(); 
	    tf13.setId("textfieldinfo");
	    PasswordField tf15=new PasswordField(); 
	    tf15.setId("textfieldinfo");
	    

    	
	    
	    Label wrn=new Label();
	    wrn.setFont(Font.font(null,15));
        wrn.setTextFill(Color.RED);
	    
	    btn1.setOnAction((e)->{
	        gp.setGridLinesVisible(true);
	        gp.getChildren().clear();
	        
	        gp.add(lbl1,0,0); gp.add(tf1,1,0);
	        gp.add(lbl2,0,1); gp.add(tf2,1,1);
		    gp.add(lbl3,0,2); gp.add(tf3,1,2);
		    gp.add(lbl4,0,3); gp.add(tf4,1,3);
	        gp.add(lbl5,0,4); gp.add(tf5,1,4);
		    gp.add(lbl6,0,5); gp.add(tf6,1,5);
	        gp.add(lbl7,0,6); gp.add(comboBox,1,6);
		    gp.add(lbl8,0,7); gp.add(tf8,1,7);
		    gp.add(lbl9,0,8); gp.add(tf9,1,8);
		    gp.add(lbl10,0,9); gp.add(tf10,1,9);
		    gp.add(lbl11,0,10); gp.add(tf11,1,10);
		    gp.add(lbl14,0,11);	
		    		    
		    gp.add(btn2,1,16);
		    
		    gp.add(wrn, 1, 17);
  		    
		    //change password fields
		    gp.add(lbl15, 0, 12);
		    gp.add(lbl12, 0,13); 
		    gp.add(lbl13, 0,14); 
		    gp.add(tf15, 1, 12);
        	gp.add(tf12,1,13);
  		    gp.add(tf13,1,14);
  		    lbl12.setVisible(false);
		    lbl13.setVisible(false);
		    lbl15.setVisible(false);
        	tf15.setVisible(false);
        	tf12.setVisible(false);
  		    tf13.setVisible(false);
		
	    });
	    
	    //change password event handler
	    lbl14.setOnAction(e->{
  		    
	    	if(lbl14.isSelected()) {
		    	lbl12.setVisible(true);
			    lbl13.setVisible(true);
			    lbl15.setVisible(true);
	        	tf15.setVisible(true);
	        	tf12.setVisible(true);
	  		    tf13.setVisible(true);
	    	}
	    	if(!lbl14.isSelected()) {
	  		    lbl12.setVisible(false);
			    lbl13.setVisible(false);
			    lbl15.setVisible(false);
	        	tf15.setVisible(false);
	        	tf12.setVisible(false);
	  		    tf13.setVisible(false);
	  		    
	  		    //clear the fields when close
	  		    tf12.clear();
	  		    tf13.clear();
	  		    tf15.clear();
	    	}

	    });
	    
	    btn2.setOnAction((e)->{
	       
	        if( tf3.getText().isEmpty()||tf4.getText().isEmpty()||tf5.getText().isEmpty()
	           ||tf8.getText().isEmpty()||tf9.getText().isEmpty()||tf10.getText().isEmpty()
	           ||tf11.getText().isEmpty()) {
	         wrn.setVisible(true);
             wrn.setText("Please Key in all the fields above");
             
	        }
	        
	        
	        //check if old password field is empty
	        else if (!tf15.getText().isEmpty() && !(tf15.getText().equals(user.getPassword()))) {
	        	wrn.setVisible(true);
	            wrn.setText("Old Password incorrect");
   
	        }
	       
	        else if (!tf12.getText().equals(tf13.getText())) {
	        	wrn.setVisible(true);
	             wrn.setText("New Password doesn't match");
	             
	        }
	        
	        else {
	        gp.setGridLinesVisible(true);
		    gp.getChildren().clear();
		   
	        infolbl3.setText(tf3.getText());  
	        infolbl4.setText(tf4.getText());
	        infolbl5.setText(tf5.getText());
	        infolbl6.setText(tf6.getValue().toString());
	        infolbl7.setText(comboBox.getValue());
	        infolbl8.setText(tf8.getText());
	        infolbl9.setText(tf9.getText());
	        infolbl10.setText(tf10.getText());
	        infolbl11.setText(tf11.getText());
	        infolbl12.setText(tf12.getText());
	        
	        gp.add(lbl1,0,0);	 gp.add(infolbl1,1,0);
		    gp.add(lbl2,0,1);    gp.add(infolbl2,1,1);
		    gp.add(lbl3,0,2);    gp.add(infolbl3,1,2);
		    gp.add(lbl4,0,3);    gp.add(infolbl4,1,3);
		    gp.add(lbl5,0,4);    gp.add(infolbl5,1,4);
		    gp.add(lbl6,0,5);    gp.add(infolbl6,1,5);
		    gp.add(lbl7,0,6);    gp.add(infolbl7,1,6);
		    gp.add(lbl8,0,7);    gp.add(infolbl8,1,7);
		    gp.add(lbl9,0,8);	 gp.add(infolbl9,1,8);
		    gp.add(lbl10,0,9);	  gp.add(infolbl10,1,9);
		    gp.add(lbl11,0,10);	  gp.add(infolbl11,1,10);
		    gp.add(btn1,1,11);	

		    user.setName(tf3.getText());
		    user.setIc(tf4.getText());
		    user.setGender(comboBox.getValue());
		    user.setContact(tf5.getText());
		    user.setDob(tf6.getValue().toString());
		    user.setAddress(tf8.getText());
		    user.setState(tf9.getText());
		    user.setPostcode(tf10.getText());
		    user.setCountry(tf11.getText());
		   
		    //check if both old and new password is key-ed in
		    if(!tf12.getText().isEmpty() && !tf15.getText().isEmpty()) {
		    	user.setPassword(tf12.getText());
		    }
		    
		    //set password incorrect label not visible
		    wrn.setVisible(false);
		    
		    }
	   });
		
     FlowPane fl=new FlowPane(image,gp);
     fl.setHgap(100);
     fl.setAlignment(Pos.CENTER);
     
     return fl;
	}
	
	
	
	//--------------------------------------------------------List of Movies------------------------------------------------------------- 
	//-------------------------Now Showing------------------------------

	//First Movie
	public BorderPane movieNS1() throws MalformedURLException{
		
		//setting back button to be visible
		backbtn.setVisible(true);
		//Back Button
		backbtn.setOnAction(e->{
			homepagemainbp.setCenter(movies());
		});
		
			
		//==================================Left Side=================================
			
		//Vbox for Image, Genre, Language, Duration, Date Released & PG Rating
		VBox LeftSide = new VBox(20);
		LeftSide.setPadding(new Insets(5,0,0,20));
			
		//Movie Image
		Image NowShowing1 = new Image(getClass().getResourceAsStream("/application/images/A Simple Favour.jpg"));
		ImageView Image1 = new ImageView();
		Image1.setImage(NowShowing1);
		Image1.setFitWidth(300);
		Image1.setFitHeight(420);
		Image1.setSmooth(true);
		Image1.setCache(true);
		
		//Genre
		Text Genre = new Text();
		Genre.setText("Genre: Drama/Thriller/Crime");
		Genre.setFont(Font.font(null, 20));
		Genre.setFill(Color.BLACK);
		
		//Language
		Text Language = new Text();
		Language.setText("Language: English");
		Language.setFont(Font.font(null, 20));
		Language.setFill(Color.BLACK);
		
		//Duration
		Text Duration = new Text();
		Duration.setText("Duration: 115 Minutes");
		Duration.setFont(Font.font(null, 20));
		Duration.setFill(Color.BLACK);
		
		//Date Released
		Text DR = new Text();
		DR.setText("Date Released: 11 Oct 2018");
		DR.setFont(Font.font(null, 20));
		DR.setFill(Color.BLACK);
		
		//PG Rating
		Text PG = new Text();
		PG.setText("          18 ABOVE");
		PG.setFont(Font.font(null, 20));
		PG.setFill(Color.FIREBRICK);
		
		
		LeftSide.getChildren().addAll(Image1, Genre, Language, Duration, DR, PG);
		//==================================Right Side=================================
		
		//VBox for Description
		VBox RightSide = new VBox(40);
		
		
		//Name for Movie
		Text Name1 = new Text();
		Name1.setText("A Simple Favour");
		Name1.setFont(Font.font(null, FontWeight.BOLD, 35));
		Name1.setFill(Color.FIREBRICK);
				
		//Description for movie
		Text text1 = new Text();
		text1.setText("A SIMPLE FAVOR, directed by Paul Feig, centers around Stephanie (Anna Kendrick),"
				+ "a mommy vlogger who seeks to uncover the truth behind her best friend Emily's"
				+ "(Blake Lively) sudden disappearance from their small town.");
		text1.setFont(Font.font (null, 20));
		text1.setWrappingWidth(700);
		text1.setTextAlignment(TextAlignment.JUSTIFY);
		
		//Trailer
		URL path = getClass().getResource("/application/media/A Simple Favor.mp4");
	
		Media Trailer1 = new Media(path.toString());
		MediaPlayer player = new MediaPlayer (Trailer1);
		MediaView NS1T = new MediaView(player);
		NS1T.setFitWidth(420);
		NS1T.setFitHeight(300);
		NS1T.setSmooth(true);
			
	
		RightSide.getChildren().addAll(Name1, text1, NS1T);	
		//================================Creating The Layout=================================
		//HBox For Movie & Desc
		HBox movie1 = new HBox(30);
			
	
		movie1.getChildren().addAll(LeftSide, RightSide);
		
		BorderPane bp = new BorderPane();
		bp.setCenter(movie1);
		bp.setPadding(new Insets(20,20,20,20));
		
		Button buyNow = new Button("Buy Now");
		buyNow.setPrefSize(150, 70);
		bp.setBottom(buyNow);
		bp.setAlignment(buyNow, Pos.BOTTOM_RIGHT);
		
		buyNow.setOnAction(e->{
			chosenmovie.setPath("/application/images/A Simple Favour.jpg");
			chosenmovie.setMovieName("A Simple Favour");
			chosenmovie.setGenre("Drama/Thriller/Crime");
			chosenmovie.setLanguage("English");
			chosenmovie.setDuration("115minutes");
			
			
			homepagemainbp.setCenter(choosingShowtime());
		});
		
		
		
		//video play and pause
		NS1T.setOnMouseClicked(e->{
	
			if(player.getStatus().equals(Status.PLAYING)) {
				player.pause();
			}
			if(player.getStatus().equals(Status.PAUSED)) {
				player.play();
			}
		});
	
		
		player.play();
		player.setCycleCount(MediaPlayer.INDEFINITE);
		
		return bp;
	
	}
	
	//Second Movie
	public BorderPane movieNS2() throws MalformedURLException{

		//setting back button to be visible
		backbtn.setVisible(true);
		//Back Button
		backbtn.setOnAction(e->{
			homepagemainbp.setCenter(movies());
		});

		
		//==================================Left Side=================================

			
		
		//Vbox for Image, Genre, Language, Duration, Date Released & PG Rating
		VBox LeftSide = new VBox(20);
		LeftSide.setPadding(new Insets(5,0,0,20));
			
		//Movie Image
		Image NowShowing2 = new Image(getClass().getResourceAsStream("/application/images/CRA.png"));
		ImageView Image2 = new ImageView();
		Image2.setImage(NowShowing2);
		Image2.setFitWidth(300);
		Image2.setFitHeight(420);
		Image2.setSmooth(true);
		Image2.setCache(true);
		
		//Genre
		Text Genre = new Text();
		Genre.setText("Genre: Comedy/Drama");
		Genre.setFont(Font.font(null, 20));
		Genre.setFill(Color.BLACK);
		
		//Language
		Text Language = new Text();
		Language.setText("Language: English");
		Language.setFont(Font.font(null, 20));
		Language.setFill(Color.BLACK);
		
		//Duration
		Text Duration = new Text();
		Duration.setText("Duration: 121 Minutes");
		Duration.setFont(Font.font(null, 20));
		Duration.setFill(Color.BLACK);
		
		//Date Released
		Text DR = new Text();
		DR.setText("Date Released: 23 Aug 2018");
		DR.setFont(Font.font(null, 20));
		DR.setFill(Color.BLACK);
		
		//PG Rating
		Text PG = new Text();
		PG.setText("          PG13");
		PG.setFont(Font.font(null, 20));
		PG.setFill(Color.ORANGE);
		
		
		LeftSide.getChildren().addAll(Image2, Genre, Language, Duration, DR, PG);
		//==================================Right Side=================================
		
		//VBox for Description
		VBox RightSide = new VBox(40);
		
		
		//Name for Movie
		Text Name2 = new Text();
		Name2.setText("Crazy Rich Asians");
		Name2.setFont(Font.font(null, FontWeight.BOLD, 35));
		Name2.setFill(Color.FIREBRICK);
				
		//Description for movie
		Text text2 = new Text();
		text2.setText("Three wealthy Chinese families prepare for the wedding of the year.");
		text2.setFont(Font.font (null, 20));
		text2.setWrappingWidth(700);
		text2.setTextAlignment(TextAlignment.JUSTIFY);
		
		//Trailer
		URL path = getClass().getResource("/application/media/Crazy Rich Asians.mp4");
		
		Media Trailer2 = new Media(path.toString());
		MediaPlayer player = new MediaPlayer (Trailer2);
		MediaView NS2T = new MediaView(player);
		NS2T.setFitWidth(420);
		NS2T.setFitHeight(300);
		NS2T.setSmooth(true);
		

		RightSide.getChildren().addAll(Name2, text2, NS2T);	
		//================================Creating The Layout=================================
		//HBox For Movie & Desc
		HBox movie2 = new HBox(30);
			

		movie2.getChildren().addAll(LeftSide, RightSide);
		
		
		//BorderPane for align
		BorderPane bp = new BorderPane();
		bp.setCenter(movie2);
		bp.setPadding(new Insets(20,20,20,20));
		
		Button buyNow = new Button("Buy Now");
		buyNow.setPrefSize(150, 70);
		bp.setBottom(buyNow);
		bp.setAlignment(buyNow, Pos.BOTTOM_RIGHT);
		
		buyNow.setOnAction(e->{
			chosenmovie.setPath("/application/images/CRA.png");
			chosenmovie.setMovieName("Crazy Rich Asians");
			chosenmovie.setGenre("Comedy/Drama");
			chosenmovie.setLanguage("English");
			chosenmovie.setDuration("121 Minutes");
			
			
			homepagemainbp.setCenter(choosingShowtime());
		});
		
		
		
		//video play and pause
		NS2T.setOnMouseClicked(e->{

			if(player.getStatus().equals(Status.PLAYING)) {
				player.pause();
			}
			if(player.getStatus().equals(Status.PAUSED)) {
				player.play();
			}
		});

		
		player.play();
		player.setCycleCount(MediaPlayer.INDEFINITE);
		
		return bp;
	}
	
	//Third Movie
	public BorderPane movieNS3() throws MalformedURLException{
	
		//setting back button to be visible
		backbtn.setVisible(true);
		//Back Button
		backbtn.setOnAction(e->{
			homepagemainbp.setCenter(movies());
		});

		
		
		//==================================Left Side=================================
		
		//Vbox for Image, Genre, Language, Duration, Date Released & PG Rating
		VBox LeftSide = new VBox(20);
		LeftSide.setPadding(new Insets(5,0,0,20));
			
		//Movie Image
		Image NowShowing3 = new Image(getClass().getResourceAsStream("/application/images/First Man.jpg"));
		ImageView Image3 = new ImageView();
		Image3.setImage(NowShowing3);
		Image3.setFitWidth(300);
		Image3.setFitHeight(420);
		Image3.setSmooth(true);
		Image3.setCache(true);
		
		//Genre
		Text Genre = new Text();
		Genre.setText("Genre: Biography/Drama/Historical");
		Genre.setFont(Font.font(null, 20));
		Genre.setFill(Color.BLACK);
		
		//Language
		Text Language = new Text();
		Language.setText("Language: English");
		Language.setFont(Font.font(null, 20));
		Language.setFill(Color.BLACK);
		
		//Duration
		Text Duration = new Text();
		Duration.setText("Duration: 142 Minutes");
		Duration.setFont(Font.font(null, 20));
		Duration.setFill(Color.BLACK);
		
		//Date Released
		Text DR = new Text();
		DR.setText("Date Released: 18 Oct 2018");
		DR.setFont(Font.font(null, 20));
		DR.setFill(Color.BLACK);
		
		//PG Rating
		Text PG = new Text();
		PG.setText("          PG13");
		PG.setFont(Font.font(null, 20));
		PG.setFill(Color.ORANGE);
		
		
		LeftSide.getChildren().addAll(Image3, Genre, Language, Duration, DR, PG);
		//==================================Right Side=================================
		
		//VBox for Description
		VBox RightSide = new VBox(40);
		
		
		//Name for Movie
		Text Name3 = new Text();
		Name3.setText("First Man");
		Name3.setFont(Font.font(null, FontWeight.BOLD, 35));
		Name3.setFill(Color.FIREBRICK);
				
		//Description for movie
		Text text3 = new Text();
		text3.setText("A look at the life of the astronaut, Neil Armstrong, and the legendary space mission "
				+ "that led him to become the first man to walk on the Moon on July 20, 1969.");
		text3.setFont(Font.font (null, 20));
		text3.setWrappingWidth(700);
		text3.setTextAlignment(TextAlignment.JUSTIFY);
		
		//Trailer
		URL path = getClass().getResource("/application/media/First Man.mp4");
		
		Media Trailer3 = new Media(path.toString());
		MediaPlayer player = new MediaPlayer (Trailer3);
		MediaView NS3T = new MediaView(player);
		NS3T.setFitWidth(420);
		NS3T.setFitHeight(300);
		NS3T.setSmooth(true);
		

		RightSide.getChildren().addAll(Name3, text3, NS3T);	
		//================================Creating The Layout=================================
		//HBox For Movie & Desc
		HBox movie3 = new HBox(30);
			

		movie3.getChildren().addAll(LeftSide, RightSide);
		
		
		//BorderPane for align
		BorderPane bp = new BorderPane();
		bp.setCenter(movie3);
		bp.setPadding(new Insets(20,20,20,20));
		
		Button buyNow = new Button("Buy Now");
		buyNow.setPrefSize(150, 70);
		bp.setBottom(buyNow);
		bp.setAlignment(buyNow, Pos.BOTTOM_RIGHT);
		
		buyNow.setOnAction(e->{
			chosenmovie.setPath("/application/images/First Man.jpg");
			chosenmovie.setMovieName("First Man");
			chosenmovie.setGenre("Biography/Drama/Historical");
			chosenmovie.setLanguage("English");
			chosenmovie.setDuration("142minutes");
			
			
			homepagemainbp.setCenter(choosingShowtime());
		});
		
		
		
		//video play and pause
		NS3T.setOnMouseClicked(e->{

			if(player.getStatus().equals(Status.PLAYING)) {
				player.pause();
			}
			if(player.getStatus().equals(Status.PAUSED)) {
				player.play();
			}
		});

		
		player.play();
		player.setCycleCount(MediaPlayer.INDEFINITE);
		
		return bp;
	}
	
	//Forth Movie
	public BorderPane movieNS4() throws MalformedURLException{

		//setting back button to be visible
		backbtn.setVisible(true);
		//Back Button
		backbtn.setOnAction(e->{
			homepagemainbp.setCenter(movies());
		});

		
		//==================================Left Side=================================	
		
		//Vbox for Image, Genre, Language, Duration, Date Released & PG Rating
		VBox LeftSide = new VBox(20);
		LeftSide.setPadding(new Insets(5,0,0,20));
			
		//Movie Image
		Image NowShowing4 = new Image(getClass().getResourceAsStream("/application/images/Golden Job.jpg"));
		ImageView Image4 = new ImageView();
		Image4.setImage(NowShowing4);
		Image4.setFitWidth(300);
		Image4.setFitHeight(420);
		Image4.setSmooth(true);
		Image4.setCache(true);
		
		//Genre
		Text Genre = new Text();
		Genre.setText("Genre: Action");
		Genre.setFont(Font.font(null, 20));
		Genre.setFill(Color.BLACK);
		
		//Language
		Text Language = new Text();
		Language.setText("Language: Cantonese");
		Language.setFont(Font.font(null, 20));
		Language.setFill(Color.BLACK);
		
		//Duration
		Text Duration = new Text();
		Duration.setText("Duration: 100 Minutes");
		Duration.setFont(Font.font(null, 20));
		Duration.setFill(Color.BLACK);
		
		//Date Released
		Text DR = new Text();
		DR.setText("Date Released: 27 Sept 2018");
		DR.setFont(Font.font(null, 20));
		DR.setFill(Color.BLACK);
		
		//PG Rating
		Text PG = new Text();
		PG.setText("          18 ABOVE");
		PG.setFont(Font.font(null, 20));
		PG.setFill(Color.FIREBRICK);
		
		LeftSide.getChildren().addAll(Image4, Genre, Language, Duration, DR, PG);
		//==================================Right Side=================================
		
		//VBox for Description
		VBox RightSide = new VBox(40);
		
		
		//Name for Movie
		Text Name4 = new Text();
		Name4.setText("Golden Job");
		Name4.setFont(Font.font(null, FontWeight.BOLD, 35));
		Name4.setFill(Color.FIREBRICK);
				
		//Description for movie
		Text text4 = new Text();
		text4.setText("Five con artists are planning to retire soon. However, while they are throwing an innocent"
				+ " bachelor party, they unwittingly get caught up in a heist planned by an international drug lord.");
		text4.setFont(Font.font (null, 20));
		text4.setWrappingWidth(700);
		text4.setTextAlignment(TextAlignment.JUSTIFY);
		
		//Trailer
		URL path = getClass().getResource("/application/media/Golden Job.mp4");
		
		Media Trailer4 = new Media(path.toString());
		MediaPlayer player = new MediaPlayer (Trailer4);
		MediaView NS4T = new MediaView(player);
		NS4T.setFitWidth(420);
		NS4T.setFitHeight(300);
		NS4T.setSmooth(true);
		

		RightSide.getChildren().addAll(Name4, text4, NS4T);	
		//================================Creating The Layout=================================
		//HBox For Movie & Desc
		HBox movie4 = new HBox(30);
			

		movie4.getChildren().addAll(LeftSide, RightSide);
		
		
		//BorderPane for align
		BorderPane bp = new BorderPane();
		bp.setCenter(movie4);
		bp.setPadding(new Insets(20,20,20,20));
		
		Button buyNow = new Button("Buy Now");
		buyNow.setPrefSize(150, 70);
		bp.setBottom(buyNow);
		bp.setAlignment(buyNow, Pos.BOTTOM_RIGHT);
		
		buyNow.setOnAction(e->{
			chosenmovie.setPath("/application/images/Golden Job.jpg");
			chosenmovie.setMovieName("Golden Job");
			chosenmovie.setGenre("Action");
			chosenmovie.setLanguage("Cantonese");
			chosenmovie.setDuration("100minutes");
			
			
			homepagemainbp.setCenter(choosingShowtime());
		});
		
		
		
		//video play and pause
		NS4T.setOnMouseClicked(e->{

			if(player.getStatus().equals(Status.PLAYING)) {
				player.pause();
			}
			if(player.getStatus().equals(Status.PAUSED)) {
				player.play();
			}
		});

		
		player.play();
		player.setCycleCount(MediaPlayer.INDEFINITE);
		
		
		return bp;
	}

	//Fifth Movie
	public BorderPane movieNS5() throws MalformedURLException{

		//setting back button to be visible
		backbtn.setVisible(true);
		//Back Button
		backbtn.setOnAction(e->{
			homepagemainbp.setCenter(movies());
		});

		
		//==================================Left Side=================================
		
		//Vbox for Image, Genre, Language, Duration, Date Released & PG Rating
		VBox LeftSide = new VBox(20);
		LeftSide.setPadding(new Insets(5,0,0,20));
			
		//Movie Image
		Image NowShowing5 = new Image(getClass().getResourceAsStream("/application/images/Malicious.jpg"));
		ImageView Image5 = new ImageView();
		Image5.setImage(NowShowing5);
		Image5.setFitWidth(300);
		Image5.setFitHeight(420);
		Image5.setSmooth(true);
		Image5.setCache(true);
		
		//Genre
		Text Genre = new Text();
		Genre.setText("Genre: Horror");
		Genre.setFont(Font.font(null, 20));
		Genre.setFill(Color.BLACK);
		
		//Language
		Text Language = new Text();
		Language.setText("Language: English");
		Language.setFont(Font.font(null, 20));
		Language.setFill(Color.BLACK);
		
		//Duration
		Text Duration = new Text();
		Duration.setText("Duration: 88 Minutes");
		Duration.setFont(Font.font(null, 20));
		Duration.setFill(Color.BLACK);
		
		//Date Released
		Text DR = new Text();
		DR.setText("Date Released: 18 Oct 2018");
		DR.setFont(Font.font(null, 20));
		DR.setFill(Color.BLACK);
		
		//PG Rating
		Text PG = new Text();
		PG.setText("          18 ABOVE");
		PG.setFont(Font.font(null, 20));
		PG.setFill(Color.FIREBRICK);
		
		LeftSide.getChildren().addAll(Image5, Genre, Language, Duration, DR, PG);
		//==================================Right Side=================================
		
		//VBox for Description
		VBox RightSide = new VBox(40);
		
		
		//Name for Movie
		Text Name5 = new Text();
		Name5.setText("Malicious");
		Name5.setFont(Font.font(null, FontWeight.BOLD, 35));
		Name5.setFill(Color.FIREBRICK);
				
		//Description for movie
		Text text5 = new Text();
		text5.setText("A couple in an unstable relationship become stalked by an unseen adversary who threatens"				
				+ " the normalcy of their home life. As tensions and fears begin to escalate, the line becomes"				
				+ " blurred on whether the true danger remains on the outside of their home, or on the inside.");
		text5.setFont(Font.font (null, 20));
		text5.setWrappingWidth(700);
		text5.setTextAlignment(TextAlignment.JUSTIFY);
		
		//Trailer
		URL path = getClass().getResource("/application/media/Malicious.mp4");
		
		Media Trailer5 = new Media(path.toString());
		MediaPlayer player = new MediaPlayer (Trailer5);
		MediaView NS5T = new MediaView(player);
		NS5T.setFitWidth(420);
		NS5T.setFitHeight(300);
		NS5T.setSmooth(true);
		

		RightSide.getChildren().addAll(Name5, text5, NS5T);	
		//================================Creating The Layout=================================
		//HBox For Movie & Desc
		HBox movie5 = new HBox(30);
			

		movie5.getChildren().addAll(LeftSide, RightSide);
		
		
		//BorderPane for align
		BorderPane bp = new BorderPane();
		bp.setCenter(movie5);
		bp.setPadding(new Insets(20,20,20,20));
		
		Button buyNow = new Button("Buy Now");
		buyNow.setPrefSize(150, 70);
		bp.setBottom(buyNow);
		bp.setAlignment(buyNow, Pos.BOTTOM_RIGHT);
		
		buyNow.setOnAction(e->{
			chosenmovie.setPath("/application/images/Malicious.jpg");
			chosenmovie.setMovieName("Malicious");
			chosenmovie.setGenre("Horror");
			chosenmovie.setLanguage("English");
			chosenmovie.setDuration("88minutes");
			
			
			homepagemainbp.setCenter(choosingShowtime());
		});
		
		
		
		//video play and pause
		NS5T.setOnMouseClicked(e->{

			if(player.getStatus().equals(Status.PLAYING)) {
				player.pause();
			}
			if(player.getStatus().equals(Status.PAUSED)) {
				player.play();
			}
		});

		
		player.play();
		player.setCycleCount(MediaPlayer.INDEFINITE);

		return bp;
	}

	//Sixth Movie
	public BorderPane movieNS6() throws MalformedURLException{

		//setting back button to be visible
		backbtn.setVisible(true);
		//Back Button
		backbtn.setOnAction(e->{
			homepagemainbp.setCenter(movies());
		});

		
		//==================================Left Side=================================

		//Vbox for Image, Genre, Language, Duration, Date Released & PG Rating
		VBox LeftSide = new VBox(20);
		LeftSide.setPadding(new Insets(5,0,0,20));
			
		//Movie Image
		Image NowShowing6 = new Image(getClass().getResourceAsStream("/application/images/Paskal.jpg"));
		ImageView Image6 = new ImageView();
		Image6.setImage(NowShowing6);
		Image6.setFitWidth(300);
		Image6.setFitHeight(420);
		Image6.setSmooth(true);
		Image6.setCache(true);
		
		//Genre
		Text Genre = new Text();
		Genre.setText("Genre: Action/Drama");
		Genre.setFont(Font.font(null, 20));
		Genre.setFill(Color.BLACK);
		
		//Language
		Text Language = new Text();
		Language.setText("Language: Malay");
		Language.setFont(Font.font(null, 20));
		Language.setFill(Color.BLACK);
		
		//Duration
		Text Duration = new Text();
		Duration.setText("Duration: 115 Minutes");
		Duration.setFont(Font.font(null, 20));
		Duration.setFill(Color.BLACK);
		
		//Date Released
		Text DR = new Text();
		DR.setText("Date Released: 27 Sept 2018");
		DR.setFont(Font.font(null, 20));
		DR.setFill(Color.BLACK);
		
		//PG Rating
		Text PG = new Text();
		PG.setText("          PG13");
		PG.setFont(Font.font(null, 20));
		PG.setFill(Color.ORANGE);
		
		
		LeftSide.getChildren().addAll(Image6, Genre, Language, Duration, DR, PG);
		//==================================Right Side=================================
		
		//VBox for Description
		VBox RightSide = new VBox(40);
		
		
		//Name for Movie
		Text Name6 = new Text();
		Name6.setText("Paskal");
		Name6.setFont(Font.font(null, FontWeight.BOLD, 35));
		Name6.setFill(Color.FIREBRICK);
				
		//Description for movie
		Text text6 = new Text();
		text6.setText("PASKAL, or Pasukan Khas Laut, is an elite unit in the Royal Malaysian Navy. The movie"
				+ " follows the true events of PASKAL's Lieutenant Commander Arman Anwar and "
				+ "his team's mission to rescue a tanker, MV Bunga Laurel, that"
				+ " was hijacked by Somalian Pirates in 2011.");
		text6.setFont(Font.font (null, 20));
		text6.setWrappingWidth(700);
		text6.setTextAlignment(TextAlignment.JUSTIFY);
		
		//Trailer
		URL path = getClass().getResource("/application/media/Paskal.mp4");
		
		Media Trailer6 = new Media(path.toString());
		MediaPlayer player = new MediaPlayer (Trailer6);
		MediaView NS6T = new MediaView(player);
		NS6T.setFitWidth(420);
		NS6T.setFitHeight(300);
		NS6T.setSmooth(true);
		

		RightSide.getChildren().addAll(Name6, text6, NS6T);	
		//================================Creating The Layout=================================
		//HBox For Movie & Desc
		HBox movie6 = new HBox(30);
			

		movie6.getChildren().addAll(LeftSide, RightSide);
		
		
		//BorderPane for align
		BorderPane bp = new BorderPane();
		bp.setCenter(movie6);
		bp.setPadding(new Insets(20,20,20,20));
		
		Button buyNow = new Button("Buy Now");
		buyNow.setPrefSize(150, 70);
		bp.setBottom(buyNow);
		bp.setAlignment(buyNow, Pos.BOTTOM_RIGHT);
		
		buyNow.setOnAction(e->{
			chosenmovie.setPath("/application/images/Paskal.jpg");
			chosenmovie.setMovieName("Paskal");
			chosenmovie.setGenre("Action/Drama");
			chosenmovie.setLanguage("Malay");
			chosenmovie.setDuration("115minutes");
			
			
			homepagemainbp.setCenter(choosingShowtime());
		});
		
		
		
		//video play and pause
		NS6T.setOnMouseClicked(e->{

			if(player.getStatus().equals(Status.PLAYING)) {
				player.pause();
			}
			if(player.getStatus().equals(Status.PAUSED)) {
				player.play();
			}
		});

		
		player.play();
		player.setCycleCount(MediaPlayer.INDEFINITE);

		
		return bp;
	}

	//Seventh Movie
	public BorderPane movieNS7() throws MalformedURLException{

		//setting back button to be visible
		backbtn.setVisible(true);
		//Back Button
		backbtn.setOnAction(e->{
			homepagemainbp.setCenter(movies());
		});

		
		//==================================Left Side=================================

		//Vbox for Image, Genre, Language, Duration, Date Released & PG Rating
		VBox LeftSide = new VBox(20);
		LeftSide.setPadding(new Insets(5,0,0,20));
			
		//Movie Image
		Image NowShowing7 = new Image(getClass().getResourceAsStream("/application/images/Venom.jpg"));
		ImageView Image7 = new ImageView();
		Image7.setImage(NowShowing7);
		Image7.setFitWidth(300);
		Image7.setFitHeight(420);
		Image7.setSmooth(true);
		Image7.setCache(true);
		
		//Genre
		Text Genre = new Text();
		Genre.setText("Genre: Action/Science Ficton/Horror");
		Genre.setFont(Font.font(null, 20));
		Genre.setFill(Color.BLACK);
		
		//Language
		Text Language = new Text();
		Language.setText("Language: English");
		Language.setFont(Font.font(null, 20));
		Language.setFill(Color.BLACK);
		
		//Duration
		Text Duration = new Text();
		Duration.setText("Duration: 113 Minutes");
		Duration.setFont(Font.font(null, 20));
		Duration.setFill(Color.BLACK);
		
		//Date Released
		Text DR = new Text();
		DR.setText("Date Released: 4 Oct 2018");
		DR.setFont(Font.font(null, 20));
		DR.setFill(Color.BLACK);
		
		//PG Rating
		Text PG = new Text();
		PG.setText("          PG13");
		PG.setFont(Font.font(null, 20));
		PG.setFill(Color.ORANGE);
		
		
		LeftSide.getChildren().addAll(Image7, Genre, Language, Duration, DR, PG);
		//==================================Right Side=================================
		
		//VBox for Description
		VBox RightSide = new VBox(40);
		
		
		//Name for Movie
		Text Name7 = new Text();
		Name7.setText("Venom");
		Name7.setFont(Font.font(null, FontWeight.BOLD, 35));
		Name7.setFill(Color.FIREBRICK);
				
		//Description for movie
		Text text7 = new Text();
		text7.setText("One of Marvel's most enigmatic, complex and badass characters comes to the big"
				+ " screen, starring Academy Award nominated actor Tom Hardy as the lethal protector Venom.");
		text7.setFont(Font.font (null, 20));
		text7.setWrappingWidth(700);
		text7.setTextAlignment(TextAlignment.JUSTIFY);
		
		//Trailer
		URL path = getClass().getResource("/application/media/Venom.mp4");
		
		Media Trailer7 = new Media(path.toString());
		MediaPlayer player = new MediaPlayer (Trailer7);
		MediaView NS7T = new MediaView(player);
		NS7T.setFitWidth(420);
		NS7T.setFitHeight(300);
		NS7T.setSmooth(true);
		

		RightSide.getChildren().addAll(Name7, text7, NS7T);	
		//================================Creating The Layout=================================
		//HBox For Movie & Desc
		HBox movie7 = new HBox(30);
			

		movie7.getChildren().addAll(LeftSide, RightSide);
		
		
		//BorderPane for align
		BorderPane bp = new BorderPane();
		bp.setCenter(movie7);
		bp.setPadding(new Insets(20,20,20,20));
		
		Button buyNow = new Button("Buy Now");
		buyNow.setPrefSize(150, 70);
		bp.setBottom(buyNow);
		bp.setAlignment(buyNow, Pos.BOTTOM_RIGHT);
		
		buyNow.setOnAction(e->{
			chosenmovie.setPath("/application/images/Venom.jpg");
			chosenmovie.setMovieName("Venom");
			chosenmovie.setGenre("Action/Science Ficton/Horror");
			chosenmovie.setLanguage("English");
			chosenmovie.setDuration("113minutes");
			
			
			homepagemainbp.setCenter(choosingShowtime());
		});
		
		
		
		//video play and pause
		NS7T.setOnMouseClicked(e->{

			if(player.getStatus().equals(Status.PLAYING)) {
				player.pause();
			}
			if(player.getStatus().equals(Status.PAUSED)) {
				player.play();
			}
		});

		
		player.play();
		player.setCycleCount(MediaPlayer.INDEFINITE);

		
		return bp;
	}

	
	//-----------------------Coming Soon---------------------------------
	
	//First Movie
	public BorderPane movieCS1() throws MalformedURLException {

		//setting back button to be visible
		backbtn.setVisible(true);
		//Back Button
		backbtn.setOnAction(e->{
			homepagemainbp.setCenter(movies());
		});
		
		//==================================Left Side=================================

		//Vbox for Image, Genre, Language, Duration, Date Released & PG Rating
		VBox LeftSide = new VBox(20);
		LeftSide.setPadding(new Insets(5,0,0,20));
			
		//Movie Image
		Image ComingSoon1 = new Image(getClass().getResourceAsStream("/application/images/Bohemian Rhapsody.jpg"));
		ImageView Image8 = new ImageView();
		Image8.setImage(ComingSoon1);
		Image8.setFitWidth(300);
		Image8.setFitHeight(420);
		Image8.setSmooth(true);
		Image8.setCache(true);
		
		//Genre
		Text Genre = new Text();
		Genre.setText("Genre: Biography/Drama/Musical");
		Genre.setFont(Font.font(null, 20));
		Genre.setFill(Color.BLACK);
		
		//Language
		Text Language = new Text();
		Language.setText("Language: English");
		Language.setFont(Font.font(null, 20));
		Language.setFill(Color.BLACK);
		
		//Duration
		Text Duration = new Text();
		Duration.setText("Duration: 131 Minutes");
		Duration.setFont(Font.font(null, 20));
		Duration.setFill(Color.BLACK);
		
		//Date Released
		Text DR = new Text();
		DR.setText("Date Released: 8 Nov 2018");
		DR.setFont(Font.font(null, 20));
		DR.setFill(Color.BLACK);
		
		//PG Rating
		Text PG = new Text();
		PG.setText("          18 ABOVE");
		PG.setFont(Font.font(null, 20));
		PG.setFill(Color.FIREBRICK);
		
		
		LeftSide.getChildren().addAll(Image8, Genre, Language, Duration, DR, PG);
		//==================================Right Side=================================
		
		//VBox for Description
		VBox RightSide = new VBox(40);
		
		
		//Name for Movie
		Text Name8 = new Text();
		Name8.setText("Bohemian Rhapsody");
		Name8.setFont(Font.font(null, FontWeight.BOLD, 35));
		Name8.setFill(Color.FIREBRICK);
				
		//Description for movie
		Text text8 = new Text();
		text8.setText("Bohemian Rhapsody is a foot-stomping celebration of Queen, their music and their extraordinary lead singer Freddie"
				+ " Mercury. Freddie defied stereotypes and shattered convention to become one of the most beloved entertainers"
				+ " on the planet. The film traces the meteoric rise of the band through their iconic songs and revolutionary sound. ");
		text8.setFont(Font.font (null, 20));
		text8.setWrappingWidth(700);
		text8.setTextAlignment(TextAlignment.JUSTIFY);
		
		//Trailer
		URL path = getClass().getResource("/application/media/Bohemian Rhapsody.mp4");
		
		Media Trailer8 = new Media(path.toString());
		MediaPlayer player = new MediaPlayer (Trailer8);
		MediaView CS1T = new MediaView(player);
		CS1T.setFitWidth(420);
		CS1T.setFitHeight(300);
		CS1T.setSmooth(true);
		

		RightSide.getChildren().addAll(Name8, text8, CS1T);	
		//================================Creating The Layout=================================
		//HBox For Movie & Desc
		HBox movie8 = new HBox(30);
			

		movie8.getChildren().addAll(LeftSide, RightSide);
		
		
		//BorderPane for align
		BorderPane bp = new BorderPane();
		bp.setCenter(movie8);

		//video play and pause
		CS1T.setOnMouseClicked(e->{

			if(player.getStatus().equals(Status.PLAYING)) {
				player.pause();
			}
			if(player.getStatus().equals(Status.PAUSED)) {
				player.play();
			}
		});

		
		player.play();
		player.setCycleCount(MediaPlayer.INDEFINITE);
		
		return bp;
	}
	
	//Second Movie
	public BorderPane movieCS2() throws MalformedURLException {

		//setting back button to be visible
		backbtn.setVisible(true);
		//Back Button
		backbtn.setOnAction(e->{
			homepagemainbp.setCenter(movies());
		});
		
		//==================================Left Side=================================

		//Vbox for Image, Genre, Language, Duration, Date Released & PG Rating
		VBox LeftSide = new VBox(20);
		LeftSide.setPadding(new Insets(5,0,0,20));
			
		//Movie Image
		Image ComingSoon2 = new Image(getClass().getResourceAsStream("/application/images/Fantastic Beasts 2.jpg"));
		ImageView Image9 = new ImageView();
		Image9.setImage(ComingSoon2);
		Image9.setFitWidth(300);
		Image9.setFitHeight(420);
		Image9.setSmooth(true);
		Image9.setCache(true);
		
		//Genre
		Text Genre = new Text();
		Genre.setText("Genre: Adventure/Family/Fantasy");
		Genre.setFont(Font.font(null, 20));
		Genre.setFill(Color.BLACK);
		
		//Language
		Text Language = new Text();
		Language.setText("Language: English");
		Language.setFont(Font.font(null, 20));
		Language.setFill(Color.BLACK);
		
		//Duration
		Text Duration = new Text();
		Duration.setText("Duration: 133 Minutes");
		Duration.setFont(Font.font(null, 20));
		Duration.setFill(Color.BLACK);
		
		//Date Released
		Text DR = new Text();
		DR.setText("Date Released: 15 Nov 2018");
		DR.setFont(Font.font(null, 20));
		DR.setFill(Color.BLACK);
		
		//PG Rating
		Text PG = new Text();
		PG.setText("          PG13");
		PG.setFont(Font.font(null, 20));
		PG.setFill(Color.ORANGE);
		
		
		LeftSide.getChildren().addAll(Image9, Genre, Language, Duration, DR, PG);
		//==================================Right Side=================================
		
		//VBox for Description
		VBox RightSide = new VBox(40);
		
		
		//Name for Movie
		Text Name9 = new Text();
		Name9.setText("Fantastic Beasts 2");
		Name9.setFont(Font.font(null, FontWeight.BOLD, 35));
		Name9.setFill(Color.FIREBRICK);
				
		//Description for movie
		Text text9 = new Text();
		text9.setText("Fantastic Beasts: The Crimes of Grindelwald is the second of five all "
				+ "new adventures in J.K. Rowling's Wizarding World.");
		text9.setFont(Font.font (null, 20));
		text9.setWrappingWidth(700);
		text9.setTextAlignment(TextAlignment.JUSTIFY);
		
		
		//Trailer
		URL path = getClass().getResource("/application/media/Fantastic Beasts 2.mp4");
		
		Media Trailer9 = new Media(path.toString());
		MediaPlayer player = new MediaPlayer (Trailer9);
		MediaView CS2T = new MediaView(player);
		CS2T.setFitWidth(420);
		CS2T.setFitHeight(300);
		CS2T.setSmooth(true);
		

		RightSide.getChildren().addAll(Name9, text9, CS2T);	
		//================================Creating The Layout=================================
		//HBox For Movie & Desc
		HBox movie9 = new HBox(30);
			

		movie9.getChildren().addAll(LeftSide, RightSide);
		
		
		//BorderPane for align
		BorderPane bp = new BorderPane();
		bp.setCenter(movie9);
				
		//video play and pause
		CS2T.setOnMouseClicked(e->{

			if(player.getStatus().equals(Status.PLAYING)) {
				player.pause();
			}
			if(player.getStatus().equals(Status.PAUSED)) {
				player.play();
			}
		});

		
		player.play();
		player.setCycleCount(MediaPlayer.INDEFINITE);
		
		return bp;

	}
	
	//Third Movie
	public BorderPane movieCS3() throws MalformedURLException {
		
		//setting back button to be visible
		backbtn.setVisible(true);
		//Back Button
		backbtn.setOnAction(e->{
			homepagemainbp.setCenter(movies());
		});

		//==================================Left Side=================================

		//Vbox for Image, Genre, Language, Duration, Date Released & PG Rating
		VBox LeftSide = new VBox(20);
		LeftSide.setPadding(new Insets(5,0,0,20));
			
		//Movie Image
		Image ComingSoon3 = new Image(getClass().getResourceAsStream("/application/images/The Grinch.jpg"));
		ImageView Image10 = new ImageView();
		Image10.setImage(ComingSoon3);
		Image10.setFitWidth(300);
		Image10.setFitHeight(420);
		Image10.setSmooth(true);
		Image10.setCache(true);
		
		//Genre
		Text Genre = new Text();
		Genre.setText("Genre: Animation/Comedy/Family");
		Genre.setFont(Font.font(null, 20));
		Genre.setFill(Color.BLACK);
		
		//Language
		Text Language = new Text();
		Language.setText("Language: English");
		Language.setFont(Font.font(null, 20));
		Language.setFill(Color.BLACK);
		
		//Duration
		Text Duration = new Text();
		Duration.setText("Duration: 90 Minutes");
		Duration.setFont(Font.font(null, 20));
		Duration.setFill(Color.BLACK);
		
		//Date Released
		Text DR = new Text();
		DR.setText("Date Released: 8 Nov 2018");
		DR.setFont(Font.font(null, 20));
		DR.setFill(Color.BLACK);
		
		//PG Rating
		Text PG = new Text();
		PG.setText("               U");
		PG.setFont(Font.font(null, 20));
		PG.setFill(Color.GREEN);
		
		
		LeftSide.getChildren().addAll(Image10, Genre, Language, Duration, DR, PG);
		//==================================Right Side=================================
		
		//VBox for Description
		VBox RightSide = new VBox(40);
		
		
		//Name for Movie
		Text Name10 = new Text();
		Name10.setText("The Grinch");
		Name10.setFont(Font.font(null, FontWeight.BOLD, 35));
		Name10.setFill(Color.FIREBRICK);
				
		//Description for movie
		Text text10 = new Text();
		text10.setText("A grumpy Grinch plots to ruin Christmas for the village of Whoville.");
		text10.setFont(Font.font (null, 20));
		text10.setWrappingWidth(700);
		text10.setTextAlignment(TextAlignment.JUSTIFY);
		
		
		//Trailer
		URL path = getClass().getResource("/application/media/The Grinch.mp4");
		
		Media Trailer10 = new Media(path.toString());
		MediaPlayer player = new MediaPlayer (Trailer10);
		MediaView CS3T = new MediaView(player);
		CS3T.setFitWidth(420);
		CS3T.setFitHeight(300);
		CS3T.setSmooth(true);
		

		RightSide.getChildren().addAll(Name10, text10, CS3T);	
		//================================Creating The Layout=================================
		//HBox For Movie & Desc
		HBox movie10 = new HBox(30);
			

		movie10.getChildren().addAll(LeftSide, RightSide);
		
		
		//BorderPane for align
		BorderPane bp = new BorderPane();
		bp.setCenter(movie10);
				
		//video play and pause
		CS3T.setOnMouseClicked(e->{

			if(player.getStatus().equals(Status.PLAYING)) {
				player.pause();
			}
			if(player.getStatus().equals(Status.PAUSED)) {
				player.play();
			}
		});

		
		player.play();
		player.setCycleCount(MediaPlayer.INDEFINITE);
		
		return bp;
	}


}
	