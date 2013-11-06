package main.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;


@SuppressWarnings({"serial", "deprecation"})
public class Chart extends JPanel
{
	private Map<Color, Integer> bars = new LinkedHashMap<Color, Integer>();
	
	private JFrame barFrame;
	
	public Chart (JFrame mainFrame) {
		final JFrame frame = mainFrame;
		barFrame = new JFrame();
		barFrame.setSize(100, 200);
		barFrame.setBounds(1000, 100, 100, 200);
		barFrame.addWindowListener(new WindowListener () {
			public void windowClosing(WindowEvent arg0) {
				frame.enable();
			}
			public void windowOpened(WindowEvent arg0) { 
				frame.disable();
			}
			public void windowActivated(WindowEvent arg0) { }
			public void windowClosed(WindowEvent arg0) { }
			public void windowDeactivated(WindowEvent arg0) { }
			public void windowDeiconified(WindowEvent arg0) { }
			public void windowIconified(WindowEvent arg0) { }
		});
		barFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		barFrame.setVisible(true);
	}
	
	public void addBar(Color color, int value) {
		bars.put(color, value);
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		int max = Integer.MIN_VALUE;
		for (Integer value : bars.values())
			max = Math.max(max, value);
		
		int width = (getWidth() / bars.size()) - 2;
		int x = 1;
		for (Color color : bars.keySet()) {
			int value = bars.get(color);
			int height = (int) 
                            ((getHeight()-5) * ((double)value / max));
			g.setColor(color);
			g.fillRect(x, getHeight() - height, width, height);
			g.setColor(Color.black);
			g.drawRect(x, getHeight() - height, width, height);
			x += (width + 2);
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(bars.size() * 10 + 2, 50);
	}
	
	public void addChart(Chart chart) {
		barFrame.getContentPane().add(chart);
	}
}