/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AccessServices;

/**
 *
 * @author zapju
 */
public class ServiciosGestionTI {
    private static ServiciosGestionTI instancia;

    private ServiciosGestionTI() { }
    
    public static ServiciosGestionTI getInstancia() {
        if (instancia == null) {
            instancia = new ServiciosGestionTI();
        }
        return instancia;
    }
    public String obtenerClient() {
        return "http://localhost:8012/sv_viajes_taxis/listarClientes.php";
    }
    public String obtenerTacista() {
        return "http://localhost:8012/sv_viajes_taxis/listarTaxistas.php";
    }
    
    public String taxisDisponible() {
        return "http://localhost:8012/sv_viajes_taxis/listTaxDisp.php";
    }
    
    public String obtenerViajeCliente() {
        return "http://localhost:8012/sv_viajes_taxis/listarSolicTaxi.php";
    }
    public String registrarCliente(String ced_cli, String nom_cli, String ape_cli, String tel_cli, String email_cli,
                                        String passw_cli) {
        return "http://localhost:8012/sv_viajes_taxis/ingresarCliente.php?ced_cli="
                    + ced_cli + "&nom_cli=" + nom_cli + "&ape_cli=" + ape_cli + "&tel_cli=" + tel_cli + "&email_cli=" + email_cli
                    + "&passw_cli=" + passw_cli;
    }
    public String editarCliente(String ced_cli, String nom_cli, String ape_cli, String tel_cli, String email_cli) {
        return "http://localhost:8012/sv_viajes_taxis/actualizarCliente.php?ced_cli=" + ced_cli 
                + "&nom_cli=" + nom_cli + "&ape_cli=" + ape_cli + "&tel_cli=" + tel_cli + "&email_cli=" + email_cli;
    }
}
