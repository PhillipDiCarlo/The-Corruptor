/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corruptor.controllers;

import corruptor.models.BoundTypes;
import corruptor.models.Constrains;
import corruptor.models.Events;
import corruptor.models.Tests;
import corruptor.models.Variants;
import java.io.File;
import javax.swing.JComboBox;
import javax.swing.JTree;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import org.xml.sax.SAXException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class TestController {

    public TestController() {
    }

    public Tests ImportXmltoModel(File xmlFile,String testname) throws SAXException,
            IOException, ParserConfigurationException  {
        
        Tests test=new Tests();
        test.setName(testname);
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);

        doc.getDocumentElement().normalize();

       //Iteration of Events
        NodeList eventListnode = doc.getElementsByTagName("event");
        
        List<Events> eventlist = new ArrayList();
        
        for (int i = 0; i < eventListnode.getLength(); i++) {

            Element eventnode =(Element) eventListnode.item(i);
            Events event=new Events();
            event.setName("event-"+i+1);
            
            //Get the Variant
            Node variantnode=eventnode.getElementsByTagName("variant").item(0);
            Variants variant=new Variants();
            variant.setXmlname(variantnode.getTextContent());
            variant.setName(GetVariantName(variantnode.getTextContent()));
            event.setVariant(variant);
            
            //System.out.println("\nCurrent Element: " + variantnode.getTextContent());
            
            //Iterate by Contraints
            NodeList constraintsListnode = doc.getElementsByTagName("constraint");
            List<Constrains> constraintsList=new ArrayList();
            
            for (int j = 0; j < constraintsListnode.getLength(); j++) {
                    
                
                    Element constraintnode =(Element) constraintsListnode.item(j);
                    Constrains constraint=new Constrains();
               
                    //Get the Type
                    Node typenode=constraintnode.getElementsByTagName("type").item(0);
                    constraint.setXmlname(typenode.getTextContent());
                    constraint.setName( GetConstraintName(typenode.getTextContent()));
              

                    //Get the Bound
                    Node boundnode=constraintnode.getElementsByTagName("bounds").item(0);
                    BoundTypes bound=new BoundTypes();
                    
                    if (boundnode!=null)
                    bound.setName(boundnode.getTextContent());
 
                    //Get the Value
                    Node valuenode=constraintnode.getElementsByTagName("value").item(0);
                    
                    if (valuenode!=null)
                    bound.setValue(Double.parseDouble(valuenode.getTextContent()));
                    
                    constraint.setBound(bound);
                    
                    constraintsList.add(constraint);
                    
            
            }
            event.setConstraintlist(constraintsList);
          
            
            eventlist.add(event);
        }
        
        test.setEventlist(eventlist);
        
        return test;
        
    }
    
    public void ExportModeltoXml(Tests test,File file) throws SAXException,
            IOException, ParserConfigurationException, TransformerConfigurationException, TransformerException  {
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();
        
        Element root= doc.createElement("test");
        doc.appendChild(root);
        
        //Events
        List<Events> eventlist = test.getEventlist();
        
        for (Events event:eventlist){
            Element eventnode= doc.createElement("event");
            //Variant
            Variants variant = event.getVariant();
            Element variantnode= doc.createElement("variant");
            variantnode.appendChild(doc.createTextNode(variant.getXmlname()));
            eventnode.appendChild(variantnode);
            
           //Constraints         
           List<Constrains> constraintlist = event.getConstraintlist();
           for(Constrains constrain :constraintlist){
                 Element constraintnode= doc.createElement("constraint");
                 Element typenode= doc.createElement("type");
                 typenode.appendChild(doc.createTextNode(constrain.getXmlname()));
                 constraintnode.appendChild(typenode);
                 
                 
                 //Add Bounds
                 BoundTypes bound=constrain.getBound();
                 if (bound!=null){
                    Element boundnode= doc.createElement("bounds");
                    boundnode.appendChild(doc.createTextNode(bound.getName()));
                    Element valuenode= doc.createElement("value");
                    valuenode.appendChild(doc.createTextNode(bound.getValue().toString()));
                    constraintnode.appendChild(boundnode);
                    constraintnode.appendChild(valuenode);
                 }
                 
                 eventnode.appendChild(constraintnode);
                 
           }
                  
           root.appendChild(eventnode);
        }
        
        
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
         transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
         DOMSource source = new DOMSource(doc);
         StreamResult result = new StreamResult(file);
         transformer.transform(source, result);
         
         // Output to console for testing
         StreamResult consoleResult = new StreamResult(System.out);
         transformer.transform(source, consoleResult);
        
        
        
        
        
    }
    

    public void GenerateTreeFromModel(JTree tree, Tests test){
      
        
        
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Test");
        //Events
        List<Events> eventlist = test.getEventlist();
        
        for (Events event:eventlist){
            DefaultMutableTreeNode eventnode =new DefaultMutableTreeNode(event.getName());
            //Variant
            Variants variant = event.getVariant();
            DefaultMutableTreeNode variantnode =new DefaultMutableTreeNode(variant.getName());
            eventnode.add(variantnode);
            
           //Constraints         
           List<Constrains> constraintlist = event.getConstraintlist();
           for(Constrains constrain :constraintlist){
                 DefaultMutableTreeNode constraintnode = new DefaultMutableTreeNode(constrain.getName());
                 
                 //Add Bounds
                 BoundTypes bound=constrain.getBound();
                 if (bound!=null)
                 {
                 DefaultMutableTreeNode boundnode = new DefaultMutableTreeNode(bound.getName());
                 constraintnode.add(boundnode);
                 }
                 eventnode.add(constraintnode);
                 
           }
                   
            root.add(eventnode);
        }
        
        JTree tree1=new JTree(root);
        
        tree.setModel(tree1.getModel());
        ExpandAllNodes(tree, 0, tree.getRowCount());
        
                
        
    }
    
    private void ExpandAllNodes (JTree tree,  int start, int rowcount){
        
        for (int i=start;i<rowcount;i++){
            tree.expandRow(i);
        }
        
        if (tree.getRowCount()!=rowcount){
            ExpandAllNodes(tree, rowcount, tree.getRowCount());
        }
        
    }
    
    public void FillVariantComboboxfromEvent(JComboBox combo, Events event ){
      
        combo.removeAllItems();
        combo.addItem(event.getVariant().getName());
        
    }

    public void FillConstraintsComboboxfromEvent(JComboBox combo, Events event ){
        
            combo.removeAllItems();
            List<Constrains> constrainslist= event.getConstraintlist();
            
            for (Constrains constraint : constrainslist ){
                combo.addItem(constraint.getName());
            }
            
            
        
    }

    public void FillBoundsandValueComboboxfromEvent(JComboBox combo,JComboBox combo2, Events event, String constraintname ){
        
            combo.removeAllItems();
            combo2.removeAllItems();
            List<Constrains> constrainslist= event.getConstraintlist();
            
            for (Constrains constraint : constrainslist ){
                if (constraint.getName().equals(constraintname)){
                     if (constraint.getBound()!=null){
                            combo.addItem(constraint.getBound().getName());
                            combo2.addItem(constraint.getBound().getValue());
                     }
                    
                }
                
            }
            
        
        
    }
    
    public String GetConstraintName(String xmlname){
        String name="";
        switch ( xmlname.trim()) {
            case "acq_complete_flag":
            name="Acq Complete";
            break;
            case "dwell_cnt":
            name="Dwell Count";
            break;
            case "link_event_done":
            name="Link Event Done";
            break;
            case "link_sequence_done":
            name="Link Sequence Done";
            break;
            case "time_of_flight":
            name="Time of Flight";
            break;
            case "time_until_intercept":
            name="Time Until Intercept";
            break;
            case "uplink_lost_inject_count":
            name="Uplink Lost Inject Count";
            break;
            case "endless_event_flag":
            name="Endless Event Flag";
            break;
            case "link_manifest":
            name="Link Manifest";
            break;
            case "acquisition_count":
            name="Acquisition Count";
            break;
            case "uplink_count":
            name="Uplink Count";
            break;
            case "rate_change":
            name="Rate Change";
            break;
            case "ege_flag":
            name="EGE Flag";
            break;
            case "time_based_error":
            name="Time Based Error";
            break;
            default:
            name="N/A";
        }
        return name;
        
    }
    
    
    
    public String GetVariantName(String xmlname){
        String name="";
        switch ( xmlname.trim()) {
            case "ACQ_LINK_ERRORS":
            name="Acquisition Link Errors";
            break;
            case "ACQ_SUBPULSE_ERRORS":
            name="Acquisition Subpulse Errors";
            break;
            case "EXIT_AFTER_ZONE":
            name="Exit After Zone";
            break;
            case "EXIT_ON_REACQ":
            name="Exit On Reaquisition";
            break;
            case "EXIT_ON_TRANSITION":
            name="Exit On Transition";
            break;
            case "LINK_ERRORS":
            name="Link Errors";
            break;
            case "NO_ERRORS":
            name="No Errors";
            break;
            case "NO_ERRORS_IGNORE_REAQ":
            name="No Errors Ignore Reaq";
            break;
            case "PHASE_THREE_LINK_ERRORS":
            name="Phase 3 Link Errors";
            break;
            case "PHASE_TRANSITION_DOWNLINK_ERRORS":
            name="Phase Transition Downlink Errors";
            break;
            case "PHASE_TRANSITION_UPLINK_ERRORS":
            name="Phase Transition Uplink Errors";
            break;
            case "SEND_SDM_PARAMETERS":
            name="Send SDM Parameters";
            break;
            case "UPLINK_LATE":
            name="Uplink Late";
            break;
            case "UPLINK_LOST":
            name="Uplink Lost";
            break;
            case "BURST_INTERRUPT":
            name="Burst Interrupt";
            break;
            default:
              name="N/A";
                    
        }
        return name;
        
    }
 
    
    public void DeleteBound(Events event,String constraintname){
        
            List<Constrains> constrainslist= event.getConstraintlist();
            
            for (Constrains constraint : constrainslist ){
                if (constraint.getName().equals(constraintname)){
                     constraint.setBound(null);
                }
                
            }

    }


    
    
    public void DeleteEvent(Tests test ,String eventname){
        
        List<Events> eventlist = test.getEventlist();
        
        for (Events event :eventlist){
                
            if (event.getName().equals(eventname)){
                eventlist.remove(event);
                break;
            }
            
        }
        
            
    }
    
    public void ListVariant(JComboBox combo, List<Variants> variantlist){
        //Add all the Variant to the list
        
        combo.removeAllItems();
        variantlist.add(new Variants("Acquisition Link Errors","ACQ_LINK_ERRORS"));
        variantlist.add(new Variants("Acquisition Subpulse Errors","ACQ_SUBPULSE_ERRORS"));
        variantlist.add(new Variants("Exit After Zone","EXIT_AFTER_ZONE"));
        variantlist.add(new Variants("Exit On Reaquisition","EXIT_ON_REACQ"));
        variantlist.add(new Variants("Exit On Transition","EXIT_ON_TRANSITION"));
        variantlist.add(new Variants("Link Errors","LINK_ERRORS"));
        variantlist.add(new Variants("No Errors","NO_ERRORS"));
        variantlist.add(new Variants("No Errors Ignore Reaq","NO_ERRORS_IGNORE_REAQ"));
        variantlist.add(new Variants("Phase 3 Link Errors","PHASE_THREE_LINK_ERRORS"));
        variantlist.add(new Variants("Phase Transition Downlink Errors","PHASE_TRANSITION_DOWNLINK_ERRORS"));
        variantlist.add(new Variants("Phase Transition Uplink Errors","PHASE_TRANSITION_UPLINK_ERRORS"));
        variantlist.add(new Variants(" Send SDM Parameters ","SEND_SDM_PARAMETERS"));
        variantlist.add(new Variants("Uplink Late","UPLINK_LATE"));
        variantlist.add(new Variants("Uplink Lost","UPLINK_LOST"));
        variantlist.add(new Variants("Burst Interrupt","BURST_INTERRUPT"));
        
         for (Variants variant : variantlist ){
                combo.addItem(variant.getName());
            }
        
    }
    
    public void ListConstraint(JComboBox combo,List<Constrains> constraintlist){
           //Add all the constraint to the list
        
        combo.removeAllItems();
        constraintlist.add(new Constrains("Acq Complete","acq_complete_flag"));
        constraintlist.add(new Constrains("Dwell Count","dwell_cnt"));
        constraintlist.add(new Constrains("Link Event Done","link_event_done"));
        constraintlist.add(new Constrains("Link Sequence Done","link_sequence_done"));
        constraintlist.add(new Constrains("Time of Flight","time_of_flight"));
        constraintlist.add(new Constrains("Time Until Intercept","time_until_intercept"));
        constraintlist.add(new Constrains("Uplink Lost Inject Count","uplink_lost_inject_count"));
        constraintlist.add(new Constrains("Endless Event Flag","endless_event_flag"));
        constraintlist.add(new Constrains("Link Manifest","link_manifest"));
        constraintlist.add(new Constrains("Acquisition Count","acquisition_count"));
        constraintlist.add(new Constrains("Uplink Count","uplink_count"));
        constraintlist.add(new Constrains("Rate Change","rate_change"));
        constraintlist.add(new Constrains("EGE Flag","ege_flag"));
        constraintlist.add(new Constrains("Time Based Error","time_based_error"));
        
         for (Constrains constraint : constraintlist ){
                combo.addItem(constraint.getName());
            }
        
        
    }
    
    public void ListBounds(JComboBox combo, List<BoundTypes> boundlist){
           //Add all the Bounds to the list
        
        combo.removeAllItems();
        boundlist.add(new BoundTypes("CONSTRAINT_BOUND_DISABLED"));
        boundlist.add(new BoundTypes("CONSTRAINT_BOUND_GREATER"));
        boundlist.add(new BoundTypes("CONSTRAINT_BOUND_GREATER_OR_EQUAL"));
        boundlist.add(new BoundTypes("CONSTRAINT_BOUND_LESSER"));
        boundlist.add(new BoundTypes("CONSTRAINT_BOUND_LESSER_OR_EQUAL"));
        boundlist.add(new BoundTypes("CONSTRAINT_BOUND_EQUAL"));
        
         for (BoundTypes bound : boundlist ){
                combo.addItem(bound.getName());
            }
        
    }
    
    public String getVariantXML(List<Variants> listavariants, String variantname){
        String xmlname="";
        for(Variants variant:listavariants){
            if (variant.getName().equals(variantname)){
                xmlname=variant.getXmlname();
            }
        }
        
        return xmlname;
    }

    public String getConstraintXML(List<Constrains> listaconstraints, String constraintname){
        String xmlname="";
        for(Constrains constraint:listaconstraints){
            if (constraint.getName().equals(constraintname)){
                xmlname=constraint.getXmlname();
            }
        }
        
        return xmlname;
    }
    
    public void ListValues(JComboBox combo){
        combo.removeAllItems();
        combo.addItem(1.0);
        combo.addItem(2.0);
        combo.addItem(3.0);
        combo.addItem(4.0);
        combo.addItem(5.0);
        combo.addItem(6.0);
        combo.addItem(7.0);
        combo.addItem(8.0);
        combo.addItem(9.0);
    }
    
    public Events GetEventbyText(Tests test, String eventname){
        
        List<Events> eventlist=test.getEventlist();
        for (Events event:eventlist){
                if (event.getName().equals(eventname)){
                    return event;
                }
        }
        return null;
    }
    
}
