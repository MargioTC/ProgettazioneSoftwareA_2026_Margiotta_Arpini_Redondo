package main;
import model.Film;
import model.Prenotazione;
import model.Proiezione;
import model.Utente;
import utils.GestoreFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Punto di ingresso dell'applicazione CineMax.
 * Gestisce il menu principale e smista le operazioni in base al ruolo dell'utente.
 */
public class CineMax {


    /** Scanner globale per l'input da tastiera. */
    private static final Scanner sc = new Scanner(System.in);

    /** Formatter per le date (senza orario). */
    private static final DateTimeFormatter FMT_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /** Formatter per data e ora. */
    private static final DateTimeFormatter FMT_DATAORA = Proiezione.FORMATTER;

    /** Lista in memoria delle proiezioni. */
    private static List<Proiezione> proiezioni;

    /** Lista in memoria degli utenti. */
    private static List<Utente> utenti;

    /** Lista in memoria delle prenotazioni. */
    private static List<Prenotazione> prenotazioni;

    /** Utente attualmente loggato (null se guest). */
    private static Utente utenteCorrente = null;

    // ===================== MAIN =====================

    /**
     * Metodo principale di avvio dell'applicazione.
     *
     * @param args argomenti da riga di comando (non utilizzati)
     */
    public static void main(String[] args) {
        GestoreFile.inizializzaFileSeNonEsistono();
        proiezioni   = GestoreFile.caricaProiezioni();
        utenti       = GestoreFile.caricaUtenti();
        prenotazioni = GestoreFile.caricaPrenotazioni();

        System.out.println("");
        System.out.println("       Benvenuto in CineMax       ");
        System.out.println("");

        menuPrincipale();
    }

    // ===================== MENU PRINCIPALE =====================

    /**
     * Mostra il menu principale e gestisce login, registrazione e accesso guest.
     */
    private static void menuPrincipale() {
        while (true) {
            System.out.println("\n--- MENU PRINCIPALE ---");
            System.out.println("1. Login");
            System.out.println("2. Registrati come cliente");
            System.out.println("3. Continua come guest");
            System.out.println("0. Esci");
            System.out.print("Scelta: ");
            String scelta = sc.nextLine().trim();

            switch (scelta) {
                case "1": login(); break;
                case "2": registraCliente(); break;
                case "3": menuGuest(); break;
                case "0":
                    System.out.println("Arrivederci!");
                    return;
                default:
                    System.out.println("Scelta non valida.");
            }
        }
    }

    // ===================== LOGIN / LOGOUT =====================

    /**
     * Gestisce il processo di login dell'utente.
     */
    private static void login() {
        System.out.print("Username: ");
        String username = sc.nextLine().trim();
        System.out.print("Password: ");
        String password = sc.nextLine().trim();
        String hash = GestoreFile.hashSHA256(password);

        Utente trovato = utenti.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPasswordHash().equals(hash))
                .findFirst().orElse(null);

        if (trovato == null) {
            System.out.println("Credenziali errate.");
            return;
        }

        utenteCorrente = trovato;
        System.out.println("Benvenuto, " + trovato.getNomeCompleto() + " [" + trovato.getRuolo() + "]");

        if (trovato.isCliente())        menuCliente();
        else if (trovato.isProiezionista()) menuProiezionista();
        else if (trovato.isBigliettaio())   menuBigliettaio();

