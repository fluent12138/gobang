#! /bin/bash

./to_dev.sh

cd /d/AcWing/AcApp/gobang-project/gobang-frontend/
npm run build

scp -r dist : gobang:~

ssh gobang 'mv dist web && cd ./gobang && rm web -r&& cd ~ && mv web ./gobang'

cd /d/AcWing/AcApp/gobang-project/scripts/
./to_test.sh


