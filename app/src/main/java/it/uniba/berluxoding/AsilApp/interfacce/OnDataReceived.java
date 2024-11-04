package it.uniba.berluxoding.AsilApp.interfacce;

/**
 * Interfaccia per la gestione dei dati ricevuti.
 * @param <T> Il tipo di dati da gestire quando vengono ricevuti.
 */
public interface OnDataReceived<T> {

    /**
     * Metodo di callback chiamato quando i dati vengono ricevuti.
     *
     * @param data I dati ricevuti.
     */
    void onDataReceived(T data);
}
