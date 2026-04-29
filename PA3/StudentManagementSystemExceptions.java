/**
 * Base exception class for the student management system.
 * All custom runtime exceptions in the system extend from this class.
 */
public class StudentManagementSystemExceptions extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message detailed error message
     */
    public StudentManagementSystemExceptions(String message) {
        super(message);
    }
}

/**
 * Thrown when an invalid person type identifier is encountered
 * while processing input (e.g., an unrecognized prefix for student or faculty).
 */
class InvalidPersonType extends StudentManagementSystemExceptions {
    /**
     * Constructs a new InvalidPersonType exception.
     *
     * @param message detailed error message
     */
    public InvalidPersonType(String message) {
        super(message);
    }
}

/**
 * Generic exception indicating that a required entity was null
 * or otherwise missing.
 */
class NonExistentException extends StudentManagementSystemExceptions {
    /**
     * Constructs a new NonExistentException.
     *
     * @param message detailed error message
     */
    public NonExistentException(String message) {
        super(message);
    }
}

/**
 * Thrown when a person-related entity (student or academic member)
 * is expected but not found.
 */
class NonExistentPerson extends NonExistentException {
    /**
     * Constructs a new NonExistentPerson exception.
     *
     * @param message detailed error message
     */
    public NonExistentPerson(String message) {
        super(message);
    }
}

/**
 * Thrown when an operation references a student ID
 * that does not exist in the system.
 */
class NonExistentStudent extends NonExistentPerson {
    /**
     * Constructs a new NonExistentStudent exception.
     *
     * @param message detailed error message
     */
    public NonExistentStudent(String message) {
        super(message);
    }
}

/**
 * Thrown when an operation references an academic member ID
 * that does not exist in the system.
 */
class NonExistentAcademicMember extends NonExistentPerson {
    /**
     * Constructs a new NonExistentAcademicMember exception.
     *
     * @param message detailed error message
     */
    public NonExistentAcademicMember(String message) {
        super(message);
    }
}

/**
 * Thrown when a program code is referenced that does not
 * correspond to any loaded program.
 */
class NonExistentProgram extends NonExistentException {
    /**
     * Constructs a new NonExistentProgram exception.
     *
     * @param message detailed error message
     */
    public NonExistentProgram(String message) {
        super(message);
    }
}

/**
 * Thrown when a course code is referenced that does not
 * correspond to any loaded course.
 */
class NonExistentCourse extends NonExistentException {
    /**
     * Constructs a new NonExistentCourse exception.
     *
     * @param message detailed error message
     */
    public NonExistentCourse(String message) {
        super(message);
    }
}

/**
 * Thrown when a department identifier is referenced that does not
 * correspond to any loaded department.
 */
class NonExistentDepartment extends NonExistentException {
    /**
     * Constructs a new NonExistentDepartment exception.
     *
     * @param message detailed error message
     */
    public NonExistentDepartment(String message) {
        super(message);
    }
}

/**
 * Thrown when an invalid letter grade value is encountered
 * in the grades input file.
 */
class InvalidLetterGrades extends StudentManagementSystemExceptions {
    /**
     * Constructs a new InvalidLetterGrades exception.
     *
     * @param message detailed error message
     */
    public InvalidLetterGrades(String message) {
        super(message);
    }
}
