import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Diese Klasse speichert alle Benutzer und alle Immobilien.
 *
 * @author Fabio Kron.
 * @version 1.2021.
 */
public class DataCenter
{   
    /** Speichert Referenzen zu allen Benutzern. */
    private static ArrayList<User> users = new ArrayList<User>();
    
    /** Speichert Referenzen zu allen Immobilien. */
    private static LinkedList<RealEstate> realEstates = new LinkedList<RealEstate>();
    
    /**
     * Es wird überprüft, ob die E-Mail und das Passwort mit einem gespeicherten
     * Nutzer übereinstimmen; falls das der Fall ist, wird dieser Nutzer zurückgegeben.
     * 
     * @param eMail String - E-Mail des Nutzers der authentifiziert werden soll.
     * @param passwort String - verschlüsseltes Passwort des Nutzers der authentifiziert
     * werden soll.
     * 
     * @return user User - falls die Anmeldedaten mit einem Nutzer übereinstimmen;
     * ansonsten null.
     */
    public static User authenticateUser(String eMail, String password) {
        for (User user: users) {
            if (user.matchesSignInData(eMail, password)) {
                return user;
            }
        }
        
        return null;
    }
    
    
    /**
     * Die Methode speichert neue Benutzer.
     * 
     * @param newUser User: der neu erstellte Benutzer.
     */
    public static void addNewUser(User newUser) {
        users.add(newUser);
    }
}