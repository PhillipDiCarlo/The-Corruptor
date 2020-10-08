// Java Packages //	 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

// JTree model viewer //
public class XMLTreeViewer extends DefaultHandler {

   private JTree xmlJTree;
   DefaultTreeModel treeModel;
   int lineCounter;

   DefaultMutableTreeNode base = new DefaultMutableTreeNode("XML Viewer in Tree Structure");
   static XMLTreeViewer treeViewer = null;
   JTextField txtFile = null;

   // Method to begin to using SAX Parser starting from base root branching out to child nodes //
   @Override
   public void startElement(String uri, String localName, String tagName, Attributes attr) throws SAXException {

      DefaultMutableTreeNode current = new DefaultMutableTreeNode(tagName);

      base.add(current);

      base = current;

      for (int i = 0; i < attr.getLength(); i++) {

         DefaultMutableTreeNode currentAtt = new DefaultMutableTreeNode(attr.getLocalName(i) + " = "

               + attr.getValue(i));

         base.add(currentAtt);
      }

   }

   // error handling for entity parsing if not caught
   public void skippedEntity(String name) throws SAXException {

      System.out.println("Skipped Entity: '" + name + "'");
   }

   // Initial view of Jtree model idle //
   @Override
   public void startDocument() throws SAXException {

      super.startDocument();
      base = new DefaultMutableTreeNode("XML Viewer");

      ((DefaultTreeModel) xmlJTree.getModel()).setRoot(base);

   }

   // Adding Description to each subsequent child node //
   public void characters(char[] ch, int start, int length) throws SAXException {

      String s = new String(ch, start, length).trim();

      if (!s.equals("")) {

         DefaultMutableTreeNode current = new DefaultMutableTreeNode("Description : " + s);

         base.add(current);

      }

   }

   // End of current child node branching stopping at parent //
   public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

      base = (DefaultMutableTreeNode) base.getParent();

   }

   // Main Program Start //
   public static void main(String[] args) {

      treeViewer = new XMLTreeViewer();

      // treeViewer.xmlSetUp();

      treeViewer.createUI();

   }

   // Ends current XML file parsing and reloads //
   @Override
   public void endDocument() throws SAXException {

      // Refresh JTree

      ((DefaultTreeModel) xmlJTree.getModel()).reload();

      //Comment this out if you want the trees to be closed by default
      //expandAll(xmlJTree);

   }

   // Expansion of Jtree structure levels //
   public void expandAll(JTree tree) {

      int row = 0;

      while (row < tree.getRowCount()) {

         tree.expandRow(row);

         row++;
      }

   }

   // Parser takes in current XML file to parse // 
   public void xmlSetUp(File xmlFile) {

      try {

         SAXParserFactory fact = SAXParserFactory.newInstance();

         SAXParser parser = fact.newSAXParser();

         parser.parse(xmlFile, this);

      } catch (Exception e) {

      }

   }

   // Jtree View component //
   public void createUI() {

      treeModel = new DefaultTreeModel(base);
      xmlJTree = new JTree(treeModel);

      JScrollPane scrollPane = new JScrollPane(xmlJTree);
      JFrame windows = new JFrame();

      windows.setTitle("XML file JTree Viewer using SAX Parser");

      JPanel pnl = new JPanel();
      pnl.setLayout(null);

      JLabel lbl = new JLabel("File :");
      txtFile = new JTextField("Selected File Name Here");

      JButton btn = new JButton("Import File");
      btn.addActionListener(new ActionListener() {

         // JFile Chooser is opened after JButton interaction //
         @Override
         public void actionPerformed(ActionEvent evt) {

            JFileChooser fileopen = new JFileChooser();
            FileFilter filter = new FileNameExtensionFilter("xml files", "xml");

            fileopen.addChoosableFileFilter(filter);
            int ret = fileopen.showDialog(null, "Open file");

            if (ret == JFileChooser.APPROVE_OPTION) {

               File file = fileopen.getSelectedFile();
               txtFile.setText(file.getPath() + File.separator + file.getName());
               xmlSetUp(file);

            }

         }

      });

      // Pane window size and interaction //
      lbl.setBounds(0, 0, 100, 30);
      txtFile.setBounds(110, 0, 250, 30);

      btn.setBounds(360, 0, 100, 30);
      scrollPane.setBounds(0, 50, 500, 600);

      pnl.add(lbl);
      pnl.add(txtFile);

      pnl.add(btn);
      pnl.add(scrollPane);

      windows.add(pnl);
      windows.setSize(500, 700);

      windows.setVisible(true);
      windows.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	    }

	}