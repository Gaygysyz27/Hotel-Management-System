import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class main {
    private static Scanner scanner = new Scanner(System.in);
    private static HotelService hotelService = new HotelService();
    private static AuditService auditService = new AuditService();
    
    public static void main(String[] args) {
        initializeTestData();
        
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    roomManagementMenu();
                    break;
                case 2:
                    guestManagementMenu();
                    break;
                case 3:
                    bookingManagementMenu();
                    break;
                case 4:
                    checkInOutMenu();
                    break;
                case 5:
                    billingMenu();
                    break;
                case 6:
                    reportMenu();
                    break;
                case 7:
                    employeeManagementMenu();
                    break;
                case 0:
                    running = false;
                    System.out.println("Thank you for using Hotel Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void displayMainMenu() {
        System.out.println("\n===== HOTEL MANAGEMENT SYSTEM =====");
        System.out.println("1. Room Management");
        System.out.println("2. Guest Management");
        System.out.println("3. Booking Management");
        System.out.println("4. Check-In/Check-Out");
        System.out.println("5. Billing");
        System.out.println("6. Reports");
        System.out.println("7. Employee Management");
        System.out.println("0. Exit");
        System.out.println("===================================");
    }
    
    // Room Management Menu
    private static void roomManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== ROOM MANAGEMENT =====");
            System.out.println("1. Add New Room");
            System.out.println("2. View All Rooms");
            System.out.println("3. Update Room Details");
            System.out.println("4. Change Room Status");
            System.out.println("0. Back to Main Menu");
            System.out.println("==========================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addNewRoom();
                    break;
                case 2:
                    viewAllRooms();
                    break;
                case 3:
                    updateRoomDetails();
                    break;
                case 4:
                    changeRoomStatus();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void addNewRoom() {
        System.out.println("\n----- Add New Room -----");
        int roomNumber = getIntInput("Enter room number: ");
        
        // Check if room already exists
        if (hotelService.getRoomByNumber(roomNumber) != null) {
            System.out.println("Room with this number already exists!");
            return;
        }
        
        System.out.println("Available room types:");
        System.out.println("1. STANDARD");
        System.out.println("2. DELUXE");
        System.out.println("3. SUITE");
        System.out.println("4. EXECUTIVE");
        
        int typeChoice = getIntInput("Select room type (1-4): ");
        RoomType roomType;
        
        switch (typeChoice) {
            case 1: roomType = RoomType.STANDARD; break;
            case 2: roomType = RoomType.DELUXE; break;
            case 3: roomType = RoomType.SUITE; break;
            case 4: roomType = RoomType.EXECUTIVE; break;
            default:
                System.out.println("Invalid choice. Setting to STANDARD by default.");
                roomType = RoomType.STANDARD;
        }
        
        double price = getDoubleInput("Enter room price per night: ");
        int maxOccupancy = getIntInput("Enter maximum occupancy: ");
        
        Room newRoom = new Room(roomNumber, roomType, price, RoomStatus.AVAILABLE, maxOccupancy);
        
        System.out.println("Enter amenities (comma separated): ");
        String amenitiesInput = scanner.nextLine();
        String[] amenities = amenitiesInput.split(",");
        
        for (String amenity : amenities) {
            if (!amenity.trim().isEmpty()) {
                newRoom.addAmenity(amenity.trim());
            }
        }
        
        hotelService.addRoom(newRoom);
        auditService.logAction("Added new room: " + roomNumber);
        System.out.println("Room added successfully!");
    }
    
    private static void viewAllRooms() {
        System.out.println("\n----- All Rooms -----");
        List<Room> rooms = hotelService.getAllRooms();
        
        if (rooms.isEmpty()) {
            System.out.println("No rooms found in the system.");
            return;
        }
        
        System.out.printf("%-10s %-15s %-10s %-15s %-10s %-30s\n", 
                "Room No.", "Type", "Price", "Status", "Max Occ.", "Amenities");
        System.out.println("---------------------------------------------------------------------------------");
        
        for (Room room : rooms) {
            System.out.printf("%-10d %-15s $%-9.2f %-15s %-10d %-30s\n", 
                    room.getRoomNumber(), 
                    room.getType(), 
                    room.getPrice(), 
                    room.getStatus(),
                    room.getMaxOccupancy(),
                    String.join(", ", room.getAmenities()));
        }
    }
    
    private static void updateRoomDetails() {
        System.out.println("\n----- Update Room Details -----");
        int roomNumber = getIntInput("Enter room number to update: ");
        
        Room room = hotelService.getRoomByNumber(roomNumber);
        if (room == null) {
            System.out.println("Room not found!");
            return;
        }
        
        System.out.println("Current room details:");
        System.out.printf("Room Number: %d\nType: %s\nPrice: $%.2f\nStatus: %s\nMax Occupancy: %d\nAmenities: %s\n",
                room.getRoomNumber(), room.getType(), room.getPrice(), room.getStatus(), 
                room.getMaxOccupancy(), String.join(", ", room.getAmenities()));
        
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Room Type");
        System.out.println("2. Price");
        System.out.println("3. Max Occupancy");
        System.out.println("4. Amenities");
        System.out.println("0. Cancel");
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                updateRoomType(room);
                break;
            case 2:
                updateRoomPrice(room);
                break;
            case 3:
                updateRoomOccupancy(room);
                break;
            case 4:
                updateRoomAmenities(room);
                break;
            case 0:
                System.out.println("Update cancelled.");
                return;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        hotelService.updateRoom(room);
        auditService.logAction("Updated room: " + roomNumber);
        System.out.println("Room updated successfully!");
    }
    
    private static void updateRoomType(Room room) {
        System.out.println("Available room types:");
        System.out.println("1. STANDARD");
        System.out.println("2. DELUXE");
        System.out.println("3. SUITE");
        System.out.println("4. EXECUTIVE");
        
        int typeChoice = getIntInput("Select new room type (1-4): ");
        
        switch (typeChoice) {
            case 1: room.setType(RoomType.STANDARD); break;
            case 2: room.setType(RoomType.DELUXE); break;
            case 3: room.setType(RoomType.SUITE); break;
            case 4: room.setType(RoomType.EXECUTIVE); break;
            default:
                System.out.println("Invalid choice. No changes made.");
        }
    }
    
    private static void updateRoomPrice(Room room) {
        double newPrice = getDoubleInput("Enter new price per night: ");
        room.setPrice(newPrice);
    }
    
    private static void updateRoomOccupancy(Room room) {
        int newOccupancy = getIntInput("Enter new maximum occupancy: ");
        room.setMaxOccupancy(newOccupancy);
    }
    
    private static void updateRoomAmenities(Room room) {
        System.out.println("Current amenities: " + String.join(", ", room.getAmenities()));
        System.out.println("1. Add amenities");
        System.out.println("2. Remove amenities");
        System.out.println("3. Replace all amenities");
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                System.out.println("Enter amenities to add (comma separated): ");
                String addInput = scanner.nextLine();
                String[] amenitiesAdd = addInput.split(",");
                
                for (String amenity : amenitiesAdd) {
                    if (!amenity.trim().isEmpty()) {
                        room.addAmenity(amenity.trim());
                    }
                }
                break;
            case 2:
                System.out.println("Enter amenities to remove (comma separated): ");
                String removeInput = scanner.nextLine();
                String[] amenitiesRemove = removeInput.split(",");
                
                for (String amenity : amenitiesRemove) {
                    if (!amenity.trim().isEmpty()) {
                        room.removeAmenity(amenity.trim());
                    }
                }
                break;
            case 3:
                room.clearAmenities();
                System.out.println("Enter new amenities (comma separated): ");
                String newInput = scanner.nextLine();
                String[] newAmenities = newInput.split(",");
                
                for (String amenity : newAmenities) {
                    if (!amenity.trim().isEmpty()) {
                        room.addAmenity(amenity.trim());
                    }
                }
                break;
            default:
                System.out.println("Invalid choice. No changes made.");
        }
    }
    
    private static void changeRoomStatus() {
        System.out.println("\n----- Change Room Status -----");
        int roomNumber = getIntInput("Enter room number: ");
        
        Room room = hotelService.getRoomByNumber(roomNumber);
        if (room == null) {
            System.out.println("Room not found!");
            return;
        }
        
        System.out.println("Current status: " + room.getStatus());
        System.out.println("Available statuses:");
        System.out.println("1. AVAILABLE");
        System.out.println("2. OCCUPIED");
        System.out.println("3. UNDER_MAINTENANCE");
        System.out.println("4. RESERVED");
        
        int statusChoice = getIntInput("Select new status (1-4): ");
        
        RoomStatus newStatus;
        switch (statusChoice) {
            case 1: newStatus = RoomStatus.AVAILABLE; break;
            case 2: newStatus = RoomStatus.OCCUPIED; break;
            case 3: newStatus = RoomStatus.UNDER_MAINTENANCE; break;
            case 4: newStatus = RoomStatus.RESERVED; break;
            default:
                System.out.println("Invalid choice. No changes made.");
                return;
        }
        
        room.setStatus(newStatus);
        hotelService.updateRoom(room);
        auditService.logAction("Changed status of room " + roomNumber + " to " + newStatus);
        System.out.println("Room status updated successfully!");
    }
    
    // Guest Management Menu
    private static void guestManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== GUEST MANAGEMENT =====");
            System.out.println("1. Register New Guest");
            System.out.println("2. Search Guest");
            System.out.println("3. View All Guests");
            System.out.println("4. Update Guest Information");
            System.out.println("0. Back to Main Menu");
            System.out.println("===========================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    registerNewGuest();
                    break;
                case 2:
                    searchGuest();
                    break;
                case 3:
                    viewAllGuests();
                    break;
                case 4:
                    updateGuestInfo();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void registerNewGuest() {
        System.out.println("\n----- Register New Guest -----");
        
        System.out.println("Enter guest name: ");
        String name = scanner.nextLine();
        
        System.out.println("Enter contact number: ");
        String contactNumber = scanner.nextLine();
        
        System.out.println("Enter email: ");
        String email = scanner.nextLine();
        
        System.out.println("Enter address: ");
        String address = scanner.nextLine();
        
        Guest newGuest = new Guest(hotelService.generateGuestId(), name, contactNumber, email, address);
        hotelService.addGuest(newGuest);
        
        auditService.logAction("Registered new guest: " + name + " (ID: " + newGuest.getGuestId() + ")");
        System.out.println("Guest registered successfully with ID: " + newGuest.getGuestId());
    }
    
    private static void searchGuest() {
        System.out.println("\n----- Search Guest -----");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        
        int choice = getIntInput("Enter your choice: ");
        
        List<Guest> results = new ArrayList<>();
        
        switch (choice) {
            case 1:
                String guestId = getStringInput("Enter guest ID: ");
                Guest guest = hotelService.getGuestById(guestId);
                if (guest != null) {
                    results.add(guest);
                }
                break;
            case 2:
                String name = getStringInput("Enter guest name: ");
                results = hotelService.searchGuestsByName(name);
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        if (results.isEmpty()) {
            System.out.println("No guests found matching your criteria.");
            return;
        }
        
        displayGuestResults(results);
    }
    
    private static void displayGuestResults(List<Guest> guests) {
        System.out.printf("%-10s %-20s %-15s %-25s %-30s\n", 
                "ID", "Name", "Contact", "Email", "Address");
        System.out.println("--------------------------------------------------------------------------------------------");
        
        for (Guest guest : guests) {
            System.out.printf("%-10s %-20s %-15s %-25s %-30s\n", 
                    guest.getGuestId(), 
                    guest.getName(), 
                    guest.getContactNumber(), 
                    guest.getEmail(),
                    guest.getAddress());
        }
    }
    
    private static void viewAllGuests() {
        System.out.println("\n----- All Guests -----");
        List<Guest> guests = hotelService.getAllGuests();
        
        if (guests.isEmpty()) {
            System.out.println("No guests found in the system.");
            return;
        }
        
        displayGuestResults(guests);
    }
    
    private static void updateGuestInfo() {
        System.out.println("\n----- Update Guest Information -----");
        String guestId = getStringInput("Enter guest ID: ");
        
        Guest guest = hotelService.getGuestById(guestId);
        if (guest == null) {
            System.out.println("Guest not found!");
            return;
        }
        
        System.out.println("Current guest details:");
        System.out.printf("ID: %s\nName: %s\nContact: %s\nEmail: %s\nAddress: %s\n",
                guest.getGuestId(), guest.getName(), guest.getContactNumber(), 
                guest.getEmail(), guest.getAddress());
        
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Name");
        System.out.println("2. Contact Number");
        System.out.println("3. Email");
        System.out.println("4. Address");
        System.out.println("0. Cancel");
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                String newName = getStringInput("Enter new name: ");
                guest.setName(newName);
                break;
            case 2:
                String newContact = getStringInput("Enter new contact number: ");
                guest.setContactNumber(newContact);
                break;
            case 3:
                String newEmail = getStringInput("Enter new email: ");
                guest.setEmail(newEmail);
                break;
            case 4:
                String newAddress = getStringInput("Enter new address: ");
                guest.setAddress(newAddress);
                break;
            case 0:
                System.out.println("Update cancelled.");
                return;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        hotelService.updateGuest(guest);
        auditService.logAction("Updated information for guest: " + guestId);
        System.out.println("Guest information updated successfully!");
    }
    
    // Booking Management Menu
    private static void bookingManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== BOOKING MANAGEMENT =====");
            System.out.println("1. Create New Booking");
            System.out.println("2. View All Bookings");
            System.out.println("3. Search Booking");
            System.out.println("4. Modify Booking");
            System.out.println("5. Cancel Booking");
            System.out.println("0. Back to Main Menu");
            System.out.println("=============================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    createNewBooking();
                    break;
                case 2:
                    viewAllBookings();
                    break;
                case 3:
                    searchBooking();
                    break;
                case 4:
                    modifyBooking();
                    break;
                case 5:
                    cancelBooking();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void createNewBooking() {
        System.out.println("\n----- Create New Booking -----");
        
        // First, find the guest
        String guestId = getStringInput("Enter guest ID (or type 'new' to register a new guest): ");
        
        Guest guest;
        if (guestId.equalsIgnoreCase("new")) {
            registerNewGuest();
            List<Guest> allGuests = hotelService.getAllGuests();
            guest = allGuests.get(allGuests.size() - 1); // Get the last registered guest
        } else {
            guest = hotelService.getGuestById(guestId);
            if (guest == null) {
                System.out.println("Guest not found! Please register the guest first.");
                return;
            }
        }
        
        // Show available rooms
        System.out.println("\nAvailable Rooms:");
        List<Room> availableRooms = hotelService.getAvailableRooms();
        
        if (availableRooms.isEmpty()) {
            System.out.println("No rooms available for booking!");
            return;
        }
        
        System.out.printf("%-10s %-15s %-10s %-10s %-30s\n", 
                "Room No.", "Type", "Price", "Max Occ.", "Amenities");
        System.out.println("-------------------------------------------------------------------------");
        
        for (Room room : availableRooms) {
            System.out.printf("%-10d %-15s $%-9.2f %-10d %-30s\n", 
                    room.getRoomNumber(), 
                    room.getType(), 
                    room.getPrice(), 
                    room.getMaxOccupancy(),
                    String.join(", ", room.getAmenities()));
        }
        
        // Select room
        int roomNumber = getIntInput("\nEnter room number to book: ");
        Room selectedRoom = hotelService.getRoomByNumber(roomNumber);
        
        if (selectedRoom == null) {
            System.out.println("Invalid room number!");
            return;
        }
        
        if (selectedRoom.getStatus() != RoomStatus.AVAILABLE) {
            System.out.println("This room is not available for booking!");
            return;
        }
        
        // Get booking dates
        LocalDate checkInDate = getDateInput("Enter check-in date (yyyy-MM-dd): ");
        LocalDate checkOutDate = getDateInput("Enter check-out date (yyyy-MM-dd): ");
        
        if (checkOutDate.isBefore(checkInDate) || checkOutDate.isEqual(checkInDate)) {
            System.out.println("Check-out date must be after check-in date!");
            return;
        }
        
        // Get number of guests
        int numberOfGuests = getIntInput("Enter number of guests: ");
        
        if (numberOfGuests > selectedRoom.getMaxOccupancy()) {
            System.out.println("Number of guests exceeds room capacity!");
            return;
        }
        
        // Create booking
        String bookingId = hotelService.generateBookingId();
        Booking newBooking = new Booking(
            bookingId, 
            guest.getGuestId(), 
            roomNumber, 
            checkInDate, 
            checkOutDate, 
            BookingStatus.CONFIRMED,
            numberOfGuests
        );
        
        // Add special requests if any
        System.out.println("Enter special requests (if any): ");
        String specialRequests = scanner.nextLine();
        if (!specialRequests.trim().isEmpty()) {
            newBooking.setSpecialRequests(specialRequests);
        }
        
        // Save booking and update room status
        hotelService.addBooking(newBooking);
        selectedRoom.setStatus(RoomStatus.RESERVED);
        hotelService.updateRoom(selectedRoom);
        
        // Calculate total amount
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        double totalAmount = days * selectedRoom.getPrice();
        
        auditService.logAction("Created new booking: " + bookingId + " for guest: " + guest.getName());
        
        System.out.println("\nBooking created successfully!");
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Guest: " + guest.getName());
        System.out.println("Room: " + roomNumber + " (" + selectedRoom.getType() + ")");
        System.out.println("Check-in: " + checkInDate);
        System.out.println("Check-out: " + checkOutDate);
        System.out.println("Number of nights: " + days);
        System.out.println("Total amount: $" + String.format("%.2f", totalAmount));
    }
    
    private static void viewAllBookings() {
        System.out.println("\n----- All Bookings -----");
        List<Booking> bookings = hotelService.getAllBookings();
        
        if (bookings.isEmpty()) {
            System.out.println("No bookings found in the system.");
            return;
        }
        
        displayBookingResults(bookings);
    }
    
    private static void displayBookingResults(List<Booking> bookings) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        System.out.printf("%-10s %-10s %-10s %-12s %-12s %-15s %-8s\n", 
                "Booking ID", "Guest ID", "Room No.", "Check-in", "Check-out", "Status", "Guests");
        System.out.println("---------------------------------------------------------------------------------");
        
        for (Booking booking : bookings) {
            System.out.printf("%-10s %-10s %-10d %-12s %-12s %-15s %-8d\n", 
                    booking.getBookingId(), 
                    booking.getGuestId(), 
                    booking.getRoomNumber(), 
                    booking.getCheckInDate().format(formatter),
                    booking.getCheckOutDate().format(formatter),
                    booking.getStatus(),
                    booking.getNumberOfGuests());
        }
    }
    
    private static void searchBooking() {
        System.out.println("\n----- Search Booking -----");
        System.out.println("1. Search by Booking ID");
        System.out.println("2. Search by Guest ID");
        System.out.println("3. Search by Room Number");
        
        int choice = getIntInput("Enter your choice: ");
        
        List<Booking> results = new ArrayList<>();
        
        switch (choice) {
            case 1:
                String bookingId = getStringInput("Enter booking ID: ");
                Booking booking = hotelService.getBookingById(bookingId);
                if (booking != null) {
                    results.add(booking);
                }
                break;
            case 2:
                String guestId = getStringInput("Enter guest ID: ");
                results = hotelService.getBookingsByGuestId(guestId);
                break;
            case 3:
                int roomNumber = getIntInput("Enter room number: ");
                results = hotelService.getBookingsByRoomNumber(roomNumber);
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        if (results.isEmpty()) {
            System.out.println("No bookings found matching your criteria.");
            return;
        }
        
        displayBookingResults(results);
    }
    
    private static void modifyBooking() {
        System.out.println("\n----- Modify Booking -----");
        String bookingId = getStringInput("Enter booking ID: ");
        
        Booking booking = hotelService.getBookingById(bookingId);
        if (booking == null) {
            System.out.println("Booking not found!");
            return;
        }
        
        if (booking.getStatus() == BookingStatus.CHECKED_OUT || booking.getStatus() == BookingStatus.CANCELLED) {
            System.out.println("Cannot modify a completed or cancelled booking!");
            return;
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println("Current booking details:");
        System.out.printf("Booking ID: %s\nGuest ID: %s\nRoom Number: %d\nCheck-in: %s\nCheck-out: %s\nStatus: %s\nNumber of Guests: %d\nSpecial Requests: %s\n",
                booking.getBookingId(), booking.getGuestId(), booking.getRoomNumber(), 
                booking.getCheckInDate().format(formatter), booking.getCheckOutDate().format(formatter),
                booking.getStatus(), booking.getNumberOfGuests(), booking.getSpecialRequests());
        
        System.out.println("\nWhat would you like to modify?");
        System.out.println("1. Check-in Date");
        System.out.println("2. Check-out Date");
        System.out.println("3. Number of Guests");
        System.out.println("4. Special Requests");
        System.out.println("5. Room");
        System.out.println("0. Cancel");
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                LocalDate newCheckIn = getDateInput("Enter new check-in date (yyyy-MM-dd): ");
                if (newCheckIn.isAfter(booking.getCheckOutDate()) || newCheckIn.isEqual(booking.getCheckOutDate())) {
                    System.out.println("Check-in date must be before check-out date!");
                    return;
                }
                booking.setCheckInDate(newCheckIn);
                break;
            case 2:
                LocalDate newCheckOut = getDateInput("Enter new check-out date (yyyy-MM-dd): ");
                if (newCheckOut.isBefore(booking.getCheckInDate()) || newCheckOut.isEqual(booking.getCheckInDate())) {
                    System.out.println("Check-out date must be after check-in date!");
                    return;
                }
                booking.setCheckOutDate(newCheckOut);
                break;
            case 3:
                int newGuests = getIntInput("Enter new number of guests: ");
                Room room = hotelService.getRoomByNumber(booking.getRoomNumber());
                if (newGuests > room.getMaxOccupancy()) {
                    System.out.println("Number of guests exceeds room capacity!");
                    return;
                }
                booking.setNumberOfGuests(newGuests);
                break;
            case 4:
                System.out.println("Current special requests: " + booking.getSpecialRequests());
                System.out.println("Enter new special requests: ");
                String newRequests = scanner.nextLine();
                booking.setSpecialRequests(newRequests);
                break;
            case 5:
                changeBookingRoom(booking);
                break;
            case 0:
                System.out.println("Modification cancelled.");
                return;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        hotelService.updateBooking(booking);
        auditService.logAction("Modified booking: " + bookingId);
        System.out.println("Booking modified successfully!");
    }
    
    private static void changeBookingRoom(Booking booking) {
        // Show available rooms
        System.out.println("\nAvailable Rooms:");
        List<Room> availableRooms = hotelService.getAvailableRooms();
        
        // Add current room to the list if it's not available (since it's the one being changed)
        Room currentRoom = hotelService.getRoomByNumber(booking.getRoomNumber());
        boolean currentRoomAdded = false;
        
        if (!availableRooms.contains(currentRoom)) {
            availableRooms.add(currentRoom);
            currentRoomAdded = true;
        }
        
        if (availableRooms.isEmpty()) {
            System.out.println("No rooms available for changing!");
            return;
        }
        
        System.out.printf("%-10s %-15s %-10s %-10s %-15s %-30s\n", 
                "Room No.", "Type", "Price", "Max Occ.", "Status", "Amenities");
        System.out.println("-----------------------------------------------------------------------------------");
        
        for (Room room : availableRooms) {
            System.out.printf("%-10d %-15s $%-9.2f %-10d %-15s %-30s\n", 
                    room.getRoomNumber(), 
                    room.getType(), 
                    room.getPrice(), 
                    room.getMaxOccupancy(),
                    room.getStatus(),
                    String.join(", ", room.getAmenities()));
        }
        
        // Select new room
        int roomNumber = getIntInput("\nEnter new room number: ");
        
        if (roomNumber == booking.getRoomNumber()) {
            System.out.println("This is the same room! No changes made.");
            return;
        }
        
        Room selectedRoom = hotelService.getRoomByNumber(roomNumber);
        
        if (selectedRoom == null) {
            System.out.println("Invalid room number!");
            return;
        }
        
        if (selectedRoom.getStatus() != RoomStatus.AVAILABLE && selectedRoom.getRoomNumber() != booking.getRoomNumber()) {
            System.out.println("This room is not available for booking!");
            return;
        }
        
        // Check if new room can accommodate the guests
        if (booking.getNumberOfGuests() > selectedRoom.getMaxOccupancy()) {
            System.out.println("Number of guests exceeds new room capacity!");
            return;
        }
        
        // Update room statuses
        currentRoom.setStatus(RoomStatus.AVAILABLE);
        selectedRoom.setStatus(RoomStatus.RESERVED);
        
        hotelService.updateRoom(currentRoom);
        hotelService.updateRoom(selectedRoom);
        
        // Update booking
        booking.setRoomNumber(roomNumber);
    }
    
    private static void cancelBooking() {
        System.out.println("\n----- Cancel Booking -----");
        String bookingId = getStringInput("Enter booking ID: ");
        
        Booking booking = hotelService.getBookingById(bookingId);
        if (booking == null) {
            System.out.println("Booking not found!");
            return;
        }
        
        if (booking.getStatus() == BookingStatus.CHECKED_OUT || booking.getStatus() == BookingStatus.CANCELLED) {
            System.out.println("Cannot cancel a completed or already cancelled booking!");
            return;
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println("Booking details:");
        System.out.printf("Booking ID: %s\nGuest ID: %s\nRoom Number: %d\nCheck-in: %s\nCheck-out: %s\nStatus: %s\n",
                booking.getBookingId(), booking.getGuestId(), booking.getRoomNumber(), 
                booking.getCheckInDate().format(formatter), booking.getCheckOutDate().format(formatter),
                booking.getStatus());
        
        String confirm = getStringInput("Are you sure you want to cancel this booking? (y/n): ");
        
        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println("Cancellation aborted.");
            return;
        }
        
        // Update booking status
        booking.setStatus(BookingStatus.CANCELLED);
        hotelService.updateBooking(booking);
        
        // Update room status
        Room room = hotelService.getRoomByNumber(booking.getRoomNumber());
        room.setStatus(RoomStatus.AVAILABLE);
        hotelService.updateRoom(room);
        
        auditService.logAction("Cancelled booking: " + bookingId);
        System.out.println("Booking cancelled successfully!");
    }
    
    // Check-In/Check-Out Menu
    private static void checkInOutMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== CHECK-IN/CHECK-OUT =====");
            System.out.println("1. Check-In Guest");
            System.out.println("2. Check-Out Guest");
            System.out.println("3. View Checked-In Guests");
            System.out.println("0. Back to Main Menu");
            System.out.println("============================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    checkInGuest();
                    break;
                case 2:
                    checkOutGuest();
                    break;
                case 3:
                    viewCheckedInGuests();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void checkInGuest() {
        System.out.println("\n----- Check-In Guest -----");
        
        // First, find the booking
        System.out.println("1. Search by Booking ID");
        System.out.println("2. Search by Guest Name");
        
        int searchChoice = getIntInput("Enter your choice: ");
        
        Booking booking = null;
        
        switch (searchChoice) {
            case 1:
                String bookingId = getStringInput("Enter booking ID: ");
                booking = hotelService.getBookingById(bookingId);
                break;
            case 2:
                String guestName = getStringInput("Enter guest name: ");
                List<Guest> guests = hotelService.searchGuestsByName(guestName);
                
                if (guests.isEmpty()) {
                    System.out.println("No guests found with that name!");
                    return;
                }
                
                if (guests.size() > 1) {
                    System.out.println("Multiple guests found with that name. Please select one:");
                    for (int i = 0; i < guests.size(); i++) {
                        System.out.println((i + 1) + ". " + guests.get(i).getName() + " (ID: " + guests.get(i).getGuestId() + ")");
                    }
                    
                    int guestChoice = getIntInput("Enter your choice: ");
                    if (guestChoice < 1 || guestChoice > guests.size()) {
                        System.out.println("Invalid choice!");
                        return;
                    }
                    
                    Guest selectedGuest = guests.get(guestChoice - 1);
                    List<Booking> guestBookings = hotelService.getBookingsByGuestId(selectedGuest.getGuestId());
                    
                    if (guestBookings.isEmpty()) {
                        System.out.println("No bookings found for this guest!");
                        return;
                    }
                    
                    System.out.println("Bookings for " + selectedGuest.getName() + ":");
                    displayBookingResults(guestBookings);
                    
                    String bookingIdChoice = getStringInput("Enter booking ID to check in: ");
                    booking = hotelService.getBookingById(bookingIdChoice);
                } else {
                    Guest selectedGuest = guests.get(0);
                    List<Booking> guestBookings = hotelService.getBookingsByGuestId(selectedGuest.getGuestId());
                    
                    if (guestBookings.isEmpty()) {
                        System.out.println("No bookings found for this guest!");
                        return;
                    }
                    
                    System.out.println("Bookings for " + selectedGuest.getName() + ":");
                    displayBookingResults(guestBookings);
                    
                    String bookingIdChoice = getStringInput("Enter booking ID to check in: ");
                    booking = hotelService.getBookingById(bookingIdChoice);
                }
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }
        
        if (booking == null) {
            System.out.println("Booking not found!");
            return;
        }
        
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            System.out.println("This booking cannot be checked in (status: " + booking.getStatus() + ")");
            return;
        }
        
        // Check if check-in date is today or in the past
        LocalDate today = LocalDate.now();
        if (booking.getCheckInDate().isAfter(today)) {
            System.out.println("This booking is for a future date (" + booking.getCheckInDate() + ")");
            String forcedCheckIn = getStringInput("Do you still want to check in? (y/n): ");
            
            if (!forcedCheckIn.equalsIgnoreCase("y")) {
                System.out.println("Check-in cancelled.");
                return;
            }
        }
        
        // Update booking status
        booking.setStatus(BookingStatus.CHECKED_IN);
        hotelService.updateBooking(booking);
        
        // Update room status
        Room room = hotelService.getRoomByNumber(booking.getRoomNumber());
        room.setStatus(RoomStatus.OCCUPIED);
        hotelService.updateRoom(room);
        
        Guest guest = hotelService.getGuestById(booking.getGuestId());
        
        auditService.logAction("Checked in guest: " + guest.getName() + " (Booking: " + booking.getBookingId() + ")");
        System.out.println("Guest checked in successfully!");
        System.out.println("Guest: " + guest.getName());
        System.out.println("Room: " + room.getRoomNumber() + " (" + room.getType() + ")");
    }
    
    private static void checkOutGuest() {
        System.out.println("\n----- Check-Out Guest -----");
        
        // First, find the booking
        System.out.println("1. Search by Booking ID");
        System.out.println("2. Search by Room Number");
        
        int searchChoice = getIntInput("Enter your choice: ");
        
        Booking booking = null;
        
        switch (searchChoice) {
            case 1:
                String bookingId = getStringInput("Enter booking ID: ");
                booking = hotelService.getBookingById(bookingId);
                break;
            case 2:
                int roomNumber = getIntInput("Enter room number: ");
                List<Booking> roomBookings = hotelService.getBookingsByRoomNumber(roomNumber);
                
                if (roomBookings.isEmpty()) {
                    System.out.println("No bookings found for this room!");
                    return;
                }
                
                // Find checked-in booking for this room
                for (Booking b : roomBookings) {
                    if (b.getStatus() == BookingStatus.CHECKED_IN) {
                        booking = b;
                        break;
                    }
                }
                
                if (booking == null) {
                    System.out.println("No checked-in booking found for this room!");
                    return;
                }
                break;
            default:
                System.out.println("Invalid choice!");
                return;
        }
        
        if (booking == null) {
            System.out.println("Booking not found!");
            return;
        }
        
        if (booking.getStatus() != BookingStatus.CHECKED_IN) {
            System.out.println("This booking cannot be checked out (status: " + booking.getStatus() + ")");
            return;
        }
        
        Guest guest = hotelService.getGuestById(booking.getGuestId());
        Room room = hotelService.getRoomByNumber(booking.getRoomNumber());
        
        // Calculate stay duration and total amount
        LocalDate checkInDate = booking.getCheckInDate();
        LocalDate checkOutDate = LocalDate.now();
        if (checkOutDate.isBefore(booking.getCheckOutDate())) {
            System.out.println("Guest is checking out earlier than planned.");
            String confirm = getStringInput("Confirm early check-out? (y/n): ");
            
            if (!confirm.equalsIgnoreCase("y")) {
                System.out.println("Check-out cancelled.");
                return;
            }
            
            booking.setCheckOutDate(checkOutDate);
        } else if (checkOutDate.isAfter(booking.getCheckOutDate())) {
            System.out.println("Guest is checking out later than planned.");
            String confirm = getStringInput("Confirm late check-out? (y/n): ");
            
            if (!confirm.equalsIgnoreCase("y")) {
                System.out.println("Using planned check-out date for billing.");
                checkOutDate = booking.getCheckOutDate();
            } else {
                booking.setCheckOutDate(checkOutDate);
            }
        }
        
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        if (days == 0) days = 1; // Minimum 1 day charge
        
        double roomCharge = days * room.getPrice();
        
        // Ask for additional charges
        System.out.println("\nAny additional charges?");
        System.out.println("1. Yes");
        System.out.println("2. No");
        
        int additionalChoice = getIntInput("Enter your choice: ");
        
        double additionalCharges = 0.0;
        String additionalInfo = "";
        
        if (additionalChoice == 1) {
            additionalCharges = getDoubleInput("Enter additional charges amount: $");
            System.out.println("Enter description for additional charges: ");
            additionalInfo = scanner.nextLine();
        }
        
        double totalAmount = roomCharge + additionalCharges;
        
        // Generate invoice
        String invoiceId = hotelService.generateInvoiceId();
        Invoice invoice = new Invoice(
            invoiceId,
            booking.getBookingId(),
            totalAmount,
            LocalDate.now(),
            "Unpaid"
        );
        
        invoice.addCharge("Room Charge", roomCharge);
        if (additionalCharges > 0) {
            invoice.addCharge(additionalInfo, additionalCharges);
        }
        
        hotelService.addInvoice(invoice);
        
        // Update booking status
        booking.setStatus(BookingStatus.CHECKED_OUT);
        hotelService.updateBooking(booking);
        
        // Update room status
        room.setStatus(RoomStatus.UNDER_MAINTENANCE); // Set to maintenance for cleaning
        hotelService.updateRoom(room);
        
        auditService.logAction("Checked out guest: " + guest.getName() + " (Booking: " + booking.getBookingId() + ")");
        
        System.out.println("\nGuest checked out successfully!");
        System.out.println("Guest: " + guest.getName());
        System.out.println("Room: " + room.getRoomNumber() + " (" + room.getType() + ")");
        System.out.println("Stay duration: " + days + " day(s)");
        System.out.println("Room charge: $" + String.format("%.2f", roomCharge));
        if (additionalCharges > 0) {
            System.out.println("Additional charges: $" + String.format("%.2f", additionalCharges) + " (" + additionalInfo + ")");
        }
        System.out.println("Total amount: $" + String.format("%.2f", totalAmount));
        System.out.println("Invoice ID: " + invoiceId);
        
        // Process payment
        System.out.println("\nProcess payment now?");
        System.out.println("1. Yes");
        System.out.println("2. No (Bill to guest account)");
        
        int paymentChoice = getIntInput("Enter your choice: ");
        
        if (paymentChoice == 1) {
            processPayment(invoice);
        } else {
            System.out.println("Invoice saved. Payment can be processed later.");
        }
    }
    
    private static void viewCheckedInGuests() {
        System.out.println("\n----- Checked-In Guests -----");
        List<Booking> checkedInBookings = hotelService.getBookingsByStatus(BookingStatus.CHECKED_IN);
        
        if (checkedInBookings.isEmpty()) {
            System.out.println("No guests are currently checked in.");
            return;
        }
        
        System.out.printf("%-10s %-20s %-10s %-15s %-12s %-12s\n", 
                "Room No.", "Guest Name", "Guest ID", "Contact", "Check-in", "Check-out");
        System.out.println("---------------------------------------------------------------------------------");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (Booking booking : checkedInBookings) {
            Guest guest = hotelService.getGuestById(booking.getGuestId());
            
            System.out.printf("%-10d %-20s %-10s %-15s %-12s %-12s\n", 
                    booking.getRoomNumber(), 
                    guest.getName(), 
                    guest.getGuestId(), 
                    guest.getContactNumber(),
                    booking.getCheckInDate().format(formatter),
                    booking.getCheckOutDate().format(formatter));
        }
    }
    
    // Billing Menu
    private static void billingMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== BILLING =====");
            System.out.println("1. View All Invoices");
            System.out.println("2. Search Invoice");
            System.out.println("3. Process Payment");
            System.out.println("0. Back to Main Menu");
            System.out.println("===================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    viewAllInvoices();
                    break;
                case 2:
                    searchInvoice();
                    break;
                case 3:
                    String invoiceId = getStringInput("Enter invoice ID: ");
                    Invoice invoice = hotelService.getInvoiceById(invoiceId);
                    if (invoice == null) {
                        System.out.println("Invoice not found!");
                    } else {
                        processPayment(invoice);
                    }
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void viewAllInvoices() {
        System.out.println("\n----- All Invoices -----");
        List<Invoice> invoices = hotelService.getAllInvoices();
        
        if (invoices.isEmpty()) {
            System.out.println("No invoices found in the system.");
            return;
        }
        
        displayInvoiceResults(invoices);
    }
    
    private static void displayInvoiceResults(List<Invoice> invoices) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        System.out.printf("%-10s %-10s %-12s %-12s %-15s\n", 
                "Invoice ID", "Booking ID", "Amount", "Issue Date", "Status");
        System.out.println("----------------------------------------------------------");
        
        for (Invoice invoice : invoices) {
            System.out.printf("%-10s %-10s $%-11.2f %-12s %-15s\n", 
                    invoice.getInvoiceId(), 
                    invoice.getBookingId(), 
                    invoice.getAmount(), 
                    invoice.getIssueDate().format(formatter),
                    invoice.getPaymentStatus());
        }
    }
    
    private static void searchInvoice() {
        System.out.println("\n----- Search Invoice -----");
        System.out.println("1. Search by Invoice ID");
        System.out.println("2. Search by Booking ID");
        System.out.println("3. Search by Guest ID");
        
        int choice = getIntInput("Enter your choice: ");
        
        List<Invoice> results = new ArrayList<>();
        
        switch (choice) {
            case 1:
                String invoiceId = getStringInput("Enter invoice ID: ");
                Invoice invoice = hotelService.getInvoiceById(invoiceId);
                if (invoice != null) {
                    results.add(invoice);
                }
                break;
            case 2:
                String bookingId = getStringInput("Enter booking ID: ");
                results = hotelService.getInvoicesByBookingId(bookingId);
                break;
            case 3:
                String guestId = getStringInput("Enter guest ID: ");
                List<Booking> guestBookings = hotelService.getBookingsByGuestId(guestId);
                
                for (Booking booking : guestBookings) {
                    List<Invoice> bookingInvoices = hotelService.getInvoicesByBookingId(booking.getBookingId());
                    results.addAll(bookingInvoices);
                }
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        if (results.isEmpty()) {
            System.out.println("No invoices found matching your criteria.");
            return;
        }
        
        displayInvoiceResults(results);
        
        String viewDetails = getStringInput("View invoice details? (Enter invoice ID or 'n' to cancel): ");
        
        if (!viewDetails.equalsIgnoreCase("n")) {
            Invoice selectedInvoice = hotelService.getInvoiceById(viewDetails);
            
            if (selectedInvoice == null) {
                System.out.println("Invalid invoice ID!");
                return;
            }
            
            displayInvoiceDetails(selectedInvoice);
        }
    }
    
    private static void displayInvoiceDetails(Invoice invoice) {
        System.out.println("\n----- Invoice Details -----");
        System.out.println("Invoice ID: " + invoice.getInvoiceId());
        System.out.println("Booking ID: " + invoice.getBookingId());
        
        Booking booking = hotelService.getBookingById(invoice.getBookingId());
        Guest guest = hotelService.getGuestById(booking.getGuestId());
        
        System.out.println("Guest: " + guest.getName() + " (ID: " + guest.getGuestId() + ")");
        System.out.println("Room: " + booking.getRoomNumber());
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println("Stay Period: " + booking.getCheckInDate().format(formatter) + 
                " to " + booking.getCheckOutDate().format(formatter));
        
        System.out.println("\nItemized Charges:");
        System.out.println("------------------");
        
        Map<String, Double> charges = invoice.getItemizedCharges();
        for (Map.Entry<String, Double> entry : charges.entrySet()) {
            System.out.printf("%-30s $%.2f\n", entry.getKey(), entry.getValue());
        }
        
        System.out.println("------------------");
        System.out.printf("Total Amount: $%.2f\n", invoice.getAmount());
        System.out.println("Issue Date: " + invoice.getIssueDate().format(formatter));
        System.out.println("Payment Status: " + invoice.getPaymentStatus());
        
        if (invoice.getPaymentStatus().equals("Paid")) {
            System.out.println("Payment Date: " + invoice.getPaymentDate().format(formatter));
            System.out.println("Payment Method: " + invoice.getPaymentMethod());
        }
    }
    
    private static void processPayment(Invoice invoice) {
        if (invoice.getPaymentStatus().equals("Paid")) {
            System.out.println("This invoice has already been paid!");
            return;
        }
        
        displayInvoiceDetails(invoice);
        
        System.out.println("\n----- Process Payment -----");
        System.out.println("Select payment method:");
        System.out.println("1. Cash");
        System.out.println("2. Credit Card");
        System.out.println("3. Debit Card");
        System.out.println("4. Bank Transfer");
        System.out.println("0. Cancel");
        
        int choice = getIntInput("Enter your choice: ");
        
        String paymentMethod;
        
        switch (choice) {
            case 1:
                paymentMethod = "Cash";
                break;
            case 2:
                paymentMethod = "Credit Card";
                break;
            case 3:
                paymentMethod = "Debit Card";
                break;
            case 4:
                paymentMethod = "Bank Transfer";
                break;
            case 0:
                System.out.println("Payment cancelled.");
                return;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        // For card payments, collect additional information
        if (choice == 2 || choice == 3) {
            System.out.println("Enter card number: ");
            String cardNumber = scanner.nextLine();
            
            System.out.println("Enter expiry date (MM/YY): ");
            String expiryDate = scanner.nextLine();
            
            System.out.println("Enter CVV: ");
            String cvv = scanner.nextLine();
            
            // In a real system, this would process the payment through a payment gateway
            System.out.println("Processing card payment...");
            // Simulate processing delay
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        // Update invoice
        invoice.setPaymentStatus("Paid");
        invoice.setPaymentDate(LocalDate.now());
        invoice.setPaymentMethod(paymentMethod);
        
        hotelService.updateInvoice(invoice);
        
        auditService.logAction("Processed payment for invoice: " + invoice.getInvoiceId() + 
                " (Amount: $" + String.format("%.2f", invoice.getAmount()) + ", Method: " + paymentMethod + ")");
        
        System.out.println("\nPayment processed successfully!");
        System.out.println("Invoice ID: " + invoice.getInvoiceId());
        System.out.println("Amount: $" + String.format("%.2f", invoice.getAmount()));
        System.out.println("Payment Method: " + paymentMethod);
        System.out.println("Payment Date: " + invoice.getPaymentDate());
    }
    
    // Report Menu
    private static void reportMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== REPORTS =====");
            System.out.println("1. Occupancy Report");
            System.out.println("2. Revenue Report");
            System.out.println("3. Booking Statistics");
            System.out.println("0. Back to Main Menu");
            System.out.println("===================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    generateOccupancyReport();
                    break;
                case 2:
                    generateRevenueReport();
                    break;
                case 3:
                    generateBookingStatistics();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void generateOccupancyReport() {
        System.out.println("\n----- Occupancy Report -----");
        
        List<Room> allRooms = hotelService.getAllRooms();
        int totalRooms = allRooms.size();
        
        if (totalRooms == 0) {
            System.out.println("No rooms found in the system.");
            return;
        }
        
        Map<RoomStatus, Integer> statusCounts = new HashMap<>();
        Map<RoomType, Integer> typeCounts = new HashMap<>();
        
        for (Room room : allRooms) {
            // Count by status
            RoomStatus status = room.getStatus();
            statusCounts.put(status, statusCounts.getOrDefault(status, 0) + 1);
            
            // Count by type
            RoomType type = room.getType();
            typeCounts.put(type, typeCounts.getOrDefault(type, 0) + 1);
        }
        
        int occupiedRooms = statusCounts.getOrDefault(RoomStatus.OCCUPIED, 0);
        int reservedRooms = statusCounts.getOrDefault(RoomStatus.RESERVED, 0);
        int availableRooms = statusCounts.getOrDefault(RoomStatus.AVAILABLE, 0);
        int maintenanceRooms = statusCounts.getOrDefault(RoomStatus.UNDER_MAINTENANCE, 0);
        
        double occupancyRate = (double) (occupiedRooms + reservedRooms) / totalRooms * 100;
        
        System.out.println("Date: " + LocalDate.now());
        System.out.println("Total Rooms: " + totalRooms);
        System.out.println("\nRoom Status Distribution:");
        System.out.println("------------------------");
        System.out.printf("Occupied: %d (%.1f%%)\n", occupiedRooms, (double) occupiedRooms / totalRooms * 100);
        System.out.printf("Reserved: %d (%.1f%%)\n", reservedRooms, (double) reservedRooms / totalRooms * 100);
        System.out.printf("Available: %d (%.1f%%)\n", availableRooms, (double) availableRooms / totalRooms * 100);
        System.out.printf("Under Maintenance: %d (%.1f%%)\n", maintenanceRooms, (double) maintenanceRooms / totalRooms * 100);
        System.out.println("------------------------");
        System.out.printf("Current Occupancy Rate: %.1f%%\n", occupancyRate);
        
        System.out.println("\nRoom Type Distribution:");
        System.out.println("----------------------");
        for (Map.Entry<RoomType, Integer> entry : typeCounts.entrySet()) {
            System.out.printf("%s: %d (%.1f%%)\n", 
                    entry.getKey(), 
                    entry.getValue(), 
                    (double) entry.getValue() / totalRooms * 100);
        }
        
        auditService.logAction("Generated occupancy report");
    }
    
    private static void generateRevenueReport() {
        System.out.println("\n----- Revenue Report -----");
        
        System.out.println("Select report period:");
        System.out.println("1. Daily (Today)");
        System.out.println("2. Weekly");
        System.out.println("3. Monthly");
        System.out.println("4. Custom Date Range");
        
        int periodChoice = getIntInput("Enter your choice: ");
        
        LocalDate startDate = null;
        LocalDate endDate = null;
        String periodName = "";
        
        switch (periodChoice) {
            case 1:
                startDate = LocalDate.now();
                endDate = LocalDate.now();
                periodName = "Daily (Today)";
                break;
            case 2:
                startDate = LocalDate.now().minusDays(7);
                endDate = LocalDate.now();
                periodName = "Weekly";
                break;
            case 3:
                startDate = LocalDate.now().minusDays(30);
                endDate = LocalDate.now();
                periodName = "Monthly";
                break;
            case 4:
                startDate = getDateInput("Enter start date (yyyy-MM-dd): ");
                endDate = getDateInput("Enter end date (yyyy-MM-dd): ");
                periodName = "Custom (" + startDate + " to " + endDate + ")";
                break;
            default:
                System.out.println("Invalid choice. Using today's date.");
                startDate = LocalDate.now();
                endDate = LocalDate.now();
                periodName = "Daily (Today)";
        }
        
        List<Invoice> allInvoices = hotelService.getAllInvoices();
        List<Invoice> periodInvoices = new ArrayList<>();
        
        // Filter invoices by date
        for (Invoice invoice : allInvoices) {
            LocalDate issueDate = invoice.getIssueDate();
            if ((issueDate.isEqual(startDate) || issueDate.isAfter(startDate)) && 
                (issueDate.isEqual(endDate) || issueDate.isBefore(endDate))) {
                periodInvoices.add(invoice);
            }
        }
        
        if (periodInvoices.isEmpty()) {
            System.out.println("No revenue data found for the selected period.");
            return;
        }
        
        // Calculate total revenue
        double totalRevenue = 0.0;
        double paidRevenue = 0.0;
        double unpaidRevenue = 0.0;
        
        Map<String, Double> revenueByRoomType = new HashMap<>();
        
        for (Invoice invoice : periodInvoices) {
            double amount = invoice.getAmount();
            totalRevenue += amount;
            
            if (invoice.getPaymentStatus().equals("Paid")) {
                paidRevenue += amount;
            } else {
                unpaidRevenue += amount;
            }
            
            // Get room type for this invoice
            Booking booking = hotelService.getBookingById(invoice.getBookingId());
            if (booking != null) {
                Room room = hotelService.getRoomByNumber(booking.getRoomNumber());
                if (room != null) {
                    String roomType = room.getType().toString();
                    revenueByRoomType.put(roomType, revenueByRoomType.getOrDefault(roomType, 0.0) + amount);
                }
            }
        }
        
        // Display report
        System.out.println("Period: " + periodName);
        System.out.println("Total Invoices: " + periodInvoices.size());
        System.out.println("\nRevenue Summary:");
        System.out.println("----------------");
        System.out.printf("Total Revenue: $%.2f\n", totalRevenue);
        System.out.printf("Collected Revenue: $%.2f (%.1f%%)\n", paidRevenue, (paidRevenue / totalRevenue) * 100);
        System.out.printf("Outstanding Revenue: $%.2f (%.1f%%)\n", unpaidRevenue, (unpaidRevenue / totalRevenue) * 100);
        
        System.out.println("\nRevenue by Room Type:");
        System.out.println("--------------------");
        for (Map.Entry<String, Double> entry : revenueByRoomType.entrySet()) {
            System.out.printf("%s: $%.2f (%.1f%%)\n", 
                    entry.getKey(), 
                    entry.getValue(), 
                    (entry.getValue() / totalRevenue) * 100);
        }
        
        auditService.logAction("Generated revenue report for period: " + periodName);
    }
    
    private static void generateBookingStatistics() {
        System.out.println("\n----- Booking Statistics -----");
        
        List<Booking> allBookings = hotelService.getAllBookings();
        
        if (allBookings.isEmpty()) {
            System.out.println("No booking data found in the system.");
            return;
        }
        
        // Count bookings by status
        Map<BookingStatus, Integer> statusCounts = new HashMap<>();
        
        // Count bookings by month
        Map<Integer, Integer> bookingsByMonth = new HashMap<>();
        
        // Calculate average stay duration
        int totalDays = 0;
        int completedBookings = 0;
        
        for (Booking booking : allBookings) {
            // Count by status
            BookingStatus status = booking.getStatus();
            statusCounts.put(status, statusCounts.getOrDefault(status, 0) + 1);
            
            // Count by check-in month
            int month = booking.getCheckInDate().getMonthValue();
            bookingsByMonth.put(month, bookingsByMonth.getOrDefault(month, 0) + 1);
            
            // Calculate stay duration for completed bookings
            if (status == BookingStatus.CHECKED_OUT) {
                long days = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
                totalDays += days;
                completedBookings++;
            }
        }
        
        double averageStay = completedBookings > 0 ? (double) totalDays / completedBookings : 0;
        
        // Display statistics
        System.out.println("Total Bookings: " + allBookings.size());
        
        System.out.println("\nBookings by Status:");
        System.out.println("------------------");
        for (BookingStatus status : BookingStatus.values()) {
            int count = statusCounts.getOrDefault(status, 0);
            System.out.printf("%s: %d (%.1f%%)\n", 
                    status, 
                    count, 
                    (double) count / allBookings.size() * 100);
        }
        
        System.out.println("\nBookings by Month:");
        System.out.println("-----------------");
        String[] months = {"January", "February", "March", "April", "May", "June", 
                          "July", "August", "September", "October", "November", "December"};
        
        for (int i = 1; i <= 12; i++) {
            int count = bookingsByMonth.getOrDefault(i, 0);
            System.out.printf("%s: %d (%.1f%%)\n", 
                    months[i-1], 
                    count, 
                    (double) count / allBookings.size() * 100);
        }
        
        System.out.println("\nAverage Stay Duration: " + String.format("%.1f", averageStay) + " days");
        
        auditService.logAction("Generated booking statistics report");
    }
    
    // Employee Management Menu
    private static void employeeManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== EMPLOYEE MANAGEMENT =====");
            System.out.println("1. Add New Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Update Employee Information");
            System.out.println("4. Assign Role to Employee");
            System.out.println("0. Back to Main Menu");
            System.out.println("==============================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addNewEmployee();
                    break;
                case 2:
                    viewAllEmployees();
                    break;
                case 3:
                    updateEmployeeInfo();
                    break;
                case 4:
                    assignEmployeeRole();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void addNewEmployee() {
        System.out.println("\n----- Add New Employee -----");
        
        System.out.println("Enter employee name: ");
        String name = scanner.nextLine();
        
        System.out.println("Enter contact number: ");
        String contactNumber = scanner.nextLine();
        
        System.out.println("Enter email: ");
        String email = scanner.nextLine();
        
        System.out.println("Enter address: ");
        String address = scanner.nextLine();
        
        System.out.println("Available positions:");
        System.out.println("1. MANAGER");
        System.out.println("2. RECEPTIONIST");
        System.out.println("3. HOUSEKEEPER");
        System.out.println("4. MAINTENANCE");
        System.out.println("5. CHEF");
        
        int positionChoice = getIntInput("Select position (1-5): ");
        
        String position;
        switch (positionChoice) {
            case 1: position = "MANAGER"; break;
            case 2: position = "RECEPTIONIST"; break;
            case 3: position = "HOUSEKEEPER"; break;
            case 4: position = "MAINTENANCE"; break;
            case 5: position = "CHEF"; break;
            default:
                System.out.println("Invalid choice. Setting to RECEPTIONIST by default.");
                position = "RECEPTIONIST";
        }
        
        System.out.println("Enter department: ");
        String department = scanner.nextLine();
        
        Employee newEmployee = new Employee(
            hotelService.generateEmployeeId(),
            name,
            position,
            department,
            contactNumber,
            email,
            address
        );
        
        hotelService.addEmployee(newEmployee);
        
        auditService.logAction("Added new employee: " + name + " (ID: " + newEmployee.getEmployeeId() + ")");
        System.out.println("Employee added successfully with ID: " + newEmployee.getEmployeeId());
    }
    
    private static void viewAllEmployees() {
        System.out.println("\n----- All Employees -----");
        List<Employee> employees = hotelService.getAllEmployees();
        
        if (employees.isEmpty()) {
            System.out.println("No employees found in the system.");
            return;
        }
        
        System.out.printf("%-10s %-20s %-15s %-15s %-15s %-25s\n", 
                "ID", "Name", "Position", "Department", "Contact", "Email");
        System.out.println("--------------------------------------------------------------------------------------------");
        
        for (Employee employee : employees) {
            System.out.printf("%-10s %-20s %-15s %-15s %-15s %-25s\n", 
                    employee.getEmployeeId(), 
                    employee.getName(), 
                    employee.getPosition(), 
                    employee.getDepartment(),
                    employee.getContactNumber(),
                    employee.getEmail());
        }
    }
    
    private static void updateEmployeeInfo() {
        System.out.println("\n----- Update Employee Information -----");
        String employeeId = getStringInput("Enter employee ID: ");
        
        Employee employee = hotelService.getEmployeeById(employeeId);
        if (employee == null) {
            System.out.println("Employee not found!");
            return;
        }
        
        System.out.println("Current employee details:");
        System.out.printf("ID: %s\nName: %s\nPosition: %s\nDepartment: %s\nContact: %s\nEmail: %s\nAddress: %s\n",
                employee.getEmployeeId(), employee.getName(), employee.getPosition(), 
                employee.getDepartment(), employee.getContactNumber(), 
                employee.getEmail(), employee.getAddress());
        
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Name");
        System.out.println("2. Contact Number");
        System.out.println("3. Email");
        System.out.println("4. Address");
        System.out.println("5. Department");
        System.out.println("0. Cancel");
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                String newName = getStringInput("Enter new name: ");
                employee.setName(newName);
                break;
            case 2:
                String newContact = getStringInput("Enter new contact number: ");
                employee.setContactNumber(newContact);
                break;
            case 3:
                String newEmail = getStringInput("Enter new email: ");
                employee.setEmail(newEmail);
                break;
            case 4:
                String newAddress = getStringInput("Enter new address: ");
                employee.setAddress(newAddress);
                break;
            case 5:
                String newDepartment = getStringInput("Enter new department: ");
                employee.setDepartment(newDepartment);
                break;
            case 0:
                System.out.println("Update cancelled.");
                return;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        
        hotelService.updateEmployee(employee);
        auditService.logAction("Updated information for employee: " + employeeId);
        System.out.println("Employee information updated successfully!");
    }
    
    private static void assignEmployeeRole() {
        System.out.println("\n----- Assign Role to Employee -----");
        String employeeId = getStringInput("Enter employee ID: ");
        
        Employee employee = hotelService.getEmployeeById(employeeId);
        if (employee == null) {
            System.out.println("Employee not found!");
            return;
        }
        
        System.out.println("Current position: " + employee.getPosition());
        
        System.out.println("Available positions:");
        System.out.println("1. MANAGER");
        System.out.println("2. RECEPTIONIST");
        System.out.println("3. HOUSEKEEPER");
        System.out.println("4. MAINTENANCE");
        System.out.println("5. CHEF");
        
        int positionChoice = getIntInput("Select new position (1-5): ");
        
        String position;
        switch (positionChoice) {
            case 1: position = "MANAGER"; break;
            case 2: position = "RECEPTIONIST"; break;
            case 3: position = "HOUSEKEEPER"; break;
            case 4: position = "MAINTENANCE"; break;
            case 5: position = "CHEF"; break;
            default:
                System.out.println("Invalid choice. No changes made.");
                return;
        }
        
        employee.setPosition(position);
        hotelService.updateEmployee(employee);
        
        auditService.logAction("Changed position of employee " + employee.getName() + " to " + position);
        System.out.println("Employee role updated successfully!");
    }
    
    // Helper methods
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
    
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
    
    private static LocalDate getDateInput(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return LocalDate.parse(input, formatter);
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
            }
        }
    }
    
    // Initialize test data
    private static void initializeTestData() {
        // Add some rooms
        Room room1 = new Room(101, RoomType.STANDARD, 100.0, RoomStatus.AVAILABLE, 2);
        room1.addAmenity("TV");
        room1.addAmenity("WiFi");
        room1.addAmenity("Air Conditioning");
        
        Room room2 = new Room(102, RoomType.DELUXE, 150.0, RoomStatus.AVAILABLE, 2);
        room2.addAmenity("TV");
        room2.addAmenity("WiFi");
        room2.addAmenity("Air Conditioning");
        room2.addAmenity("Mini Bar");
        
        Room room3 = new Room(201, RoomType.SUITE, 250.0, RoomStatus.AVAILABLE, 4);
        room3.addAmenity("TV");
        room3.addAmenity("WiFi");
        room3.addAmenity("Air Conditioning");
        room3.addAmenity("Mini Bar");
        room3.addAmenity("Jacuzzi");
        
        hotelService.addRoom(room1);
        hotelService.addRoom(room2);
        hotelService.addRoom(room3);
        
        // Add some guests
        Guest guest1 = new Guest("G001", "John Doe", "123-456-7890", "john@example.com", "123 Main St");
        Guest guest2 = new Guest("G002", "Jane Smith", "987-654-3210", "jane@example.com", "456 Oak Ave");
        
        hotelService.addGuest(guest1);
        hotelService.addGuest(guest2);
        
        // Add some employees
        Employee emp1 = new Employee("E001", "Alice Manager", "MANAGER", "Administration", "111-222-3333", "alice@hotel.com", "789 Pine Rd");
        Employee emp2 = new Employee("E002", "Bob Receptionist", "RECEPTIONIST", "Front Desk", "444-555-6666", "bob@hotel.com", "321 Elm St");
        
        hotelService.addEmployee(emp1);
        hotelService.addEmployee(emp2);
        
        // Add some bookings
        Booking booking1 = new Booking(
            "B001", 
            "G001", 
            101, 
            LocalDate.now().minusDays(2), 
            LocalDate.now().plusDays(3), 
            BookingStatus.CHECKED_IN,
            2
        );
        
        hotelService.addBooking(booking1);
        room1.setStatus(RoomStatus.OCCUPIED);
        hotelService.updateRoom(room1);
        
        auditService.logAction("System initialized with test data");
    }
}