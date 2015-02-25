import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommandLineMain_range_prune_fp {

    static double speedCoefficient=1.6;
    static double tq_max=0;
    static double prob_max=0;
    static double[] beadarea=new double[2];
    static double[] FBarea=new double[2];
    static double rangex=0;
    static double rangey=0;
    static double radius=0;
    static double rangexf=0;
    static double rangeyf=0;
    static double ranger=0;
static double FBoldtime=0;
static double FBtotaltime=0;
	private static double[][] FBgrid;

	/**
	 * 
	 * @param inStr
	 *            -- the input String array containing float numbers.
	 * @return float[] that the input String array has.
	 */
	public static float[] parseFloatArray(String[] inStr) {
		float[] temp = new float[inStr.length];
		for (int i = 0; i < inStr.length; i++) {
			temp[i] = Float.parseFloat(inStr[i]);

		}
		return temp;
	}

	/**
	 * 
	 * @param fileName
	 *            The name of the file reside in
	 *            "/Users/leoliu/workspace/network/data/" If not given, then it
	 *            defaults to sensor.txt
	 * @throws IOException
	 */
	public static void readFile(String fileName) throws IOException {
		if (fileName.equals("")) {
			fileName = "sensor50_rand.txt";
		}
		String path ="";
		//"/Users/leoliu/workspace/network/data/";
		FileInputStream fstream = new FileInputStream(path + fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String strLine;
		ArrayList<Integer> fbResults = new ArrayList<Integer>();
		ArrayList<Integer> obResults = new ArrayList<Integer>();
                ArrayList<Integer> truth = new ArrayList<Integer>();
		// Read File Line By Line
        int lineNum = 1;
	long timeOldBead=0;
	long totaltimebead=0;
        long timeOld=0;
        long totalTime=0;
	int total_pruning_out=0;
	int total_pruning_in=0;

        while ((strLine = br.readLine()) != null) {
            System.out.println("reading line "+lineNum);
            // Print the content on the console
			// System.out.println(strLine.split(" "));
			float[] temp = parseFloatArray(strLine.split(" "));
            
			// TODO add function calls to algorithm.
			/*
			 * count the number of yess for fb and b, and then result
			 */
			// format:
			// p1x,p1y,p1t,p2x,p2y,p2t,xSensor,ySensor,tSensor,edgeX1,edgeY1,edgeX2,edgeY2,speed,p1xWidth,p1yWidth,p1Width,p2xWidth,p2yWidth,p2width
			// 0, 1, 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19
			//we transform coordinate
		//we make pruning
		double pruningx=((double) temp[0]+(double) temp[3])/2;
		double pruningy=((double) temp[1]+(double) temp[4])/2;
		double dis=eucDis((double) temp[0], (double) temp[1],(double) temp[3], (double) temp[4]);
		double pruning_s = (double) temp[13];
		double pruningr=((((double) temp[5] - (double) temp[2])/2)-(dis/(2*pruning_s)))*pruning_s+(dis/2); 
		if(eucDis(pruningx, pruningy, rangex, rangey)>(pruningr+radius))
		{
		  obResults.add(0);
		  fbResults.add(0);
		  truth.add(0);
		  total_pruning_out++;
		  
		}
		else if(eucDis(pruningx, pruningy, rangex, rangey)<(radius-pruningr))
		{
		  obResults.add(0);
		  fbResults.add(0);
		  truth.add(0);
		  total_pruning_in++;
		  
		}
		else
		{

			double x1 = 0;
			double y1 =  (temp[16] / 10.0);
			double x2 = eucDis((double) temp[0], (double) temp[1],
					(double) temp[3], (double) temp[4]) / 10.0;
			double y2 =  (temp[19] / 10.0);
			double t2 = ((double) temp[5] - (double) temp[2]); // (p2t-p1t)*5
			double xs = x2 / 2.0;
			double ts = ((double) temp[8]-(double) temp[2]);
			double speed = (double) temp[13] / 10.0; // we use speed*1.1 in the
			//double speed=eucDis((double) temp[0], (double) temp[1], (double) temp[3], (double) temp[4]) / 50.0;
                        double speed_app=speed*speedCoefficient;
			System.out.printf("x1--:%f y1--:%f x2--:%f y2--:%f t2--:%f xs--:%f ts--:%f speed--:%f",x1,y1,x2,y2,t2,xs,ts,speed_app);
		
			 //convert coordinate for range
			double rangexConvert=(rangex-((double) temp[0]-0))/10;
			double rangeyConvert=(rangey-((double) temp[1]-0))/10;
			double x2Convert=(double) temp[3] - (double) temp[0];
			double y2Convert=(double) temp[4] - (double) temp[1];
			  double cos=x2Convert/eucDis(0,0,x2Convert, y2Convert);
			  double sin=-y2Convert/eucDis(0,0,x2Convert, y2Convert);

			  rangexf=rangexConvert*cos-rangeyConvert*sin;  //this is the number we are going to use
			  rangeyf=rangexConvert*sin+rangeyConvert*cos;
			ranger=radius/10;
		     System.out.printf("\nx1'--:%f y1'--:%f rx1--:%f ry1--:%f rx'--:%f ry'--:%f r--:%f ",0.0,0.0,rangex,rangey,rangexf,rangeyf,ranger );
		

			double grid_density = (eucDis(x1, 0, x2, 0) / 100); // these two
			// functions convert
			// distance to grid
			// density
			int grid_size = (int) (15 / grid_density);
			FBgrid = new double[grid_size][grid_size];
			for (int i = 0; i < grid_size; i++) {
				for (int j = 0; j < grid_size; j++) {
					FBgrid[i][j] = 0.0;
				}
			}

/*
			timeOld = System.currentTimeMillis();
			boolean FB_result = lane_crossing_FB(x1, y1, 0, x2, y2, t2, 0, t2,
					speed_app, xs, ts, 0.2, grid_density, grid_size);
			totalTime += System.currentTimeMillis()-timeOld;
			if (FB_result == true) {
				System.out.print("\n FB judge ");
				System.out.print("1");
				fbResults.add(1);
			} else {
				System.out.print("\n FB judge ");
				System.out.print("0");
				fbResults.add(0);
			}

            
			boolean bead_result = lane_crossing_bead(x1, y1, 0, x2, y2, t2, 0,
					t2, speed_app, 0.2, grid_density, grid_size);
            
            if (bead_result == true) {
				System.out.print("\n bead judge ");
				System.out.print("YES");
				obResults.add(1);
			} else {
				System.out.print("\n bead judge ");
				System.out.print("NO");
				obResults.add(0);
			}
                        if(y2*y1<0)
			{
				truth.add(1);
			} else {
                                truth.add(0);
                        }
*/
			timeOldBead=System.currentTimeMillis();
			double bead_agg=lane_crossing_bead_agg(x1, y1, 0, x2, y2, t2, 0,t2, speed_app, 0.05, grid_density, grid_size);
			totaltimebead+=System.currentTimeMillis()-timeOldBead;
			timeOld = System.currentTimeMillis();
			double FB_agg=lane_crossing_FB_agg(x1, y1, 0, x2, y2, t2, 0, t2,speed_app, xs, ts, 0.05, grid_density, grid_size);
			totalTime += System.currentTimeMillis()-timeOld;
/*
			double FB_total_area = FB_crossArea(x1, y1, 0, x2, y2, t2, tq_max, speed_app, xs,ts, grid_density, grid_size);
		double FB_cross_area = FB_lane_cross_area(x1, y1, 0, x2, y2, t2, tq_max,speed_app, xs, ts, grid_density, grid_size);

		for (int i = 0; i < grid_size; i++) {
			for (int j = 0; j < grid_size; j++) {
				FBgrid[i][j] = 0.0;
			}
		}

                         double bead_area= len_area_aboveroad(x1, y1, 0, x2, y2, t2, tq_max, speed_app, grid_density,grid_size);
		         double bead_total=len_area_total(x1, y1, 0, x2, y2, t2, tq_max, speed_app, grid_density, grid_size);
		double crossratio=FB_cross_area/bead_area;
		double totalratio=FB_total_area/bead_total;
                
			// System.out.println(temp[0]);
		System.out.println("\ncross area FB/Bead="+crossratio);
		System.out.println("\ntotal area FB/Bead="+totalratio);
		System.out.println("\nBead total prob is:="+bead_agg);
		System.out.println("\nFB total prob is:="+FB_agg);
*/
		System.out.println("\nfinished reading line"+lineNum);
		if(bead_agg==1 && FB_agg==1)
		{
		  System.out.print("\n bead judge ");
				System.out.print("1");
				obResults.add(1);
		   System.out.print("\n FB judge ");
				System.out.print("YES");
				fbResults.add(1);
		}
		else
		{

		double bead_prob_f=beadarea[0]/beadarea[1];
		double FB_prob_f=FBarea[0]/FBarea[1];
		System.out.println("\nbead aggregated prob is:="+(bead_prob_f));
		System.out.println("\nFB aggregated prob is:="+(FB_prob_f));
			if (bead_prob_f > 0.1) {
				System.out.print("\n bead judge ");
				System.out.print("1");
				obResults.add(1);
			} else {
				System.out.print("\n bead judge ");
				System.out.print("0");
				obResults.add(0);
			}
			if (FB_prob_f >0.1) {
				System.out.print("\n FB judge ");
				System.out.print("YES");
				fbResults.add(1);
			} else {
				System.out.print("\n FB judge ");
				System.out.print("NO");
				fbResults.add(0);
			}

		}

                        if( (eucDis(x1,y1,rangexf,rangeyf)-ranger)*(eucDis(x2,y2,rangexf,rangeyf)-ranger)<0)
			{
				truth.add(1);
			} else {
                                truth.add(0);
                        }
		beadarea[0]=0;
		beadarea[1]=0;
		FBarea[0]=0;
		FBarea[1]=0;
		tq_max=0;
		prob_max=0;

		}

            lineNum++;
        }
		// Close the input stream
		br.close();
		int sumFb = 0;
		int sumOb = 0;
                int FP=0;
		int FP_notinFB=0;
		for (int i = 0; i < fbResults.size(); i++) {
			sumFb += fbResults.get(i);
		}
		for (int i = 0; i < obResults.size(); i++) {
			sumOb += obResults.get(i);
		}
		System.out.println("sumfb= " + sumFb);
		System.out.println("sumob= " + sumOb);
		System.out.println("fbResults = " + arrayToString(fbResults));
		System.out.println("obResults = " + arrayToString(obResults));
		System.out.println("total pruned outside data = " + total_pruning_out);
		System.out.println("total pruned inside data = " + total_pruning_in);
                for (int i = 0; i < fbResults.size(); i++) {
			if(truth.get(i)==0 && obResults.get(i)==1)
			{
			    FP++;
			}
                        if(truth.get(i)==0 && obResults.get(i)==1 && fbResults.get(i)==0)
			{
			    FP_notinFB++;
                        }
		}
                System.out.println("false positive= " + FP);
		System.out.println("false positive corrected= " + FP_notinFB);
	    System.out.println("bead total time: "+totaltimebead);
	    System.out.println("FB total time: "+totalTime);
    }

	public static String arrayToString(List<Integer> inArray) {
		StringBuilder sb = new StringBuilder();
		for (int str : inArray) {
			sb.append(str);
		}
		return sb.toString();
	}

	public static void main(String[] args) throws IOException {
		// now, in this exp, the arguments are -filename -coefficient -rangex - rangey -ranger
		if (args.length < 5) {
			System.out.println("please supply a file name!");
			return;
		}
		System.out.println("reading: "+args[0]);
        speedCoefficient = Double.parseDouble(args[1]);
        System.out.println("speed coefficient: "+speedCoefficient);
	rangex=Double.parseDouble(args[2]);
	rangey=Double.parseDouble(args[3]);
	radius=Double.parseDouble(args[4]);
		// write your code here
		// int x_min=0;
		// int x_max=10;
		// int y_min=0;
		// int y_max=10;
		// int grid_n=40;
		// int grid_density=(x_max-x_min)/grid_n;
		// TODO read in different files.
		 readFile(args[0]);
		// readFile("");
		//doTest();
		// we first will use grid 4000*4000, density 0.1
	}

	public static void doTest() throws IOException {
		/*
		 * 
		 *  
		 * 
		 */
		// double area = len_area_total(0, 0, 0, 100, 0, 10, 5, 12, 1, 400); //
		// theoretical
		// value:
		// 900.311122,
		// estimated
		// value:
		// 899.99
		// double area_above = len_area_aboveroad(0, -2, 0, 100, 4, 10, 5,
		// 12,0.5, 800);

		// double area_2=len_area_total(0,0,0,50,0,5,2.5,12,0.2,2000);

		// System.out.print(area);
		// System.out.print("\n");
		// System.out.print(area_above);
		// System.out.print("\n dis=50\n");
		// System.out.print(area_2);

		/* test lane-cross-bead */
		/*
		 * double grid_density=eucDis(0,-5,100,-0.2)/100; int
		 * grid_size=(int)(400/grid_density); System.out.print(grid_size);
		 * boolean result=lane_crossing_bead(0,-5,0,100,-0.2,10,0,10,0.1,
		 * grid_density, grid_size); if(result==true) { System.out.print("\n");
		 * System.out.print("YES"); } else { System.out.print("\n");
		 * System.out.print("NO"); }
		 */
		/***********/
		/* start to calculate FB */

		double grid_density = (eucDis(0, 0, 10, 0) / 100); // these two
															// functions convert
															// distance to grid
															// density
		int grid_size = (int) (100 / grid_density);
		FBgrid = new double[grid_size][grid_size];
		for (int i = 0; i < grid_size; i++) {
			for (int j = 0; j < grid_size; j++) {
				FBgrid[i][j] = 0.0;
			}
		}
		/*
		 * double FBarea = FB_crossArea(0, -5, 0, 100, -0.2, 10, 7, 12, 50, 4.5,
		 * grid_density, grid_size); System.out.print("\nFB total area is: ");
		 * System.out.print(FBarea); double bead_area = len_area_total(0, -5, 0,
		 * 100, -0.2, 10, 7, 12, grid_density, grid_size);
		 * System.out.print("\nBead total area is: ");
		 * System.out.print(bead_area); double bead_above =
		 * FB_lane_cross_area(0, -5, 0, 100, -0.2, 10, 7, 12, 50, 4.5,
		 * grid_density, grid_size);
		 * System.out.print("\nFB cross total area is: ");
		 * System.out.print(bead_above);
		 */
//double FBarea = FB_crossArea(0, -8, 0, 100, -4, 10, 5, 11, 50, 4.5,grid_density, grid_size); 
//System.out.print("\nFB total area is: ");
		//System.out.print(FBarea);
		boolean FB_result = lane_crossing_FB(0, -2, 0, 10, -3, 1, 0, 1, 15,
				5, 0.4, 0.05, grid_density, grid_size);
		if (FB_result == true) {
			System.out.print("\n FB judge ");
			System.out.print("YES");
		} else {
			System.out.print("\n FB judge ");
			System.out.print("NO");
		}

		boolean bead_result = lane_crossing_bead(0, -2, 0, 10, -3, 1, 0, 1,
				15, 0.05, grid_density, grid_size);
		if (bead_result == true) {
			System.out.print("\n bead judge ");
			System.out.print("YES");
		} else {
			System.out.print("\n bead judge ");
			System.out.print("NO");
		}

		/*********************************************/

	}

	// ///////////////////////////////////////////functions for lane-crossing
	// bead ///////////////////////////////////////////
	public static double len_area_total(double x1, double y1, double t1,
			double x2, double y2, double t2, double tq, double speed,
			double grid_d, int grid_size) // we now use (1500+2500)^2 grid
	{
		double[][] grid = new double[grid_size][grid_size]; // [y--row][x--col]
		int rowindex = 0;
		int colindex = 0;
		double count = 0;
		double unit_area = grid_d * grid_d;
		for (rowindex = 0; rowindex < grid_size; rowindex++) {
			for (colindex = 0; colindex < grid_size; colindex++) {
				double xaxis = (colindex - ((int) (0.25 * grid_size))) * grid_d;
				double yaxis = (rowindex - ((int) (0.50 * grid_size))) * grid_d;
				double radius1 = speed * (tq - t1);
				double radius2 = speed * (t2 - tq);
				if ((Math.pow((xaxis - x1), 2) + Math.pow((yaxis - y1), 2)) < Math
						.pow(radius1, 2)
						&& (Math.pow((xaxis - x2), 2) + Math.pow((yaxis - y2),
								2)) < Math.pow(radius2, 2)) {
					count++;
				}

			}
		}
		double area = count * unit_area;
		return area;

	}

	public static double len_area_aboveroad(double x1, double y1, double t1,
			double x2, double y2, double t2, double tq, double speed,
			double grid_d, int grid_size) // we now use (1500+2500)^2 grid
	{
		double[][] grid = new double[grid_size][grid_size]; // [y--row][x--col]
		int rowindex = 0;
		int colindex = 0;
		double count = 0;
		double unit_area = grid_d * grid_d;
		for (rowindex = 0; rowindex < grid_size; rowindex++) {
			for (colindex = 0; colindex < grid_size; colindex++) {
				double xaxis = (colindex - ((int) (0.25 * grid_size))) * grid_d;
				double yaxis = (rowindex - ((int) (0.50 * grid_size))) * grid_d;
				double radius1 = speed * (tq - t1);
				double radius2 = speed * (t2 - tq);
				if ((Math.pow((xaxis - x1), 2) + Math.pow((yaxis - y1), 2)) < Math
						.pow(radius1, 2) && // here, check within range and
											// check on different side at the
											// same time
						(Math.pow((xaxis - x2), 2) + Math.pow((yaxis - y2), 2)) < Math
								.pow(radius2, 2) && (eucDis(xaxis,yaxis,rangexf,rangeyf)-ranger) * (eucDis(x1,y1,rangexf,rangeyf)-ranger) < 0) {
					count++;
				}

			}
		}
		double area = count * unit_area;
		return area;

	}

	public static double lane_crossing_bead_prob(double x1, double y1,
			double t1, double x2, double y2, double t2, double tq,
			double speed, double grid_d, int grid_size) {
		double crossarea= len_area_aboveroad(x1, y1, t1, x2, y2, t2, tq, speed, grid_d,
				grid_size);
		double totalarea=len_area_total(x1, y1, t1, x2, y2, t2, tq, speed, grid_d,
						grid_size);
		
		beadarea[0]+=crossarea;
		beadarea[1]+=totalarea;
		double prob;
		if(totalarea==0)
		{
		  prob=0;
		}
		else
		{
		  prob=crossarea/totalarea;
		}
		
		System.out.printf("\nbead prob is: %f |", prob);
		return prob;
	}

	public static double eucDis(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
	}

	public static boolean lane_crossing_bead(double x1, double y1, double t1,
			double x2, double y2, double t2, double tmin, double tmax,
			double speed, double deltat, double grid_d, int grid_size) {

		// System.out.print("running");
		if (lane_crossing_bead_prob(x1, y1, t1, x2, y2, t2, tmin, speed,
				grid_d, grid_size) >1
				|| lane_crossing_bead_prob(x1, y1, t1, x2, y2, t2, tmax, speed,
						grid_d, grid_size) >1) {
			return true;
		}
		if (tmax - tmin < deltat) {
			return false;
		} else {
			return lane_crossing_bead(x1, y1, t1, x2, y2, t2, tmin,
					(tmin + tmax) / 2, speed, deltat, grid_d, grid_size)
					|| lane_crossing_bead(x1, y1, t1, x2, y2, t2,
							(tmin + tmax) / 2, tmax, speed, deltat, grid_d,
							grid_size);

		}

	}
	public static double lane_crossing_bead_agg(double x1, double y1, double t1,
			double x2, double y2, double t2, double tmin, double tmax,
			double speed, double deltat, double grid_d, int grid_size){
		double total_prob=0;
		for(double i=t1;i<=t2;i=i+deltat)
		{
		  double Curprob=lane_crossing_bead_prob(x1, y1, t1, x2, y2, t2, i, speed,
				grid_d, grid_size);
		  if(Curprob==1)
		  {
		    return 1;
		  }
		  total_prob=total_prob+Curprob;
		}
		return total_prob;

	}

	// ///////////////////////////////////////////////////function for lane
	// crossing-FB////////////////////
	public static double FB_crossArea(double x1, double y1, double t1,
			double x2, double y2, double t2, double tq, double speed,
			double xs, double ts, double grid_d, int grid_size) {
		int grid_xs = (int) (xs / grid_d) + ((int) (0.25 * grid_size));
		int grid_ys_max = Integer.MIN_VALUE;
		int grid_ys_min = Integer.MAX_VALUE;
		for (int row = 0; row < grid_size; row++) {
			double yaxis = (row - ((int) (0.50 * grid_size))) * grid_d;
			// radius for sensor time
			double radius1 = speed * (ts - t1);
			double radius2 = speed * (t2 - ts);
			if ((Math.pow((xs - x1), 2) + Math.pow((yaxis - y1), 2)) < Math
					.pow(radius1, 2)
					&& (Math.pow((xs - x2), 2) + Math.pow((yaxis - y2), 2)) < Math
							.pow(radius2, 2)) {
				if (row > grid_ys_max) {
					grid_ys_max = row;
				}
				if (row < grid_ys_min) {
					grid_ys_min = row;
				}
			}
		}
		// System.out.print(grid_xs);
		// System.out.print("upper and lower of y");
		// System.out.println(grid_ys_max + ".." + grid_ys_min);
		// here, our unit movement choose to be the grid density area.
		if (tq < ts) // left half-FB
		{
			for (int y_move = grid_ys_min; y_move < grid_ys_max; y_move=y_move+15) // y_move
																			// is
																			// grid
			{
				for (int rowindex = 0; rowindex < grid_size; rowindex++) {
					for (int colindex = 0; colindex <= grid_xs; colindex++) {
						double xaxis = (colindex - ((int) (0.25 * grid_size)))
								* grid_d;
						double yaxis = (rowindex - ((int) (0.50 * grid_size)))
								* grid_d;
						double y_move_asix = (y_move - ((int) (0.50 * grid_size)))
								* grid_d;
						double radius1 = speed * (tq - t1);
						double radius2 = speed * (ts - tq);
						if ((Math.pow((xaxis - x1), 2) + Math.pow((yaxis - y1),
								2)) < Math.pow(radius1, 2)
								&& (Math.pow((xaxis - xs), 2) + Math.pow(
										(yaxis - y_move_asix), 2)) < Math.pow(
										radius2, 2)) {
							if (FBgrid[rowindex][colindex] == 0) {
								FBgrid[rowindex][colindex] = 1;
							}
						}

					}
				}
			}

		} else // right half-FB
		{
			for (int y_move = grid_ys_min; y_move < grid_ys_max; y_move=y_move+15) // y_move
																			// is
																			// grid
			{
				for (int rowindex = 0; rowindex < grid_size; rowindex++) {
					for (int colindex = grid_xs; colindex < grid_size; colindex++) {
						double xaxis = (colindex - ((int) (0.25 * grid_size)))
								* grid_d;
						double yaxis = (rowindex - ((int) (0.50 * grid_size)))
								* grid_d;
						double y_move_asix = (y_move - ((int) (0.50 * grid_size)))
								* grid_d;
						double radius1 = speed * (tq - ts);
						double radius2 = speed * (t2 - tq);
						if ((Math.pow((xaxis - xs), 2) + Math.pow(
								(yaxis - y_move_asix), 2)) < Math.pow(radius1,
								2)
								&& (Math.pow((xaxis - x2), 2) + Math.pow(
										(yaxis - y2), 2)) < Math
										.pow(radius2, 2)) {
							if (FBgrid[rowindex][colindex] == 0) {
								FBgrid[rowindex][colindex] = 1;
							}
						}

					}
				}
			}

		}
		double count = 0;
		double unit_area = grid_d * grid_d;
		for (int i = 0; i < grid_size; i++) {
			// System.out.print("\n");
			for (int j = 0; j < grid_size; j++) {
				// System.out.print(FBgrid[i][j]);
				if (FBgrid[i][j] == 1) {
					count++;
				}
			}
		}
		double area = count * unit_area;
		// System.out.print("\ntotal area is: ");
		// System.out.print(area);
		return area;

	}

	// run FB cross_area before other function, because FB cross_area initialize
	// the matrix
	public static double FB_lane_cross_area(double x1, double y1, double t1,
			double x2, double y2, double t2, double tq, double speed,
			double xs, double ts, double grid_d, int grid_size) {
		double count = 0;
		double unit_area = grid_d * grid_d;
		for (int rowindex = 0; rowindex < grid_size; rowindex++) {
			for (int colindex = 0; colindex < grid_size; colindex++) {
				double xaxis = (colindex - ((int) (0.25 * grid_size))) * grid_d;
				double yaxis = (rowindex - ((int) (0.50 * grid_size))) * grid_d;

				if (FBgrid[rowindex][colindex] == 1 && (eucDis(xaxis,yaxis,rangexf,rangeyf)-ranger)* (eucDis(x1,y1,rangexf,rangeyf)-ranger) < 0) {
					count++;
				}

			}
		}
		double area = count * unit_area;
		return area;
	}

	public static double FB_lane_cross_prob(double x1, double y1, double t1,
			double x2, double y2, double t2, double tq, double speed,
			double xs, double ts, double grid_d, int grid_size) {
		double total_area = FB_crossArea(x1, y1, t1, x2, y2, t2, tq, speed, xs,
				ts, grid_d, grid_size);
		double cross_area = FB_lane_cross_area(x1, y1, t1, x2, y2, t2, tq,
				speed, xs, ts, grid_d, grid_size);
		double prob;
		FBarea[0]+=cross_area;
		FBarea[1]+=total_area;

		if(total_area==0)
		{
		  prob=0;
		}
		else
		{
		  prob=cross_area/total_area;
		}
		System.out.printf("\nFB prob is: %f |", prob);

                if(prob>prob_max)
		{
		  prob_max=prob;
		  tq_max=tq;
		}
		for (int i = 0; i < grid_size; i++) {
			for (int j = 0; j < grid_size; j++) {
				FBgrid[i][j] = 0.0;
			}
		}
		return prob;
	}

	public static boolean lane_crossing_FB(double x1, double y1, double t1,
			double x2, double y2, double t2, double tmin, double tmax,
			double speed, double xs, double ts, double deltat, double grid_d,
			int grid_size) {

		//System.out.print("running");
		if (FB_lane_cross_prob(x1, y1, t1, x2, y2, t2, tmin, speed, xs, ts,
				grid_d, grid_size) >1
				|| FB_lane_cross_prob(x1, y1, t1, x2, y2, t2, tmax, speed, xs,
						ts, grid_d, grid_size) >1) {
			return true;
		}
		if (tmax - tmin < deltat) {
			return false;
		} else {
			return lane_crossing_FB(x1, y1, t1, x2, y2, t2, tmin,
					(tmin + tmax) / 2, speed, xs, ts, deltat, grid_d, grid_size)
					|| lane_crossing_FB(x1, y1, t1, x2, y2, t2,
							(tmin + tmax) / 2, tmax, speed, xs, ts, deltat,
							grid_d, grid_size);

		}

	}

	public static double lane_crossing_FB_agg(double x1, double y1, double t1,
			double x2, double y2, double t2, double tmin, double tmax,
			double speed, double xs, double ts, double deltat, double grid_d,
			int grid_size) {
		double total_prob=0;
		for(double i=t1;i<=t2;i=i+deltat)
		{
		  double Curprob=FB_lane_cross_prob(x1, y1, t1, x2, y2, t2, i, speed, xs, ts,
				grid_d, grid_size);
		  if(Curprob==1)
		  {
		    return 1;
		  }
		  total_prob=total_prob+Curprob;
		}
		return total_prob;

	}
	// we need a tranformation function to transfer

	// //////////////////////////////////////////////FB range
	// query/////////////////////////////////

}
