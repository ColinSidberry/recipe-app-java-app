package ui;

import services.RecipeService;
import ui.panels.AddRecipePanel;
import ui.panels.ViewRecipesPanel;

import javax.swing.*;
import java.awt.*;

public class RecipeApp extends JFrame {
    private static final String VIEW_PANEL = "VIEW_PANEL";
    private static final String ADD_PANEL = "ADD_PANEL";

    private final RecipeService recipeService;
    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    public RecipeApp() {
        this.recipeService = new RecipeService();
        this.cardLayout = new CardLayout();
        this.contentPanel = new JPanel(cardLayout);

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Recipe App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Set up main panel with card layout
        contentPanel.add(new ViewRecipesPanel(recipeService), VIEW_PANEL);
        contentPanel.add(new AddRecipePanel(recipeService), ADD_PANEL);

        // Create navigation panel
        JPanel navPanel = createNavigationPanel();

        // Add panels to frame
        add(navPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel(new GridLayout(1, 2));
        
        JButton viewButton = new JButton("View");
        JButton addButton = new JButton("Add");

        viewButton.addActionListener(e -> cardLayout.show(contentPanel, VIEW_PANEL));
        addButton.addActionListener(e -> cardLayout.show(contentPanel, ADD_PANEL));

        navPanel.add(viewButton);
        navPanel.add(addButton);

        return navPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RecipeApp app = new RecipeApp();
            app.setVisible(true);
        });
    }
}
