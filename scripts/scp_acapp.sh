#! /bin/bash
./to_dev.sh

cd ../gobang-acapp

npm run build

cd /d/AcWing/AcApp/gobang-project/scripts/

./to_acapp.sh
./to_test.sh
scp /d/AcWing/AcApp/gobang-project/gobang-acapp/dist/js/*.js gobang:~/gobang/acapp/app.js
scp /d/AcWing/AcApp/gobang-project/gobang-acapp/dist/css/*.css gobang:~/gobang/acapp/app.css
