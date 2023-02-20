const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: { // AcApp只允许一个js文件
    optimization: {
      splitChunks: false
    }
  },
})
