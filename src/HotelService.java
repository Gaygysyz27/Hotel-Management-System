import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class HotelService {
    private List<Room> rooms;
    private List<Guest> guests;
    private List<Booking> bookings;
    private List<Employee> employees;
    private List<Invoice> invoices;
    
    private AtomicInteger guestIdCounter;
    private AtomicInteger bookingIdCounter;
    private AtomicInteger invoiceIdCounter;
    private AtomicInteger employeeIdCounter;
    
    public HotelService() {
        rooms = new ArrayList<>();
        guests = new ArrayList<>();
        bookings = new ArrayList<>();
        employees = new ArrayList<>();
        invoices = new ArrayList<>();
        
        guestIdCounter = new AtomicInteger(3);  // Starting after test data
        bookingIdCounter = new AtomicInteger(2);
        invoiceIdCounter = new AtomicInteger(1);
        employeeIdCounter = new AtomicInteger(3);
    }
    
    // Room methods
    public void addRoom(Room room) {
        rooms.add(room);
    }
    
    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms);
    }
    
    public Room getRoomByNumber(int roomNumber) {
        return rooms.stream()
                .filter(room -> room.getRoomNumber() == roomNumber)
                .findFirst()
                .orElse(null);
    }
    
    public List<Room> getAvailableRooms() {
        return rooms.stream()
                .filter(room -> room.getStatus() == RoomStatus.AVAILABLE)
                .collect(Collectors.toList());
    }
    
    public void updateRoom(Room room) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getRoomNumber() == room.getRoomNumber()) {
                rooms.set(i, room);
                break;
            }
        }
    }
    
    // Guest methods
    public String generateGuestId() {
        return "G" + String.format("%03d", guestIdCounter.getAndIncrement());
    }
    
    public void addGuest(Guest guest) {
        guests.add(guest);
    }
    
    public List<Guest> getAllGuests() {
        return new ArrayList<>(guests);
    }
    
    public Guest getGuestById(String guestId) {
        return guests.stream()
                .filter(guest -> guest.getGuestId().equals(guestId))
                .findFirst()
                .orElse(null);
    }
    
    public List<Guest> searchGuestsByName(String name) {
        String searchName = name.toLowerCase();
        return guests.stream()
                .filter(guest -> guest.getName().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
    }
    
    public void updateGuest(Guest guest) {
        for (int i = 0; i < guests.size(); i++) {
            if (guests.get(i).getGuestId().equals(guest.getGuestId())) {
                guests.set(i, guest);
                break;
            }
        }
    }
    
    // Booking methods
    public String generateBookingId() {
        return "B" + String.format("%03d", bookingIdCounter.getAndIncrement());
    }
    
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }
    
    public List<Booking> getAllBookings() {
        return new ArrayList<>(bookings);
    }
    
    public Booking getBookingById(String bookingId) {
        return bookings.stream()
                .filter(booking -> booking.getBookingId().equals(bookingId))
                .findFirst()
                .orElse(null);
    }
    
    public List<Booking> getBookingsByGuestId(String guestId) {
        return bookings.stream()
                .filter(booking -> booking.getGuestId().equals(guestId))
                .collect(Collectors.toList());
    }
    
    public List<Booking> getBookingsByRoomNumber(int roomNumber) {
        return bookings.stream()
                .filter(booking -> booking.getRoomNumber() == roomNumber)
                .collect(Collectors.toList());
    }
    
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return bookings.stream()
                .filter(booking -> booking.getStatus() == status)
                .collect(Collectors.toList());
    }
    
    public void updateBooking(Booking booking) {
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getBookingId().equals(booking.getBookingId())) {
                bookings.set(i, booking);
                break;
            }
        }
    }
    
    // Employee methods
    public String generateEmployeeId() {
        return "E" + String.format("%03d", employeeIdCounter.getAndIncrement());
    }
    
    public void addEmployee(Employee employee) {
        employees.add(employee);
    }
    
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }
    
    public Employee getEmployeeById(String employeeId) {
        return employees.stream()
                .filter(employee -> employee.getEmployeeId().equals(employeeId))
                .findFirst()
                .orElse(null);
    }
    
    public void updateEmployee(Employee employee) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getEmployeeId().equals(employee.getEmployeeId())) {
                employees.set(i, employee);
                break;
            }
        }
    }
    
    // Invoice methods
    public String generateInvoiceId() {
        return "I" + String.format("%03d", invoiceIdCounter.getAndIncrement());
    }
    
    public void addInvoice(Invoice invoice) {
        invoices.add(invoice);
    }
    
    public List<Invoice> getAllInvoices() {
        return new ArrayList<>(invoices);
    }
    
    public Invoice getInvoiceById(String invoiceId) {
        return invoices.stream()
                .filter(invoice -> invoice.getInvoiceId().equals(invoiceId))
                .findFirst()
                .orElse(null);
    }
    
    public List<Invoice> getInvoicesByBookingId(String bookingId) {
        return invoices.stream()
                .filter(invoice -> invoice.getBookingId().equals(bookingId))
                .collect(Collectors.toList());
    }
    
    public void updateInvoice(Invoice invoice) {
        for (int i = 0; i < invoices.size(); i++) {
            if (invoices.get(i).getInvoiceId().equals(invoice.getInvoiceId())) {
                invoices.set(i, invoice);
                break;
            }
        }
    }
}