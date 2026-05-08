package datiproiezione;

public class Proiezione {
	//CAMPI
	private String proiezione;
	private Film film;
	private double prezzo;
	
	//COSTRUTTORE
	public Proiezione(String proiezione, Film film, double prezzo, int numSala, int MAX_POSTI) {
		this.film = film;
		this.prezzo = prezzo;
		this.proiezione = proiezione;
	}

	//METODI
	public String getProiezione() {
		return proiezione;
	}

	public Film getFilm() {
		return film;
	}

	public double getPrezzo() {
		return prezzo;
	}	
}
