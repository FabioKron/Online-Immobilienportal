
/**
 * Diese Klasse speichert die Informationen zu der jeweiligen Immobilie.
 *
 * @author Fabio Kron.
 * @version 1/2021.
 */
public class RealEstate
{   
    /** Preis der Immobilie */
    private double price;
    /** Adresse der Immobilie */
    private String address;
    /** Anzahl der Räume der Immobilie */
    private int numOfRooms;
    /** Fläche der Immobilie */
    private int floorArea;
    /** Besitzer der Immobilie */
    private User owner;
    
    /**
     * Konstruktor von RealEstate; die Attribute werden initialisiert.
     * 
     * @param price double - Preis der Immobilie.
     * @param address String - Adresse der Immobilie.
     * @param numOfRooms int - Anzahl der Räume der Immobilie.
     * @param floorArea int - Fläche der Immobilie.
     * @param owner User - Besitzer der Immobilie.
     */
    RealEstate(double price, String address, int numOfRooms, int floorArea,
    User owner) {
        this.price = price;
        this.address = address;
        this.numOfRooms = numOfRooms;
        this.floorArea = floorArea;
        this.owner = owner;
    }
    
    /**
     * Die Methode gibt einen "Infostring" über die Immobilie zurück.
     * 
     * @return info String - Informationen zur Immobilie.
     */
    String getInfo() {
        String info = String.format("%s wird verkauft für $%,.2f!"
            + "\nDie Immobilie hat %d Räume und eine Fläche von %d qm."
            + "\nDie E-Mail des Besitzers ist: %s",
            address,
            price,
            numOfRooms,
            floorArea,
            owner.getEMail());
        return info;
    }
}
