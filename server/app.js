const express = require('express')

const app = express()


class Router {
    constructor(uri, android, web) {
        this.uri = uri
        this.android = android
        this.web = web
    }
}



app.get('/router', (req, res) => {
    const host = 'http://smilehacker.com'
    const index = new Router(host + '/user/(?<id>\\d+)',
        'TEST1', false)

    const r = {
        routers: [index],
        deploy_time: '2016-08-20',
        version: 1,
    }

    res.json(r)
})


app.listen(9999, function() {
    console.log('server start')
})
