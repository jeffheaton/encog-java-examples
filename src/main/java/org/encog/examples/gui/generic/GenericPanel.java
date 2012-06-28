package org.encog.examples.gui.generic;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class GenericPanel extends JPanel {
	private Image currentImage;

	public Image getCurrentImage() {
		return currentImage;		
	}
	
	public void setCurrentImage(Image currentImage) {
		this.currentImage = currentImage;
		setPreferredSize(new Dimension(this.currentImage.getWidth(null), this.currentImage.getHeight(null)));
		repaint();
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		if( this.currentImage!=null ) {
			g.drawImage(this.currentImage, 0, 0, null);
		}
	}
	
	
	
}
