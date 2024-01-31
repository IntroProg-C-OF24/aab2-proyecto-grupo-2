package Files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Formatter;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Arrays;
import java.util.Locale;

public class Act1Modularized_ForB2Test {

    public static void main(String[] args) {

        String inData[][] = new String[100][3];
        String outData[][] = new String[100][4];

        generarOperacionesEntrada();
        leerOperaciones(inData);
        calcularOperaciones(inData, outData);
        escribirFileOutput(outData);
    }

    public static void generarOperacionesEntrada() {
        try {
            Random rand = new Random();
            Formatter escribir = new Formatter("operaEntrada.csv", "UTF-8",Locale.US);
            String operaArray[] = {"suma", "resta", "mult", "div", "pot", "raiz"};
            for (int i = 0; i < 100; i++) {
                //String saludo = "Holaaa;div";
                int r = rand.nextInt(6 - 0) + 0;
                double n1 = 1 + rand.nextDouble() * (10 - 1);
                double n2 = rand.nextDouble() * (10 - 1);
                escribir.format("%s;%.2f;%.2f\n", operaArray[r], n1, n2);
            }
            escribir.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void leerOperaciones(String inData[][]) {
        try {
            Scanner leer = new Scanner(new File("operaEntrada.csv"));
            int i = 0;
            while (leer.hasNext()) {

                String data[] = leer.nextLine().split(";");
                System.out.println(Arrays.toString(data));
                inData[i][0] = data[0];
                inData[i][1] = data[1];
                inData[i][2] = data[2];
                i++;
            }
            // Test line
            /*for (int j = 0; j < inData.length ; j++){
                System.out.println(inData[j][0]+"\t"+inData[j][1]+"\t"+inData[j][2]);
            }
             */
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Act1Modularized_ForB2Test.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void calcularOperaciones(String[][] inData, String outData[][]) {

        for (int i = 0; i < inData.length; i++) {

            double n1 = Double.valueOf(inData[i][1]);
            double n2 = Double.valueOf(inData[i][2]);
                outData[i][0] = inData[i][0];
                outData[i][1] = inData[i][1];
                outData[i][2] = inData[i][2];
                switch (inData[i][0]) {
                case"suma":
                    outData[i][3] = String.valueOf((n1 + n2));
                    break;
                case "resta":
                    outData[i][3] = String.valueOf(n1 - n2);
                    break;
                case "mult":
                    outData[i][3] = String.valueOf(n1 * n2);
                    break;
                case "div":
                    outData[i][3] = String.valueOf(n1 / n2);
                    break;
                case "pot":
                    outData[i][3] = String.valueOf(Math.pow(n1, n2));
                    break;
                case "raiz":
                    outData[i][3] = String.valueOf(Math.pow(n1, 1.0 / n2));
                    break;
                default:
                    break;
                            
            }
        }
    }

    public static void escribirFileOutput(String outData[][]) {
        try {
            Formatter escribo2 = new Formatter("operaResultado.csv","UTF-8",Locale.US);
            for (int i = 0; i < outData.length; i++) {
                for (int j = 0; j < outData[i].length; j++) {
                    escribo2.format("%s;", outData[i][j]);
                }
                escribo2.format("\n");
            }
            //                     escribo2.format("%s;%s;%s;%s", outData[i][j], outData[i][1], outData[i][2], outData[i][3]);

            escribo2.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Act1Modularized_ForB2Test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Act1Modularized_ForB2Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}

      //        outData[i][1]= String.parseDouble(inData[i][1]);
