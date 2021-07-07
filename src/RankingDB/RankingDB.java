
package RankingDB;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class RankingDB {
    private static final String NOMBRE_BASEDATOS = "Ranking.db";
    private static final String SQL_TABLA ="CREATE TABLE IF NOT EXISTS 	jugador \n"+
    "(id_jugador INTEGER PRIMARY KEY AUTOINCREMENT , nickName TEXT, puntaje INTEGER, nivel INTEGER, fecha TEXT)";
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    private PreparedStatement pstmt;
    
    public RankingDB(){
       this.conn = null; 
       this.stmt = null;
       this.rs = null;
       this.pstmt= null;
       this.inicializar();
    }
    
    private void inicializar() {
        try { 
            String url = "jdbc:sqlite:"+NOMBRE_BASEDATOS;
            conn = DriverManager.getConnection(url); // Si no existe crea el archivo de la base de datos
            stmt = conn.createStatement();
            String sql =SQL_TABLA;
            stmt.executeUpdate(sql);
            System.out.println("Conexion exitosa");
            stmt.close();  
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error en inicializar()");
                ex.printStackTrace();
            }
        
        }
    }
    
    private void cerrar(){
        try{
            conn.close();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }  
    }
    
    public void insertarJugadorAlRanking(String nickName, int puntaje,int nivel){
        try{
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yy");
            String sql = "INSERT INTO jugador(nickName,puntaje,nivel,fecha) VALUES(?,?,?,?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nickName);
            pstmt.setInt(2, puntaje);
            pstmt.setInt(3, nivel);
            pstmt.setString(4,formateador.format(new Date()));
            pstmt.executeUpdate();

        }catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error en insertarJugadorAlRanking");
                ex.printStackTrace();
            }
        }

    }
    
    public Vector<String> mostrarRanking(){/*Notas: para operar con esta funcion de forma correcta, la misma deberia trabajar
        retornando un String y en el codigo de Arkanoid se trabaje mediante dicho codigo*/
        //Vector<String> tableRanking;
        try{
                Vector<String> tableRanking=new Vector();
                String sql ="Select * from jugador order by(puntaje) desc";
                rs = stmt.executeQuery(sql);
                while(rs.next()){
                    tableRanking.add(((Integer)(rs.getInt("id_jugador"))).toString());//1
                    //tableRanking.add(rs.getString("nickName"));                       //2
                    tableRanking.add(((Integer)(rs.getInt("nivel"))).toString());     //3
                    tableRanking.add(rs.getString("fecha"));                          //4
                    tableRanking.add(((Integer)(rs.getInt("puntaje"))).toString());   //5
                }
                return tableRanking;
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error en mostrarRanking()");
                ex.printStackTrace();
            }
        }
        return null;
    }
    
    public void borrarRanking(){

        try{
            stmt = conn.createStatement();
            stmt.executeUpdate("drop table if exists jugador");

        }catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

 
}
