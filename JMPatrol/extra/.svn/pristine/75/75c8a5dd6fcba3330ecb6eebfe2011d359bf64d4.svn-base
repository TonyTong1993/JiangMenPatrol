package com.ecity.chart.utils;

import android.content.res.Resources;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that holds predefined color integer arrays (e.g.
 * ColorTemplate.VORDIPLOM_COLORS) and convenience methods for loading colors
 * from resources.
 * 
 * @author Philipp Jahoda
 */
public class ColorTemplate {

	/**
	 * an "invalid" color that indicates that no color is set
	 */
	public static final int COLOR_NONE = -1;

	/**
	 * this "color" is used for the Legend creation and indicates that the next
	 * form should be skipped
	 */
	public static final int COLOR_SKIP = -2;

	/**
	 * THE COLOR THEMES ARE PREDEFINED (predefined color integer arrays), FEEL
	 * FREE TO CREATE YOUR OWN WITH AS MANY DIFFERENT COLORS AS YOU WANT
	 */
	public static final int[] LIBERTY_COLORS = { Color.rgb(207, 248, 246),
			Color.rgb(148, 212, 212), Color.rgb(136, 180, 187),
			Color.rgb(118, 174, 175), Color.rgb(42, 109, 130) };
	public static final int[] JOYFUL_COLORS = { Color.rgb(217, 80, 138),
			Color.rgb(254, 149, 7), Color.rgb(254, 247, 120),
			Color.rgb(106, 167, 134), Color.rgb(53, 194, 209) };
	public static final int[] PASTEL_COLORS = { Color.rgb(64, 89, 128),
			Color.rgb(149, 165, 124), Color.rgb(217, 184, 162),
			Color.rgb(191, 134, 134), Color.rgb(179, 48, 80) };
	public static final int[] COLORFUL_COLORS = { Color.rgb(193, 37, 82),
			Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
			Color.rgb(106, 150, 31), Color.rgb(179, 100, 53) };
	public static final int[] VORDIPLOM_COLORS = {
			// Color.rgb(192, 255, 140), Color.rgb(255, 247, 140),
			// Color.rgb(255, 208, 140),
			// Color.rgb(140, 234, 255), Color.rgb(255, 140, 157)
			Color.rgb(234, 219, 113), Color.rgb(237, 109, 0),
			Color.rgb(46, 175, 208), Color.rgb(3, 157, 88),
			Color.rgb(247, 192, 180), Color.rgb(236, 121, 155),
			Color.rgb(84, 47, 95), Color.rgb(201, 97, 124),
			Color.rgb(94, 183, 168), Color.rgb(101, 129, 192),
			
			Color.rgb(188, 195, 48), Color.rgb(219, 79, 46),
			Color.rgb(251, 196, 0), Color.rgb(209, 135, 131),
			Color.rgb(200, 22, 29), Color.rgb(65, 81, 110),
			Color.rgb(190, 170, 99), Color.rgb(222, 113, 98),
			Color.rgb(244, 209, 159), Color.rgb(186, 209, 164),
			
			Color.rgb(153, 202, 96), Color.rgb(255, 234, 0),
			Color.rgb(0, 150, 151), Color.rgb(108, 147, 198),
			Color.rgb(233, 115, 82), Color.rgb(126, 168, 74),
			Color.rgb(144, 173, 152), Color.rgb(152, 47, 72),
			Color.rgb(169, 200, 221), Color.rgb(243, 232, 168),
			
			Color.rgb(0, 172, 167), Color.rgb(207, 220, 40),
			Color.rgb(146, 146, 91), Color.rgb(235, 178, 188),
			Color.rgb(146, 129, 180), Color.rgb(0, 162, 123),
			Color.rgb(100, 126, 181), Color.rgb(231, 147, 162),
			Color.rgb(91, 194, 217), Color.rgb(212, 153, 141),
			
			Color.rgb(176, 220, 213), Color.rgb(240, 234, 186),
			Color.rgb(227, 184, 213), Color.rgb(196, 219, 102),
			Color.rgb(21, 184, 215), Color.rgb(172, 218, 224),
			Color.rgb(198, 168, 186), Color.rgb(162, 207, 125),
			Color.rgb(239, 199, 189), Color.rgb(134, 170, 210),
			
			Color.rgb(214, 208, 148), Color.rgb(75, 123, 143),
			Color.rgb(175, 113, 118), Color.rgb(159, 182, 168),
			Color.rgb(222, 218, 223), Color.rgb(185, 149, 161),
			Color.rgb(218, 203, 175), Color.rgb(204, 183, 184),
			Color.rgb(105, 117, 152), Color.rgb(175,184,162),
			
			Color.rgb(212, 196, 219), Color.rgb(220, 232, 199),
			Color.rgb(171, 182, 159), Color.rgb(235, 213, 182),
			Color.rgb(192, 120, 156), Color.rgb(175, 209, 165),
			Color.rgb(234, 199, 215), Color.rgb(187, 160, 203),
			Color.rgb(208, 214, 161), Color.rgb(224, 177, 130) };

	/**
	 * Returns the Android ICS holo blue light color.
	 * 
	 * @return
	 */
	public static int getHoloBlue() {
		return Color.rgb(51, 181, 229);
	}

	/**
	 * turn an array of resource-colors (contains resource-id integers) into an
	 * array list of actual color integers
	 * 
	 * @param r
	 * @param colors
	 *            an integer array of resource id's of colors
	 * @return
	 */
	public static List<Integer> createColors(Resources r, int[] colors) {

		List<Integer> result = new ArrayList<Integer>();

		for (int i : colors) {
			result.add(r.getColor(i));
		}

		return result;
	}

	/**
	 * Turns an array of colors (integer color values) into an ArrayList of
	 * colors.
	 * 
	 * @param colors
	 * @return
	 */
	public static List<Integer> createColors(int[] colors) {

		List<Integer> result = new ArrayList<Integer>();

		for (int i : colors) {
			result.add(i);
		}

		return result;
	}
}
