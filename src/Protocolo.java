import java.io.*;

public class Protocolo implements Serializable {
    Object arg1 = null;
    Object arg2 = null;

    public void envia(ObjectOutputStream out) throws Exception {
        out.writeObject(this);
        out.flush(); // para quem recebe perceber que acabou
        out.reset(); // para actualizar quando se envia coisas parecidas
    }
    public static Protocolo recebe(ObjectInputStream in) throws Exception {
        return (Protocolo)in.readObject();
    }
}