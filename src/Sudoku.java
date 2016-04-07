import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.io.IOException;

public class Sudoku extends JFrame{

    ArrayList<Regions> reg;
    JPanel main = new JPanel(new GridLayout(3,3));
    int jogoIndex = obterJogo.escolherJogoIndexAleatoriamente();
    String[] jogoPuzzle = obterJogo.deLinhaPara9x9(obterJogo.PuzzleOUSolucao(jogoIndex, 0));

    Sudoku() throws IOException{
        setTitle("Sudoku");
        setSize(500,500);

        reg = new ArrayList<Regions>();

        for(int i=0; i<9; i++){
            reg.add(new Regions(i));
            main.add(reg.get(i));
        }
        for(int line=0; line<9; line++){
            for(int col=0; col<9; col++){
                int auxili,auxili1;

                auxili = converttoCel(line,col);
                auxili1 = converttoReg(line,col);

                if(String.valueOf(jogoPuzzle[line].charAt(col)).equals(".")){
                    reg.get(auxili1).cels.get(auxili).b.setText("  ");
                }
                else{
                    reg.get(auxili1).cels.get(auxili).b.setText(String.valueOf(jogoPuzzle[line].charAt(col)));
                    reg.get(auxili1).cels.get(auxili).b.setBackground(Color.YELLOW);
                }
            }
        }

        add(main);
        obterJogo.imprimirSolucao(jogoIndex);
    }

    public int converttoCel(int line,int col){
        int a=-10;
        switch(line)
        {
            case 0:
            {
                switch(col)
                {
                    case 0:case 3:case 6:
                    a=0;break;
                    case 1: case 4: case 7:
                    a=1;break;
                    case 2: case 5: case 8:
                    a=2;break;
                }
                break;
            }
            case 1:
            {
                switch(col)
                {
                    case 0:case 3:case 6:
                    a=3;break;
                    case 1: case 4: case 7:
                    a=4;break;
                    case 2: case 5: case 8:
                    a=5;break;
                }
                break;
            }
            case 2:
            {
                switch(col)
                {
                    case 0:case 3:case 6:
                    a=6;break;
                    case 1: case 4: case 7:
                    a=7;break;
                    case 2: case 5: case 8:
                    a=8;break;
                }
                break;
            }
            case 3:
            {
                switch(col)
                {
                    case 0:case 3:case 6:
                    a=0;break;
                    case 1: case 4: case 7:
                    a=1;break;
                    case 2: case 5: case 8:
                    a=2;break;
                }
                break;
            }
            case 4:
            {
                switch(col)
                {
                    case 0:case 3:case 6:
                    a=3;break;
                    case 1: case 4: case 7:
                    a=4;break;
                    case 2: case 5: case 8:
                    a=5;break;
                }
                break;
            }
            case 5:
            {
                switch(col)
                {
                    case 0:case 3:case 6:
                    a=6;break;
                    case 1: case 4: case 7:
                    a=7;break;
                    case 2: case 5: case 8:
                    a=8;break;
                }
                break;
            }
            case 6:
            {
                switch(col)
                {
                    case 0:case 3:case 6:
                    a=0;break;
                    case 1: case 4: case 7:
                    a=1;break;
                    case 2: case 5: case 8:
                    a=2;break;
                }
                break;
            }
            case 7:
            {
                switch(col)
                {
                    case 0:case 3:case 6:
                    a=3;break;
                    case 1: case 4: case 7:
                    a=4;break;
                    case 2: case 5: case 8:
                    a=5;break;
                }
                break;
            }
            case 8:
            {
                switch(col)
                {
                    case 0:case 3:case 6:
                    a=6;break;
                    case 1: case 4: case 7:
                    a=7;break;
                    case 2: case 5: case 8:
                    a=8;break;
                }
                break;
            }
        }
        return a;
    }

    public int converttoReg(int line,int col){
        int a = -10;

        switch(line){
            case 0:
            {
                switch(col)
                {
                    case 0:case 1:case 2:
                    a=0;break;
                    case 3: case 4: case 5:
                    a=1;break;
                    case 6: case 7: case 8:
                    a=2;break;
                }
                break;
            }
            case 1:
            {
                switch(col)
                {
                    case 0:case 1:case 2:
                    a=0;break;
                    case 3: case 4: case 5:
                    a=1;break;
                    case 6: case 7: case 8:
                    a=2;break;
                }
                break;
            }
            case 2:
            {
                switch(col)
                {
                    case 0:case 1:case 2:
                    a=0;break;
                    case 3: case 4: case 5:
                    a=1;break;
                    case 6: case 7: case 8:
                    a=2;break;
                }
                break;
            }
            case 3:
            {
                switch(col)
                {
                    case 0:case 1:case 2:
                    a=3;break;
                    case 3: case 4: case 5:
                    a=4;break;
                    case 6: case 7: case 8:
                    a=5;break;
                }
                break;
            }
            case 4:
            {
                switch(col)
                {
                    case 0:case 1:case 2:
                    a=3;break;
                    case 3: case 4: case 5:
                    a=4;break;
                    case 6: case 7: case 8:
                    a=5;break;
                }
                break;
            }
            case 5:
            {
                switch(col)
                {
                    case 0:case 1:case 2:
                    a=3;break;
                    case 3: case 4: case 5:
                    a=4;break;
                    case 6: case 7: case 8:
                    a=5;break;
                }
                break;
            }
            case 6:
            {
                switch(col)
                {
                    case 0:case 1:case 2:
                    a=6;break;
                    case 3: case 4: case 5:
                    a=7;break;
                    case 6: case 7: case 8:
                    a=8;break;
                }
                break;
            }
            case 7:
            {
                switch(col)
                {
                    case 0:case 1:case 2:
                    a=6;break;
                    case 3: case 4: case 5:
                    a=7;break;
                    case 6: case 7: case 8:
                    a=8;break;
                }
                break;
            }
            case 8:
            {
                switch(col)
                {
                    case 0:case 1:case 2:
                    a=6;break;
                    case 3: case 4: case 5:
                    a=7;break;
                    case 6: case 7: case 8:
                    a=8;break;
                }
                break;
            }
        }
        return a;
    }

    public static void main(String [] args) throws IOException{
        new Sudoku().setVisible(true);
    }
}