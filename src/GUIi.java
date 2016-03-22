import javax.swing.*;
import java.awt.*;
import java.io.IOException;


public class GUIi extends JFrame {
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
    public GUIi() throws IOException {
        int jogoIndex = obterJogo.escolherJogoIndexAleatoriamente();

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

        JPanel[][] regions = new JPanel[3][3];
        for ( int i = 0; i < 3; i++){
            for ( int j=0; j < 3; j++){
                regions[i][j] = new JPanel(new GridLayout(3, 3));
                regions[i][j].setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                panelSudoku.add(regions[i][j]);
            }
        }

        String[] jogoPuzzle = obterJogo.deLinhaPara9x9(obterJogo.PuzzleOUSolucao(jogoIndex,0));

        // Allocate a common listener as the ActionEvent listener for all the
        //  JTextFields
        // ... [TODO 1] (Later) ....

        for (int line = 0; line < GRID_SIZE; ++line) {
            for (int col = 0; col < GRID_SIZE; ++col) {
                cels[line][col] = new JTextField(); // Allocate element of array
                //panelSudoku.add(cels[line][col]);   // ContentPane adds JTextField        // comentado pq regioes

                if (String.valueOf(jogoPuzzle[line].charAt(col)).equals(".")) {
                    cels[line][col].setEditable(true);
                    cels[line][col].setBackground(OPEN_CELL_BGCOLOR);

                    // Add ActionEvent listener to process the input
                    // ... [TODO 2] (Later) ...

                } else {
                    cels[line][col].setText(String.valueOf(jogoPuzzle[line].charAt(col)));
                    cels[line][col].setEditable(false);
                    cels[line][col].setBackground(CLOSED_CELL_BGCOLOR);
                    cels[line][col].setForeground(CLOSED_CELL_TEXT);
                }
                regions[line/3][col/3].add(cels[line][col]);

/*                if ( (line / 3) * 3 + col / 3 == 0 ){
                    //cels[line][col].setBackground(Color.green);
                    regions[0][0].add(cels[line][col]);
                }
                else if ( (line / 3) * 3 + col / 3 == 1 ) {
                    //cels[line][col].setBackground(Color.black);
                    regions[0][1].add(cels[line][col]);
                }
                else if ( (line / 3) * 3 + col / 3 == 2 ){
                    //cels[line][col].setBackground(Color.blue);
                    regions[0][2].add(cels[line][col]);
                }
*/
                // Beautify all the cells
                cels[line][col].setHorizontalAlignment(JTextField.CENTER);
                cels[line][col].setFont(FONT_NUMBERS);
            }
        }
        obterJogo.imprimirSolucao(jogoIndex);

        // Set the size of the content-pane and pack all the components
        //  under this container.
        panelSudoku.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        janela.pack();

        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Handle window closing
        janela.setTitle("Sudoku");
        janela.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        // [TODO 3] (Now)
        new GUIi();
    }
}
