package optest.hufu;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import optest.util.ImageUI;
/**
 * 霍夫直线检测
 * */
public class HufuDemo {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		//加载图像
		Mat src = Imgcodecs.imread("D:\\credit\\opencv-test\\test-01\\00000001.png");
		ImageUI input = new ImageUI();
		input.imShow("input image",src);
		
		Mat edges = new Mat();
		Imgproc.Canny(src, edges, 50, 150,3,true);
		
		Mat lines = new Mat();
		Imgproc.HoughLinesP(edges, lines, 1, Math.PI/180.0, 50,30,10);
		
		Mat result = Mat.zeros(src.size(), src.type());
		for(int i=0;i<lines.rows();i++){
			int[] oneLine = new int[4];
			lines.get(i, 0,oneLine);
			Imgproc.line(result, new Point(oneLine[0],oneLine[1]), 
					new Point(oneLine[2],oneLine[3]), new Scalar(0,0,255),
					2,8,0);
		}
		ImageUI gaussan = new ImageUI();
		gaussan.imShow("霍夫 image",result);
	}

}
