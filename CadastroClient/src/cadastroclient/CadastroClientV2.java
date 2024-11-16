/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package cadastroclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fel-f
 */

public class CadastroClientV2 {
    
    private final String ADDRESS = "localhost";
    private final int PORT = 4321;
    
    public CadastroClientV2() {
        
    }
    
    private void run() {
        try {
            Socket socket = new Socket(ADDRESS, PORT);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Preencha as informações de login");
            System.out.print("Usuário: ");
            String login = reader.readLine();
            System.out.print("Senha: ");
            String senha = reader.readLine();
            out.writeObject(login);
            out.writeObject(senha);
            out.flush();
            ThreadClient threadClient = new ThreadClient(in,out);
            threadClient.start();
        } catch (IOException ex) {
            Logger.getLogger(CadastroClientV2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new CadastroClientV2().run();
    }

}
