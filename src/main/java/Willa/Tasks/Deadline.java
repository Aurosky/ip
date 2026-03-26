package Willa.Tasks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import Willa.Exception.WillaException;

/**
 * Represents a task with a specific deadline.
 * Supports date-only and date-time formats with intelligent display logic.
 */
public class Deadline extends Task {
    protected LocalDateTime by;
    private static final DateTimeFormatter PARSE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd[ HHmm]");
    private static final DateTimeFormatter DATE_ONLY_DISPLAY = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static final DateTimeFormatter DATE_TIME_DISPLAY = DateTimeFormatter.ofPattern("MMM dd yyyy hh:mm a");
    
    /**
     * Constructs a Deadline task with the given description and time.
     * @param description The task description.
     * @param by The LocalDateTime representing the deadline.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }
    
    /**
     * Parses a string into a LocalDateTime object, supporting both date and date-time inputs.
     * @param byStr The raw date/time string (e.g., "2026-08-23" or "2026-08-23 1200").
     * @return A LocalDateTime object; defaults to the start of the day if time is missing.
     * @throws WillaException If the input does not match expected patterns.
     */
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
    
    /**
     * Converts the deadline back into a string format suitable for storage.
     * @return A formatted string (yyyy-MM-dd or yyyy-MM-dd HHmm).
     */
    public String getByAsString() {
        if (by.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return by.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        return by.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
    }
    
    /**
     * Returns a user-friendly string representation of the deadline task.
     * If the time is midnight, only the date is displayed.
     * @return The formatted task string.
     */
    @Override
    public String toString() {
        boolean isMidnight = by.toLocalTime().equals(LocalTime.MIDNIGHT);
        String formattedBy = by.format(isMidnight ? DATE_ONLY_DISPLAY : DATE_TIME_DISPLAY);
        return "[D]" + super.toString() + " (by: " + formattedBy + ")";
    }
}
