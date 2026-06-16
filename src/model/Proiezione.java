package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Rappresenta una proiezione di un film in una data e ora specifiche,
 * con il relativo costo del biglietto.
 */
public class Proiezione {

    /** Formato standard per date e ore nel sistema. */
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /** Identificatore univoco della proiezione. */
    private String id;

    /** Film associato alla proiezione. */
    private Film film;

    /** Data e ora della proiezione. */
    private LocalDateTime dataOra;

    /** Costo del biglietto in euro. */
    private double costoBiglietto;

    /** Capacità della sala (fissa a 200). */
    public static final int CAPACITA_SALA = 200;

    /**
     * Costruttore completo.
     *
     * @param id             identificatore univoco
     * @param film           film da proiettare
     * @param dataOra        data e ora della proiezione
     * @param costoBiglietto costo del biglietto in euro
     */
    public Proiezione(String id, Film film, LocalDateTime dataOra, double costoBiglietto) {
        this.id = id;
        this.film = film;
        this.dataOra = dataOra;
        this.costoBiglietto = costoBiglietto;
    }

    /** @return l'id della proiezione */
    public String getId() { return id; }

    /** @return il film associato */
    public Film getFilm() { return film; }

    /** @return la data e ora della proiezione */
    public LocalDateTime getDataOra() { return dataOra; }

    /** @return il costo del biglietto */
    public double getCostoBiglietto() { return costoBiglietto; }

    /** @param dataOra la nuova data e ora */
    public void setDataOra(LocalDateTime dataOra) { this.dataOra = dataOra; }

    /** @param costoBiglietto il nuovo costo del biglietto */
    public void setCostoBiglietto(double costoBiglietto) { this.costoBiglietto = costoBiglietto; }

    /** @param film il nuovo film */
    public void setFilm(Film film) { this.film = film; }

    /**
     * Restituisce la data e ora formattata come stringa.
     *
     * @return stringa formattata della data e ora
     */
    public String getDataOraFormattata() {
        return dataOra.format(FORMATTER);
    }

    /**
     * Restituisce una rappresentazione testuale della proiezione.
     *
     * @return stringa descrittiva della proiezione
     */
    @Override
    public String toString() {
        return String.format("[%s] %s | %s | € %.2f",
                id, film.getTitolo(), getDataOraFormattata(), costoBiglietto);
    }

}
