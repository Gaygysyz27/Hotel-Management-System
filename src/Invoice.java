import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Invoice {
    private String invoiceId;
    private String bookingId;
    private double amount;
    private LocalDate issueDate;
    private LocalDate paymentDate;
    private String paymentStatus;
    private String paymentMethod;
    private Map<String, Double> itemizedCharges;
    
    public Invoice(String invoiceId, String bookingId, double amount, LocalDate issueDate, String paymentStatus) {
        this.invoiceId = invoiceId;
        this.bookingId = bookingId;
        this.amount = amount;
        this.issueDate = issueDate;
        this.paymentStatus = paymentStatus;
        this.itemizedCharges = new HashMap<>();
    }
    
    public String getInvoiceId() {
        return invoiceId;
    }
    
    public String getBookingId() {
        return bookingId;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public LocalDate getIssueDate() {
        return issueDate;
    }
    
    public LocalDate getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public String getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public Map<String, Double> getItemizedCharges() {
        return itemizedCharges;
    }
    
    public void addCharge(String description, double amount) {
        itemizedCharges.put(description, amount);
    }
    
    public void calculateTotal() {
        double total = 0;
        for (double charge : itemizedCharges.values()) {
            total += charge;
        }
        this.amount = total;
    }
    
    public void applyTax(double taxRate) {
        double tax = amount * taxRate;
        amount += tax;
        itemizedCharges.put("Tax (" + (taxRate * 100) + "%)", tax);
    }
    
    public void markAsPaid() {
        this.paymentStatus = "Paid";
        this.paymentDate = LocalDate.now();
    }
}