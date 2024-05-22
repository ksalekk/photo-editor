package imageprocessing;

import javax.swing.*;
import java.awt.*;
import java.util.TreeMap;

// TODO:
//  -> repair focus on first JTextField in JDialog
//  -> regex for text from cells
//  -> undo, redo (sth is wrong)


/**
 * Represents the window for setting parameters of 3x3 kernel
 * used for linear filtration
 */
public class FiltrationDialog extends JDialog {
    /**
     * Table of text fields that get kernel cells values
     */
    private JTextField[] kernelCells;

    /**
     * Combo box for choose kernel (filter type)
     */
    private JComboBox<String> filterPicker;

    /**
     * Map of preset kernels (key - name, value - table of kernel cells)
     */
    private TreeMap<String, float[]> presetKernels;

    /**
     * Button for applying changes
     */
    public JButton runButton;


    public FiltrationDialog(Frame parent, boolean modal) {
        super(parent, "Filtration Settings", modal);
        loadKernelPresets();
        dialogSetup(parent);
    }

    /**
     * Load kernels presets (, sobel filter, gaussian filter)
     */
    private void loadKernelPresets() {
        presetKernels = new TreeMap<>();

        presetKernels.put(
                "",
                new float[]{ 0, 0, 0,
                        0, 1f, 0,
                        0, 0, 0 }
        );

        presetKernels.put(
                "Low Pass",
                new float[]{ 1f, 1f, 1f,
                        1f, 2f, 1f,
                        1f, 1f, 1f }
        );

        presetKernels.put(
                "Gaussian Filter",
                new float[]{ 1f, 2f, 1f,
                        2f, 4f, 2f,
                        1f, 2f, 1f }
        );

        presetKernels.put(
                "High Pass",
                new float[]{ -1f, -1f, -1f,
                        -1f, 9f, -1f,
                        -1f, -1f, -1f }
        );

        presetKernels.put(
                "Sobel Vertical Filter",
                new float[]{ 1f, 0, -1f,
                        2f, 0, -2f,
                        1f, 0, -1f }
        );

        presetKernels.put(
                "Sobel Horizontal Filter",
                new float[]{ 1f, 2f, 1f,
                        0, 0, 0,
                        -1f, -2f, -1f }
        );

        presetKernels.put(
                "Laplace Filter",
                new float[]{ 0, 1f, 0,
                        1f, -4f, 1f,
                        0, 1f, 0 }
        );
    }

    /**
     * Setup dialog window layout.
     * @param parent JFrame parent of window dialog.
     */
    private void dialogSetup(Frame parent) {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(500, 200);
        setResizable(false);
        setLocationRelativeTo(parent);


        JPanel dialogPane = (JPanel) getContentPane();
        dialogPane.setLayout(new BoxLayout(dialogPane, BoxLayout.X_AXIS));
        dialogPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        dialogPane.add(createLeftPanel());
        dialogPane.add(new JSeparator(SwingConstants.VERTICAL));
        dialogPane.add(createRightPanel());
    }


    /**
     * Create and returns JPanel which contains grid of text fields when user can input kernel cells values.
     * @return JPanel with text fields 3x3 grid
     */
    private JPanel createLeftPanel() {
        int KERNEL_X_SIZE = 3;
        int KERNEL_Y_SIZE = 3;
        JPanel leftPanel = new JPanel(new GridLayout(KERNEL_X_SIZE, KERNEL_Y_SIZE));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 50));

        kernelCells =  new JTextField[KERNEL_X_SIZE * KERNEL_Y_SIZE];
        for(int i = 0; i < kernelCells.length; i++) {
            kernelCells[i] = new JTextField();
            kernelCells[i].setHorizontalAlignment(JTextField.CENTER);
            leftPanel.add(kernelCells[i]);
        }

        return leftPanel;
    }

    /**
     * Create and return JPanel which contains combo box with filters presets
     * @return JPanel with combo box for filters presets choosing.
     */
    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(40, 10, 20, 10));


        filterPicker = new JComboBox<>();
        filterPicker.setPreferredSize(new Dimension(20, -5));
        for(String preset : presetKernels.keySet()) {
            filterPicker.addItem(preset);
        }
        filterPicker.addActionListener(e -> {
            String chosenFilter = (String) filterPicker.getSelectedItem();
            setKernelCellsValues(presetKernels.get(chosenFilter));
        });
        filterPicker.setSelectedItem("");


        runButton = new JButton("APPLY");
        runButton.setAlignmentX(Component.CENTER_ALIGNMENT);


        rightPanel.add(filterPicker);
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(runButton);

        return rightPanel;
    }

    /**
     * Set text fields with passed kernel cells values
     * @param kernelCellsValues kernel cells array
     */
    private void setKernelCellsValues(float[] kernelCellsValues) {
        for(int i = 0; i < kernelCells.length; i++) {
            JTextField cell = kernelCells[i];
            cell.setText(String.valueOf(kernelCellsValues[i]));
        }
    }

    /**
     * Return array of kernel cells values from text fields
     * @return float array of kernel cells values
     */
    public float[] getKernel() {
        float[] cellsValues = new float[9];
        float sum = 0;
        for(int i = 0; i < kernelCells.length; i++) {
            JTextField cell = kernelCells[i];
            cellsValues[i] = Float.parseFloat(cell.getText());
            sum += cellsValues[i];
        }
        if(sum == 0) {
            sum = 1;
        }
        for(int i = 0; i < cellsValues.length; i++) {
            cellsValues[i] *= 1/sum;
        }


        return cellsValues;
    }
}