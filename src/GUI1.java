import javax.swing.*;
import java.awt.*;


public class GUI1 extends JFrame {
    // Name-constants for the game properties
    public static final int GRID_SIZE = 9;    // Size of the board
    public static final int SUBGRID_SIZE = 3; // Size of the sub-grid
    // Name-constants for UI control (sizes, colors and fonts)
    public static final int CELL_SIZE = 60;   // Cell width/height in pixels
    public static final int CANVAS_WIDTH  = CELL_SIZE * GRID_SIZE;
    public static final int CANVAS_HEIGHT = CELL_SIZE * GRID_SIZE;
    // Board width/height in pixels
    public static final Color OPEN_CELL_BGCOLOR = Color.YELLOW;
    public static final Color OPEN_CELL_TEXT_YES = new Color(0, 255, 0);  // RGB
    public static final Color OPEN_CELL_TEXT_NO = Color.RED;
    public static final Color CLOSED_CELL_BGCOLOR = new Color(240, 240, 240); // RGB
    public static final Color CLOSED_CELL_TEXT = Color.BLACK;
    public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);

    public static final int SBORDER = 3;

    private JTextField[][] cels = new JTextField[GRID_SIZE][GRID_SIZE];

    private int[][] puzzle =
            {{5, 3, 4, 6, 7, 8, 9, 1, 2},
                    {6, 7, 2, 1, 9, 5, 3, 4, 8},
                    {1, 9, 8, 3, 4, 2, 5, 6, 7},
                    {8, 5, 9, 7, 6, 1, 4, 2, 3},
                    {4, 2, 6, 8, 5, 3, 7, 9, 1},
                    {7, 1, 3, 9, 2, 4, 8, 5, 6},
                    {9, 6, 1, 5, 3, 7, 2, 8, 4},
                    {2, 8, 7, 4, 1, 9, 6, 3, 5},
                    {3, 4, 5, 2, 8, 6, 1, 7, 9}};

    private boolean[][] masks =
            {{false, false, false, false, false, true, false, false, false},
                    {false, false, false, false, false, false, false, false, true},
                    {false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false},
                    {false, false, false, false, false, false, false, false, false}};

    /**
     * Constructor to setup the game and the UI Components
     */
    public GUI1() {
        Container cp = getContentPane();
        cp.setLayout(new GridLayout(3, 3,5,5));

        // Allocate a common listener as the ActionEvent listener for all the
        //  JTextFields
        // ... [TODO 1] (Later) ....

        JTextField[][] fields;       // Array of fields.
        JPanel[][] panels;      // Panels holding the fields.

        panels = new JPanel[3][3];
        fields = new JTextField[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                panels[y][x] = new JPanel(new GridLayout(3, 3));
                panels[y][x].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                //add(panels[y][x]);

                for (int yy = 0; yy < 3; yy++) {
                    for (int xx = 0; xx < 3; xx++) {
                        fields[yy][xx] = new JTextField();
                        panels[y][x].add(fields[yy][xx]);

                        // Beautify all the cells
                        fields[yy][xx].setHorizontalAlignment(JTextField.CENTER);
                        fields[yy][xx].setFont(FONT_NUMBERS);
                    }
                }
                add(panels[y][x]);
            }
        }


        // Set the size of the content-pane and pack all the components
        //  under this container.
        cp.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Handle window closing
        setTitle("Sudoku");
        setVisible(true);
    }

    public static void main(String[] args) {
        // [TODO 3] (Now)
        new GUI1();
    }
}
