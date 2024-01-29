
package declaracionanual_impuestos;
import java.util.Scanner;
//import java.lang.Math;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
public class DeclaracionAnual_Impuestos {
    static String[] categoria = {"Vivienda", "Educacion", "Alimentacion", "Vestimenta", "Salud", "Turismo"};
    static double iess;
    static double maxDeductRate = 0.18;
    static boolean earnsDividends;
    static boolean showTaxTable;
    static double[][] facturas = new double[12][6];
    static double[] sueldos = new double[12];
    static double totalIngresos = 0;
    static double totalDeducciones = 0;
    static double  impBasico,  impExcedente,  impExcedentePagar,  impTotal ;
    static int año = 2023;
    
    static BufferedReader leer;

    static BufferedWriter escribir;
    
        static {
        try {
            leer = new BufferedReader(new FileReader("Tablas Impositivas 2023.csv"));
            escribir = new BufferedWriter(new FileWriter("Declaracion-Impuesto del usuario.txt"));
        } catch (IOException e) {
            e.getMessage();
        }
    }
        


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        
        //File f = new File("Tablas Impositivas 2023.csv");
        System.out.println("Ingresa tu nombre: ");
        String nombre = scanner.nextLine();
        totalIngresos = ingresarSueldos(sueldos);
        totalDeducciones = ingresarFacturas(facturas, categoria, maxDeductRate);

        //Test-line: totalDeducciones = 5352.97;
        if (totalIngresos < 0 || totalDeducciones < 0) {
            System.out.println("Los ingresos y las deducciones no pueden ser negativos.");
            return;
        }
        double baseImponible = totalIngresos - totalDeducciones;
        double refund = 0;
        if (baseImponible<0){
           refund = Math.abs(baseImponible);
        }
            
        System.out.println("Obtiene usted ingresos por pertenecer a alguna sociedad o corporacion que le distribuye dividendos o utilidades? (true o false)");
        earnsDividends = scanner.nextBoolean();
        double retornoImpuestos = 0;
        if (earnsDividends) {
            System.out.println("Cuanto $ en dividendos usted recibe anualmente");
            double dividends = scanner.nextDouble();
            System.out.println("Cual es la base impositiva ( en %) que paga su sociedad por distribuir cada dividendo?");
            double dividendsTaxRate = scanner.nextDouble();
            retornoImpuestos = dividends * (dividendsTaxRate * 0.01);
        }
        
