/*
 * Este Software tem Objetivo Educacional
 * Para fins de aprendizagem e avaliacao na
 * Na Disciplina de Programacao Orientada a Objetos - Avancada
 *  do Curso de Analise de Sistemas da Fatec - Ipiranga
 * Ano 2016 - Janeiro a Junho 
 * Aluno Decio Antonio de Carvalho  * 
 */

package modelo;

import static controle.Util.reduzString;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class DBConexao {
 
 private Connection conexao;
 private Statement stmt;
 private ResultSet rs;
 //public int idClienteNow = 0;
 
  public static Connection con;
  public static Connection con1;
  public static Connection con2;
  public static Connection con3;
  public static Connection con4;
  private static String url;
  private static String usuario;
  private static String senha;
  public static boolean acesso;
  public static int tentativa;
  
  public DBConexao(String base, String user, String senha){
      
      String msg;
      msg="";
      url ="jdbc:derby://localhost:1527/"+base;
      //usuario ="DAC"; //EM CASA
      usuario = user; //"DAC";
      //senha = "123456";
      //senha "12345";
      
      try{
          msg = "Ok conexao com o banco: "+url +"estabelecida";
          System.out.println("Tentativa de conexao");
          Class.forName("org.apache.derby.jdbc.ClientDriver");
          con = DriverManager.getConnection(url, usuario, senha);
          System.out.println(msg);
          acesso=true;
      }catch (ClassNotFoundException ecl){
      msg = msg+ecl;
      JOptionPane.showMessageDialog(null,msg );
      }catch (SQLException e){
      msg="Falhou a tentativa de conexao: "+e;
      JOptionPane.showMessageDialog(null, msg);
      //System.out.println("Falhou conexao");
            acesso=false;
      }
   
  }
  /**
   * método para inclusao de registros no Banco Tutor1
     * @param nome
     * @param nasc
     * @param ende
     * @param email
     * @param rg
     * @param cpf
     * @throws java.sql.SQLException
   */
  public void incluirDados(String nome, String nasc, String ende, String email, String rg, String cpf) throws SQLException{
     // url ="INSERT INTO ROOT.Cliente VALUES('"+nome+"','"+nasc+"','"+ende+"','"+email+"','"+rg+"','"+cpf+"')";  
      Statement stm = con.createStatement();
      stm.executeUpdate("INSERT INTO ROOT.Clientes VALUES('"+nome+"','"+nasc+"','"+ende+"','"+email+"','"+rg+"','"+cpf+"')");
  }
  
  public static Connection capturaConexao(){
       con = null;
      try{
          System.out.println("Tentativa de conexao");
          Class.forName("org.apache.derby.jdbc.ClientDriver");
          con = DriverManager.getConnection(url, usuario, senha);
          // System.out.println("Ok conexao com o banco: "+url +" estabelecida");
         
      }catch (ClassNotFoundException e){ 
      
      }catch (SQLException e){
      //System.out.println("Falhou conexao");
            acesso=false;
      }
      
      return con;
  }
  
  /**
   * Método booleano que verifica se o acesso a base de dados
   * esta disponível e estabelecida
   * @return true caso consiga conectar ao banco Aerofast
   */
  public boolean verificaConexao(String base, String user, String senha){
      startDerby();
      
      boolean con1=false;
      url ="jdbc:derby://localhost:1527/"+base;
      usuario =user; //DAC usuário
      senha = senha; //12345 senha
      
      try{
          System.out.println("Tentativa de conexao");
          Class.forName("org.apache.derby.jdbc.ClientDriver");
          con = DriverManager.getConnection(url, usuario, senha);
          //System.out.println("Ok conexao com o banco: "+url +" estabelecida");
          con1=true;
      }catch (ClassNotFoundException e){ 
      
      }catch (SQLException e){
      //System.out.println("Falhou conexao");
            con1=false;
      }
      
      return con1;
  }
  /**
   * método para criar tabelas no banco
     * @return Conexao
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
   */
   public static Connection getConnectionx(String base, String user, String senha) throws ClassNotFoundException, SQLException{
        //Connection con3;
        con3=null;
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        con3 = DriverManager.getConnection("jdbc:derby://localhost:1527/"+base, user, senha);
        //con = DriverManager.getConnection("jdbc:derby://localhost:1527/Aerofast", "DAC", "12345");
        return con3;
    }
    
    public static Connection getConnection(String base, String user, String senha){
        String msg;
        //Connection con4;
        con4=null;
        msg="";
        try {
        Class.forName("org.apache.derby.jdbc.ClientDriver"); 
        con4 = DriverManager.getConnection("jdbc:derby://localhost:1527/"+base, user, senha);
        }catch (ClassNotFoundException ecl){
        msg = msg+ecl;
        JOptionPane.showMessageDialog(null,reduzString(msg) );
        } catch (Exception e) {
        msg="Falhou a tentativa de conexao: \n"+e;
        JOptionPane.showMessageDialog(null, reduzString(msg));
        }
        
        return con4;
    }
   
    
    
    //Realizar login no sistema
    
    /*
    select * from DAC.USUARIO
    WHERE LOGIN = 'Teste' AND SENHA = '12345abc';
    */
    /**
     * método para realizar conexão com uma base de dados 
     * @param base
     * @param user
     * @param senha
     * @return false nok or true ok
     */
    public boolean realizarLogin(String base, String user, String senha){
        boolean resposta = false ;
        
        String msg;
        msg="";
        conexao = DBConexao.getConnection(base, user, senha);
        ResultSet rs;
        rs = null;
            
     try {
         stmt =conexao.createStatement(
                 ResultSet.TYPE_SCROLL_INSENSITIVE,
                 ResultSet.CONCUR_READ_ONLY);
     } catch (SQLException ex) {
         msg=msg+ex;
         Logger.getLogger(DBConexao.class.getName()).log(Level.SEVERE, null, ex);
     }
        
     try {
         rs = stmt.executeQuery("SELECT * FROM usuario WHERE LOGIN "    
                 + "= "+user+ "AND SENHA ="+senha);
     } catch (SQLException ex) {
         msg=msg+ex;
         Logger.getLogger(DBConexao.class.getName()).log(Level.SEVERE, null, ex);
     }
            
            
     try {
         if (rs.first()) {
             //entra no sistema
             resposta = true;
         } else{
             resposta = false;
             if (tentativa<3)
             tentativa++;
             else
             System.exit(0);
             // msg = "tentativa "  + tentativa;
         }        
     } catch (SQLException ex) {
         msg=msg+ex;
         
         Logger.getLogger(DBConexao.class.getName()).log(Level.SEVERE, null, ex);
     }
            
     if (msg != ""){
        JOptionPane.showMessageDialog(null, reduzString(msg)); 
     }
     
     return resposta;
    }//final metodo realizarLoginPetfast
    
    
    
  /**
     * 
     * @param user //login do novo usuário Derby
     * @param password  //senha do novo usuário Derby
     * @return true se criar novo usuário
     * @throws ClassNotFoundException 
     */
    public static boolean criarUsuarioDerby(String base, String user, String senha) throws ClassNotFoundException{
        Connection con1;
        con1=null;
        boolean resultado;
        String propriedade;
        resultado = false;
        try{
            con1 = getConnection(base, user, senha);
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Properties properties = new Properties();
            properties.put(user, senha);
            propriedade = properties.getProperty(user, url);
            System.out.println(propriedade);
            resultado = true;
            
        }
        catch (ClassNotFoundException e){
            
        }    
   
       return resultado;
    
    }
    
 public static void startDerby(){
   //  public class start {
    //public static void main(String[] args) {
     String s = "C:\\db\\bin\\startNetworkServer.bat";
     String result = s.substring(s.lastIndexOf(System.getProperty("file.separator"))+1,s.length());
     System.out.println("\n"+result+"\n");
//saida:
//nomeArquivo.ext  
     
     try {
            //Executa comando do windows ou linux no rt.exec("comandos");
            Runtime rt = Runtime.getRuntime();
            //Process proc = rt.exec("cmd /c start derby\\bin\\startNetworkServer.bat");//Envés de "derby\\bin\\startNetworkServer.bat" pode ser C:\\Tao\\Oquefor
            Process proc = rt.exec("cmd /c start c:\\db\\bin\\startNetworkServer.bat");
            //Envés de "derby\\bin\\startNetworkServer.bat" pode ser C:\\Tao\\Oquefor
                                                     
            //No meu caso descompactei na pasta do meu programa e do start.jar e chamei de derby a pasta do server.
            //proc = rt.exec("cmd.exe /c start java -jar SeuPrograma.jar");//Envés de "SeuPrograma.jar" pode ser C:\\Oquefor\\SeuPrograma.jar
           //Process proc = rt.exec("cmd /c start derby\\bin\\stopNetworkServer.bat"); para parar o servidor.
        } catch (Throwable t) {
   // }
}
 }   
    
    
  public void criaTabelas(){
     /*Statement stm = con.createStatement();
     stm.executeUpdate("CREATE TABLE ROOT.Clientes ("
      +""
      +""
      +")"); */
      
  }
    
}
