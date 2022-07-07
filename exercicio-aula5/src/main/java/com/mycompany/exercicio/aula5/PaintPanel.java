/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.exercicio.aula5;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author guilh
 */
public class PaintPanel extends JPanel implements Runnable {
    
    private ArrayList<Point> points = new ArrayList<Point>();
    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private final String server = "127.0.0.1";
    public PaintPanel()
    {
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
