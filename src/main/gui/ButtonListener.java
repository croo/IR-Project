package main.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
			if(c == 0) {
				JOptionPane.showOptionDialog(null, "Positive on '"+tweets[r]+"'\nNo chance to rate as negative", "Thanks for rating", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"OK"}, 0);
				tmp.setEnabled(false);
				save(tweets[r],1);
			} else { 
				JOptionPane.showOptionDialog(null, "Negative on '"+tweets[r]+"'\nNo chance to rate as positive", "Thanks for rating", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"OK"}, 0);
				tmp.setEnabled(false);
				save(tweets[r],0);
			}
		}
		state[r][0] = true;
		state[r][1] = true;
	}

	private void save(String tweet, int label) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
			        new FileOutputStream("training_data/active_learning.csv", true), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.err.println("Output file not found.");
			e.printStackTrace();
		}
		SimpleDateFormat formatter = new SimpleDateFormat("\"yyyy-dd-MM H:m:s:S\"");
		try {
			writer.append(formatter.format(new Date()) + "\t" +
							"\"@user\"" + "\t" +
							label + "\t" +
							"\""+tweet+"\"");
			writer.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
