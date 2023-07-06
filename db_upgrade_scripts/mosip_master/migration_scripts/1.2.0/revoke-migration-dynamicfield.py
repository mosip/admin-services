#!/usr/bin/python3

import psycopg2
import json
import sys

conn = psycopg2.connect(database="mosip_master", user = sys.argv[1], password = sys.argv[2], host = sys.argv[3], port = sys.argv[4])

print("Opened database successfully")

cur = conn.cursor()

#DROP existing dynamic_field table
cur.execute('DROP TABLE IF EXISTS master.dynamic_field;')

#rollback backup table
cur.execute('ALTER TABLE master.dynamic_field_migr_bkp RENAME TO dynamic_field;')

print("Renamed dynamic_field_migr_bkp to dynamic_field")

# Commit and close connection
conn.commit()

print("Closing the database connection")
conn.close()
