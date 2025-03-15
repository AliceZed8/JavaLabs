package Lab_3;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SessionListController extends BaseController {
	public enum SessionState {
		START,
		
		SHOW_ALL_SESSIONS,
		SESSION_INFO,
		
		SEARCH_SESSIONS_FILM_SELECT,
		SEARCH_SESSIONS_FILM_RESULTS,

		SEARCH_SESSIONS_CINEMA_SELECT,
		SEARCH_SESSIONS_CINEMA_RESULTS,

		ADD_SESSION_FILM_SELECT,
		ADD_SESSION_CINEMA_SELECT,
		ADD_SESSION_HALL_SELECT,
		ADD_SESSION_DATE_SELECT,
		ADD_SESSION_TICKET_PRICE,
		ADD_SESSION_CONFIRM,
	};
	private SessionState currState;

	private CinemaListController cinemaListController;
	private FilmsController filmsController;
	
	private ArrayList<SessionController> sessions;
	private ArrayList<SessionController> filteredSessions;

	private SessionController selectedSession;
	private Film selectedFilm;
	private CinemaController selectedCinema;
	private HallController selectedHall;
	private Date newSessionDate;
	private Integer newSessionTicketPrice;
	
	private Boolean isRedirect;

	public SessionListController(CinemaListController cinemaListController, FilmsController filmsController) {
		super("Сеансы");
		this.cinemaListController = cinemaListController;
		this.filmsController = filmsController;
		this.sessions = new ArrayList<SessionController>();
		this.filteredSessions = new ArrayList<SessionController>();
		this.currState = SessionState.START;
		this.isRedirect = false;
	}
	
	public void setRedirect(Boolean value) {
		isRedirect = value;
	}

	public void setSearchByFilmResultsState(Film film) {
		selectedFilm = film;
		filteredSessions.clear();
		for (SessionController session: sessions) {
			if (session.getFilm() == selectedFilm) filteredSessions.add(session);	
		}
		selectedCinema = null;
		currState = SessionState.SEARCH_SESSIONS_FILM_RESULTS;
	}

	public void setSearchByCinemaResultsState(CinemaController cinema) {
		selectedCinema = cinema;
		filteredSessions.clear();
		for (SessionController session: sessions) {
			if (session.getCinema() == selectedCinema) filteredSessions.add(session);
		}
		selectedFilm = null;
		currState = SessionState.SEARCH_SESSIONS_CINEMA_RESULTS;
	}

	@Override
	public void showMenu() {
		ArrayList<Film> films = filmsController.getFilmsList();
		ArrayList<CinemaController> cinemaList = cinemaListController.getCinemaList();

		switch (currState) {
			case START:
				System.out.println("Выберите: ");
				System.out.println("1) Все сеансы");
				System.out.println("2) Поиск по фильму");
				System.out.println("3) Поиск по кинотеатру");
				System.out.println("0) Назад");
				System.out.print("> ");
				break;
			
			case SHOW_ALL_SESSIONS:
				System.out.println("Список всех сеансов: ");
				for (int i = 0; i < sessions.size(); i++) {
					SessionController session = sessions.get(i);
					System.out.printf("%d) %s %s (Кинотеатр \"%s\") \n", 
							i+1, session.getFilm().getTitle(), session.getDate(), session.getCinema().getCinemaName());	
				}
				System.out.print("\n");
				if (currUser.isAdmin()) {
					System.out.println("+) Добавить");
				}
				System.out.println("0) Назад");
				System.out.print("> ");
				break;

			case SESSION_INFO:
				System.out.println("Информация о сеансе");
				System.out.printf("Фильм: %s (%d) %d мин.\n", 
						selectedSession.getFilm().getTitle(), selectedSession.getFilm().getYear(), selectedSession.getFilm().getDuration());
				System.out.printf("Кинотеатр: %s\n", selectedSession.getCinema().getCinemaName());
				System.out.printf("Зал: %s (количество мест: %d)\n", 
						selectedSession.getHall().getHallName(), selectedSession.getHall().getSeatsCount()
					);
				System.out.printf("Время: %s\n", selectedSession.getDate());
				System.out.printf("Цена: %d $\n", selectedSession.getTicketPrice());
				System.out.printf("Осталось мест: %d\n\n", selectedSession.getNotOccupiedCount());

				if (currUser.isAdmin()) {
					System.out.println("-) Удалить");
				}
				System.out.println("$) Купить билет");
				System.out.println("0) Назад");
				System.out.print("> ");
				break;

			case SEARCH_SESSIONS_FILM_SELECT:
				System.out.println("Поиск сеансов по фильму");
				System.out.println("Выберите фильм:");
				for (int i = 0; i < films.size(); i++) {
					Film film = films.get(i);
					System.out.printf("%d) %s (%d)\n", i+1, film.getTitle(), film.getYear());
				}
				System.out.println("0) Назад");
				System.out.print("> ");
				break;

			case SEARCH_SESSIONS_FILM_RESULTS:
				System.out.printf("Результаты поиска сеансов по фильму: %s\n", selectedFilm.getTitle());
				for (int i = 0; i < filteredSessions.size(); i++) {
					SessionController session = filteredSessions.get(i);
					System.out.printf("%d) %s %s (Кинотеатр \"%s\")\n", 
							i+1, session.getFilm().getTitle(), session.getDate(), session.getCinema().getCinemaName());	
				}
				System.out.println("0) Назад");
				System.out.print("> ");
				break;

			case SEARCH_SESSIONS_CINEMA_SELECT:
				System.out.println("Поиск сеансов по кинотеатру");
				System.out.println("Выберите кинотеатр:");
				
				for (int i = 0; i < cinemaList.size(); i++) {
					CinemaController cinema = cinemaList.get(i);
					System.out.printf("%d) %s (%s)\n", i+1, cinema.getCinemaName(), cinema.getCinemaAddress());
				}
				System.out.println("0) Назад");
				System.out.print("> ");
				break;
			
			case SEARCH_SESSIONS_CINEMA_RESULTS:
				System.out.printf("Результаты поиска сеансов по кинотеатру: %s\n", selectedCinema.getCinemaName());
				for (int i = 0; i < filteredSessions.size(); i++) {
					SessionController session = filteredSessions.get(i);
					System.out.printf("%d) %s %s (Кинотеатр \"%s\")\n", 
							i+1, session.getFilm().getTitle(), session.getDate(), session.getCinema().getCinemaName());	
				}
				System.out.println("0) Назад");
				System.out.print("> ");
				break;
			

			// Добавление сеанса
			case ADD_SESSION_CINEMA_SELECT:
				System.out.println("Добавление сеанса");
				System.out.println("Выберите кинотеатр:");
					
				for (int i = 0; i < cinemaList.size(); i++) {
					CinemaController cinema = cinemaList.get(i);
					System.out.printf("%d) %s (%s)\n", i+1, cinema.getCinemaName(), cinema.getCinemaAddress());
				}
				System.out.println("0) Отмена");
				System.out.print("> ");
				break;

			case ADD_SESSION_HALL_SELECT:
				System.out.println("Добавление сеанса");
				System.out.printf("Кинотеатр: %s (%s)\n", selectedCinema.getCinemaName(), selectedCinema.getCinemaAddress());
				System.out.println("Выберите зал:");
				
				ArrayList<HallController> halls = selectedCinema.getHalls();
				for (int i = 0; i < halls.size(); i++) {
					HallController hall = halls.get(i);
					System.out.printf("%d) %s (количество мест: %d)\n", i+1, hall.getHallName(), hall.getSeatsCount());
				}
				System.out.println("0) Отмена");
				System.out.print("> ");
				break;
			
			case ADD_SESSION_FILM_SELECT:
				System.out.println("Добавление сеанса");
				System.out.printf("Кинотеатр: %s (%s)\n", selectedCinema.getCinemaName(), selectedCinema.getCinemaAddress());
				System.out.printf("Зал: %s (количество мест: %d)\n", selectedHall.getHallName(), selectedHall.getSeatsCount());
				System.out.println("Выберите фильм:");

				for (int i = 0; i < films.size(); i++) {
					Film film = films.get(i);
					System.out.printf("%d) %s (%d)\n", i+1, film.getTitle(), film.getYear());
				}

				System.out.println("0) Отмена");
				System.out.print("> ");
				break;
			
			case ADD_SESSION_DATE_SELECT:
				System.out.println("Добавление сеанса (введите 0 для отмены)");
				System.out.printf("Кинотеатр: %s (%s)\n", selectedCinema.getCinemaName(), selectedCinema.getCinemaAddress());
				System.out.printf("Зал: %s (количество мест: %d)\n", selectedHall.getHallName(), selectedHall.getSeatsCount());
				System.out.printf("Фильм: %s (%d)\n", selectedFilm.getTitle(), selectedFilm.getYear());
				System.out.print("Введите дату и время (формат HH:mm dd-MM-yyyy): ");
				break;
			
			case ADD_SESSION_TICKET_PRICE:
				System.out.println("Добавление сеанса (введите 0 для отмены)");
				System.out.printf("Кинотеатр: %s (%s)\n", selectedCinema.getCinemaName(), selectedCinema.getCinemaAddress());
				System.out.printf("Зал: %s (количество мест: %d)\n", selectedHall.getHallName(), selectedHall.getSeatsCount());
				System.out.printf("Фильм: %s (%d)\n", selectedFilm.getTitle(), selectedFilm.getYear());
				System.out.printf("Дата и время: %s\n", newSessionDate);
				System.out.print("Цена: ");
				break;
			
			case ADD_SESSION_CONFIRM:
				System.out.println("Добавление сеанса");
				System.out.printf("Кинотеатр: %s (%s)\n", selectedCinema.getCinemaName(), selectedCinema.getCinemaAddress());
				System.out.printf("Зал: %s (количество мест: %d)\n", selectedHall.getHallName(), selectedHall.getSeatsCount());
				System.out.printf("Фильм: %s (%d)\n", selectedFilm.getTitle(), selectedFilm.getYear());
				System.out.printf("Дата и время: %s\n", newSessionDate);
				System.out.printf("Цена: %d $\n", newSessionTicketPrice);
				System.out.println("Нажмите ENTER чтобы добавить сеанс или 0 для отмены");
				break;
			
		}
	}
	
	@Override
	public void handleInput() {
		String input = scanner.nextLine();
		
		ArrayList<Film> films = filmsController.getFilmsList();
		ArrayList<CinemaController> cinemaList = cinemaListController.getCinemaList();

		switch (currState) {
			case START:
				if (input.equals("0")) stop();
				else if (input.equals("1")) currState = SessionState.SHOW_ALL_SESSIONS;
				else if (input.equals("2")) currState = SessionState.SEARCH_SESSIONS_FILM_SELECT;
				else if (input.equals("3")) currState = SessionState.SEARCH_SESSIONS_CINEMA_SELECT;
				break;
			
			case SHOW_ALL_SESSIONS:
				if (input.equals("0")) currState = SessionState.START;
				else if (input.equals("+")) currState = SessionState.ADD_SESSION_CINEMA_SELECT;
				else if (input.isEmpty()) {}
				else {
					try {
						Integer sessionIndex = Integer.parseInt(input) - 1;
						if (sessionIndex >= 0 && sessionIndex < sessions.size()) {
							selectedSession = sessions.get(sessionIndex);
							currState = SessionState.SESSION_INFO;
						}
					} catch (NumberFormatException e) {}	
				}
				break;
			
			case SESSION_INFO:
				if (input.equals("0")) {
					if (selectedFilm != null) currState = SessionState.SEARCH_SESSIONS_FILM_RESULTS;
					else if (selectedCinema != null) currState = SessionState.SEARCH_SESSIONS_CINEMA_RESULTS;
					else currState = SessionState.SHOW_ALL_SESSIONS;
				}
				else if (input.equals("-")) {
					if (currUser.isAdmin()) {
						sessions.remove(selectedSession); // удаляем сеанс
						filteredSessions.remove(selectedSession);

						if (selectedFilm != null) currState = SessionState.SEARCH_SESSIONS_FILM_RESULTS;
						else if (selectedCinema != null) currState = SessionState.SEARCH_SESSIONS_CINEMA_RESULTS;
						else currState = SessionState.SHOW_ALL_SESSIONS;
					}
				}
				else if (input.equals("$")) {
					selectedSession.run();
				}
				break;

			case SEARCH_SESSIONS_FILM_SELECT:
				if (input.equals("0")) currState = SessionState.START;
				else if (input.isEmpty()) {}
				else {
					try {
						Integer filmIndex = Integer.parseInt(input) - 1;
						if (filmIndex >= 0 && filmIndex < films.size()) {
							setSearchByFilmResultsState(films.get(filmIndex));
						}
					} catch (NumberFormatException e) {}	
				}
				break;

			case SEARCH_SESSIONS_FILM_RESULTS:
			case SEARCH_SESSIONS_CINEMA_RESULTS:
				if (input.equals("0")) {
					if (selectedFilm != null) currState = SessionState.SEARCH_SESSIONS_FILM_SELECT;
					else if (selectedCinema != null) currState = SessionState.SEARCH_SESSIONS_CINEMA_SELECT;
					else currState = SessionState.START;

					if (isRedirect) {
						currState = SessionState.START;
						isRedirect = false;
						stop();
					}
				}
				else if (input.isEmpty()) {}
				else {
					try {
						Integer sessionIndex = Integer.parseInt(input) - 1;
						if (sessionIndex >= 0 && sessionIndex < filteredSessions.size()) {
							selectedSession = filteredSessions.get(sessionIndex);
							currState = SessionState.SESSION_INFO;
						}
					} catch (NumberFormatException e) {}	
				}
				break;
			
			case SEARCH_SESSIONS_CINEMA_SELECT:
				if (input.equals("0")) currState = SessionState.START;
				else if (input.isEmpty()) {}
				else {
					try {
						Integer cinemaIndex = Integer.parseInt(input) - 1;
						if (cinemaIndex >= 0 && cinemaIndex < cinemaList.size()) {
							setSearchByCinemaResultsState(cinemaList.get(cinemaIndex));
						}
					} catch (NumberFormatException e) {}	
				}
				break;

			case ADD_SESSION_CINEMA_SELECT:
				if (input.equals("0")) currState = SessionState.SHOW_ALL_SESSIONS;
				else if (input.isEmpty()) {}
				else {
					try {
						Integer cinemaIndex = Integer.parseInt(input) - 1;
						if (cinemaIndex >= 0 && cinemaIndex < cinemaList.size()) {
							selectedCinema = cinemaList.get(cinemaIndex);
							currState = SessionState.ADD_SESSION_HALL_SELECT;
						}
					} catch (NumberFormatException e) {}	
				}
				break;

			case ADD_SESSION_HALL_SELECT:
				if (input.equals("0")) currState = SessionState.SHOW_ALL_SESSIONS;
				else if (input.isEmpty()) {}
				else {
					try {
						Integer hallIndex = Integer.parseInt(input) - 1;
						ArrayList<HallController> halls = selectedCinema.getHalls();

						if (hallIndex >= 0 && hallIndex < halls.size()) {
							selectedHall = halls.get(hallIndex);
							currState = SessionState.ADD_SESSION_FILM_SELECT;
						}
					} catch (NumberFormatException e) {}	
				}
				break;

			case ADD_SESSION_FILM_SELECT:
				if (input.equals("0")) currState = SessionState.SHOW_ALL_SESSIONS;
				else if (input.isEmpty()) {}
				else {
					try {
						Integer filmIndex = Integer.parseInt(input) - 1;
						if (filmIndex >= 0 && filmIndex < films.size()) {
							selectedFilm = films.get(filmIndex);
							currState = SessionState.ADD_SESSION_DATE_SELECT;
						}
					} catch (NumberFormatException e) {}	
				}
				break;

			case ADD_SESSION_DATE_SELECT:
				if (input.equals("0")) currState = SessionState.SHOW_ALL_SESSIONS;
				else if (input.isEmpty()) {}
				else {
					try {
						SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
						newSessionDate = dateFormat.parse(input);
						currState = SessionState.ADD_SESSION_TICKET_PRICE;
					} catch (ParseException e) {}		
				}
				break;

			case ADD_SESSION_TICKET_PRICE:
				if (input.equals("0")) currState = SessionState.SHOW_ALL_SESSIONS;
				else if (input.isEmpty()) {}
				else {
					try {
						newSessionTicketPrice = Integer.parseInt(input);
						if (newSessionTicketPrice > 0) currState = SessionState.ADD_SESSION_CONFIRM;
					} catch (NumberFormatException e) {}	
				}
				break;
			
			case ADD_SESSION_CONFIRM:
				if (input.equals("0")) currState = SessionState.SHOW_ALL_SESSIONS;
				else if (input.isEmpty()) {
					sessions.add(new SessionController(
								selectedFilm, selectedCinema, 
								selectedHall, newSessionDate, 
								newSessionTicketPrice));
					currState = SessionState.SHOW_ALL_SESSIONS;
					selectedFilm = null;
					selectedCinema = null;
				}
				break;
		}

	}

}

