package it.uniba.berluxoding.AsilApp.model;

public class Valutazione {
    private float rating;
    private String comment;

    public Valutazione () {
        // metodo necessario per far funzionare il db
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

    @Override
    public String toString() {
        return "Valutazione: [Stelle = " + rating + "], [Commento = " + comment + "]";
    }
}
