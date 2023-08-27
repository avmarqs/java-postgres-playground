package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AppBd {
    private static final String PASSWORD = ""; //estatico pq é uma constante
    private static final String USERNAME = "gitpod";
    private static final String JDBC_URL = "jdbc:postgresql://localhost/postgres";
    private Connection conn;
    public static void main(String[] args) { //nao pode chamar métodos nao estaticos dentro do main. Então colocamos para o construtor e no main chama o construtor
        new AppBd();
    }

    public AppBd(){ //construtor precisa ter o mesmo nome da classe
        try(var conn = getConnection()){
            //carregarDriverJDBC();
            listarEstados(conn);
            localizarEstado(conn, "PR");
        } catch (SQLException e) {
            //if(statement == null)
                System.err.println("Não foi possível conectar ao banco de dados:" + e.getMessage());
            //else System.err.println("Não foi possível executar a consulta ao banco:" + e.getMessage());
        }
    }

    private void localizarEstado(Connection conn, String uf) {
        try {
            //var statement = conn.createStatement();
            //var sql = "select * from estado where uf = '" + uf + "'"; //precisa de aspas simples a string do nome do estado. suscetível a sql injection
            var sql = "select * from estado where uf = ?";
            var statement = conn.prepareStatement(sql); //cria uma declaração/consult preparada
            System.out.println(sql);
            statement.setString(1, uf);
            var result = statement.executeQuery(sql); //pq retorna um resultset
            if(result.next()){
                System.out.printf("Id: %d Nome %s UF: %s\n", result.getInt("id"), result.getString("nome"), result.getString("uf"));
            }

        } catch (SQLException e){
            System.err.println("Erro ao executar consulta SQL: " + e.getMessage());
        }
    }

    private void listarEstados(Connection conn) {
        //Statement statement =  null;// declarada aqui pra diferenciar erro do banco vs erro sql. depois ela some pq a conn ja vem pronta
        try{
            System.out.println("Conexão com o banco realizada com sucesso.");

            var statement = conn.createStatement();
            var result = statement.executeQuery("select * from estado");
            while(result.next()){
                System.out.printf("Id: %d Nome %s UF: %s\n", result.getInt("id"), result.getString("nome"), result.getString("uf"));
            }
        } catch (SQLException e) {
            System.err.println("Não foi possível conectar ao banco de dados:" + e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException{
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    private void carregarDriverJDBC() {
        try {
            Class.forName("org.postgresql.Driver");        
        } catch (ClassNotFoundException e) {
            System.err.println("Não foi possível carregar a biblioteca para acesso ao banco de dados: " + e.getMessage());

        }
    }
}

