/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package cadastroclient;

import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author fel-f
 */

public class ThreadClient extends Thread {

    private ObjectOutputStream out = null;
    private ObjectInputStream in = null;

    private JTextArea textArea;
    private JFrame frame;
    private ArrayList<String> output;
    
    public ThreadClient() {
        
    }

    public ThreadClient(ObjectInputStream in, ObjectOutputStream out) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run() {
        output = new ArrayList<>();
        try {
            Boolean validate = (Boolean) in.readObject();
            Integer idUsuario = (Integer) in.readObject();

            if (validate) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

                String comando;
                String idPessoa;
                String idProduto;
                String quantidade;
                String precoUnitario;

                do {
                    System.out.println("| L - Listar");
                    System.out.println("| E - Entrada");
                    System.out.println("| S - Saida");
                    System.out.println("| F - Finalizar aplicacao");
                    System.out.print("Digite a opcao desejada: ");
                    comando = reader.readLine();

                    switch (comando) {
                        case "E", "e" -> {
                            out.writeObject("E");

                            System.out.println("Nova Entrada");

                            System.out.print("ID Pessoa: ");
                            idPessoa = reader.readLine();
                            out.writeObject(idPessoa);

                            System.out.print("ID Produto: ");
                            idProduto = reader.readLine();
                            out.writeObject(idProduto);

                            System.out.print("ID Usuario: " + idUsuario);
                            out.writeObject(idUsuario);
                            System.out.println("");

                            System.out.print("Quantidade: ");
                            quantidade = reader.readLine();
                            out.writeObject(quantidade);

                            System.out.print("Preco Unitario: ");
                            precoUnitario = reader.readLine();
                            out.writeObject(precoUnitario);
                            
                            output.add("Entrada realizada com sucesso.\n");
                        }
                        
                        case "S", "s" -> {
                            out.writeObject("S");

                            System.out.println("Nova Saida");

                            System.out.print("ID Pessoa: ");
                            idPessoa = reader.readLine();
                            out.writeObject(idPessoa);

                            System.out.print("ID Produto: ");
                            idProduto = reader.readLine();
                            out.writeObject(idProduto);

                            System.out.print("ID Usuario: " + idUsuario);
                            out.writeObject(idUsuario);
                            System.out.println("");

                            System.out.print("Quantidade: ");
                            quantidade = reader.readLine();
                            out.writeObject(quantidade);

                            System.out.print("Preco Unitario: ");
                            precoUnitario = reader.readLine();
                            out.writeObject(precoUnitario);
                            
                            output.add("Saida realizada com sucesso.\n");
                        }

                        case "L", "l" -> {
                            out.writeObject("L");
                            try {
                                ArrayList<String> produtoList = (ArrayList<String>) in.readObject();
                                ArrayList<Integer> produtoQuantidade = (ArrayList<Integer>) in.readObject();
                                if (frame == null || !frame.isVisible()) {
                                    frame = new JFrame("Retorno do Servidor");
                                    frame.setSize(400, 600);
                                    textArea = new JTextArea(20, 50);
                                    textArea.setEditable(false);
                                    frame.add(new JScrollPane(textArea));
                                    frame.pack();
                                    frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                                    frame.setVisible(true);
                                    frame.setVisible(true);
                                    SwingUtilities.invokeLater(() -> {
                                        output.add("Lista de Produtos:\n");
                                        for (int i = 0; i < produtoList.size(); i++) {
                                            output.add(produtoList.get(i) + " " + produtoQuantidade.get(i) + "\n");
                                        }
                                        for (String line : output) {
                                            textArea.append(line);
                                        }
                                        textArea.setCaretPosition(textArea.getDocument().getLength());
                                    });
                                } else {
                                    frame.setVisible(false);
                                }
                                
                            } catch (ClassNotFoundException | IOException e) {
                                e.printStackTrace();
                            }
                        }

                        case "F", "f" -> {
                            out.writeObject("F");
                            System.out.println("Finalizando a aplicacao...");
                        }

                        default -> System.out.println("Por favor, digite uma opcao valida.");
                    }

                } while (!"f".equalsIgnoreCase(comando));

            } else {
                System.out.println("Usuario ou senha incorretos!");
            }

        } catch (HeadlessException | IOException | ClassNotFoundException e) {
            if (!(e instanceof java.io.EOFException)) {
                System.out.println("Finalizando a thread...");
            }
        }
    }
}