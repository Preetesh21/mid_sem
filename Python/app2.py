# importing libraries
from flask import Flask
from app import c_app
import json
# using only the INTERFACE provided by the library and following SOLID Principle
app = Flask(__name__)
# responsible for getting the configuration information from config.json file
def json_helper(name_list):
    f=open("config.json")
    data=json.load(f)
    item_a=data[name_list[0]]
    item_b=data[name_list[1]]
    f.close()
    return item_a,item_b

# passing the instance of the app object to the supporter module again displaying the usage of the SOLID Principle.
c_app(app)

if __name__ == '__main__':
    
    host,port=json_helper(['host','port'])
    print(host,port)
    # run() method of Flask class runs the application 
    # on the local development server.
    app.run(host=host,port=port,debug=True)