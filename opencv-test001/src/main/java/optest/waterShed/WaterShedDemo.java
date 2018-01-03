package optest.waterShed;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import optest.util.ImageUI;
/**
 * 分水岭算法
 * 内部梯度
 * 外部梯度
 * */
public class WaterShedDemo {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		//1、加载图像
		Mat src = Imgcodecs.imread("D:\\credit\\opencv-test\\test-01\\cards.png");
		ImageUI input = new ImageUI();
		input.imShow("input image",src);
		
		BackColorCV(src);
		DistanceCV(src);
	}
	//背景图像转换为黑色
	public static void BackColorCV(Mat src){
		int width = src.cols();
		int height = src.rows();
		int channel = src.channels();
		
		byte[] data = new byte[channel*width*height];
		src.get(0, 0, data);
		int r=0,g=0,b=0;
		int index = 0;
		for(int row=0;row<height;row++){
			for(int col=0;col<width;col++){
				index = row*channel*width+col*channel;
				b = data[index]&0xff;
				g = data[index+1]&0xff;
				r = data[index+2]&0xff;
				if(b == 255 && g == 255 && r == 255){
					data[index] = (byte)0;
					data[index+1] = (byte)0;
					data[index+2] = (byte)0;
				}
			}
		}
		src.put(0, 0, data);
		ImageUI back = new ImageUI();
		back.imShow("back image",src);
	}
	//背景图像转换为黑色
	public static Mat DistanceCV(Mat src){
		Mat gray = new Mat();
		Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
		
		Mat binary = new Mat();
		Imgproc.threshold(gray, binary, 0, 255, 
				Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
		
		Mat dist = new Mat();
		Imgproc.distanceTransform(binary, dist, Imgproc.DIST_L2, 3);
		Core.normalize(dist, dist,0,255,Core.NORM_MINMAX);//将浮点像素转换为整数
		Mat dist_8u = new Mat();
		dist.convertTo(dist_8u, CvType.CV_8U);
		ImageUI binaryShow = new ImageUI();
		binaryShow.imShow("距离变换",dist_8u);
		return dist_8u;
	}
}
