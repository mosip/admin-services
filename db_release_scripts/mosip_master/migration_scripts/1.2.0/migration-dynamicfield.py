#!/usr/bin/python3

import psycopg2
import json

conn = psycopg2.connect(database="mosip_master", user = "postgres", password = "mosip123", host = "qa3.mosip.net", port = "30090")

print("Opened database successfully")

cur = conn.cursor()

#Backup existing dynamic_field table
cur.execute('ALTER TABLE master.dynamic_field RENAME TO dynamic_field_migr_bkp;')

print("Renamed dynamic_field table to dynamic_field_migr_bkp")

#Create dynamic_field table
cur.execute('''CREATE TABLE master.dynamic_field(
	id character varying(36) NOT NULL,
	name character varying(36) NOT NULL,
	description character varying(256),
	data_type character varying(16),
	value_json character varying,
	lang_code character varying(3) NOT NULL,
	is_active boolean NOT NULL,
	cr_by character varying(256) NOT NULL,
	cr_dtimes timestamp NOT NULL,
	upd_by character varying(256),
	upd_dtimes timestamp,
	is_deleted boolean DEFAULT FALSE,
	del_dtimes timestamp,
	CONSTRAINT pk_dynamic_id PRIMARY KEY (id));''')

print("created table dynamic_field")

#Query all the records from backup table
cur.execute('select * from master.dynamic_field_migr_bkp')
rows = cur.fetchall()

print("Data fetched from backup table")

id = 1000
stmt = 'insert into dynamic_field values (%s,%s,%s,%s,%s,%s,True,%s,now(),NULL,NULL,False,NULL);'
#Iterate through each row and create new insert statements
for row in rows:
 name = row[1]
 desc = row[2]
 data_type = row[3]
 lang = row[5]
 cr_by = row[7]
 cr_dtimes = row[8]
 values = json.loads(row[4])
 for val in values:
   id = id + 1
   vmap = {'code' : val['code'], 'value': val['value']}
   #Execute the insert statement
   cur.execute(stmt, (str(id), name, desc, data_type, json.dumps(vmap), lang, cr_by))


# Commit and close connection
conn.commit()
print("Closing the database connection")
conn.close()
