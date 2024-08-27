import java.util.*;

public class StudentCourseRegistrationSystem {

    // Course class
    static class Course {
        private String code;
        private String title;
        private String description;
        private int capacity;
        private int enrolled;

        // Constructor
        public Course(String code, String title, String description, int capacity) {
            this.code = code;
            this.title = title;
            this.description = description;
            this.capacity = capacity;
            this.enrolled = 0;
        }

        // Getters and setters
        public String getCode() {
            return code;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public int getCapacity() {
            return capacity;
        }

        public int getEnrolled() {
            return enrolled;
        }

        public boolean isAvailable() {
            return enrolled < capacity;
        }

        public void enrollStudent() {
            if (isAvailable()) {
                enrolled++;
            }
        }

        public void dropStudent() {
            if (enrolled > 0) {
                enrolled--;
            }
        }

        @Override
        public String toString() {
            return String.format("%-10s %-30s %-40s %5d %10d",
                    code, title, description, capacity, enrolled);
        }
    }

    // Student class
    static class Student {
        private String id;
        private String name;
        private Set<String> registeredCourses;

        // Constructor
        public Student(String id, String name) {
            this.id = id;
            this.name = name;
            this.registeredCourses = new HashSet<>();
        }

        // Getters
        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public Set<String> getRegisteredCourses() {
            return registeredCourses;
        }

        // Register for a course
        public void registerCourse(String courseCode) {
            registeredCourses.add(courseCode);
        }

        // Drop a course
        public void dropCourse(String courseCode) {
            registeredCourses.remove(courseCode);
        }
    }

    // RegistrationSystem class
    static class RegistrationSystem {
        private Map<String, Course> courses;
        private Map<String, Student> students;

        // Constructor
        public RegistrationSystem() {
            courses = new HashMap<>();
            students = new HashMap<>();
            initializeCourses();
        }

        // Initialize some courses
        private void initializeCourses() {
            courses.put("CS101", new Course("CS101", "Introduction to Programming", "Learn the basics of programming.", 30));
            courses.put("MATH201", new Course("MATH201", "Calculus I", "An introduction to calculus.", 25));
            courses.put("BIO301", new Course("BIO301", "Biology Fundamentals", "Study the fundamentals of biology.", 20));
        }

        // Add a student
        public void addStudent(String id, String name) {
            if (students.containsKey(id)) {
                System.out.println("Student with this ID already exists.");
                return;
            }
            Student newStudent = new Student(id, name);
            students.put(id, newStudent);

            // Display available courses for selection
            displayCourses();
            Scanner scanner = new Scanner(System.in);

            System.out.println("Select courses for registration (enter course codes separated by commas):");
            String[] courseCodes = scanner.nextLine().split(",");

            for (String courseCode : courseCodes) {
                courseCode = courseCode.trim();
                Course course = courses.get(courseCode);
                if (course != null && course.isAvailable()) {
                    newStudent.registerCourse(courseCode);
                    course.enrollStudent();
                    System.out.println("Registered for " + course.getTitle() + " successfully.");
                } else {
                    System.out.println("Course " + courseCode + " is not available or does not exist.");
                }
            }
        }

        // Display available courses
        public void displayCourses() {
            System.out.println("\nAvailable Courses:");
            System.out.printf("%-10s %-30s %-40s %5s %10s\n", "Code", "Title", "Description", "Capacity", "Enrolled");
            for (Course course : courses.values()) {
                System.out.println(course);
            }
        }

        // Register a student for a course
        public void registerStudent(String studentId, String courseCode) {
            Student student = students.get(studentId);
            Course course = courses.get(courseCode);

            if (student == null) {
                System.out.println("Student not found.");
                return;
            }

            if (course == null) {
                System.out.println("Course not found.");
                return;
            }

            if (course.isAvailable()) {
                student.registerCourse(courseCode);
                course.enrollStudent();
                System.out.println("Registration successful.");
            } else {
                System.out.println("Course is full.");
            }
        }

        // Drop a course for a student
        public void dropCourse(String studentId, String courseCode) {
            Student student = students.get(studentId);
            Course course = courses.get(courseCode);

            if (student == null) {
                System.out.println("Student not found.");
                return;
            }

            if (course == null) {
                System.out.println("Course not found.");
                return;
            }

            if (student.getRegisteredCourses().contains(courseCode)) {
                student.dropCourse(courseCode);
                course.dropStudent();
                System.out.println("Course dropped successfully.");
            } else {
                System.out.println("Student is not registered for this course.");
            }
        }

        // Display student registration
        public void displayStudentCourses(String studentId) {
            Student student = students.get(studentId);

            if (student == null) {
                System.out.println("Student not found.");
                return;
            }

            System.out.println("\n" + student.getName() + "'s Registered Courses:");
            System.out.printf("%-10s %-30s %-40s %5s %10s\n", "Code", "Title", "Description", "Capacity", "Enrolled");
            for (String courseCode : student.getRegisteredCourses()) {
                Course course = courses.get(courseCode);
                System.out.println(course);
            }
        }
    }

    // Main method
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RegistrationSystem system = new RegistrationSystem();

        while (true) {
            System.out.println("\nStudent Course Registration System");
            System.out.println("1. Register a New Student");
            System.out.println("2. Display Courses");
            System.out.println("3. Register for a Course");
            System.out.println("4. Drop a Course");
            System.out.println("5. Display Registered Courses");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter student ID: ");
                    String studentId = scanner.nextLine();
                    System.out.print("Enter student name: ");
                    String studentName = scanner.nextLine();
                    system.addStudent(studentId, studentName);
                    break;
                case 2:
                    system.displayCourses();
                    break;
                case 3:
                    System.out.print("Enter student ID: ");
                    String regStudentId = scanner.nextLine();
                    System.out.print("Enter course code: ");
                    String regCourseCode = scanner.nextLine();
                    system.registerStudent(regStudentId, regCourseCode);
                    break;
                case 4:
                    System.out.print("Enter student ID: ");
                    String dropStudentId = scanner.nextLine();
                    System.out.print("Enter course code: ");
                    String dropCourseCode = scanner.nextLine();
                    system.dropCourse(dropStudentId, dropCourseCode);
                    break;
                case 5:
                    System.out.print("Enter student ID: ");
                    String displayStudentId = scanner.nextLine();
                    system.displayStudentCourses(displayStudentId);
                    break;
                case 6:
                    System.out.println("Thank you for using the Student Course Registration System. Have a great day!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            // Ask if the user wants to continue
            System.out.print("Do you want to continue? (yes/no): ");
            String continueChoice = scanner.nextLine().trim().toLowerCase();
            if (!continueChoice.equals("yes")) {
                System.out.println("Thank you for using the Student Course Registration System. Goodbye!");
                break;
            }
        }

        scanner.close();
    }
}
