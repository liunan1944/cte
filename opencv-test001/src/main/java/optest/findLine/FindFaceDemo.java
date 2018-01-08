package optest.findLine;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import optest.util.ImageUI;
/**
 * 人脸检测:
 * 人脸检测模型:haarcascade_frontalface_alt.xml
 * */
public class FindFaceDemo {
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		CascadeClassifier faceDetector = new CascadeClassifier("E:\\git\\cte\\opencv-test001\\src\\main\\resources\\haarcascade_frontalface_alt.xml");
		//1、加载图像
		Mat src = Imgcodecs.imread("D:\\credit\\opencv-test\\test-01\\00000001.jpg");
		MatOfRect faceDetecions = new MatOfRect();
		faceDetector.detectMultiScale(src, faceDetecions);
		for(Rect rect:faceDetecions.toArray()){
			Imgproc.rectangle(src, rect.tl(), rect.br(), 
					new Scalar(0,255,0),3);
		}
		ImageUI input = new ImageUI();
		input.imShow("input image",src);
		
	}
	public static boolean cvMatEQ(Mat src,Mat dst){
		boolean success = true;
		
		int src_height = src.rows();
		int src_width = src.cols();
		int src_dims = src.channels();
		byte[] src_data = new byte[src_width*src_height*src_dims];
		src.get(0, 0, src_data);
		
		int dst_height = dst.rows();
		int dst_width = dst.cols();
		int dst_dims = dst.channels();
		byte[] dst_data = new byte[dst_width*dst_height*dst_dims];
		dst.get(0, 0, dst_data);
		if(src_height==dst_height && src_width==dst_width 
				&& src_dims==dst_dims ){
			
		}
		return success;
	}
}
