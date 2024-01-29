package declaracionanual_impuestos;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class temp {

    static BufferedReader leer;
    static BufferedWriter escribir;

    static {
        try {
            leer = new BufferedReader(new FileReader("Tablas Impositivas 2023.csv"));
            escribir = new BufferedWriter(new FileWriter("Declaracion-Impuesto del usuario.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        List<String[]> linea = new ArrayList<>();

        try {
            String line;
            while ((line = leer.readLine()) != null) {
                linea.add(line.split(","));
            }

            for (String[] row : linea) {
                for (String cell : row) {
                    System.out.printf("%s\t", cell);
                }
                System.out.println();
            }

            leer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
