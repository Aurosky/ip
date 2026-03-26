package Willa.Tasks;

/**
 * Base class for all task types, defining common properties and behaviors.
 * Includes task descriptions, completion status, and common methods.
 */
public class Task {
    /** task description */
    protected String description;
    /** Task completion status: true indicates completed, false indicates incomplete */
    protected boolean isDone;
    
    /**
     * Constructs a Task instance, initializes the task description, sets the completion status to incomplete.
     * @param description The specific description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }
    
    /**
     * Retrieves the status icon for a task to display in the console.
     * @return "X" indicates completed, space indicates incomplete
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }
    
    /**
     * Marks the task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }
    
    /**
     * Marks the task as incompleted.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }
    
    public String getDescription() {
        return description;
    }
    
    public boolean isDone() {
        return isDone;
    }
    
    /**
     * Override the toString method to return the task's string representation (including status icon and description).
     * @return A formatted task string in the format "[status icon] description".
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
