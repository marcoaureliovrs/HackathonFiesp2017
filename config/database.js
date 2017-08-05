module.exports = function(uri) {

	var mongoose = require ('mongoose');

	mongoose.connect('mongodb://' + uri);

	mongoose.connection.on('connected', function() {
		console.log('conectado ao mongodb');
	});

	mongoose.connection.on('error', function(error) {
		console.log('Erro na conexão: '+ error)
	});

	mongoose.connection.on('disconnected', function() {
		console.log('Desconectado do mongodb');
	});

	process.on('SIGINT', function() {
		mongoose.connection.close(function() {
			console.log('Conexão fechada pelo término da aplicação');
			process.exit(0);
		});
	});

};