package panel;

import javax.imageio.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.text.*;

public class Stats extends JPanel {
	private BufferedImage days10;
	private BufferedImage days7;
	private BufferedImage dcheck;
	private List stats;
	private List<Double> aggStats;
	private NumberFormat nf = NumberFormat.getIntegerInstance();

	public Stats() {
		try {
			days10 = ImageIO.read(Stats.class.getResource("/days_10.png"));
			days7 = ImageIO.read(Stats.class.getResource("/days_7.png"));
			dcheck = ImageIO.read(Stats.class.getResource("/day_check.png"));
		} catch(IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void setStats(List stats) {
		this.stats = stats;
	}

	public void setAggStats(List<Double> aggStats) {
		this.aggStats = aggStats;
	}

	public Dimension getPreferredSize() {
		return new Dimension(30 * 8, 41 /* weird value. ... */);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(stats != null) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, 1000, 100);
			g.setColor(Color.BLACK);
			g.drawImage(days10, 0, 0, null);
			g.drawImage(days10, 80, 0, null);
			g.drawImage(days10, 160, 0, null);
			g.drawImage(days7, 0, 8, null);
			g.drawImage(days7, 7*8, 8, null);
			g.drawImage(days7, 14*8, 8, null);
			g.drawImage(days7, 21*8, 8, null);
			g.drawImage(days7, 28*8, 8, null);
			g.drawLine(240, 0, 240, 30);
			for(int i : (List<Integer>)stats) {
				g.drawImage(dcheck, i*8, 16, null);
			}
			g.setFont(new Font("Bitstream Vera Sans Mono", Font.PLAIN, 10));
			g.drawString(x(7, aggStats.get(0)), 247, 9);
			g.drawString(x(10, aggStats.get(1)), 247, 21);
			g.drawLine(350, 0, 350, 30);
			g.drawString(x(30, aggStats.get(2)), 357, 9);
			g.drawString(x(60, aggStats.get(3)), 357, 21);
		}
	}

	private String x(int num, double stat) {
		if(stat > 1.0) /* days_between multiplier can screw this up */
			stat = 1.0;
		return String.format("%2d days: " + nf.format(stat * 100.0) + " %%", num);
	}
}
