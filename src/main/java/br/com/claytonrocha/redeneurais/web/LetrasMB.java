/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.claytonrocha.redeneurais.web;

import java.io.Serializable;
import java.util.Random;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author ClaytonRocha
 */
@ManagedBean(name = "letraMB")
@ViewScoped
public class LetrasMB implements Serializable {

    private String letra;
    private final Random r = new Random();
    private double limite = 0;
    private int epocas = 30;
    private int contador;
    private int[] entradas = new int[16];
    private double[] pesos = new double[16];
    private int[][] matrizAprendizado = new int[][]{
        {1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1}, //C
        {1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1}, //C
        {1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0}, //C
        {1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0}, //C
        {1, 1, 1, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0}, //C
        //B  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15
        {1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1}, //E 
        {1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 1, 1}, //E 
        {1, 1, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 1, 0}, //E 
        {1, 1, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 1, 1}, //E 
    };

    private int[] esperado = new int[]{
        1, 1, 1, 1,1, //C

        0, 0, 0, 0 //E
    };

    public LetrasMB() {
//        Inicia todos so pesos com valores aleatórios
        for (int i = 0; i < pesos.length; i++) {
            pesos[i] = r.nextDouble();
        }
        entradas[0] = 1;
        limite = (r.nextDouble() * 2 - 1) * 0.05;
    }

    public int[] getEntradas() {
        return entradas;
    }

    public void setEntradas(int[] entradas) {
        this.entradas = entradas;
    }

    public double[] getPesos() {
        return pesos;
    }

    public void setPesos(double[] pesos) {
        this.pesos = pesos;
    }

    public int[][] getMatrizAprendizado() {
        return matrizAprendizado;
    }

    public void setMatrizAprendizado(int[][] matrizAprendizado) {
        this.matrizAprendizado = matrizAprendizado;
    }

    public double getLimite() {
        return limite;
    }

    public void setLimite(double limite) {
        this.limite = limite;
    }

    public int getEpocas() {
        return epocas;
    }

    public void setEpocas(int epocas) {
        this.epocas = epocas;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public int getContador() {
        return contador;
    }

    public void setContador(int contador) {
        this.contador = contador;
    }

    public void treinar() {
        boolean treinou = true;
        int saida;
        for (int i = 0; i < matrizAprendizado.length; i++) {
            saida = calcularSaida(matrizAprendizado[i]);
            if (saida != esperado[i]) {
                corrigirPeso(i, saida);
                treinou = false;
            }
        }
        this.contador++;
        if ((this.contador < this.epocas)) {
            treinar();
        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Treinamento efetuado com sucesso!!!"));
    }

    private int calcularSaida(int[] entradas) {
        double sum = 0.0;
        for (int i = 0; i < entradas.length; i++) {
            sum += pesos[i] * entradas[i];
        }
        if (sum >= limite) {
            return 1;
        } else {
            return 0;
        }
    }

    public void corrigirPeso(int i, int saida) {
        for (int j = 0; j < pesos.length; j++) {
            pesos[j] = pesos[j] + (1 * (esperado[i] - saida) * matrizAprendizado[i][j]);
        }
    }

    public void calcular() {
        int resultado = calcularSaida(entradas);
        letra = resultado == 1 ? "C" : "E";
    }
}
