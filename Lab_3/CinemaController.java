package Lab_3;

import java.util.ArrayList;

public class CinemaController extends BaseController {
	public enum CinemaState {
		START,
		HALL_INFO,
		ADD_HALL_ROWS,
		ADD_HALL_COLS,
		ERROR,
		FINAL
	}
	private CinemaState currState;
	private String cinemaName, cinemaAddress;

	private ArrayList<HallController> halls;
	private HallController selectedHall;
	private Integer newHallRows, newHallCols;

	public CinemaController() {
		super("Кинотеатр");
		cinemaName = cinemaAddress = "";
		currState = CinemaState.START;
		halls = new ArrayList<HallController>();
	}

	public CinemaController(String cinemaName, String cinemaAddress) {
		super("Кинотеатр " + cinemaName);
		this.cinemaName = cinemaName;
		this.cinemaAddress = cinemaAddress;
		currState = CinemaState.START;
		halls = new ArrayList<HallController>();
	}
	
	public String getCinemaName() {
		return cinemaName;
	}
	public String getCinemaAddress() {
		return cinemaAddress;
	}
	

	public Integer getHallsCount() {
		return halls.size();
	}

	public ArrayList<HallController> getHalls() {
		return halls;
	}

	public void addHall(HallController newHall) {
		HallController hall = newHall.copy();
		hall.setHallName(String.format("Зал %d", halls.size() + 1));
		halls.add(hall);
	}
	
	@Override
	public void showMenu() {
		switch (currState) {
			case START:
				System.out.println("Залы: ");
				
				for (int i = 0; i < halls.size(); i++) {
					System.out.printf("%d) %s\n", i+1, halls.get(i).getHallName());
				}

				System.out.print("\n");
				if (currUser.isAdmin()) {
					System.out.println("+) Добавить");
				}
				System.out.println("0) Назад");
				break;

			case HALL_INFO:
				System.out.printf("Информация о зале: %s\n", selectedHall.getHallName());
				System.out.printf("Размеры: %dx%d\n", selectedHall.getRows(), selectedHall.getCols());
				System.out.printf("Количество мест: %d\n\n", selectedHall.getSeatsCount());
				System.out.println("Схема зала: ");
				selectedHall.showSeatsConfiguration();
				System.out.print("\n");
				if (currUser.isAdmin()) {
					System.out.println("/) Настроить");
					System.out.println("+) Скопировать в новый зал");
				}
				System.out.println("0) Назад");
				break;

			case ADD_HALL_ROWS:
				System.out.println("Добавление нового зала (введите 0 для отмены)");
				System.out.print("Количество рядов: ");
				break;

			case ADD_HALL_COLS:
				System.out.println("Добавление нового зала (введите 0 для отмены)");
				System.out.printf("Количество рядов: %d\n", newHallRows);
				System.out.print("Количество мест в ряду: ");
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
					if (currUser.isAdmin()) currState = CinemaState.ADD_HALL_ROWS;	
				}
				else if (input.isEmpty()) {
				}
				else {
					try {
						Integer hallIndex = Integer.parseInt(input);
						if (hallIndex > 0 && hallIndex <= halls.size()) {
							selectedHall = halls.get(hallIndex - 1);
							currState = CinemaState.HALL_INFO;
						}
					} catch (NumberFormatException e) {
						errMsg = "Неправильный формат числа";
						currState = CinemaState.ERROR;
					}	
				}
				break;
			
			case ADD_HALL_ROWS:
				if (input.equals("0")) currState = CinemaState.START;
				else {
					try {
						newHallRows = Integer.parseInt(input);
						if (newHallRows < 5 || newHallRows > 25) {
							currState = CinemaState.ERROR;
							errMsg = "Количество рядов должно быть не менее 5 и не более 25";
							break;
						}
						currState = CinemaState.ADD_HALL_COLS;
					} catch (NumberFormatException e) {}	
				}
				break;
			
			case ADD_HALL_COLS:
				if (input.equals("0")) currState = CinemaState.START;
				else {
					try {
						newHallCols = Integer.parseInt(input);
						if (newHallCols < 5 || newHallCols > 25) {
							currState = CinemaState.ERROR;
							errMsg = "Количество мест в ряду должно быть не менее 5 и не более 25";
							break;
						}
	
						currState = CinemaState.FINAL;
						halls.add( new HallController(String.format("Зал %d", halls.size() + 1), newHallRows, newHallCols) );
						finishMsg = "Зал успешно добавлен";
					} catch (NumberFormatException e) {}	
				}
				break;
			
			case HALL_INFO:
				if (input.equals("0")) currState = CinemaState.START;
				if (currUser.isAdmin()) {
					if (input.equals("+")) {
						HallController newHall = selectedHall.copy();
						newHall.setHallName(String.format("Зал %d", halls.size() + 1));
						halls.add(newHall);

						currState = CinemaState.FINAL;
						finishMsg = "Скопировано в " + newHall.getHallName();
					}
					else if (input.equals("/")) {
						selectedHall.setUser(currUser);
						selectedHall.run();	
					}
				}
				break;

			case ERROR:
				currState = CinemaState.START;
				break;

			case FINAL:
				currState = CinemaState.START;
				break;
		}
	}


}


