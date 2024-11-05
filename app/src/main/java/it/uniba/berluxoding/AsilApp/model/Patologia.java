package it.uniba.berluxoding.AsilApp.model;



/**
 * La classe {@code Patologia} rappresenta una patologia diagnosticata a un paziente.
 * Contiene informazioni come il nome della patologia, la data della diagnosi e il medico
 * che ha effettuato la diagnosi.
 */
public class Patologia {
    /**
     * Il nome della patologia.
     */
    private String nome;

    /**
     * La data in cui è stata effettuata la diagnosi.
     */
    private String dataDiagnosi;

    /**
     * Il medico che ha diagnosticato la patologia.
     */
    private String diagnosta;

    /**
     * Costruttore di default necessario per le chiamate a DataSnapshot.getValue(Patologia.class).
     */
    public Patologia() {
    }

    /**
     * Restituisce la data della diagnosi della patologia.
     *
     * @return La data della diagnosi.
     */
    public String getDataDiagnosi() {
        return dataDiagnosi;
    }

    /**
     * Imposta la data della diagnosi della patologia.
     *
     * @param dataDiagnosi La data da impostare per la diagnosi.
     */
    public void setDataDiagnosi(String dataDiagnosi) {
        this.dataDiagnosi = dataDiagnosi;
    }

    /**
     * Restituisce il nome della patologia.
     *
     * @return Il nome della patologia.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome della patologia.
     *
     * @param nome Il nome da impostare per la patologia.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il nome del medico che ha diagnosticato la patologia.
     *
     * @return Il nome del medico.
     */
    public String getDiagnosta() {
        return diagnosta;
    }

    /**
     * Imposta il nome del medico che ha diagnosticato la patologia.
     *
     * @param diagnosta Il nome del medico da impostare.
     */
    public void setDiagnosta(String diagnosta) {
        this.diagnosta = diagnosta;
    }

}
