package optest.drawContours;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import optest.util.ImageUI;
/**
 * 寻找轮廓
 * */
public class DrawContoursDemo {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		double threshold1 = 3;
		//1、加载图像
		Mat src = Imgcodecs.imread("D:\\credit\\opencv-test\\test-01\\00000001.jpg");
		Mat destination = new Mat(src.rows(),src.cols(),src.type());
		Imgproc.GaussianBlur(src, src,new Size(9,9),20,0);
		Imgproc.Canny(src, destination, threshold1, 100);
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>(100);
		Mat hierarchy = new Mat(src.rows(),src.cols(),
				CvType.CV_8UC1,new Scalar(0));
		Imgproc.findContours(destination, contours, hierarchy, 
				Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
		Mat drawing = Mat.zeros(destination.size(), CvType.CV_8UC3);
		for(int i=0;i<contours.size();i++){
			Imgproc.drawContours(drawing, contours, i, 
					new Scalar(255,0,0,255),1);
		}
		ImageUI input = new ImageUI();
		input.imShow("input image",drawing);
		
	}
}
