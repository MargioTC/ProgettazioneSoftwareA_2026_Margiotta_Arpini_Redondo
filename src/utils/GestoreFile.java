package utils;

import model.Film;
import model.Prenotazione;
import model.Proiezione;
import model.Utente;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce la lettura e la scrittura dei dati su file CSV.
 * I file sono memorizzati nella cartella {@code data/}.
 */
public class GestoreFile {

    /** Percorso del file delle proiezioni. */
    public static final String FILE_PROIEZIONI = "data/proiezioni.csv";

    /** Percorso del file degli utenti. */
    public static final String FILE_UTENTI = "data/utenti.csv";

    /** Percorso del file delle prenotazioni. */
    public static final String FILE_PRENOTAZIONI = "data/prenotazioni.csv";

    /** Separatore usato nei CSV. */
    private static final String SEP = ";";

    // ===================== PROIEZIONI =====================

    /**
     * Carica tutte le proiezioni dal file CSV.
     *
     * @return lista delle proiezioni caricate
     */
    public static List<Proiezione> caricaProiezioni() {
        List<Proiezione> lista = new ArrayList<>();
        File f = new File(FILE_PROIEZIONI);
        if (!f.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(f), StandardCharsets.UTF_8))) {
            String linea;
            boolean prima = true;
            while ((linea = br.readLine()) != null) {
                if (prima) { prima = false; continue; } // salta intestazione
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                String[] campi = linea.split(SEP, -1);
                if (campi.length < 9) continue;
                try {
                    String id            = campi[0].trim();
                    String titolo        = campi[1].trim();
                    String genere        = campi[2].trim();
                    String regista       = campi[3].trim();
                    int    anno          = Integer.parseInt(campi[4].trim());
                    int    durata        = Integer.parseInt(campi[5].trim());
                    int    etaMinima     = Integer.parseInt(campi[6].trim());
                    LocalDateTime dataOra = LocalDateTime.parse(campi[7].trim(), Proiezione.FORMATTER);
                    double costo         = Double.parseDouble(campi[8].trim().replace(",", "."));

                    Film film = new Film(titolo, genere, regista, anno, durata, etaMinima);
                    lista.add(new Proiezione(id, film, dataOra, costo));
                } catch (Exception e) {
                    System.err.println("Riga proiezione non valida: " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore lettura proiezioni: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Salva tutte le proiezioni sul file CSV.
     *
     * @param proiezioni lista delle proiezioni da salvare
     */
    public static void salvaProiezioni(List<Proiezione> proiezioni) {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(FILE_PROIEZIONI), StandardCharsets.UTF_8))) {
            pw.println("id;titolo;genere;regista;anno;durata;etaMinima;dataOra;costo");
            for (Proiezione p : proiezioni) {
                Film f = p.getFilm();
                pw.printf("%s%s%s%s%s%s%s%s%d%s%d%s%d%s%s%s%.2f%n",
                        p.getId(), SEP,
                        f.getTitolo(), SEP,
                        f.getGenere(), SEP,
                        f.getRegista(), SEP,
                        f.getAnno(), SEP,
                        f.getDurata(), SEP,
                        f.getEtaMinima(), SEP,
                        p.getDataOraFormattata(), SEP,
                        p.getCostoBiglietto());
            }
        } catch (IOException e) {
            System.err.println("Errore scrittura proiezioni: " + e.getMessage());
        }
    }

    // ===================== UTENTI =====================

    /**
     * Carica tutti gli utenti dal file CSV.
     *
     * @return lista degli utenti caricati
     */
    public static List<Utente> caricaUtenti() {
        List<Utente> lista = new ArrayList<>();
        File f = new File(FILE_UTENTI);
        if (!f.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(f), StandardCharsets.UTF_8))) {
            String linea;
            boolean prima = true;
            while ((linea = br.readLine()) != null) {
                if (prima) { prima = false; continue; }
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                String[] campi = linea.split(SEP, -1);
                if (campi.length < 7) continue;
                try {
                    lista.add(new Utente(
                            campi[0].trim(), // nome
                            campi[1].trim(), // cognome
                            campi[2].trim(), // username
                            campi[3].trim(), // passwordHash
                            campi[4].trim(), // dataNascita
                            campi[5].trim(), // domicilio
                            campi[6].trim()  // ruolo
                    ));
                } catch (Exception e) {
                    System.err.println("Riga utente non valida: " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore lettura utenti: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Salva tutti gli utenti sul file CSV.
     *
     * @param utenti lista degli utenti da salvare
     */
    public static void salvaUtenti(List<Utente> utenti) {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(FILE_UTENTI), StandardCharsets.UTF_8))) {
            pw.println("nome;cognome;username;passwordHash;dataNascita;domicilio;ruolo");
            for (Utente u : utenti) {
                pw.printf("%s%s%s%s%s%s%s%s%s%s%s%s%s%n",
                        u.getNome(), SEP,
                        u.getCognome(), SEP,
                        u.getUsername(), SEP,
                        u.getPasswordHash(), SEP,
                        u.getDataNascita(), SEP,
                        u.getDomicilio(), SEP,
                        u.getRuolo());
            }
        } catch (IOException e) {
            System.err.println("Errore scrittura utenti: " + e.getMessage());
        }
    }

    // ===================== PRENOTAZIONI =====================

    /**
     * Carica tutte le prenotazioni dal file CSV.
     *
     * @return lista delle prenotazioni caricate
     */
    public static List<Prenotazione> caricaPrenotazioni() {
        List<Prenotazione> lista = new ArrayList<>();
        File f = new File(FILE_PRENOTAZIONI);
        if (!f.exists()) return lista;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(f), StandardCharsets.UTF_8))) {
            String linea;
            boolean prima = true;
            while ((linea = br.readLine()) != null) {
                if (prima) { prima = false; continue; }
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                String[] campi = linea.split(SEP, -1);
                if (campi.length < 5) continue;
                try {
                    lista.add(new Prenotazione(
                            campi[0].trim(), // codice UUID
                            campi[1].trim(), // usernameCliente
                            campi[2].trim(), // nomeCliente
                            campi[3].trim(), // idProiezione
                            Integer.parseInt(campi[4].trim()) // numeroPosti
                    ));
                } catch (Exception e) {
                    System.err.println("Riga prenotazione non valida: " + linea);
                }
            }
        } catch (IOException e) {
            System.err.println("Errore lettura prenotazioni: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Salva tutte le prenotazioni sul file CSV.
     *
     * @param prenotazioni lista delle prenotazioni da salvare
     */
    public static void salvaPrenotazioni(List<Prenotazione> prenotazioni) {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(FILE_PRENOTAZIONI), StandardCharsets.UTF_8))) {
            pw.println("codice;usernameCliente;nomeCliente;idProiezione;numeroPosti");
            for (Prenotazione p : prenotazioni) {
                pw.printf("%s%s%s%s%s%s%s%s%d%n",
                        p.getCodice(), SEP,
                        p.getUsernameCliente(), SEP,
                        p.getNomeCliente(), SEP,
                        p.getIdProiezione(), SEP,
                        p.getNumeroPosti());
            }
        } catch (IOException e) {
            System.err.println("Errore scrittura prenotazioni: " + e.getMessage());
        }
    }

    // ===================== UTILITY =====================

    /**
     * Calcola l'hash SHA-256 di una stringa (usato per cifrare le password).
     *
     * @param input stringa da cifrare
     * @return hash SHA-256 in formato esadecimale, oppure stringa vuota in caso di errore
     */
    public static String hashSHA256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Errore hashing: " + e.getMessage());
            return "";
        }
    }

    /**
     * Inizializza i file di dati se non esistono, creando file vuoti con intestazione.
     * Popola anche il file utenti con i dati di default (2 proiezionisti, 5 bigliettai).
     */
    public static void inizializzaFileSeNonEsistono() {
        File dataDir = new File("data");
        if (!dataDir.exists()) dataDir.mkdirs();

        // Proiezioni
        File fp = new File(FILE_PROIEZIONI);
        if (!fp.exists()) {
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(fp), StandardCharsets.UTF_8))) {
                pw.println("id;titolo;genere;regista;anno;durata;etaMinima;dataOra;costo");
            } catch (IOException e) {
                System.err.println("Errore creazione file proiezioni: " + e.getMessage());
            }
        }

        // Prenotazioni
        File fpr = new File(FILE_PRENOTAZIONI);
        if (!fpr.exists()) {
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(fpr), StandardCharsets.UTF_8))) {
                pw.println("codice;usernameCliente;nomeCliente;idProiezione;numeroPosti");
            } catch (IOException e) {
                System.err.println("Errore creazione file prenotazioni: " + e.getMessage());
            }
        }

        // Utenti: crea con utenti di default se non esiste
        File fu = new File(FILE_UTENTI);
        if (!fu.exists()) {
            List<Utente> utentiDefault = new ArrayList<>();
            String pwDefault = hashSHA256("password123");

            // 2 proiezionisti
            utentiDefault.add(new Utente("Mario",  "Rossi",   "mrossi",   pwDefault, "01/01/1980", "Milano",  Utente.RUOLO_PROIEZIONISTA));
            utentiDefault.add(new Utente("Laura",  "Bianchi", "lbianchi", pwDefault, "15/06/1975", "Varese",  Utente.RUOLO_PROIEZIONISTA));

            // 5 bigliettai
            utentiDefault.add(new Utente("Anna",   "Verdi",   "averdi",   pwDefault, "20/03/1990", "Como",    Utente.RUOLO_BIGLIETTAIO));
            utentiDefault.add(new Utente("Luca",   "Neri",    "lneri",    pwDefault, "05/11/1988", "Varese",  Utente.RUOLO_BIGLIETTAIO));
            utentiDefault.add(new Utente("Sara",   "Gialli",  "sgialli",  pwDefault, "12/07/1993", "Milano",  Utente.RUOLO_BIGLIETTAIO));
            utentiDefault.add(new Utente("Paolo",  "Marini",  "pmarini",  pwDefault, "30/09/1985", "Gallarate",Utente.RUOLO_BIGLIETTAIO));
            utentiDefault.add(new Utente("Elena",  "Ferri",   "eferri",   pwDefault, "22/02/1991", "Busto Arsizio", Utente.RUOLO_BIGLIETTAIO));

            salvaUtenti(utentiDefault);
        }
    }

}
