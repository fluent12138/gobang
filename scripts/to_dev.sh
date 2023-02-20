#! /bin/bash


find ../gobang-frontend/src/ -type f -path "*.js" | xargs sed -i 's#http://localhost:10020#https://app4362.acapp.acwing.com.cn#g'
find ../gobang-acapp/src/ -type f -path "*.js" | xargs sed -i 's#http://localhost:10020#https://app4362.acapp.acwing.com.cn#g'
find ../gobang-frontend/src/ -type f -path "*.vue" | xargs sed -i 's#ws://localhost:10020#wss://app4362.acapp.acwing.com.cn#g'
find ../gobang-acapp/src/ -type f -path "*.vue" | xargs sed -i 's#ws://localhost:10020#wss://app4362.acapp.acwing.com.cn#g'
find ../gobang-frontend/src/ -type f -path "*.vue" | xargs sed -i 's#http://localhost:10020#https://app4362.acapp.acwing.com.cn#g'
find ../gobang-acapp/src/ -type f -path "*.vue" | xargs sed -i 's#http://localhost:10020#https://app4362.acapp.acwing.com.cn#g'
find ../gobang-backend/src/ -type f -path "*.yml" | xargs sed -i 's#localhost:3306#远程mysql服务地址#g'


