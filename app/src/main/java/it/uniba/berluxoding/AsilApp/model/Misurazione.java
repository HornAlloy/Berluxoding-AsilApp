package it.uniba.berluxoding.AsilApp.model;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    private boolean dataItaliana = false;

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

    /**
     * Converte una data da un formato di input a un formato di output più leggibile.
     *
     * @param dateStr La data in formato di input "yyyy/MM/dd".
     */
    private void convertDateFormat(String dateStr) {
        // Definire il formato di input e output
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);

        String formattedDate = null;
        try {
            // Parsing della data in formato yyyy/MM/dd
            Date date = inputFormat.parse(dateStr);
            Log.d("CONVERT_DATE_FORMAT", "Data input = " + dateStr);
            Log.d("CONVERT_DATE_FORMAT", "Data = " + date);
            // Formattazione della data in formato dd/MM/yyyy
            formattedDate = outputFormat.format(date);
            Log.d("CONVERT_DATE_FORMAT", "Data formattata = " + formattedDate);
            Log.d("CONVERT_DATE_FORMAT", "Data = " + date);
        } catch (ParseException e) {
            Log.e("CONVERSIONE_DATA", "Fallita la modifica della data nel formato dd/MM/yyyy");
        }

        data = formattedDate;
    }

    /**
     * Verifica che la data sia nel formato {@code dd/MM/yyyy} altrimenti chiama il metodo convertDateFormat
     */
    public void checkDate() {
        if (!dataItaliana) {
            convertDateFormat(data);
            dataItaliana = true;
        }
    }
}