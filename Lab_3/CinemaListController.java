package Lab_3;

import java.util.ArrayList;

public class CinemaListController extends BaseController {
	public enum CinemaListState { // состояния
		START,
		ADD_CINEMA_NAME,
		ADD_CINEMA_ADDRESS,
		CINEMA_INFO,
		ERROR,
		FINAL,
	};
	
	private CinemaListState currState; // текущее состояние
	private ArrayList<CinemaController> cinemaList; // список кинотеатров
	private CinemaController selectedCinema;

	private String newCinemaName, newCinemaAddress; 
	private Boolean redirectToSessions;

	public CinemaListController() {
		super("Кинотеатры");
		currState = CinemaListState.START;
		newCinemaName = newCinemaAddress = "";
		cinemaList = new ArrayList<CinemaController>();
		selectedCinema = new CinemaController();
		redirectToSessions = false;
	}
	
	public ArrayList<CinemaController> getCinemaList() {
		return cinemaList;
	}

	public void addCinema(CinemaController cinema) {
		cinemaList.add(cinema);
	}
	public CinemaController getSelectedCinema() {
		return selectedCinema;
	}
	public Boolean isRedirectToSessions() {
		return redirectToSessions;
	}
	public void cancelRedirect() {
		redirectToSessions = false;
	}

	@Override
	public void showMenu() {
		switch (currState) {
			case START:
				System.out.println("Выберите кинотеатр: ");
				for (int i = 0; i < cinemaList.size(); i++) {
					System.out.printf("%d) Кинотеатр \"%s\" (%s)\n", i+1, cinemaList.get(i).getCinemaName(), cinemaList.get(i).getCinemaAddress());
				}
				System.out.print("\n");	
				if (currUser.isAdmin()) {
					System.out.println("+) Добавить кинотеатр");
				}
				System.out.println("0) Назад");
				System.out.print(">");
				break;

			case ADD_CINEMA_NAME:
				System.out.println("Добавление кинотеатра (введите 0 для отмены)");
				System.out.print("Название: ");
				break;

			case ADD_CINEMA_ADDRESS:
				System.out.println("Добавление кинотеатра (введите 0 для отмены)");
				System.out.printf("Название: %s\n", newCinemaName);
				System.out.print("Адрес: ");
				break;
			
			case CINEMA_INFO:
				System.out.println("Информация о кинотеатре");
				System.out.printf("Название: %s\n", selectedCinema.getCinemaName());
				System.out.printf("Адрес: %s\n", selectedCinema.getCinemaAddress());
				System.out.printf("Количество залов: %d\n\n", selectedCinema.getHallsCount());

				if (currUser.isAdmin()) {
					System.out.println("-) Удалить");
					System.out.println("/) Настройки");
				}
				System.out.println("#) Найти сеансы");
				System.out.println("0) Назад");
				break;

			case ERROR:
				System.out.printf("Ошибка: %s\n", errMsg);
				System.out.println("Нажмите ENTER чтобы продолжить");
				break;
			
			case FINAL:
				System.out.println(finishMsg);
				System.out.println("Нажмите ENTER чтобы продолжить");
				break;
		}
	}
	@Override
	public void handleInput() {
		String input = scanner.nextLine();	
		switch (currState) {
			case START:
				if (input.equals("0")) stop();
				else if (input.equals("+")) {
					if (currUser.isAdmin()) currState = CinemaListState.ADD_CINEMA_NAME;
				}
				else if (input.isEmpty()) {}
				else {
					try {
						Integer cinemaIndex = Integer.parseInt(input);
						if (cinemaIndex > 0 && cinemaIndex <= cinemaList.size()) {
							selectedCinema = cinemaList.get(cinemaIndex - 1);
							currState =	CinemaListState.CINEMA_INFO;
						}
					} catch (NumberFormatException e) {
						errMsg = "Неправильный формат числа";
						currState = CinemaListState.ERROR;
					}
				}
				break;

			case ADD_CINEMA_NAME:
				if (input.equals("0")) currState = CinemaListState.START;
				else if (!input.isEmpty()) {
					newCinemaName = input;
					currState = CinemaListState.ADD_CINEMA_ADDRESS;
				}
				break;

			case ADD_CINEMA_ADDRESS:
				if (input.equals("0")) currState = CinemaListState.START;
				else if (!input.isEmpty()) {
					newCinemaAddress = input;
					currState = CinemaListState.FINAL;
					finishMsg = "Кинотеатр успешно добавлен";
					cinemaList.add(new CinemaController(newCinemaName, newCinemaAddress));
				}
				break;

			case CINEMA_INFO:
				if (input.equals("0")) currState = CinemaListState.START;
				else if (input.equals("-")) {
					if (currUser.isAdmin()) {
						cinemaList.remove(selectedCinema);
						finishMsg = "Кинотеатр успешно удалён";
						currState = CinemaListState.FINAL;
					}
				}
				else if (input.equals("/")) {
					if (currUser.isAdmin()) {
						selectedCinema.setUser(currUser);
						selectedCinema.run();
					}
				}
				else if (input.equals("#")) {
					redirectToSessions = true;
					stop();
				}
				break;


			case ERROR:
				currState = CinemaListState.START;
				break;
			
			case FINAL:
				currState = CinemaListState.START;
				break;
		}
	}
};
