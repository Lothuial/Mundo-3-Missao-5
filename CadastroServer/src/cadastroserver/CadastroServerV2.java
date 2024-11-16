/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package cadastroserver;

import controller.UsuarioJpaController;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 *
 * @author fel-f
 */
public class CadastroServerV2 {

    private final int PORT = 4321;
    
    public CadastroServerV2() {
        
    }
    
    private void run() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("CadastroServerPU");
        UsuarioJpaController ctrlUsu = new UsuarioJpaController(emf);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor conectado em: " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                CadastroThread teste = new CadastroThread(ctrlUsu, socket);
                teste.start();
                System.out.println("A thread foi iniciada com sucesso!");
            }
        } catch (IOException ex) {
            Logger.getLogger(CadastroServerV2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new CadastroServerV2().run();
    }
    
}
