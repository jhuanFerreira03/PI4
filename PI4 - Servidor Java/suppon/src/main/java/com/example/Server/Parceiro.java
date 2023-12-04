package com.example.Server;

import java.util.*;
import java.net.*;
import java.nio.Buffer;
import java.io.*;
import java.util.concurrent.Semaphore;

public class Parceiro {
    private Socket conexao;
    private BufferedReader receptor;
    private PrintWriter transmissor; 

    private String proxComunicado = null;
    private Semaphore mutex = new Semaphore(1, true);

    public Parceiro(Socket conexao, BufferedReader receptor, PrintWriter transmissor) throws Exception{
        if (conexao == null){
            throw new Exception("Conexao ausente!");
        }
        if(receptor == null){
            throw new Exception("Receptor ausente!");
        }
        if(transmissor == null){
            throw new Exception("Transmissor ausente!");
        }

        this.conexao = conexao;
        this.receptor = receptor;
        this.transmissor = transmissor;
    }

    public void receber(String com) throws Exception{
        try{
            this.transmissor.write(com);
            this.transmissor.flush();
        }catch(Exception e){
            throw new Exception("Erro de transmissão!");
        }
    }

    public String espiar() throws Exception{
        try{
            this.mutex.acquireUninterruptibly();
            if (this.proxComunicado == null){
                this.proxComunicado = (String) this.receptor.readLine();
            }
            this.mutex.release();
            return this.proxComunicado;
        }catch(Exception e){
            throw new Exception("Erro de recepção!");
        }
    }

    public String enviar() throws Exception{
        try{
            if(this.proxComunicado == null){
                this.proxComunicado = (String) this.receptor.readLine();
            }
            String ret = this.proxComunicado;
            this.proxComunicado = null;
            return ret;
        }catch(Exception e){
            throw new Exception("Erro de recepção!");
        }
    }
    /*public Comunicado enviar() throws Exception{
        try{
            if(this.proxComunicado == null){
                this.proxComunicado = (Comunicado) this.receptor.readObject();
            }
            Comunicado ret = this.proxComunicado;
            this.proxComunicado = null;
            return ret;
        }catch(Exception e){
            throw new Exception("Erro de recepção!");
        }
    }*/

    public void fechar() throws Exception{
        try{
            transmissor.write("o servidor foi fechado!");
            transmissor.flush();
            this.transmissor.close();
            this.conexao.close();
            this.receptor.close();
        }catch(Exception e){
            throw new Exception("Erro ao desconectar!");
        }
    }
}
