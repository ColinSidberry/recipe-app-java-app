//package com.recipeswingapp;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.List;
//
//public class RecipeApp extends JFrame {
//  private JPanel mainPanel;
//  private JPanel buttonPanel;
//  private List<Integer> recipes = new ArrayList<>(Arrays.asList(1, 2, 3));
//
//
//  public RecipeApp() {
//    setTitle("Recipe Guide");
//    setSize(400, 600);
//    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    setLocationRelativeTo(null);
//
//    mainPanel = new JPanel(new BorderLayout());
//    buttonPanel = new JPanel();
//    buttonPanel.setLayout(new GridLayout(0, 1)); // Vertical list of buttons
//
//    mainPanel.add(new JScrollPane(buttonPanel), BorderLayout.CENTER);
//    add(mainPanel);
//
//    fetchAndDisplayRecipes();
//  }
//
//  private void fetchAndDisplayRecipes() {
////    List<Recipe> recipes = RecipeService.fetchRecipes();
////    for (Recipe recipe : recipes) {
//    for (Integer recipe : recipes) {
////      JButton recipeButton = new JButton(recipe.getName());
//      JButton recipeButton = new JButton(String.valueOf(recipe));
//      recipeButton.addActionListener(new ActionListener() {
//        @Override
//        public void actionPerformed(ActionEvent e) {
////          showRecipeDetails(recipe);
//          System.out.println("Recipe: " + recipe + " was clicked.");
//        }
//      });
//      buttonPanel.add(recipeButton);
//    }
//
//    buttonPanel.revalidate();
//    buttonPanel.repaint();
//  }
//
//  private void showRecipeDetails(Recipe recipe) {
//    JOptionPane.showMessageDialog(this,
//        "Recipe: " + recipe.getName() + "\n\n" +
//            "Ingredients: " + recipe.getIngredients() + "\n\n" +
//            "Instructions: " + recipe.getInstructions(),
//        "Recipe Details",
//        JOptionPane.INFORMATION_MESSAGE);
//  }
//
//  public static void main(String[] args) {
//    SwingUtilities.invokeLater(() -> new RecipeApp().setVisible(true));
//  }
//}

package com.recipeswingapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.border.EmptyBorder;

public class RecipeApp extends JFrame {
  private JPanel mainPanel;
  private JPanel buttonPanel;
  private JTextField searchField;
  private List<String> recipes = new ArrayList<>(Arrays.asList(
      "Quesadilla", "Savory French Toast", "Pasta Carbonara", "Chicken Curry",
      "Garlic Bread", "Tacos", "Miso Soup", "Omelette"
  ));
  private List<JButton> recipeButtons = new ArrayList<>();

  public RecipeApp() {
    setTitle("Recipe Guide");
    setSize(500, 600); // Adjusted for better layout
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    mainPanel = new JPanel(new BorderLayout());

    // Search Bar
    JPanel searchPanel = new JPanel(new BorderLayout());
    searchField = new JTextField("Find Recipes");
    searchField.addActionListener(e -> filterRecipes());
    searchPanel.add(searchField, BorderLayout.CENTER);
    searchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    mainPanel.add(searchPanel, BorderLayout.NORTH);

    // Recipe Buttons Grid (2 columns)
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayout(0, 2, 10, 10)); // 2 columns, with spacing

    mainPanel.add(new JScrollPane(buttonPanel), BorderLayout.CENTER);
    add(mainPanel);

    fetchAndDisplayRecipes();
  }

  private void fetchAndDisplayRecipes() {
    buttonPanel.removeAll();
    recipeButtons.clear();

    for (String recipe : recipes) {
      JButton recipeButton = new JButton(recipe);
      recipeButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          showRecipeDetails(recipe);
        }
      });

      buttonPanel.add(recipeButton);
      recipeButtons.add(recipeButton);
    }

    buttonPanel.revalidate();
    buttonPanel.repaint();
  }

  private void filterRecipes() {
    String query = searchField.getText().toLowerCase();
    buttonPanel.removeAll();

    for (JButton button : recipeButtons) {
      if (button.getText().toLowerCase().contains(query)) {
        buttonPanel.add(button);
      }
    }

    buttonPanel.revalidate();
    buttonPanel.repaint();
  }

  private void showRecipeDetails(String recipe) {
    JOptionPane.showMessageDialog(this,
        "Recipe: " + recipe + "\n\nIngredients: TBD\nInstructions: TBD",
        "Recipe Details", JOptionPane.INFORMATION_MESSAGE);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new RecipeApp().setVisible(true));
  }
}

