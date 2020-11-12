/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corruptor.models;


public class BoundTypes {
    private String name;
    private Double value;
    
    public BoundTypes(){
        
    }
    
    public BoundTypes(String name){
        this.name=name;
        this.value=1.0;
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
     * @return the value
     */
    public Double getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Double value) {
        this.value = value;
    }
    
    
    
    
}
