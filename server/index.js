var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

connections = [];

// create a webserver
server.listen(8082, function () {
    console.log("Server is now running");
});

io.on('connection', function (socket) {
    connections.push(socket);
    console.log("Player Connected");
    console.log('connected: %s sockets', connections.length);
    socket.emit('socketID', {id: socket.id});
    socket.broadcast.emit('newPlayer', {id: socket.id});
    socket.on("pawnMoved", function (data){
        data.id =socket.id;
        socket.broadcast.emit("pawnMoved",data);
    });
    socket.on('disconnect', function () {
        connections.splice(connections.indexOf(socket), 1);
        console.log("player diconnected");
        console.log('disconnected: %s sockets', connections.length);
    });
});