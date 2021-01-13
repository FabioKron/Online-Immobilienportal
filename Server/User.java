import java.util.ArrayList;

/**
 * Die Klasse speichert Informationen zu dem Benutzer und
 * stellt Funktionalitäten bereit, die auf den jeweiligen Benutzer angepasst sind.
 *
 * @author Fabio Kron.
 * @version 1.2021.
 */
public class User
{
    /** Name des Benutzers. */
    private String name;
    
    /** E-Mail des Benutzers. */
    private String eMail;
    
    /** Verschlüsseltes Passwort. */
    private String password;
    
    
    /** Alle Immobilien im Besitz des Benutzers. */
    private ArrayList<RealEstate> ownedRealEstates = new ArrayList<RealEstate>();
    
    /**
     * Initialisieren des Benutzers mit Name, E-Mail und Passwort.
     * 
     * @param name String : Name des Benutzers.
     * @param eMail String : E-Mail des Benutzers.
     * @param password String : Verschlüsseltes Passwort des Benutzers.
     */
    public User(String name, String eMail, String password) {
        this.name = name;
        this.eMail = eMail;
        this.password = password;
        
        System.out.println("Neuer Benutzer hinzugefügt: " + this.name);
    }
    
    /**
     * Es wird überprüft, ob die Argumente mit der E-Mail und dem Passwort des Nutzers 
     * übereinstimmen.
     * 
     * @param eMail String - E-Mail,die mit der des Benutzers überprüft werden soll.
     * @param password String - verschlüsseltes Passwort, das mit dem des Benutzers
     * überprüft werden soll.
     * 
     * @return isMatching boolean - true falls die Daten übereinstimmen, ansonsten false.
     */
    public boolean matchesSignInData(String eMail, String password) {
        boolean isMatching = this.eMail.equals(eMail) && this.password.equals(password);
        return isMatching;
    }
    
    /**
     * Die Methode gibt den Namen des Benutzers zurück.
     * 
     * return name String - Name des Benutzers.
     */
    public String getName() {
        return name;
    }
}
