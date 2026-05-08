package utenti;

public class Bigliettaio {
	//CAMPI
	private String nome;
	private String cognome;
	private String dataDiNascita;
	private String username;
	private String password;
	private String luogoDelDomicilio;
	
	//COSTRUTTORI
	public Bigliettaio (String nome, String cognome, String dataDiNascita, String username, String password, String luogoDelDomicilio) {
		this.nome = nome;
		this.cognome = cognome;
		this.dataDiNascita = dataDiNascita;
		this.username = username;
		this.password = password;
		this.luogoDelDomicilio = luogoDelDomicilio;
	}
	
	public Bigliettaio (String nome, String cognome, String username, String password, String luogoDelDomicilio) {
		this.nome = nome;
		this.cognome = cognome;
		this.username = username;
		this.password = password;
		this.luogoDelDomicilio = luogoDelDomicilio;
	}
	
	//METODI
}
