
package Files;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
//import java.utill.;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
        
public class EstePro {

    public static void main(String[] args) {
        Scanner leer;
        try {
            leer = new Scanner(new File("opera2.csv"));
            
            while (leer.hasNext()) {
            
            String c2[]= leer.next().split(";");
            /*System.out.println(c);
            for (String c3 : c2) {
                System.out.println(c3);*/
            System.out.println(Arrays.toString(c2));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EstePro.class.getName()).log(Level.SEVERE, null, ex);
        }

        }
    }
    

