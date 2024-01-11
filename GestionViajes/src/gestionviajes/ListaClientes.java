/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionviajes;

import AccessServices.ServiciosGestionTI;
import Models.Clientes;
import comMdf.devazt.networking.HttpClient;
import comMdf.devazt.networking.OnHttpRequestComplete;
import comMdf.devazt.networking.Response;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author zapju
 */
public class ListaClientes extends javax.swing.JInternalFrame {
    
    ServiciosGestionTI services = ServiciosGestionTI.getInstancia();
    static String cedulaCliente;
    
    
    /**
     * Creates new form ListaClientes
     */
    public ListaClientes() {
        initComponents();
        cargarCliente();
        CamposVisiblesIniciales();
    }
    private void CamposVisiblesIniciales() {
        jtxtIDD.setEnabled(false);
        jtxtCedula.setEnabled(true);
        jtxtApellido.setEnabled(true);
        jtxtNombre.setEnabled(true);
        jtxtTelefono.setEnabled(true);
        jtxtEmail.setEnabled(true);
        jtxtContraseña.setEnabled(true); 
        
    }
    private void limpiarCampos() {
        jtxtCedula.setText("");
        jtxtApellido.setText("");
        jtxtNombre.setText("");
        jtxtTelefono.setText("");
        jtxtEmail.setText("");
        jtxtContraseña.setText(""); 
    }
    private void CamposVisibles() {
        jtxtIDD.setEnabled(false);
        jtxtCedula.setEnabled(false);
        jtxtApellido.setEnabled(true);
        jtxtNombre.setEnabled(true);
        jtxtTelefono.setEnabled(true);
        jtxtEmail.setEnabled(true);
        jtxtContraseña.setEnabled(false); 
        
    }
    public void cargarDatos(){
        jtblCliente.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(jtblCliente.getSelectedRow() != -1){
                    int fila = jtblCliente.getSelectedRow();
        String id = jtblCliente.getValueAt(fila, 0).toString();
        jtxtIDD.setText(id);
        String cedula = jtblCliente.getValueAt(fila, 1).toString();
        jtxtCedula.setText(cedula);
        String nombre = jtblCliente.getValueAt(fila, 2).toString();
        jtxtNombre.setText(nombre);
        String apellido = jtblCliente.getValueAt(fila, 3).toString();
        jtxtApellido.setText(apellido);
        String telefono = jtblCliente.getValueAt(fila, 4).toString();
        jtxtTelefono.setText(telefono);
        String email = jtblCliente.getValueAt(fila, 5).toString();
        jtxtEmail.setText(email);
        CamposVisibles();
                }
            }
        });
        
    }
    private void cargarCliente() {
        HttpClient cliente = new HttpClient(new OnHttpRequestComplete() {
            @Override
            public void onComplete(Response status) {
                if (status.isSuccess()) {
                    try {
                        JSONArray arrayJsonReporte = new JSONArray(status.getResult());
                        JSONObject objReporte;
                        DefaultTableModel modelo = new DefaultTableModel();
                        modelo.addColumn("ID");
                        modelo.addColumn("Cedula");
                        modelo.addColumn("Nombre");
                        modelo.addColumn("Apellido");
                        modelo.addColumn("Telefono");
                        modelo.addColumn("Email");
                        int count = 0;
                        while (count < arrayJsonReporte.length()) {
                            objReporte = arrayJsonReporte.getJSONObject(count);
                            String[] reporte = {objReporte.getString("id_cli"), 
                                objReporte.getString("ced_cli"), 
                                objReporte.getString("nom_cli"),
                                objReporte.getString("ape_cli"), 
                                objReporte.getString("tel_cli"),
                                objReporte.getString("email_cli")};
                            modelo.addRow(reporte);
                            count++;
                        }
                        jtblCliente.setModel(modelo);                        
                    } catch (JSONException e) {
                    }
                }
            }  
        });
        cliente.excecute(services.obtenerClient());

    }
    private void registrarCliente(Clientes c) {
        try {
            HttpClient cliente = new HttpClient(new OnHttpRequestComplete() {
            @Override
            public void onComplete(Response status) {
                if (status.isSuccess()) {
                    JOptionPane.showMessageDialog(null, "La actividad se a registrado correctamente");
                    cargarCliente();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al conectarse con el servidor");
                }
            }
        });
        cliente.excecute("http://localhost:8012/sv_viajes_taxis/ingresarCliente.php?ced_cli="
                    + c.getCedula() + "&nom_cli=" + c.getNombre() + "&ape_cli=" + c.getApellido() + "&tel_cli=" + c.getTelefono() + "&email_cli=" + c.getEmail()
                    + "&passw_cli=" + c.getPassw()+"");
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
    private boolean validarCamposNoVacios() {
        return (!jtxtCedula.getText().equals("") && !jtxtNombre.getText().equals("")
                && !jtxtApellido.getText().equals("") && !jtxtTelefono.getText().equals("")
                && !jtxtEmail.getText().equals("") && !jtxtContraseña.getText().equals("")) ? true : false;
    }
    private boolean validarCamposNoVacios2() {
        return (!jtxtCedula.getText().equals("") && !jtxtNombre.getText().equals("")
                && !jtxtApellido.getText().equals("") && !jtxtTelefono.getText().equals("")
                && !jtxtEmail.getText().equals("")) ? true : false;
    }
    
    
    private void editar(Clientes c) {
        try {
            HttpClient cliente = new HttpClient(new OnHttpRequestComplete() {
            @Override
            public void onComplete(Response status) {
                if (status.isSuccess()) {
                    JOptionPane.showMessageDialog(null, "La actividad se a Actualizo correctamente");
                    cargarCliente();
                    CamposVisiblesIniciales();
                    limpiarCampos();
                    
                } else {
                    JOptionPane.showMessageDialog(null, "Error al conectarse con el servidor");
                }
            }
        });
        cliente.excecute("http://localhost:8012/sv_viajes_taxis/actualizarCliente.php?id_cli="
                +c.getId()+"&nom_cli="+c.getNombre()+"&ape_cli="
                        + c.getApellido()+ "&tel_cli="+c.getTelefono()+"&email_cli="+c.getEmail()+"");
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
    
    public void eliminar(Clientes c){
        try {
            HttpClient cliente = new HttpClient(new OnHttpRequestComplete() {
            @Override
            public void onComplete(Response status) {
                if (status.isSuccess()) {
                    JOptionPane.showMessageDialog(null, "Cliente eliminado correctamente");
                    cargarCliente();
                    CamposVisiblesIniciales();
                    limpiarCampos();
                    
                } else {
                    JOptionPane.showMessageDialog(null, "Error al conectarse con el servidor");
                }
            }
        });
            cliente.excecute("http://localhost:8012/sv_viajes_taxis/eliminarCliente.php?id_cli="
                +c.getId()+"");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void guardarCliente(){
        if(validarCamposNoVacios()){
            Clientes cl = new Clientes();
            cl.setCedula(jtxtCedula.getText());
            cl.setNombre(jtxtNombre.getText());
            cl.setApellido(jtxtApellido.getText());
            cl.setTelefono(jtxtTelefono.getText());
            cl.setEmail(jtxtEmail.getText());
            cl.setPassw(jtxtContraseña.getText());
            registrarCliente(cl);
        }else{
            JOptionPane.showMessageDialog(null, "No se permiten campos vacios");
        }
        
    }
    
    public void editarCliente(){
        if(validarCamposNoVacios2()){
        Clientes cl = new Clientes();
        //cl.setCedula(ced_cli = jtxtCedula.getName());
        cl.setId(jtxtIDD.getText());
        cl.setNombre( jtxtNombre.getText());
        cl.setApellido(jtxtApellido.getText());
        cl.setTelefono(jtxtTelefono.getText());
        cl.setEmail(jtxtEmail.getText());
        editar(cl);
         }else{
            JOptionPane.showMessageDialog(null, "No se permiten campos vacios");
        }
    }
    
    public void eliminarCliente(){
        
        Clientes cl = new Clientes();
        cl.setId(jtxtIDD.getText());
        
        eliminar(cl);
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblCliente = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jbtnRegistro = new javax.swing.JButton();
        jbtnEditar = new javax.swing.JButton();
        jbtnEliminar = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jtxtCedula = new javax.swing.JTextField();
        jtxtNombre = new javax.swing.JTextField();
        jtxtApellido = new javax.swing.JTextField();
        jtxtTelefono = new javax.swing.JTextField();
        jtxtEmail = new javax.swing.JTextField();
        jtxtContraseña = new javax.swing.JTextField();
        jtxtID1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jtxtIDD = new javax.swing.JTextField();

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("CLIENTES");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jtblCliente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtblCliente.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jtblClientePropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(jtblCliente);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 678, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jbtnRegistro.setText("Registro");
        jbtnRegistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnRegistroActionPerformed(evt);
            }
        });

        jbtnEditar.setText("Editar");
        jbtnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEditarActionPerformed(evt);
            }
        });

        jbtnEliminar.setText("Eliminar");
        jbtnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jbtnRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jbtnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jbtnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jbtnRegistro)
                .addGap(39, 39, 39)
                .addComponent(jbtnEditar)
                .addGap(49, 49, 49)
                .addComponent(jbtnEliminar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel8.setText("Cedula:");

        jLabel2.setText("Nombre:");

        jLabel3.setText("Apellido:");

        jLabel5.setText("Telefono:");

        jLabel6.setText("E-Mail:");

        jLabel7.setText("Contraseña:");

        jtxtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtxtNombreActionPerformed(evt);
            }
        });

        jLabel9.setText("ID:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9))
                .addGap(52, 52, 52)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jtxtCedula)
                    .addComponent(jtxtNombre)
                    .addComponent(jtxtApellido)
                    .addComponent(jtxtTelefono)
                    .addComponent(jtxtEmail)
                    .addComponent(jtxtContraseña, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                    .addComponent(jtxtIDD))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 166, Short.MAX_VALUE)
                .addComponent(jtxtID1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jtxtIDD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jtxtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jtxtID1))
                .addGap(24, 24, 24)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jtxtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jtxtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jtxtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jtxtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jtxtContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1))
                .addGap(0, 5, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(274, 274, 274))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(jLabel4))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEliminarActionPerformed
        // TODO add your handling code here:
        eliminarCliente();
    }//GEN-LAST:event_jbtnEliminarActionPerformed

    private void jbtnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEditarActionPerformed
        // TODO add your handling code here:
        
        editarCliente();
        
    }//GEN-LAST:event_jbtnEditarActionPerformed

    private void jbtnRegistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnRegistroActionPerformed
        // TODO add your handling code here:
        guardarCliente();
        limpiarCampos();
        
    }//GEN-LAST:event_jbtnRegistroActionPerformed

    private void jtblClientePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jtblClientePropertyChange
        // TODO add your handling code here:
        cargarDatos();
    }//GEN-LAST:event_jtblClientePropertyChange

    private void jtxtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jtxtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jtxtNombreActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDialog jDialog1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton jbtnEditar;
    private javax.swing.JButton jbtnEliminar;
    private javax.swing.JButton jbtnRegistro;
    private javax.swing.JTable jtblCliente;
    private javax.swing.JTextField jtxtApellido;
    private javax.swing.JTextField jtxtCedula;
    private javax.swing.JTextField jtxtContraseña;
    private javax.swing.JTextField jtxtEmail;
    private javax.swing.JLabel jtxtID1;
    private javax.swing.JTextField jtxtIDD;
    private javax.swing.JTextField jtxtNombre;
    private javax.swing.JTextField jtxtTelefono;
    // End of variables declaration//GEN-END:variables
}
