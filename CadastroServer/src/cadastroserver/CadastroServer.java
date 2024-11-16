/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

package cadastroserver;

import controller.MovimentoJpaController;
import controller.PessoaJpaController;
import controller.ProdutoJpaController;
import controller.UsuarioJpaController;
import model.Produto;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 *
 * @author fel-f
 */

public class CadastroServer {

    /**
     * @param args the command line arguments
     */
    
    private final int PORT = 4321;
    
    public CadastroServer() {
        
    }
    
    private void run() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor conectado em:" + PORT);
            // Inicializa controladores
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("CadastroServerPU");
            ProdutoJpaController produtoController = new ProdutoJpaController(emf);
            MovimentoJpaController movimentoController = new MovimentoJpaController(emf);
            PessoaJpaController pessoaController = new PessoaJpaController(emf);
            UsuarioJpaController usuarioController = new UsuarioJpaController(emf);
            while (true) {
                System.out.println("Aguardando conexao de cliente...");
                Socket socket = serverSocket.accept();
                System.out.println("Usuario conectado.");
                ClientHandler clientHandler = new ClientHandler(socket, produtoController,
                    movimentoController, pessoaController, usuarioController);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            Logger.getLogger(CadastroServer.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void main(String[] args) {
        new CadastroServer().run();
    }

    private class ClientHandler implements Runnable {
        private final Socket socket;
        private final ProdutoJpaController produtoController;
        private final UsuarioJpaController usuarioController;

        public ClientHandler(Socket socket, ProdutoJpaController produtoController,
            MovimentoJpaController movimentoController, PessoaJpaController pessoaController,
            UsuarioJpaController usuarioController) {
            this.socket = socket;
            this.produtoController = produtoController;
            this.usuarioController = usuarioController;
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                // Autenticacao
                String username = in.readLine();
                String password = in.readLine();
                if (validateCredentials(username, password)) {
                    out.println("Login efetuado com sucesso. Exibindo produtos do banco de dados:");
                    sendProductList(out);
                } else {
                    try (socket) {
                        out.println("Ocorreu um erro durante o login.");
                    }
                }
            } catch (IOException e) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, e);
            } catch (Exception e) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        private boolean validateCredentials(String username, String password) {
            return usuarioController.validarUsuario(username, password) != null;
        }

        private void sendProductList(PrintWriter out) {
            List<Produto> productList = produtoController.findProdutoEntities();
            for (Produto product : productList) {
                out.println(product.getNome());
            }
            out.println();
        }

    }
    
}
