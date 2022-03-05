from pydoc import cli
from flask import Flask
import os, sys; sys.path.append(os.path.dirname(os.path.realpath(__file__)))
import xmltodict
from app import c_app,json_helper

def test_base_route():
    app = Flask(__name__)
    c_app(app)
    client = app.test_client()
    url = '/'

    response = client.get(url)
    print(response.get_data)
    assert response.get_data() == b'Hello World'
    assert response.status_code == 200

def read():
    with open('data.xml', 'r') as f:
        data = f.read()
    l=str(data)
    return l


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

def test_json():
    
    host,port=json_helper(['host','port'])
    assert host!=None
    assert port!=None
    
