public class Employee {
    private String employeeId;
    private String name;
    private String position;
    private String department;
    private String contactNumber;
    private String email;
    private String address;
    
    public Employee(String employeeId, String name, String position, String department, 
                   String contactNumber, String email, String address) {
        this.employeeId = employeeId;
        this.name = name;
        this.position = position;
        this.department = department;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
    }
    
    public String getEmployeeId() {
        return employeeId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
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
    
    public void assignTask(String task) {
        System.out.println("Task assigned to " + name + ": " + task);
    }
    
    public void updateSchedule(String schedule) {
        System.out.println("Schedule updated for " + name + ": " + schedule);
    }
}