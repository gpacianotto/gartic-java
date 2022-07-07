/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.exercicio.aula5;

/**
 *
 * @author guilh
 */
public class BrushPicker {
    
    private static BrushPicker instance;
    
    private boolean lapis;
    private boolean reta;
    private boolean quadrado;
    
    private BrushPicker()
    {
        setLapis();
    }

    public void setLapis() {
        this.lapis = true;
        this.quadrado = false;
        this.reta = false;
    }

    public void setReta() {
        this.lapis = false;
        this.quadrado = false;
        this.reta = true;
        
    }
    public void setQuadrado() {
        this.lapis = false;
        this.quadrado = true;
        this.reta = false;
    }
    
    public static BrushPicker getInstance()
    {
        if(instance == null)
        {
            instance = new BrushPicker();
        }
        
        return instance;
    }
}
