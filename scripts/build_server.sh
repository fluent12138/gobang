#! /bin/bash
./to_dev.sh
cd ../gobang-backend

mvn clean
mvn package
cd /d/AcWing/AcApp/gobang-project/gobang-backend/target/

cd /d/AcWing/AcApp/gobang-project/scripts
./to_test.sh
