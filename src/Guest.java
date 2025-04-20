import java.util.ArrayList;
import java.util.List;

public class Guest {
    private String guestId;
    private String name;
    private String contactNumber;
    private String email;
    private String address;
    private int loyaltyPoints;
    private List<String> stayHistory;
    
    public Guest(String guestId, String name, String contactNumber, String email, String address) {
        this.guestId = guestId;
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
        this.loyaltyPoints = 0;
        this.stayHistory = new ArrayList<>();
    }
    
    public String getGuestId() {
        return guestId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getContactNumber() {
        return contactNumber;
    }
    
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }
    
    public void addLoyaltyPoints(int points) {
        this.loyaltyPoints += points;
    }
    
    public List<String> getStayHistory() {
        return stayHistory;
    }
    
    public void addStayToHistory(String stayDetails) {
        stayHistory.add(stayDetails);
    }
    
    public String calculateLoyaltyStatus() {
        if (loyaltyPoints >= 1000) {
            return "GOLD";
        } else if (loyaltyPoints >= 500) {
            return "SILVER";
        } else if (loyaltyPoints >= 100) {
            return "BRONZE";
        } else {
            return "STANDARD";
        }
    }
}