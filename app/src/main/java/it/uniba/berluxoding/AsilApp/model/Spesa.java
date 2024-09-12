package it.uniba.berluxoding.AsilApp.model;

import java.util.HashMap;
import java.util.Map;

public class Spesa {
    private String id;
    private String data;
    private String orario;
    private String costo;
    private String ambito;
    private String articolo;

    public Spesa() {}

    public void setId (String id) {
        this.id = id;
    }

    public String getId () {
        return id;
    }

    public String getData () {
        return data;
    }

    public void setData (String data) {
        this.data = data;
    }

    public String getCosto () {
        return costo;
    }

    public void setCosto (String costo) {
        this.costo = costo;
    }

    public String getAmbito () {
        return ambito;
    }

    public void setAmbito (String ambito) {
        this.ambito = ambito;
    }

    public String getOrario () {
        return orario;
    }

    public void setOrario (String orario) {
        this.orario = orario;
    }

    public String getArticolo () {
        return articolo;
    }

    public void setArticolo (String articolo) {
        this.articolo = articolo;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("ambito", ambito);
        result.put("articolo", articolo);
        result.put("costo", costo);
        result.put("data", data);
        result.put("orario", orario);

        return result;
    }
}
