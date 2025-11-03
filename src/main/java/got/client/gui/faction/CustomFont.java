// 
// Decompiled by Procyon v0.5.36
// 

package got.client.gui.faction;

public class CustomFont {
	public static CustomFont hud = new CustomFont("f", 17);
	public static CustomFont toolhud = new CustomFont("hud", 23);

	public static CustomFont hudbold = new CustomFont("faction", 27);
	public static CustomFont hudboldtool = new CustomFont("fractionfont", 23);

	public static CustomFont hudbold65 = new CustomFont("hudbold", 65);
	public static CustomFont main = new CustomFont("main", 60);
	public static CustomFont toolmain = new CustomFont("main", 17);

	public static CustomFont main3 = new CustomFont("main", 30);
	public static CustomFont mainthin = new CustomFont("mainthin", 55);
	public static CustomFont cool = new CustomFont("cool", 55);
	public static CustomFont main1 = new CustomFont("main", 10);
	public static CustomFont cool5 = new CustomFont("cool", 10);
	public static CustomFont main5 = new CustomFont("main", 15);

	public static CustomFont main2 = new CustomFont("main", 12);
	public static CustomFont main22 = new CustomFont("main", 25);
	public static CustomFont main18 = new CustomFont("main", 21);
	public static CustomFont main16 = new CustomFont("main", 19);
	public static CustomFont main14 = new CustomFont("main", 17);

	public static CustomFont main20 = new CustomFont("main", 23);
	public static CustomFont main24 = new CustomFont("main", 26);
	public static CustomFont main30 = new CustomFont("main", 33);

	public final String font;
	public final int size;

	public CustomFont(final String font, final int size) {
		this.font = font;
		this.size = size;
	}
}
