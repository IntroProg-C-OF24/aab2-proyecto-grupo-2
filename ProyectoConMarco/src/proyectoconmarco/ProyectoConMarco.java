package proyectoconmarco;

import java.util.Scanner;

public class ProyectoConMarco {

    /**
     * @param args the command line arguments
     */
    
    static Scanner sc = new Scanner(System.in);
      
    public static void main(String[] args) {
          System.out.println("Ingresa tu nombre");
        String nombreUsuario = sc.nextLine();
        int numeroParametros = 6;
        String datosUsuario[][] = new String[numeroParametros][2];

        pedirDatosUsuario(datosUsuario);

        System.out.println("------FACTURA GENERADA: -------");
        System.out.println(generarFacturas(nombreUsuario, datosUsuario));
        System.out.println("-------------");

    }

    public static String generarFacturas(String nombreUsuario, String[][] datosUsuario) {
        String factura = ""; //Aquí se acumulará la factura de acuerdo a los datos proporcionado por el usuario
        
        return factura;
    }

    
    //Se hará de manera aleatoria / o de manera manual. Pendiente. 
    private static void pedirDatosUsuario(String[][] datosUsuario) {
        System.out.println("Ingrese su sueldo mensual: ");
        datosUsuario[0][0] = "Sueldo mensual: ";
        datosUsuario[0][1] = sc.nextLine();
        
        System.out.println("Ingresa el parámetro N. 2: ");
        System.out.println("Ingresa el parámetro N. 3: ");
        System.out.println("Ingresa el parámetro N. 4: ");
        System.out.println("Ingresa el parámetro N. 5: ");
        System.out.println("Ingresa el parámetro N. 6: ");
    }

}
