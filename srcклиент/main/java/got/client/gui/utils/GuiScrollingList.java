package got.client.gui.utils;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class GuiScrollingList<T> {

    public static final float DEFAULT_SCROLL_SPEED = 10.0F;

    protected final Minecraft mc;

    protected float x;
    protected float y;
    protected float width;
    protected float height;
    public float entryHeight;

    protected float scrollOffset;
    protected float scrollSpeed;

    protected int hoverIndex;
    protected int selected;

    protected float mouseX, mouseY;

    protected int hoverColor, selectedColor, textColor, selectedTextColor, hoverTextColor, sliderColor, sliderBackgroundColor;
    protected float sliderOffset, sliderWidth;
    protected boolean drawHoverColor, drawSelectedColor, drawSliderBackground, canLoseFocus, canSelect;

    private boolean dragging;
    private float dragged, mouseYOffset;
  
    protected CopyOnWriteArrayList<T> elementData;

    public GuiScrollingList(float listEntryHeight){
        this(0, 0, 100, 50, listEntryHeight);
    }

    public GuiScrollingList(float posX, float posY, float listWidth, float listHeight, float listEntryHeight){
        mc = Minecraft.getMinecraft();
        width = listWidth;
        height = listHeight;
        x = posX;
        y = posY;
        entryHeight = listEntryHeight;
        scrollSpeed = DEFAULT_SCROLL_SPEED;
        hoverIndex = -1;
        selected = -1;
        hoverColor = 839518730;
        selectedColor = -1778384896;
        sliderColor = new Color(0, 0, 0, 125).getRGB();
        sliderBackgroundColor = new Color(14, 18, 30).getRGB();
        textColor = selectedTextColor = hoverTextColor = -1;
        sliderOffset = 1;
        sliderWidth = 5;
        drawHoverColor = true;
        drawSelectedColor = true;
        canLoseFocus = true;
        canSelect = true;
        drawSliderBackground = true;
        elementData = new CopyOnWriteArrayList<T>();
    }

    public void onEntryClicked(T entry, float index, int mouseX, int mouseY, int button){}

    public void drawEntry(T entry, int index, float x2, float currentY, boolean hovered){
     }

    public void setPos(float x, float y, float w, float h) {
    	this.x = x;
    	this.y = y;
    	this.width = w;
    	this.height = height;
    }
    
    public void drawEntryForeground(T entry, int index, float x2, float currentY, boolean hovered){}

    public int getSize(){
        return elementData.size();
    }

    public float getContentSize(){
        return getSize() * entryHeight;
    }

    public boolean isSelected(int index){
        return index == selected;
    }

    public boolean isSelected(){
        return selected >= 0;
    }

    public void mouseClicked(int mouseX, int mouseY, int button){
        if(hoverIndex != -1){
            if(canSelect){
                selected = hoverIndex;
            }
            T entry = getElement(hoverIndex);
            if(entry != null){
                onEntryClicked(entry, hoverIndex, mouseX, mouseY, button); 
            }
        }else{
            if(canLoseFocus){
                selected = -1;
            }
        }
      
        float start = getContentSize() - height;
        if(start > 0){
        	float scrollBarXStart = x + width + sliderOffset;
        	float scrollBarXEnd = scrollBarXStart + sliderWidth;
            float length = 100;
  
            float end = scrollOffset * (height - length) / start + y;
  
            if(end < y){
                end = y;
            }
          
            if(mouseX > scrollBarXStart && mouseY >= end && mouseX < scrollBarXEnd && mouseY < end + length){
                dragging = true;
                mouseYOffset = mouseY;
            }
        }
    }
  
    public boolean isHover() {
		return mouseX >= x && mouseX < x + width+25 && mouseY >= y && mouseY < y + height+10;
	}
    
    public void mouseClickMove(int mouseX, int mouseY, int button){
    	if(!isHover()) {
    		return;
    	}
        if(dragging){
            scrollOffset += (mouseY - mouseYOffset) * (getContentSize() / height);
            if(scrollOffset > getContentSize() - height){
                scrollOffset = getContentSize() - height;
            }
            if(scrollOffset < 0){
                scrollOffset = 0;
            }
            mouseYOffset = mouseY;
        }
    }
  
    public void mouseReleased(int mouseX, int mouseY, int button){
        dragging = false;
    }

    public void handleMouseInput(int delta){
        if(isMouseOver()){
            if(delta != 0){
                if(delta > 0){
                    delta = -1;
                }else if(delta < 0){
                    delta = 1;
                }
                float maxScrollOffset = Math.max(0, getSize() * entryHeight - height);
                scrollOffset = (int)Math.max(Math.min(scrollOffset + (delta * scrollSpeed), maxScrollOffset), 0);
            }
        }
       
    }

    public void updateScreen(){
        if(isMouseOver()){
            hoverIndex = (int) ((mouseY - y + scrollOffset) / entryHeight);
            if(hoverIndex >= getSize() || hoverIndex < 0){
                hoverIndex = -1;
            }
        }else{
            hoverIndex = -1;
        }
    }

    public void drawScreen(int mX, int mY, float ticks){
        mouseX = mX;
        mouseY = mY;

        float currentY = y - scrollOffset;
        for(int l = 0; l < getSize(); l++){
            if(currentY >= y - entryHeight && currentY <= y + height){
                boolean isHover = hoverIndex == l;
                T entry = getElement(l);
                if(entry != null){
                    drawEntry(entry, l, x, currentY, isHover);
                }
            }
            currentY += entryHeight;
        }

        float start = getContentSize() - height;
    
        if(start > 0){
        	
        	float length = height * height / getContentSize();

            if(length < 3){
                length = 3;
            }

            if(length > height - 3){
                length = height - 3;
            }

        	
            float end = scrollOffset * (height - length) / start + y;

            if(end < y){
                end = y;
            }

            float scrollBarXStart = x + width + sliderOffset;
            float scrollBarXEnd = scrollBarXStart + sliderWidth;

            if(drawSliderBackground){
                Gui.drawRect((int) scrollBarXStart,(int) y, (int)scrollBarXEnd, (int)y + (int)height, new Color(255, 255, 255, 80).getRGB()); // Background
            }
            Gui.drawRect((int)scrollBarXStart, (int)end +(int) length, (int)scrollBarXEnd, (int)end, new Color(180, 180, 180, 225).getRGB());
        }

        currentY = y - scrollOffset;
        for(int l = 0; l < getSize(); l++){
            if(currentY >= y - entryHeight && currentY <= y + height){
                boolean isHover = hoverIndex == l;
                T entry = getElement(l);
                if(entry != null){
                    drawEntryForeground(entry, l, x, currentY, isHover);
                }
            }
            currentY += entryHeight;
        }
    }
    
	public static void drawRect(double left, double top, double right, double bottom, int color) {
		right = left + right;
		bottom = top + bottom;

		float f3 = (color >> 24 & 0xFF) / 255.0f;
		float f4 = (color >> 16 & 0xFF) / 255.0f;
		float f5 = (color >> 8 & 0xFF) / 255.0f;
		float f6 = (color & 0xFF) / 255.0f;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		GL11.glPushMatrix();
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glColor4f(f4, f5, f6, f3);
		tessellator.addVertex(left, bottom, 0.0);
		tessellator.addVertex(right, bottom, 0.0);
		tessellator.addVertex(right, top, 0.0);
		tessellator.addVertex(left, top, 0.0);
		tessellator.draw();
		GL11.glEnable(3553);
		GL11.glPopMatrix();
	}

    public void cleanUp(){
        hoverIndex = -1;
        selected = -1;
        scrollOffset = 0;
        elementData.clear();
    }

    public boolean isMouseOver(){
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    public List<T> getElements(){
        return elementData;
    }

    public void clear(){
        elementData.clear();
    }

    public T removeFirstElement(){
        return elementData.size() > 0 ? elementData.remove(0) : null;
    }

    public T removeLastElement(){
        return elementData.size() > 0 ? elementData.remove(elementData.size() - 1) : null;
    }

    public T removeSelectedElement(){
        return isSelected() ? elementData.remove(getSelectedIndex()) : null;
    }

    public boolean removeElement(T element){
        return elementData.remove(element);
    }

    public T removeElement(int index){
        return elementData.remove(index);
    }

   

    public boolean addElement(T element){
        return elementData.add(element);
    }

    public boolean addElements(T... elements){
        return elementData.addAll(Arrays.asList(elements));
    }

    public boolean addElements(Collection<? extends T> elements){
        return elementData.addAll(elements);
    }

    public T getElement(int index){
        return index >= 0 && index < elementData.size() ? elementData.get(index) : null;
    }

   
    public T getSelectedElement(){
        return getElement((int) selected);
    }

    public float getScrollSpeed(){
        return scrollSpeed;
    }

    @SuppressWarnings("rawtypes")
	public GuiScrollingList setScrollSpeed(float speed){
        scrollSpeed = speed;
        return this;
    }

    public int getHoverColor(){
        return hoverColor;
    }

    public GuiScrollingList setHoverColor(int color){
        hoverColor = color;
        return this;
    }

    public int getSelectedColor(){
        return selectedColor;
    }

    public GuiScrollingList setSelectedColor(int color){
        selectedColor = color;
        return this;
    }

    public int getSliderColor(){
        return sliderColor;
    }

    public GuiScrollingList setSliderColor(int color){
        sliderColor = color;
        return this;
    }

    public boolean isDrawHoverColor(){
        return drawHoverColor;
    }

    public GuiScrollingList setDrawHoverColor(boolean draw){
        drawHoverColor = draw;
        return this;
    }

    public boolean isDrawSelectedColor(){
        return drawSelectedColor;
    }

    public GuiScrollingList setDrawSelectedColor(boolean draw){
        drawSelectedColor = draw;
        return this;
    }

    public boolean isDrawSliderBackground(){
        return drawSliderBackground;
    }

    public GuiScrollingList setDrawSliderBackground(boolean draw){
        drawSliderBackground = draw;
        return this;
    }

    public int getTextColor(){
        return textColor;
    }

    public GuiScrollingList setTextColor(int color){
        textColor = color;
        return this;
    }

    public int getHoverTextColor(){
        return hoverTextColor;
    }

    public GuiScrollingList setHoverTextColor(int color){
        hoverTextColor = color;
        return this;
    }

    public int getSelectedTextColor(){
        return selectedTextColor;
    }

    public GuiScrollingList setSelectedTextColor(int color){
        selectedTextColor = color;
        return this;
    }

    public int getSliderBackgroundColor(){
        return sliderBackgroundColor;
    }

    public GuiScrollingList setSliderBackgroundColor(int color){
        sliderBackgroundColor = color;
        return this;
    }

    public float getSliderOffset(){
        return sliderOffset;
    }

    public GuiScrollingList setSliderOffset(int offset){
        sliderOffset = offset;
        return this;
    }

    public float getSliderWidth(){
        return sliderWidth;
    }

    public GuiScrollingList setSliderWidth(int width){
        sliderWidth = width;
        return this;
    }

    public float getScrollOffset(){
        return scrollOffset;
    }

    public void setScrollOffset(int offset){
        scrollOffset = offset;
    }

    public int getSelectedIndex(){
        return (int) selected;
    }

    public void setSelectedIndex(int index){
        selected = index;
    }

    public boolean canLoseFocus(){
        return canLoseFocus;
    }

    public GuiScrollingList setCanLoseFocus(boolean loseFocus){
        canLoseFocus = loseFocus;
        return this;
    }

    public boolean canSelect(){
        return canSelect;
    }

    public GuiScrollingList setCanSelect(boolean select){
        canSelect = select;
        return this;
    }

    public void setPosition(int xPos, int yPos){
        x = xPos;
        y = yPos;
    }

    public void setSize(float f, float g){
        width = f;
        height = g;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getWidth(){
        return width;
    }

    public float getHeight(){
        return height;
    }

    public float getEntryHeight(){
        return entryHeight;
    }

    public int getHoverIndex(){
        return hoverIndex;
    }

    public float getMouseX(){
        return mouseX;
    }

    public float getMouseY(){
        return mouseY;
    }
    
    public void setElementData(CopyOnWriteArrayList<T> elementData) {
		this.elementData = elementData;
	}
    
    public List<T> getList(){
    	return elementData;
    }
}