/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package buscaminas;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 *
 * @author DARKCEUS
 */
public class Buscaminas extends JFrame {
    String direccionServer;
    Scanner in;
    PrintWriter out;
    private TableroJuego juego;
    private JLabel tiempo;
    private JLabel texto;
    private JButton Comenzar;
    
    public Buscaminas(){
        //this.direccionServer=direccionServer;
        setTitle("Buscaminas");
        texto = new JLabel("");
        add(texto, BorderLayout.NORTH);
        tiempo = new JLabel("");
        add(tiempo, BorderLayout.SOUTH);
        juego = new TableroJuego(texto, tiempo,Comenzar);
        juego.setEnabled(false);
        add(juego, BorderLayout.CENTER);
        Comenzar = new JButton("Comenzar juego");
        add(Comenzar,BorderLayout.AFTER_LINE_ENDS);
        //frame.setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
     private  String getNombre(){
        return JOptionPane.showInputDialog(
        this,
        "Nombre",
        "ingresa nombre",
        JOptionPane.PLAIN_MESSAGE
    );}
       private void run() throws IOException{
        try {
            Socket socket = new Socket(direccionServer, 59001);
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);
            while (in.hasNextLine()) {
                String line = in.nextLine();
                if (line.startsWith("NOMBREDEENVIO")) {
                    out.println(getNombre());
                } 
            }    
        }finally {
            setVisible(false);
            dispose();
        }
    }
                 
    public void Nimbus(){
        try{
          UIManager.setLookAndFeel(new NimbusLookAndFeel());  
        }catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Buscaminas.class.getName()).log(Level.SEVERE, null, ex);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    public  void Abrir() throws IOException{
        //run();
        this.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        Buscaminas buscaminas = new Buscaminas();
        buscaminas.Abrir();
        //buscaminas.run();
       
    }
}
