
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
            DefaultTableModel modelo = new DefaultTableModel();
            tableName.setModel(modelo);
            modelo.addColumn("ref_paper");
            modelo.addColumn("Titulo");
            modelo.addColumn("Autores");
            modelo.addColumn("Año");
            modelo.addColumn("url");
            modelo.addColumn("Publicación");
            
            ResultSetMetaData rsMD = rs.getMetaData();
            int cantidadColumnas = rsMD.getColumnCount();
            
            while (rs.next()){
                
                String[] filas = new String[cantidadColumnas];
                
                for(int i=0; i<cantidadColumnas; i++){
                    filas[i] = rs.getString(i+1);
                }
                modelo.addRow(filas);
            }
            
            int numFilas = modelo.getRowCount();
            Object refAnt = modelo.getValueAt(0, 0);
            Object refActual;
            
            String autorAnt = modelo.getValueAt(0, 2).toString();
            String autorActual;
            String autores = "";
            ArrayList<String> autoresTot = new ArrayList<>();
            
            ArrayList<Integer> eliminarFilas = new ArrayList<>();
            
            int jj = 0;
            for (int i = 1; i < numFilas; i++) {
                refActual = modelo.getValueAt(i, 0);
                autorActual = modelo.getValueAt(i, 2).toString();
                if (refActual.toString().equals(refAnt.toString())) {
                    eliminarFilas.add(i);
                    autores = autorAnt + " " + autorActual;
                    
                    autorAnt = autores;
                    jj = jj+1;
                }else{
                    if (jj ==0){
                        autores = modelo.getValueAt(i-1, 2).toString();
                    }
                    autoresTot.add(autores);
                    autores = "";
                    
                    autorAnt = modelo.getValueAt(i, 2).toString();
                    jj = 0;
                }
                refAnt = modelo.getValueAt(i, 0);
                
            }
            //Añado la última fila
            autoresTot.add(autores);
            
            int ii = 0;
            for(Integer j:eliminarFilas){
                
                modelo.removeRow(j-ii);
                
                ii = ii+1;
            }
            
            for (int i = 0; i < autoresTot.size(); i++) {
                String newString = autoresTot.get(i).replace(". ", ".").replace(" ", ", ");
                modelo.setValueAt(newString, i, 2);
            }
            
        }catch(SQLException ex){
        }
    
    }
    
}
