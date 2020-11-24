/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corruptor.models;

import java.util.List;


public class LinkManifest {
  private String name;
  private List<Step> steplist; 

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
     * @return the steplist
     */
    public List<Step> getSteplist() {
        return steplist;
    }

    /**
     * @param steplist the steplist to set
     */
    public void setSteplist(List<Step> steplist) {
        this.steplist = steplist;
    }
  
  
  
  
}
