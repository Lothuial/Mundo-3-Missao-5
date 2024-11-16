/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package cadastroclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author fel-f
 */

public class CadastroClient {

    /**
     * @param args the command line arguments
     */
    
    private final String ADDRESS = "localhost"; 
    private final int PORT = 4321;
    
    public CadastroClient() {
        
    }
    
    private void run() {
        try (Socket socket = new Socket(ADDRESS, PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Aplicação conectada ao servidor Digite as informações de login:");
            System.out.print("Usuario: ");
            String username = consoleIn.readLine();
            System.out.print("Senha: ");
            String password = consoleIn.readLine();
            out.println(username);
            out.println(password);
            String response = in.readLine();
            System.out.println(response);
            if (response.equals("Login efetuado com sucesso. Exibindo produtos do banco de dados:")) {
            receiveAndDisplayProductList(in);
            } else {
                System.out.println("Ocorreu um erro durante o login.");
            }
        } catch (IOException e) {
            Logger.getLogger(CadastroClient.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void receiveAndDisplayProductList(BufferedReader in) throws IOException {
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            System.out.println(line);
        }
    }

    public static void main(String[] args) {
        new CadastroClient().run();
    }
    
}
