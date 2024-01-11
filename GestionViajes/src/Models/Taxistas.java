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
public class Taxistas {
    String idTaxi,cedula, nombre,apellido,coopTaxi,email,passw;

    public Taxistas(String idTaxi, String cedula, String nombre, String apellido, String coopTaxi, String email, String passw) {
        this.idTaxi = idTaxi;
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
        this.coopTaxi = coopTaxi;
        this.email = email;
        this.passw = passw;
    }

    

    public Taxistas() {
    }

    public String getIdTaxi() {
        return idTaxi;
    }

    public void setIdTaxi(String idTaxi) {
        this.idTaxi = idTaxi;
    }
    
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCoopTaxi() {
        return coopTaxi;
    }

    public void setCoopTaxi(String coopTaxi) {
        this.coopTaxi = coopTaxi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassw() {
        return passw;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }
    
    
}
