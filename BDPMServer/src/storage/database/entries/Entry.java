package storage.database.entries;

/**
 * Objets répresentant une ligne d'une Table de la BDD.
 * une classe Entry par Table dans la BDD
 */
public abstract class Entry {
    private int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
