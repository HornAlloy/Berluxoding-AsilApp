package it.uniba.berluxoding.AsilApp.model;

import androidx.annotation.NonNull;

public class Valutazione {
    private float rating;
    private String comment;

    /**
     * Default constructor required for calls to DataSnapshot.getValue(User.class)
     */
    public Valutazione () {
    }

    public float getRating () {
        return rating;
    }

    public void setRating (float rating) {
        this.rating = rating;
    }

    public String getComment () {
        return comment;
    }

    public void setComment (String comment) {
        this.comment = comment;
    }

    @NonNull
    @Override
    public String toString() {
        return "Valutazione: [Stelle = " + rating + "], [Commento = " + comment + "]";
    }
}
