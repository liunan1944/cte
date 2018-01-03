package optest.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JDialog;

import org.opencv.core.Mat;

public class ImageUI extends JComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	public ImageUI(){
		this.image=null;
	}
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		if(image == null){
			g2d.setPaint(Color.BLACK);
			g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		}else{
			g2d.drawImage(image, 0, 0, 
					image.getWidth(),image.getHeight(),null);
		}
	}
	public void imShow(String title,Mat src){
		this.image = convert2BufferedImage(src);
		JDialog ui = new JDialog();
		ui.setTitle(title);
		ui.getContentPane().setLayout(new BorderLayout());
		ui.getContentPane().add(this, BorderLayout.CENTER);
		ui.setSize(image.getWidth()+1, image.getHeight()+2);
		ui.setVisible(true);
		this.repaint();
	}
	private BufferedImage convert2BufferedImage(Mat src) {
		int width = src.cols();
		int height = src.rows();
		int dims = src.channels();
		int[] pixels = new int[width*height];
		byte[] rgbdata = new byte[width*height*dims];
		src.get(0, 0, rgbdata);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int index = 0;
		int r=0, g=0, b=0;
		for(int row=0; row<height; row++) {
			for(int col=0; col<width; col++) {
				if(dims == 3) {
					index = row*width*dims + col*dims;
					b = rgbdata[index]&0xff;
					g = rgbdata[index+1]&0xff;
					r = rgbdata[index+2]&0xff;
					pixels[row*width+col] = ((255&0xff)<<24) | ((r&0xff)<<16) | ((g&0xff)<<8) | b&0xff;	
				}
				if(dims == 1) {
					index = row*width + col;
					b = rgbdata[index]&0xff;
					pixels[row*width+col] = ((255&0xff)<<24) | ((b&0xff)<<16) | ((b&0xff)<<8) | b&0xff;	
				}
			}
		}
		setRGB( image, 0, 0, width, height, pixels);
		return image;
	}
	public void setRGB( BufferedImage image, int x, int y, int width, int height, int[] pixels ) {
		int type = image.getType();
		if ( type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB )
			image.getRaster().setDataElements( x, y, width, height, pixels );
		else
			image.setRGB( x, y, width, height, pixels, 0, width );
    }
}
