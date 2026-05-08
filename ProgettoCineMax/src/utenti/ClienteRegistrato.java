package utenti;

public class ClienteRegistrato extends ClienteNonRegistrato{
	//CAMPI
	private String username;
	private String password;
	private String luogoDelDomicilio;
	
	//COSTRUTTORI
	public ClienteRegistrato(String nome, String cognome, String dataDiNascita,String username, String password, String luogoDelDomicilio) {
		super(nome,cognome,dataDiNascita);
		this.username = username;
		this.password = password;
		this.luogoDelDomicilio = luogoDelDomicilio;
	}
	
	public ClienteRegistrato(String nome, String cognome, String dataDiNascita, String username, String password) {
		super(nome,cognome,dataDiNascita);
		this.username = username;
		this.password = password;
	}
	
	
	//METODI
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getLuogoDelDomicilio() {
		return luogoDelDomicilio;
	}
}