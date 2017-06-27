import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;

public class DrawMath {
    // Boolean init
	static boolean draw = false;
	static boolean textDraw = false;
	static boolean importingImage = false;
	static boolean selectMode = false;
	static boolean moveSelected = false;
	static boolean autoSaveStep = false;
    // Save Page function init
	static int index = 0;
	static int editIndex = 1;
	static int i[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
	static boolean define[] = { false, false, false, false, false, false, false, false, false };
	static ArrayList<BufferedImage> listOfStep = new ArrayList<BufferedImage>();
    // UI init
	static JFrame mainFrame  = new JFrame("Draw Math by Derek !");
	static JPanel displayPane = new JPanel();
	static JPanel drawPane = new JPanel();
	static BufferedImage imageToImport = null;
	static BufferedImage img = null;
	static Color c;
	static int size = 8;
	static int xSize,ySize;
	static Point selectPt1 = null;
	static Point selectPt2 = null;
	static Point selectPt3 = null;
	static Point absSelectPt1 = null;
	static Point absSelectPt2 = null;
	static Rectangle selectedArea = null;
	static Rectangle absSelectedArea = null;

	public static void main(String[] argv) {
		Toolkit tk = Toolkit.getDefaultToolkit();
		xSize = ((int) tk.getScreenSize().getWidth());
		ySize = ((int) tk.getScreenSize().getHeight());
		displayPane.setSize(xSize, ySize / 2);
		displayPane.setVisible(true);
		displayPane.setBackground(Color.white);
		drawPane.setSize(xSize, ySize / 2);
		drawPane.setBackground(Color.white);
		drawPane.setVisible(true);
		drawPane.setFocusable(true);
		drawPane.addMouseListener(new MouseListener(){

			public void mouseClicked(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
				if (textDraw) {
					drawText(e);
				}
				if (importingImage) {
					drawPane.getGraphics().drawImage(imageToImport,e.getX(),e.getY(),null);
				    importingImage = false;
				}
				if (selectMode){
					if(selectPt1 == null && selectPt2 == null){
						selectPt1 = new Point(e.getX(),e.getY());
						absSelectPt1 = new Point(e.getLocationOnScreen().x,e.getLocationOnScreen().y);
					}else if(selectPt2 == null){
						selectPt2 = new Point(e.getX(),e.getY());
						absSelectPt2 = new Point(e.getLocationOnScreen().x,e.getLocationOnScreen().y);
						selectedArea = new Rectangle(selectPt1.x,selectPt1.y,selectPt2.x-selectPt1.x,selectPt2.y-selectPt1.y);
						absSelectedArea = new Rectangle(absSelectPt1.x,absSelectPt1.y,absSelectPt2.x-absSelectPt1.x,absSelectPt2.y-absSelectPt1.y);
						drawSelectedArea();
						}
					}
				if(moveSelected){
					selectPt3 = new Point(e.getX(),e.getY());
					try {
						drawPane.getGraphics().drawImage(ScreenImage.createImage(absSelectedArea),selectPt3.x,selectPt3.y,null);
					} catch (AWTException e1) {e1.printStackTrace();}
					clearDrawPane(selectPt1.x,selectPt1.y,selectPt2.x,selectPt2.y);
					disposeSelectedArea();

					Graphics g = drawPane.getGraphics();
					g.setColor(Color.WHITE);
					g.drawRect(selectPt3.x,selectPt3.y,selectedArea.width,selectedArea.height);

					exitSelectMode();
				}
			}

			public void mouseReleased(MouseEvent e){
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

		});
		drawPane.addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e){
				e.consume();
				Graphics g = drawPane.getGraphics();
				g.setColor(c);
				g.fillOval(e.getPoint().x, e.getPoint().y, size, size);
			}

			public void mouseMoved(MouseEvent e) {
				e.consume();
				Graphics g = drawPane.getGraphics();
				if (draw) {
					g.setColor(c);
					g.fillOval(e.getPoint().x, e.getPoint().y, size, size);
				}
			}
		});
		drawPane.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_ENTER:
					if (!e.isShiftDown()) {
						try {
							listOfStep.add(ScreenImage.createImage(drawPane
									.findComponentAt(0, 0)));
						} catch (AWTException e1) {
						}
						displayPane.getGraphics().drawImage(
								(Image) listOfStep.get(index), 0, 0, null);
						if(autoSaveStep && index != 0){
							int a = 1;
							while(define[a]){
								a++;
							}
							if( a <= 7){
								define[a] = true;
								i[a] = index-1;
								define[a+1] = true;
								i[a+1] = index;
							}
						}
						autoSaveStep = false;
						drawPane.updateUI();
						if (index == 0) {
							define[0] = true;
							i[0] = 0;
						}
						index++;
					} else {
						saveAllStepIntoJpg();
						exit();
					}
					break;
				case KeyEvent.VK_SPACE:
					draw = true;
					c = Color.BLACK;
					break;
				case KeyEvent.VK_R:
					draw = true;
					c = Color.RED;
					break;
				case KeyEvent.VK_B:
					draw = true;
					c = Color.BLUE;
					break;
				case KeyEvent.VK_E:
					draw = true;
					size = 20;
					c = Color.WHITE;
					if(selectMode){
						if(selectPt1 != null && selectPt2 != null){
							clearDrawPane(selectPt1.x,selectPt1.y,selectPt2.x,selectPt2.y);
							disposeSelectedArea();
						}
						exitSelectMode();
					}
					break;
				case KeyEvent.VK_T:
					textDraw();
					break;
				case KeyEvent.VK_I:
					if (!e.isShiftDown()){
						importImage();
					}else{
						importImageInLocation();
					}
					break;
				case KeyEvent.VK_S:
						selectMode = true;
					break;
				case KeyEvent.VK_M:
						moveSelected = true;
					break;
				case KeyEvent.VK_1:
					if (define[0]) {
						displaySavedStep(0);
					} else if (e.isShiftDown()) {
						saveStep(0);
					}
					break;
				case KeyEvent.VK_2:
					if (define[1]) {
						displaySavedStep(1);
					} else if (e.isShiftDown()) {
						saveStep(1);
					}
					break;
				case KeyEvent.VK_3:
					if (define[2]) {
						displaySavedStep(2);
					} else if (e.isShiftDown()) {
						saveStep(2);
					}
					break;
				case KeyEvent.VK_4:
					if (define[3]) {
						displaySavedStep(3);
					} else if (e.isShiftDown()) {
						saveStep(3);
					}
					break;
				case KeyEvent.VK_5:
					if (define[4]) {
						displaySavedStep(4);
					} else if (e.isShiftDown()) {
						saveStep(4);
					}
					break;
				case KeyEvent.VK_6:
					if (define[5]) {
						displaySavedStep(5);
					} else if (e.isShiftDown()) {
						saveStep(5);
					}
					break;
				case KeyEvent.VK_7:
					if (define[6]) {
						displaySavedStep(6);
					} else if (e.isShiftDown()) {
						saveStep(6);
					}
					break;
				case KeyEvent.VK_8:
					if (define[7]) {
						displaySavedStep(7);
					} else if (e.isShiftDown()) {
						saveStep(7);
					}
					break;
				case KeyEvent.VK_9:
					if (define[8]) {
						displaySavedStep(8);
					} else if (e.isShiftDown()) {
						saveStep(8);
					}
					break;
				case KeyEvent.VK_UP:
					startEditStep(index-editIndex);
					editIndex++;
					break;
				case KeyEvent.VK_DOWN:
					endEdit(index-(editIndex-1));
					break;
				case KeyEvent.VK_X:
					if(editIndex!=1)
						endEdit();
					if(selectMode){
						disposeSelectedArea();
						exitSelectMode();
					}
					break;
				}
			}

			public void keyReleased(KeyEvent e) {
				draw = false;
				textDraw = false;
				selectMode = false;
				moveSelected = false;
				switch (e.getKeyCode()) {
				case KeyEvent.VK_1:
				case KeyEvent.VK_2:
				case KeyEvent.VK_3:
				case KeyEvent.VK_4:
				case KeyEvent.VK_5:
				case KeyEvent.VK_6:
				case KeyEvent.VK_7:
				case KeyEvent.VK_8:
				case KeyEvent.VK_9:
					displayPane.getGraphics().drawImage(
							(Image) listOfStep.get(index-1), 0, 0, null);
					break;
				case KeyEvent.VK_SPACE:
					draw = false;
					break;
				case KeyEvent.VK_E:
					size = 8;
					c = Color.BLACK;
					break;
				}
			}
		});
		mainFrame.setBounds(0, 0, xSize, ySize);
		mainFrame.setLayout(new GridLayout(2, 1));
		mainFrame.add(displayPane);
		mainFrame.add(drawPane);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
	}

	private static void drawSelectedArea() {
		Graphics g = drawPane.getGraphics();
		g.setColor(Color.YELLOW);
		g.drawRect(selectedArea.x,selectedArea.y,selectedArea.width,selectedArea.height);
	}

	private static void disposeSelectedArea(){
		Graphics g = drawPane.getGraphics();
		g.setColor(Color.WHITE);
		g.drawRect(selectedArea.x,selectedArea.y,selectedArea.width,selectedArea.height);
	}

	private static void exitSelectMode(){
		selectMode = false;
		disposeSelectedArea();
		selectedArea = null;
		selectPt1 = null;
		selectPt2 = null;
	}

	private static void exit(){
		mainFrame.dispose();
	}

	private static void textDraw() {
		textDraw = true;
	}

	private static void drawText(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		Graphics g = drawPane.getGraphics();
		String drawText;
		drawText = JOptionPane.showInputDialog("Type your string");
		g.setFont(new Font("default", Font.BOLD, 30));
		try {
			g.drawString(drawText, x, y);
		} catch (NullPointerException npe) {
		}
		if(stringOfnewQuestion(drawText)){
			autoSaveStep = true;
		}
	}

	private static void clearDrawPane(int x1,int y1,int x2,int y2){
		Graphics g = drawPane.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(x1,y1,x2-x1,y2-y1);
	}

	private static void importImage(){
		importingImage = true;
		JFileChooser fc = new JFileChooser();
	    fc.setDialogTitle ("Choose Image..");
	    int returnVal = fc.showDialog(mainFrame,"Choose..");
	    File file = null;

	    if (returnVal == JFileChooser.APPROVE_OPTION)
	        file = fc.getSelectedFile();
	    try
	    {
	    		imageToImport = ImageIO.read(file);
	    } catch (IOException e){}
	    drawPane.getGraphics().drawImage(imageToImport,0,0,null);
	    importingImage = false;
	}

	private static void importImageInLocation(){
		importingImage = true;
		JFileChooser fc = new JFileChooser();
	    fc.setDialogTitle ("Choose Image..");
	    int returnVal = fc.showDialog(mainFrame,"Choose..");
	    File file = null;

	    if (returnVal == JFileChooser.APPROVE_OPTION)
	        file = fc.getSelectedFile();
	    try
	    {
	    		System.out.println(file);
	    		imageToImport = ImageIO.read(file);
	    } catch (IOException e){}
	}

	private static void startEditStep(int index){
		drawPane.getGraphics().drawImage((Image)listOfStep.get(i[index]),0,0,null);
	}

	private static void endEdit(int i){
		try{
			listOfStep.set(i,(ScreenImage.createImage(drawPane.findComponentAt(0, 0))));
			drawPaneClear();
			displayPane.getGraphics().drawImage((Image)listOfStep.get(index-1),0,0, null);
			editIndex = 1;
		}catch(Exception e){}
	}

	private static void endEdit(){
		displayPane.getGraphics().drawImage((Image)listOfStep.get(index-1),0,0,null);
		drawPaneClear();
		editIndex = 1;
	}

	private static void drawPaneClear(){
		Graphics g = drawPane.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0,0,xSize,ySize/2);
	}
	private static void displaySavedStep(int index) {
		displayPane.getGraphics().drawImage((Image) listOfStep.get(i[index]),
				0, 0, null);
	}

	private static void saveStep(int index) {
		i[index] = DrawMath.index - 1;
		define[index] = true;
	}

	private static boolean stringOfnewQuestion(String s){
		if(s.contains(")")){
			int i = s.indexOf(")");
			s = s.substring(0,i);
			if(s.contains("(")){
				return false;
			}else{
				return true;
			}
		}return false;
	}

	private static void saveAllStepIntoJpg() {
		int i = 2;
		try {
			boolean exit = false;
			String fileName = null;
			while (!exit) {
				fileName = JOptionPane
						.showInputDialog("Please enter the name of the final image =)");
				if (fileName.equals("")) {
					fileName = "finalImage";
				}
				File f = new File(fileName + ".jpg");
				if (!f.exists()) {
					exit = true;
				} else {
					File file;
					do {
						file = new File(fileName + "(" + i + ").jpg");
						i++;
					} while (file.exists());
					fileName += ("(" + (i - 1) + ")");
					exit = true;
				}
			}
			ImageIO.write(createFinalImage(), "jpeg", new File(fileName
					+ ".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static BufferedImage createFinalImage() {
		int rows = listOfStep.size();
		int cols = 1;
		int chunkWidth, chunkHeight;
		int type;
		type = listOfStep.get(0).getType();
		chunkWidth = listOfStep.get(0).getWidth();
		chunkHeight = listOfStep.get(0).getHeight();
		BufferedImage finalImg = new BufferedImage(chunkWidth * cols,
				chunkHeight * rows, type);
		int num = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				finalImg.createGraphics().drawImage(listOfStep.get(num),
						chunkWidth * j, chunkHeight * i, null);
				num++;
			}
		}
		return finalImg;
	}
}
