
package Files;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
//import java.utill.;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
        
public class EstePro2 {

    public static void main(String[] args) {
        Scanner leer;
        try {
            leer = new Scanner(new File("opera2.csv"));
            
            while (leer.hasNext()) {
                String c[]= leer.next().split(";");
                float n1 = Float.valueOf(c[1]);
                float n2 = Float.valueOf(c[2]);
            /*System.out.println(c);
            for (String c3 : c2) {
                System.out.println(c3);*/
           // System.out.println(Arrays.toString(c));
                switch (c[0]) {
                    case "suma": System.out.println(c[0]+":"+(n1+n2));
                        
                        break;
                    case "resta": System.out.println(c[0]+":"+(n1-n2));
                        break;
                    case "mult": System.out.println(c[0]+":"+(n1*n2));
                        break;
                    case "div": System.out.println(c[0]+":"+(n1/n2));
                        break;
                    case "pot": System.out.println(c[0]+":"+(Math.pow(n1,n2)));
                    break;
                    case "raiz": System.out.println(c[0]+":"+ Math.round(Math.pow(n1, 1.0 / n2)));
                    break;
                    default:
                        throw new AssertionError();
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EstePro.class.getName()).log(Level.SEVERE, null, ex);
        }

        }
    }
    

