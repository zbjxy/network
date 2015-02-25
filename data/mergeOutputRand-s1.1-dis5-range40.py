import random
widthRange = 40#this means the width range from -40 to 40. 
#-5 to -30 is the actual range we picked. 
speedCoefficient = 1.1
distance=59
#kill the first line from output.txt
#merge the 2 files together, taking the first output.txt's speed, and then assign a random width. 
#provide x1 y1 x2 y2 xc yc and width 
#return xn yn which is the point xc yc correspond to. 
#distance functions for the 2 points
def dis(x1,y1,x2,y2):
    import math
    return math.sqrt((x1-x2)**2+(y1-y2)**2)

#check if the point given x and y is in the rectangle
#this step is for range query. 
def inRectangle(x,y):
    x = float(x)
    y = float(y)
    if x<8400 or x>11600 or y<8400 or y>11600:
        print 'not in rectangle',x,y
        return False
    return True


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
    if y2==yc:
        if xc>x2:
            return xc,yc-width
        else:
            return xc,yc+width
    elif x2 == xc:
        if yc>y2:
            return xc+width,yc
        else:
            return xc-width,yc

    slopeL1 = (y2-yc)/(x2-xc)
    if y2<yc and x2>xc:
        if width>0:
            y = yc + math.sqrt(width**2/(1+1/slopeL1**2))
            x = xc + math.sqrt(width**2 - (y-yc)**2)
        else:
            y = yc - math.sqrt(width**2/(1+1/slopeL1**2))
            x = xc - math.sqrt(width**2 - (y-yc)**2)
    elif y2>yc and x2>xc:
        if width>0:
            y = yc + math.sqrt(width**2/(1+1/slopeL1**2))
            x = xc - math.sqrt(width**2 - (y-yc)**2)
        else:
            y = yc - math.sqrt(width**2/(1+1/slopeL1**2))
            x = xc + math.sqrt(width**2 - (y-yc)**2)
    elif y2<yc and x2<xc:
        if width>0:
            y = yc - math.sqrt(width**2/(1+1/slopeL1**2))
            x = xc - math.sqrt(width**2 - (y-yc)**2)
        else:
            y = yc + math.sqrt(width**2/(1+1/slopeL1**2))
            x = xc + math.sqrt(width**2 - (y-yc)**2)
    else:
        if width>0:
            y = yc - math.sqrt(width**2/(1+1/slopeL1**2))
            x = xc + math.sqrt(width**2 - (y-yc)**2)
        else:
            y = yc + math.sqrt(width**2/(1+1/slopeL1**2))
            x = xc - math.sqrt(width**2 - (y-yc)**2)
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


#random width generator.
#this generate a number thats -width -> -0.25 width
def nextWidthRand(prev,width):
    temp = random.random()
    return temp*2*widthRange - 1*widthRange

def correctLines(lineNum, line1, line2):
    try:
        assert line1[-2]==line2[2], '%r: assertion error, %r is not equal to %r'%(lineNum,line1[-2],line2[2])
        assert line1[-1] == line2[3], '%r: assertion error, %r is not equal to %r'%(lineNum,line1[-1],line2[3])
        return True
    except AssertionError:
        print "assertionError at "+str(lineNum)
        return False
#check for the same speed. If yes, then return true. If not, then return false. 
def sameSpeed(data1, data2):
    if len(data1)<8 or len(data2)<8:
        return False
    if round(float(data1[7]))==round(float(data2[7])):
        return True
    return False
#create sensor creates 1 sensor based on given x1y1x2y2 and speed. 
def createSensor(x1,y1,t1,x2,y2,t2,speed):
    
    x1 = float(x1)
    y1 = float(y1)
    x2 = float(x2)
    y2 = float(y2)
    t1 = float(t1)
    t2 = float(t2)
    speed = speedCoefficient*float(speed)
    
    probX = (x2+x1)/2
    probY = (y2+y1)/2
    tmin = t1+dis(x1,y1, probX, probY)/speed
    tmax = t2-dis(probX, probY, x2, y2)/speed
    t = 0
    mu,sigma = (tmin+tmax)/2,((tmin+tmax)/2-tmin)/2.5
    t = random.gauss(mu, sigma)
    return [str(x1),str(y1),str(t1),str(x2),str(y2),str(t2),str(probX),str(probY),str(t)]
#the first file format: 
# point, na, na, na, time, currentx, currenty, speed, nextx, nexty
# the second file format: 
# x1 y1 x2 y2 currentx currenty
# the new file format: 
#x1 y1 x2 y2 currentx currenty speed width alteredx alteredy.
fname1 = 'output.txt'
fname2 = 'output2.txt'
fname3 = 'finaloutput.txt'
fname4 = 'sensor.txt'
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
prevLine=[]
tempSensor=[]
differentLines = False
notInRec = 0
with open(fname2,'r') as f2:
    for line2 in f2:
        lineNum+=1
        lineTemp = []
        line1Temp = f1.readline().strip().split()
        line2Temp = line2.strip().split()
        while(not correctLines(lineNum,line1Temp, line2Temp)):
            differentLines = True
            line1Temp = f1.readline().strip().split()
            if not line1Temp:
                print 'no mapping for file1 last line. '
                import sys
                sys.exit(1)
        if differentLines:
            differentLines = False
            prevLine = []
            continue
        
        widthTemp = nextWidthRand(prev,widthRange)
        prev = widthTemp
        lineTemp.append(line1Temp[4])
        lineTemp.extend(line2Temp)
        lineTemp.append(line1Temp[7])
        lineTemp.append(str(widthTemp))
        x,y = calPoint(float(lineTemp[1]),float(lineTemp[2]),float(lineTemp[3]),float(lineTemp[4]),float(lineTemp[5]),float(lineTemp[6]),float(widthTemp)) 
        lineTemp.extend([str(x),str(y)])

        #add a check for the same speed. If it has the same speed, then record the two records in a new file. Otherwise, dont record those. 
        if sameSpeed(prevLine,lineTemp):
            tempDistance = dis(float(prevLine[5]),float(prevLine[6]),float(lineTemp[5]),float(lineTemp[6]))
            if tempDistance>1.01*float(lineTemp[7]) or tempDistance<0.99*float(lineTemp[7]):
                prevLine = []
                print 'distance mismatch at line Number '+str(lineNum)+"tempDistance: "+str(tempDistance)+" speed: "+lineTemp[7]
                continue
            if round(float(lineTemp[7]))!=distance:
                prevLine=[]
                continue
            tempSensor = createSensor(prevLine[5],prevLine[6],prevLine[0],lineTemp[5],lineTemp[6],lineTemp[0],lineTemp[7])
            tempSensor.extend([prevLine[1],prevLine[2],lineTemp[1],lineTemp[2],lineTemp[7]])
            tempSensor.extend([prevLine[9],prevLine[10],prevLine[8],lineTemp[9],lineTemp[10],lineTemp[8]])
            #check if the generated points are in the range of our rectangle. 
            if inRectangle(prevLine[9],prevLine[10]) and inRectangle(lineTemp[9],lineTemp[10]):
                f4.write(' '.join(tempSensor)+'\n')
            else:
                notInRec+=1
                print "not in rectangle ",notInRec
        prevLine = lineTemp
        f3.write(' '.join(lineTemp)+'\n')	
        
f1.close()
f2.close()
f3.close()
f4.close()
