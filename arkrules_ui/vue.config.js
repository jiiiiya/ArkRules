const { defineConfig } = require('@vue/cli-service')
const path = require('path')

module.exports = defineConfig({
  transpileDependencies: true,
  outputDir: path.resolve(__dirname, '../arkrules/src/main/resources/static'),
  devServer: {
    proxy: {
      '/api': {
        target: 'http://172.16.10.44:38080',
        changeOrigin: true
      }
    }
  }
})
