#!/bin/bash
cd /home/ec2-user
aws s3 cp s3://film-store-be/filmstore-backend-0.0.1-SNAPSHOT.jar .
java -jar filmstore-backend-0.0.1-SNAPSHOT.jar