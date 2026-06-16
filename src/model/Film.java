package model;

/**
 * Rappresenta un film con le sue caratteristiche principali.
 */
public class Film {

    /** Titolo del film. */
    private String titolo;

    /** Genere del film (es. Azione, Commedia, Drammatico...). */
    private String genere;

    /** Regista del film. */
    private String regista;

    /** Anno di uscita del film. */
    private int anno;

    /** Durata del film in minuti. */
    private int durata;

    /** Età minima del pubblico (0 = per tutti). */
    private int etaMinima;

    /**
     * Costruttore completo.
     *
     * @param titolo   titolo del film
     * @param genere   genere del film
     * @param regista  regista del film
     * @param anno     anno di uscita
     * @param durata   durata in minuti
     * @param etaMinima età minima del pubblico
     */
    public Film(String titolo, String genere, String regista, int anno, int durata, int etaMinima) {
        this.titolo = titolo;
        this.genere = genere;
        this.regista = regista;
        this.anno = anno;
        this.durata = durata;
        this.etaMinima = etaMinima;
    }

    /** @return il titolo del film */
    public String getTitolo() { return titolo; }

    /** @return il genere del film */
    public String getGenere() { return genere; }

    /** @return il regista del film */
    public String getRegista() { return regista; }

    /** @return l'anno di uscita */
    public int getAnno() { return anno; }

    /** @return la durata in minuti */
    public int getDurata() { return durata; }

    /** @return l'età minima del pubblico */
    public int getEtaMinima() { return etaMinima; }

    /** @param titolo il nuovo titolo */
    public void setTitolo(String titolo) { this.titolo = titolo; }

    /** @param genere il nuovo genere */
    public void setGenere(String genere) { this.genere = genere; }

    /** @param regista il nuovo regista */
    public void setRegista(String regista) { this.regista = regista; }

    /** @param anno il nuovo anno */
    public void setAnno(int anno) { this.anno = anno; }

    /** @param durata la nuova durata */
    public void setDurata(int durata) { this.durata = durata; }

    /** @param etaMinima la nuova età minima */
    public void setEtaMinima(int etaMinima) { this.etaMinima = etaMinima; }

    /**
     * Restituisce una rappresentazione testuale del film.
     *
     * @return stringa descrittiva del film
     */
    @Override
    public String toString() {
        return String.format("Titolo: %s | Genere: %s | Regista: %s | Anno: %d | Durata: %d min | Età minima: %d",
                titolo, genere, regista, anno, durata, etaMinima);
    }

}
