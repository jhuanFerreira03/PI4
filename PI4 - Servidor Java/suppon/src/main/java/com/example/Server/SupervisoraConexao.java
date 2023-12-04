package com.example.Server;

import java.util.*;
import java.net.*;
import java.io.*;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.json.JsonObject;
import com.mongodb.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Object;
import com.fasterxml.jackson.core.TreeCodec;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class SupervisoraConexao extends Thread {
    private Parceiro usuario;
    private Socket conexao;
    private ArrayList<Parceiro> usuarios;

    public SupervisoraConexao(Socket conexao, ArrayList<Parceiro> usuarios) throws Exception{
        if(conexao == null) {
            throw new Exception("Conexao ausente!");
        }
        if(usuarios == null) {
            throw new Exception("Usuarios ausentes!");
        }
        this.conexao = conexao;
        this.usuarios = usuarios;
    }

    public void run () {

        Logger logger = LoggerFactory.getLogger("App");
        logger.error("Logging an Error");

        PrintWriter transmissor;
        
        try{
            transmissor = new PrintWriter(this.conexao.getOutputStream());
        }catch(Exception e){
            return;
        }
        BufferedReader receptor;
        try{
            receptor = new BufferedReader(new InputStreamReader(this.conexao.getInputStream()));
            this.usuario = new Parceiro(this.conexao, receptor, transmissor);
        }catch(Exception e) {
            try{
                transmissor.close();
            }catch(Exception falha){}
        }
        try{
            synchronized(this.usuarios){
               this.usuarios.add(this.usuario);
            }

            ObjectMapper jso = new ObjectMapper();

            String doc = usuario.enviar();
            Document resultado = null;
            try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            // Acessar o banco de dados e a coleção desejada
            MongoDatabase database = mongoClient.getDatabase("suppon");

            // Chamar a função de busca

            String [] val = doc.split(",");
            MongoCollection<Document> collection = database.getCollection("contas");
            if (val[0].toLowerCase().equals("in_account")) {
                if (buscarDocumento(collection, val[1], val[2]) != null) {
                    SupervisoraConexao.inserirDocumento(collection, val);
                }
            }
            else if(val[0].toLowerCase().equals("out_account")){
                resultado = buscarDocumento(collection, val[1], val[2]);
                System.out.println(resultado.toJson());
                usuario.receber(resultado.toJson());
             }
        }
            // Exibir o resultado
            for(;;) {
            }
        }
        catch(Exception e){}
    }
    private static void inserirDocumento(MongoCollection<Document> collection, String[] list) {
        Document novoDocumento = new Document("nome", list[1]).append("email", list[2]);
        System.out.println("ta inserido! " + novoDocumento.toJson());
        collection.insertOne(novoDocumento);
    }

    private static Document buscarDocumento(MongoCollection<Document> collection, String campo, String valor) {
        return collection.find(Filters.eq(campo, valor)).first();
    }
}