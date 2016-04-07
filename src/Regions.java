import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;


public class Regions extends JPanel{

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

    int jogoIndex = obterJogo.escolherJogoIndexAleatoriamente();
    String[] jogoPuzzle = obterJogo.deLinhaPara9x9(obterJogo.PuzzleOUSolucao(jogoIndex, 0));
    public Cells[][] cels = new Cells[GRID_SIZE][GRID_SIZE];
    public int selector=-2,selector2=-2,line,col;

   Regions() throws IOException {
       setLayout(new GridLayout(3, 3));
       setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
       for( line = 0; line < GRID_SIZE; ++line) {
           for ( col = 0; col < GRID_SIZE; ++col) {
               if (String.valueOf(jogoPuzzle[line].charAt(col)).equals(".")) {
                   cels[line][col].setEditable(false);
                   cels[line][col].setBackground(OPEN_CELL_BGCOLOR);

                   cels[line][col].setText("  ");
                   // Add ActionEvent listener to process the input
                   // [TODO 2]
                   //cels[line][col].addActionListener(new ButtonListener());

               } else {
                   cels[line][col].setText(String.valueOf(jogoPuzzle[line].charAt(col)));
                   cels[line][col].setEditable(false);
                   cels[line][col].setBackground(CLOSED_CELL_BGCOLOR);
                   cels[line][col].setForeground(CLOSED_CELL_TEXT);
               }
               add(cels[line][col]);

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
               cels[line][col].addMouseListener(new MouseAdapter(){
                   public void mouseClicked(MouseEvent e)
                   {
                       selector=cels[line][col].getLinhas();
                       selector2=cels[line][col].getColunas();
                   }
               });
           }
       }

       obterJogo.imprimirSolucao(jogoIndex);
   }
}
