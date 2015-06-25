/**
 * 
 */
package com.crowd.streetbuzzalgo.jfreechart;

import java.awt.Color;
import java.awt.Paint;

import org.jfree.chart.renderer.category.BarRenderer;

/**
 * @author Atrijit
 *
 */
public class CustomRenderer extends BarRenderer {
	private Paint[] colors;
	
	public CustomRenderer() 
	 { 
	    /*this.colors = new Paint[] {Color.green, Color.red, Color.red, 
	      Color.yellow, Color.orange, Color.cyan, 
	      Color.magenta, Color.blue};*/ 
		this.colors = new Paint[] {Color.green, Color.red, Color.gray};
	 }
	public Paint getItemPaint(final int row, final int column) 
	 { 
	    // returns color for each column 
	    return (this.colors[column % this.colors.length]); 
	 } 
}
