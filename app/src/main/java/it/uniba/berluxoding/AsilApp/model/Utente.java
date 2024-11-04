package it.uniba.berluxoding.AsilApp.model;

/**
 * La classe {@code Utente} rappresenta un utente del sistema.
 * Contiene informazioni personali come nome, cognome, data di nascita,
 * luogo di provenienza e un PIN per l'autenticazione.
 */
public class Utente {
    private String nome;                // Il nome dell'utente
    private String cognome;             // Il cognome dell'utente
    private String dataNascita;         // La data di nascita dell'utente
    private String luogoProvenienza;     // Il luogo di provenienza dell'utente
    private String pin;                 // Il PIN dell'utente per l'autenticazione

    /**
     * Costruttore di default necessario per le chiamate a DataSnapshot.getValue(Utente.class).
     */
    public Utente() {
    }

    /**
     * Imposta il nome dell'utente.
     *
     * @param nome Il nome da impostare.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Imposta il cognome dell'utente.
     *
     * @param cognome Il cognome da impostare.
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Imposta la data di nascita dell'utente.
     *
     * @param dataNascita La data di nascita da impostare.
     */
    public void setDataNascita(String dataNascita) {
        this.dataNascita = dataNascita;
    }

    /**
     * Imposta il luogo di provenienza dell'utente.
     *
     * @param luogo Il luogo di provenienza da impostare.
     */
    public void setLuogoProvenienza(String luogo) {
        this.luogoProvenienza = luogo;
    }

    /**
     * Imposta il PIN dell'utente per l'autenticazione.
     *
     * @param pin Il PIN da impostare.
     */
    public void setPin(String pin) {
        this.pin = pin;
    }

    /**
     * Restituisce il nome dell'utente.
     *
     * @return Il nome dell'utente.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Restituisce il cognome dell'utente.
     *
     * @return Il cognome dell'utente.
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Restituisce la data di nascita dell'utente.
     *
     * @return La data di nascita dell'utente.
     */
    public String getDataNascita() {
        return dataNascita;
    }

    /**
     * Restituisce il luogo di provenienza dell'utente.
     *
     * @return Il luogo di provenienza dell'utente.
     */
    public String getLuogoProvenienza() {
        return luogoProvenienza;
    }

    /**
     * Restituisce il PIN dell'utente per l'autenticazione.
     *
     * @return Il PIN dell'utente.
     */
    public String getPin() {
        return pin;
    }
}
