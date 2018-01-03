package optest.policePhoto;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import optest.util.ImageUI;

public class MatPolice01 {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		//加载图像
		Mat src = Imgcodecs.imread("D:\\credit\\opencv-test\\test-01\\00000001.jpg");
		ImageUI input = new ImageUI();
		input.imShow("input image",src);
		
		Mat dst = new Mat();
		List<Mat> mv = new ArrayList<Mat>();
		Core.split(src, mv);
		int index = 1;
		for(Mat mat:mv){
			ImageUI output = new ImageUI();
			output.imShow("output image-"+index,mat);
			index++;
		}
//		Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2HSV);
//		Core.inRange(src, new Scalar(20,0,0), new Scalar(255,100,100), dst);
//		ImageUI output = new ImageUI();
//		output.imShow("output image",dst);
	}
}
