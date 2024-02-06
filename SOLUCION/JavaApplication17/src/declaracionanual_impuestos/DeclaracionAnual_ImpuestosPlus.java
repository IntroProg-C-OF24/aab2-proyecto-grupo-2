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
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeclaracionAnual_ImpuestosPlus {

    static String[] categoria = {"Vivienda", "Educacion", "Alimentacion", "Vestimenta", "Salud", "Turismo"};
    static double maxDeductRate = 0.18;
    static boolean earnsDividends;
    static boolean showTaxTable;
    static boolean paramAdicionales;
    static Random rGast = new Random();
    static Random rIngres = new Random();

    static int año = 2023;
    static double[][] facturas = new double[12][6];
    static double[] sueldos = new double[12];

    static BufferedReader leer;

    static BufferedWriter escribir;
    static DecimalFormat df;

    static {
        try {
            leer = new BufferedReader(new FileReader("Tablas Impositivas 2023.csv"));
            escribir = new BufferedWriter(new FileWriter("Declaracion-Impuesto del usuario.txt"));
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            df = new DecimalFormat("#.##", symbols);
        } catch (IOException e) {
            e.getMessage();
        }
    }
    static double[] impBasico;
    static double[] impExcedente;
    static double[] impExcedentePagar;
    static double[] impTotal;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, "UTF-8");

        //File f = new File("Tablas Impositivas 2023.csv");
        System.out.println("Cuantos contribuyentes desea registrar?");
        int taxPayers = scanner.nextInt();
        scanner.nextLine();
        String[] nombre = new String[taxPayers];

        double iess[] = new double[taxPayers];

        for (int i = 0; i < taxPayers; i++) {
            System.out.println("Ingresa el nombre de contribuyente: " + (i + 1));
            nombre[i] = scanner.nextLine();
        }

        // Creo los archivos de Ingresos y de Facturas
        generarIngresos(taxPayers);
        generarFacturas(taxPayers);

        double[] totalIngresosAux = new double[taxPayers];
        double[] totalDeduccionesArr = new double[taxPayers];
        double[] totalDeduccionesAux = new double[taxPayers];

        // Leeo e ingreso las ganancias y las Deducciones a partir de estos archivos antes creados
        double[] totalIngresos = ingresarSueldos(sueldos, taxPayers, totalIngresosAux, iess);
        double[] totalDeducciones = ingresarFacturas(facturas, categoria, maxDeductRate, taxPayers, totalDeduccionesAux);

        //Test-line: totalDeducciones = 5352.97;
        for (int i = 0; i < taxPayers; i++) {
            if (totalIngresos[i] < 0 || totalDeducciones[i] < 0) {
                System.out.println("Los ingresos y las deducciones no pueden ser negativos.");
                return; // acabar prog
            }
        }

        System.out.println("Desea ingresar datos adicionales? (pertenece a una corporacion / cosas no comunes");
        paramAdicionales = scanner.nextBoolean();

        double retornoImpuestos[] = new double[taxPayers];
        if (paramAdicionales == true) {
            double dividends[] = new double[taxPayers];
            double dividendsTaxRate[] = new double[taxPayers];
            for (int i = 0; i < taxPayers; i++) {
                System.out.println("Obtiene usted ingresos por pertenecer a alguna sociedad o corporacion que le distribuye dividendos o utilidades? (true o false)");
                earnsDividends = scanner.nextBoolean();
                scanner.nextLine(); //Limpiar Scanner para leer Strings en el metodo debajo
                if (earnsDividends) {
                    System.out.println("Cuanto $ en dividendos usted recibe anualmente");
                    dividends[i] = Double.parseDouble(scanner.nextLine());
                    System.out.println("Cual es la base impositiva ( en %) que paga su sociedad por distribuir cada dividendo?");
                    dividendsTaxRate[i] = Double.parseDouble(scanner.nextLine());
                    retornoImpuestos[i] = dividends[i] * (dividendsTaxRate[i] * 0.01);
                }
            }
        }
        double[] baseImponible = calcBaseImponible(taxPayers, totalIngresos, totalDeducciones);
        double[] refund = calcRefund(taxPayers, baseImponible);

        calcularImpuesto(baseImponible);
        generarDeclaracion(nombre, totalIngresos, totalDeducciones, baseImponible, impExcedentePagar, impExcedente, impBasico, impTotal, iess, retornoImpuestos, refund, taxPayers);
        System.out.println("Desea obtener y guardar su/s reporte de la/s declaracion/es actual/es");
        boolean decision = scanner.nextBoolean();
        if (decision) {
            guardarReporte(nombre, totalIngresos, totalDeducciones, baseImponible, iess, retornoImpuestos, refund, taxPayers);
        }

        System.out.println("Desea conocer la tabla de Impuesto a la Renta para Personas Naturales (2023)? (true o false)");
        showTaxTable = scanner.nextBoolean();
        if (showTaxTable) {
            taxTable(año);
        }
        System.out.println("");
        System.out.println("GRACIAS POR USAR NUESTRO SISTEMA :3");
    }

    public static void generarIngresos(int taxPayers) {
        try {
            for (int i = 0; i < taxPayers; i++) {
                String nombreArchivo = "IngresosContribuyente_" + (i + 1) + ".csv";
                Formatter escritura = new Formatter(nombreArchivo, "UTF-8", Locale.US);

                for (int j = 0; j < 12; j++) {
                    double ingresos = rIngres.nextDouble() * (500 + (4000 - 500));
                    escritura.format("%.2f\n", ingresos);
                }

                escritura.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void generarFacturas(int taxPayers) {
        for (int i = 0; i < taxPayers; i++) {
            String nombreArchivo = "Facturas_Contribuyente_" + (i + 1) + ".csv";
            try (BufferedWriter genExpense = new BufferedWriter(new FileWriter(nombreArchivo))) {
                //Meses
                for (int mes = 0; mes < 12; mes++) {
                    //Categorias
                    for (int cat = 0; cat < 6; cat++) {
                        double gastos = rGast.nextDouble() * (20 + (270 - 20));
                        genExpense.write(String.format(Locale.US, "%.2f", gastos));
                        if (cat < 5) {  
                            genExpense.write(",");
                        }
                    }
                    genExpense.newLine();  
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static double[] ingresarSueldos(double sueldos[], int taxPayers, double totalIngresosAux[], double[] iess) {
        // Java lee los enteros de un .csv sin problemas
        double[] totalIngresos = totalIngresosAux;
        for (int i = 0; i < taxPayers; i++) {
            String nombreArchivo = "IngresosContribuyente_" + (i + 1) + ".csv";
            try {
                BufferedReader income = new BufferedReader(new FileReader(nombreArchivo));
                String line;
                int mes = 0;
                while ((line = income.readLine()) != null && mes < 12) {
                    System.out.println("Ingrese su sueldo del mes " + (mes + 1) + ": ");
                    sueldos[mes] = Double.parseDouble(line.trim());
                    totalIngresosAux[i] += sueldos[mes];
                    System.out.println("El sueldo de este mes es de: " + sueldos[mes]);
                    mes++;
                }
                iess[i] = totalIngresosAux[i] * 0.1145;
                totalIngresosAux[i] -= iess[i];
                totalIngresos[i] = totalIngresosAux[i];
                income.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return totalIngresos;
    }

    public static double[] ingresarFacturas(double[][] factura, String[] categoria, double maxDeductRate, int taxPayers, double[] totalDeduccionesAux) {
        //Java al leer doubles de un .csv los interpreta primero como Strings, por ello debemos convertirlos
        double[][] linea = new double[12][6];

// Convertimos el archivo a una matriz local
        try {

            for (int i = 0; i < taxPayers; i++) {
                String line;
                int row = 0;
                String nombreArchivo = "Facturas_Contribuyente_" + (i + 1) + ".csv";
                BufferedReader receipts = new BufferedReader(new FileReader(nombreArchivo));
                while ((line = receipts.readLine()) != null && row < 12) {
                    String[] stringValues = line.split(",");  // 
                    for (int col = 0; col < stringValues.length && col < 6; col++) {
                        linea[row][col] = Double.parseDouble(stringValues[col]);
                    }
                    row++;
                }

                receipts.close();
// Desde aqui procesamos la matriz local
                for (int mes = 0; mes < 12; mes++) {
                    for (int cat = 0; cat < 6; cat++) {
                        System.out.println("Ingrese el total en costo de facturas de " + categoria[cat] + " del mes " + (mes + 1) + ": ");
                        facturas[mes][cat] = linea[mes][cat];
                        totalDeduccionesAux[i] += facturas[mes][cat];
                        System.out.println("El costo de la factura de este mes es de: " + facturas[mes][cat]);
                    }
                }
                                    if (totalDeduccionesAux[i] > 5352.97) {
                        totalDeduccionesAux[i] = 5352.97;
                    }
                    totalDeduccionesAux[i] *= maxDeductRate;
            }

        } catch (IOException e) {
        }

        return totalDeduccionesAux;

    }

    public static void generarDeclaracion(String[] nombre, double[] totalIngresos, double[] totalDeducciones, double[] baseImponible, double[] impExcedentePagar, double[] impExcedente, double[] impBasico, double[] impTotal, double[] iess, double[] retornoImpuestos, double[] refund, int taxPayers) {
        for (int i = 0; i < taxPayers; i++) {

            System.out.println("Estimado/a " + nombre[i]);
            System.out.println("Total de ingresos: " + totalIngresos[i]);
            System.out.println("Total de deducciones: " + totalDeducciones[i]);
            System.out.println("-------------------------------------------------");
            System.out.println("Sus ingresos netos son: " + baseImponible[i]);
            System.out.println("*");
            System.out.println("Porcentaje que usted pagara de impuesto: " + impExcedente[i]);
            System.out.println("-------------------------------------------------");
            System.out.println("Impuesto de Fraccion Excedente a pagar: " + impExcedentePagar[i]);
            System.out.println("Impuesto de Fraccion Basica a pagar: " + impBasico[i]);
            System.out.println("");
            System.out.println("Total de Impuesto a pagar: " + impTotal[i]);
            System.out.println("-------------------------------------------------");
            System.out.println("Informacion adicional:");
            System.out.println("Aporte al IESS: " + iess[i]);
            System.out.println("Credito tributario o devolucion (por dividendos Corporativos): " + retornoImpuestos[i] + "$");
            System.out.println("Devolucion por Excedente de impuestos: (Ingresos netos negativos) $" + refund[i]);
            System.out.println("-------------------------------------------------");
        }
    }

    public static void calcularImpuesto(double[] baseImponible) {
        impBasico = new double[baseImponible.length];
        impExcedente = new double[baseImponible.length];
        impExcedentePagar = new double[baseImponible.length];
        impTotal = new double[baseImponible.length];

        for (int i = 0; i < baseImponible.length; i++) {
            if (baseImponible[i] > 0 && baseImponible[i] <= 11722) {
                impBasico[i] = 0;
                impExcedente[i] = 0;
            } else if (baseImponible[i] > 11722 && baseImponible[i] <= 14930) {
                impBasico[i] = 0;
                impExcedente[i] = 0.05;
            } else if (baseImponible[i] > 14930 && baseImponible[i] <= 19385) {
                impBasico[i] = 160;
                impExcedente[i] = 0.1;
            } else if (baseImponible[i] > 19385 && baseImponible[i] <= 25638) {
                impBasico[i] = 606;
                impExcedente[i] = 0.12;
            } else if (baseImponible[i] > 25638 && baseImponible[i] <= 33738) {
                impBasico[i] = 1356;
                impExcedente[i] = 0.15;
            } else if (baseImponible[i] > 33738 && baseImponible[i] <= 44721) {
                impBasico[i] = 2571;
                impExcedente[i] = 0.2;
            } else if (baseImponible[i] > 44721 && baseImponible[i] <= 59537) {
                impBasico[i] = 4768;
                impExcedente[i] = 0.25;
            } else if (baseImponible[i] > 59537 && baseImponible[i] <= 79388) {
                impBasico[i] = 8472;
                impExcedente[i] = 0.3;
            } else if (baseImponible[i] > 79388 && baseImponible[i] <= 105580) {
                impBasico[i] = 14427;
                impExcedente[i] = 0.35;
            } else if (baseImponible[i] > 105580) {
                impBasico[i] = 23594;
                impExcedente[i] = 0.37;
            }
            impExcedentePagar[i] = baseImponible[i] * impExcedente[i];
            impTotal[i] = impBasico[i] + impExcedentePagar[i];

        }
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

    public static void guardarReporte(String[] nombre, double[] totalIngresos, double[] totalDeducciones, double[] baseImponible, double[] iess, double[] retornoImpuestos, double[] refund, int taxPayers) {
        try {
            for (int i = 0; i < taxPayers; i++) {
                String nombreArchivo = "Declaracion_Contribuyente_" + (i + 1) + ".csv";
                BufferedWriter output = new BufferedWriter(new FileWriter(nombreArchivo));

                output.write("Estimado/a," + nombre[i]);
                output.newLine();
                output.write("Total de ingresos:," + df.format(totalIngresos[i]));
                output.newLine();
                output.write("Total de deducciones:," + df.format(totalDeducciones[i]));
                output.newLine();
                output.write("-------------------------------------------------");
                output.newLine();
                output.write("Sus ingresos netos son:," + df.format(baseImponible[i]));
                output.newLine();
                output.write("*");
                output.newLine();
                output.write("Porcentaje que usted pagara de impuesto:," + df.format(impExcedente[i]));
                output.newLine();
                output.write("-------------------------------------------------");
                output.newLine();
                output.write("Impuesto de Fraccion Excedente a pagar:," + df.format(impExcedentePagar[i]));
                output.newLine();
                output.write("Impuesto de Fraccion Basica a pagar:," + df.format(impBasico[i]));
                output.newLine();
                output.write("");
                output.newLine();
                output.write("Total de Impuesto a pagar:," + df.format(impTotal[i]));
                output.newLine();
                output.write("-------------------------------------------------");
                output.newLine();
                output.write("Informacion adicional,");
                output.newLine();
                output.write("Aporte al IESS:;" + df.format(iess[i]));
                output.newLine();
                output.write("Credito tributario o devolucion (por dividendos Corporativos):," + df.format(retornoImpuestos[i]) + "$");
                output.newLine();
                output.write("Devolucion por Excedente de impuestos: (Ingresos netos negativos), $" + df.format(refund[i]));
                output.newLine();
                output.write("-------------------------------------------------");
                output.newLine();

                output.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double[] calcBaseImponible(int taxPayers, double[] totalIngresos, double[] totalDeducciones) {
        double[] baseImponible = new double[taxPayers];
        for (int i = 0; i < taxPayers; i++) {
            baseImponible[i] = totalIngresos[i] - totalDeducciones[i];

        }
        return baseImponible;

    }

    public static double[] calcRefund(int taxPayers, double[] baseImponible) {
        double refund[] = new double[taxPayers];
        for (int i = 0; i < taxPayers; i++) {
            refund[i] = 0;
            if (baseImponible[i] < 0) {
                refund[i] = Math.abs(baseImponible[i]);
            }
        }
        return refund;

    }

}
