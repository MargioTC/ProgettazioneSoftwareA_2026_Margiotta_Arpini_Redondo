package datiproiezione;

public class Film {
	//CAMPI
	private String titolo;
	private String genere;
	private String regista;
	private int anno;
	private double durata;
	private int etaMinima;
	
	//COSTRUTTORI
	public Film (String titolo, String genere, String regista, int anno, double durata,int etaMinima) {
		this.titolo = titolo;
		this.genere = genere;
		this.regista = regista;
		this.anno = anno;
		this.durata = durata;
		this.etaMinima = etaMinima;
	}

	//METODI
	public String getTitolo() {
		return titolo;
	}

	public String getGenere() {
		return genere;
	}

	public String getRegista() {
		return regista;
	}

	public int getAnno() {
		return anno;
	}

	public double getDurata() {
		return durata;
	}

	public int getEtaMinima() {
		return etaMinima;
	}
}
