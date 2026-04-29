import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an abstract user in the library system.
 * This class serves as the base for all types of users.
 * Users can borrow items, receive penalties, and have specific borrowing restrictions.
 *
 * @author Yusuf Mert Yıldız
 * @see IBorrower
 * @see IPrintable
 * @see LibraryItem
 * @see ItemTypes
 */
public abstract class User implements IBorrower, IPrintable {
    private String name;
    private final int uniqueID;
    private String phoneNumber;
    private int penalty = 0;

    private final ArrayList<LibraryItem> borrowedItems = new ArrayList<>();
    private final ArrayList<ItemTypes> restrictedItemTypes = new ArrayList<>();

    /**
     * Abstract method to get the printable text representation of this user.
     * Must be implemented by subclasses.
     *
     * @return a string containing printable user information
     */
    public abstract String getPrintText();

    /**
     * Constructs a new User with the specified attributes.
     *
     * @param name         the name of the user
     * @param uniqueID     the unique identifier for the user
     * @param phoneNumber  the contact phone number
     */
    public User(String name, int uniqueID, String phoneNumber) {
        this.name = name;
        this.uniqueID = uniqueID;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns a string representation of this user.
     * The string includes user information and penalty information if applicable.
     *
     * @return a string representation of the user
     */
    @Override
    public String toString() {
        String firstLine = "------ User Information for "+this.getUniqueID()+" ------\n";
        String lastLine = "";
        if (this.getPenalty() > 0){
            lastLine = "\nPenalty: "+this.getPenalty()+"$";
        }
        return firstLine + this.getPrintText() + lastLine;
    }

    /**
     * Returns the name of this user.
     *
     * @return the user name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this user.
     *
     * @param name  the new name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the unique identifier of this user.
     *
     * @return the unique identifier
     */
    public int getUniqueID() {
        return uniqueID;
    }

    /**
     * Returns the phone number of this user.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the phoneNumber of this user.
     *
     * @param phoneNumber  the new phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Returns the current penalty amount for this user.
     *
     * @return the penalty amount
     */
    public int getPenalty() {
        return penalty;
    }

    /**
     * Sets the penalty amount for this user.
     *
     * @param penalty  the new penalty amount to set
     */
    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }

    /**
     * Returns the list of items currently borrowed by this user.
     *
     * @return an ArrayList of borrowed library items
     */
    public ArrayList<LibraryItem> getBorrowedItems() {
        return borrowedItems;
    }

    /**
     * Returns an unmodifiable list of item types that this user is restricted from borrowing.
     *
     * @return an unmodifiable list of restricted ItemTypes
     */
    public List<ItemTypes> getRestrictedItemTypes() {
        return Collections.unmodifiableList(restrictedItemTypes);
    }

    /**
     * Adds a restricted item type to this user's list of restricted item types.
     *
     * @param itemType  the item type to add to restricted item types
     */
    public void addRestrictedItemType(ItemTypes itemType) {
        restrictedItemTypes.add(itemType);
    }

    /**
     * Removes a restricted item type from this user's list of restricted item types.
     *
     * @param itemType  the item type to remove from restricted item types
     */
    public void removeRestrictedItemType(ItemTypes itemType) {
        restrictedItemTypes.remove(itemType);
    }

    /**
     * Adds a library item to this user's list of borrowed items.
     *
     * @param item  the library item to add to borrowed items
     */
    public void addBorrowedItem(LibraryItem item) {
        borrowedItems.add(item);
    }

    /**
     * Removes a library item from this user's list of borrowed items.
     *
     * @param item  the library item to remove from borrowed items
     */
    public void removeBorrowedItem(LibraryItem item) {
        borrowedItems.remove(item);
    }
}

/**
 * Represents an academic user in the library system.
 * This abstract class extends the base User class with academic-specific information.
 * Academic users belong to a specific faculty and department.
 *
 * @author Yusuf Mert Yıldız
 * @see User
 */
abstract class AcademicUser extends User {
    private String faculty;
    private String department;

