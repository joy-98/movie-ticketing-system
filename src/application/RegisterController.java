package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController implements Initializable {
	@FXML
	private TextField txtname;

	@FXML
	private TextField txtemail;

	@FXML
	private PasswordField txtpassword;

	@FXML
	private PasswordField txtpassword1;

	@FXML
	private TextField txtic;

	@FXML
	private TextField txtcontact;

	@FXML
	private DatePicker txtdob;

	@FXML
	private ComboBox<String> combogender;

	@FXML
	private TextField txtadd;

	@FXML
	private TextField txtstate;

	@FXML
	private TextField txtcountry;

	@FXML
	private TextField txtpost;

	@FXML
	private Button submit;

	@FXML
	private CheckBox agreement;

	@FXML
	private Label Status;

	private ObservableList<String> list = FXCollections.observableArrayList();

	public void Back(ActionEvent event) throws IOException {
		Parent login_page_parent = FXMLLoader.load(getClass().getResource("/application/Login.fxml"));
		Scene login_page_scene = new Scene(login_page_parent);
		Stage login_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		login_stage.setScene(login_page_scene);
		login_stage.setFullScreen(true);
		login_stage.setFullScreenExitHint("");
		login_stage.show();
	}

	public void FlipAccept(ActionEvent event) {
		submit.setDisable(!submit.isDisabled());
	}

	public void Submit(ActionEvent event) throws IOException {
		// Register:
		// Checking
		Pattern p = Pattern.compile(
				"(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",
				Pattern.CASE_INSENSITIVE);
		if (txtemail.getText().isEmpty() || txtname.getText().isEmpty() || txtpassword.getText().isEmpty()
				|| txtic.getText().isEmpty() || txtcontact.getText().isEmpty() || txtdob.getValue().toString().isEmpty()
				|| txtadd.getText().isEmpty() || txtstate.getText().isEmpty() || txtcountry.getText().isEmpty()
				|| txtpost.getText().isEmpty()) {
			Status.setText("Please Key in all the fields");
		} else if (!p.matcher(txtemail.getText()).matches()) {
			Status.setText("Please input valid email");
		} else if (!txtpassword.getText().equals(txtpassword1.getText())) {
			Status.setText("Password doesn't match!!");
		} else {
			UserManager.usermanager.addUser(txtemail.getText(), txtname.getText(), txtpassword.getText(),
					txtic.getText(), txtcontact.getText(), txtdob.getValue().toString(),
					combogender.getSelectionModel().getSelectedItem(), txtadd.getText(), txtstate.getText(),
					txtcountry.getText(), txtpost.getText());
			Back(event);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		list.add("Male");
		list.add("Female");
		combogender.setItems(list);
		combogender.getSelectionModel().select(0);

	}
}
