package ui.panels;

import models.Recipe;
import services.RecipeService;
import ui.components.RecipeCard;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ViewRecipesPanel extends JPanel {
    private final RecipeService recipeService;
    private final JPanel recipesPanel;

    public ViewRecipesPanel(RecipeService recipeService) {
        this.recipeService = recipeService;
        setLayout(new BorderLayout());

        recipesPanel = new JPanel();
        recipesPanel.setLayout(new BoxLayout(recipesPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(recipesPanel);
        add(scrollPane, BorderLayout.CENTER);

        loadRecipes();
    }

    private void loadRecipes() {
        // Clear existing recipes
        recipesPanel.removeAll();

        List<Recipe> recipes = recipeService.getAllRecipes();
        for (Recipe recipe : recipes) {
            RecipeCard recipeCard = new RecipeCard(recipe);
            recipesPanel.add(recipeCard);
            // recipesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        // Revalidate and repaint
        recipesPanel.revalidate();
        recipesPanel.repaint();
    }
}