    /**
     * Constructs a new AcademicUser with the specified attributes.
     *
     * @param name         the name of the academic user
     * @param uniqueID     the unique identifier for the academic user
     * @param phoneNumber  the contact phone number
     * @param department   the department to which the academic user belongs
     * @param faculty      the faculty to which the academic user belongs
     */
    public AcademicUser(String name, int uniqueID, String phoneNumber, String department, String faculty) {
        super(name, uniqueID, phoneNumber);
        this.department = department;
        this.faculty = faculty;
    }

    /**
     * Returns the faculty to which this academic user belongs.
     *
     * @return the faculty name
     */
    public String getFaculty() {
        return faculty;
    }

    /**
     * Sets the faculty of this Academic User.
     *
     * @param faculty the new faculty to set
     */
    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    /**
     * Returns the department to which this academic user belongs.
     *
     * @return the department name
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department of this Academic User.
     *
     * @param department the new department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }
}

/**
 * Represents an academic staff member in the library system.
 * Academic members are academic users with faculty titles and specific borrowing privileges.
 * They can borrow a maximum of 3 items for up to 15 days, with no specific item type restrictions.
 *
 * @author Yusuf Mert Yıldız
 * @see AcademicUser
 */
class AcademicMember extends AcademicUser {
    private static final int MAX_ITEMS = 3;
    private static final int OVERDUE = 15;

    private String title;

    /**
     * Constructs a new AcademicMember with the specified attributes.
     *
     * @param name         the name of the academic member
     * @param uniqueID     the unique identifier for the academic member
     * @param phoneNumber  the contact phone number
     * @param department   the department to which the academic member belongs
     * @param faculty      the faculty to which the academic member belongs
     * @param title        the academic title of the member (e.g., Prof., Dr., etc.)
     */
    public AcademicMember(String name, int uniqueID, String phoneNumber, String department, String faculty, String title) {
        super(name, uniqueID, phoneNumber, department, faculty);
        this.title = title;
    }

    /**
     * Returns the maximum number of items this academic member can borrow at the same time.
     *
     * @return the maximum number of items (3 for academic members)
     */
    @Override
    public int getMaxItems() {
        return MAX_ITEMS;
    }

    /**
     * Returns the number of days after which a borrowed item is considered overdue.
     *
     * @return the overdue period in days (15 for academic members)
     */
    @Override
    public int getOverdue() {
        return OVERDUE;
    }

    /**
     * Returns a formatted string with academic member information for printing.
     * Includes title, name, phone number, faculty, and department.
     *
     * @return a string containing academic member information
     */
    @Override
    public String getPrintText() {
        return "Name: " + this.getTitle() + " " + this.getName() + " Phone: " + this.getPhoneNumber() + "\n" +
                "Faculty: " + this.getFaculty() + " Department: " + this.getDepartment();
    }

    /**
     * Returns the academic title of this member.
     *
     * @return the academic title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this Academic Member.
     *
     * @param title the new title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
}

/**
 * Represents a student user in the library system.
 * Students are academic users with their own set of borrowing privileges.
 * They can borrow a maximum of 5 items for up to 30 days, and are restricted from
 * borrowing reference items.
 *
 * @author Yusuf Mert Yıldız
 * @see AcademicUser
 * @see ItemTypes
 */
class Student extends AcademicUser {
    private static final int MAX_ITEMS = 5;
    private static final int OVERDUE = 30;

    private String grade;

    /**
     * Constructs a new Student user with the specified attributes.
     * Initializes the restricted item types to include reference items.
     *
     * @param name         the name of the student
     * @param uniqueID     the unique identifier for the student
     * @param phoneNumber  the contact phone number
     * @param department   the department to which the student belongs
     * @param faculty      the faculty to which the student belongs
     * @param grade        the grade or academic year of the student
     */
    public Student(String name, int uniqueID, String phoneNumber, String department, String faculty, String grade) {
        super(name, uniqueID, phoneNumber, department, faculty);
        this.grade = grade;
        this.addRestrictedItemType(ItemTypes.reference);
    }

