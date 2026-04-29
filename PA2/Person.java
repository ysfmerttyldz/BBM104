/**
 * Abstract base class representing a person in the zoo system.
 * <p>
 * Each Person has a name and an ID, and can perform actions
 * such as visiting or feeding an animal. Concrete subclasses
 * must implement the visitAnimal and feedAnimal methods.
 * </p>
 *
 * @author
 */
public abstract class Person {
    private String name;
    private int id;

    /**
     * Constructs a new Person.
     *
     * @param name the person's name
     * @param id   the unique identifier for the person
     */
    public Person(String name, int id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Returns the name of this person.
     *
     * @return the person's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the ID of this person.
     *
     * @return the person's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Perform a visit action on the specified animal.
     *
     * @param animalName the name of the animal to visit
     * @throws ZooException if the animal does not exist or another error occurs
     */
    public abstract void visitAnimal(String animalName) throws ZooException;

    /**
     * Perform a feed action on the specified animal.
     *
     * @param animalName     the name of the animal to feed
     * @param numberOfMeals  the number of meals to provide
     * @throws ZooException if the animal does not exist or feeding is not permitted
     */
    public abstract void feedAnimal(String animalName, int numberOfMeals) throws ZooException;
}


/**
 * Personnel is a type of Person responsible for
 * feeding animals and cleaning their habitats.
 */
class Personnel extends Person {

    /**
     * Constructs a new Personnel.
     *
     * @param name the personnel's name
     * @param id   the unique identifier for the personnel
     */
    public Personnel(String name, int id) {
        super(name, id);
    }

    /**
     * Feeds the specified animal a given number of meals.
     * <p>
     * Before feeding, validates that the animal exists.
     * </p>
     *
     * @param animalName    the name of the animal to feed
     * @param numberOfMeals the number of meals to give
     * @throws ZooException if the animal does not exist or another feeding error occurs
     */
    @Override
    public void feedAnimal(String animalName, int numberOfMeals) throws ZooException {
        System.out.println(getName() + " attempts to feed " + animalName + ".");
        ZooManager.ControlAnimal(animalName);
        ZooManager.animals.get(animalName).feed(numberOfMeals);
    }

    /**
     * Cleans the habitat of the specified animal.
     * <p>
     * Before cleaning, validates that the animal exists.
     * </p>
     *
     * @param animalName the name of the animal whose habitat will be cleaned
     * @throws ZooException if the animal does not exist or another cleaning error occurs
     */
    @Override
    public void visitAnimal(String animalName) throws ZooException {
        System.out.println(getName() + " attempts to clean " + animalName + "'s habitat.");
        ZooManager.ControlAnimal(animalName);
        System.out.println(getName() + " started cleaning " + animalName + "'s habitat.");
        ZooManager.animals.get(animalName).cleanHabitat();
    }
}


/**
 * Visitor is a type of Person allowed to visit animals
 * but not permitted to feed them.
 */
class Visitor extends Person {

    /**
     * Constructs a new Visitor.
     *
     * @param name the visitor's name
     * @param id   the unique identifier for the visitor
     */
    public Visitor(String name, int id) {
        super(name, id);
    }

    /**
     * Attempt to feed an animal, but visitors are not authorized.
     * <p>
     * Always throws an UnauthorizedFeedException.
     * </p>
     *
     * @param animalName    the name of the animal to feed
     * @param numberOfMeals the number of meals attempted
     * @throws ZooException always thrown to indicate lack of feed authority
     */
    @Override
    public void feedAnimal(String animalName, int numberOfMeals) throws ZooException {
        ZooManager.ControlAnimal(animalName);
        System.out.println(getName() + " tried to feed " + animalName + ".");
        throw new UnauthorizedFeedException(
                "Error: Visitors do not have the authority to feed animals."
        );
    }

    /**
     * Registers a visit to the specified animal for the visitor.
     * <p>
     * Validates that the animal exists before confirming the visit.
     * </p>
     *
     * @param animalName the name of the animal to visit
     * @throws ZooException if the animal does not exist or another error occurs
     */
    @Override
    public void visitAnimal(String animalName) throws ZooException {
        System.out.println(getName() + " tried to register for a visit to " + animalName + ".");
        ZooManager.ControlAnimal(animalName);
        System.out.println(getName() + " successfully visited " + animalName + ".");
    }
}
