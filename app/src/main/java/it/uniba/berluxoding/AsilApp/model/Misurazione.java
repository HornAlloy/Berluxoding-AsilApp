package it.uniba.berluxoding.AsilApp.model;

public class Misurazione {
    private String id;
    private String strumento;
    private String valore;
    private String data;
    private String orario;


    /**
     * Default constructor required for calls to DataSnapshot.getValue(User.class)
     */
    public Misurazione () {

    }

    /**
     * Necessario per il funzionamento del database
     */
    public String getValore () {
        return valore;
    }

    /**
     * Necessario per il funzionamento del database
     */
    public void setValore (String valore) {
        this.valore = valore;
    }

    /**
     * Necessario per il funzionamento del database
     */
    public String getStrumento () {
        return strumento;
    }

    /**
     * Necessario per il funzionamento del database
     */
    public void setStrumento (String strumento) {
        this.strumento = strumento;
    }

    /**
     * Necessario per il funzionamento del database
     */
    public String getData () {
        return data;
    }

    /**
     * Necessario per il funzionamento del database
     */
    public void setData (String data) {
        this.data = data;
    }

    /**
     * Necessario per il funzionamento del database
     */
    public String getId () {
        return id;
    }

    /**
     * Necessario per il funzionamento del database
     */
    public void setId (String id) {
        this.id = id;
    }

    /**
     * Necessario per il funzionamento del database
     */
    public String getOrario () {
        return orario;
    }

    /**
     * Necessario per il funzionamento del database
     */
    public void setOrario (String orario) {
        this.orario = orario;
    }
}
