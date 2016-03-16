import javax.swing.*;
import java.awt.*;


public class GUI extends JFrame {
    public static final int GRID_SIZE = 9;
    public static final int CELL_SIZE = 60;
    public static final int CANVAS_HEIGHT = CELL_SIZE * GRID_SIZE;
    public static final int CANVAS_WIDTH = CELL_SIZE * GRID_SIZE;

    public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);
    public static final Color OPEN_CELL_BGCOLOR = Color.YELLOW;
    public static final Color CLOSED_CELL_BGCOLOR = new Color(240, 240, 240);
    public static final Color CLOSED_CELL_TEXT = Color.BLACK;

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
    public GUI() {
        Container cp = getContentPane();
        cp.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));

        for (int line = 0; line < GRID_SIZE; ++line) {
            for (int col = 0; col < GRID_SIZE; ++col) {
                cels[line][col] = new JTextField(); // Allocate element of array
                cp.add(cels[line][col]);            // ContentPane adds JTextField
                if (masks[line][col]) {
                    cels[line][col].setText("");     // set to empty string
                    cels[line][col].setEditable(true);
                    cels[line][col].setBackground(OPEN_CELL_BGCOLOR);

                    // Add ActionEvent listener to process the input
                    // ... [TODO 2] (Later) ...
                } else {
                    cels[line][col].setText(puzzle[line][col] + "");
                    cels[line][col].setEditable(false);
                    cels[line][col].setBackground(CLOSED_CELL_BGCOLOR);
                    cels[line][col].setForeground(CLOSED_CELL_TEXT);
                }
                // Beautify all the cells
                cels[line][col].setHorizontalAlignment(JTextField.CENTER);
                cels[line][col].setFont(FONT_NUMBERS);
            }
        }

        // Set the size of the content-pane and pack all the components
        //  under this container.
        cp.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        pack();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Handle window closing
        setTitle("Sudoku");
        setVisible(true);
    };

    public static void main(String[] args) {
        // [TODO 3] (Now)
        new GUI();
    }
};
