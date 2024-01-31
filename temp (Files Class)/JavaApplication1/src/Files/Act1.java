
package Files;

import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;

public class Act1 {

    public static void main(String[] args) {
        try {
            Random rand = new Random();
            Formatter escribir = new Formatter("operaEntrada.csv");
            String operaArray[] = {"suma", "resta", "mult","div", "pot","raiz"};
            for (int i = 0; i < 100; i++) {
                //String saludo = "Holaaa;div";
                int r = rand.nextInt(6-0)+0;
                double n1 = 1 + rand.nextDouble() * (10 - 1);
                double n2 = rand.nextDouble()*(10-1);
                escribir.format(" %s;%.2f;%.2f\n", operaArray[r],n1,n2);

            }
            escribir.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Act1.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
