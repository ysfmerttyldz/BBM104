import java.util.Map;
import java.util.HashMap;

/**
 * Manages the zoo's inventory of {@link Food} items.
 *
 * Uses a map from food type name to Food instance.
 */
public class FoodStock {
    private final Map<String, Food> foods = new HashMap<>();

    /**
     * Initializes or resets the stock for each main food type.
     *
     * @param meat  kilograms of meat to stock
     * @param fish  kilograms of fish to stock
     * @param plant kilograms of plant food to stock
     */
    public void listFoodStock(float meat, float fish, float plant) {
        foods.put(FoodType.Meat.name(),  new Food(FoodType.Meat,  meat));
        foods.put(FoodType.Fish.name(),  new Food(FoodType.Fish,  fish));
        foods.put(FoodType.Plant.name(), new Food(FoodType.Plant, plant));
    }

    /**
     * Adds additional quantity to an existing food entry.
     *
     * @param food   the Food object whose type identifies the entry
     * @param amount extra kilograms to add to stock
     * @throws IllegalArgumentException if the food type is not initialized
     */
    public void addFood(Food food, float amount) {
        String key = food.getFoodType().name();
        if (!foods.containsKey(key)) {
            throw new IllegalArgumentException("Food type not initialized: " + key);
        }
        foods.get(key).setAmount(foods.get(key).getAmount() + amount);
    }

    /**
     * @return unmodifiable view of the food type name to Food instance map
     */
    public Map<String, Food> getFoods() {
        return foods;
    }
}
