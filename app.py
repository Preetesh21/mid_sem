# Importing flask module in the project is mandatory
# An object of Flask class is our WSGI application.
# importing module
import logging
from unicodedata import name
from flask import Flask,g, jsonify,request
import json
import sqlite3
import datetime
import requests


def get_db(DATABASE):
    db = getattr(g, '_database', None)
    if db is None:
        db = g._database = sqlite3.connect(DATABASE,check_same_thread=False)
    return db

def logger_code(log_file):
    # Create and configure logger
    logging.basicConfig(filename=log_file,
                        format='%(asctime)s %(message)s',
                        filemode='w')
    # Creating an object
    logger = logging.getLogger()
    # Setting the threshold of logger to DEBUG
    logger.setLevel(logging.DEBUG)
    return logger

def json_helper(name_list):
    f=open("config.json")
    data=json.load(f)
    item_a=data[name_list[0]]
    item_b=data[name_list[1]]
    f.close()
    return item_a,item_b

async def send_message(destination_address,message):
    dictweSend={message:message}
    r= requests.post(destination_address,json=dictweSend)
    print(r)
    return r

async def add_message_logs(sqliteConnection,cursor,RouteId,EventType,EventTime):
    try:
        data_tuple = (RouteId,EventType,EventTime)
        cursor.execute("INSERT INTO message_logs(RouteID, EventType, EventTime) VALUES (?, ?, ?);", data_tuple)
        sqliteConnection.commit()
        print("Message added successfully \n")
    except:
        print("Something went wrong")

async def helper(db,cur,sender,message_type,message=None):
    routing_cursor = cur.execute('select * from routing where Sender = ? AND MessageType = ?',[sender,message_type])
    
    if routing_cursor is None:
        print('No such routing')
    else:
        routing=routing_cursor.fetchall()[0]
        destination_address=routing[3]
        print(destination_address) 
        route_id=routing[0]
        #print(sender, 'has the id', routing)
        await add_message_logs(db,cur,route_id,"Received",datetime.datetime.now())
        try:
        # send request to the destination address with message from sender
            await send_message(destination_address,message)
        except:
            print("Some issue")
        await add_message_logs(db,cur,route_id,"Sent",datetime.datetime.now())


def c_app(app):
    # Flask constructor takes the name of 
    # current module (__name__) as argument.
    # app = Flask(__name__)

    @app.route('/',methods=["GET"])
    async def base_function():
        return "Hello World"

    @app.route('/', methods=['POST'])
    async def hello_world():
        db_url,log_file=json_helper(['db_url','log_file'])
        db=get_db(db_url)
        print(db)
        logger=logger_code(log_file)
        # here get the sender,message and message_type from HTTP Request
        data=(request.json)
        print(data)
        print("*************************************************8")
        message=data['Message']['Body']
        sender=data['Message']['Sender'] 
        message_type=data['Message']['MessageType']
        print(message,sender,message_type)
        logger.info("Parsing done")
        cur = db.cursor()
        try:
            await helper(db,cur,sender,message_type,message)
        except:
            print("Some issues")
            logger.info("Certain Issues in Database Parsing")
        db.commit()
        db.close()
        logger.info("Done")
        return 'Hello World'


# main driver function
# if __name__ == '__main__':
#     host,port=json_helper(['host','port'])
#     app=c_app()
#     # run() method of Flask class runs the application 
#     # on the local development server.
#     app.run(host=host,port=port,debug=True)