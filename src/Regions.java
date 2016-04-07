import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class Regions extends JPanel{

    public int regionro = -1, selector = -1;
    public int i;
    ArrayList<Cells> cels;

    Regions(int a){
        setLayout(new GridLayout(3,3));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cels = new ArrayList<Cells>();
        regionro = a;

        for(i=0; i<9; i++){
            cels.add(new Cells(i));
            add(cels.get(i));
        }
    }
}