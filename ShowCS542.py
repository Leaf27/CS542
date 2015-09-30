
f=open("cs542.db",'r')
lines=f.readlines()
x=0
for line in lines:
	if(x==0): print (line.rstrip() +"// Amount of key-value paris")
	elif(x%2==1): print line.rstrip()," //This is the ",x, 'th key'
	else: print len(line)," //This is the ",(x+1)/2,'th value'
	x=x+1
f.close()


