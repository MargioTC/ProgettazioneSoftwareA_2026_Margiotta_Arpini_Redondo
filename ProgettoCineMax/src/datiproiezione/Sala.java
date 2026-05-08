package datiproiezione;

public class Sala {
	//CAMPI
	private String nomeSala;
	private final int MAX_POSTI; 
	
	//COSTRUTTORE
	public Sala (String nomeSala, int MAX_POSTI) {
		this.nomeSala = nomeSala;
		this.MAX_POSTI = 200;
	}
	
	//METODI
	public String getNomeSala() {
		return nomeSala;
	}

	public int getMAX_POSTI() {
		return MAX_POSTI;
	}
}
