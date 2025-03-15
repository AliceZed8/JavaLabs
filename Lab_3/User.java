package Lab_3;



public class User {
	private String username;
	private String passwordHash;
	private int role;
	
	public User() {
		username = "";
		passwordHash = "";
		role = 1;
	}

	public User(String username, String passwordHash) {
		this.username = username;
		this.passwordHash = passwordHash;

		if (username.equals("admin")) {
			this.role = 0;
		} else this.role = 1;
	}

	public boolean isAdmin() {
		return (role == 0);
	}

	public String getUsername() {
		return username;
	}

};
