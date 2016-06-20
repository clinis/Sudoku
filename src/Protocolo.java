import java.io.*;

class Protocolo implements Serializable {
    Object arg1 = null;
    Object arg2 = null;

    void envia(ObjectOutputStream out) throws Exception {
        out.writeObject(this);
        out.flush();
        out.reset();
    }
    static Protocolo recebe(ObjectInputStream in) throws Exception {
        return (Protocolo)in.readObject();
    }
}