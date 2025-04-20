import java.util.HashSet;
import java.util.Set;

enum RoomType {
    STANDARD,
    DELUXE,
    SUITE,
    EXECUTIVE
}

enum RoomStatus {
    AVAILABLE,
    OCCUPIED,
    UNDER_MAINTENANCE,
    RESERVED
}

public class Room {
    private int roomNumber;
    private RoomType type;
    private double price;
    private RoomStatus status;
    private int maxOccupancy;
    private Set<String> amenities;
    
    public Room(int roomNumber, RoomType type, double price, RoomStatus status, int maxOccupancy) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.status = status;
        this.maxOccupancy = maxOccupancy;
        this.amenities = new HashSet<>();
    }
    
    public int getRoomNumber() {
        return roomNumber;
    }
    
    public RoomType getType() {
        return type;
    }
    
    public void setType(RoomType type) {
        this.type = type;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public RoomStatus getStatus() {
        return status;
    }
    
    public void setStatus(RoomStatus status) {
        this.status = status;
    }
    
    public int getMaxOccupancy() {
        return maxOccupancy;
    }
    
    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }
    
    public Set<String> getAmenities() {
        return amenities;
    }
    
    public void addAmenity(String amenity) {
        amenities.add(amenity);
    }
    
    public void removeAmenity(String amenity) {
        amenities.remove(amenity);
    }
    
    public void clearAmenities() {
        amenities.clear();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Room room = (Room) obj;
        return roomNumber == room.roomNumber;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(roomNumber);
    }
}