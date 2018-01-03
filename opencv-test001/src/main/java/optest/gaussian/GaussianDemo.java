package optest.gaussian;

import java.util.Random;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import optest.util.ImageUI;
/**
 * 高斯模糊
 * */
public class GaussianDemo {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		//加载图像
		Mat src = Imgcodecs.imread("D:\\credit\\opencv-test\\test-01\\lena.png");
		ImageUI input = new ImageUI();
		input.imShow("input image",src);
		
		int height = src.rows();
		int width = src.cols();
		int dims = src.channels();
		byte[] data = new byte[width*height*dims];
		src.get(0, 0, data);
		
		int r=0,g=0,b=0;
		Random random = new Random();
		for(int row=0;row<height;row++){
			for(int col=0;col<width;col++){
				double rf = random.nextGaussian()*30;
				double gf = random.nextGaussian()*30;
				double bf = random.nextGaussian()*30;
				
				b = data[row*width*dims+col*dims]&0xff;
				g = data[row*width*dims+col*dims+1]&0xff;
				r = data[row*width*dims+col*dims+2]&0xff;
				
				b = clamp(b+bf);
				g = clamp(g+gf);
				r = clamp(r+rf);
				
				data[row*width*dims+col*dims] = (byte)b;
				data[row*width*dims+col*dims+1] = (byte)g;
				data[row*width*dims+col*dims+2] = (byte)r;
			}
		}
		src.put(0, 0, data);
		ImageUI gaussan = new ImageUI();
		gaussan.imShow("gaussan image",src);
	}

	private static int clamp(double d) {
		// TODO Auto-generated method stub
		if(d>255)
			return 255;
		else if(d<0)
			return 0;
		else
			return (int)d;
	}
}
