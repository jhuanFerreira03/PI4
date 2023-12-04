package com.example.Server;

import com.example.Server.*;
import java.util.*;

import org.openide.util.io.NbObjectInputStream;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.*;

public class AceitaConexao extends Thread{

    private ServerSocket pedido;
    private ArrayList<Parceiro> usuarios;
    private int count = 0;

    public AceitaConexao(String porta, ArrayList<Parceiro> usuarios) throws Exception{
        if(porta == null){
            throw new Exception("Porta ausente!");
        }

        try{
            this.pedido = new ServerSocket(Integer.parseInt(porta));
        }catch(Exception e){
            throw new Exception("Porta invalida!");
        }
        if(usuarios == null){
            throw new Exception("Ususarios ausentes!");
        }
        this.usuarios = usuarios;
    }

    public void run(){
        for(;;){
            Socket conexao = null;
            try{
                conexao = this.pedido.accept();
            }catch(Exception e){
                continue;
            }

            SupervisoraConexao supervisoraConexao = null;

            try{
                supervisoraConexao = new SupervisoraConexao(conexao, usuarios);
                supervisoraConexao.start();
            }catch(Exception e){}
        }
    }
}
