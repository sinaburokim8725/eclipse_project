package compont.jlist;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.plaf.basic.BasicIconFactory;
import javax.swing.plaf.metal.MetalIconFactory;

public class IconListRenderer extends DefaultListCellRenderer {
	private static ImageIcon image;
	private Map<Object, Icon> icons = null;
	
	public IconListRenderer(Map<Object, Icon> icons) {
		this.icons = icons;
	}
 
	@Override
	public Component getListCellRendererComponent(
		JList list, Object value, int index,
		boolean isSelected, boolean cellHasFocus) {
		// Get the renderer component from parent class
		JLabel label = (JLabel) super.getListCellRendererComponent(list,value, index, isSelected, cellHasFocus);
		 
		// Get icon to use for the list item value
		Icon icon = icons.get(value);
		
		// Set icon to display for value
		label.setIcon(icon);


		return label;
	}
 
	public static void main(String[] args) {
		// setup mappings for which icon to use for each value

		//getClass().getResource(path)
		//String path = "resource/image/pig.png";

		//image = new ImageIcon(IconListRenderer.class.getClass().getResource("path"));


		Map<Object, Icon> icons = new HashMap<Object, Icon>();
		icons.put("details", MetalIconFactory.getFileChooserDetailViewIcon());
		icons.put("folder",  MetalIconFactory.getTreeFolderIcon());
		icons.put("computer",MetalIconFactory.getTreeComputerIcon());

		icons.put("pig", new ImageIcon("pig.png"));
		
		JFrame frame = new JFrame("resource/image/pig.pngIcon List");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// create a list with some test data
		JList list = new JList(new Object[] {"details", "computer", "folder", "computer","pig"});
		
		// create a cell renderer to add the appropriate icon
		list.setCellRenderer(new IconListRenderer(icons));
		
		frame.add(list);
		
		frame.pack();
		
		frame.setVisible(true);
	}
}
