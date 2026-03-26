package Willa.Tasks;

import Willa.Exception.WillaException;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;

/**
 * Represents a task that occurs within a specific time period.
 * Stores both start and end times as LocalDateTime objects.
 */
public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;
    
    private static final DateTimeFormatter PARSE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd[ HHmm]");
    private static final DateTimeFormatter DATE_ONLY_DISPLAY = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static final DateTimeFormatter DATE_TIME_DISPLAY = DateTimeFormatter.ofPattern("MMM dd yyyy hh:mm a");
    
    /**
     * Constructs an Event task with a description, start time, and end time.
     * @param description The task description.
     * @param from The start date/time.
     * @param to The end date/time.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }
    
    /**
     * Parses a string into a LocalDateTime object, defaulting to start of day if time is omitted.
     * @param str The raw date/time string.
     * @return A LocalDateTime object.
     * @throws WillaException If the format is invalid.
     */
    public static LocalDateTime parseDateTime(String str) throws WillaException {
        try {
            TemporalAccessor acc = PARSE_FORMATTER.parseBest(str.trim(), LocalDateTime::from, LocalDate::from);
            return (acc instanceof LocalDateTime) ? (LocalDateTime) acc : ((LocalDate) acc).atStartOfDay();
        } catch (DateTimeParseException e) {
            throw new WillaException("Invalid date format! Use: yyyy-MM-dd or yyyy-MM-dd HHmm");
        }
    }
    
    /**
     * Helper method to format date-time for display based on whether time is midnight.
     * @param dateTime The LocalDateTime to format.
     * @return User-friendly formatted string.
     */
    private String formatDateTime(LocalDateTime dateTime) {
        boolean isMidnight = dateTime.toLocalTime().equals(LocalTime.MIDNIGHT);
        return dateTime.format(isMidnight ? DATE_ONLY_DISPLAY : DATE_TIME_DISPLAY);
    }
    
    /**
     * Converts the start time back to a string for file storage.
     * @return Formatted string (yyyy-MM-dd or yyyy-MM-dd HHmm).
     */
    public String getFromAsString() {
        return from.toLocalTime().equals(LocalTime.MIDNIGHT)
                ? from.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                : from.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
    }
    
    /**
     * Converts the end time back to a string for file storage.
     * @return Formatted string (yyyy-MM-dd or yyyy-MM-dd HHmm).
     */
    public String getToAsString() {
        return to.toLocalTime().equals(LocalTime.MIDNIGHT)
                ? to.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                : to.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
    }
    
    /**
     * Returns a formatted string representation of the event task.
     * @return The formatted task string with date/time range.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + formatDateTime(from)
                + " to: " + formatDateTime(to) + ")";
    }
}
