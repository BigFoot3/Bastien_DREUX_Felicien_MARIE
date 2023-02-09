package CoteServer;

import java.io.Serializable;

public class Lecteur implements Serializable { // l'implement Serializable sur la class sert à transformer en une forme qui peux etre stocker
    private String nom;
    private int age;

    public Lecteur() {
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
