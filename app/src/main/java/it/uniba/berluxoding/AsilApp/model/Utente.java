package it.uniba.berluxoding.AsilApp.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utente {
    private String nome;
    private String cognome;
    private String dataNascita;
    private String luogoNascita;
    private String pin;
    private String username;
    private String email;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public Utente () {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Utente (String username, String email) {
        this.username = username;
        this.email = email;
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
    public void setDateNascita (Date dataNascita) {
        setDataNascita (sdf.format(dataNascita));
    }
    public void setLuogoNascita (String luogoNascita) {
        this.luogoNascita = luogoNascita;
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
    public String getLuogoNascita() {
        return luogoNascita;
    }


}
