/**
 * Represents an abstract library item that can be borrowed by users.
 * This class serves as the base for all types of items in the library.
 * Each library item has a unique ID, name, type, and keeps track of its borrowing status.
 *
 * @author Yusuf Mert Yıldız
 * @see ItemTypes
 * @see User
 */
public abstract class LibraryItem {
    private final int uniqueID;
    private String name;
    private ItemTypes type;

    private boolean isBorrowed = false;
    private String borrowDate;
    private User whoBorrowed;

    /**
     * Constructs a new LibraryItem with the specified attributes.
     *
     * @param uniqueId  the unique identifier for the library item
     * @param name      the name or title of the library item
     * @param type      the type of the library item, defined by the ItemTypes enum
     */
    public LibraryItem(int uniqueId, String name, ItemTypes type) {
        this.uniqueID = uniqueId;
        this.name = name;
        this.type = type;
    }

    /**
     * Returns the unique identifier of this library item.
     *
     * @return the unique identifier
     */
    public int getUniqueId() {
        return uniqueID;
    }

    /**
     * Returns the name or title of this library item.
     *
     * @return the name of the item
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name or title of this library item.
     *
     * @param name  the new name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the type of this library item.
     *
     * @return the item type as defined by ItemTypes enum
     */
    public ItemTypes getType() {
        return type;
    }

    /**
     * Sets the ItemTypes of this library item.
     *
     * @param type  the new ItemType to set
     */
    public void setType(ItemTypes type) {
        this.type = type;
    }

    /**
     * Checks if this library item is currently borrowed.
     *
     * @return true if the item is borrowed, false otherwise
     */
    public boolean isBorrowed() {
        return isBorrowed;
    }

    /**
     * Sets the borrowed status of this library item.
     *
     * @param borrowed  the new borrowed status
     */
    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    /**
     * Returns the date when this library item was borrowed.
     *
     * @return the borrow date as a string
     */
    public String getBorrowDate() {
        return borrowDate;
    }

    /**
     * Sets the date when this library item was borrowed.
     *
     * @param borrowDate  the borrow date to set
     */
    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    /**
     * Returns the user who borrowed this library item.
     *
     * @return the user who borrowed the item, or null if the item is not borrowed
     */
    public User getWhoBorrowed() {
        return whoBorrowed;
    }

    /**
     * Sets the user who borrowed this library item.
     *
     * @param whoBorrowed  the user who borrowed the item
     */
    public void setWhoBorrowed(User whoBorrowed) {
        this.whoBorrowed = whoBorrowed;
    }

    /**
     * Returns a string representation of this library item.
     * The string includes item ID, name, and borrowing status information.
     *
     * @return a string representation of the library item
     */
    @Override
    public String toString() {
        String firstLine = "------ Item Information for "+this.getUniqueId()+" ------\n";
        String status = "";

        if (this.isBorrowed) {
            status = " Status: Borrowed Borrowed Date: " + this.getBorrowDate() + " Borrowed by: "+this.getWhoBorrowed().getName()+"\n";
        }
        else
        {
            status = " Status: Available\n";
        }
        return firstLine+"ID: "+this.getUniqueId()+" Name: "+this.getName()+status;
    }
}

/**
 * Represents a magazine in the library system.
 * Magazines are library items with additional information such as publisher and category.
 *
 * @author Yusuf Mert Yıldız
 * @see LibraryItem
 * @see ItemTypes
 */
class Magazine extends LibraryItem {
    private String publisher;
    private String category;

    /**
     * Constructs a new Magazine with the specified attributes.
     *
     * @param id         the unique identifier for the magazine
     * @param name       the title of the magazine
     * @param publisher  the publisher of the magazine
     * @param category   the category of the magazine
     * @param type       the type of the magazine as defined by ItemTypes enum
     */
    public Magazine(int id, String name, String publisher, String category, ItemTypes type) {
        super(id, name, type);
        this.publisher = publisher;
        this.category = category;
    }

