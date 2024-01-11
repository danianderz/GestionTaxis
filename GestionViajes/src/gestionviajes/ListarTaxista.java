/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionviajes;

import AccessServices.ServiciosGestionTI;
import Models.Taxistas;
import comMdf.devazt.networking.HttpClient;
import comMdf.devazt.networking.OnHttpRequestComplete;
import comMdf.devazt.networking.Response;
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
public class ListarTaxista extends javax.swing.JInternalFrame {
ServiciosGestionTI services = ServiciosGestionTI.getInstancia();
    static String cedulaCliente;
    /**
     * Creates new form ListarTaxista
     */
    public ListarTaxista() {
        initComponents();
        cargarTaxistas();
        CamposVisiblesIniciales();
    }
    private void CamposVisiblesIniciales() {
        jtxtID.setEnabled(false);
        jtxtCedula.setEnabled(true);
        jtxtApellido.setEnabled(true);
        jtxtNombre.setEnabled(true);
        jtxtCoopTaxi.setEnabled(true);
        jtxtEmail.setEnabled(true);
        jtxtContraseña.setEnabled(true); 
        
    }
    private void limpiarCampos() {
        jtxtCedula.setText("");
        jtxtApellido.setText("");
        jtxtNombre.setText("");
        jtxtCoopTaxi.setText("");
        jtxtEmail.setText("");
        jtxtContraseña.setText(""); 
    }
    private void CamposVisibles() {
        jtxtID.setEnabled(false);
        jtxtCedula.setEnabled(false);
        jtxtApellido.setEnabled(true);
        jtxtNombre.setEnabled(true);
        jtxtCoopTaxi.setEnabled(true);
        jtxtEmail.setEnabled(true);
        jtxtContraseña.setEnabled(false); 
        
    }
    public void cargarDatos(){
        jtblTaxis.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(jtblTaxis.getSelectedRow() != -1){
                    int fila = jtblTaxis.getSelectedRow();
        String id = jtblTaxis.getValueAt(fila, 0).toString();
        jtxtID.setText(id);
        String cedula = jtblTaxis.getValueAt(fila, 1).toString();
        jtxtCedula.setText(cedula);
        String nombre = jtblTaxis.getValueAt(fila, 2).toString();
        jtxtNombre.setText(nombre);
        String apellido = jtblTaxis.getValueAt(fila, 3).toString();
        jtxtApellido.setText(apellido);
        String telefono = jtblTaxis.getValueAt(fila, 4).toString();
        jtxtCoopTaxi.setText(telefono);
        String email = jtblTaxis.getValueAt(fila, 5).toString();
        jtxtEmail.setText(email);
        CamposVisibles();
                }
            }
        });
        
    }
    private void cargarTaxistas() {
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
                        modelo.addColumn("Coop. Taxi");
                        modelo.addColumn("Email");
                        int count = 0;
                        while (count < arrayJsonReporte.length()) {
                            objReporte = arrayJsonReporte.getJSONObject(count);
                            String[] reporte = {objReporte.getString("id_tax"), 
                                objReporte.getString("ced_tax"), 
                                objReporte.getString("nom_tax"),
                                objReporte.getString("ape_tax"), 
                                objReporte.getString("coop_tax"),
                                objReporte.getString("email_tax")};
                            modelo.addRow(reporte);
                            count++;
                        }
                        jtblTaxis.setModel(modelo);                        
                    } catch (JSONException e) {
                    }
                }
            }  
        });
        cliente.excecute(services.obtenerTacista());

    }
    private void registrarCliente(Taxistas c) {
        try {
            HttpClient cliente = new HttpClient(new OnHttpRequestComplete() {
            @Override
            public void onComplete(Response status) {
                if (status.isSuccess()) {
                    JOptionPane.showMessageDialog(null, "El Taxista se a registrado correctamente");
                    cargarTaxistas();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al conectarse con el servidor");
                }
            }
        });
        cliente.excecute("http://localhost:8012/sv_viajes_taxis/ingresarTaxista.php?ced_tax="
                    + c.getCedula() + "&nom_tax=" + c.getNombre() + "&ape_tax=" + c.getApellido() + "&coop_tax=" + c.getCoopTaxi()+ "&email_tax=" + c.getEmail()
                    + "&passw_tax=" + c.getPassw()+"");
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
    private void editar(Taxistas c) {
        try {
            HttpClient cliente = new HttpClient(new OnHttpRequestComplete() {
            @Override
            public void onComplete(Response status) {
                if (status.isSuccess()) {
                    JOptionPane.showMessageDialog(null, "El taxista se a Actualizo correctamente");
                    cargarTaxistas();
                    CamposVisiblesIniciales();
                    limpiarCampos();
                    
                } else {
                    JOptionPane.showMessageDialog(null, "Error al conectarse con el servidor");
                }
            }
        });
        cliente.excecute("http://localhost:8012/sv_viajes_taxis/actualizarTaxista.php?id_tax="
                +c.getIdTaxi()+"&nom_tax="+c.getNombre()+"&ape_tax="
                        + c.getApellido()+ "&coop_tax="+c.getCoopTaxi()+"&email_tax="+c.getEmail()+"");
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
    
    public void eliminar(Taxistas c){
        try {
            HttpClient cliente = new HttpClient(new OnHttpRequestComplete() {
            @Override
            public void onComplete(Response status) {
                if (status.isSuccess()) {
                    JOptionPane.showMessageDialog(null, "Taxista eliminado correctamente");
                    cargarTaxistas();
                    CamposVisiblesIniciales();
                    limpiarCampos();
                    
                } else {
                    JOptionPane.showMessageDialog(null, "Error al conectarse con el servidor");
                }
            }
        });
            cliente.excecute("http://localhost:8012/sv_viajes_taxis/eliminarTaxista.php?id_tax="
                +c.getIdTaxi()+"");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    private boolean validarCamposNoVacios() {
        return (!jtxtCedula.getText().equals("") && !jtxtNombre.getText().equals("")
                && !jtxtApellido.getText().equals("") && !jtxtCoopTaxi.getText().equals("")
                && !jtxtEmail.getText().equals("") && !jtxtContraseña.getText().equals("")) ? true : false;
    }
    private boolean validarCamposNoVacios2() {
        return (!jtxtCedula.getText().equals("") && !jtxtNombre.getText().equals("")
                && !jtxtApellido.getText().equals("") && !jtxtCoopTaxi.getText().equals("")
                && !jtxtEmail.getText().equals("")) ? true : false;
    }
    
    public void guardarTaxista(){
        if(validarCamposNoVacios()){
        Taxistas cl = new Taxistas();
        cl.setCedula(jtxtCedula.getText());
        cl.setNombre(jtxtNombre.getText());
        cl.setApellido(jtxtApellido.getText());
        cl.setCoopTaxi(jtxtCoopTaxi.getText());
        cl.setEmail(jtxtEmail.getText());
        cl.setPassw(jtxtContraseña.getText());
        registrarCliente(cl);
        }else{
            JOptionPane.showMessageDialog(null, "No se permiten campos vacios");
        }
    }
    
    public void editarTaxista(){
        if(validarCamposNoVacios2()){
        Taxistas cl = new Taxistas();
        //cl.setCedula(ced_cli = jtxtCedula.getName());
        cl.setIdTaxi(jtxtID.getText());
        cl.setNombre( jtxtNombre.getText());
        cl.setApellido(jtxtApellido.getText());
        cl.setCoopTaxi(jtxtCoopTaxi.getText());
        cl.setEmail(jtxtEmail.getText());
        editar(cl);
        }else{
            JOptionPane.showMessageDialog(null, "No se permiten campos vacios");
        }
    }
    
    public void eliminarTaxista(){
        
        Taxistas cl = new Taxistas();
        cl.setIdTaxi(jtxtID.getText());
        
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

        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblTaxis = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jtxtID = new javax.swing.JTextField();
        jtxtCedula = new javax.swing.JTextField();
        jtxtNombre = new javax.swing.JTextField();
        jtxtApellido = new javax.swing.JTextField();
        jtxtCoopTaxi = new javax.swing.JTextField();
        jtxtEmail = new javax.swing.JTextField();
        jtxtContraseña = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jbtnRegistrar = new javax.swing.JButton();
        jbtnEditar = new javax.swing.JButton();
        jbtnEliminar = new javax.swing.JButton();

        jTextField1.setText("jTextField1");

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setText("Taxistas");

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jtblTaxis.setModel(new javax.swing.table.DefaultTableModel(
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
        jtblTaxis.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jtblTaxisPropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(jtblTaxis);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel2.setText("ID:");

        jLabel3.setText("Cedula:");

        jLabel4.setText("Nombre:");

        jLabel5.setText("Apellido:");

        jLabel6.setText("Coop. Taxi:");

        jLabel7.setText("E-Mail:");

        jLabel8.setText("Contraseña:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addGap(50, 50, 50)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jtxtID)
                    .addComponent(jtxtCedula)
                    .addComponent(jtxtNombre)
                    .addComponent(jtxtApellido)
                    .addComponent(jtxtCoopTaxi)
                    .addComponent(jtxtEmail)
                    .addComponent(jtxtContraseña, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE))
                .addContainerGap(45, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jtxtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jtxtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jtxtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jtxtApellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jtxtCoopTaxi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jtxtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jtxtContraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jbtnRegistrar.setText("Registrar");
        jbtnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnRegistrarActionPerformed(evt);
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
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jbtnEliminar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jbtnRegistrar)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jbtnEditar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(jbtnRegistrar)
                .addGap(43, 43, 43)
                .addComponent(jbtnEditar)
                .addGap(41, 41, 41)
                .addComponent(jbtnEliminar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(233, 233, 233)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnRegistrarActionPerformed
        // TODO add your handling code here:
        guardarTaxista();
        limpiarCampos();
    }//GEN-LAST:event_jbtnRegistrarActionPerformed

    private void jbtnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEditarActionPerformed
        // TODO add your handling code here:
        editarTaxista();
    }//GEN-LAST:event_jbtnEditarActionPerformed

    private void jbtnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEliminarActionPerformed
        // TODO add your handling code here:
        eliminarTaxista();
    }//GEN-LAST:event_jbtnEliminarActionPerformed

    private void jtblTaxisPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jtblTaxisPropertyChange
        // TODO add your handling code here:
        cargarDatos();
    }//GEN-LAST:event_jtblTaxisPropertyChange


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton jbtnEditar;
    private javax.swing.JButton jbtnEliminar;
    private javax.swing.JButton jbtnRegistrar;
    private javax.swing.JTable jtblTaxis;
    private javax.swing.JTextField jtxtApellido;
    private javax.swing.JTextField jtxtCedula;
    private javax.swing.JTextField jtxtContraseña;
    private javax.swing.JTextField jtxtCoopTaxi;
    private javax.swing.JTextField jtxtEmail;
    private javax.swing.JTextField jtxtID;
    private javax.swing.JTextField jtxtNombre;
    // End of variables declaration//GEN-END:variables
}
