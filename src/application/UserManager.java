package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class UserManager {
	public static UserManager usermanager = new UserManager();
	ArrayList<User> UserList = new ArrayList<>();

	void load() {
		File yourFile = new File("user.dat");
		try {
			yourFile.createNewFile();
		} catch (IOException e) {
			// e.printStackTrace();
		} // Create a file if it doesn't exist
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream(yourFile));
			UserList = (ArrayList<User>) ois.readObject();
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// e.printStackTrace();
		}
	}

	void save() {
		File yourFile = new File("user.dat");
		try {
			yourFile.createNewFile();
		} catch (IOException e) {
			// e.printStackTrace();
		} // Create a file if it doesn't exist
		ObjectOutputStream oos;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(yourFile));
			oos.writeObject(UserList);
		} catch (FileNotFoundException e) {
			// e.printStackTrace();
		} catch (IOException e) {
			// e.printStackTrace();
		}
	}

	public boolean delUser(String email) {
		int index = getUserIndex(email);
		if (index != -1) {
			UserList.remove(index);
			save();
			return true;
		}
		return false;
	}

	public boolean delUser(int id) {
		int index = getUserIndex(id);
		if (index != -1) {
			UserList.remove(index);
			save();
			return true;
		}
		return false;
	}

	public boolean addUser(String email, String name, String password, String ic, String contact, String dob,
			String gender, String Address, String State, String Country, String postcode) {
		load();

		// if user already exist return false
		if (getUserIndex(email) != -1)
			return false;
		User newuser = new User(getMaxUserID() + 1, name, email, password, ic, contact, dob, gender, Address, State,
				Country, postcode);
		UserList.add(newuser); // add user to arraylist
		save();
		return true;
	}

	int getMaxUserID() {
		load();
		int max = 0;

		for (User i : UserList) {
			if (i.id > max)
				max = i.id;
		}
		return max;
	}

	public int getUserIndex(String email) {
			load();// load from file
		for (int i = 0; i < UserList.size(); i++) {
			if (UserList.get(i).email.equals(email)) {
				return i;
			}
		}
		return -1;
	}

	public int getUserIndex(int id) {
		load();// load from file
		for (int i = 0; i < UserList.size(); i++) {
			if (UserList.get(i).id == id) {
				return i;
			}
		}
		return -1;
	}

	boolean checkLoginPassword(int index, String password) {
		load();
		if (UserList.get(index).checkPassword(password))
			return true;
		else
			return false;
	}

	User getUser(int index) {
		load();
		return UserList.get(index); // return user at index
	}
}
