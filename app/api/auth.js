module.exports = function (app) {
	var mongoose = require('mongoose');
	var jwt = require('jsonwebtoken');
	var api = {};
	var model = mongoose.model('User');

	api.autentica = function (req, res) {
		model
			.findOne({email: req.body.email, passwd: req.body.passwd})
			.select('email _id')
			.then(function(user) {
				if (!user) {
					console.log('Login e senha inválidos');
					res.sendStatus(401);
				} else {
					console.log(user);
					var token = jwt.sign({email: user.email, _id: user._id}, app.get('secret'), {
						expiresIn:84500
					});
					console.log('token criado e sendo enviado no header de resposta');
					res.set('x-access-token', token);
					res.end();
				}
			}, function(error) {
				console.log('Login e senha inválidos devido ao erro:');
				console.log(error);
				return res.sendStatus(401);
		});
	};

	api.verificaToken = function (req, res, next) {
		var token = req.headers['x-access-token'];
		if (token) {
			console.log('Verificando o token...');
			jwt.verify(token, app.get('secret'), function(err, decoded) {
				if(err) {
					console.log('Token rejeitado');
					return res.sendStatus(401);
				}
				req.usuario = decoded;
				next();	
			});
		} else {
			console.log('Token não enviado');
			return res.sendStatus(401);
		}
	};

	return api;
};