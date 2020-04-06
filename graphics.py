# Import "pyplot"
import matplotlib.pyplot as plt
import numpy as np
# Import "math functions"
import math 
# Import "staticts"
import statistics as stat 

import seaborn as sns

import scipy.stats

from sklearn import linear_model
from sklearn.metrics import mean_squared_error, r2_score

#Global variables
PATH_GENERAL = ""
ANIMATION_PATH = "ovito"
DATA_PATH = "dataTCACUM5.txt"
DATA_PATH_TC = "dataTC5.txt"
BIG_PARTICLE_ID = 0
PXIndex =1
PYIndex =2

x2y2=[]
tc =[]
PX = []
PY = []
def bigParticlePath(path):
	BPPath = []
	file = open(path,"r")
	lines = file.readlines()
	for line in lines:
		atributes = line.split()
		if len(atributes) > 2:
			if int(atributes[0]) == BIG_PARTICLE_ID:
				BPPath.append([float(atributes[1]),float(atributes[2])])
	return BPPath
	pass
def particlePath(path,id):
	BPPath = []
	file = open(path,"r")
	lines = file.readlines()
	for line in lines:
		atributes = line.split()
		if len(atributes) > 2:
			if int(atributes[0]) == id:
				BPPath.append([float(atributes[1]),float(atributes[2])])
	return BPPath
	pass

def crashTimes():
	file = open(DATA_PATH,"r")
	lines = file.readlines()
	for line in lines:
		atributes = line.split()
		tc.append(atributes[0])
	pass

def getTBC():
	result = []
	file = open(DATA_PATH_TC,"r")
	lines = file.readlines()
	for line in lines:
		param = line.split()
		result.append(float(param[0]))

	return result
	pass

def ultimoTercio(path):
	modules =[]
	file = open(PATH_GENERAL+"|v|"+path+".txt")
	lines = file.readlines()
	for line in lines:
		atr = line.split()
		modules.append(float(atr[0]))
		pass
	return modules
	pass

def v0(path):
	modules = []
	file = open(PATH_GENERAL+"|v0|"+path+".txt")
	lines = file.readlines()
	for line in lines:
		atr = line.split()
		modules.append(float(atr[0]))
		pass
	return modules
	pass

def promediarLista(lista):
    sum=0.0
    for i in range(0,len(lista)):
        sum=sum+lista[i]
 
    return sum/len(lista)


def f1(x,c):
    return x*c-0.0007

x=[]
y=[]

plt.figure(1)
plt.title("movimiento a distintas temperaturas\n")
for i in range(1,5):
	points = bigParticlePath(ANIMATION_PATH+str(5*i)+".txt")
	x=[]
	y=[]
	for point in points:
		x.append(point[0])
		y.append(point[1])
		pass
	plt.plot(x,y, label ="<K>= "+str((4E-4-0.5E-4)*i/2)+" J")
	pass
plt.legend()


crashTimes()

tbc = getTBC()
plt.figure(2)
plt.ylabel("Densidad de probabilidad\n")
plt.xlabel("Tiempo entre coliciones en ms")
tiempo=[]
for t in tbc:
	tiempo.append(t*1000)
	pass

sns.distplot(tiempo, hist=True,
	kde=False,norm_hist = True,
    bins=60, hist_kws={'edgecolor':'white'},
    color = "blue")
tt = 0
for t in tbc:
	tt = tt+t
print("Tiempo total = "+str(tt))
print("Cantidad de choques = "+ str(len(tbc)))
print("Frecuencia= "+str(len(tbc)/tt))
print("Promedio = "+ str(stat.mean(tbc)))

#sns.distplot(tbc, hist=True, kde=True, 
#             bins=int(180/5), color = 'darkblue', 
#             hist_kws={'edgecolor':'black'},
#             kde_kws = {'shade': True, 'linewidth': 3})

#<K> with i var

i = 1
plt.figure(4)
plt.title("|v| en ultimo tercio\n")
plt.ylabel("Densidad de probabilidad")
plt.xlabel("Modulo de la velocidad en m/s")
module = ultimoTercio(str(i*5))
sns.distplot(module, hist=True,
	kde=False, bins=60,
	hist_kws={'edgecolor':'white'},
	color = "blue", norm_hist = True)

plt.figure(5)
plt.title("|v0|\n")
plt.ylabel("Densidad de Probabilidad")
plt.xlabel("Modulo de la velocidad en m/s")
module = v0(str(i*5))
sns.distplot(module, hist=True, kde=False, norm_hist = True, 
        bins=20, hist_kws={'edgecolor':'white'},
    	color = "blue")

