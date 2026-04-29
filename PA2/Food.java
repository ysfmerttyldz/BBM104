/**
 * Represents a quantity of a specific type of food in the zoo's inventory.
 */
public class Food {
    /** The category of this food (Meat, Fish, or Plant). */
    private final FoodType foodType;

    /** The available amount in kilograms. */
    private float amount;

    /**
     * Constructs a Food entry.
     *
     * @param foodType category of the food
     * @param amount   initial stock amount in kilograms
     */
    public Food(FoodType foodType, float amount) {
        this.foodType = foodType;
        this.amount = amount;
    }

    /** @return the food category */
    public FoodType getFoodType() {
        return foodType;
    }

    /** @return the current amount in kilograms */
    public float getAmount() {
        return amount;
    }

    /**
     * Updates the stored amount.
     *
     * @param amount new stock amount in kilograms
     */
    public void setAmount(float amount) {
        this.amount = amount;
    }
}

/**
 * Enumeration of valid food categories.
 */
enum FoodType {
    /** Meat for carnivores. */
    Meat,
    /** Fish for piscivores. */
    Fish,
    /** Plant-based food for herbivores. */
    Plant
}
