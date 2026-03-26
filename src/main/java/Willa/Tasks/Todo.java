package Willa.Tasks;

/**
 * Task class, inheriting from the Task base class, a basic task with no time constraints.
 */
public class Todo extends Task {
    /**
     * Constructs a Todo task instance.
     * @param description The specific description of the task
     */
    public Todo(String description) {
        super(description);
    }
    
    /**
     * Override the toString method to add the Todo task type identifier [T].
     * @return A formatted Todo task string in the format “[T][status icon] description”.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
