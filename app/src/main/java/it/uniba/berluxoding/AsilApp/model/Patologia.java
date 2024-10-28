package it.uniba.berluxoding.AsilApp.model;

public class Patologia {
    /**
     * La patologia si identifica con il nome
     */
    private String nome;
    private String dataDiagnosi;
    /**
     * Medico che ha diagnosticato la patologia.
     */
    private String diagnosta;

    /**
     * Default constructor required for calls to DataSnapshot.getValue(User.class)
     */
    public Patologia () {

    }

    /**
     * Necessario per il funzionamento del database
     */
    public String getDataDiagnosi () {
        return dataDiagnosi;
    }

    /**
     * Necessario per il funzionamento del database
     */
    public void setDataDiagnosi (String dataDiagnosi) {
        this.dataDiagnosi = dataDiagnosi;
    }

    /**
     * Necessario per il funzionamento del database
     */
    public String getNome () {
        return nome;
    }

    /**
     * Necessario per il funzionamento del database
     */
    public void setNome (String nome) {
        this.nome = nome;
    }

    /**
     * Necessario per il funzionamento del database
     */
    public String getDiagnosta () {
        return diagnosta;
    }

    /**
     * Necessario per il funzionamento del database
     */
    public void setDiagnosta (String diagnosta) {
        this.diagnosta = diagnosta;
    }
}