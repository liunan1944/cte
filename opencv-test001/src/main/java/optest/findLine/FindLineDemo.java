package optest.findLine;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.opencv.core.Mat;
import org.opencv.core.Point;

public class FindLineDemo {
	public static void main(String[] args) {
		
	}
	//寻找图像曲线上某个点的下一个点
	public static boolean findNextPoint(List<Point> neighbor_points,
	Mat src,Point inpoint,
	int flag, Point outpoint, int  outflag){
	 	int i = flag;
	    int count = 1;
	    boolean success = false;
	    
	    int width = src.cols();
		int height = src.rows();
		int channel = src.channels();
		
		byte[] data = new byte[channel*width*height];
		src.get(0, 0, data);
		int r=0,g=0,b=0;
		int index = 0;
	    while (count <= 7){
	        Point tmppoint = new Point((inpoint.x + neighbor_points.get(i).x)/2,
	        		(inpoint.y + neighbor_points.get(i).y)/2);
	        if (tmppoint.x > 0 && tmppoint.y > 0 && tmppoint.x
	        		< src.cols()&&tmppoint.y < src.rows()){
	        	for(int row=0;row<height;row++){
	    			for(int col=0;col<width;col++){
	    				if(row==tmppoint.y && col==tmppoint.x){
	    					index = row*channel*width+col*channel;
		    				b = data[index]&0xff;
		    				g = data[index+1]&0xff;
		    				r = data[index+2]&0xff;
		    				if(b == 255 && g == 255 && r == 255){
		    					data[index] = (byte)0;
		    					data[index+1] = (byte)0;
		    					data[index+2] = (byte)0;
		    					success = true;
		    				}
	    				}
	    				if(success){
	    					break;
	    				}
	    			}
	    			if(success){
    					break;
    				}
	    		}
	    		src.put(0, 0, data);
	        }
	        if (count % 2==0)
	        {
	            i += count;
	            if (i > 7)
	            {
	                i -= 8;
	            }
	        }
	        else
	        {
	            i += -count;
	            if (i < 0)
	            {
	                i += 8;
	            }
	        }
	        count++;
	    }
	    return success;
	}
	//寻找图像上的第一个点
	public static boolean findFirstPoint(Mat inputimg, Point outputpoint){
		boolean success = false;
		
		int width = inputimg.cols();
		int height = inputimg.rows();
		int channel = inputimg.channels();
		
		byte[] data = new byte[channel*width*height];
		inputimg.get(0, 0, data);
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
    					success = true;
    				}
				
				if(success){
					break;
				}
			}
			if(success){
				break;
			}
		}
		inputimg.put(0, 0, data);
	    return success;
	}
	
	//寻找曲线 
	public static void findLines(Mat inputimg, List<Deque<Point>> outputlines)
	{
//		List<Point> neighbor_points = new ArrayList<Point>();
//		neighbor_points.add(new Point(-1,-1));
//		neighbor_points.add(new Point(0,-1));
//		neighbor_points.add(new Point(1,-1));
//		neighbor_points.add(new Point(1,0));
//		neighbor_points.add(new Point(1,1));
//		neighbor_points.add(new Point(0,1));
//		neighbor_points.add(new Point(-1,1));
//		neighbor_points.add(new Point(-1,0));
//	    Point first_point;
//	    while (findFirstPoint(inputimg, first_point))
//	    {
//	    	Deque<Point> line = ;
//	        line.push(first_point);
//	        //由于第一个点不一定是线段的起始位置，双向找
//	        Point this_point = first_point;
//	        int this_flag = 0;
//	        Point next_point;
//	        int next_flag;
//	        while (findNextPoint(neighbor_points, inputimg, this_point, this_flag, next_point, next_flag))
//	        {
//	            line.push(next_point);
//	            this_point = next_point;
//	            this_flag = next_flag;
//	        }
//	        //找另一边
//	        this_point = first_point;
//	        this_flag = 0;
//	        //cout << "flag:" << this_flag << endl;
//	        while (findNextPoint(neighbor_points, inputimg, this_point, this_flag, next_point, next_flag))
//	        {
//	            line.push(next_point);
//	            this_point = next_point;
//	            this_flag = next_flag;
//	        }
//	        if (line.size() > 10)
//	        {
//	            outputlines.push(line);
//	        }
//	    }
	}
}
