import random
#kill the first line from output.txt
#merge the 2 files together, taking the first output.txt's speed, and then assign a random width. 






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

#the first file format: 
# point, na, na, na, time, currentx, currenty, speed, nextx, nexty
# the second file format: 
# x1 y1 x2 y2 currentx currenty
# the new file format: 
#x1 y1 x2 y2 currentx currenty speed width.
fname1 = 'output.txt'
fname2 = 'output2.txt'
fname3 = 'finaloutput.txt'
widthRange = 5#this means range from -5 to 5. 
f1 = open(fname1,'r')
#f2 = open(fname2,'r')
f3 = open(fname3,'w')
f3.truncate()
#burn the first line because we dont need it. 
f1.readline()
lineNum=0
prev=0
with open(fname2,'r') as f2:
	for line2 in f2:
		lineNum+=1
		lineTemp = []
		line1Temp = f1.readline().strip().split()
		line2Temp = line2.strip().split()
		try:
			assert line1Temp[-2]==line2Temp[2], '%r: assertion error, %r is not equal to %r'%(lineNum,line1Temp[-2],line2Temp[2])
			assert line1Temp[-1] == line2Temp[3], '%r: assertion error, %r is not equal to %r'%(lineNum,line1Temp[-1],line2Temp[3])
		except AssertionError:
			print "assertionError at "+str(lineNum)
			line1Temp = f1.readline().strip().split()
		widthTemp = nextWidth(prev,widthRange)
		prev = widthTemp
		lineTemp.extend(line2Temp)
		lineTemp.append(line1Temp[7])
		lineTemp.append(str(widthTemp))
		f3.write(' '.join(lineTemp)+'\n')		
f1.close()
f2.close()
f3.close()

