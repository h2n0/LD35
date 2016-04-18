package uk.fls.h2n0.main.util;

import java.awt.image.BufferedImage;

import fls.engine.main.art.SplitImage;

public class Loader {

	
	public static int[] getPixels(BufferedImage img, int x, int y, int s){
		return getPixels(img, x, y, s,s);
	}
	
	public static int[] getPixels(BufferedImage img, int x, int y, int xs, int ys){
		
		BufferedImage[][] nimg = new SplitImage(img).split(xs, ys);
		int w = nimg[x][y].getWidth();
		int h = nimg[x][y].getHeight();
		
		int[] current = new int[w * h];
		nimg[x][y].getRGB(0, 0, w, h, current, 0, w);
		
		int ss = xs * ys;
		int[] res = new int[w * h];
		
		if(nimg[0][0].getType() == BufferedImage.TYPE_INT_RGB){// No transparency
			for(int i = 0; i < ss; i++){
				int c = current[i];
				int r = (c >> 16) & 0xFF;
				int g = (c >> 8) & 0xFF;
				int b = (c) & 0xFF;
				int rgb = (r << 16) | (g << 8) | b;
				res[i] = rgb;
			}
		}else{
			for(int i = 0; i < ss; i++){
				int c = current[i];
				int a = (c >> 24) & 0xFF;
				int r = (c >> 16) & 0xFF;
				int g = (c >> 8) & 0xFF;
				int b = (c) & 0xFF;
				int rgb = (r << 16) | (g << 8) | b;
				res[i] = rgb;
			}
		}
		
		return res;
	}
}
