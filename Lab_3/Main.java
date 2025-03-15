package	Lab_3;

public class Main {
	
	public static void main(String[] args) {
		AuthController authController = new AuthController();
		CinemaMainController mainController = new CinemaMainController();

		while (true) {
			authController.run();
			User user = authController.getUser();
			if (user.getUsername().isEmpty()) break;


			mainController.setUser(user);
			mainController.run();
		}
	}
 
}
