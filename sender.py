import requests
import xmltodict
import json
#!/usr/bin/env python2
# -*- coding: utf-8 -*-
# Reading the data inside the xml
# file to a variable under the name
# data
def read():
    with open('data.xml', 'r') as f:
        data = f.read()
    l=str(data)
    return l

def json_helper(name_list):
    f=open("config.json")
    data=json.load(f)
    item_a=data[name_list[0]]
    item_b=data[name_list[1]]
    f.close()
    return item_a,item_b

def hello_world():
    xml=read()
    data=xmltodict.parse(xml)
    host,port=json_helper(['host','port'])
    url="http://"+host+":"+str(port)
    r = requests.post(url, json=data)
    print(r)
    
hello_world()