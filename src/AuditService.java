import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AuditService {
    private List<String> auditLog;
    private DateTimeFormatter formatter;
    
    public AuditService() {
        auditLog = new ArrayList<>();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }
    
    public void logAction(String action) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = timestamp + " - " + action;
        auditLog.add(logEntry);
        System.out.println("[LOG] " + logEntry);
    }
    
    public List<String> getAuditLog() {
        return new ArrayList<>(auditLog);
    }
    
    public List<String> searchLogs(String keyword) {
        return auditLog.stream()
                .filter(log -> log.contains(keyword))
                .collect(java.util.stream.Collectors.toList());
    }
    
    public void generateAuditReport() {
        System.out.println("\n===== AUDIT LOG REPORT =====");
        System.out.println("Total Entries: " + auditLog.size());
        System.out.println("---------------------------");
        
        for (String log : auditLog) {
            System.out.println(log);
        }
        
        System.out.println("===========================");
    }
}