/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corruptor.models;

import java.util.List;


public class Constrains {
     private String xmlname;
     private String name;
     private BoundTypes bound;
     private Variants variant;

     
     public Constrains(){
         
     }
     
     public Constrains(String name, String xml){
         this.xmlname=xml;
         this.name=name;
     }
     
     
    /**
     * @return the xmlname
     */
    public String getXmlname() {
        return xmlname;
    }

    /**
     * @param xmlname the xmlname to set
     */
    public void setXmlname(String xmlname) {
        this.xmlname = xmlname;
    }

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
     * @return the bound
     */
    public BoundTypes getBound() {
        return bound;
    }

    /**
     * @param bound the bound to set
     */
    public void setBound(BoundTypes bound) {
        this.bound = bound;
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
     
    
     
     
     
}
