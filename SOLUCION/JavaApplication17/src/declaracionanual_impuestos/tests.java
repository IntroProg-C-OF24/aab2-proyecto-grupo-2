package declaracionanual_impuestos;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class tests{

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
        String[][] linea = null;
        
        try {
                for (int i = 0;linea[i] != null ; i++) {
                    for (int j = 0; linea[i][j] != null; j++) {
                        linea[i][j] = leer.readLine();
                        System.out.printf("\n%s\t", linea);
                    }
            }
           
             
            leer.close();
            
        } catch (IOException e) {
            e.printStackTrace();
    }
    }
}



