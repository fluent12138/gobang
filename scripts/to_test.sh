#! /bin/bash


find ../gobang-frontend/src/ -type f -path "*.js" | xargs sed -i 's#https://app4362.acapp.acwing.com.cn#http://localhost:10020#g'
find ../gobang-acapp/src/ -type f -path "*.js" | xargs sed -i 's#https://app4362.acapp.acwing.com.cn#http://localhost:10020#g'
find ../gobang-frontend/src/ -type f -path "*.vue" | xargs sed -i 's#wss://app4362.acapp.acwing.com.cn#ws://localhost:10020#g'
find ../gobang-acapp/src/ -type f -path "*.vue" | xargs sed -i 's#wss://app4362.acapp.acwing.com.cn#ws://localhost:10020#g'
find ../gobang-frontend/src/ -type f -path "*.vue" | xargs sed -i 's#https://app4362.acapp.acwing.com.cn#http://localhost:10020#g'
find ../gobang-acapp/src/ -type f -path "*.vue" | xargs sed -i 's#https://app4362.acapp.acwing.com.cn#http://localhost:10020#g'
find ../gobang-backend/src/ -type f -path "*.yml" | xargs sed -i 's#远程数据库地址#localhost:3306#g'

