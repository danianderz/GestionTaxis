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
public class Viajes {
    String id_via,id_cli_per,call_pri_via,ref_via,call_sec_via,sect_via,inf_via;

    public Viajes(String id_via, String id_cli_per, String call_pri_via, String ref_via, String call_sec_via, String sect_via, String inf_via) {
        this.id_via = id_via;
        this.id_cli_per = id_cli_per;
        this.call_pri_via = call_pri_via;
        this.ref_via = ref_via;
        this.call_sec_via = call_sec_via;
        this.sect_via = sect_via;
        this.inf_via = inf_via;
    }

    

    public Viajes() {
    }

    public String getRef_via() {
        return ref_via;
    }

    public void setRef_via(String ref_via) {
        this.ref_via = ref_via;
    }
    
    
    public String getId_via() {
        return id_via;
    }

    public void setId_via(String id_via) {
        this.id_via = id_via;
    }

    public String getId_cli_per() {
        return id_cli_per;
    }

    public void setId_cli_per(String id_cli_per) {
        this.id_cli_per = id_cli_per;
    }

    public String getCall_pri_via() {
        return call_pri_via;
    }

    public void setCall_pri_via(String call_pri_via) {
        this.call_pri_via = call_pri_via;
    }

    public String getCall_sec_via() {
        return call_sec_via;
    }

    public void setCall_sec_via(String call_sec_via) {
        this.call_sec_via = call_sec_via;
    }

    public String getSect_via() {
        return sect_via;
    }

    public void setSect_via(String sect_via) {
        this.sect_via = sect_via;
    }

    public String getInf_via() {
        return inf_via;
    }

    public void setInf_via(String inf_via) {
        this.inf_via = inf_via;
    }
    
    
}
