import matplotlib.pyplot as plt
def showPlot(xList,yList):
    plt.plot(xList, yList, 'b^')
    plt.show()
def median(numList):
    tempList = sorted(numList)
    if(len(numList)%2==0):
        return (tempList[len(numList)/2-1]+tempList[len(numList)/2])/2
    else:
        return tempList[len(numList)/2]
def average(numList):
    return sum(numList)/len(numList)

def removeOutlier(radiusList):
    '''
    remove the outlier in a dataset, 
    determined by mean+-2std
    '''
    import numpy as np
    std = np.std(radiusList)
    mean =np.mean(radiusList)
    stdCoefficient = 1.9
    resultList = []
    for radius in radiusList:
        if radius> stdCoefficient*std+mean or radius<mean-stdCoefficient*std:
            continue
        resultList.append(radius)

    return resultList

def dis(x1,y1,x2,y2):
        import math
        return math.sqrt((x1-x2)**2+(y1-y2)**2)

f = open('sensor2000_rand-s1.1-dis5.txt','r')
lineNum=0
averageX=0
averageY=0
meanR=0
xList = []
yList = []
lines = f.readlines()
f.close()
for line in lines:
    lineNum+=1
    data = line.strip().split()
    averageX+=float(data[0])
    averageX+=float(data[3])
    averageY+=float(data[1])
    averageY+=float(data[4])
    xList.append(float(data[0]))
    xList.append(float(data[3]))
    yList.append(-1*float(data[1]))
    yList.append(-1*float(data[4]))

averageX = averageX/(2*lineNum)
averageY = averageY/(2*lineNum)
radiuses=[]

for line in lines:
    data = line.strip().split()
    radiuses.append(dis(float(data[0]),float(data[1]),averageX,averageY))
    radiuses.append(dis(float(data[3]),float(data[4]),averageX,averageY))
    radiuses = removeOutlier(radiuses)
    
print(str(averageX)+' '+str(averageY)+' '+str(median(radiuses))+' length: '+str(len(radiuses)))
print(str(averageX)+' '+str(averageY)+' '+str(average(radiuses))+' length: '+str(len(radiuses)))
circle1=plt.Circle((averageX,-1*averageY),radius=median(radiuses),color='g')
circle2=plt.Circle((averageX,-1*averageY),radius = average(radiuses),color='r')
plt.gca().add_patch(circle1)
plt.gca().add_patch(circle2)
showPlot(xList,yList)
