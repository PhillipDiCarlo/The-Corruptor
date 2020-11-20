/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corruptor.models;


public class CorruptionType {
    
        private String name;
        private String xmlname;

        public CorruptionType(){
            
        }
        
        public CorruptionType(String name,String xmlname){
            this.name=name;
            this.xmlname=xmlname;
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
        
        
    
}