    /**
     * Returns the maximum number of items this student can borrow simultaneously.
     *
     * @return the maximum number of items (5 for students)
     */
    @Override
    public int getMaxItems() {
        return MAX_ITEMS;
    }

    /**
     * Returns the number of days after which a borrowed item is considered overdue.
     *
     * @return the overdue period in days (30 for students)
     */
    @Override
    public int getOverdue() {
        return OVERDUE;
    }

    /**
     * Returns a formatted string with student information for printing.
     * Includes name, phone number, faculty, department, and grade.
     *
     * @return a string containing student information
     */
    @Override
    public String getPrintText() {
        return "Name: " + this.getName() + " Phone: " + this.getPhoneNumber() + "\n" +
                "Faculty: " + this.getFaculty() + " Department: " + this.getDepartment() + " Grade: " + this.getGrade();
    }

    /**
     * Returns the grade or academic year of this student.
     *
     * @return the grade
     */
    public String getGrade() {
        return grade;
    }

    /**
     * Sets the grade of this Student.
     *
     * @param grade the new grade to set
     */
    public void setGrade(String grade) {
        this.grade = grade;
    }
}

/**
 * Represents a guest user in the library system.
 * Guests have the most limited borrowing privileges compared to other user types.
 * They can borrow a maximum of 1 item for up to 7 days, and are restricted from
 * borrowing rare and limited items.
 *
 * @author Yusuf Mert Yıldız
 * @see User
 * @see ItemTypes
 */
class Guest extends User {
    private static final int MAX_ITEMS = 1;
    private static final int OVERDUE = 7;

    private String occupation;

    /**
     * Constructs a new Guest user with the specified attributes.
     * Initializes the restricted item types to include rare and limited items.
     *
     * @param name         the name of the guest
     * @param uniqueID     the unique identifier for the guest
     * @param phoneNumber  the contact phone number
     * @param occupation   the occupation of the guest
     */
    public Guest(String name, int uniqueID, String phoneNumber, String occupation) {
        super(name, uniqueID, phoneNumber);
        this.occupation = occupation;

        this.addRestrictedItemType(ItemTypes.rare);
        this.addRestrictedItemType(ItemTypes.limited);
    }

    /**
     * Returns the maximum number of items this guest can borrow simultaneously.
     *
     * @return the maximum number of items (1 for guests)
     */
    @Override
    public int getMaxItems() {
        return MAX_ITEMS;
    }

    /**
     * Returns the number of days after which a borrowed item is considered overdue.
     *
     * @return the overdue period in days (7 for guests)
     */
    @Override
    public int getOverdue() {
        return OVERDUE;
    }

    /**
     * Returns a formatted string with guest information for printing.
     * Includes name, phone number, and occupation.
     *
     * @return a string containing guest information
     */
    @Override
    public String getPrintText() {
        return "Name: " + getName() + " Phone: " + getPhoneNumber() + "\n" +
                "Occupation: " + this.getOccupation();
    }

    /**
     * Returns the occupation of this guest.
     *
     * @return the occupation
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * Sets the occupation of this Guest.
     *
     * @param occupation the new occupation to set
     */
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
}

/**
 * Interface that defines the borrowing capabilities and restrictions.
 * Classes implementing this interface can borrow library items.
 */
interface IBorrower {
    /**
     * Returns the maximum number of items this borrower can check out simultaneously.
     *
     * @return the maximum number of items
     */
    int getMaxItems();

    /**
     * Returns the number of days after which an item is considered overdue.
     *
     * @return the overdue period in days
     */
    int getOverdue();

    /**
     * Returns the list of item types that this borrower is restricted from borrowing.
     *
     * @return an unmodifiable list of restricted ItemTypes
     */
    List<ItemTypes> getRestrictedItemTypes();
}

/**
 * Interface for objects that can be represented as printable text.
 */
interface IPrintable {
    /**
     * Returns a string representation suitable for printing.
     *
     * @return a printable string representation
     */
    String getPrintText();
}