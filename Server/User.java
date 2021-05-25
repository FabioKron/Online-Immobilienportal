import java.util.ArrayList;

/**
 * Die Klasse speichert Informationen zu dem Benutzer und
 * stellt Funktionalitäten bereit, die auf den jeweiligen Benutzer angepasst sind
 *
 * @author Fabio Kron
 * @version 1.2021
 */
public class User
{
    /** Name des Benutzers */
    private String name;
    
    /** E-Mail des Benutzers */
    private String eMail;
    
    /** Verschlüsseltes Passwort */
    private String password;
    
    
    /** Alle Immobilien im Besitz des Benutzers */
    private ArrayList<RealEstate> ownedRealEstates = new ArrayList<RealEstate>();
    
    /**
     * Initialisieren des Benutzers mit Name, E-Mail und Passwort.
     * 
     * @param name String - Name des Benutzers
     * @param eMail String - E-Mail des Benutzers
     * @param password String - Verschlüsseltes Passwort des Benutzers
     */
    User(String name, String eMail, String password) {
        this.name = name;
        this.eMail = eMail;
        this.password = password;
        }
    
    /**
     * Die Methode löscht die Immobilie mit dem angegebenen Index beim
     * Nutzer und aus dem DataCenter.
     * 
     * @param indexOfRealEstate int - Index der Immobilie, die gelöscht werden soll
     */
    void removeRealEstate(int indexOfRealEstate) {
        RealEstate toRemove = ownedRealEstates.get(indexOfRealEstate);
        ownedRealEstates.remove(toRemove);
        DataCenter.removeRealEstate(toRemove);
    }
    
    /**
     * Die Methode gibt Informationen zu allen Immobilien des Besitzers zurück.
     * 
     * @return realEstatesInformation String[] enthält Informationen zu allen Immobilien
     */
    String[] getRealEstatesInformation() {
        String[] realEstatesInformation = new String[ownedRealEstates.size()];
        for (int i = 0; i < ownedRealEstates.size(); i++) {
            realEstatesInformation[i] = ownedRealEstates.get(i).getInfo();
        }
        
        return realEstatesInformation;
    }
    
    /**
     * Die übergebene Immobilie wird beim Benutzer gespeichert.
     * 
     * @param newRealEstate RealEstate - die Immobilie, die gespeichert werden soll
     */
    void addRealEstate(RealEstate newRealEstate) {
        ownedRealEstates.add(newRealEstate);
    }
    
    /**
     * Es wird überprüft, ob die Argumente mit der E-Mail und dem Passwort des Nutzers 
     * übereinstimmen.
     * 
     * @param eMail String - E-Mail,die mit der des Benutzers überprüft werden soll
     * @param password String - verschlüsseltes Passwort, das mit dem des Benutzers
     * überprüft werden soll
     * 
     * @return isMatching boolean - true falls die Daten übereinstimmen, ansonsten false
     */
    boolean matchesSignInData(String eMail, String password) {
        boolean isMatching = this.eMail.equals(eMail) && this.password.equals(password);
        return isMatching;
    }
    
    /**
     * Die Methode gibt den Namen des Benutzers zurück.
     * 
     * @return name String - Name des Benutzers
     */
    String getName() {
        return name;
    }
    
    /**
     * Die E-Mail des Benutzers wird zurückgegeben.
     * 
     * @return eMail String - E-Mail des Benutzers
     */
    String getEMail() {
        return eMail;
    }
}
