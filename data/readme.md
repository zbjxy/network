To run the program first make sure you have
output.txt -- format: original output format
 
output2.txt -- format: x1,y1,x2,y2,xc,yc from the modified java program. 

then simpley type: 
python mergeOutput.py 

you will get: 
finalOutput.txt
sensor.txt


A modified road network generator: 

We have modified the original road network generator so it gives us output2.txt whenever generate a new set of points. We base our experiments on those points. 


mergeOutput.py:

This is the main for our program. 
Functions: 
dis(x1,y1,x2,y2) 
Input:
x1y1, x2y2 -- Coordinates of 2 points 
Output:
The distance between the two points. . 

calPoint(x1,y1,x2,y2,xc,yc,width)
Input: 
x1y1, x2y2 -- coordinates two ends of the current line on the edge
xcyc -- current point location
width --  The width for this (xc,yc) point. 
Output: 
x,y -- representing the altered current point location. 

nextWidth(prev,width)
Input: 
prev -- previous width from gps
width --  width of the road
Output: 
next width based on markov chain(+1 or -1 increment)

nextWidthRand(prev,width)
Input: 
prev -- previous width
width --  width of the road
Output: 
next width based on pure random selection from width of the road. 

correctLines(lineNum,line1,line2)
Input: 
lineNum -- current line number used for debug
line1 -- first line
line2 --  second line
Output: 
True if the lines matches. 
False if the lines do not match. 

sameSpeed(data1, data2)
Input: 
data1 -- first set of data
data2 -- second set of data
Output: 
True if data1 and data2 have the same speed
False otherwise

createSensor(x1,y1,t1,x2,y2,t2,speed)
Input:
x1,y1,t1 -- first GPS point
x2,y2,t2 -- second GPS point
assumes that the first and second GPS point are on the same road. 
speed -- the speed of car between the first and second point. 
output: 
list of [x1,y1,t1,x2,y2,t2,xSensor,ySensor,tSensor]
First 6 variables are the same as input, last 3 are calculated sensor location and time. 

Main
Input:
output.txt -- the original data file from network java program. 
output2.txt -- the modified data file from network java program. 

Output: 
finalOutput.txt -- format: time, edgex1,edgey1,edgex2,edgey2,px,py,speed,width,pxWidth,pyWithWidth
sensor.txt -- format: p1x,p1y,p1t,p2x,p2y,p2t,xSensor,ySensor,tSensor,edgeX1,edgeY1,edgeX2,edgeY2,speed,p1xWidth,p1yWidth,p1Width,p2xWidth,p2yWidth,p2width












