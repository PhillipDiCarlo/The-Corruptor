import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.*;

import javax.swing.tree.TreeNode;

public class WriteXML{
//used to call
//WriteXML test = new WriteXML( root, "WrittenTest.xml");

	//Create new instance of WriteXML from main and the file will be outputted.
	public WriteXML(Node root, String filename){
		writeData(root,filename);
	}

	private void writeData(Node root, String filename){

		try {

			//create document
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	
			// create root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("test");
			doc.appendChild(rootElement);
	
			NodeList nList = root.getChildNodes();

			//loop to iterate through each child of root
			for (int temp = 0; temp < nList.getLength(); temp++) {

				//set current node to nNode
				Node nNode = nList.item(temp);
	
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	
					Element eElement = (Element) nNode;

					//Create first event
					Element event = doc.createElement("event");
					rootElement.appendChild(event);

					//Add variant to event
					Element variant = doc.createElement("variant");
					variant.appendChild(doc.createTextNode(eElement.getElementsByTagName("variant").item(0).getTextContent()));
					event.appendChild(variant);

					//Find the number of constraints and loop that number of times
					for(int i = 0; i < eElement.getElementsByTagName("constraint").getLength(); i++){

						//create constraint folder
						Element constraint = doc.createElement("constraint");
						event.appendChild(constraint);

						//add the type to constraint
						Element type = doc.createElement("type");
						type.appendChild(doc.createTextNode(eElement.getElementsByTagName("type").item(i).getTextContent()));
						constraint.appendChild(type);

						//add the bounds to constraint
						Element bounds = doc.createElement("bounds");
						bounds.appendChild(doc.createTextNode(eElement.getElementsByTagName("bounds").item(i).getTextContent()));
						constraint.appendChild(bounds);

						//add the value to constraint
						Element value = doc.createElement("value");
						value.appendChild(doc.createTextNode(eElement.getElementsByTagName("value").item(i).getTextContent()));
						constraint.appendChild(value);
					}
				}
			}
		
		
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filename));

			transformer.transform(source, result);
	
			System.out.println("File saved!");
	
		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } catch (TransformerException tfe) {
			tfe.printStackTrace();
		  }
		}
	}