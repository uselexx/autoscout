package at.technikum;

public class AutoScout24Record {
    // Example fields
    public String id;
    public String title;
    public String description;
    // ... other fields

    // Implement a toString method if necessary
    @Override
    public String toString() {
        // Return a string representation of the record
        return id + " - " + title; // Customize the format as needed
    }
}
