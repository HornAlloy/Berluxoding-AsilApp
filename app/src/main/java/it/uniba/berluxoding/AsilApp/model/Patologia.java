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
     * Costruttore necessario per poter leggere i dati dal Database Realtime Firebase
     */
    public Patologia () {

    }

    public String getDataDiagnosi () {
        return dataDiagnosi;
    }

    public void setDataDiagnosi (String dataDiagnosi) {
        this.dataDiagnosi = dataDiagnosi;
    }

    public String getNome () {
        return nome;
    }

    public void setNome (String nome) {
        this.nome = nome;
    }

    public String getDiagnosta () {
        return diagnosta;
    }

    public void setDiagnosta (String diagnosta) {
        this.diagnosta = diagnosta;
    }
}
