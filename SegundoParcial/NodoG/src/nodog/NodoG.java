/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package nodog;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier
 */
public class NodoG extends javax.swing.JFrame {

    /**
     * Creates new form NodoG
     */
    public NodoG() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Soy un Nodo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(40, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(35, 35, 35))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String args[]) throws IOException {
        new Thread() {
            @Override
            public void run() {
                HashMap<Integer, DataOutputStream> douts = new HashMap<>();
                HashMap<Integer, String> tiposDeConeccion = new HashMap<>();
                HashMap<String, Integer> uuids = new HashMap<>();
                HashMap<String, Integer> ids = new HashMap<>();
                int puerto = 0;
                ServerSocket ss = null;
                int counter = 0;

                System.out.printf("Parametros recibidos: \n");
                for (int i = 0; i < args.length; i++) {
                    //Para cada piertp checo si puedo crear el listener
                    //Si se crea el listener continua
                    //Si no se crea el listener asumimos que otro nodo existe en ese puerto previamente
                    // Creamos un thread y nos conectamos a dicho nodo.
                    System.out.printf("args[%d] = %s \n", i, args[i]);
                    try {
                        puerto = Integer.parseInt(args[i]);
                        ss = new ServerSocket(puerto);

                    } catch (BindException e) {
                        System.out.printf("No escuchando \n");
                        try {
                            Socket s = new Socket("localhost", puerto);
                            int c = counter;
                            int p = puerto;
                            System.out.println(" >> Nodo No: " + c + " por iniciar en puerto " + p);

                            hilo(s, c, douts, uuids, ids, tiposDeConeccion, true);

                        } catch (BindException e2) {
                        } catch (IOException ex) {
                            Logger.getLogger(NodoG.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        counter++;
                        continue;
                    } catch (IOException ex) {
                        Logger.getLogger(NodoG.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.printf("Escuchando \n");
                    break;
                }
                System.out.printf("\n\n\n ");

                try {

                    // ServerSocket ss = new ServerSocket(puerto);
                    //int counter = 0;
                    //HashMap<Integer, DataOutputStream> douts = new HashMap<>();
                    while (true) {
                        Socket s = ss.accept();
                        int c = counter;
                        System.out.println(" >> " + "Client No: " + c + " por iniciar!");

                        hilo(s, c, douts, uuids, ids, tiposDeConeccion, false);

                        counter++;
                    }

                } catch (IOException e) {
                    System.out.println(e);
                }
            }

            private static void hilo(Socket s, int c, HashMap<Integer, DataOutputStream> douts, HashMap<String, Integer> uuids, HashMap<String, Integer> id, HashMap<Integer, String> tiposDeConeccion, boolean amIClient) {
                // Hilo Genera un Thread que Guarda el output stream de s en el hashtable socks con el numero c
                // Y luego escucha lo que llegue en el input stream de c y lo refleja a todos outputs streams guardados en douts
                // Hilo Termina
                new Thread() {
                    Socket soc;
                    int count;
                    //int p;

                    @Override
                    public void run() {
                        soc = s;
                        count = c;

                        try {

                            DataInputStream din = new DataInputStream(soc.getInputStream());
                            DataOutputStream dout = new DataOutputStream(soc.getOutputStream());
                            douts.put(count, dout);
                            String tipoDeConneccion = "";
                            try {

                                if (amIClient) {
                                    //Me identifico ante el serve si es que soy cliente
                                    dout.writeUTF("Nodo");
                                    tiposDeConeccion.put(count, "Nodo");
                                } else {
                                    tipoDeConneccion = din.readUTF();
                                    System.out.println(" >> Un " + tipoDeConneccion);
                                    tiposDeConeccion.put(count, tipoDeConneccion);
                                }

                                while (true) {
                                    String str, uuid, id;
                                    str = din.readUTF();
                                    ParseaOperacion mensaje = new ParseaOperacion(str);
                                    uuid = mensaje.uuid;
                                    id = mensaje.id;
                                    if (uuids.containsKey(uuid)) {
                                        int uuidCount = uuids.get(uuid);
                                        uuids.put(uuid, uuidCount + 1);
                                        System.out.printf("Ya lei %s  %d  veces \n", uuid, uuidCount + 1);
                                        //break;
                                    } else {
                                        uuids.put(uuid, 0);
                                        for (Map.Entry<Integer, DataOutputStream> set : douts.entrySet()) {
                                            System.out.printf("PROCESO %d ENVIO %s a: %d  \n", count, str, set.getKey());
                                            set.getValue().writeUTF(str);
                                            set.getValue().flush();
                                        }

                                    }
                                    // System.out.printf("client %d says: %s  \n", counter, str);
                                    /*for (Map.Entry<Integer, DataOutputStream> set : douts.entrySet()) {
                                            System.out.printf("PROCESO %d ENVIO %s a: %d  \n", count ,str, set.getKey());

                                            set.getValue().writeUTF(str);
                                            set.getValue().flush();
                                        }*/

                                }
                            } catch (IOException ex) {
                                Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
                            }
                            din.close();
                            dout.close();
                            System.out.println(" >> " + "Coneccion No: " + count + " fuera de línea! Era " + tipoDeConneccion);
                            douts.remove(c);
                            tiposDeConeccion.remove(c);
                            s.close();
                        } catch (IOException ex) {
                            Logger.getLogger(this.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            System.out.printf("Client -%d- exit!! ", count);
                        }
                    }
                }.start();
            }

        }.start();
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NodoG.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NodoG.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NodoG.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NodoG.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        new Thread() {
            @Override
            public void run() {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new NodoG().setVisible(true);
                    }
                });
            }
        }.start();

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}

class ParseaOperacion {

    public char operacion;
    public int operador1, operador2;
    public String uuid, id;

    ParseaOperacion(String st) {
        String[] divisiones = st.split(" ");
        id = divisiones[0];
        uuid = divisiones[1];
        operacion = divisiones[3].charAt(0);
        operador1 = Integer.parseInt(divisiones[2]);
        operador2 = Integer.parseInt(divisiones[4]);
    }
}