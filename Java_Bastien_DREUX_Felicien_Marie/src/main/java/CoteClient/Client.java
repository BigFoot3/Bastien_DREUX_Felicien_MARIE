package CoteClient;

import CoteServer.Lecteur;
import CoteServer.Livre;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String SERVER_IP = "127.0.0.1"; // Adresse IP du serveur
    private static final int PORT = 1234; // Port sur lequel le serveur écoute

    public static void main(String[] args) {

        try (Socket socket = new Socket(SERVER_IP, PORT);
             ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {

            BufferedOutputStream bos = new BufferedOutputStream(outputStream, 201);
            BufferedInputStream bis = new BufferedInputStream(inputStream, 201);

            System.out.println("Connexion établie avec le serveur");

            Scanner scan = new Scanner(System.in); // permet la saisie de l'utilisateur

            boolean continuedemarcher = true;

            while (continuedemarcher) {
                System.out.println("1. Envoyer un livre");
                System.out.println("2. Envoyer un lecteur");
                System.out.println("3. Se déconnecter");
                System.out.print("Choix: ");
                String choice = scan.nextLine();

                // Utilisation d'un switch pour traiter le choix de l'utilisateur.
                switch (choice) {

                    //Création d'un livre suivant les détails de la classe
                    case "1":
                        Livre livre = new Livre();
                        System.out.print("Titre: ");
                        String titre = scan.nextLine();
                        livre.setTitre(titre);
                        System.out.print("Auteur: ");
                        String auteur = scan.nextLine();
                        livre.setAuteur(auteur);
                        outputStream.writeObject(livre);
                        outputStream.flush();
                        break;

                    //Création d'un lecteur suivant les détails de la classe
                    case "2":
                        Lecteur lecteur = new Lecteur();
                        System.out.print("Nom: ");
                        String nom = scan.nextLine();
                        lecteur.setNom(nom);
                        System.out.print("Age: ");
                        int age = Integer.parseInt(scan.nextLine());
                        scan.nextLine();
                        lecteur.setAge(age);
                        outputStream.writeObject(lecteur);
                        outputStream.flush();
                        break;

                    //Le choix 3 permet de se déconnecter
                    case "3":
                        outputStream.writeObject("deconnexion");
                        // Fermeture des ressources
                        socket.close();
                        inputStream.close();
                        outputStream.close();
                        outputStream.flush();
                        continuedemarcher = false;
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
