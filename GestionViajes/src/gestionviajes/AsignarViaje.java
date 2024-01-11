/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestionviajes;

import AccessServices.ServiciosGestionTI;
import Models.Asignaciones;
import Models.Viajes;
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
public class AsignarViaje extends javax.swing.JInternalFrame {
    
    ServiciosGestionTI services = ServiciosGestionTI.getInstancia();
    /**
     * Creates new form AsignarViaje
     */
    public AsignarViaje() {
        initComponents();
        cargarAsigViajeCliente();
        cargarTaxisDisponible();
        CamposVisibles();
    }
    private void CamposVisibles() {
        jtxtAClienteeee.setEnabled(false);
        
    }
    private void LimpiarCampo() {
        jtxtAClienteeee.setText("");
        
    }
    private void cancelarSeleccion() {
        jtblClientes.removeAll();
        
    }
    
    
    public void cargarDatosClientes(){
        jtblClientes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(jtblClientes.getSelectedRow() != -1){
                    int fila = jtblClientes.getSelectedRow();
        String id = "N. Viaje: "+jtblClientes.getValueAt(fila, 0).toString() 
               +"\n" +"ID Cliente: "+ jtblClientes.getValueAt(fila, 1).toString()
               +"\n" +"Calle Principal: "+jtblClientes.getValueAt(fila, 2).toString()
               +"\n" +"Calle Secundaria: "+ jtblClientes.getValueAt(fila, 3).toString()
               +"\n"+"Referencia: "+ jtblClientes.getValueAt(fila, 4).toString()
               +"\n"+"Sector: " +jtblClientes.getValueAt(fila, 5).toString()
               +"\n"+"Informacion: " +jtblClientes.getValueAt(fila, 5).toString();
        jtxtAClienteeee.setText(id);
           }
            }
 
        });
        
    }
    
    private void cargarAsigViajeCliente() {
        HttpClient cliente = new HttpClient(new OnHttpRequestComplete() {
            @Override
            public void onComplete(Response status) {
                if (status.isSuccess()) {
                    try {
                        JSONArray arrayJsonReporte = new JSONArray(status.getResult());
                        JSONObject objReporte;
                        DefaultTableModel modelo = new DefaultTableModel();
                        modelo.addColumn("N. Viaje");
                        modelo.addColumn("ID Cliente");
                        modelo.addColumn("C. Principal");
                        modelo.addColumn("C. Secundaria");
                        modelo.addColumn("Referencia");
                        modelo.addColumn("Sector");
                        modelo.addColumn("Info.");
                        int count = 0;
                        while (count < arrayJsonReporte.length()) {
                            objReporte = arrayJsonReporte.getJSONObject(count);
                            String[] reporte = {objReporte.getString("id_via"), 
                                objReporte.getString("id_cli_per"), 
                                objReporte.getString("call_pri_via"), 
                                objReporte.getString("call_sec_via"),
                                objReporte.getString("ref_via"), 
                                objReporte.getString("sect_via"),
                                objReporte.getString("inf_via")};
                            modelo.addRow(reporte);
                            count++;
                        }
                        jtblClientes.setModel(modelo);                        
                    } catch (JSONException e) {
                    }
                }
            }  
        });
        cliente.excecute(services.obtenerViajeCliente());

    }
    
    
    private void cargarTaxisDisponible() {
        HttpClient cliente = new HttpClient(new OnHttpRequestComplete() {
            @Override
            public void onComplete(Response status) {
                if (status.isSuccess()) {
                    try {
                        JSONArray arrayJsonReporte = new JSONArray(status.getResult());
                        JSONObject objReporte;
                        DefaultTableModel modelo = new DefaultTableModel();
                        modelo.addColumn("ID Taxi");
                        modelo.addColumn("Cedula");
                        modelo.addColumn("Nombre");
                        modelo.addColumn("Apellido");
                        modelo.addColumn("Cooperativa");
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
                        jtblTaxiDisponible.setModel(modelo);                        
                    } catch (JSONException e) {
                    }
                }
            }  
        });
        cliente.excecute(services.taxisDisponible());

    }
    
    
    private void registrarAsignacion(Asignaciones c) {
        try {
            HttpClient cliente = new HttpClient(new OnHttpRequestComplete() {
            @Override
            public void onComplete(Response status) {
                if (status.isSuccess()) {
                    JOptionPane.showMessageDialog(null, "la carrera se a registrado correctamente");
                    cargarAsigViajeCliente();
                    cargarTaxisDisponible();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al conectarse con el servidor");
                }
            }
        });
            //System.out.println(c.getId_via_per()+ " "+ c.getId_tax_per());
            
        cliente.excecute("http://localhost:8012/sv_viajes_taxis/asignarViajes.php?id_via_per="
                    + c.getId_via_per() + "&id_tax_per=" + c.getId_tax_per()+"");
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
    
    public void registrarAsignacion(){
        int filaCliente = jtblClientes.getSelectedRow();
        int filaTaxi = jtblTaxiDisponible.getSelectedRow();
        Asignaciones a = new Asignaciones();
        a.setId_via_per(jtblClientes.getValueAt(filaCliente, 0).toString());
        a.setId_tax_per(jtblTaxiDisponible.getValueAt(filaTaxi, 0).toString());
        
        registrarAsignacion(a);
    } 
    
    
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jtblClientes = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jbtnPasarDatos = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtblTaxiDisponible = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtxtAClienteeee = new javax.swing.JTextArea();
        jbtnAceptar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);

        jtblClientes.setModel(new javax.swing.table.DefaultTableModel(
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
        jtblClientes.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jtblClientesPropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(jtblClientes);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("Asignar Viajes");

        jbtnPasarDatos.setText(">>>>>");
        jbtnPasarDatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbtnPasarDatosMouseClicked(evt);
            }
        });
        jbtnPasarDatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnPasarDatosActionPerformed(evt);
            }
        });

        jButton1.setText("<<<<<");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jtblTaxiDisponible.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jtblTaxiDisponible);

        jtxtAClienteeee.setColumns(20);
        jtxtAClienteeee.setFont(new java.awt.Font("Monospaced", 1, 13)); // NOI18N
        jtxtAClienteeee.setRows(5);
        jScrollPane3.setViewportView(jtxtAClienteeee);

        jbtnAceptar.setText("Asignar");
        jbtnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnAceptarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1)
                            .addComponent(jbtnPasarDatos))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)))
                .addGap(18, 18, 18))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(453, 453, 453))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jbtnAceptar)
                        .addGap(190, 190, 190))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jbtnAceptar)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGap(160, 160, 160)
                .addComponent(jbtnPasarDatos)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jtblClientesPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jtblClientesPropertyChange
        // TODO add your handling code here:
        cargarDatosClientes();
    }//GEN-LAST:event_jtblClientesPropertyChange

    private void jbtnPasarDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnPasarDatosActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jbtnPasarDatosActionPerformed

    private void jbtnPasarDatosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jbtnPasarDatosMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jbtnPasarDatosMouseClicked

    private void jbtnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnAceptarActionPerformed
        // TODO add your handling code here:
        registrarAsignacion();
        cargarTaxisDisponible();
    }//GEN-LAST:event_jbtnAceptarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        LimpiarCampo();
        cancelarSeleccion();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton jbtnAceptar;
    private javax.swing.JButton jbtnPasarDatos;
    private javax.swing.JTable jtblClientes;
    private javax.swing.JTable jtblTaxiDisponible;
    private javax.swing.JTextArea jtxtAClienteeee;
    // End of variables declaration//GEN-END:variables
}
