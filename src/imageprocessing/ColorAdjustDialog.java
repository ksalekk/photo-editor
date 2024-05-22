package imageprocessing;

import javax.swing.*;
import java.awt.*;


/**
 * Dialog window for brightness and contrast adjustment.
 */
public class ColorAdjustDialog extends JDialog {
    /**
     * Slider for brightness set
     */
    public JSlider brightnessSlider;

    /**
     * Factor scaling brightness slider values
     */
    public final int BRIGHTNESS_FACTOR = 1;

    /**
     * Slider for contrast slider
     */
    public JSlider contrastSlider;

    /**
     * Factor scaling contrast slider values
     */
    public final float CONTRAST_FACTOR = 0.01f;

    /**
     * Button for apply changes.
     */
    public JButton submitButton;

    public ColorAdjustDialog(Frame parent, boolean modal) {
        super(parent, "Change Color Properties", modal);

        dialogSetup(parent);
    }

    /**
     * Setup dialog layout.
     * @param parent parent window for dialog window
     */
    private void dialogSetup(Frame parent) {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setResizable(false);
        setLocationRelativeTo(parent);

        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 30));

        brightnessSlider = createSlider(-300, 300, 0);
        contrastSlider = createSlider(0, 1000, 100);

        submitButton = new JButton("APPLY");
        submitButton.setAlignmentX(JButton.CENTER);

        contentPane.add(createColorPropertyPanel("Brightness", brightnessSlider, BRIGHTNESS_FACTOR));
        contentPane.add(createColorPropertyPanel("Contrast", contrastSlider, CONTRAST_FACTOR));
        contentPane.add(submitButton);
    }

    /**
     * Create single panel with slider and spinner
     * @param name Name for property changed by slider
     * @param slider JSlider object for changing property
     * @param scalingFactor factor which scaling values from slider
     * @return built color property pane with slider and spinner
     */
    private JPanel createColorPropertyPanel(String name, JSlider slider, float scalingFactor) {
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
//            if(spinner.getValue() instanceof Double) {
//                System.out.println("Double");
//            } else if (spinner.getValue() instanceof Float) {
//                System.out.println("Float");
//            }

            if(spinner.getValue() instanceof Float) {
                float spinnerVal = (float) spinner.getValue();
                int realValue = (int)(spinnerVal / scalingFactor);
                slider.setValue(realValue);
            } else {
                double spinnerVal = (double) spinner.getValue();
                int realValue = (int)(spinnerVal / scalingFactor);
                slider.setValue(realValue);
            }

        });
        slider.addChangeListener(e -> {
            float sliderVal = (float) slider.getValue();
            spinner.setValue(sliderVal * scalingFactor);

        });
        panel.add(label);
        panel.add(Box.createHorizontalGlue());
        panel.add(slider);
        panel.add(spinner);

        return panel;
    }

    /**
     * Create single slider with passed parameters
     * @param min min slider value
     * @param max max slider value
     * @param init init slider value
     * @return built JSlider object
     */
    private JSlider createSlider(int min, int max, int init) {
        return new JSlider(min, max, init);
    }

    /**
     * Set brightness value to 0, and contrast value to 1
     */
    public void resetSliders() {
        brightnessSlider.setValue(0);
        contrastSlider.setValue(100);
    }
}