        calcularImpuesto(baseImponible);
        generarDeclaracion(nombre, totalIngresos, totalDeducciones, baseImponible, impExcedentePagar, impExcedente, impBasico, impTotal, iess, retornoImpuestos, refund);
        System.out.println("Desea obtener y guardar su reporte de la declaracion actual");
        boolean decision = scanner.nextBoolean();
        if (decision){
            guardarReporte(nombre, totalIngresos, totalDeducciones, baseImponible, impExcedentePagar, impExcedente, impBasico, impTotal, iess, retornoImpuestos, refund);
        }
        System.out.println("Desea conocer la tabla de Impuesto a la Renta para Personas Naturales (2023)? (true o false)");
        showTaxTable = scanner.nextBoolean();
        if (showTaxTable) {
            taxTable(año);
        }
        System.out.println("");
        System.out.println("GRACIAS POR USAR NUESTRO SISTEMA :3");
    }

    public static double ingresarSueldos(double sueldos[]) {
       // Java lee los enteros de un .csv sin problemas
        try {
            Scanner income = new Scanner(new File("MonthlyIncome.csv"));
                    for (int mes = 0; mes < 12; mes++) {
            System.out.println("Ingrese su sueldo del mes " + (mes + 1) + ": ");
            sueldos[mes] = income.nextDouble();
            totalIngresos += sueldos[mes];
        }
        iess = totalIngresos * 0.1145;
        totalIngresos -= iess;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DeclaracionAnual_Impuestos.class.getName()).log(Level.SEVERE, null, ex);
        }

        return totalIngresos;
    }

    public static double ingresarFacturas(double [][]factura, String []categoria, double maxDeductRate) {
        //Java al leer doubles de un .csv los interpreta primero como Strings, por ello debemos convertirlos
        double[][] linea = new double[12][6];  
        
    try {
        String line;
        int row = 0;
        BufferedReader receipts = new BufferedReader(new FileReader("Facturas.csv"));
        while ((line = receipts.readLine()) != null && row < 12) {
            String[] stringValues = line.split(",");  // Assuming your values are separated by tabs
            for (int col = 0; col < stringValues.length && col < 6; col++) {
                linea[row][col] = Double.parseDouble(stringValues[col]);
            }
            row++;
        }
/* Printing values (just for verification)
        for (double[] rowValues : linea) {
            for (double cell : rowValues) {
                System.out.printf("%f\t", cell);
            }
            System.out.println();
        }*/

        receipts.close();
        
        for (int mes = 0; mes < 12; mes++) {
            for (int cat = 0; cat < 6; cat++) {
                System.out.println("Ingrese el total en costo de facturas de " + categoria[cat] + " del mes " + (mes + 1) + ": ");
                facturas[mes][cat] = linea[mes][cat];
                totalDeducciones += facturas[mes][cat];
            }
        }
        if (totalDeducciones > 5352.97) {
            totalDeducciones = 5352.97;
        }
        totalDeducciones *= maxDeductRate;

    } catch (IOException e) {
        e.printStackTrace();
    }
            
  
         return totalDeducciones;

    }

    public static void generarDeclaracion(String nombre, double totalIngresos, double totalDeducciones, double baseImponible, double impExcedentePagar, double impExcedente, double impBasico, double impTotal, double iess, double retornoImpuestos, double refund) {
        System.out.println("Estimado/a " + nombre);
        System.out.println("Total de ingresos: " + totalIngresos);
        System.out.println("Total de deducciones: " + totalDeducciones);
        System.out.println("-------------------------------------------------");
        System.out.println("Sus ingresos netos son: " + baseImponible);
        System.out.println("*");
        System.out.println("Porcentaje que usted pagara de impuesto: " + impExcedente);
        System.out.println("-------------------------------------------------");
        System.out.println("Impuesto de Fraccion Excedente a pagar: " + impExcedentePagar);
        System.out.println("Impuesto de Fraccion Basica a pagar: " + impBasico);
        System.out.println("");
        System.out.println("Total de Impuesto a pagar: " + impTotal);
        System.out.println("-------------------------------------------------");
        System.out.println("Informacion adicional:");
        System.out.println("Aporte al IESS: " + iess);
        System.out.println("Credito tributario o devolucion (por dividendos Corporativos): " + retornoImpuestos + "$");
        System.out.println("Devolucion por Excedente de impuestos: (Ingresos netos negativos) $"+refund);
        System.out.println("-------------------------------------------------");
}

    public static void calcularImpuesto(double baseImponible) {
        if (baseImponible > 0 && baseImponible <= 11722) {
            impBasico = 0;
            impExcedente = 0;
        } else if (baseImponible > 11722 && baseImponible <= 14930) {
            impBasico = 0;
            impExcedente = 0.05;
        } else if (baseImponible > 14930 && baseImponible <= 19385) {
            impBasico = 160;
            impExcedente = 0.1;
        } else if (baseImponible > 19385 && baseImponible <= 25638) {
            impBasico = 606;
            impExcedente = 0.12;
        } else if (baseImponible > 25638 && baseImponible <= 33738) {
            impBasico = 1356;
            impExcedente = 0.15;
        } else if (baseImponible > 33738 && baseImponible <= 44721) {
            impBasico = 2571;
            impExcedente = 0.2;
        } else if (baseImponible > 44721 && baseImponible <= 59537) {
            impBasico = 4768;
            impExcedente = 0.25;
        } else if (baseImponible > 59537 && baseImponible <= 79388) {
            impBasico = 8472;
            impExcedente = 0.3;
        } else if (baseImponible > 79388 && baseImponible <= 105580) {
            impBasico = 14427;
            impExcedente = 0.35;
        } else if (baseImponible > 105580) {
            impBasico = 23594;
            impExcedente = 0.37;
        }
        impExcedentePagar = baseImponible * impExcedente;
        impBasico = impBasico;
        impTotal = impBasico + impExcedentePagar;
    }

    public static void taxTable(int año) {
        
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
    public static void guardarReporte(String nombre, double totalIngresos, double totalDeducciones, double baseImponible, double impExcedentePagar, double impExcedente, double impBasico, double impTotal, double iess, double retornoImpuestos, double refund){
       try {
        escribir.write("Estimado/a," + nombre);
        escribir.newLine();
        escribir.write("Total de ingresos:," + totalIngresos);
        escribir.newLine();
        escribir.write("Total de deducciones:," + totalDeducciones);
        escribir.newLine();
        escribir.write("-------------------------------------------------");
        escribir.newLine();
        escribir.write("Sus ingresos netos son:," + baseImponible);
        escribir.newLine();
        escribir.write("*");
        escribir.newLine();
        escribir.write("Porcentaje que usted pagara de impuesto:," + impExcedente);
        escribir.newLine();
        escribir.write("-------------------------------------------------");
        escribir.newLine();
        escribir.write("Impuesto de Fraccion Excedente a pagar:," + impExcedentePagar);
        escribir.newLine();
        escribir.write("Impuesto de Fraccion Basica a pagar:," + impBasico);
        escribir.newLine();
        escribir.write("");
        escribir.newLine();
        escribir.write("Total de Impuesto a pagar:," + impTotal);
        escribir.newLine();
        escribir.write("-------------------------------------------------");
        escribir.newLine();
        escribir.write("Informacion adicional:");
        escribir.newLine();
        escribir.write("Aporte al IESS:," + iess);
        escribir.newLine();
        escribir.write("Credito tributario o devolucion (por dividendos Corporativos):," + retornoImpuestos + "$");
        escribir.newLine();
        escribir.write("Devolucion por Excedente de impuestos: (Ingresos netos negativos),$"+refund);
        escribir.newLine();
        escribir.write("-------------------------------------------------");
        escribir.newLine();

        escribir.close();

    } catch (IOException e) {
        e.printStackTrace();
    }
}
}

    

