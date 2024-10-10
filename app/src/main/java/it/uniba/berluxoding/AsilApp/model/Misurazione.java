package it.uniba.berluxoding.AsilApp.model;

public class Misurazione {
    private String id;
    private String strumento;
    private String valore;
    private String data;
    private String orario;


    public Misurazione () {
    }

    public String getValore () {
        return valore;
    }

    public void setValore (String valore) {
        this.valore = valore;
    }

    public String getStrumento () {
        return strumento;
    }

    public void setStrumento (String strumento) {
        this.strumento = strumento;
    }

    public String getData () {
        return data;
    }

    public void setData (String data) {
        this.data = data;
    }

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getOrario () {
        return orario;
    }

    public void setOrario (String orario) {
        this.orario = orario;
    }
}
