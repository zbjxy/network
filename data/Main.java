package data;

import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

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
			fileName = "sensor.txt";
		}
		String path = "/Users/leoliu/workspace/network/data/";
		FileInputStream fstream = new FileInputStream(path + fileName);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String strLine;

		// Read File Line By Line
		while ((strLine = br.readLine()) != null) {
			// Print the content on the console
			System.out.println(strLine.split(" "));
			float[] temp = parseFloatArray(strLine.split(" "));
			// todo
			/*
			 * do whatever you want here.
			 */
			System.out.println(temp[0]);
		}
		// Close the input stream
		br.close();
	}

	public static void main(String[] args) throws IOException {
		System.out.println(args[0]);
		// write your code here
		// int x_min=0;
		// int x_max=10;
		// int y_min=0;
		// int y_max=10;
		// int grid_n=40;
		// int grid_density=(x_max-x_min)/grid_n;

		// we first will use grid 4000*4000, density 0.1
	}

	public static void doTest(float[] line) throws IOException {
		/*
		 * 
		 *  
		 * 
		 */
		double area = len_area_total(0, 0, 0, 100, 0, 10, 5, 12, 1, 400); // theoretical
																			// value:
																			// 900.311122,
																			// estimated
																			// value:
																			// 899.99
		double area_above = len_area_aboveroad(0, -2, 0, 100, 4, 10, 5, 12,
				0.5, 800);

		// double area_2=len_area_total(0,0,0,50,0,5,2.5,12,0.2,2000);

		System.out.print(area);
		System.out.print("\n");
		System.out.print(area_above);
		System.out.print("\n dis=50\n");
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

		double grid_density = (eucDis(0, 0, 100, 0) / 100); // these two
															// functions convert
															// distance to grid
															// density
		int grid_size = (int) (400 / grid_density);
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
		boolean FB_result = lane_crossing_FB(0, -5, 0, 100, -0.2, 10, 0, 10,
				12, 50, 4.5, 0.1, grid_density, grid_size);
		if (FB_result == true) {
			System.out.print("\n FB judge ");
			System.out.print("YES");
		} else {
			System.out.print("\n FB judge ");
			System.out.print("NO");
		}

		boolean bead_result = lane_crossing_bead(0, -5, 0, 100, -0.2, 10, 0,
				10, 12, 0.1, grid_density, grid_size);
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
								.pow(radius2, 2) && yaxis * y1 < 0) {
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
		return len_area_aboveroad(x1, y1, t1, x2, y2, t2, tq, speed, grid_d,
				grid_size)
				/ len_area_total(x1, y1, t1, x2, y2, t2, tq, speed, grid_d,
						grid_size);
	}

	public static double eucDis(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
	}

	public static boolean lane_crossing_bead(double x1, double y1, double t1,
			double x2, double y2, double t2, double tmin, double tmax,
			double speed, double deltat, double grid_d, int grid_size) {

		System.out.print("running");
		if (lane_crossing_bead_prob(x1, y1, t1, x2, y2, t2, tmin, speed,
				grid_d, grid_size) > 0.3
				&& lane_crossing_bead_prob(x1, y1, t1, x2, y2, t2, tmax, speed,
						grid_d, grid_size) > 0.3) {
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
		System.out.print(grid_xs);
		System.out.print("upper and lower of y");
		System.out.println(grid_ys_max + ".." + grid_ys_min);
		// here, our unit movement choose to be the grid density area.
		if (tq < ts) // left half-FB
		{
			for (int y_move = grid_ys_min; y_move < grid_ys_max; y_move++) // y_move
																			// is
																			// grid
			{
				for (int rowindex = 0; rowindex < grid_size; rowindex++) {
					for (int colindex = 0; colindex < grid_size; colindex++) {
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
			for (int y_move = grid_ys_min; y_move < grid_ys_max; y_move++) // y_move
																			// is
																			// grid
			{
				for (int rowindex = 0; rowindex < grid_size; rowindex++) {
					for (int colindex = 0; colindex < grid_size; colindex++) {
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

				if (FBgrid[rowindex][colindex] == 1 && yaxis * y1 < 0) {
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
		double prob = FB_lane_cross_area(x1, y1, t1, x2, y2, t2, tq, speed, xs,
				ts, grid_d, grid_size)
				/ FB_crossArea(x1, y1, t1, x2, y2, t2, tq, speed, xs, ts,
						grid_d, grid_size);
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

		System.out.print("running");
		if (FB_lane_cross_prob(x1, y1, t1, x2, y2, t2, tmin, speed, xs, ts,
				grid_d, grid_size) > 0.3
				&& FB_lane_cross_prob(x1, y1, t1, x2, y2, t2, tmax, speed, xs,
						ts, grid_d, grid_size) > 0.3) {
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
	// we need a tranformation function to transfer

	// //////////////////////////////////////////////FB range
	// query/////////////////////////////////

}
