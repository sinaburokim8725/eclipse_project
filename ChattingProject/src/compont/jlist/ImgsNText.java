package compont.jlist;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import javax.swing.Icon;

///MODEL FOR CUSTOM ROW IN A JLIST
public class ImgsNText{
 
    //FIELDS
    private String name;
    private Icon img;

    //CONSTRUCTOR
    public ImgsNText(String text,Icon icon)
    {
        this.name=text;
        this.img=icon;
    }

    //GETTERS AND SET
	
	public String getName() {
	    return name;
	}
	
	public void setName(String name) {
	    this.name = name;
	}
	
	public Icon getImg() {
	    return img;
	}
	
	public void setImg(Icon img) {
	    this.img = img;
	}
	
	
	public class Renderer extends DefaultListCellRenderer implements ListCellRenderer<Object> {

	    /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
	    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

	        //ASSIGN TO VALUE THAT IS PASSED
	        ImgsNText is=(ImgsNText) value;

	        setText(is.getName());
	        setIcon(is.getImg());

	        //SET BACKGROUND AND FOREGROUND COLORS TO CUSTOM LIST ROW
	        if(isSelected)
	        {
	            setBackground(list.getSelectionBackground());
	         setForeground(list.getSelectionForeground());
	        }else
	        {

	            setBackground(list.getBackground());
	         setForeground(list.getForeground());
	        }

	        setEnabled(true);
	        setFont(list.getFont());

	         return this;
	    }
	}
}

/**

package ListImgs;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class ListMain extends javax.swing.JFrame {

    //MODEL
    DefaultListModel dm=new DefaultListModel();

    public ListMain() {
        initComponents();

    }

    //ADD TO LIST
    private void populate()
    {
        dm.clear();

        dm.addElement(new ImgsNText("Michael Carrick", new ImageIcon("D:\\Imgs\\carrick.jpg")));
        dm.addElement(new ImgsNText("Ander Herera", new ImageIcon("D:\\Imgs\\herera.jpg")));
        dm.addElement(new ImgsNText("Diego Costa", new ImageIcon("D:\\Imgs\\costa.jpg")));
        dm.addElement(new ImgsNText("Oscar", new ImageIcon("D:\\Imgs\\oscar.jpg")));
        dm.addElement(new ImgsNText("David Gea", new ImageIcon("D:\\Imgs\\degea.jpg")));

        //RENDER IMAGES N TEXT
        jList1.setCellRenderer(new Renderer());
        jList1.setModel(dm);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        populateBtn = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(45, 155, 193));

        jList1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        jLabel2.setText("ProgrammingWizards Channel");

        populateBtn.setBackground(javax.swing.UIManager.getDefaults().getColor("CheckBoxMenuItem.selectionBackground"));
        populateBtn.setText("Populate");
        populateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                populateBtnActionPerformed(evt);
            }
        });

        clearBtn.setText("Clear");
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(171, 171, 171)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(73, 73, 73)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(populateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(8, 8, 8)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(294, Short.MAX_VALUE)
                .addComponent(populateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72))
        );

        pack();
    }// </editor-fold>

    private void populateBtnActionPerformed(java.awt.event.ActionEvent evt) {
        populate();
    }

    private void jList1MouseClicked(java.awt.event.MouseEvent evt) {
        JOptionPane.showMessageDialog(null, ((ImgsNText)jList1.getSelectedValue()).getName());
    }

    //CLEAR
    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {
        dm.clear();
        jList1.setModel(dm);
    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
//                try {
//                    // select Look and Feel
//                    UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
//            // start application
//
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
                new ListMain().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify
    private javax.swing.JButton clearBtn;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton populateBtn;
    // End of variables declaration
}**/
