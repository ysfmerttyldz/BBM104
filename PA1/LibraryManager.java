import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Manages a library system with users and library items.
 * Handles operations like borrowing, returning items, and managing penalties.
 */
public class LibraryManager {
    /** Stores users mapped by their unique IDs. */
    private Map<Integer,User> users = new TreeMap<>();

    /** Stores library items mapped by their unique IDs. */
    private Map<Integer,LibraryItem> items = new TreeMap<>();

    /** PrintWriter for writing output to a file instead of console */
    private PrintWriter outputWriter;

    /**
     * Constructs a LibraryManager with initial data from text files.
     *
     * @param userstxtpath Path to the file containing user data
     * @param itemtxtpath Path to the file containing library item data
     * @param commandstxt Path to the file containing commands to be processed
     * @param outputpath Path to the file will write the results
     */
    public LibraryManager(String userstxtpath, String itemtxtpath, String commandstxt,String outputpath) {
        try {
            // Initialize the PrintWriter for writing to output.txt
            outputWriter = new PrintWriter(new FileWriter(outputpath));
        } catch (IOException e) {
            e.printStackTrace();
            // If we can't open the output file, fall back to console output
            System.err.println("Could not open output.txt for writing. Output will be printed to console.");
            outputWriter = new PrintWriter(System.out, true);
        }

        readFile(userstxtpath, line -> {
            String[] parts = line.split(",");
            switch (parts[0]) {
                case "S":
                    return new Student(parts[1], Integer.parseInt(parts[2]), parts[3], parts[4], parts[5], parts[6] + "th");
                case "A":
                    return new AcademicMember(parts[1], Integer.parseInt(parts[2]), parts[3], parts[4], parts[5], parts[6]);
                case "G":
                    return new Guest(parts[1], Integer.parseInt(parts[2]), parts[3], parts[4]);
                default:
                    throw new IllegalArgumentException("Unknown user type: " + parts[0]);
            }
        }, user -> users.put(user.getUniqueID(),user));

        readFile(itemtxtpath, line -> {
            String[] parts = line.split(",");
            switch (parts[0]) {
                case "B":
                    return new Book(Integer.parseInt(parts[1]), parts[2], parts[3], parts[4], ItemTypes.valueOf(parts[5].toLowerCase()));
                case "M":
                    return new Magazine(Integer.parseInt(parts[1]), parts[2], parts[3], parts[4], ItemTypes.valueOf(parts[5].toLowerCase()));
                case "D":
                    return new DVD(Integer.parseInt(parts[1]), parts[2], parts[3], parts[4], parts[5],ItemTypes.valueOf(parts[6].toLowerCase()));
                default:
                    throw new IllegalArgumentException("Unknown user type: " + parts[0]);
            }
        }, libraryItem -> items.put(libraryItem.getUniqueId(),libraryItem));
    }

    /**
     * Returns an unmodifiable map of all users in the system.
     *
     * @return Unmodifiable map of users with user IDs as keys
     */
    public Map<Integer,User> getUsers() {
        return Collections.unmodifiableMap(users);
    }

    /**
     * Returns an unmodifiable map of all library items in the system.
     *
     * @return Unmodifiable map of library items with item IDs as keys
     */
    public Map<Integer,LibraryItem> getItems() {
        return Collections.unmodifiableMap(items);
    }

    /**
     * Displays all users in the system to the output file.
     */
    public void displayUsers(){
        outputWriter.println("\n");
        int size = users.size();
        int i = 0;
        for (User user : users.values()) {
            outputWriter.println(user);
            if (i < size - 1) {
                outputWriter.println();
                i++;
            }
        }
        outputWriter.flush(); // Make sure output is written immediately
    }

    /**
     * Displays all library items in the system to the output file.
     */
    public void displayItems() {
        outputWriter.println("\n");
        int size = items.size();
        int i = 0;
        for (LibraryItem item : items.values()) {
            outputWriter.println(item);
            if (++i < size) {
                outputWriter.println();
            }
        }
        outputWriter.flush(); // Make sure output is written immediately
    }

