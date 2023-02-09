package CoteServer;
import java.io.Serializable;

public class Livre implements Serializable { // l'implement Serializable sur la class sert à transformer en une forme qui peux etre stocker
    private String titre;
    private String auteur;

    public Livre() {
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }
}
