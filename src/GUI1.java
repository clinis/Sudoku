import javax.swing.*;
import java.awt.*;
import java.io.IOException;


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


    private JTextField[][] cels = new JTextField[GRID_SIZE][GRID_SIZE];


    /**
     * Constructor to setup the game and the UI Components
     */
    public GUI1() throws IOException {
        //Container janela = getContentPane();
        //janela.setLayout(new BorderLayout());
        JFrame janela = new JFrame();
        janela.setLayout(new BorderLayout());

        JPanel panelTopo = new JPanel(new FlowLayout());
        janela.add(panelTopo, BorderLayout.NORTH);

        JButton button1 = new JButton("Button 1"),
                button2 = new JButton("Button 2"),
                button3 = new JButton("Button 3"),
                button4 = new JButton("Button 4");
        panelTopo.add(button1);
        panelTopo.add(button2);
        panelTopo.add(button3);
        panelTopo.add(button4);

        JPanel panelSudoku = new JPanel(new GridLayout(3, 3, 1, 1));
        janela.add(panelSudoku, BorderLayout.CENTER);
        //Container panelSudoku = getContentPane();
        //panelSudoku.setLayout(new GridLayout(3, 3,5,5));


        // Allocate a common listener as the ActionEvent listener for all the
        //  JTextFields
        // ... [TODO 1] (Later) ....

        JTextField[][] tfields;       // Array of fields.
        JPanel[][] regions;      // Panels holding the fields.

        regions = new JPanel[3][3];
        tfields = new JTextField[3][3];
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                regions[x][y] = new JPanel(new GridLayout(3, 3));
                //regions[x][y].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

                for (int a = 0; a < 3; a++) {
                    for (int b = 0; b < 3; b++) {
                        tfields[a][b] = new JTextField();

                        regions[x][y].add(tfields[a][b]);
                        //-----
                        String[] temp = obterJogo.deLinhaPara9x9(obterJogo.PuzzleOUSolucao(1,0));
                        //for (int s = 0; s < 9; s++) {
                        //    for (int ss = 0; ss < 9; ss++) {
                        tfields[a][b].setText(String.valueOf(temp[a].charAt(b)));
                        //    }
                        //}
                        //------

                        // Beautify all the cells
                        tfields[a][b].setHorizontalAlignment(JTextField.CENTER);
                        tfields[a][b].setFont(FONT_NUMBERS);
                    }
                }
                panelSudoku.add(regions[x][y]);
            }
        }


        // Set the size of the content-pane and pack all the components
        //  under this container.
        panelSudoku.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        janela.pack();

        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Handle window closing
        janela.setTitle("Sudoku");
        janela.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        new GUI1();
    }
}
