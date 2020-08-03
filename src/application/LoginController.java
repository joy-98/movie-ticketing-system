package application;

import java.io.IOException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginController {
	@FXML
	private Label status;

	@FXML
	private TextField txtusername;

	@FXML
	private PasswordField txtpassword;
	
	
	
	public void Login(ActionEvent event) {
		int index = UserManager.usermanager.getUserIndex(txtusername.getText());
		if (index != -1) {
			//checking username and password
			if (UserManager.usermanager.checkLoginPassword(index, txtpassword.getText())) {
				// change scene here
				MTS mts = new MTS();
				mts.setUsernameGlobal(txtusername.getText());
				Scene scene = new Scene(mts.homepage());
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				scene.getStylesheets().add("/application/SpadesTicketing.css");
				stage.setScene(scene);
				stage.setFullScreen(true);
				//disabling exit full screen hint message
				stage.setFullScreenExitHint("");
				//disabling esc key from exiting full screen
				stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
				stage.show();
				
			}
			else {
				status.setText("Invalid Email or Password!");
			}
		} else {
			status.setText("Invalid Email or Password!");
		}
	}



	boolean login_check(String email, String password) {
		if (UserManager.usermanager.getUserIndex(email) != -1) // check actual username and password here
			return true;
		else
			return false;
	}

	public void Signup(ActionEvent event) throws IOException {
		Parent register_page_parent = FXMLLoader.load(getClass().getResource("/application/Register.fxml"));
		Scene register_page_scene = new Scene(register_page_parent);
		Stage register_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		register_stage.setScene(register_page_scene);
		register_stage.setFullScreen(true);
		register_stage.setFullScreenExitHint("");
		register_stage.show();
	}
	
	public void Exit(ActionEvent event) {
		Alert exitmsg = new Alert(Alert.AlertType.CONFIRMATION);
		exitmsg.setHeaderText("Are you sure you want to exit the application?");
		exitmsg.setContentText("Click OK to exit.");
		
		Optional<ButtonType> result = exitmsg.showAndWait();
		if(result.get() == ButtonType.OK) {
			Platform.exit();
		}
	}

	
}
