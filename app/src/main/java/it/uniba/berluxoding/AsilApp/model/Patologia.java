package it.uniba.berluxoding.AsilApp.model;

public class Patologia {
    private String id;
    private String nome;
    private String dataDiagnosi;

    public Patologia () {

    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
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
}
