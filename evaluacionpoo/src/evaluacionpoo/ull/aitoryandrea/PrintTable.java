
package evaluacionpoo.ull.aitoryandrea;

import java.awt.List;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author aitor
 */
public class PrintTable {
    
    /**
     *
     * @param tableName
     * @param rs
     * @throws ArrayIndexOutOfBoundsException
     */
    public PrintTable(JTable tableName, ResultSet rs) throws ArrayIndexOutOfBoundsException{
        try{
            //Defino el modelo a usar en la tabla
            DefaultTableModel modelo = new DefaultTableModel();
            tableName.setModel(modelo); //Adjudico el modelo a la tabla
            //Añado las columnas correspondientes a la tabla
            modelo.addColumn("ref_paper");
            modelo.addColumn("Titulo");
            modelo.addColumn("Autores");
            modelo.addColumn("Año");
            modelo.addColumn("url");
            modelo.addColumn("Publicación");
            
            ResultSetMetaData rsMD = rs.getMetaData();
            int cantidadColumnas = rsMD.getColumnCount();
            
            // Leo lo obtenido en el ResultSet y lo añado a la tabla
            while (rs.next()){
                String[] filas = new String[cantidadColumnas];
                
                for(int i=0; i<cantidadColumnas; i++){
                    filas[i] = rs.getString(i+1);
                }
                modelo.addRow(filas);
            }
            
            /*
            Paso a eliminar las filas con artículos repetidos y a unir los autores
            de los articulos para que solo se mustre una fila por artículo
            */
            int numFilas = modelo.getRowCount(); //número de filas
            Object refAnt = modelo.getValueAt(0, 0); //leo la primera referencia
            Object refActual; //defino una variable para leer la referencia actual en el bucle
            
            String autorAnt = modelo.getValueAt(0, 2).toString(); //leo el primer autor
            String autorActual; //defino una variable para leer el autor actual
            String autores = ""; //defino una variable para guardar los autores de un artículo
            ArrayList<String> autoresTot = new ArrayList<>(); //defino un array para guardar los autores
            
            ArrayList<Integer> eliminarFilas = new ArrayList<>(); //defino un array para guardar las filas a eliminar en el modelo
            
            int jj = 0; //Variable para tener en cuenta el cambio de artículo
            for (int i = 1; i < numFilas; i++) {
                refActual = modelo.getValueAt(i, 0); //leo la referencia actual
                autorActual = modelo.getValueAt(i, 2).toString(); //leo el autor actual
                
                if (refActual.toString().equals(refAnt.toString())) { //Si la referencia actual es igual a la referencia anterior
                    eliminarFilas.add(i); //añado el índice de esta fila al array eliminarFilas
                    autores = autorAnt + " " + autorActual; //añado este autor a los autores del artículo
                    
                    autorAnt = autores; //redefino los autores anteriores para la siguiente vuelta del bucle
                    jj = jj+1; //sumo uno a la variable que cuenta el cambio de artículo
                
                }else{//si la referencia actual es distinta a la anterior
                    if (jj == 0){ //en el caso de que solo haya un autor para un artículo
                        autores = modelo.getValueAt(i-1, 2).toString(); //leo el autor del artículo que será el de la fila anterior
                    }
                    autoresTot.add(autores); //añado los autores al array de autores totales
                    autores = ""; //vuelvo a poner a cero el número de autores
                    
                    autorAnt = modelo.getValueAt(i, 2).toString(); //para la siguiente vuelta del bucle defino autoresAnt como el autor actual
                    jj = 0; //vuelvo a poner a cero la variable contadora del número de autores por artículo
                }
                refAnt = modelo.getValueAt(i, 0);
                
            }
            //Añado la última fila que no se tiene en cuenta en el bucle
            autoresTot.add(autores);
            
            //Bucle para eliminar las filas que sobran
            int ii = 0; //variable para tener en cuenta las filas eliminadas
            for(Integer j:eliminarFilas){//recorro el array eliminarFilas
                modelo.removeRow(j-ii); //elimino las filas correspondientes 
                ii = ii+1;//sumo uno a la variable para tener en cuenta las filas eliminadas
            }
            //Bucle para cambiar el nombre del autor y poner el de todos los autores
            for (int i = 0; i < autoresTot.size(); i++){
                //Para empezar uno nombre y apellido y añado una coma entre los nombres
                String newString = autoresTot.get(i).replace(". ", ".").replace(" ", ", ");
                modelo.setValueAt(newString, i, 2); //Cambio autor por autores
            }
            
        }catch(SQLException ex){
        }
    
    }
    
}
