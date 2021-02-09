package tool;

import java.io.*;

public class ObjectWR {
    private ObjectWR() { ; }

    public static void writeNS(Object obj, FileOutputStream file) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(obj);
        out.close();
    }

    public static Object readNS(FileInputStream file) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(file);
        return in.readObject();
    }

    public static void writeLY(Object obj, DataOutputStream file) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(file);
        out.writeObject(obj);
        out.close();
    }

    public static Object readLY(DataInputStream file) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(file);
        return in.readObject();
    }
}
