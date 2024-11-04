package it.uniba.berluxoding.AsilApp.model;

/**
 * La classe {@code Misurazione} rappresenta una misurazione effettuata con uno strumento
 * specifico. Contiene informazioni come il valore della misurazione, la data e l'orario
 * in cui è stata effettuata, e l'identificatore univoco della misurazione.
 */
public class Misurazione {
    private String id;
    private String strumento;
    private String valore;
    private String data;
    private String orario;

    /**
     * Costruttore di default necessario per le chiamate a DataSnapshot.getValue(Misurazione.class).
     */
    public Misurazione() {
    }

    /**
     * Restituisce il valore della misurazione.
     *
     * @return Il valore della misurazione.
     */
    public String getValore() {
        return valore;
    }

    /**
     * Imposta il valore della misurazione.
     *
     * @param valore Il valore da impostare per la misurazione.
     */
    public void setValore(String valore) {
        this.valore = valore;
    }

    /**
     * Restituisce il nome dello strumento utilizzato per la misurazione.
     *
     * @return Il nome dello strumento.
     */
    public String getStrumento() {
        return strumento;
    }

    /**
     * Imposta il nome dello strumento utilizzato per la misurazione.
     *
     * @param strumento Il nome dello strumento da impostare.
     */
    public void setStrumento(String strumento) {
        this.strumento = strumento;
    }

    /**
     * Restituisce la data in cui è stata effettuata la misurazione.
     *
     * @return La data della misurazione.
     */
    public String getData() {
        return data;
    }

    /**
     * Imposta la data in cui è stata effettuata la misurazione.
     *
     * @param data La data da impostare.
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Restituisce l'identificatore univoco della misurazione.
     *
     * @return L'ID della misurazione.
     */
    public String getId() {
        return id;
    }

    /**
     * Imposta l'identificatore univoco della misurazione.
     *
     * @param id L'ID da impostare per la misurazione.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Restituisce l'orario in cui è stata effettuata la misurazione.
     *
     * @return L'orario della misurazione.
     */
    public String getOrario() {
        return orario;
    }

    /**
     * Imposta l'orario in cui è stata effettuata la misurazione.
     *
     * @param orario L'orario da impostare.
     */
    public void setOrario(String orario) {
        this.orario = orario;
    }
}