#8 puntos
plt.figure(6)
plt.title("10 simulaciones N=50 v = 0.1m/s. Particula grande. Desplazamiento cuadratico medio\n")
plt.xlabel("Tiempo en segundos")
plt.ylabel("Desplazamiento cuadratico medio [m2]")
events = [[],[],[],[],[],[],[],[]]

minmo = float('inf')
for i in range(0,10):
	points = bigParticlePath(ANIMATION_PATH+"D"+str(i)+".txt")
	if(len(points)< minmo):
		minmo = len(points)
	pass

medio = int(minmo/2)
for i in range(0,10):
	points = bigParticlePath(ANIMATION_PATH+"D"+str(i)+".txt")
	#medio = int(len(points)/2)
	for j in range(0,8):
		points[medio+j]
		events[j]
		events[j].append(math.pow(points[medio+j][0]-points[medio][0],2)+math.pow(points[medio+j][1]-points[medio][1],2))
		pass
	pass
y = []
x = []
clock = 20
u = medio * clock
for data in events:
	u+=1*clock
	x.append(u)
	y.append(stat.mean(data))
	plt.errorbar(u,stat.mean(data), yerr=stat.stdev(data), fmt ='o',uplims=True, lolims=True )

x_train = np.array(x)
x_train = x_train.reshape(-1,1)
y_train =np.array(y)

regr = linear_model.LinearRegression()
# training
regr.fit(x_train,y_train)

# predict
y_pred = regr.predict(x_train)
#coeficioente
print('Coefficients: \n', regr.coef_)
print('Variance score: %.2f' % r2_score(y_train, y_pred))
plt.plot(x,x*regr.coef_+regr.intercept_)

plt.figure(7)
plt.xlabel("c (pendiente)")
plt.ylabel("E(c) (error cuadratico)")
min = float('inf')
c = -2.25732292e-05
p = c
dist = 0
errors = []
xc=[]
for i in range(1,10):
    xc.append(p)
    dist = 0
    for j in range(0, len(y)-1):
        dist+=  math.pow(y[j]-f1(x[j],p),2)
    if dist < min:
        min = dist
        c = p
    errors.append(dist)
    p += 1e-05
    print(p)

plt.plot(xc,errors)


plt.figure(8)
plt.title("10 simulaciones N=50 v = 0.1m/s. Particula chica id=1. Desplazamiento cuadratico medio\n")
plt.xlabel("Tiempo en segundos")
plt.ylabel("Desplazamiento cuadratico medio [m2]")
events = [[],[],[],[],[],[],[],[]]

minmo = float('inf')
for i in range(0,10):
	points = particlePath(ANIMATION_PATH+"D"+str(i)+".txt",1)
	if(len(points)< minmo):
		minmo = len(points)
	pass

medio = int(minmo/2)
for i in range(0,10):
	points = particlePath(ANIMATION_PATH+"D"+str(i)+".txt",1)
	#medio = int(len(points)/2)
	for j in range(0,8):
		points[medio+j]
		events[j]
		events[j].append(math.pow(points[medio+j][0]-points[medio][0],2)+math.pow(points[medio+j][1]-points[medio][1]	,2))
		pass
	pass
y = []
x = []
u = medio*20
clock = 20
for data in events:
	u+=1*20
	x.append(u)
	y.append(stat.mean(data))
	plt.errorbar(u,stat.mean(data), yerr=stat.stdev(data), fmt ='o',uplims=True, lolims=True )

x_train = np.array(x)
x_train = x_train.reshape(-1,1)
y_train =np.array(y)

regr = linear_model.LinearRegression()
# training
regr.fit(x_train,y_train)

# predict
y_pred = regr.predict(x_train)
#coeficioente
print('Coefficients: \n', regr.coef_)
print('Variance score: %.2f' % r2_score(y_train, y_pred))
plt.plot(x,x*regr.coef_+regr.intercept_)


plt.figure(9)
plt.xlabel("c (pendiente)")
plt.ylabel("E(c) (error cuadratico)")
min = float('inf')
c = -0.0002
p = c
dist = 0
errors = []
xc=[]
for i in range(1,10):
    xc.append(p)
    dist = 0
    for j in range(0, len(y)-1):
        dist+=  math.pow(y[j]-f1(x[j],p),2)
    if dist < min:
        min = dist
        c = p
    errors.append(dist)
    p += 0.0001
    print(p)


plt.plot(xc,errors)

plt.show()

