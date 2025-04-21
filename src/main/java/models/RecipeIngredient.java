package models;

public class RecipeIngredient {
    private String id;
    private String recipeId;
    private String name;
    private double amount;
    private String unit;

    public RecipeIngredient() {}

    public RecipeIngredient(String id, String recipeId, String name, double amount, String unit) {
        this.id = id;
        this.recipeId = recipeId;
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
