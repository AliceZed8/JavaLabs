package Lab_3;

import java.util.Date;

public class SessionController extends BaseController {
	public enum SessionState {
		START,
		TAKE_SEAT,
		ERROR,
	};
	private SessionState currState;

	private Film film;
	private CinemaController cinema;
	private HallController hall;
	private Date date;
	private Integer ticketPrice;
	
	public SessionController(Film film, CinemaController cinema, HallController hall, Date date, Integer ticketPrice) {
		super(String.format("Сеанс \"%s\" (%d)(%d мин.) %s (Кинотеатр %s, %s)", 
					film.getTitle(), film.getYear(), film.getDuration(), date, cinema.getCinemaName(), hall.getHallName()));
		this.film = film;
		this.cinema = cinema;
		this.hall = hall.copy();
		this.date = date;
		this.ticketPrice = ticketPrice;
		this.currState = SessionState.START;
	}
	
	public Film getFilm() {
		return film;
	}
	public CinemaController getCinema() {
		return cinema;
	}
	public HallController getHall() {
		return hall;
	}
	public Date getDate() {
		return date;
	}
	public Integer getTicketPrice() {
		return ticketPrice;
	}
	public Integer getNotOccupiedCount() {
		return hall.getNotOccupiedCount();
	}

	
	@Override
	public void showMenu() {
		switch (currState) {
			case START:
				System.out.println("Схема зала:");
				hall.showSeatsConfiguration();
				System.out.print("\n");
				System.out.println("$) Занять место");
				System.out.println("0) Назад");
				break;

			case TAKE_SEAT:
				System.out.print("Выбор места (введите 0 для отмены)");
				hall.showSeatsConfiguration();
				System.out.print("Выберите место: ");
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
				if (input.equals("0")) stop();
				else if (input.equals("$")) currState = SessionState.TAKE_SEAT;
				break;

			case TAKE_SEAT:
				if (input.equals("0")) currState = SessionState.START;
				else if (input.isEmpty()) {}
				else {
					try {
						Integer seatNum = Integer.parseInt(input) - 1;
						if (seatNum >= 0 && seatNum < hall.getSeatsCount()) {
							Seat seat = hall.getSeat(seatNum);
							if (!seat.isOccupied()) {
								seat.setOccupied(true);
								currState = SessionState.START;
							}	
						}
					} catch (NumberFormatException e) {}		
				}
				break;

			case ERROR:
				currState = SessionState.START;
				break;
		}
	
	}


}
