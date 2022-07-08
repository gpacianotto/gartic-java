/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.exercicio.aula5;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author guilh
 */
public class PaintPanel extends JPanel implements Runnable {
    
    private ArrayList<Point> points = new ArrayList<Point>();
    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private JTextField inputUser;
    private final String server = "127.0.0.1";
    public PaintPanel()
    {
        inputUser = new JTextField("insira sua resposta e aperte Enter");
        
        //inputUser.setLocation(20, 20);
        inputUser.setBounds( 100, 100, 150, 20 );
        inputUser.setSize(200,30);
        inputUser.setPreferredSize(new Dimension(250, 20));
        inputUser.setVisible(true);
        this.setFocusable(true);
        this.add(inputUser, BorderLayout.NORTH);
        
        inputUser.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    sendData(inputUser.getText());  
                }
                catch(Exception ex)
                {
                    System.out.println("Erro: "+ ex.getMessage());
                }
            }
        
        });
        
        
//        addKeyListener(new KeyListener(){
//            @Override
//            public void keyTyped(KeyEvent e) {
//                
//            }
//
//            @Override
//            public void keyPressed(KeyEvent e) {
//                System.out.println("Key pressed code=" + e.getKeyCode() + ", char=" + e.getKeyChar());
//                if(e.getKeyCode() == 10)
//                {
//                    
//                    
//                }
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//                
//            }
//        });
        //127.0.0.1
        
//        addMouseMotionListener(
//        new MouseMotionAdapter()
//        {
//            @Override
//            public void mouseDragged(MouseEvent event)
//            {
//                points.add(event.getPoint());
//                repaint();
//            }
//        }
//        );
    }
    
    public void runClient()
    {
        try{
            connectToServer();
            getStreams();
            processConnection();
        }
        catch(EOFException ex)
        {
            System.out.println("Erro! Servidor encerrou conexão");
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
        finally{
            closeConnection();
        }
    }
    
    private void connectToServer() throws IOException
    {
        System.out.println("tentando conectar");
        client = new Socket(InetAddress.getByName(server), 8080);
        System.out.println("conectado: "+client.getInetAddress().getCanonicalHostName());
    }
    
    private void getStreams() throws IOException
    {
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush();
        
        input = new ObjectInputStream(client.getInputStream());
        System.out.println("obtêm IO streams");
    }
    private void processConnection() throws IOException
    {
        do{
            try{
               points.add((Point) input.readObject()); 
               repaint();
            }
            catch(ClassNotFoundException exc)
            {
                repaint();
            }
        }while(true);
    }
    
    private void sendData(String message)
    {
        try {
            output.writeObject(message);
            output.flush();
            
        }
        catch(Exception ex)
        {
            System.out.println("erro ao enviar resposta");
        }
    }
    private void closeConnection()
    {
        System.out.println("Encerrando Conexão");
        try
        {
            output.close();
            input.close();
            client.close();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        for(Point point: points)
        {
            g.fillOval(point.x, point.y, 4, 4);
        }
    }

    @Override
    public void run() {
        
        runClient();
        
    }
    
    
}