    /**
     * Returns a string representation of this magazine.
     * The string includes basic library item information (from the parent class)
     * as well as publisher and category information.
     *
     * @return a string representation of the magazine
     */
    @Override
    public String toString() {
        return super.toString() + "Publisher: " +
                this.getPublisher() + " Category: " + this.getCategory();
    }

    /**
     * Returns the publisher of this magazine.
     *
     * @return the publisher's name
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Sets the pubşisher of this Magazine.
     *
     * @param publisher  the new publisher to set
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    /**
     * Returns the category of this magazine.
     *
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of this Magazine.
     *
     * @param category  the new category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }
}

/**
 * Represents a book in the library system.
 * Books are library items with additional information such as author and genre.
 *
 * @author Yusuf Mert Yıldız
 * @see LibraryItem
 * @see ItemTypes
 */
class Book extends LibraryItem {
    private String author;
    private String genre;

    /**
     * Constructs a new Book with the specified attributes.
     *
     * @param uniqueId  the unique identifier for the book
     * @param name      the title of the book
     * @param author    the author of the book
     * @param genre     the genre of the book
     * @param type      the type of the book as defined by ItemTypes enum
     */
    public Book(int uniqueId, String name, String author, String genre, ItemTypes type) {
        super(uniqueId, name, type);
        this.author = author;
        this.genre = genre;
    }

    /**
     * Returns a string representation of this book.
     * The string includes basic library item information (from the parent class)
     * as well as author and genre information.
     *
     * @return a string representation of the book
     */
    @Override
    public String toString() {
        return super.toString() + "Author: " + this.getAuthor() + " Genre: " + getGenre();
    }

    /**
     * Returns the author of this book.
     *
     * @return the author's name
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of this book.
     *
     * @param author the new author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Returns the genre of this book.
     *
     * @return the genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Sets the genre of this book.
     *
     * @param genre the new author to set
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }
}

/**
 * Represents a DVD in the library system.
 * DVDs are library items with additional information such as directory, category, and runtime.
 *
 * @author Yusuf Mert Yıldız
 * @see LibraryItem
 * @see ItemTypes
 */
class DVD extends LibraryItem {
    private String directory;
    private String category;
    private String runtime;

    /**
     * Constructs a new DVD with the specified attributes.
     *
     * @param id         the unique identifier for the DVD
     * @param name       the title of the DVD
     * @param directory  the directory/director of the DVD
     * @param category   the category of the DVD
     * @param runtime    the runtime of the DVD
     * @param type       the type of the DVD as defined by ItemTypes enum
     */
    public DVD(int id, String name, String directory, String category, String runtime, ItemTypes type) {
        super(id, name, type);
        this.directory = directory;
        this.category = category;
        this.runtime = runtime;
    }

    /**
     * Returns a string representation of this DVD.
     * The string includes basic library item information (from the parent class)
     * as well as director, category, and runtime information.
     *
     * @return a string representation of the DVD
     */
    @Override
    public String toString() {
        return super.toString() + "Director: " + this.getDirectory() + " Category: " + this.getCategory() + " Runtime: " + this.getRuntime();
    }

    /**
     * Returns the directory/director of this DVD.
     *
     * @return the director's name
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * Sets the directory of this DVD.
     *
     * @param directory the new directory to set
     */
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    /**
     * Returns the category of this DVD.
     *
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of this DVD.
     *
     * @param category the new category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Returns the runtime of this DVD.
     *
     * @return the runtime
     */
    public String getRuntime() {
        return runtime;
    }

    /**
     * Sets the runtime of this DVD.
     *
     * @param runtime the new runtime to set
     */
    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }
}

/**
 * Defines the different types of library items.
 * Each type may have different borrowing rules and restrictions.
 */
enum ItemTypes{
    /** Rare items that may have special borrowing restrictions */
    rare,
    /** Limited availability items with special borrowing conditions */
    limited,
    /** Reference items typically not allowed to be borrowed */
    reference,
    /** Regular items with standard borrowing rules */
    normal
}
