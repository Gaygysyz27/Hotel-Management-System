import java.time.LocalDate;

enum BookingStatus {
    CONFIRMED,
    CHECKED_IN,
    CHECKED_OUT,
    CANCELLED
}

public class Booking {
    private String bookingId;
    private String guestId;
    private int roomNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private BookingStatus status;
    private int numberOfGuests;
    private String specialRequests;
    
    public Booking(String bookingId, String guestId, int roomNumber, LocalDate checkInDate, 
                  LocalDate checkOutDate, BookingStatus status, int numberOfGuests) {
        this.bookingId = bookingId;
        this.guestId = guestId;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
        this.numberOfGuests = numberOfGuests;
        this.specialRequests = "";
    }
    
    public String getBookingId() {
        return bookingId;
    }
    
    public String getGuestId() {
        return guestId;
    }
    
    public int getRoomNumber() {
        return roomNumber;
    }
    
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    public LocalDate getCheckInDate() {
        return checkInDate;
    }
    
    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }
    
    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }
    
    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }
    
    public BookingStatus getStatus() {
        return status;
    }
    
    public void setStatus(BookingStatus status) {
        this.status = status;
    }
    
    public int getNumberOfGuests() {
        return numberOfGuests;
    }
    
    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }
    
    public String getSpecialRequests() {
        return specialRequests;
    }
    
    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }
    
    public int calculateDuration() {
        return (int) java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }
    
    public void applyDiscount(double discountPercentage) {
        // This would be used in conjunction with a pricing system
        System.out.println("Applied discount of " + discountPercentage + "% to booking " + bookingId);
    }
}