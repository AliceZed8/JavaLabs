package Lab_3;

import java.util.Scanner;

class BaseController {
	protected static Scanner scanner;
	protected String controllerTitle;
	protected String errMsg, finishMsg;
	protected User currUser; 
	private Boolean isRunning;
	
	public BaseController(String controllerTitle) {
		scanner = new Scanner(System.in);
		this.controllerTitle = controllerTitle;
		errMsg = finishMsg = "";
		isRunning = false;
		currUser = new User();
	}
	
	public Boolean getIsRunning() {
		return isRunning;
	}
	public void setIsRunning(Boolean value) {
		isRunning  = value;
	}

	public void setUser(User user) {
		currUser = user;
	}
	public User getUser() {
		return currUser;
	}
	

	public void clear() {
		try {
			String os = System.getProperty("os.name").toLowerCase();

			if (os.contains("win")) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			}
			else {
				new ProcessBuilder("clear").inheritIO().start().waitFor();
			}
		}
		catch (Exception e) {
			System.out.println("Failed to clear console");
		}
	}
	


	public void showMenu() {

	}

	public void handleInput() {
		stop();
	}

	public void run() {
		clear();
		isRunning = true;
		while (isRunning) {
			System.out.printf("==== %s ====\n", controllerTitle);
			showMenu();
			handleInput();
			clear();
		}
	}
	
	public void stop() {
		isRunning = false;
	}
};

