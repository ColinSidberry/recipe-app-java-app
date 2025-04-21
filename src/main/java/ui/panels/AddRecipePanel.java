package ui.panels;

import models.Category;
import models.Recipe;
import models.RecipeIngredient;
import models.Instruction;
import services.RecipeService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AddRecipePanel extends JPanel {
    private final RecipeService recipeService;
    private final JTextField titleField;
    private final JTextArea descriptionArea;
    private final JTextField categoryField;
    private final JPanel ingredientsPanel;
    private final JPanel instructionsPanel;
    private final List<JPanel> ingredientRows;
    private final List<JPanel> instructionRows;

    public AddRecipePanel(RecipeService recipeService) {
        this.recipeService = recipeService;
        this.ingredientRows = new ArrayList<>();
        this.instructionRows = new ArrayList<>();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title
        add(new JLabel("Title:"), gbc);
        titleField = new JTextField(20);
        add(titleField, gbc);

        // Description
        add(new JLabel("Description:"), gbc);
        descriptionArea = new JTextArea(3, 20);
        add(new JScrollPane(descriptionArea), gbc);

        // Category
        add(new JLabel("Category:"), gbc);
        categoryField = new JTextField(20);
        add(categoryField, gbc);

        // Add Ingredient button
        JButton addIngredientButton = new JButton("Add Ingredient");
        addIngredientButton.addActionListener(e -> addIngredientRow());
        add(addIngredientButton, gbc);

        // Ingredients list
        ingredientsPanel = new JPanel();
        ingredientsPanel.setLayout(new BoxLayout(ingredientsPanel, BoxLayout.Y_AXIS));
        add(ingredientsPanel, gbc);

        // Add Instruction button
        JButton addInstructionButton = new JButton("Add Instruction");
        addInstructionButton.addActionListener(e -> addInstructionRow());
        add(addInstructionButton, gbc);

        // Instructions list
        instructionsPanel = new JPanel();
        instructionsPanel.setLayout(new BoxLayout(instructionsPanel, BoxLayout.Y_AXIS));
        add(instructionsPanel, gbc);

        // Submit button
        JButton submitButton = new JButton("Add Recipe");
        submitButton.addActionListener(e -> handleSubmit());
        add(submitButton, gbc);
    }

    private void addIngredientRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField nameField = new JTextField(15);
        JTextField amountField = new JTextField(5);
        JTextField unitField = new JTextField(5);
        JButton removeButton = new JButton("Remove");

        row.add(new JLabel("Name:"));
        row.add(nameField);
        row.add(new JLabel("Amount:"));
        row.add(amountField);
        row.add(new JLabel("Unit:"));
        row.add(unitField);
        row.add(removeButton);

        removeButton.addActionListener(e -> {
            ingredientsPanel.remove(row);
            ingredientRows.remove(row);
            ingredientsPanel.revalidate();
            ingredientsPanel.repaint();
        });

        ingredientRows.add(row);
        ingredientsPanel.add(row);
        ingredientsPanel.revalidate();
        ingredientsPanel.repaint();
    }

    private void addInstructionRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField stepField = new JTextField(30);
        JButton removeButton = new JButton("Remove");

        row.add(new JLabel("Step " + (instructionRows.size() + 1) + ":"));
        row.add(stepField);
        row.add(removeButton);

        removeButton.addActionListener(e -> {
            instructionsPanel.remove(row);
            instructionRows.remove(row);
            // Update step numbers
            for (int i = 0; i < instructionRows.size(); i++) {
                JPanel instructionRow = instructionRows.get(i);
                ((JLabel)instructionRow.getComponent(0)).setText("Step " + (i + 1) + ":");
            }
            instructionsPanel.revalidate();
            instructionsPanel.repaint();
        });

        instructionRows.add(row);
        instructionsPanel.add(row);
        instructionsPanel.revalidate();
        instructionsPanel.repaint();
    }

    private void handleSubmit() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        String categoryName = categoryField.getText().trim();

        if (title.isEmpty() || description.isEmpty() || categoryName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all required fields",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Recipe recipe = new Recipe();
        recipe.setTitle(title);
        recipe.setDescription(description);

        Category category = new Category();
        category.setName(categoryName);
        recipe.setCategory(category);

        // Add ingredients
        List<RecipeIngredient> ingredients = new ArrayList<>();
        for (JPanel row : ingredientRows) {
            JTextField nameField = (JTextField) row.getComponent(1);
            JTextField amountField = (JTextField) row.getComponent(3);
            JTextField unitField = (JTextField) row.getComponent(5);

            String name = nameField.getText().trim();
            String amountText = amountField.getText().trim();
            String unit = unitField.getText().trim();

            if (!name.isEmpty() && !amountText.isEmpty() && !unit.isEmpty()) {
                try {
                    double amount = Double.parseDouble(amountText);
                    RecipeIngredient ingredient = new RecipeIngredient();
                    ingredient.setName(name);
                    ingredient.setAmount(amount);
                    ingredient.setUnit(unit);
                    ingredients.add(ingredient);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid amount for ingredient: " + name,
                            "Validation Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        recipe.setIngredients(ingredients);

        // Add instructions
        List<Instruction> instructions = new ArrayList<>();
        for (int i = 0; i < instructionRows.size(); i++) {
            JPanel row = instructionRows.get(i);
            JTextField stepField = (JTextField) row.getComponent(1);
            String stepDescription = stepField.getText().trim();

            if (!stepDescription.isEmpty()) {
                Instruction instruction = new Instruction();
                instruction.setStepNumber(i + 1);
                instruction.setDescription(stepDescription);
                instructions.add(instruction);
            }
        }
        recipe.setInstructions(instructions);

        Recipe createdRecipe = recipeService.createRecipe(recipe);
        if (createdRecipe != null) {
            JOptionPane.showMessageDialog(this,
                    "Recipe created successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Clear fields after successful submission
            titleField.setText("");
            descriptionArea.setText("");
            categoryField.setText("");
            clearIngredients();
            clearInstructions();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to create recipe. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearIngredients() {
        ingredientRows.clear();
        ingredientsPanel.removeAll();
        ingredientsPanel.revalidate();
        ingredientsPanel.repaint();
    }

    private void clearInstructions() {
        instructionRows.clear();
        instructionsPanel.removeAll();
        instructionsPanel.revalidate();
        instructionsPanel.repaint();
    }
}
