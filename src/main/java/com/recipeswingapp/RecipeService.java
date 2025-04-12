package com.recipeswingapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class RecipeService {
  private static final String API_URL = "https://your-backend-api.com/recipes"; // Replace with actual backend URL

  public static List<Recipe> fetchRecipes() {
    List<Recipe> recipeList = new ArrayList<>();

    try {
      URL url = new URL(API_URL);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");

      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      StringBuilder response = new StringBuilder();
      String line;

      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
      reader.close();

      JSONArray recipesArray = new JSONArray(response.toString());

      for (int i = 0; i < recipesArray.length(); i++) {
        JSONObject obj = recipesArray.getJSONObject(i);
        String name = obj.getString("name");
        String ingredients = obj.getString("ingredients");
        String instructions = obj.getString("instructions");

        recipeList.add(new Recipe(name, ingredients, instructions));
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return recipeList;
  }
}
