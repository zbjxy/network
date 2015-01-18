import random

#kill the first line from output.txt
#merge the 2 files together, taking the first output.txt's speed, and then assign a random width. 
#provide x1 y1 x2 y2 xc yc and width 
#return xn yn which is the point xc yc correspond to. 
#distance functions for the 2 points
def dis(x1,y1,x2,y2):
    import math
    return math.sqrt((x1-x2)**2+(y1-y2)**2)

#caculate the corresponding xy values from the current xc, yc. 
def calPoint(x1,y1,x2,y2,xc,yc,width):
    import math
    #There are four situations. 
    #1. slope is postive, and the width is postive
    #2. slope is positive, and the width is negative
    #3. slope is negative, and the width is postive
    #4. slope is negative, and the width is negative
    #check for vertical lines first. 
    #if the lines are vertical, then we can do easy processing
    if y2==y1:
        return xc,yc-width
    elif x2 == x1:
        return xc+width,yc

    slopeL1 = (y2-yc)/(x2-xc)
    if width>0:
        y = yc - math.sqrt(width**2/(1+1/slopeL1**2))
        x = xc - math.sqrt(width**2 - (y-yc)**2)
    else:
        y = yc + math.sqrt(width**2/(1+1/slopeL1**2))
        x = xc + math.sqrt(width**2 - (y-yc)**2)
    return x,y

#input: previous width parameter. 
#output: the next width parameter. 
def nextWidth(prev,width):
    #0/1
    temp = round(random.random())
    if temp==0:
        temp=-1
    if prev+temp>width:
        temp = -1
    elif prev+temp<-1*width:
        temp = 1
    return temp+prev
def nextWidthRand(prev,width):
    temp = random.random()
    return temp * 2 * width -width

def correctLines(lineNum, line1, line2):
    try:
        assert line1[-2]==line2[2], '%r: assertion error, %r is not equal to %r'%(lineNum,line1[-2],line2[2])
        assert line1[-1] == line2[3], '%r: assertion error, %r is not equal to %r'%(lineNum,line1[-1],line2[3])
        return True
    except AssertionError:
        print "assertionError at "+str(lineNum)
        return False

#the first file format: 
# point, na, na, na, time, currentx, currenty, speed, nextx, nexty
# the second file format: 
# x1 y1 x2 y2 currentx currenty
# the new file format: 
#x1 y1 x2 y2 currentx currenty speed width alteredx alteredy.
fname1 = 'output.txt'
fname2 = 'output2.txt'
fname3 = 'finaloutput.txt'
fname4 = 'newCorrOutput.txt'
widthRange = 5#this means range from -5 to 5. 
f1 = open(fname1,'r')
#f2 = open(fname2,'r')
#f3 is going to be the new file holding a width and line segements with format x1 y1 x2 y2 xc yc speed width
f3 = open(fname3,'w')
f3.truncate()
f4 = open(fname4,'w')
f4.truncate()
#burn the first line because we dont need it. 
f1.readline()
lineNum=0
prev=0
x = 0.0
y = 0.0
with open(fname2,'r') as f2:
    for line2 in f2:
        lineNum+=1
        lineTemp = []
        line1Temp = f1.readline().strip().split()
        line2Temp = line2.strip().split()
        while(not correctLines(lineNum,line1Temp, line2Temp)):
            line1Temp = f1.readline().strip().split()
        widthTemp = nextWidth(prev,widthRange)
        prev = widthTemp
        lineTemp.extend(line2Temp)
        lineTemp.append(line1Temp[7])
        lineTemp.append(str(widthTemp))
        x,y = calPoint(float(lineTemp[0]),float(lineTemp[1]),float(lineTemp[2]),float(lineTemp[3]),float(lineTemp[4]),float(lineTemp[5]),float(widthTemp)) 
        lineTemp.extend([str(x),str(y)])
        f3.write(' '.join(lineTemp)+'\n')		
f1.close()
f2.close()
f3.close()
f4.close()
