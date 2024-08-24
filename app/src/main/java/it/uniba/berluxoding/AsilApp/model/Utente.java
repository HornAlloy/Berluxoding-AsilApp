package it.uniba.berluxoding.AsilApp.model;

public class Utente {
    private String nome;
    private String cognome;
    private String dataNascita;
    private String luogoNascita;
    private String pin;
    private String username;
    private String email;

    public Utente () {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Utente (String username, String email) {
        this.username = username;
        this.email = email;
    }


}
