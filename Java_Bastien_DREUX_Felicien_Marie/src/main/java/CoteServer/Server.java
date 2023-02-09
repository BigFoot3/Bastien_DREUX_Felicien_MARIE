package CoteServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

public class Server {
    private static final int PORT = 1234; // Port sur lequel le serveur écoute
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/mabd"; // jdbc:posgresql://localhost:5432/mabd
    private static final String DB_UTILISATEUR = "root"; // Nom utilisateur
    private static final String DB_MOT_DE_PASSE = "root"; // Son mot de passe
    private static Connection dbConnection;

    public static void main(String[] args) {
        try {

            // Création du serveur
            ServerSocket monServerSocket = new ServerSocket(PORT);
            System.out.println("Serveur en écoute sur le port " + PORT);

            // Connexion à la base de donnée
            dbConnection = DriverManager.getConnection(DB_URL, DB_UTILISATEUR, DB_MOT_DE_PASSE);

            // Création object statement
            Statement stmt = dbConnection.createStatement();

            // Execution de la requête Livre
            String livresql = "CREATE TABLE IF NOT EXISTS livre " +
                    "(id SERIAL PRIMARY KEY, " +
                    "titre VARCHAR(255), " +
                    " auteur VARCHAR(255))";

            stmt.executeUpdate(livresql);

            System.out.println("Table livre créée avec succès");

            // Execution de la requête Lecteur
            String lecteursql = "CREATE TABLE IF NOT EXISTS lecteur " +
                    " (id SERIAL PRIMARY KEY, " +
                    " nom VARCHAR(255), " +
                    " age INTEGER)";

            stmt.executeUpdate(lecteursql);

            System.out.println("Table lecteur créée avec succès");

            boolean continuedemarcher = true;

            while (true) {

                // Attente d'une connexion entrante
                Socket clientSocket = monServerSocket.accept();
                System.out.println("Connexion établie avec le client " + clientSocket.getInetAddress());

                // Création des flux d'entrée et de sortie
                ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
                BufferedInputStream bis = new BufferedInputStream(inputStream, 201);
                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                BufferedOutputStream bos = new BufferedOutputStream(outputStream, 201);



                // Boucle de traitement des requêtes du client
                while (continuedemarcher) {

                    // Lecture de l'objet reçu depuis le client
                    Object obj = inputStream.readObject();

                    // Vérification du type de l'objet
                    if (obj instanceof Livre livre) {

                        // Insertion du livre dans la base de données
                        PreparedStatement statement = dbConnection.prepareStatement("INSERT INTO livre (titre, auteur) VALUES (?, ?)");
                        statement.setString(1, livre.getTitre());
                        statement.setString(2, livre.getAuteur());
                        statement.executeUpdate();
                    }

                    else if (obj instanceof Lecteur lecteur) {

                        // Insertion du lecteur dans la base de données
                        PreparedStatement statement = dbConnection.prepareStatement("INSERT INTO lecteur (nom, age) VALUES (?, ?)");
                        statement.setString(1, lecteur.getNom());
                        statement.setInt(2, lecteur.getAge());
                        statement.executeUpdate();
                    }

                    else if (obj instanceof String command) {
                        if (command.equals("deconnexion")) {

                            // Commande de déconnexion reçue, on ferme la connexion avec le client
                            inputStream.close();
                            outputStream.close();
                            clientSocket.close();
                            continuedemarcher = false;

                            break;
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }finally {
            // Fermeture des ressources
            try {
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