        utenteCorrente = null;
    }

    /**
     * Effettua il logout dell'utente corrente.
     */
    private static void logout() {
        System.out.println("Logout effettuato. Arrivederci, " + utenteCorrente.getNome() + "!");
        utenteCorrente = null;
    }

    // ===================== REGISTRAZIONE =====================

    /**
     * Registra un nuovo cliente nel sistema.
     */
    private static void registraCliente() {
        System.out.println("\n--- REGISTRAZIONE CLIENTE ---");
        System.out.print("Nome: ");
        String nome = sc.nextLine().trim();
        System.out.print("Cognome: ");
        String cognome = sc.nextLine().trim();

        String username;
        while (true) {
            System.out.print("Username: ");
            username = sc.nextLine().trim();
            if (username.isEmpty()) { System.out.println("Username non può essere vuoto."); continue; }
            String u = username;
            if (utenti.stream().anyMatch(x -> x.getUsername().equals(u))) {
                System.out.println("Username già in uso. Sceglierne un altro.");
            } else break;
        }

        System.out.print("Password: ");
        String password = sc.nextLine().trim();
        if (password.isEmpty()) { System.out.println("Password non può essere vuota."); return; }
        String hash = GestoreFile.hashSHA256(password);

        System.out.print("Data di nascita (dd/MM/yyyy, premi Invio per saltare): ");
        String dataNascita = sc.nextLine().trim();
        if (!dataNascita.isEmpty()) {
            try { LocalDate.parse(dataNascita, FMT_DATA); }
            catch (DateTimeParseException e) {
                System.out.println("Formato data non valido. Campo lasciato vuoto.");
                dataNascita = "";
            }
        }

        System.out.print("Domicilio: ");
        String domicilio = sc.nextLine().trim();

        Utente nuovo = new Utente(nome, cognome, username, hash, dataNascita, domicilio, Utente.RUOLO_CLIENTE);
        utenti.add(nuovo);
        GestoreFile.salvaUtenti(utenti);
        System.out.println("Registrazione completata! Puoi ora effettuare il login.");
    }

    // ===================== MENU GUEST =====================

    /**
     * Menu per utenti non autenticati (guest).
     */
    private static void menuGuest() {
        System.out.print("Cerca un film (titolo parziale, premi Invio per vedere tutti): ");
        String query = sc.nextLine().trim();

        List<Proiezione> risultati = proiezioni.stream()
                .filter(p -> p.getFilm().getTitolo().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        if (risultati.isEmpty()) {
            System.out.println("Nessuna proiezione trovata per: " + query);
        } else {
            System.out.println("\nProiezioni trovate:");
            stampaListaProiezioni(risultati);
        }

        while (true) {
            System.out.println("\n--- MENU GUEST ---");
            System.out.println("1. Cerca proiezioni");
            System.out.println("2. Visualizza dettagli proiezione");
            System.out.println("0. Torna al menu principale");
            System.out.print("Scelta: ");
            String scelta = sc.nextLine().trim();

            switch (scelta) {
                case "1": cercaProiezione(); break;
                case "2": visualizzaProiezione(); break;
                case "0": return;
                default: System.out.println("Scelta non valida.");
            }
        }
    }

    // ===================== MENU CLIENTE =====================

    /**
     * Menu per clienti autenticati.
     */
    private static void menuCliente() {
        while (true) {
            System.out.println("\n--- MENU CLIENTE ---");
            System.out.println("1. Cerca proiezioni");
            System.out.println("2. Visualizza dettagli proiezione");
            System.out.println("3. Prenota posti");
            System.out.println("4. Le mie prenotazioni");
            System.out.println("5. Modifica una prenotazione");
            System.out.println("6. Cancella una prenotazione");
            System.out.println("0. Logout");
            System.out.print("Scelta: ");
            String scelta = sc.nextLine().trim();

            switch (scelta) {
                case "1": cercaProiezione(); break;
                case "2": visualizzaProiezione(); break;
                case "3": creaPrenotazione(); break;
                case "4": visualizzaMiePrenotazioni(); break;
                case "5": modificaPrenotazione(); break;
                case "6": eliminaPrenotazione(); break;
                case "0": logout(); return;
                default: System.out.println("Scelta non valida.");
            }
        }
    }

    // ===================== MENU PROIEZIONISTA =====================

    /**
     * Menu per proiezionisti autenticati.
     */
    private static void menuProiezionista() {
        while (true) {
            System.out.println("\n--- MENU PROIEZIONISTA ---");
            System.out.println("1. Visualizza tutte le proiezioni");
            System.out.println("2. Aggiungi proiezione");
            System.out.println("3. Modifica proiezione");
            System.out.println("4. Elimina proiezione");
            System.out.println("0. Logout");
            System.out.print("Scelta: ");
            String scelta = sc.nextLine().trim();

            switch (scelta) {
                case "1": stampaListaProiezioni(proiezioni); break;
                case "2": aggiungiProiezione(); break;
                case "3": modificaProiezione(); break;
                case "4": eliminaProiezione(); break;
                case "0": logout(); return;
                default: System.out.println("Scelta non valida.");
            }
        }
    }

    // ===================== MENU BIGLIETTAIO =====================

    /**
     * Menu per bigliettai autenticati.
     */
    private static void menuBigliettaio() {
        while (true) {
            System.out.println("\n--- MENU BIGLIETTAIO ---");
            System.out.println("1. Prenotazioni di oggi");
            System.out.println("2. Cerca prenotazione");
            System.out.println("0. Logout");
            System.out.print("Scelta: ");
            String scelta = sc.nextLine().trim();

            switch (scelta) {
                case "1": visualizzaPrenotazioniOggi(); break;
                case "2": cercaPrenotazione(); break;
                case "0": logout(); return;
                default: System.out.println("Scelta non valida.");
            }
        }
    }

    // ===================== FUNZIONALITÀ PROIEZIONI =====================

    /**
     * Ricerca proiezioni in base a uno o più criteri combinabili.
     */
    private static void cercaProiezione() {
        System.out.println("\n--- CERCA PROIEZIONE ---");
        System.out.print("Titolo (parziale, Invio per saltare): ");
        String titolo = sc.nextLine().trim();

        System.out.print("Genere (Invio per saltare): ");
        String genere = sc.nextLine().trim();

        System.out.print("Data inizio (dd/MM/yyyy, Invio per saltare): ");
        String dataInizioStr = sc.nextLine().trim();

        System.out.print("Data fine (dd/MM/yyyy, Invio per saltare): ");
        String dataFineStr = sc.nextLine().trim();

        System.out.print("Costo massimo (es. 10.00, Invio per saltare): ");
        String costoMaxStr = sc.nextLine().trim();

        System.out.print("Costo minimo (es. 5.00, Invio per saltare): ");
        String costoMinStr = sc.nextLine().trim();

        LocalDate dataInizio = null, dataFine = null;
        double costoMax = Double.MAX_VALUE, costoMin = 0;

        try { if (!dataInizioStr.isEmpty()) dataInizio = LocalDate.parse(dataInizioStr, FMT_DATA); }
        catch (DateTimeParseException e) { System.out.println("Data inizio non valida, ignorata."); }

        try { if (!dataFineStr.isEmpty()) dataFine = LocalDate.parse(dataFineStr, FMT_DATA); }
        catch (DateTimeParseException e) { System.out.println("Data fine non valida, ignorata."); }

        try { if (!costoMaxStr.isEmpty()) costoMax = Double.parseDouble(costoMaxStr.replace(",", ".")); }
        catch (NumberFormatException e) { System.out.println("Costo massimo non valido, ignorato."); }

        try { if (!costoMinStr.isEmpty()) costoMin = Double.parseDouble(costoMinStr.replace(",", ".")); }
        catch (NumberFormatException e) { System.out.println("Costo minimo non valido, ignorato."); }

        final LocalDate di = dataInizio, df = dataFine;
        final double cMax = costoMax, cMin = costoMin;

        List<Proiezione> risultati = proiezioni.stream()
                .filter(p -> titolo.isEmpty() || p.getFilm().getTitolo().toLowerCase().contains(titolo.toLowerCase()))
                .filter(p -> genere.isEmpty() || p.getFilm().getGenere().equalsIgnoreCase(genere))
                .filter(p -> di == null || !p.getDataOra().toLocalDate().isBefore(di))
                .filter(p -> df == null || !p.getDataOra().toLocalDate().isAfter(df))
                .filter(p -> p.getCostoBiglietto() >= cMin && p.getCostoBiglietto() <= cMax)
                .collect(Collectors.toList());

        if (risultati.isEmpty()) {
            System.out.println("Nessuna proiezione trovata con i criteri specificati.");
        } else {
            System.out.println("\nRisultati (" + risultati.size() + "):");
            stampaListaProiezioni(risultati);
        }
    }

    /**
     * Visualizza i dettagli completi di una proiezione selezionata per ID.
     */
    private static void visualizzaProiezione() {
        System.out.print("Inserisci ID proiezione: ");
        String id = sc.nextLine().trim();
        Proiezione p = trovaProiezioneById(id);
        if (p == null) { System.out.println("Proiezione non trovata."); return; }

        long postiFree = calcolaPostiLiberi(p);
        Film f = p.getFilm();

        System.out.println("\n========== DETTAGLI PROIEZIONE ==========");
        System.out.println("ID:           " + p.getId());
        System.out.println("Titolo:       " + f.getTitolo());
        System.out.println("Genere:       " + f.getGenere());
        System.out.println("Regista:      " + f.getRegista());
        System.out.println("Anno:         " + f.getAnno());
        System.out.println("Durata:       " + f.getDurata() + " min");
        System.out.println("Età minima:   " + (f.getEtaMinima() == 0 ? "Per tutti" : f.getEtaMinima() + " anni"));
        System.out.println("Data/Ora:     " + p.getDataOraFormattata());
        System.out.printf ("Costo:        € %.2f%n", p.getCostoBiglietto());
        System.out.println("Posti liberi: " + postiFree + "/" + Proiezione.CAPACITA_SALA);
        System.out.println("==========================================");
    }

    /**
     * Aggiunge una nuova proiezione (solo proiezionista).
     */
    private static void aggiungiProiezione() {
        System.out.println("\n--- AGGIUNGI PROIEZIONE ---");

        // Dati film
        System.out.print("Titolo film: ");
        String titolo = sc.nextLine().trim();
        System.out.print("Genere: ");
        String genere = sc.nextLine().trim();
        System.out.print("Regista: ");
        String regista = sc.nextLine().trim();

        int anno = leggiIntero("Anno: ", 1888, 2100);
        int durata = leggiIntero("Durata (minuti): ", 1, 600);
        int etaMin = leggiIntero("Età minima (0 = per tutti): ", 0, 18);

        // Data e ora
        LocalDateTime dataOra = leggiDataOra();
        if (dataOra == null) return;

        // Controlla sovrapposizioni (stessa data/ora esatta o sovrapposizione per durata)
        boolean sovrappone = proiezioni.stream().anyMatch(p -> {
            LocalDateTime inizioEsistente = p.getDataOra();
            LocalDateTime fineEsistente   = inizioEsistente.plusMinutes(p.getFilm().getDurata() + 30);
            LocalDateTime inizioNuovo     = dataOra;
            LocalDateTime fineNuovo       = dataOra.plusMinutes(durata + 30);
            return inizioNuovo.isBefore(fineEsistente) && inizioEsistente.isBefore(fineNuovo);
        });

        if (sovrappone) {
            System.out.println("Impossibile aggiungere: la proiezione si sovrappone con una esistente.");
            return;
        }

        double costo = leggiDouble("Costo biglietto (€): ", 0.01, 999.99);

        String id = "P" + System.currentTimeMillis();
        Film film = new Film(titolo, genere, regista, anno, durata, etaMin);
        Proiezione nuova = new Proiezione(id, film, dataOra, costo);
        proiezioni.add(nuova);
        GestoreFile.salvaProiezioni(proiezioni);
        System.out.println("Proiezione aggiunta con ID: " + id);
    }

    /**
     * Modifica la data di una proiezione esistente (solo se nessuna prenotazione).
     */
    private static void modificaProiezione() {
        System.out.print("ID proiezione da modificare: ");
        String id = sc.nextLine().trim();
        Proiezione p = trovaProiezioneById(id);
        if (p == null) { System.out.println("Proiezione non trovata."); return; }

        boolean haPrenotazioni = prenotazioni.stream().anyMatch(pr -> pr.getIdProiezione().equals(id));
        if (haPrenotazioni) {
            System.out.println("Impossibile modificare: esistono prenotazioni per questa proiezione.");
            return;
        }

        System.out.println("Proiezione corrente: " + p);
        System.out.println("Inserisci nuova data e ora:");
        LocalDateTime nuovaData = leggiDataOra();
        if (nuovaData == null) return;

        p.setDataOra(nuovaData);
        GestoreFile.salvaProiezioni(proiezioni);
        System.out.println("Proiezione modificata.");
    }

    /**
     * Elimina una proiezione (solo se nessuna prenotazione).
     */
    private static void eliminaProiezione() {
        System.out.print("ID proiezione da eliminare: ");
        String id = sc.nextLine().trim();
        Proiezione p = trovaProiezioneById(id);
        if (p == null) { System.out.println("Proiezione non trovata."); return; }

        boolean haPrenotazioni = prenotazioni.stream().anyMatch(pr -> pr.getIdProiezione().equals(id));
        if (haPrenotazioni) {
            System.out.println("Impossibile eliminare: esistono prenotazioni per questa proiezione.");
            return;
        }

        proiezioni.remove(p);
        GestoreFile.salvaProiezioni(proiezioni);
        System.out.println("Proiezione eliminata.");
    }

    // ===================== FUNZIONALITÀ PRENOTAZIONI (CLIENTE) =====================

    /**
     * Crea una nuova prenotazione per il cliente corrente.
     */
    private static void creaPrenotazione() {
        cercaProiezione();
        System.out.print("Inserisci ID proiezione da prenotare: ");
        String id = sc.nextLine().trim();
        Proiezione p = trovaProiezioneById(id);
        if (p == null) { System.out.println("Proiezione non trovata."); return; }

        if (p.getDataOra().isBefore(LocalDateTime.now())) {
            System.out.println("Non è possibile prenotare una proiezione già passata.");
            return;
        }

        long postiLiberi = calcolaPostiLiberi(p);
        System.out.println("Posti liberi: " + postiLiberi);

        int nPosti = leggiIntero("Numero posti da prenotare: ", 1, (int) postiLiberi);
        if (nPosti > postiLiberi) {
            System.out.println("Posti insufficienti.");
            return;
        }

        Prenotazione pr = new Prenotazione(
                utenteCorrente.getUsername(),
                utenteCorrente.getNomeCompleto(),
                id,
                nPosti
        );
        prenotazioni.add(pr);
        GestoreFile.salvaPrenotazioni(prenotazioni);
        System.out.println("Prenotazione confermata! Codice: " + pr.getCodice());
    }

    /**
     * Visualizza le prenotazioni del cliente corrente.
     */
    private static void visualizzaMiePrenotazioni() {
        List<Prenotazione> mie = prenotazioni.stream()
                .filter(pr -> pr.getUsernameCliente().equals(utenteCorrente.getUsername()))
                .collect(Collectors.toList());

        if (mie.isEmpty()) { System.out.println("Nessuna prenotazione trovata."); return; }

        System.out.println("\n--- LE MIE PRENOTAZIONI ---");
        for (Prenotazione pr : mie) {
            Proiezione p = trovaProiezioneById(pr.getIdProiezione());
            String infoFilm = p != null ? p.getFilm().getTitolo() + " | " + p.getDataOraFormattata() : "[proiezione non trovata]";
            double costoTot = p != null ? p.getCostoBiglietto() * pr.getNumeroPosti() : 0;
            System.out.printf("Codice: %s | %s | Posti: %d | Totale: € %.2f%n",
                    pr.getCodice(), infoFilm, pr.getNumeroPosti(), costoTot);
        }
    }

    /**
     * Modifica (cambia proiezione/data) di una prenotazione esistente del cliente.
     */
    private static void modificaPrenotazione() {
        visualizzaMiePrenotazioni();
        System.out.print("Inserisci codice prenotazione da modificare: ");
        String codice = sc.nextLine().trim();

        Prenotazione pr = prenotazioni.stream()
                .filter(x -> x.getCodice().equals(codice) && x.getUsernameCliente().equals(utenteCorrente.getUsername()))
                .findFirst().orElse(null);

        if (pr == null) { System.out.println("Prenotazione non trovata."); return; }

        Proiezione vecchia = trovaProiezioneById(pr.getIdProiezione());
        if (vecchia == null || vecchia.getDataOra().isBefore(LocalDateTime.now())) {
            System.out.println("La vecchia data è già passata: non è possibile modificare.");
            return;
        }

        System.out.println("Scegli la nuova proiezione:");
        cercaProiezione();
        System.out.print("ID nuova proiezione: ");
        String nuovoId = sc.nextLine().trim();
        Proiezione nuova = trovaProiezioneById(nuovoId);
        if (nuova == null) { System.out.println("Proiezione non trovata."); return; }

        if (nuova.getDataOra().isBefore(LocalDateTime.now())) {
            System.out.println("La nuova data è già passata: non è possibile modificare.");
            return;
        }

        long postiLiberi = calcolaPostiLiberi(nuova);
        if (pr.getNumeroPosti() > postiLiberi) {
            System.out.println("Posti insufficienti nella nuova proiezione.");
            return;
        }

        pr.setIdProiezione(nuovoId);
        GestoreFile.salvaPrenotazioni(prenotazioni);
        System.out.println("Prenotazione modificata.");
    }

    /**
     * Cancella una prenotazione del cliente (solo se la proiezione è futura).
     */
    private static void eliminaPrenotazione() {
        visualizzaMiePrenotazioni();
        System.out.print("Inserisci codice prenotazione da cancellare: ");
        String codice = sc.nextLine().trim();

        Prenotazione pr = prenotazioni.stream()
                .filter(x -> x.getCodice().equals(codice) && x.getUsernameCliente().equals(utenteCorrente.getUsername()))
                .findFirst().orElse(null);

        if (pr == null) { System.out.println("Prenotazione non trovata."); return; }

        Proiezione p = trovaProiezioneById(pr.getIdProiezione());
        if (p != null && p.getDataOra().isBefore(LocalDateTime.now())) {
            System.out.println("Non è possibile cancellare una prenotazione per una proiezione già passata.");
            return;
        }

        prenotazioni.remove(pr);
        GestoreFile.salvaPrenotazioni(prenotazioni);
        System.out.println("Prenotazione cancellata.");
    }

    // ===================== FUNZIONALITÀ BIGLIETTAIO =====================

    /**
     * Visualizza tutte le prenotazioni per le proiezioni di oggi.
     */
    private static void visualizzaPrenotazioniOggi() {
        LocalDate oggi = LocalDate.now();
        System.out.println("\n--- PRENOTAZIONI DI OGGI (" + oggi.format(FMT_DATA) + ") ---");

        List<Prenotazione> oggi_pr = prenotazioni.stream()
                .filter(pr -> {
                    Proiezione p = trovaProiezioneById(pr.getIdProiezione());
                    return p != null && p.getDataOra().toLocalDate().equals(oggi);
                })
                .collect(Collectors.toList());

        if (oggi_pr.isEmpty()) { System.out.println("Nessuna prenotazione per oggi."); return; }

        for (Prenotazione pr : oggi_pr) {
            stampaDettaglioPrenotazione(pr);
        }
    }

    /**
     * Ricerca prenotazioni per il bigliettaio con vari criteri.
     */
    private static void cercaPrenotazione() {
        System.out.println("\n--- CERCA PRENOTAZIONE ---");
        System.out.print("Codice prenotazione (Invio per saltare): ");
        String codice = sc.nextLine().trim();

        System.out.print("Nome cliente (Invio per saltare): ");
        String nome = sc.nextLine().trim();

        System.out.print("Titolo film (parziale, Invio per saltare): ");
        String titolo = sc.nextLine().trim();

        System.out.print("Data inizio (dd/MM/yyyy, Invio per saltare): ");
        String dataInizioStr = sc.nextLine().trim();

        System.out.print("Data fine (dd/MM/yyyy, Invio per saltare): ");
        String dataFineStr = sc.nextLine().trim();

        LocalDate di = null, df = null;
        try { if (!dataInizioStr.isEmpty()) di = LocalDate.parse(dataInizioStr, FMT_DATA); }
        catch (DateTimeParseException e) { System.out.println("Data inizio non valida, ignorata."); }
        try { if (!dataFineStr.isEmpty()) df = LocalDate.parse(dataFineStr, FMT_DATA); }
        catch (DateTimeParseException e) { System.out.println("Data fine non valida, ignorata."); }

        final LocalDate diF = di, dfF = df;

        List<Prenotazione> risultati = prenotazioni.stream()
                .filter(pr -> codice.isEmpty() || pr.getCodice().equals(codice))
                .filter(pr -> nome.isEmpty() || pr.getNomeCliente().toLowerCase().contains(nome.toLowerCase()))
                .filter(pr -> {
                    if (titolo.isEmpty()) return true;
                    Proiezione p = trovaProiezioneById(pr.getIdProiezione());
                    return p != null && p.getFilm().getTitolo().toLowerCase().contains(titolo.toLowerCase());
                })
                .filter(pr -> {
                    if (diF == null && dfF == null) return true;
                    Proiezione p = trovaProiezioneById(pr.getIdProiezione());
                    if (p == null) return false;
                    LocalDate dataPr = p.getDataOra().toLocalDate();
                    if (diF != null && dataPr.isBefore(diF)) return false;
                    if (dfF != null && dataPr.isAfter(dfF)) return false;
                    return true;
                })
                .collect(Collectors.toList());

        if (risultati.isEmpty()) { System.out.println("Nessuna prenotazione trovata."); return; }

        System.out.println("\nRisultati (" + risultati.size() + "):");
        for (Prenotazione pr : risultati) {
            stampaDettaglioPrenotazione(pr);
        }
    }

    // ===================== UTILITY =====================

    /**
     * Calcola il numero di posti liberi per una proiezione.
     *
     * @param p la proiezione
     * @return numero di posti ancora disponibili
     */
    private static long calcolaPostiLiberi(Proiezione p) {
        long prenotati = prenotazioni.stream()
                .filter(pr -> pr.getIdProiezione().equals(p.getId()))
                .mapToInt(Prenotazione::getNumeroPosti)
                .sum();
        return Proiezione.CAPACITA_SALA - prenotati;
    }

    /**
     * Cerca una proiezione per ID.
     *
     * @param id l'identificatore della proiezione
     * @return la proiezione trovata, o null se non esiste
     */
    private static Proiezione trovaProiezioneById(String id) {
        return proiezioni.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    /**
     * Stampa una lista di proiezioni in formato tabellare.
     *
     * @param lista la lista di proiezioni da stampare
     */
    private static void stampaListaProiezioni(List<Proiezione> lista) {
        if (lista.isEmpty()) { System.out.println("Nessuna proiezione."); return; }
        System.out.printf("%-18s %-30s %-15s %-18s %8s%n",
                "ID", "Titolo", "Genere", "Data/Ora", "Costo");
        System.out.println("-".repeat(95));
        for (Proiezione p : lista) {
            System.out.printf("%-18s %-30s %-15s %-18s %7.2f€%n",
                    p.getId(),
                    tronca(p.getFilm().getTitolo(), 29),
                    tronca(p.getFilm().getGenere(), 14),
                    p.getDataOraFormattata(),
                    p.getCostoBiglietto());
        }
    }

    /**
     * Stampa il dettaglio completo di una prenotazione.
     *
     * @param pr la prenotazione da stampare
     */
    private static void stampaDettaglioPrenotazione(Prenotazione pr) {
        Proiezione p = trovaProiezioneById(pr.getIdProiezione());
        System.out.println("----------------------------------");
        System.out.println("Codice:    " + pr.getCodice());
        System.out.println("Cliente:   " + pr.getNomeCliente());
        if (p != null) {
            System.out.println("Film:      " + p.getFilm().getTitolo());
            System.out.println("Data/Ora:  " + p.getDataOraFormattata());
            System.out.printf ("Biglietti: %d x € %.2f = € %.2f%n",
                    pr.getNumeroPosti(), p.getCostoBiglietto(),
                    pr.getNumeroPosti() * p.getCostoBiglietto());
        } else {
            System.out.println("(proiezione non trovata)");
            System.out.println("Biglietti: " + pr.getNumeroPosti());
        }
    }

    /**
     * Legge un intero da tastiera in un intervallo definito, con richiesta ripetuta in caso di errore.
     *
     * @param prompt messaggio da mostrare
     * @param min    valore minimo accettato
     * @param max    valore massimo accettato
     * @return il valore intero inserito
     */
    private static int leggiIntero(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int v = Integer.parseInt(sc.nextLine().trim());
                if (v >= min && v <= max) return v;
                System.out.println("Valore fuori range (" + min + "-" + max + ").");
            } catch (NumberFormatException e) {
                System.out.println("Inserire un numero intero.");
            }
        }
    }

    /**
     * Legge un numero decimale da tastiera, con richiesta ripetuta in caso di errore.
     *
     * @param prompt messaggio da mostrare
     * @param min    valore minimo accettato
     * @param max    valore massimo accettato
     * @return il valore double inserito
     */
    private static double leggiDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                double v = Double.parseDouble(sc.nextLine().trim().replace(",", "."));
                if (v >= min && v <= max) return v;
                System.out.println("Valore fuori range (" + min + "-" + max + ").");
            } catch (NumberFormatException e) {
                System.out.println("Inserire un numero valido.");
            }
        }
    }

    /**
     * Legge una data e ora da tastiera nel formato dd/MM/yyyy HH:mm.
     *
     * @return la data/ora inserita, o null in caso di annullamento
     */
    private static LocalDateTime leggiDataOra() {
        while (true) {
            System.out.print("Data e ora (dd/MM/yyyy HH:mm, Invio per annullare): ");
            String s = sc.nextLine().trim();
            if (s.isEmpty()) return null;
            try {
                return LocalDateTime.parse(s, FMT_DATAORA);
            } catch (DateTimeParseException e) {
                System.out.println("Formato non valido. Usare dd/MM/yyyy HH:mm (es. 20/06/2026 21:00).");
            }
        }
    }

    /**
     * Tronca una stringa alla lunghezza massima specificata aggiungendo "..." se necessario.
     *
     * @param s   la stringa da troncare
     * @param max lunghezza massima
     * @return la stringa troncata
     */
    private static String tronca(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 3) + "...";
    }
}
