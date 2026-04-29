import java.util.Locale;
import java.util.Map;
import java.util.HashMap;

/**
 * Abstract base class representing a zoo animal.
 * <p>
 * Encapsulates common properties such as name and age, and provides
 * shared logic for calculating meal sizes, feeding, and habitat cleaning.
 * Subclasses must specify their own food requirements and feeding messages.
 * </p>
 */
public abstract class Animal {
    private final String name;
    private final int age;
    private final int ageForMealSizeCalculation;
    private final int baseMealSize;
    private final float perYearIncreasingDecreasingRate;

    /**
     * Constructs a new Animal.
     *
     * @param name                             the animal's name
     * @param age                              the animal's age in years
     * @param ageForMealSizeCalculation        the reference age for meal size adjustments
     * @param baseMealSize                     the base meal size at the reference age
     * @param perYearIncreasingDecreasingRate  the rate at which meal size changes per year
     */
    public Animal(String name,
                  int age,
                  int ageForMealSizeCalculation,
                  int baseMealSize,
                  float perYearIncreasingDecreasingRate) {
        this.name = name;
        this.age = age;
        this.ageForMealSizeCalculation = ageForMealSizeCalculation;
        this.baseMealSize = baseMealSize;
        this.perYearIncreasingDecreasingRate = perYearIncreasingDecreasingRate;
    }

    /**
     * Returns the animal's name.
     *
     * @return the name of this animal
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the animal's age.
     *
     * @return the age of this animal in years
     */
    public int getAge() {
        return age;
    }

    /**
     * Returns the reference age used for meal size calculation.
     *
     * @return the age at which the base meal size applies
     */
    public int getAgeForMealSizeCalculation() {
        return ageForMealSizeCalculation;
    }

    /**
     * Returns the base meal size at the reference age.
     *
     * @return the base meal size in kilograms
     */
    public int getBaseMealSize() {
        return baseMealSize;
    }

    /**
     * Returns the per-year rate at which the meal size increases or decreases.
     *
     * @return the rate of change in kilograms per year
     */
    public float getPerYearIncreasingDecreasingRate() {
        return perYearIncreasingDecreasingRate;
    }

    /**
     * Calculates the meal size for one feeding based on age.
     *
     * @return the meal size in kilograms
     */
    public final float calculateMeal() {
        return baseMealSize
                + ((age - ageForMealSizeCalculation)
                * perYearIncreasingDecreasingRate);
    }

    /**
     * Determines the specific food type and quantities required for a total meal.
     * <p>
     * Subclasses must implement this to map {@link FoodType} to required amounts.
     * </p>
     *
     * @param totalMeal the total meal size in kilograms
     * @return a map of food types to required kilograms
     */
    protected abstract Map<FoodType, Float> getRequiredFood(float totalMeal);

    /**
     * Prints a message indicating that the animal has been fed.
     * <p>
     * Subclasses should format their own feeding messages here.
     * </p>
     *
     * @param totalMeal the total meal size in kilograms
     */
    protected abstract void printFeedMessage(float totalMeal);

    /**
     * Feeds the animal the specified number of meals.
     * <p>
     * Calculates the total food required, checks stock levels,
     * deducts the required amount, and prints a feed message.
     * </p>
     *
     * @param numberOfMeals the number of meals to feed
     * @throws NotEnoughFoodException if the zoo does not have sufficient food
     */
    public final void feed(int numberOfMeals) throws NotEnoughFoodException {
        float totalMeal = numberOfMeals * calculateMeal();
        Map<FoodType, Float> requiredFood = getRequiredFood(totalMeal);

        // Verify stock is sufficient
        for (Map.Entry<FoodType, Float> entry : requiredFood.entrySet()) {
            FoodType type = entry.getKey();
            float amountNeeded = entry.getValue();
            Food food = ZooManager.foodStock.getFoods().get(type.name());
            if (food.getAmount() < amountNeeded) {
                throw new NotEnoughFoodException("Error: Not enough " + type.name());
            }
        }

        // Deduct from stock
        for (Map.Entry<FoodType, Float> entry : requiredFood.entrySet()) {
            FoodType type = entry.getKey();
            float amountNeeded = entry.getValue();
            Food food = ZooManager.foodStock.getFoods().get(type.name());
            food.setAmount(food.getAmount() - amountNeeded);
        }

        printFeedMessage(totalMeal);
    }

