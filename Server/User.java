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
}
