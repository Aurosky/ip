# Willa Task Management Chatbot

Willa is a simple and efficient command-line task management chatbot that helps you track todos, deadlines, and events effortlessly.

## Quick Start

1. Ensure you have Java 11 or above installed on your system.
2. Compile and run the Willa.java file.
3. Once the welcome screen appears, you can start entering commands to manage your tasks.

## Features

### Add Tasks

Willa supports three types of tasks:

#### Todo
A simple task with no time constraints.

- Command: `todo <description>`
- Example: `todo Buy milk`

#### Deadline
A task that must be completed by a specific date/time.

- Command: `deadline <description> /by <date>`
- Date format: `yyyy-MM-dd` or `yyyy-MM-dd HHmm`
- Example: `deadline Submit assignment /by 2025-05-20 2359`

#### Event
A task with a start time and end time.

- Command: `event <description> /from <start> /to <end>`
- Example: `event Project meeting /from Mon 2pm /to Mon 4pm`

### List All Tasks

- Command: `list`

### Mark Task as Done

- Command: `mark <task number>`
- Example: `mark 2`

### Mark Task as Not Done

- Command: `unmark <task number>`
- Example: `unmark 2`

### Delete Task

- Command: `delete <task number>`
- Example: `delete 3`

### Find Tasks by Keyword

- Command: `find <keyword>`
- Example: `find assignment`

### Exit the Program

- Command: `bye`

## Data Storage

All tasks are automatically saved to:
`./data/willa.txt`

The file is loaded automatically when the program starts.

## Command Summary

| Command | Format |
| --- | --- |
| Add Todo | `todo DESCRIPTION` |
| Add Deadline | `deadline DESCRIPTION /by DATE` |
| Add Event | `event DESCRIPTION /from START /to END` |
| List tasks | `list` |
| Mark done | `mark INDEX` |
| Mark undone | `unmark INDEX` |
| Delete task | `delete INDEX` |
| Find tasks | `find KEYWORD` |
| Exit | `bye` |