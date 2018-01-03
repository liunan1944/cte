package optest.mat;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
/**
 * 读取每个像素点
 * */
public class MatLearn03 {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		//加载图像
		Mat src = Imgcodecs.imread("D:\\credit\\opencv-test\\test-01\\lena.png");
		getAllPixel(src);
	}
	//循环获取像素点
	public static void getPixel(Mat src){
		int width = src.cols();
		int height = src.rows();
		int channel = src.channels();
		
		byte[] onepixel = new byte[channel];
		int r=0,g=0,b=0;
		int gray = 0;
		for(int row=0;row<height;row++){
			for(int col=0;col<width;col++){
				src.get(row, col, onepixel);
				if(channel == 3){
					b = onepixel[0]&0xff;
					g = onepixel[1]&0xff;
					r = onepixel[2]&0xff;
					//修改像素点
					b = 255 - b;
					g = 255 - g;
					r = 255 - r;
					onepixel[0] = (byte)b;
					onepixel[1] = (byte)g;
					onepixel[2] = (byte)r;
				}else{//灰度图像
					gray = onepixel[0]&0xff;
					
					gray = 255 - gray;
					onepixel[0] = (byte)gray;
				}
				src.put(row, col, onepixel);
			}
		}
		
		Imgcodecs.imwrite("D:\\credit\\opencv-test\\test-01\\lena_result.png", 
				src);
		src.release();
	}
	//一次性获取像素点
	public static void getAllPixel(Mat src){
		int width = src.cols();
		int height = src.rows();
		int channel = src.channels();
		
		byte[] data = new byte[channel*width*height];
		src.get(0, 0, data);
		int r=0,g=0,b=0;
		int gray = 0;
		for(int row=0;row<height;row++){
			for(int col=0;col<width;col++){
				if(channel == 3){
					b = data[row*channel*width+col*channel]&0xff;
					g = data[row*channel*width+col*channel+1]&0xff;
					r = data[row*channel*width+col*channel+2]&0xff;
					//修改像素点
					b = 255 - b;
					g = 255 - g;
					r = 255 - r;
					data[row*channel*width+col*channel] = (byte)b;
					data[row*channel*width+col*channel+1] = (byte)g;
					data[row*channel*width+col*channel+2] = (byte)r;
				}else{//灰度图像
					gray = data[row*channel*width+col*channel]&0xff;
					
					gray = 255 - gray;
					data[row*channel*width+col*channel] = (byte)gray;
				}
			}
		}
		src.put(0, 0, data);
		Imgcodecs.imwrite("D:\\credit\\opencv-test\\test-01\\lena_result01.png", 
				src);
		src.release();
	}
}
