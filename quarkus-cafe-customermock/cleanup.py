#!/usr/bin/env python

import re

file_in = open("names", "r")
#file_out = open("names_ready, "w+")

for line in file_in:
    name = line.split()
    #str = name[0] + " " +  name[len(name)-1][0:1]
    #print(name[-1], name[len(name)-1][0,1])
    print('\"'+ name[0] + " " +  name[len(name)-1][0:1] +'\",')

