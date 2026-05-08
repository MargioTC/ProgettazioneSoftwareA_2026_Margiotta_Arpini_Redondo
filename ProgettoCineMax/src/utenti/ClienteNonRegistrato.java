package utenti;

public class ClienteNonRegistrato {
	//CAMPI
	private String nome;
	private String cognome;
	private String dataDiNascita;
	
	//COSTRUTTORE
	public ClienteNonRegistrato(String nome, String cognome, String dataDiNascita) {
		this.nome = nome;
		this.cognome = cognome;
		this.dataDiNascita = dataDiNascita;
	}
	
	//METODI
	public void cercaProiezione() {
		
	}

	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	public String getDataDiNascita() {
		return dataDiNascita;
	}
}
