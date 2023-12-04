//const { ClientRequest } = require('http');

//  Funçao metodos

//  Módulos
const { lstat, write } = require('fs');
const express = require('express');
const path = require('path');
const app = express();
const bodyParser = require('body-parser');
//const { send } = require('process');

app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());
app.use(express.static(path.join(__dirname, "public")));

const net = require('net');
const { stdin, stdout, stderr } = require('process');
const readline = require('readline');

const rl = readline.createInterface({
    input: stdin,
    output: stdout
    })

    //rl.addListener('line', line => {
    //    client.write(line.toString(), 'utf-8');
    //})

app.get('/Home', (req, res) => {
    res.sendFile(__dirname + "/html/Home.html");
})
app.get('/Login', (req, res) => {
    res.sendFile(__dirname + "/html/Login.html");
})
app.get('/SignUp', (req, res) => {
    res.sendFile(__dirname + "/html/SignUp.html");
})
app.get('/Queries', (req, res) => {
    res.sendFile(__dirname + "/html/Queries.html");
})
app.get('/Search', (req, res) => {
    res.sendFile(__dirname + "/html/search.html");
})
app.get('/Payments', (req, res) => {
    res.sendFile(__dirname + "/html/Payments.html");
});
app.get('/Perfil/:def?', (req, res) => {
    res.sendFile(__dirname + "/html/perfil.html");
})
app.get('/Contracts', (req, res) => {
    res.sendFile(__dirname + "/html/Contracts.html");
})
app.get('/Chat', (req, res) => {
    res.sendFile(__dirname + "/html/Chat.html");
})
app.get('/error/:def?/:mensagem_erro?', (req, res) => {
    res.render('login_erro', {erro:req.params.mensagem_erro, def: req.params.def});
})

app.post('/form_login', (req, res) => {
    const client = new net.Socket();
    client.connect(3000, '127.0.0.1', () => {
    console.log(`inp!`);
        client.write(`out_account,nome,${req.body.email_log}`, 'utf-8');
        client.end();
        client.on('data', data => {
            console.log(data.toString());
            if(data.toString() != null){
                console.log(data.toString());
                if(data.toString() != null){
                    res.redirect(`/Perfil/${data.toString()}`);
                }
                else{
                    alert("Login não encontrao!");
                    res.redirect(`/Login`);
                }
            }
        })
    })
})
app.post('/form_cadastro', (req, res) => {
    const client = new net.Socket();
    client.connect(3000, '127.0.0.1', () => {
    console.log(`cad!`);
    client.write(`in_account,${req.body.nome_cad},${req.body.email_cad}`, 'utf-8');
    client.end(); 
    res.redirect(`/Perfil/${req.body.email_cad}`);
    })
})
app.set('view engine', 'ejs');

//  BodyParser
//app.engine('handlebars', handlebars.engine({defaultLayout: 'main'}));

//http://localhost:8080/
app.listen(8080);