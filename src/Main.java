import javax.swing.*;
import java.awt.*;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDeepOceanIJTheme;

/**
 * <h2>Simple Photo Editor App</h2>
 * <li>Load and Save JPG files</li>
 * <li>Edge Detection Filter</li>
 * <li>Contrast Changing</li>
 * <li>Brightness Changing</li>
 *
 * @author Jakub Sałek
 * <p>PJAVA 23Z</p>
 */

public class Main {
    public static void main(String[] args) {
        App app = new App();
        app.run();
    }
}