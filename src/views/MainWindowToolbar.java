package views;

import javax.swing.*;

/**
 * Represents a toolbar for image editing (gray scale, filtration, contrast and brightness adjustment)
 */
public class MainWindowToolbar extends JToolBar {
    public JButton grayScaleButton;
    public JButton filtrationButton;
    public JButton adjustColorButton;


    public MainWindowToolbar() {
//        setPreferredSize(new Dimension(300, 40));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        grayScaleButton = createButton("Convert Image to Gray Scale", "resources\\black-and-white.png");
        filtrationButton = createButton("Image Linear Filtration", "resources\\filter.png");
        adjustColorButton = createButton("Adjust Brightness or Contrast", "resources\\christmas-stars.png");

        add(grayScaleButton);
        add(filtrationButton);
        add(adjustColorButton);
    }

    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton();
        ImageIcon icon = new ImageIcon(iconPath);
        if(icon.getIconWidth() != 0) {
            button.setIcon(icon);
        } else {
            button.setText(text);
        }
        button.setToolTipText(text);
        button.setFocusable(false);
        button.setEnabled(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 30, 5, 30));
        return button;
    }
}

