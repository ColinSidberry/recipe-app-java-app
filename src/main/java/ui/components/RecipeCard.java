package ui.components;

import models.Recipe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import ui.dialogs.RecipeDetailsDialog;

public class RecipeCard extends JPanel {
    private final Recipe recipe;

    public RecipeCard(Recipe recipe) {
        this.recipe = recipe;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Title
        JLabel titleLabel = new JLabel(recipe.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Make title clickable
        titleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    showRecipeDetails();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                titleLabel.setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                titleLabel.setForeground(Color.BLACK);
            }
        });
        
        add(titleLabel, BorderLayout.NORTH);
    }

    private void showRecipeDetails() {
        JFrame owner = (JFrame) SwingUtilities.getWindowAncestor(this);
        new RecipeDetailsDialog(owner, recipe).setVisible(true);
    }
}
