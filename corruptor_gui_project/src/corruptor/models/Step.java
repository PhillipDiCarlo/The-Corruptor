/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corruptor.models;

import java.util.List;


public class Step {

    private String name;
    private LinkErrorCount linkerrorcount;
    private CooldownTime cooldowntime;
    private List<CorruptionType>  corruptiontypelist;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the linkerrorcount
     */
    public LinkErrorCount getLinkerrorcount() {
        return linkerrorcount;
    }

    /**
     * @param linkerrorcount the linkerrorcount to set
     */
    public void setLinkerrorcount(LinkErrorCount linkerrorcount) {
        this.linkerrorcount = linkerrorcount;
    }

    /**
     * @return the cooldowntime
     */
    public CooldownTime getCooldowntime() {
        return cooldowntime;
    }

    /**
     * @param cooldowntime the cooldowntime to set
     */
    public void setCooldowntime(CooldownTime cooldowntime) {
        this.cooldowntime = cooldowntime;
    }

    /**
     * @return the corruptiontypelist
     */
    public List<CorruptionType> getCorruptiontypelist() {
        return corruptiontypelist;
    }

    /**
     * @param corruptiontypelist the corruptiontypelist to set
     */
    public void setCorruptiontypelist(List<CorruptionType> corruptiontypelist) {
        this.corruptiontypelist = corruptiontypelist;
    }

    
    
    
}
