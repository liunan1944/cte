package optest.openAndClose;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import optest.util.ImageUI;
/**
 * 开操作:先腐蚀，在膨胀
 * */
public class OpenDemo {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		//1、加载图像
		Mat src = Imgcodecs.imread("D:\\credit\\opencv-test\\test-01\\example.png");
		ImageUI input = new ImageUI();
		input.imShow("input image",src);
		//2、灰度处理，二值化
		Mat gray = new Mat();
		Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
		
		Mat binary = new Mat();
		Imgproc.threshold(gray, binary, 0, 255, 
				Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
		ImageUI binaryShow = new ImageUI();
		binaryShow.imShow("二值化",binary);
		//3、膨胀
		Mat dst = new Mat();
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
		
		Imgproc.morphologyEx(binary, dst, Imgproc.MORPH_OPEN, kernel);

		ImageUI open = new ImageUI();
		open.imShow("开操作",dst);
	}

}
