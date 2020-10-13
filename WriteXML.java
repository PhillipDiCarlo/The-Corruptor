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

public class WriteXML{

	public static void main(String argv[]) {

	  try {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("Pets");
		doc.appendChild(rootElement);

		
		for(int i = 0; i < 2; i++) {
			String Type;
			String Name;
			String Owner;
			
			
			//Need a way to determine how many and what each characteristic is
			if(i == 0) {
				Type = "Dog";
				Name = "Fido";
				Owner = "Anthony";
			}
			else {
				Type = "Cat";
				Name = "Garfield";
				Owner = "Josh";
			}
			
			
			// petInfo elements
			Element petInfo = doc.createElement("PetInfo");
			rootElement.appendChild(petInfo);
	
			// type elements
			Element type = doc.createElement("Type");
			type.appendChild(doc.createTextNode(Type));
			petInfo.appendChild(type);
	
			// name elements
			Element name = doc.createElement("Name");
			name.appendChild(doc.createTextNode(Name));
			petInfo.appendChild(name);
	
			// owner elements
			Element owner = doc.createElement("Owner");
			owner.appendChild(doc.createTextNode(Owner));
			petInfo.appendChild(owner);
		}

		
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("pets.xml"));

		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);

		transformer.transform(source, result);

		System.out.println("File saved!");

	  } catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	  } catch (TransformerException tfe) {
		tfe.printStackTrace();
	  }
	}
}