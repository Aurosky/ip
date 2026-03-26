package Willa.Tasks;

import Willa.Exception.WillaException;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;

public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;
    
    private static final DateTimeFormatter PARSE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd[ HHmm]");
    private static final DateTimeFormatter DATE_ONLY_DISPLAY = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static final DateTimeFormatter DATE_TIME_DISPLAY = DateTimeFormatter.ofPattern("MMM dd yyyy hh:mm a");
    
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }
    
    public static LocalDateTime parseDateTime(String str) throws WillaException {
        try {
            TemporalAccessor acc = PARSE_FORMATTER.parseBest(str.trim(), LocalDateTime::from, LocalDate::from);
            return (acc instanceof LocalDateTime) ? (LocalDateTime) acc : ((LocalDate) acc).atStartOfDay();
        } catch (DateTimeParseException e) {
            throw new WillaException("Invalid date format! Use: yyyy-MM-dd or yyyy-MM-dd HHmm");
        }
    }
    
    private String formatDateTime(LocalDateTime dateTime) {
        boolean isMidnight = dateTime.toLocalTime().equals(LocalTime.MIDNIGHT);
        return dateTime.format(isMidnight ? DATE_ONLY_DISPLAY : DATE_TIME_DISPLAY);
    }
    
    public String getFromAsString() {
        return from.toLocalTime().equals(LocalTime.MIDNIGHT)
                ? from.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                : from.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
    }
    
    public String getToAsString() {
        return to.toLocalTime().equals(LocalTime.MIDNIGHT)
                ? to.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                : to.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
    }
    
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + formatDateTime(from)
                + " to: " + formatDateTime(to) + ")";
    }
}
