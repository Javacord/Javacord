package de.btobastian.javacord.api.listener;

public interface ReadyListener extends Listener {
    
    /**
     * Called when the connection is ready (= the ready packet was received)
     */
    public void onReady();
    
    /**
     * Called when the connection failed (e.g. wrong password and email).
     */
    public void onFail();

}
