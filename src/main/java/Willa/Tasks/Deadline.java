package Willa.Tasks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import Willa.Exception.WillaException;

public class Deadline extends Task {
    protected LocalDateTime by;
    private static final DateTimeFormatter PARSE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd[ HHmm]");
    private static final DateTimeFormatter DATE_ONLY_DISPLAY = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static final DateTimeFormatter DATE_TIME_DISPLAY = DateTimeFormatter.ofPattern("MMM dd yyyy hh:mm a");
    
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }
    
    public static LocalDateTime parseBy(String byStr) throws WillaException {
        try {
            TemporalAccessor accessor = PARSE_FORMATTER.parseBest(byStr.trim(),
                    LocalDateTime::from, LocalDate::from);
            
            if (accessor instanceof LocalDateTime) {
                return (LocalDateTime) accessor;
            }
            return ((LocalDate) accessor).atStartOfDay();
        } catch (DateTimeParseException e) {
            throw new WillaException("Invalid date format! Use: yyyy-MM-dd or yyyy-MM-dd HHmm");
        }
    }
    
    public String getByAsString() {
        if (by.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return by.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return by.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
    }
    
    @Override
    public String toString() {
        boolean isMidnight = by.toLocalTime().equals(LocalTime.MIDNIGHT);
        String formattedBy = by.format(isMidnight ? DATE_ONLY_DISPLAY : DATE_TIME_DISPLAY);
        return "[D]" + super.toString() + " (by: " + formattedBy + ")";
    }
}
