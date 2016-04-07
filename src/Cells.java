import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by dinis on 06/04/2016.
 */
public class Cells extends JTextField {
    int linhas,colunas;
    public void setLinhas(int a)
    {
        linhas=a;
    }
    public void setColunas(int a)
    {
        colunas=a;
    }
    public int getColunas()
    {
        return colunas;
    }
    public int getLinhas()
    {
        return linhas;
    }
}
