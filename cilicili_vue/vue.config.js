const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true
}),
module.exports = {
  lintOnSave: false,
  devServer: {
    port: 5050,
    open: true
  }
}