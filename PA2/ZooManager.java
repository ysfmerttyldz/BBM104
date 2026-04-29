import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * ZooManager handles loading of zoo data (animals, persons, food stock),
 * processes commands for visitation and feeding, and provides utility
 * methods for validating and listing data.
 *
 * <p>Data is loaded from text files, parsed, and stored in static maps
 * and a FoodStock instance. Commands are read from a file and dispatched
 * to the appropriate handlers, with error handling for missing entities
 * and unauthorized actions.</p>
 *
 * @see Animal
 * @see Person
 * @see FoodStock
 */
public class ZooManager {
    /** Map from animal name to Animal instance. */
    static Map<String, Animal> animals = new HashMap<>();
    /** Map from person ID to Person instance (Visitor or Personnel). */
    static Map<Integer, Person> persons = new HashMap<>();
    /** FoodStock holding available food quantities. */
    static FoodStock foodStock = new FoodStock();

    /**
     * Returns the map of all animals.
     *
     * @return map with animal names as keys and Animal objects as values
     */
    public static Map<String, Animal> getAnimals() {
        return animals;
    }

    /**
     * Returns the map of all persons.
     *
     * @return map with person IDs as keys and Person objects as values
     */
    public static Map<Integer, Person> getPersons() {
        return persons;
    }

    /**
     * Reads and initializes all data files, then processes commands.
     *
     * <p>Order of initialization:
     * <ol>
     *   <li>Animal information</li>
     *   <li>Visitor and Personnel information</li>
     *   <li>Food stock</li>
     * </ol>
     * Finally, the commands file is read line by line and dispatched.</p>
     *
     * @param animalTxtPath   path to the animals file
     * @param commandsTxtPath path to the commands file
     * @param foodsTxtPath    path to the foods file
     * @param personsTxtPath  path to the persons file
     */
    public static void ProcessFiles(String animalTxtPath,
                                    String commandsTxtPath,
                                    String foodsTxtPath,
                                    String personsTxtPath) {
        System.out.println("***********************************\n*** Initializing Animal information ***");
        readFile(animalTxtPath,
                line -> {
                    String[] parts = line.split(",");
                    switch (parts[0]) {
                        case "Lion":
                            return new Lion(parts[1], Integer.parseInt(parts[2]));
                        case "Elephant":
                            return new Elephant(parts[1], Integer.parseInt(parts[2]));
                        case "Chimpanzee":
                            return new Chimpanzee(parts[1], Integer.parseInt(parts[2]));
                        case "Penguin":
                            return new Penguin(parts[1], Integer.parseInt(parts[2]));
                        default:
                            return null;
                    }
                },
                animal -> {
                    if (animal != null) {
                        animals.put(animal.getName(), animal);
                        System.out.println("Added new " +
                                animal.getClass().getSimpleName() +
                                " with name " + animal.getName() +
                                " aged " + animal.getAge() + ".");
                    }
                });

        System.out.println("***********************************\n*** Initializing Visitor and Personnel information ***");
        readFile(personsTxtPath,
                line -> {
                    String[] parts = line.split(",");
                    switch (parts[0]) {
                        case "Visitor":
                            return new Visitor(parts[1], Integer.parseInt(parts[2]));
                        case "Personnel":
                            return new Personnel(parts[1], Integer.parseInt(parts[2]));
                        default:
                            return null;
                    }
                },
                person -> {
                    if (person != null) {
                        persons.put(person.getId(), person);
                        System.out.println("Added new " +
                                person.getClass().getSimpleName() +
                                " with id " + person.getId() +
                                " and name " + person.getName() + ".");
                    }
                });

        System.out.println("***********************************\n*** Initializing Food Stock ***");
        readFile(foodsTxtPath,
                line -> {
                    String[] parts = line.split(",");
                    return new Food(FoodType.valueOf(parts[0]),
                            Float.parseFloat(parts[1]));
                },
                food -> {
                    System.out.printf(Locale.US,
                            "There are %.3f kg of %s in stock%n",
                            food.getAmount(),
                            food.getFoodType());
                    foodStock.getFoods().put(food.getFoodType().name(), food);
                });

        try (BufferedReader br = new BufferedReader(new FileReader(commandsTxtPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println("***********************************\n*** Processing new Command ***");
                try {
                    String[] parts = line.split(",");
                    switch (parts[0]) {
                        case "Animal Visitation":
                            ControlPerson(Integer.parseInt(parts[1]));
                            AnimalVisitation(Integer.parseInt(parts[1]), parts[2]);
                            break;
                        case "Feed Animal":
                            ControlPerson(Integer.parseInt(parts[1]));
                            FeedAnimal(Integer.parseInt(parts[1]),
                                    parts[2],
                                    Integer.parseInt(parts[3]));
                            break;
                        case "List Food Stock":
                            ListFoodStock();
                            break;
                        default:
                            // Unknown command; skip.
                            continue;
                    }
                } catch (ZooException e) {
                    System.out.println(e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Error processing command: " + line);
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generic file reader utility.
     *
     * @param <T>        the type produced by the converter
     * @param input      path to the input file
     * @param converter  maps each line to an object of type T
     * @param consumer   processes each converted object
     */
    private static <T> void readFile(String input,
                                     Function<String, T> converter,
                                     Consumer<T> consumer) {
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
     * Instructs a person to visit an animal.
     *
     * @param id         ID of the person
     * @param animalName name of the animal to visit
     * @throws ZooException if the person or animal is not found or visit fails
     */
    public static void AnimalVisitation(int id, String animalName) throws ZooException {
        persons.get(id).visitAnimal(animalName);
    }

    /**
     * Instructs a person to feed an animal.
     *
     * @param id             ID of the person
     * @param animalName     name of the animal to feed
     * @param numberOfMeals  number of meals to give
     * @throws ZooException if the person or animal is not found or feeding fails
     */
    public static void FeedAnimal(int id, String animalName, int numberOfMeals) throws ZooException {
        persons.get(id).feedAnimal(animalName, numberOfMeals);
    }

    /**
     * Prints the current food stock levels for each food type.
     */
    public static void ListFoodStock() {
        System.out.println("Listing available Food Stock:");
        System.out.printf(Locale.US, "Plant: %.3f kgs%n", foodStock.getFoods().get("Plant").getAmount());
        System.out.printf(Locale.US, "Fish: %.3f kgs%n",  foodStock.getFoods().get("Fish").getAmount());
        System.out.printf(Locale.US, "Meat: %.3f kgs%n",  foodStock.getFoods().get("Meat").getAmount());
    }

    /**
     * Validates that an animal with the given name exists.
     *
     * @param animalName name of the animal to check
     * @throws ZooException if no such animal is registered
     */
    public static void ControlAnimal(String animalName) throws ZooException {
        if (!animals.containsKey(animalName)) {
            throw new AnimalNotFoundException(
                    "Error: There are no animals with the name " + animalName + "."
            );
        }
    }

    /**
     * Validates that a person with the given ID exists.
     *
     * @param id the person's ID to check
     * @throws ZooException if no such person is registered
     */
    public static void ControlPerson(int id) throws ZooException {
        if (!persons.containsKey(id)) {
            throw new PersonNotFoundException(
                    "Error: There are no visitors or personnel with the id " + id + "."
            );
        }
    }
}
