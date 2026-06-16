package model;
import java.util.UUID;

/**
 * Rappresenta una prenotazione effettuata da un cliente per una proiezione.
 * Ogni prenotazione ha un codice univoco generato tramite UUID.
 */
public class Prenotazione {

    /** Codice univoco della prenotazione (UUID). */
    private String codice;

    /** Username del cliente che ha effettuato la prenotazione. */
    private String usernameCliente;

    /** Nome completo del cliente. */
    private String nomeCliente;

    /** ID della proiezione prenotata. */
    private String idProiezione;

    /** Numero di posti prenotati. */
    private int numeroPosti;

    /**
     * Costruttore per una nuova prenotazione (genera automaticamente il codice UUID).
     *
     * @param usernameCliente username del cliente
     * @param nomeCliente     nome completo del cliente
     * @param idProiezione    id della proiezione
     * @param numeroPosti     numero di posti da prenotare
     */
    public Prenotazione(String usernameCliente, String nomeCliente, String idProiezione, int numeroPosti) {
        this.codice = UUID.randomUUID().toString();
        this.usernameCliente = usernameCliente;
        this.nomeCliente = nomeCliente;
        this.idProiezione = idProiezione;
        this.numeroPosti = numeroPosti;
    }


    /**
     * Costruttore per caricare una prenotazione esistente da CSV (codice già noto).
     *
     * @param codice          codice univoco esistente
     * @param usernameCliente username del cliente
     * @param nomeCliente     nome completo del cliente
     * @param idProiezione    id della proiezione
     * @param numeroPosti     numero di posti prenotati
     */
    public Prenotazione(String codice, String usernameCliente, String nomeCliente,
                        String idProiezione, int numeroPosti) {
        this.codice = codice;
        this.usernameCliente = usernameCliente;
        this.nomeCliente = nomeCliente;
        this.idProiezione = idProiezione;
        this.numeroPosti = numeroPosti;
    }

    /** @return il codice univoco */
    public String getCodice() { return codice; }

    /** @return lo username del cliente */
    public String getUsernameCliente() { return usernameCliente; }

    /** @return il nome completo del cliente */
    public String getNomeCliente() { return nomeCliente; }

    /** @return l'id della proiezione */
    public String getIdProiezione() { return idProiezione; }

    /** @return il numero di posti prenotati */
    public int getNumeroPosti() { return numeroPosti; }

    /** @param idProiezione il nuovo id proiezione (per modifica prenotazione) */
    public void setIdProiezione(String idProiezione) { this.idProiezione = idProiezione; }

    /** @param numeroPosti il nuovo numero di posti */
    public void setNumeroPosti(int numeroPosti) { this.numeroPosti = numeroPosti; }

    /**
     * Restituisce una rappresentazione testuale della prenotazione.
     *
     * @return stringa descrittiva della prenotazione
     */
    @Override
    public String toString() {
        return String.format("Codice: %s | Cliente: %s | Proiezione: %s | Posti: %d",
                codice, nomeCliente, idProiezione, numeroPosti);
    }

}
