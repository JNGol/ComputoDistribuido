/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package calc;

/**
 *
 * @author juani
 */
import java.io.*;
import java.net.*;

public class Calc {

    public Calc() {
    }

    private static BufferedReader stdin1 = new BufferedReader(new InputStreamReader(System.in));
    private static BufferedReader stdin2 = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String args[])
            throws IOException {
        String IP_CALCU = "localhost";
        int PUERTO_CALCU = 5001;
        String sigue = "s";
        while (sigue.equals("s")) {
            try {
                System.out.println("CALCULADORA BÁSICA");
                System.out.print("Operación a realizar: ");
                System.out.print("(a+b, a-b, a*b, a/b) \n--> ");
                String valor = stdin2.readLine();
                String resultado = realizar_operacion(IP_CALCU, PUERTO_CALCU, valor);
                System.out.println("El resultado es: " + resultado);
            } catch (Exception e) {
                System.err.print(e);
            }
            System.out.print("\nDesea realizar otra operación? (s/n): ");
            sigue = stdin1.readLine();
            System.out.print("\n");
        }
        System.out.println("Adios.");
    }
    
    static String realizar_operacion(String host, int puerto, String valor){
        String respuesta=null;
        try{
            Socket socketEn = new Socket(host,puerto);
            
            DataOutputStream salida = new DataOutputStream(new BufferedOutputStream(socketEn.getOutputStream()));
            DataInputStream entrada = new DataInputStream(new BufferedInputStream(socketEn.getInputStream()));
            salida.writeUTF(valor);
            salida.flush();
            respuesta = entrada.readUTF();
            try{
                socketEn.close();
            }catch(Exception ex){}
        }catch(Exception e){
            System.err.println(e);
        }
        return respuesta;
    }

}
