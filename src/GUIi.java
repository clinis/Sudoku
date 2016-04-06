import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


public class GUIi extends JFrame {
    //h
    public int line, col,linharel,linhabs,colrel,colabs;
    //h

    // Name-constants for the game properties
    public static final int GRID_SIZE = 9;    // Size of the board
    public static final int SUBGRID_SIZE = 3; // Size of the sub-grid
    // Name-constants for UI control (sizes, colors and fonts)
    public static final int CELL_SIZE = 60;   // Cell width/height in pixels
    public static final int CANVAS_WIDTH = CELL_SIZE * GRID_SIZE;
    public static final int CANVAS_HEIGHT = CELL_SIZE * GRID_SIZE;
    // Board width/height in pixels
    public static final Color OPEN_CELL_BGCOLOR = Color.YELLOW;
    public static final Color OPEN_CELL_TEXT_YES = new Color(0, 255, 0);  // RGB
    public static final Color OPEN_CELL_TEXT_NO = Color.RED;
    public static final Color CLOSED_CELL_BGCOLOR = new Color(240, 240, 240); // RGB
    public static final Color CLOSED_CELL_TEXT = Color.BLACK;
    public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);



    /**
     * Constructor to setup the game and the UI Components
     */
    public GUIi() throws IOException {

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

        button4.addActionListener(new dial());

        JPanel panelSudoku = new JPanel(new GridLayout(3, 3, 1, 1));
        janela.add(panelSudoku, BorderLayout.CENTER);
        Regions[][] regions = new Regions[3][3];
        panelSudoku.getInputMap().put(KeyStroke.getKeyStroke("1"),"um");
        panelSudoku.addKeyListener(new KeyAdapter() {
            /**
             * Invoked when a key has been pressed.
             *
             * @param e
             */
            public void keyPressed(KeyEvent e) {

                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if(regions[i][j].selector !=-2 && regions[i][j].selector2!=-2)
                        {
                            regions[i][j].cels[regions[i][j].selector][regions[i][j].selector2].setText("ok");
                        }
                        panelSudoku.add(regions[i][j]);
                    }
                }
            }
        });

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                regions[i][j] = new Regions();
                panelSudoku.add(regions[i][j]);
            }
        }

        // Allocate a common listener as the ActionEvent listener for all the
        // JTextFields
        // [TODO 1]

        // Set the size of the content-pane and pack all the components under this container.
        panelSudoku.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        janela.pack();

        janela.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  //janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setTitle("Sudoku");
        janela.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        new GUIi();
    }

    public class dial implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, "....from the other side!!!", "Hello...", 1);
        }
    }
}
