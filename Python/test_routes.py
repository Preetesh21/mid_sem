# importing all the necessary modules
from pydoc import cli
from flask import Flask
import os, sys; sys.path.append(os.path.dirname(os.path.realpath(__file__)))
import xmltodict
from app import c_app,json_helper

# here I am testing the basic app object from flask library and its get functionality using a client
def test_base_route():
    app = Flask(__name__)
    c_app(app)
    client = app.test_client()
    url = '/'

    response = client.get(url)
    print(response.get_data)
    assert response.get_data() == b'Hello World'
    assert response.status_code == 200

# helper function to read the xml file which is the message
def read():
    with open('data.xml', 'r') as f:
        data = f.read()
    l=str(data)
    return l

# this is the test which successfully runs through most of the code. Here it creates a client which sends a request to the server which
# after its own configurations and processings passes it to the receiver and the receiver sends back the acknowledgment
def test_route():
    app = Flask(__name__)
    c_app(app)
    client = app.test_client()
    url = '/'

    xml=read()
    data=xmltodict.parse(xml)
    response = client.post(url, json=(data))
    #response = client.post(url)
    print(response.get_data)
    assert response.get_data() == b'Hello World'
    assert response.status_code == 200

# testing of a helper function used to parse json file
def test_json():
    
    host,port=json_helper(['host','port'])
    assert host!=None
    assert port!=None
    