    /**
     * Generic method to read data from a file, convert each line to an object,
     * and process that object with a consumer.
     *
     * @param <T> The type of object to create from each line
     * @param input Path to the input file
     * @param converter Function to convert a line of text to an object of type T
     * @param consumer Consumer to process each created object
     */
    private <T> void readFile(String input, Function<String, T> converter, Consumer<T> consumer) {
        try (BufferedReader br = new BufferedReader(new FileReader(input))) {
            String line;
            while ((line = br.readLine()) != null) {
                T item = converter.apply(line);
                consumer.accept(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows a user to borrow a library item.
     * Checks various conditions like item availability, user penalties,
     * item type restrictions, and borrowing limits before allowing the borrow.
     *
     * @param whoBorrowed ID of the user borrowing the item
     * @param itemId ID of the item to be borrowed
     * @param date Date when the item is borrowed (format: DD/MM/YYYY)
     */
    private void processBorrowingTransaction(int whoBorrowed, int itemId, String date){
        controllItemsForOverdue(date);

        User user = users.get(whoBorrowed);
        LibraryItem item = items.get(itemId);


        if (items.get(itemId).isBorrowed())
        {
            outputWriter.println(user.getName()+" cannot borrow "+item.getName() +", it is not available!");
            outputWriter.flush();
            return;
        }

        if (user.getPenalty()>=6)
        {
            outputWriter.println(user.getName()+" cannot borrow "+ item.getName()+", you must first pay the penalty amount! 6$");
            outputWriter.flush();
            return;
        }

        if (user.getRestrictedItemTypes().contains(item.getType()))
        {
            outputWriter.println(user.getName()+" cannot borrow "+item.getType()+" item!");
            outputWriter.flush();
            return;
        }

        if(user.getBorrowedItems().size()==user.getMaxItems()) {
            outputWriter.println(user.getName()+" cannot borrow " + item.getName()+", since the borrow limit has been reached!");
            outputWriter.flush();
            return;
        }

        user.addBorrowedItem(item);
        item.setBorrowed(true);
        item.setBorrowDate(date);
        item.setWhoBorrowed(user);
        outputWriter.println(user.getName() + " successfully borrowed! "+ item.getName());
        outputWriter.flush();
    }

    /**
     * Processes the return of a borrowed item.
     *
     * @param whoReturned ID of the user returning the item
     * @param itemId ID of the item being returned
     */
    private void processReturnTransaction(int whoReturned, int itemId){
        User user = users.get(whoReturned);
        LibraryItem item = items.get(itemId);
        if(user.getBorrowedItems().contains(item))
        {
            user.removeBorrowedItem(item);
            item.setBorrowed(false);
            outputWriter.println(user.getName()+" successfully returned "+item.getName());
            outputWriter.flush();
        }
    }

    /**
     * Resets the penalty amount for a user after payment.
     *
     * @param userId ID of the user paying the penalty
     */
    private void processPayTransaction(int userId){
        users.get(userId).setPenalty(0);
        outputWriter.println(users.get(userId).getName()+" has paid penalty");
        outputWriter.flush();
    }

    /**
     * Checks all borrowed items for overdue status and applies penalties.
     * Items that are overdue are automatically returned and a penalty fee is applied.
     *
     * @param todaysDate Current date in format DD/MM/YYYY
     */
    private void controllItemsForOverdue(String todaysDate) {
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            User user = entry.getValue();

            for (int i = user.getBorrowedItems().size() - 1; i >= 0; i--) {
                LibraryItem item = user.getBorrowedItems().get(i);
                if (item.isBorrowed()) {
                    LocalDate bugun = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    int daysBetween = getDaysBetween(item.getBorrowDate(),bugun.format(formatter))+1;
                    if (daysBetween > user.getOverdue()) {
                        user.setPenalty(user.getPenalty() + 2);
                        user.removeBorrowedItem(item);
                        item.setBorrowed(false);
                    }
                }
            }
        }
    }

    /**
     * Processes commands from a file to perform library operations.
     * Supported commands: displayUsers, displayItems, borrow, return, pay.
     *
     * @param commandspath Path to the file containing commands
     */
    public void processCommands(String commandspath){
        try (BufferedReader br = new BufferedReader(new FileReader(
                commandspath))) {
            String line;
            while((line = br.readLine()) != null){
                String[] parts = line.split(",");
                switch (parts[0]) {
                    case "displayUsers":
                        displayUsers();
                        break;
                    case "displayItems":
                        displayItems();
                        break;
                    case "borrow":
                        processBorrowingTransaction(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]),parts[3]);
                        break;
                    case "return":
                        processReturnTransaction(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]));
                        break;
                    case "pay":
                        processPayTransaction(Integer.parseInt(parts[1]));
                        break;
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculates the number of days between two dates.
     *
     * @param date1 First date in format DD/MM/YYYY
     * @param date2 Second date in format DD/MM/YYYY
     * @return Number of days between the two dates
     */
    public static int getDaysBetween(String date1,String date2) {
        String[] parseDate = date1.split("/");
        LocalDate localDate1 = LocalDate.of(Integer.parseInt(parseDate[2]), Integer.parseInt(parseDate[1]), Integer.parseInt(parseDate[0]));

        String[] parseDate2 = date2.split("/");
        LocalDate localDate2 = LocalDate.of(Integer.parseInt(parseDate2[2]), Integer.parseInt(parseDate2[1]), Integer.parseInt(parseDate2[0]));
        return (int) ChronoUnit.DAYS.between(localDate1,localDate2);
    }
}