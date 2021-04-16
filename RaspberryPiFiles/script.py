#! /usr/bit/python3.7

from datetime import timedelta, datetime
import csv
import uuid
import time
from io import StringIO
import threading
import schedule
import boto3

#############

tableName = "honorsS1717689"
device = "device1"
dumpFile = "/home/pi/dump-01.csv"

continent = "Europe"
country = "UK"
state = "Scotland"
city = "Glasgow"
postCode = "G4 0BA"
street = "Cowcaddens Rd"
number = "1"

keyCode = continent + "#" + country + "#" + state + "#" + city + "#" + postCode + "#" + street + "#" + number

glat = "55.866476"
glong = "-4.250263"

maxCapacity = 50
name = "Glasgow Caledonian University"

#############

dbResource = boto3.resource('dynamodb')
table = dbResource.Table(tableName)

#############

def readCSV(file):
    f = open(file, 'r')
    csvdata = (f.read()).split("Station MAC, First time seen, Last time seen, Power, # packets, BSSID, Probed ESSIDs")
    accessPoints = csvdata[0]
    users = csvdata[1]
    getBatchUsers(users)

def getBatchUsers(users):
    f = StringIO(users)
    timeNow = datetime.now() - timedelta(seconds=33)
    updateTime = datetime.now()
    spamreader = csv.reader(f, delimiter=',')
    with table.batch_writer() as batch:
        for row in spamreader:
            if len(row) < 7:
                print("Not enoguh columns")
            else:
                if datetime.strptime(row[2].strip(), '%Y-%m-%d %H:%M:%S') > timeNow:
                    mac = updateTime.strftime('%Y-%m-%d %H:%M:%S') + "#" + device + "#" + row[0].strip()
                    batch.put_item(
                        Item={
                            'location' :  keyCode,
                            'device # MAC # UUID' : mac,
                            'time' : row[2].strip(),
                            'power' : row[3].strip(),
                            'BSSID' : row[5].strip()
                        }
                    )

def updateLocationDetails():
    table.put_item(
        Item={
            'location' :  "AllLocations",
            'device # MAC # UUID' : keyCode,
            'Glat' : glat,
            'Glong' : glong,
            'maxCapacity' : maxCapacity,
            'name' : name
            }
        )

updateLocationDetails()

schedule.every().minute.at(":10").do(readCSV, dumpFile)
schedule.every().minute.at(":40").do(readCSV, dumpFile)

while True:
    schedule.run_pending()
    time.sleep(1)

