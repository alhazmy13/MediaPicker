package net.alhazmy13.mediapicker.Image.Filters;

import android.graphics.Bitmap;

public class NeonFilter {
	
	static {
		System.loadLibrary("AndroidImageFilter");
	}
	

	public static Bitmap changeToNeon(Bitmap bitmap, int r, int g, int b) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		
		
		int[] pixels = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		
		int[] returnPixels = NativeFilterFunc.neonFilter(pixels, width, height, r, g, b);
		Bitmap returnBitmap = Bitmap.createBitmap(returnPixels, width, height, Bitmap.Config.ARGB_8888);
		
		return returnBitmap;
	}
}
