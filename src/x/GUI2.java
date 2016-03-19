package x;

import javax.swing.*;
import java.awt.*;

// FROM http://stackoverflow.com/questions/19072690/java-sudoku-gui-board


public class GUI2 extends JFrame{
//    private static final long serialVersionUID = 0;
    private JTextField ft[][]= new JTextField[9][9] ;
    private JPanel pan[][]= new JPanel [3][3];

    public GUI2(){

        for(int x=0; x<=8; x++){
            for(int y=0; y<=8; y++){
                ft[x][y]=new JTextField(1);
            }
        }

        for(int x=0; x<=2; x++){
            for(int y=0; y<=2; y++){
                pan[x][y]=new JPanel(new GridLayout(3,3));
            }
        }

        setLayout(new GridLayout(3,3,5,5)); // rows, cols, hgap, vgap

        for(int u=0; u<=2; u++){
            for(int i=0; i<=2; i++){
                for(int x=0; x<=2; x++ ){
                    for(int y=0; y<=2; y++){
                        pan[u][i].add(ft[y+u*3][x+i*3]);
                    }
                }
                add(pan[u][i]);
            }
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Handle window closing
        setTitle("Sudoku");
        setVisible(true);
    }

    public static void main(String[] args) {
        // [TODO 3] (Now)
        new GUI2();
    }
}
