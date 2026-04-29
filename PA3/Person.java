import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for any person in the system, holding common attributes.
 */
public abstract class Person implements IPerson {
    /** Unique identifier for the person */
    private String id;
    /** Full name of the person */
    private String name;
    /** Email address */
    private String email;
    /** Academic major or department */
    private Major major;

    /**
     * Constructs a new Person.
     *
     * @param id    unique ID
     * @param name  full name
     * @param email email address
     * @param major academic major
     */
    public Person(String id, String name, String email, Major major) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.major = major;
    }

    /** {@inheritDoc} */
    @Override
    public String getID() {
        return this.id;
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return this.name;
    }

    /** {@inheritDoc} */
    @Override
    public String getEmail() {
        return this.email;
    }

    /** {@inheritDoc} */
    @Override
    public Major getMajor() {
        return this.major;
    }
}

/**
 * Represents a faculty member who can teach courses.
 */
class AcademicMember extends Person implements IReportable {
    /** List of courses this member teaches */
    private final ArrayList<Course> teachingCourses = new ArrayList<>();
    /** Last assigned course (if any) */
    private Course instructorOfTheCourse;

    /**
     * Constructs a new AcademicMember.
     *
     * @param id    unique ID
     * @param name  full name
     * @param email email address
     * @param major associated department or major
     */
    public AcademicMember(String id, String name, String email, Major major) {
        super(id, name, email, major);
    }

    /**
     * Adds a teaching assignment for this member.
     *
     * @param courseCode code of the course to teach
     * @throws NonExistentCourse if the course is not found
     */
    public void AddTeachingCourse(String courseCode) throws NonExistentCourse {
        if (!StudentManagementSystem.courses.containsKey(courseCode)) {
            throw new NonExistentCourse("Course " + courseCode + " Not Found");
        }
        teachingCourses.add(StudentManagementSystem.courses.get(courseCode));
    }

    /** {@inheritDoc} */
    @Override
    public String generateReport() {
        return "Faculty ID: " + getID() + "\n" +
                "Name: " + getName() + "\n" +
                "Email: " + getEmail() + "\n" +
                "Department: " + getMajor();
    }
}

/**
 * Represents a student, tracks enrolled and completed courses.
 */
class Student extends Person implements IReportable {
    /** Courses the student is currently enrolled in */
    private ArrayList<Course> enrolledCourse = new ArrayList<>();
    /** Completed courses with their letter grades */
    private Map<Course, LetterGrade> completedGrades = new HashMap<>();

    /**
     * Constructs a new Student.
     *
     * @param id    unique ID
     * @param name  full name
     * @param email email address
     * @param major academic major
     */
    public Student(String id, String name, String email, Major major) {
        super(id, name, email, major);
    }

    /** {@inheritDoc} */
    @Override
    public String generateReport() {
        return "Student ID: " + getID() + "\n" +
                "Name: " + getName() + "\n" +
                "Email: " + getEmail() + "\n" +
                "Major: " + getMajor() + "\n" +
                "Status: Active";
    }

    /**
     * Returns the list of enrolled courses, sorted alphabetically by course name.
     *
     * @return sorted list of enrolled courses
     */

    public ArrayList<Course> getEnrolledCourse() {
        // sort in place by Course.getName()
        enrolledCourse.sort(Comparator.comparing(Course::getName));
        return enrolledCourse;
    }

    /**
     * Returns the map of completed courses and grades.
     *
     * @return completed courses with grades
     */
    public Map<Course, LetterGrade> getCompletedGrades() {
        return completedGrades;
    }

    /**
     * Enrolls the student in a course by its ID.
     *
     * @param courseId code of the course
     * @throws NonExistentCourse if the course is not found
     */
    public void enrollCourse(String courseId) throws NonExistentCourse {
        if (!StudentManagementSystem.courses.containsKey(courseId)) {
            throw new NonExistentCourse("Course " + courseId + " Not Found");
        }
        enrolledCourse.add(StudentManagementSystem.courses.get(courseId));
    }

    /**
     * Calculates the GPA weighted by course credits.
     *
     * @return weighted average score
     */
    public double getAverageGrade() {
        double sum = 0.0;
        int count = 0;
        for (Map.Entry<Course, LetterGrade> entry : completedGrades.entrySet()) {
            Course c = entry.getKey();
            LetterGrade l = entry.getValue();
            sum += l.getLetterScore() * c.getTotalCredits();
            count += c.getTotalCredits();
        }
        if (count == 0) {
            return 0.00;
        }
        return sum / count;
    }
}

/**
 * Possible letter grades and their corresponding numeric scores.
 */
enum LetterGrade {
    A1, A2, B1, B2, C1, C2, D1, D2, F3;

    /**
     * Converts this letter grade to a numeric score.
     *
     * @return score between 4.0 and 0.0
     */
    public double getLetterScore() {
        return 4.0 - 0.5 * this.ordinal();
    }
}

/**
 * Academic majors or departments in the institution.
 */
enum Major {
    CS, AI
}
