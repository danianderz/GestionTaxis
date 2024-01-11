/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author zapju
 */
public class Asignaciones {
    String id_via_per, id_tax_per;

    public Asignaciones(String id_cli_per, String id_tax_per) {
        this.id_via_per = id_cli_per;
        this.id_tax_per = id_tax_per;
    }

    public Asignaciones() {
    }

    public String getId_via_per() {
        return id_via_per;
    }

    public void setId_via_per(String id_cli_per) {
        this.id_via_per = id_cli_per;
    }

    public String getId_tax_per() {
        return id_tax_per;
    }

    public void setId_tax_per(String id_tax_per) {
        this.id_tax_per = id_tax_per;
    }
    
    
    
    
    
}
