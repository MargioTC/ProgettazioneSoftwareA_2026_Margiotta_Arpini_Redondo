package model;
/**
 * Rappresenta un utente del sistema con il suo ruolo (cliente, proiezionista o bigliettaio).
 */
public class Utente {

    /** Ruolo cliente: può cercare proiezioni e fare prenotazioni. */
    public static final String RUOLO_CLIENTE = "cliente";

    /** Ruolo proiezionista: può gestire il palinsesto. */
    public static final String RUOLO_PROIEZIONISTA = "proiezionista";

    /** Ruolo bigliettaio: può cercare e visualizzare prenotazioni. */
    public static final String RUOLO_BIGLIETTAIO = "bigliettaio";

    /** Nome dell'utente. */
    private String nome;

    /** Cognome dell'utente. */
    private String cognome;

    /** Username univoco dell'utente. */
    private String username;

    /** Password cifrata (hash SHA-256). */
    private String passwordHash;

    /** Data di nascita (facoltativa, formato dd/MM/yyyy o stringa vuota). */
    private String dataNascita;

    /** Luogo di domicilio. */
    private String domicilio;

    /** Ruolo dell'utente nel sistema. */
    private String ruolo;

    /**
     * Costruttore completo.
     *
     * @param nome         nome dell'utente
     * @param cognome      cognome dell'utente
     * @param username     username univoco
     * @param passwordHash hash della password
     * @param dataNascita  data di nascita (può essere vuota)
     * @param domicilio    luogo di domicilio
     * @param ruolo        ruolo nel sistema
     */
    public Utente(String nome, String cognome, String username, String passwordHash,
                  String dataNascita, String domicilio, String ruolo) {
        this.nome = nome;
        this.cognome = cognome;
        this.username = username;
        this.passwordHash = passwordHash;
        this.dataNascita = dataNascita;
        this.domicilio = domicilio;
        this.ruolo = ruolo;
    }

    /** @return il nome */
    public String getNome() { return nome; }

    /** @return il cognome */
    public String getCognome() { return cognome; }

    /** @return il nome completo (nome + cognome) */
    public String getNomeCompleto() { return nome + " " + cognome; }

    /** @return lo username */
    public String getUsername() { return username; }

    /** @return l'hash della password */
    public String getPasswordHash() { return passwordHash; }

    /** @return la data di nascita */
    public String getDataNascita() { return dataNascita; }

    /** @return il domicilio */
    public String getDomicilio() { return domicilio; }

    /** @return il ruolo */
    public String getRuolo() { return ruolo; }

    /** @param nome il nuovo nome */
    public void setNome(String nome) { this.nome = nome; }

    /** @param cognome il nuovo cognome */
    public void setCognome(String cognome) { this.cognome = cognome; }

    /** @param passwordHash il nuovo hash della password */
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    /** @param dataNascita la nuova data di nascita */
    public void setDataNascita(String dataNascita) { this.dataNascita = dataNascita; }

    /** @param domicilio il nuovo domicilio */
    public void setDomicilio(String domicilio) { this.domicilio = domicilio; }

    /**
     * Verifica se l'utente ha il ruolo di cliente.
     *
     * @return true se è un cliente
     */
    public boolean isCliente() { return RUOLO_CLIENTE.equals(ruolo); }

    /**
     * Verifica se l'utente ha il ruolo di proiezionista.
     *
     * @return true se è un proiezionista
     */
    public boolean isProiezionista() { return RUOLO_PROIEZIONISTA.equals(ruolo); }

    /**
     * Verifica se l'utente ha il ruolo di bigliettaio.
     *
     * @return true se è un bigliettaio
     */
    public boolean isBigliettaio() { return RUOLO_BIGLIETTAIO.equals(ruolo); }

    /**
     * Restituisce una rappresentazione testuale dell'utente.
     *
     * @return stringa descrittiva dell'utente
     */
    @Override
    public String toString() {
        return String.format("%s %s (@%s) [%s] - %s", nome, cognome, username, ruolo, domicilio);
    }

}
