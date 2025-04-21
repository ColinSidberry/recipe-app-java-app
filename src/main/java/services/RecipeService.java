package services;

import models.Category;
import models.Instruction;
import models.Recipe;
import models.RecipeIngredient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RecipeService {
    private static final String API_URL = "http://localhost:8080/api/recipes";
    private final HttpClient httpClient;

    public RecipeService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public List<Recipe> getAllRecipes() {
        try {
            String response = makeGetRequest(API_URL);
            JSONArray jsonArray = new JSONArray(response);
            List<Recipe> recipes = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonRecipe = jsonArray.getJSONObject(i);
                Recipe recipe = parseRecipe(jsonRecipe);
                recipes.add(recipe);
            }

            return recipes;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private Recipe parseRecipe(JSONObject jsonRecipe) {
        Recipe recipe = new Recipe();
        
        recipe.setId(jsonRecipe.getString("id"));
        recipe.setTitle(jsonRecipe.getString("title"));
        
        // Handle optional fields with default values
        recipe.setDescription(jsonRecipe.optString("description", ""));
        recipe.setUserId(jsonRecipe.optString("userId", ""));
        recipe.setImageUrl(jsonRecipe.optString("imageUrl", ""));

        // Parse createdAt if present
        if (!jsonRecipe.isNull("createdAt")) {
            String createdAtStr = jsonRecipe.getString("createdAt");
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            recipe.setCreatedAt(LocalDateTime.parse(createdAtStr, formatter));
        }

        // Parse ingredients if present
        if (!jsonRecipe.isNull("ingredients")) {
            JSONArray ingredientsArray = jsonRecipe.getJSONArray("ingredients");
            List<RecipeIngredient> ingredients = new ArrayList<>();
            for (int j = 0; j < ingredientsArray.length(); j++) {
                JSONObject jsonIngredient = ingredientsArray.getJSONObject(j);
                ingredients.add(parseIngredient(jsonIngredient));
            }
            recipe.setIngredients(ingredients);
        }

        // Parse instructions if present
        if (!jsonRecipe.isNull("instructions")) {
            JSONArray instructionsArray = jsonRecipe.getJSONArray("instructions");
            List<Instruction> instructions = new ArrayList<>();
            for (int j = 0; j < instructionsArray.length(); j++) {
                JSONObject jsonInstruction = instructionsArray.getJSONObject(j);
                instructions.add(parseInstruction(jsonInstruction));
            }
            recipe.setInstructions(instructions);
        }

        // Parse category if present
        if (!jsonRecipe.isNull("category")) {
            JSONObject jsonCategory = jsonRecipe.getJSONObject("category");
            recipe.setCategory(parseCategory(jsonCategory));
        }

        return recipe;
    }

    private RecipeIngredient parseIngredient(JSONObject jsonIngredient) {
        RecipeIngredient ingredient = new RecipeIngredient();
        ingredient.setId(jsonIngredient.optString("id", null));
        ingredient.setRecipeId(jsonIngredient.optString("recipeId", ""));
        ingredient.setName(jsonIngredient.optString("name", ""));
        ingredient.setAmount(jsonIngredient.optDouble("amount", 0.0));
        ingredient.setUnit(jsonIngredient.optString("unit", ""));
        return ingredient;
    }

    private Instruction parseInstruction(JSONObject jsonInstruction) {
        Instruction instruction = new Instruction();
        instruction.setId(jsonInstruction.optString("id", null));
        instruction.setRecipeId(jsonInstruction.optString("recipeId", ""));
        instruction.setStepNumber(jsonInstruction.optInt("stepNumber", 1));
        instruction.setDescription(jsonInstruction.optString("description", ""));
        return instruction;
    }

    private Category parseCategory(JSONObject jsonCategory) {
        Category category = new Category();
        category.setId(jsonCategory.optString("id", null));
        category.setName(jsonCategory.optString("name", "Uncategorized"));
        return category;
    }

    public Recipe createRecipe(Recipe recipe) {
        try {
            // Create ingredients array
            JSONArray ingredientsArray = new JSONArray();
            for (RecipeIngredient ingredient : recipe.getIngredients()) {
                ingredientsArray.put(new JSONObject()
                    .put("name", ingredient.getName())
                    .put("amount", ingredient.getAmount())
                    .put("unit", ingredient.getUnit()));
            }

            // Create instructions array
            JSONArray instructionsArray = new JSONArray();
            for (Instruction instruction : recipe.getInstructions()) {
                instructionsArray.put(new JSONObject()
                    .put("stepNumber", instruction.getStepNumber())
                    .put("description", instruction.getDescription()));
            }

            String jsonBody = new JSONObject()
                .put("title", recipe.getTitle())
                .put("description", recipe.getDescription())
                .put("userId", "default_user")  // Temporary default user ID
                .put("category", new JSONObject()
                    .put("name", recipe.getCategory().getName()))
                .put("ingredients", ingredientsArray)
                .put("instructions", instructionsArray)
                .toString();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 201 || response.statusCode() == 200) {
                JSONObject jsonResponse = new JSONObject(response.body());
                return parseRecipe(jsonResponse);
            } else {
                System.err.println("Error creating recipe. Status code: " + response.statusCode());
                System.err.println("Response body: " + response.body());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String makeGetRequest(String urlString) throws IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlString))
                // .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }
}
