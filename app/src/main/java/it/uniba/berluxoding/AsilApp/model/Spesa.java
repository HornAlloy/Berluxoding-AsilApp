package it.uniba.berluxoding.AsilApp.model;

import java.util.HashMap;
import java.util.Map;

import java.util.HashMap;
import java.util.Map;

/**
 * La classe {@code Spesa} rappresenta una spesa effettuata da un utente.
 * Contiene informazioni relative alla spesa, come la data, l'orario,
 * il costo, l'ambito e l'articolo associato.
 */
public class Spesa {
    private String id;        // L'identificativo unico della spesa
    private String data;      // La data della spesa
    private String orario;    // L'orario della spesa
    private String costo;     // Il costo della spesa
    private String ambito;    // L'ambito della spesa
    private String articolo;   // L'articolo acquistato

    /**
     * Costruttore di default necessario per le chiamate a DataSnapshot.getValue(Spesa.class).
     */
    public Spesa() {}

    /**
     * Imposta l'identificativo unico della spesa.
     *
     * @param id L'identificativo da impostare.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Restituisce l'identificativo unico della spesa.
     *
     * @return L'identificativo della spesa.
     */
    public String getId() {
        return id;
    }

    /**
     * Restituisce la data della spesa.
     *
     * @return La data della spesa.
     */
    public String getData() {
        return data;
    }

    /**
     * Imposta la data della spesa.
     *
     * @param data La data da impostare.
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Restituisce il costo della spesa.
     *
     * @return Il costo della spesa.
     */
    public String getCosto() {
        return costo;
    }

    /**
     * Imposta il costo della spesa.
     *
     * @param costo Il costo da impostare.
     */
    public void setCosto(String costo) {
        this.costo = costo;
    }

    /**
     * Restituisce l'ambito della spesa.
     *
     * @return L'ambito della spesa.
     */
    public String getAmbito() {
        return ambito;
    }

    /**
     * Imposta l'ambito della spesa.
     *
     * @param ambito L'ambito da impostare.
     */
    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    /**
     * Restituisce l'orario della spesa.
     *
     * @return L'orario della spesa.
     */
    public String getOrario() {
        return orario;
    }

    /**
     * Imposta l'orario della spesa.
     *
     * @param orario L'orario da impostare.
     */
    public void setOrario(String orario) {
        this.orario = orario;
    }

    /**
     * Restituisce l'articolo associato alla spesa.
     *
     * @return L'articolo della spesa.
     */
    public String getArticolo() {
        return articolo;
    }

    /**
     * Imposta l'articolo associato alla spesa.
     *
     * @param articolo L'articolo da impostare.
     */
    public void setArticolo(String articolo) {
        this.articolo = articolo;
    }

    /**
     * Converte l'oggetto Spesa in una mappa di chiavi e valori.
     * Questa mappa può essere utilizzata per interagire con il database.
     *
     * @return Una mappa contenente i dati della spesa.
     */
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
