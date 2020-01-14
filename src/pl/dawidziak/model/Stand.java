package pl.dawidziak.model;

public class Stand {
    private Client storedClient = null;

    public Stand() {

    }

    public Client getStoredClient() {
        return storedClient;
    }

    public void setStoredClient(Client storedClient) {
        this.storedClient = storedClient;
    }
}
