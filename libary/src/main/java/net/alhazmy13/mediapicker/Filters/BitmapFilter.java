package net.alhazmy13.mediapicker.Filters;

import android.R.style;
import android.graphics.Bitmap;

import net.alhazmy13.mediapicker.StaticAttributes;

public class BitmapFilter implements Filter {
	/**
	 * filter style id;
	 */


	public static final int TOTAL_FILTER_NUM = GOTHAM_STYLE;
	
	/**
	 * change bitmap filter style
	 * @param originBitmap
	 * @param styleNo, filter sytle id
	 */



	public static Bitmap applyStyle(Bitmap originBitmap,int styleNo) {
		Bitmap changeBitmap;
		switch (styleNo) {
			case BitmapFilter.AVERAGE_BLUR_STYLE:
				changeBitmap = changeStyle(originBitmap, BitmapFilter.AVERAGE_BLUR_STYLE, 5); // maskSize, must odd
				break;
			case BitmapFilter.GAUSSIAN_BLUR_STYLE:
				changeBitmap = changeStyle(originBitmap, BitmapFilter.GAUSSIAN_BLUR_STYLE, 1.2); // sigma
				break;
			case BitmapFilter.SOFT_GLOW_STYLE:
				changeBitmap =  changeStyle(originBitmap, BitmapFilter.SOFT_GLOW_STYLE, 0.6);
				break;
			case BitmapFilter.LIGHT_STYLE:
				int width = originBitmap.getWidth();
				int height = originBitmap.getHeight();
				changeBitmap =  changeStyle(originBitmap, BitmapFilter.LIGHT_STYLE, width / 3, height / 2, width / 2);
				break;
			case BitmapFilter.LOMO_STYLE:
				changeBitmap =  changeStyle(originBitmap, BitmapFilter.LOMO_STYLE,
						(originBitmap.getWidth() / 2.0) * 95 / 100.0);
				break;
			case BitmapFilter.NEON_STYLE:
				changeBitmap =  changeStyle(originBitmap, BitmapFilter.NEON_STYLE, 200, 100, 50);
				break;
			case BitmapFilter.PIXELATE_STYLE:
				changeBitmap =  changeStyle(originBitmap, BitmapFilter.PIXELATE_STYLE, 10);
				break;
			case BitmapFilter.MOTION_BLUR_STYLE:
				changeBitmap =  changeStyle(originBitmap, BitmapFilter.MOTION_BLUR_STYLE, 10, 1);
				break;
			case BitmapFilter.OIL_STYLE:
				changeBitmap =  changeStyle(originBitmap, BitmapFilter.OIL_STYLE, 5);
				break;
			default:
				changeBitmap =  changeStyle(originBitmap, styleNo);
				break;
		}
		return changeBitmap;
	}

	private static Bitmap changeStyle(Bitmap bitmap, int styleNo, Object... options) {
		if (styleNo == GRAY_STYLE) {
			return GrayFilter.changeToGray(bitmap);
		}
		else if (styleNo == RELIEF_STYLE) {
			return ReliefFilter.changeToRelief(bitmap);
		}
		else if (styleNo == AVERAGE_BLUR_STYLE) {
			if (options.length < 1) {
				return BlurFilter.changeToAverageBlur(bitmap, 5);
			}
			return BlurFilter.changeToAverageBlur(bitmap, (Integer)options[0]); // maskSize
		}
		else if (styleNo == OIL_STYLE) {
			if (options.length < 1) {
				return OilFilter.changeToOil(bitmap, 5);
			}
			return OilFilter.changeToOil(bitmap, (Integer)options[0]);
		}
		else if (styleNo == NEON_STYLE) {
			if (options.length < 3) {
				return NeonFilter.changeToNeon(bitmap, 200, 50, 100);
			}
			return NeonFilter.changeToNeon(bitmap, (Integer)options[0], (Integer)options[1], (Integer)options[2]);
		}
		else if (styleNo == PIXELATE_STYLE) {
			if (options.length < 1) {
				return PixelateFilter.changeToPixelate(bitmap, 10);
			}
			return PixelateFilter.changeToPixelate(bitmap, (Integer)options[0]);
		}			
		else if (styleNo == TV_STYLE) {
			return TvFilter.changeToTV(bitmap);
		}
		else if (styleNo == INVERT_STYLE) {
			return InvertFilter.chageToInvert(bitmap);
		}
		else if (styleNo == BLOCK_STYLE) {
			return BlockFilter.changeToBrick(bitmap);
		}
		else if (styleNo == OLD_STYLE) {
			return OldFilter.changeToOld(bitmap);
		}
		else if (styleNo == SHARPEN_STYLE) {
			return SharpenFilter.changeToSharpen(bitmap);
		}
		else if (styleNo == LIGHT_STYLE) {
			if (options.length < 3) {
				int width = bitmap.getWidth();
				int height = bitmap.getHeight();
				return LightFilter.changeToLight(bitmap, width / 2, height / 2, Math.min(width / 2, height / 2));
			}
			return LightFilter.changeToLight(bitmap, (Integer)options[0], 
					(Integer)options[1], (Integer)options[2]); // centerX, centerY, radius
		}
		else if (styleNo == LOMO_STYLE) {
			if (options.length < 1) {
				double radius = (bitmap.getWidth() / 2) * 95 / 100;
				return LomoFilter.changeToLomo(bitmap, radius);
			}
			return LomoFilter.changeToLomo(bitmap, (Double)options[0]);
		}
		else if (styleNo == HDR_STYLE) {
			return HDRFilter.changeToHDR(bitmap);
		}
		else if (styleNo == GAUSSIAN_BLUR_STYLE) {
			if (options.length < 1) {
				return GaussianBlurFilter.changeToGaussianBlur(bitmap, 1.2);
			}
			return GaussianBlurFilter.changeToGaussianBlur(bitmap, (Double)options[0]); // sigma
		}
		else if (styleNo == SOFT_GLOW_STYLE) {
			if (options.length < 1) {
				return SoftGlowFilter.softGlowFilter(bitmap, 0.6);
			}
			return SoftGlowFilter.softGlowFilter(bitmap, (Double)options[0]);
		}
		else if (styleNo == SKETCH_STYLE) {
			return SketchFilter.changeToSketch(bitmap);
		} 
		else if (styleNo == MOTION_BLUR_STYLE) {
			if (options.length < 2) {
				return MotionBlurFilter.changeToMotionBlur(bitmap, 5, 1);
			}
			return MotionBlurFilter.changeToMotionBlur(bitmap, (Integer)options[0], (Integer)options[1]);
		}
		else if (styleNo == GOTHAM_STYLE) {
			return GothamFilter.changeToGotham(bitmap);
		}
		return bitmap;
	}

}