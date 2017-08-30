/*
 * Encog(tm) Java Examples v3.4
 * http://www.heatonresearch.com/encog/
 * https://github.com/encog/encog-java-examples
 *
 * Copyright 2008-2017 Heaton Research, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *   
 * For more information on Heaton Research copyrights, licenses 
 * and trademarks visit:
 * http://www.heatonresearch.com/copyright
 */
package org.encog.examples.gui.elementary;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class DisplayPanel extends JPanel {
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
