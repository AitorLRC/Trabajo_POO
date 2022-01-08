
package evaluacionpoo.ull.aitoryandrea;

import java.awt.HeadlessException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author aitor
 */
public class NewJFrame extends javax.swing.JFrame {

    /**
     * Creates new form NewJFrame
     */
    public NewJFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jComboBox = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        textPanel = new javax.swing.JTextPane();
        Buscar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Buscador");
        setPreferredSize(new java.awt.Dimension(1000, 427));

        jPanel1.setLayout(new java.awt.BorderLayout());

        jComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "--Seleccionar--", "Titulo", "Autor", "Publicación", "Año", "Universidad", "País", "Link", "Todos los campos" }));
        jComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxActionPerformed(evt);
            }
        });
        jPanel1.add(jComboBox, java.awt.BorderLayout.LINE_START);

        jScrollPane2.setViewportView(textPanel);

        jPanel1.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        Buscar.setText("Buscar");
        Buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuscarActionPerformed(evt);
            }
        });
        jPanel1.add(Buscar, java.awt.BorderLayout.LINE_END);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ref_paper", "Title", "Autores", "Año", "url", "publicacion"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        jPanel2.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBoxActionPerformed

    private void BuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuscarActionPerformed

        try{
            // Conexión con la base de datos
            Conexion conn = Conexion.getInstance();
            conn.instanciaEsquema();
            
            //Leo la opción seleccionada por el usuario
            String opcion = String.valueOf(jComboBox.getSelectedItem());
            //Leo el texto escrito por el usuario
            String texto = textPanel.getText();
            // Inicializo la variable para el resultado de la búsqueda
            ResultSet s = null;
            
            //Primer switch para comprobar que el usuario no ha seleccionado la opción --Seleccionar--
            switch(opcion){
                case "--Seleccionar--":
                    //En tal caso salta un mensaje de error
                    JOptionPane.showMessageDialog(this, "Seleccione alguno de los campos");
                    break;
                default:
                    //En caso contrario usamos el método correspondiente a la opción elegida
                    switch(opcion){
                        case "Autor":
                            s = conn.author(texto);
                            break;

                        case "Año":
                            s = conn.year(texto);
                            break;

                        case "Titulo":
                            s = conn.titleUrl("Titulo", texto);
                            break;

                        case "Link":
                            s = conn.titleUrl("url", texto);
                            break;

                        case "Publicación":
                            s = conn.publication(texto);
                            break;

                        case "Universidad":
                            s = conn.universityCountry("Universidad", texto);
                            break;

                        case "País":
                            s = conn.universityCountry("Pais", texto);
                            break;

                        case "Todos los campos":
                            s = conn.all(texto);

                            break;
                    }
                    //Con la busqueda realizada procedo a imprimirla en la tabla
                    //El try sirve para comprobar que el resultado no está vacío
                    try{
                        PrintTable print = new PrintTable(jTable1, s);
                    }catch(ArrayIndexOutOfBoundsException ex){
                        //Si estuviera vacío muestro un mensaje de error en pantalla
                        JOptionPane.showMessageDialog(this, "No se ha encontrado ningún artículo con esas características");
                    }
                }

        }catch (SQLException | HeadlessException ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_BuscarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Buscar;
    private javax.swing.JComboBox<String> jComboBox;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextPane textPanel;
    // End of variables declaration//GEN-END:variables
}
