package main.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class ButtonListener implements ActionListener {

	private String[] tweets;
	private boolean state[][];
	
	public ButtonListener (String[] uncertainTweets) {
		tweets = uncertainTweets;
		state = new boolean[tweets.length][2];
		for(int i = 0; i < tweets.length; i++) 
			for(int j = 0; j < 2; j++) 
				state[i][j] = false;
	}
	
	public void actionPerformed(ActionEvent ae) {
		JButton tmp = (JButton)ae.getSource();
		final int r = Integer.valueOf(tmp.getName().substring(0, 1));
		final int c = Integer.valueOf(tmp.getName().substring(1, 2));
		if(!state[r][c]) {
			if(c == 0) 
				JOptionPane.showOptionDialog(null, "Positive on '"+tweets[r]+"'\nNo chance to rate as negative", "Thanks for rating", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"OK"}, 0);
			else 
				JOptionPane.showOptionDialog(null, "Negative on '"+tweets[r]+"'\nNo chance to rate as positive", "Thanks for rating", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"OK"}, 0);
		}
		state[r][0] = true;
		state[r][1] = true;
	}
	
}
