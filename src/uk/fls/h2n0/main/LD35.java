package uk.fls.h2n0.main;

import java.awt.image.BufferedImage;

import fls.engine.main.Init;
import fls.engine.main.input.Input;
import uk.fls.h2n0.main.screens.SetupScreen;

@SuppressWarnings("serial")
public class LD35 extends Init{

	
	private static final int w = 400;
	private static final int h = 300;
	private static final int s = 2;
	
	public LD35(){
		super("LD35 Entry", w * s,h * s);
		useCustomBufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		setInput(new Input(this, Input.KEYS, Input.CONTROLLER));
		setScreen(new SetupScreen());
		skipInit();
		setDestieredAmtOfTicks(60);
		showFPS();
	}
	
	public static void main(String[] args){
		new LD35().start();
	}
}
