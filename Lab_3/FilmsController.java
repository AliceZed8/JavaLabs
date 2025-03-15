package Lab_3;

import java.util.ArrayList;

public class FilmsController extends BaseController {
	public enum FilmControllerState {
		START,
		ADD_FILM_TITLE,
		ADD_FILM_YEAR,
		ADD_FILM_DURATION,
		FILM_INFO,
		ERROR,
		FINAL
	};	
	private FilmControllerState currState;

	private ArrayList<Film> films;
	private Film selectedFilm;

	private String newFilmTitle;
	private Integer newFilmYear, newFilmDuration;
	
	private Boolean redirectToSessions;


	public FilmsController() {
		super("Фильмы");
		films = new ArrayList<Film>();
		selectedFilm = new Film();

		newFilmTitle = "";
		newFilmYear = newFilmDuration = 0;
		currState = FilmControllerState.START;
		redirectToSessions = false;
	}
		
	public ArrayList<Film> getFilmsList() {
		return films;
	}
	
	public void addFilm(Film film) {
		films.add(film);
	}

	public Film getSelectedFilm() {
		return selectedFilm;
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
				System.out.println("Список фильмов:");
				for (int i = 0; i < films.size(); i++) {
					Film film = films.get(i);
					System.out.printf("%d) %s (%d год) %d мин. \n", i+1, film.getTitle(), film.getYear(), film.getDuration());
				}
				System.out.print("\n");
				if (currUser.isAdmin()) System.out.println("+) Добавить");
				System.out.println("0) Назад");
				System.out.print("> ");
				break;

			case ADD_FILM_TITLE:
				System.out.println("Добавление фильма (введите 0 для отмены)");
				System.out.print("Название: ");
				break;
			
			case ADD_FILM_YEAR:
				System.out.println("Добавление фильма (введите 0 для отмены)");
				System.out.printf("Название: %s\n", newFilmTitle);
				System.out.print("Год: ");
				break;

			case ADD_FILM_DURATION:
				System.out.println("Добавление фильма (введите 0 для отмены)");
				System.out.printf("Название: %s\n", newFilmTitle);
				System.out.printf("Год: %d\n", newFilmYear);
				System.out.print("Продолжительность (в мин.): ");
				break;

			case FILM_INFO:
				System.out.println("Информация о фильме");
				System.out.printf("Название: %s\n", selectedFilm.getTitle());
				System.out.printf("Год: %d\n", selectedFilm.getYear());
				System.out.printf("Продолжительность: %d мин.\n\n", selectedFilm.getDuration());
				
				if (currUser.isAdmin()) {
					System.out.println("-) Удалить фильм");
				}
				System.out.println("#) Найти сеанс");
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
					if (currUser.isAdmin()) currState = FilmControllerState.ADD_FILM_TITLE;
				}
				else if (input.isEmpty()) {}
				else {
					try {
						Integer filmIndex = Integer.parseInt(input);
						if (filmIndex > 0 && filmIndex <= films.size()) {
							selectedFilm = films.get(filmIndex - 1);
							currState = FilmControllerState.FILM_INFO;
						}
					} catch (NumberFormatException e) {
				errMsg = "Неправильный формат числа";
						currState = FilmControllerState.ERROR;
					}
				}
				break;

			case ADD_FILM_TITLE:
				if (input.equals("0")) currState = FilmControllerState.START;
				else {
					if (input.length() < 3) {
						currState = FilmControllerState.ERROR;
						errMsg = "Название должно быть не менее 3 символов";
					} else {
						newFilmTitle = input;
						currState = FilmControllerState.ADD_FILM_YEAR;
					}
				}
				break;

			case ADD_FILM_YEAR:
				if (input.equals("0")) currState = FilmControllerState.START;
				else {
					try {
						newFilmYear = Integer.parseInt(input);
						currState = FilmControllerState.ADD_FILM_DURATION;
					} catch (NumberFormatException e) {
						errMsg = "Неправильный формат числа";
						currState = FilmControllerState.ERROR;
					}
				}
				break;

			case ADD_FILM_DURATION:
				if (input.equals("0")) currState = FilmControllerState.START;
				else {
					try {
						newFilmDuration = Integer.parseInt(input);
						currState = FilmControllerState.FINAL;
						finishMsg = "Фильм успешно добавлен";
						
						films.add(new Film(newFilmTitle, newFilmYear, newFilmDuration));
					} catch (NumberFormatException e) {
						errMsg = "Неправильный формат числа";
						currState = FilmControllerState.ERROR;
					}
				}
				break;

			case FILM_INFO:
				if (input.equals("0")) currState = FilmControllerState.START;
				else if (input.equals("-")) {
					if (currUser.isAdmin()) {
						films.remove(selectedFilm);
						finishMsg = "Фильм успешно удалён";
						currState = FilmControllerState.FINAL;
					}
				}
				else if (input.equals("#")) {
					redirectToSessions = true;
					stop();
				}
				break;

			case ERROR:
				currState = FilmControllerState.START;
				break;

			case FINAL:
				currState = FilmControllerState.START;
				break;

		}
	}
};

