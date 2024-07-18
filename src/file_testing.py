import csv 
import random as rand
from io import StringIO
import os
from py4j.java_gateway import JavaGateway


dir = "..\\" 

with open(os.path.join(dir, "Test_file.csv"), 'w') as file:
    writer = csv.writer(file, lineterminator='\n')
    writer.writerow(["X", "Y"])
    for i in range(0,10):
        writer.writerow([rand.randint(0,100), rand.randint(0,100)])
    
