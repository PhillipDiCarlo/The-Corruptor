/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corruptor.models;

import java.util.List;


public class Events {
    private String name;
    private Variants  variant;
    private List<Constrains> constraintlist;
    private LinkManifest linkmanigest;

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
     * @return the variant
     */
    public Variants getVariant() {
        return variant;
    }

    /**
     * @param variant the variant to set
     */
    public void setVariant(Variants variant) {
        this.variant = variant;
    }

    /**
     * @return the constraintlist
     */
    public List<Constrains> getConstraintlist() {
        return constraintlist;
    }

    /**
     * @param constraintlist the constraintlist to set
     */
    public void setConstraintlist(List<Constrains> constraintlist) {
        this.constraintlist = constraintlist;
    }

    /**
     * @return the linkmanigest
     */
    public LinkManifest getLinkmanigest() {
        return linkmanigest;
    }

    /**
     * @param linkmanigest the linkmanigest to set
     */
    public void setLinkmanigest(LinkManifest linkmanigest) {
        this.linkmanigest = linkmanigest;
    }
    
    
    
}
