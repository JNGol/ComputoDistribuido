/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calc;

/**
 *
 * @author juani
 */
import java.io.*;
import java.net.*;

public class Servidor {

    public Servidor() {
    }

    public static void main(String[] args) {
        int PUERTO_CALCU = 5001;
        String error = "no hay operando";
        Socket socket = null;
        ServerSocket serverSocket = null;
        String[] lista;
        String resultado = null;
        try {
            serverSocket = new ServerSocket(PUERTO_CALCU);
        } catch (Exception e) {
            System.err.println("Error al crear socket");
            return;
        }
        while (true) {
            try {
                System.out.println("SERVIDOR CALCULADORA");

                socket = serverSocket.accept();
                System.out.println("Esperando operaciones a realizar...");

                DataOutputStream salida = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                DataInputStream entrada = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

                String valor = entrada.readUTF();
                if (valor.indexOf("+") != -1) {
                    lista=valor.split("\\+");
                    Operaciones operar = new Operaciones(lista [0], lista[1]);
                    System.out.print("La suma de "+valor+" es... ");
                    resultado = String.valueOf(operar.suma());
                }
                else if(valor.indexOf("-")!=-1){
                    lista=valor.split("\\-");
                    Operaciones operar = new Operaciones(lista [0], lista[1]);
                    System.out.print("La resta de "+valor+" es... ");
                    resultado = String.valueOf(operar.resta());
                }else if(valor.indexOf("*")!=-1){
                    lista=valor.split("\\*");
                    Operaciones operar = new Operaciones(lista [0], lista[1]);
                    System.out.print("La multiplicación de "+valor+" es... ");
                    resultado = String.valueOf(operar.multiplicacion());
                }else if(valor.indexOf("/")!=-1){
                    lista=valor.split("\\/");
                    Operaciones operar = new Operaciones(lista [0], lista[1]);
                    System.out.print("La división de "+valor+" es... ");
                    resultado = String.valueOf(operar.division());
                }else{
                    System.out.println(error);
                }
                System.out.print(resultado);
                salida.writeUTF(resultado);
                System.out.print("\nEnviando respuesta...");
                salida.flush();
                System.out.println("OK\n");
                try{
                    socket.close();
                }catch(IOException ex){}
            }catch (Exception e){
                System.err.println("Cerrando la conexión del socket...");
                if(socket!=null){
                    try{
                        socket.close();
                    }catch(IOException ex){}
                }
            }
        }
    }

}
