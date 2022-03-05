# Importing flask module in the project is mandatory
# An object of Flask class is our WSGI application.
# importing module

from flask import Flask,request

# Flask constructor takes the name of 
# current module (__name__) as argument.
app = Flask(__name__)
# receiving requests from server at the destination port.
@app.route('/', methods=['POST','GET'])
async def hello_world():
    data=request.data
    print(data)
    return 'Hello World'


# main driver function
if __name__ == '__main__':
    # run() method of Flask class runs the application 
    # on the local development server.
    app.run(port=8080,debug=True)