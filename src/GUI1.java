import javax.swing.*;
import javax.swing.border.Border;
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

    private int[][] nums =
            {{5, 3, 4, 6, 7, 8, 9, 1, 2},
            {6, 7, 2, 1, 9, 5, 3, 4, 8},
            {1, 9, 8, 3, 4, 2, 5, 6, 7},
            {8, 5, 9, 7, 6, 1, 4, 2, 3},
            {4, 2, 6, 8, 5, 3, 7, 9, 1},
            {7, 1, 3, 9, 2, 4, 8, 5, 6},
            {9, 6, 1, 5, 3, 7, 2, 8, 4},
            {2, 8, 7, 4, 1, 9, 6, 3, 5},
            {3, 4, 5, 2, 8, 6, 1, 7, 9}};

    private boolean[][] mascara =
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
        Container janela = getContentPane();
        janela.setLayout(new BorderLayout());

        JPanel panelTopo = new JPanel(new FlowLayout());
        janela.add(panelTopo, BorderLayout.NORTH);

        JButton button1 = new JButton("Button 1");
        panelTopo.add(button1);
        JButton button2 = new JButton("Button 2");
        panelTopo.add(button2);
        JButton button3 = new JButton("Button 3");
        panelTopo.add(button3);
        JButton button4 = new JButton("Button 4");
        panelTopo.add(button4);

        JPanel panelSudoku = new JPanel(new GridLayout(3, 3, 5, 5));
        janela.add(panelSudoku, BorderLayout.CENTER);
        //Container panelSudoku = getContentPane();
        //panelSudoku.setLayout(new GridLayout(3, 3,5,5));


        // Allocate a common listener as the ActionEvent listener for all the
        //  JTextFields
        // ... [TODO 1] (Later) ....

        JTextField[][] fields;       // Array of fields.
        JPanel[][] panels;      // Panels holding the fields.

        panels = new JPanel[3][3];
        fields = new JTextField[3][3];
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                panels[x][y] = new JPanel(new GridLayout(3, 3));
                panels[x][y].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                for (int a = 0; a < 3; a++) {
                    for (int b = 0; b < 3; b++) {
                        fields[a][b] = new JTextField();
                        panels[x][y].add(fields[a][b]);

                        // Beautify all the cells
                        fields[a][b].setHorizontalAlignment(JTextField.CENTER);
                        fields[a][b].setFont(FONT_NUMBERS);
                    }
                }
                panelSudoku.add(panels[x][y]);
            }
        }


        // Set the size of the content-pane and pack all the components
        //  under this container.
        panelSudoku.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
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
