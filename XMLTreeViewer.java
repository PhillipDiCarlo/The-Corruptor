//Java Packages //

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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

// JTree model viewer //
public class XMLTreeViewer {

    private JTree xmlJTree;
    DefaultTreeModel treeModel;
    int lineCounter;

    DefaultMutableTreeNode base = new DefaultMutableTreeNode("XML Viewer in Tree Structure");
    static XMLTreeViewer treeViewer = null;
    JTextField txtFile = null;


    // Main Program Start //
    public static void main(String[] args) {

        treeViewer = new XMLTreeViewer();

// treeViewer.xmlSetUp();

        treeViewer.createUI();

    }

    // Parser takes in current XML file to parse //
    public void xmlSetUp(File xmlFile) {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            Node root = (Node) doc.getDocumentElement();

            if (root != null) {
                DefaultTreeModel dtModel = new DefaultTreeModel(builtTreeNode(root));
                xmlJTree.setModel(dtModel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private DefaultMutableTreeNode builtTreeNode(Node root) {
        DefaultMutableTreeNode dmtNode;

        dmtNode = new DefaultMutableTreeNode(root.getNodeName());

        NamedNodeMap attrMap = root.getAttributes();
        if (attrMap != null) {
            for (int i = 0; i < attrMap.getLength(); i++) {
                Node attribute = attrMap.item(i);
                DefaultMutableTreeNode currentAtt = new DefaultMutableTreeNode(attribute.getNodeName() + " : "
                        + attribute.getNodeValue());
                dmtNode.add(currentAtt);
            }
        }

        NodeList nodeList = root.getChildNodes();
        for (int count = 0; count < nodeList.getLength(); count++) {
            Node tempNode = nodeList.item(count);

            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                if (tempNode.hasChildNodes()) {
                    dmtNode.add(builtTreeNode(tempNode));
                }
            } else if (tempNode.getNodeType() == Node.TEXT_NODE) {
                String text = tempNode.getNodeValue();
                if (text.trim().length() > 0) {
                    dmtNode.add(new DefaultMutableTreeNode("Description : " + tempNode.getNodeValue()));
                }
            }
        }
        return dmtNode;
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
