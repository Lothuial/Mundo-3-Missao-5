/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package cadastroserver;

import controller.UsuarioJpaController;
import model.Usuario;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fel-f
 */

public class CadastroThread extends Thread {

    private final UsuarioJpaController ctrlUsu;

    private final Socket s1;

    public CadastroThread(UsuarioJpaController ctrlUsu, Socket s1) {
        this.ctrlUsu = ctrlUsu;
        this.s1 = s1;
    }

    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(s1.getOutputStream()); ObjectInputStream in = new ObjectInputStream(s1.getInputStream())) {

            String login = (String) in.readObject();
            String senha = (String) in.readObject();

            Date dataAtual = new Date();
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String dataFormatada = formato.format(dataAtual);
            System.out.println("Nova comunicacao em -> " + dataFormatada);

            boolean usuarioValido = (validar(login, senha) != null);

            if (usuarioValido) {
                out.writeObject(usuarioValido);
                out.writeObject(validar(login, senha).getIdUsuario());

                System.out.println("Login bem sucedido!");

                ComandosHandler comandos = new ComandosHandler(out, in);
                comandos.executarComandos();

            } else {
                out.writeObject(usuarioValido);
               
                out.writeObject(null);

            }
            out.flush();

        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(CadastroThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Usuario validar(String login, String senha) {
        return ctrlUsu.validarUsuario(login, senha);
    }

}