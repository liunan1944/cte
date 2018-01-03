package optest.mat;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;

public class MatLearn02 {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//		Mat src = new Mat();
//		src.create(300, 300, CvType.CV_8UC3);
//		src.setTo(new Scalar(0,0,255));
//		Imgcodecs.imwrite("D:\\credit\\opencv-test\\test-01\\image_01.png",
//				src);
		
//		Mat src1 = Mat.zeros(300,300, CvType.CV_8UC3);
//		Imgcodecs.imwrite("D:\\credit\\opencv-test\\test-01\\image_02.jpg",
//				src1);
		
		//加载图像
		Mat src2 = Imgcodecs.imread("D:\\credit\\opencv-test\\test-01\\image_02.jpg");
		int type = src2.type();
		int width = src2.cols();
		int height = src2.rows();
		int channel = src2.channels();
		
		System.out.println("type:"+type);
		System.out.println("width:"+width);
		System.out.println("height:"+height);
		System.out.println("channel:"+channel);
	}
}
