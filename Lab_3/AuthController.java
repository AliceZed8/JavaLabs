package Lab_3;

import java.security.CryptoPrimitive;

public class AuthController extends BaseController {
	public enum AuthState { // состояния
		START,
		LOGIN_USERNAME, LOGIN_PASSWORD,
		REGISTER_USERNAME, REGISTER_PASSWORD, REGISTER_PASSWORD_RETRY,
		FINAL,
		ERROR,
	};

	private AuthState currState; // текущее состояние
	private String username, password;

	public AuthController() {
		super("Авторизация");
		currState = AuthState.START;
		username = password = "";	
	}
	
	@Override
	public void showMenu() {
		switch (currState) {
			case START:
				System.out.println("1) Вход");
				System.out.println("2) Регистрация");
				System.out.println("0) Выйти\n");
				System.out.print(">");
				break;

			case LOGIN_USERNAME:
				System.out.println("Вход");
				System.out.print("Логин: ");
				break;

			case LOGIN_PASSWORD:
				System.out.println("Вход");
				System.out.printf("Логин: %s\n", username);
				System.out.print("Пароль: ");
				break;

			case REGISTER_USERNAME:
				System.out.println("Регистрация");
				System.out.print("Логин: ");
				break;
			case REGISTER_PASSWORD:
				System.out.println("Регистрация");
				System.out.printf("Логин: %s\n", username);
				System.out.print("Пароль: ");
				break;
				
			case REGISTER_PASSWORD_RETRY:
				System.out.println("Регистрация");
				System.out.printf("Логин: %s\n", username);
				System.out.printf("Пароль: %s\n", password);
				System.out.print("Повторите пароль: ");
				break;
				
			case FINAL:
				System.out.println(finishMsg);
				System.out.println("Нажмите ENTER чтобы продолжить");
				break;

			case ERROR:
				System.out.printf("Ошибка: %s\n", errMsg);
				System.out.println("Нажмите ENTER чтобы продолжить");
				break;
		}
	}
	
	@Override
	public void handleInput() {
		String input = scanner.nextLine();	
		switch (currState) {
			case START:
				if (input.equals("1")) {
					currState = AuthState.LOGIN_USERNAME;
				}
				else if (input.equals("2")) {
					currState = AuthState.REGISTER_USERNAME;
				}
				else if (input.equals("0")) {
					setUser(new User());
					stop();	
				}
				break;

			case LOGIN_USERNAME:
				if (input.length() < 5) {
					currState = AuthState.ERROR;
					errMsg = "Логин должен быть не менее 5 символов";
				} else {
					username = input;
					currState = AuthState.LOGIN_PASSWORD;
				}
				break;

			case LOGIN_PASSWORD:
				if (input.length() < 5) {
					currState = AuthState.ERROR;
					errMsg = "Пароль должен быть не менее 5 символов";
				} else {
					password = input;
					currState = AuthState.FINAL;
					finishMsg = "Вход успешно выполнен";
				}
				break;

			case REGISTER_USERNAME:
				if (input.length() < 5) {
					currState = AuthState.ERROR;
					errMsg = "Логин должен быть не менее 5 символов";
				} else {
					username = input;
					currState = AuthState.REGISTER_PASSWORD;
				}
				break;

			case REGISTER_PASSWORD:
				if (input.length() < 5) {
					currState = AuthState.ERROR;
					errMsg = "Пароль должен быть не менее 5 символов";
				} else {
					password = input;
					currState = AuthState.REGISTER_PASSWORD_RETRY;
				}
				break;
				
			case REGISTER_PASSWORD_RETRY:
				if (!input.equals(password)) {
					currState = AuthState.ERROR;
					errMsg = "Пароли не совпадают";
				} else {
					currState = AuthState.FINAL;
					finishMsg = "Регистрация успешно завершена";
				}
				break;
				
			case FINAL:
				setUser(new User(username, password));
				currState = AuthState.START;
				stop();
				break;
				
			case ERROR:
				currState = AuthState.START;
				break;

			default:
				currState = AuthState.START;
				break;
		}
	}
};