    /**
     * Cleans the animal's habitat.
     * <p>
     * Subclasses must implement this to provide species-specific cleaning steps.
     * </p>
     */
    abstract void cleanHabitat();
}


/**
 * Represents a lion in the zoo.
 * <p>
 * Lions eat meat and have specific habitat cleaning requirements.
 * </p>
 */
class Lion extends Animal {
    /**
     * Constructs a new Lion.
     *
     * @param name the lion's name
     * @param age  the lion's age in years
     */
    public Lion(String name, int age) {
        super(name, age, 5, 5, 0.05f);
    }

    @Override
    protected Map<FoodType, Float> getRequiredFood(float totalMeal) {
        Map<FoodType, Float> map = new HashMap<>();
        map.put(FoodType.Meat, totalMeal);
        return map;
    }

    @Override
    protected void printFeedMessage(float totalMeal) {
        System.out.printf(Locale.US,
                "%s has been given %.3f kgs of meat%n",
                getName(),
                totalMeal
        );
    }

    @Override
    void cleanHabitat() {
        System.out.println("Cleaning " + getName() +
                "'s habitat: Removing bones and refreshing sand.");
    }
}


/**
 * Represents an elephant in the zoo.
 * <p>
 * Elephants eat plants (fruits and hay) and require water area cleaning.
 * </p>
 */
class Elephant extends Animal {
    /**
     * Constructs a new Elephant.
     *
     * @param name the elephant's name
     * @param age  the elephant's age in years
     */
    public Elephant(String name, int age) {
        super(name, age, 20, 10, 0.015f);
    }

    @Override
    protected Map<FoodType, Float> getRequiredFood(float totalMeal) {
        Map<FoodType, Float> map = new HashMap<>();
        map.put(FoodType.Plant, totalMeal);
        return map;
    }

    @Override
    protected void printFeedMessage(float totalMeal) {
        System.out.printf(Locale.US,
                "%s has been given %.3f kgs assorted fruits and hay%n",
                getName(),
                totalMeal
        );
    }

    @Override
    void cleanHabitat() {
        System.out.println("Cleaning " + getName() +
                "'s habitat: Washing the water area.");
    }
}


/**
 * Represents a chimpanzee in the zoo.
 * <p>
 * Chimpanzees eat both plants and meat, and require branch replacement cleaning.
 * </p>
 */
class Chimpanzee extends Animal {
    /**
     * Constructs a new Chimpanzee.
     *
     * @param name the chimpanzee's name
     * @param age  the chimpanzee's age in years
     */
    public Chimpanzee(String name, int age) {
        super(name, age, 10, 3, 0.0125f);
    }

    @Override
    protected Map<FoodType, Float> getRequiredFood(float totalMeal) {
        Map<FoodType, Float> map = new HashMap<>();
        map.put(FoodType.Plant, totalMeal);
        map.put(FoodType.Meat, totalMeal);
        return map;
    }

    @Override
    protected void printFeedMessage(float totalMeal) {
        System.out.printf(Locale.US,
                "%s has been given %.3f kgs of meat and %.3f kgs of leaves%n",
                getName(), totalMeal, totalMeal
        );
    }

    @Override
    void cleanHabitat() {
        System.out.println("Cleaning " + getName() +
                "'s habitat: Sweeping the enclosure and replacing branches.");
    }
}


/**
 * Represents a penguin in the zoo.
 * <p>
 * Penguins eat fish and require ice replenishment and scrubbing.
 * </p>
 */
class Penguin extends Animal {
    /**
     * Constructs a new Penguin.
     *
     * @param name the penguin's name
     * @param age  the penguin's age in years
     */
    public Penguin(String name, int age) {
        super(name, age, 4, 3, 0.04f);
    }

    @Override
    protected Map<FoodType, Float> getRequiredFood(float totalMeal) {
        Map<FoodType, Float> map = new HashMap<>();
        map.put(FoodType.Fish, totalMeal);
        return map;
    }

    @Override
    protected void printFeedMessage(float totalMeal) {
        System.out.printf(Locale.US,
                "%s has been given %.3f kgs of various kinds of fish%n",
                getName(),
                totalMeal
        );
    }

    @Override
    void cleanHabitat() {
        System.out.println("Cleaning " + getName() +
                "'s habitat: Replenishing ice and scrubbing walls.");
    }
}
