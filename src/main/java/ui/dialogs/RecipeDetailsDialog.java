package ui.dialogs;

import models.Recipe;
import models.RecipeIngredient;
import models.Instruction;
import models.Category;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class RecipeDetailsDialog extends JDialog {
    private static final String DEFAULT_VALUE = "Not specified";

    public RecipeDetailsDialog(JFrame owner, Recipe recipe) {
        super(owner, "Recipe Details - " + Optional.ofNullable(recipe.getTitle()).orElse(DEFAULT_VALUE), true);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title
        add(createLabel("Title: " + Optional.ofNullable(recipe.getTitle()).orElse(DEFAULT_VALUE)), gbc);

        // Description
        add(createLabel("Description:"), gbc);
        JTextArea descriptionArea = new JTextArea(
            Optional.ofNullable(recipe.getDescription()).orElse(DEFAULT_VALUE), 
            3, 20
        );
        descriptionArea.setEditable(false);
        add(new JScrollPane(descriptionArea), gbc);

        // Category
        String categoryName = Optional.ofNullable(recipe.getCategory())
            .map(Category::getName)
            .orElse(DEFAULT_VALUE);
        add(createLabel("Category: " + categoryName), gbc);

        // Ingredients
        add(createLabel("Ingredients:"), gbc);
        JTextArea ingredientsArea = new JTextArea(5, 20);
        ingredientsArea.setEditable(false);
        
        List<RecipeIngredient> ingredients = Optional.ofNullable(recipe.getIngredients())
            .orElse(List.of());
        
        if (ingredients.isEmpty()) {
            ingredientsArea.append(DEFAULT_VALUE);
        } else {
            for (RecipeIngredient ingredient : ingredients) {
                String ingredientText = String.format(
                    "%s - %.2f %s",
                    Optional.ofNullable(ingredient.getName()).orElse(DEFAULT_VALUE),
                    Optional.ofNullable(ingredient.getAmount()).orElse(0.0),
                    Optional.ofNullable(ingredient.getUnit()).orElse(DEFAULT_VALUE)
                );
                ingredientsArea.append(ingredientText + "\n");
            }
        }
        
        add(new JScrollPane(ingredientsArea), gbc);

        // Instructions
        add(createLabel("Instructions:"), gbc);
        JTextArea instructionsArea = new JTextArea(5, 20);
        instructionsArea.setEditable(false);
        
        List<Instruction> instructions = Optional.ofNullable(recipe.getInstructions())
            .orElse(List.of());
        
        if (instructions.isEmpty()) {
            instructionsArea.append(DEFAULT_VALUE);
        } else {
            for (Instruction instruction : instructions) {
                String instructionText = String.format(
                    "Step %d: %s",
                    Optional.ofNullable(instruction.getStepNumber()).orElse(1),
                    Optional.ofNullable(instruction.getDescription()).orElse(DEFAULT_VALUE)
                );
                instructionsArea.append(instructionText + "\n");
            }
        }
        
        add(new JScrollPane(instructionsArea), gbc);

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        add(closeButton, gbc);

        pack();
        setLocationRelativeTo(owner);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(label.getFont().getName(), Font.BOLD, label.getFont().getSize()));
        return label;
    }
}
