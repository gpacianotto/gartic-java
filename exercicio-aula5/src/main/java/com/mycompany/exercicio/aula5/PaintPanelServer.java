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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author guilh
 */
public class PaintPanelServer extends JPanel implements Runnable{

    private ArrayList<Point> points = new ArrayList<Point>();
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    private final String resposta = "relógio";
    private String palpite;
    
    
    public PaintPanelServer()
    {
        addMouseMotionListener(
        new MouseMotionAdapter()
        {
            @Override
            public void mouseDragged(MouseEvent event)
            {
                points.add(event.getPoint());
                if(output != null && connection != null)
                {
                    sendData(event.getPoint());
                }
                
                repaint();
            }
        }
        );
    }
    
    public void runServer() throws IOException
    {
        try
        {
            server = new ServerSocket(8080, 100);
            
            while(true)
            {
                try{
                    waitForConnection();
                    getStreams();
                    processConnection();
                }
                catch(EOFException ex)
                {
                    System.out.println("Conexão encerrada");
                }
            }
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    private void waitForConnection() throws IOException
    {
        System.out.println("Aguardando Conexões");
        connection = server.accept();
        System.out.println("Conexão recebida de: "+connection.getInetAddress().getCanonicalHostName());
        
    }
    
    private void getStreams() throws IOException
    {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        
        input = new ObjectInputStream(connection.getInputStream());
        
        System.out.println("IOStreams obtidos!");
    }
    
    private void setPalpite(String palpite)
    {
        this.palpite = palpite;
    }
    
    private void processConnection() throws IOException
    {
        String message = "";
        System.out.println("conexão bem sucedida");
        //sendData(points.get(points.size() - 1));
        
        do{
            try{
                message = (String)input.readObject();
                setPalpite(message);
                System.out.println("mensagem: "+message);
                if(this.palpite == null ? this.resposta == null : this.palpite.equals(this.resposta))
                {
                    if (JOptionPane.showConfirmDialog(null, "Você Venceu!!", "WARNING",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    } else {
                        System.exit(0);
                    }
                }
                
            }
            catch(ClassNotFoundException ex)
            {
                System.out.println("objeto desconhecido");
            }
        }while(true);
    }
    
    private void closeConnection()
    {
        System.out.println("Encerrando conexão");
        
        try{
            output.close();
            input.close();
            connection.close();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
    
    private void sendData(Point plottedPoint)
    {
        
        try{
//            connection = server.accept();
//            output = new ObjectOutputStream(connection.getOutputStream());
//            output.flush();
            output.writeObject(plottedPoint);
            output.flush();
        }
        catch(IOException ex)
        {
            System.out.println("Erro ao enviar mesnagem");
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
        try{
            runServer();
        }
        catch(Exception e)
        {
            System.out.println("erro: "+e.getMessage());
        }
        
    }
    
    
    
}
