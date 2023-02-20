#! /bin/bash


# find ../dist/js/ -type f -path "*.js" | xargs sed -i 's:被替换的内容:新的内容:g'
find ../gobang-acapp/dist/ -type f -path "*.js" | xargs sed -i 's#(function(){var __web#const myfunc = (function(myappid, AcWingOS){var __web#g'
find ../gobang-acapp/dist/ -type f -path "*.js" | xargs sed -i 's:.mount("#app"):.mount(myappid):g'
find ../gobang-acapp/dist/ -type f -path "*.js" | xargs sed -i 's#"AcWingOS"#AcWingOS#g'
find ../gobang-acapp/dist/ -type f -path "*.js" | xargs sed -i 's#)}()})()#)}()})#g'
echo "
export class Game {
    constructor(id, AcWingOS) {
        myfunc('#' + id, AcWingOS);
    }
}
" >> ../gobang-acapp/dist/js/*.js

