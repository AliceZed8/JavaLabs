package Lab_3;

import java.util.ArrayList;
import java.util.Collections;

public class HallController extends BaseController {
	public enum HallState {
		START,
		ADD_SEAT_ROW,
		ADD_SEAT_COL,
		ADD_SEAT_CONFIRM,
		REMOVE_SEAT_NUM,
	}
	private HallState currState;

	private ArrayList<Seat> seats;
	private Integer rows, cols;	
	private String hallName;
	private Integer newSeatRow, newSeatCol;

	public HallController() {
		super("Зал");
		hallName = "Зал";
		rows = cols = 0;
		newSeatRow = newSeatCol = -1;
		seats = new ArrayList<Seat>();
		currState = HallState.START;
	}

	public HallController(String hallName, Integer rows, Integer cols) {
		super(hallName);
		this.hallName = hallName;
		this.rows = rows;
		this.cols = cols;
		newSeatCol = newSeatRow = -1;
		seats = new ArrayList<Seat>();
		currState = HallState.START;
	}
	
	public void setHallName(String hallName) {
		this.hallName = hallName;
	}
	public String getHallName() {
		return hallName;
	}
	public Integer getRows() {
		return rows;
	}
	public Integer getCols() {
		return cols;
	}
	public Integer getSeatsCount() {
		return seats.size();
	}
	
	public Seat getSeat(Integer seatIndex) {
		return seats.get(seatIndex);
	}

	public Integer getNotOccupiedCount() {
		Integer count = 0;
		for (Seat seat: seats) {
			if (!seat.isOccupied()) count++;
		}
		return count;
	}
	
	public void addSeat(Seat newSeat) {
		Seat seat = newSeat.copy();
		seats.add(seat);
	}

	public HallController copy() {
		HallController newHall = new HallController(hallName, rows, cols);
		for (Seat seat: seats) {
			newHall.seats.add(seat.copy());
		}
		return newHall;
	}
	
	private String getTabs(Integer i) {
		String tabs;
		if (i < 10) tabs = "  ";
		else if (i < 100) tabs = " ";
		else tabs = "";
		return tabs;
	}
		
	public void showSeatsConfiguration() {
		Collections.sort(seats); // сортировка мест (так как могут добавляться по разному)
		String[][] layout = new String[rows][cols];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				layout[i][j] = "   ";
				if ((i == newSeatRow) && (j == newSeatCol)) layout[i][j] = "\u001B[43m S \u001B[0m"; // желтый
			}
		}

		for (int i = 0; i < seats.size(); i++) {
			Seat seat = seats.get(i);
			Integer row = seat.getRowNum();
			Integer col = seat.getColNum();
			if ((row >= 0) && (row < rows) && (col >= 0) && (col < cols)) {
				layout[row][col] = seat.isOccupied() ? 
					String.format("\u001B[41m%d%s\u001B[0m", i + 1, getTabs(i+1)) 
					: String.format("\u001B[42m%d%s\u001B[0m", i + 1, getTabs(i+1)); // красный и зеленый
			}
		}
		
		// нумерация сверху
		System.out.print("   ");
		for (int i = 0; i < cols; i++) {
			System.out.printf("%d%s", i, getTabs(i));
		}
		System.out.print("\n");

		for (int i = 0; i < rows; i++) {
			System.out.printf("%d%s", i, getTabs(i));
			for (int j = 0; j < cols; j++) {
				System.out.print(layout[i][j]);
			}
			System.out.print("\n");
		}
	}


	@Override
	public void showMenu() {
		System.out.println("Схема зала");
		showSeatsConfiguration();
		System.out.print("\n");

		switch (currState) {
			case START:
				if (currUser.isAdmin()) {
					System.out.println("+) Добавить место");
					System.out.println("-) Удалить место");
				}
				System.out.println("0) Назад");
				System.out.print(">");
				break;

			case ADD_SEAT_ROW:
				System.out.println("Добавление нового места (введите 00 для отмены)");
				System.out.print("Ряд: ");
				break;

			case ADD_SEAT_COL:
				System.out.println("Добавление нового места (введите 00 для отмены)");
				System.out.printf("Ряд: %d\n", newSeatRow);
				System.out.print("Номер в ряду: ");
				break;
			
			case ADD_SEAT_CONFIRM:
				System.out.println("Добавление нового места (введите 00 для отмены)");
				System.out.printf("Ряд: %d\n", newSeatRow);
				System.out.printf("Номер в ряду: %d\n", newSeatCol);
				System.out.println("Нажмите ENTER чтобы добавить или 00 для отмены");
				break;

			case REMOVE_SEAT_NUM:
				System.out.println("Удаление места (введите 00 для отмены)");
				System.out.print("Номер места: ");
				break;
		}	
	}

	@Override
	public void handleInput() {
		String input = scanner.nextLine();
		switch (currState) {
			case START:
				if (input.equals("0")) stop();
				if (currUser.isAdmin()) {
					if (input.equals("+")) currState = HallState.ADD_SEAT_ROW;
					else if (input.equals("-")) currState = HallState.REMOVE_SEAT_NUM;
				}
				break;

			case ADD_SEAT_ROW:
				if (input.equals("00")) currState = HallState.START;
				else if (input.isEmpty()) {}
				else {
					try {
						newSeatRow = Integer.parseInt(input);
						if (newSeatRow >= 0 && newSeatRow < rows) currState = HallState.ADD_SEAT_COL;
					} catch (NumberFormatException e) {}		
				}
				break;

			case ADD_SEAT_COL:
				if (input.equals("00")) currState = HallState.START;
				else if (input.isEmpty()) {}
				else {
					try {
						newSeatCol = Integer.parseInt(input);
						if (newSeatCol >= 0 && newSeatCol < cols) currState = HallState.ADD_SEAT_CONFIRM; 
					} catch (NumberFormatException e) {}
				}
				break;

			case ADD_SEAT_CONFIRM:
				if (input.equals("00")) currState = HallState.START;
				else {
					currState = HallState.ADD_SEAT_ROW;
					Boolean seatExists = false;
					for (Seat seat: seats) { // проверка наличия места
						if ((seat.getRowNum() == newSeatRow) && (seat.getColNum() == newSeatCol)) {
							seatExists = true;
							break;
						}
					}
					if (!seatExists) seats.add(new Seat(newSeatRow, newSeatCol));
				}
				newSeatRow = newSeatCol = -1;
				break;

			case REMOVE_SEAT_NUM:
				if (input.equals("00")) currState = HallState.START;
				else {
					try {
						Integer seatIndex = Integer.parseInt(input) - 1;
						if (seatIndex >= 0 && seatIndex < seats.size()) {
							seats.remove(seats.get(seatIndex));
						}
					} catch (NumberFormatException e) {}
				}
		}
	
	}
	
}
