#kill the first line from output.txt
#merge the 2 files together, taking the first output.txt's speed, and then assign a random width. 
#the first file format: 
# point, na, na, na, time, currentx, currenty, speed, nextx, nexty
# the second file format: 
# x1 y1 x2 y2 currentx currenty
# the new file format: 
#x1 y1 x2 y2 currentx currenty speed width.
fname1 = 'output.txt'
fname2 = 'output2.txt'
fname3 = 'finaloutput.txt'
f1 = open(fname1,'r')
#f2 = open(fname2,'r')
f3 = open(fname3,'w')
f3.truncate()
#burn the first line because we dont need it. 
f1.readline()
lineNum=0
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
		lineTemp.extend(line2Temp)
		lineTemp.append(line1Temp[7])
		f3.write(' '.join(lineTemp)+'\n')		

f1.close()
f2.close()
f3.close()

