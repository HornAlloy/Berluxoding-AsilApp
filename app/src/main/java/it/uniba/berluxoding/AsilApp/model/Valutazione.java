package it.uniba.berluxoding.AsilApp.model;

import androidx.annotation.NonNull;

/**
 * La classe {@code Valutazione} rappresenta una valutazione che include un punteggio
 * e un commento. Utilizzata per fornire feedback su un determinato oggetto o servizio.
 */
public class Valutazione {
    private float rating;        // Punteggio della valutazione, espresso in stelle
    private String comment;      // Commento dell'utente relativo alla valutazione

    /**
     * Costruttore di default necessario per le chiamate a DataSnapshot.getValue(Valutazione.class).
     */
    public Valutazione() {
    }

    /**
     * Restituisce il punteggio della valutazione.
     *
     * @return Il punteggio della valutazione, espresso come numero decimale.
     */
    public float getRating() {
        return rating;
    }

    /**
     * Imposta il punteggio della valutazione.
     *
     * @param rating Il punteggio da impostare, espresso come numero decimale.
     */
    public void setRating(float rating) {
        this.rating = rating;
    }

    /**
     * Restituisce il commento associato alla valutazione.
     *
     * @return Il commento dell'utente.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Imposta il commento associato alla valutazione.
     *
     * @param comment Il commento da impostare.
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Restituisce una rappresentazione in stringa della valutazione.
     *
     * @return Una stringa che rappresenta il punteggio e il commento della valutazione.
     */
    @NonNull
    @Override
    public String toString() {
        return "Valutazione: [Stelle = " + rating + "], [Commento = " + comment + "]";
    }
}
