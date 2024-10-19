package it.uniba.berluxoding.AsilApp.model;

public class Utente {
    private String nome;
    private String cognome;
    private String dataNascita;
    private String luogoProvenienza;
    private String pin;

    public Utente () {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public void setNome (String nome) {
        this.nome = nome;
    }
    public void setCognome (String cognome) {
        this.cognome = cognome;
    }
    public void setDataNascita (String dataNascita) {
        this.dataNascita = dataNascita;
    }
    public void setLuogoProvenienza(String luogo) {
        this.luogoProvenienza = luogo;
    }
    public void setPin (String pin) {
        this.pin = pin;
    }

    public String getNome() {
        return nome;
    }
    public String getCognome() {
        return cognome;
    }
    public String getDataNascita() {
        return dataNascita;
    }
    public String getLuogoProvenienza () {
        return luogoProvenienza;
    }
    public String getPin() {
        return pin;
    }


}
