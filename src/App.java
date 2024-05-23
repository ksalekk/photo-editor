import com.formdev.flatlaf.FlatIntelliJLaf;
import controller.AppController;
import model.ImageModel;
import views.MainWindow;

import javax.swing.*;

/**
 * App manager
 */
public class App {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatIntelliJLaf());
        } catch (Exception e) {
            System.err.println("Failed to initialize Look and Feel");
        }

        ImageModel imageModel = new ImageModel();
        AppController appController = new AppController();

        appController.setModel(imageModel);

        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow(appController);
            mainWindow.setVisible(true);
            appController.setView(mainWindow);
        });
    }
}
