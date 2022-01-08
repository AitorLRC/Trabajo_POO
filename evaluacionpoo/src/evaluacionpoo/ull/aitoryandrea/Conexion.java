
package evaluacionpoo.ull.aitoryandrea;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author aitor
 */
class Conexion implements AutoCloseable{
    
    // Inicializo las variables para la conexión y el nombre de la base de datos
    private static Conexion elSingleton = null;
    private static Connection con = null;
    private static final String DATABASE_NAME = "MUMMEC2122";
    
    // Statement para generar la vista en la que hacer las búsquedas
    private final String tablaCompleta = "CREATE VIEW tabla_completa AS\n" +
"	SELECT ref_paper, tb4.title, name, publiyear, url, university, webpage, country, publication.title as publisher FROM\n" +
"	(SELECT ref_paper, ref_publication, title, name, publiyear, url, University, webpage, country FROM\n" +
"	(SELECT ref_paper, name, university, webpage, country FROM\n" +
"	(SELECT * FROM \n" +
"	(SELECT * FROM authors NATURAL JOIN papersauthors) as tb1\n" +
"       NATURAL JOIN authorafiliation) as tb2\n" +
"       NATURAL JOIN afiliations) as tb3\n" +
"       NATURAL JOIN papersinfo) as tb4\n" +
"       INNER JOIN publication ON publication.ref_publication = tb4.ref_publication;";
    
            
    private Conexion() throws SQLException{
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/?serverTimezone=UTC","root","1234");
        elSingleton = this; 
    }
    
    static Conexion getInstance() throws SQLException{
        if (elSingleton != null) return elSingleton;
        else return elSingleton = new Conexion();
    }
    
    void instanciaEsquema() throws SQLException{
        //Defino la base de datos que voy a usar
        con.createStatement().executeUpdate("USE "+DATABASE_NAME+";");
        con.setAutoCommit(false);
        
        // Comprobamos si la vista existe, si no existe la creamos
        try{
            con.createStatement().executeQuery("SELECT * FROM tabla_completa");
        }catch(SQLException e){
            con.createStatement().executeUpdate(tablaCompleta);
        }
        
        con.commit();
    }
    
    // Inicializo lo statement y el resultset de la búsqueda
    private static Statement sAuthor, sYear, sTitleUrl, sPublication, sUniversityCountry, sAll;
    private ResultSet rs;
    
    //Para el caso en el que es un autor
    public ResultSet author(String autor) throws SQLException{
        String autStatement = "SELECT ref_paper, title, name, publiyear, url, publisher  FROM \n" +
                              "	(SELECT ref_paper FROM\n" +
                              "	(SELECT * FROM authors WHERE authors.name LIKE '%"+autor+"%') as tb1\n" +
                              "    NATURAL JOIN papersauthors) as tb2\n" +
                              "    NATURAL JOIN tabla_completa;";
        sAuthor = con.createStatement();
        rs = sAuthor.executeQuery(autStatement);
        return rs;
    }
    
    //Para el caso en el que es año 
    public ResultSet year(String strYear) throws SQLException{
        Integer year;
        // Compruebo que el año introducido es un entero
        try{
            year = Integer.parseInt(strYear);
        }catch(NumberFormatException e){
            year = null;
        }
        String yearStatement = "SELECT ref_paper, title, name, publiyear, url, publisher  FROM \n" +
                              "	  (SELECT ref_paper FROM papersinfo WHERE papersinfo.publiyear = "+year+") as tb1\n" +
                              "    NATURAL JOIN tabla_completa;";
        sYear = con.createStatement();
        rs = sYear.executeQuery(yearStatement);
        return rs;
    }
    
    //Para el caso en el que es título o url
    @SuppressWarnings("empty-statement")
    public ResultSet titleUrl(String tipos, String intro) throws SQLException{
        String tipo = null;
        // Compruebo si se va a buscar por un titulo o por una url
        if ("Titulo".equals(tipos)){tipo = "title";}
        if ("url".equals(tipos)){tipo = "url";}
        
        String titStatement = "SELECT * FROM \n" +
                              "	(SELECT ref_paper FROM papersinfo WHERE papersinfo."+tipo+" LIKE '%"+intro+"%') as tb1\n" +
                              "    NATURAL JOIN tabla_completa;";
        sTitleUrl = con.createStatement();
        rs = sTitleUrl.executeQuery(titStatement);
        return rs;
    }
    
    //Para el caso en el que es publicación
    public ResultSet publication(String publi) throws SQLException{
        String publiStatement = "SELECT ref_paper, title, name, publiyear, url, publisher  FROM \n" +
                                "	(SELECT ref_paper FROM\n" +
                                "	(SELECT ref_publication FROM publication WHERE publication.title = '"+publi+"') as tb1\n" +
                                "    NATURAL JOIN papersinfo) as tb2\n" +
                                "    NATURAL JOIN tabla_completa";
        sPublication = con.createStatement();
        rs = sPublication.executeQuery(publiStatement);
        return rs;
    }
    
    //Para el caso en el que es universidad o país
    public ResultSet universityCountry(String tipos, String intro) throws SQLException{
        String tipo = null;
        // Compruebo si se va a buscar un pais o una universidad
        if ("Pais".equals(tipos)){tipo = "country";}
        if ("Universidad".equals(tipos)){tipo = "university";}
        
        String uniStatement = "SELECT ref_paper, title, name, publiyear, url, publisher FROM \n" +
                              "	(SELECT ref_paper FROM(\n" +
                              "	SELECT ref_author FROM\n" +
                              "	(SELECT ref_afiliation FROM afiliations WHERE afiliations."+tipo+" = '"+intro+"') as tb1\n" +
                              "    NATURAL JOIN authorafiliation) as tb2\n" +
                              "    NATURAL JOIN papersauthors) as tb3\n" +
                              "    NATURAL JOIN tabla_completa;";
        
        sUniversityCountry = con.createStatement();
        rs = sUniversityCountry.executeQuery(uniStatement);
        return rs;
    }
    
    
    //Todos los campos
    public ResultSet all(String intro) throws SQLException{
        Integer year;
        // Compruebo que el año introducido es un entero
        try{
            year = Integer.parseInt(intro);
        }catch(NumberFormatException e){
            year = null;
        }
        String allStatement = "SELECT ref_paper, title, name, publiyear, url, publisher  FROM \n" +
                              "	(SELECT ref_paper FROM tabla_completa WHERE (name LIKE '%"+intro+"%' \n" +
                              "                                                 OR title LIKE '%"+intro+"%' \n" +
                              "                                                 OR publisher = '"+intro+"'  \n" +
                              "                                                 OR publiyear = "+year+" \n" +
                              "							OR country = '"+intro+"'  \n" +
                              "							OR url = '"+intro+"' )) as tb1\n" +
                              "	NATURAL JOIN tabla_completa;";
        sAll = con.createStatement();
        rs = sAll.executeQuery(allStatement);
        return rs;
        
    }

    @Override
    public void close() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
