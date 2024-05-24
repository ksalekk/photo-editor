package views.modals;

import javax.swing.*;
import java.awt.*;

/**
 * Dialog window for image brightness and contrast processing.
 */
public class ColorAdjustDialog extends JDialog {

    /** Factors that allows to map slider value to processed value */
    public final int BRIGHTNESS_FACTOR = 1;
    public final double CONTRAST_FACTOR = 0.01f;


    public JSlider brightnessSlider;
    public JSlider contrastSlider;
    public JButton submitButton;

    public ColorAdjustDialog(Frame parent, boolean modal) {
        super(parent, "Changing color", modal);
        dialogSetup(parent);
    }


    private void dialogSetup(Frame parent) {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setResizable(false);
        setLocationRelativeTo(parent);

        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 30));

        brightnessSlider = buildSlider(-255, 255, 0);
        contrastSlider = buildSlider(0, 1000, 100);

        submitButton = new JButton("APPLY");
        submitButton.setAlignmentX(JButton.CENTER);

        contentPane.add(createColorPropertyPanel("Brightness", brightnessSlider, BRIGHTNESS_FACTOR));
        contentPane.add(createColorPropertyPanel("Contrast", contrastSlider, CONTRAST_FACTOR));
        contentPane.add(submitButton);
    }


    /**
     * Create and return single panel with slider and spinner
     * @param name Name for property changed by slider
     * @param slider JSlider object for changing property
     * @param scalingFactor factor that scales slider values
     */
    private JPanel createColorPropertyPanel(String name, JSlider slider, double scalingFactor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        panel.setPreferredSize(new Dimension());

        JLabel label = new JLabel(name);

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(
                slider.getValue() * scalingFactor,
                slider.getMinimum()*scalingFactor,
                slider.getMaximum()*scalingFactor,
                scalingFactor
        ));

        spinner.setMaximumSize(new Dimension(spinner.getWidth(), 30));
        spinner.addChangeListener(e -> {
            double spinnerVal = (double) spinner.getValue();
            int realValue = (int)(spinnerVal / scalingFactor);
            slider.setValue(realValue);
        });

        slider.addChangeListener(e -> {
            double sliderVal = slider.getValue();
            spinner.setValue(sliderVal * scalingFactor);
        });

        panel.add(label);
        panel.add(Box.createHorizontalGlue());
        panel.add(slider);
        panel.add(spinner);

        return panel;
    }

    /**
     * Create and return single slider with specified parameters
     * @param min min slider value
     * @param max max slider value
     * @param init init slider value
     */
    private JSlider buildSlider(int min, int max, int init) {
        return new JSlider(min, max, init);
    }

    /**
     * Set brightness value to 0, and contrast value to 1
     */
    public void resetSliders() {
        brightnessSlider.setValue(0);
        contrastSlider.setValue(100);
    }


    public double getBrightnessValue() {
        return this.brightnessSlider.getValue();
    }

    public float getContrastValue() {
        return (float) (this.contrastSlider.getValue() * CONTRAST_FACTOR);
    }
}
