package Lab_3;

import java.util.ArrayList;

public class CinemaMainController extends BaseController {

	private CinemaListController cinemaListController; 
	private FilmsController filmsController;
	private SessionListController sessionListController;


	public CinemaMainController() {
		super("Главное меню");
		cinemaListController = new CinemaListController();
		filmsController = new FilmsController();
		sessionListController = new SessionListController(cinemaListController, filmsController);
		insertTestData();
	}
	
	

	@Override
	public void setUser(User user) {
		currUser = user;
		cinemaListController.setUser(user);
		filmsController.setUser(user);
		sessionListController.setUser(user);
	}

	@Override
	public void showMenu() {
		System.out.println("1) Сеансы");
		System.out.println("2) Фильмы");
		System.out.println("3) Кинотеатры");
		System.out.println("0) Назад");
	}

	@Override
	public void handleInput() {
		String input = scanner.nextLine();

		if (input.equals("0")) stop();
		else if (input.equals("1")) {
			sessionListController.run();	
		}
		else if (input.equals("2")) {
			while (true) {
				filmsController.run();
				if (filmsController.isRedirectToSessions()) { // если требуется переход к сеансам
					Film film = filmsController.getSelectedFilm();
					sessionListController.setSearchByFilmResultsState(film);
					sessionListController.setRedirect(true);
					sessionListController.run();

					filmsController.cancelRedirect();
					filmsController.setIsRunning(true);
				}
				if (!filmsController.getIsRunning()) break;
			}
		}
		else if (input.equals("3")) {
			while (true) {
				cinemaListController.run();
				if (cinemaListController.isRedirectToSessions()) { // если требуется переход к сеансам
					CinemaController cinema = cinemaListController.getSelectedCinema();
					sessionListController.setSearchByCinemaResultsState(cinema);
					sessionListController.setRedirect(true);
					sessionListController.run();

					cinemaListController.cancelRedirect();
					cinemaListController.setIsRunning(true);
				}
				if (!cinemaListController.getIsRunning()) break;
			}	
		}
	}


	public void insertTestData() {
		CinemaController cinema1 = new CinemaController("Каро", "пр-т. Ленинский, 30");
		CinemaController cinema2 = new CinemaController("Синема Парк", "ул. Профессора Баранова, 40");
		CinemaController cinema3 = new CinemaController("Люмен", "Московский пр., 257");

		ArrayList<Seat> seats = new ArrayList<Seat>();
		seats.add(new Seat(1, 1));
		seats.add(new Seat(1, 2));
		seats.add(new Seat(1, 3));
		seats.add(new Seat(1, 4));
		seats.add(new Seat(1, 5));
		seats.add(new Seat(1, 6));
		seats.add(new Seat(1, 7));
		seats.add(new Seat(3, 1));
		seats.add(new Seat(3, 2));
		seats.add(new Seat(3, 3));
		seats.add(new Seat(3, 4));
		seats.add(new Seat(3, 5));
		seats.add(new Seat(3, 6));
		seats.add(new Seat(3, 7));
		seats.add(new Seat(5, 1));
		seats.add(new Seat(5, 2));
		seats.add(new Seat(5, 3));
		seats.add(new Seat(5, 4));
		seats.add(new Seat(5, 5));
		seats.add(new Seat(5, 6));
		seats.add(new Seat(5, 7));

		HallController hall1 = new HallController("Зал 1", 10, 10);
		HallController hall2 = new HallController("Зал 2", 15, 15);
		HallController hall3 = new HallController("Зал 3", 10, 20);
		
		for (Seat seat: seats) {
			hall1.addSeat(seat);
			hall2.addSeat(seat);
			hall3.addSeat(seat);
		}
		hall3.addSeat(new Seat(9, 1));
		hall3.addSeat(new Seat(9, 2));
		hall3.addSeat(new Seat(9, 3));
		hall3.addSeat(new Seat(9, 4));
		hall3.addSeat(new Seat(9, 5));
		hall3.addSeat(new Seat(9, 6));
		hall3.addSeat(new Seat(9, 7));
		hall3.addSeat(new Seat(9, 8));

		cinema1.addHall(hall1);
		cinema1.addHall(hall1);
		cinema1.addHall(hall1);
		cinema1.addHall(hall2);
		cinema1.addHall(hall3);
		
		cinema2.addHall(hall1);
		cinema2.addHall(hall2);
		cinema2.addHall(hall2);
		cinema2.addHall(hall3);
	
		cinema3.addHall(hall1);
		cinema3.addHall(hall2);
		cinema3.addHall(hall1);
		cinema3.addHall(hall3);
		cinema3.addHall(hall3);
		cinema3.addHall(hall2);
		cinema3.addHall(hall1);
		
		cinemaListController.addCinema(cinema1);
		cinemaListController.addCinema(cinema2);
		cinemaListController.addCinema(cinema3);
		
		filmsController.addFilm(new Film("Ловушка Джокера", 2025, 120));
		filmsController.addFilm(new Film("Джокер", 2023, 100));
		filmsController.addFilm(new Film("Я бегемот S-класса", 2024, 80));
	}
}	
