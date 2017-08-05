var mongoose = require('mongoose');
var api = {};
var model = mongoose.model('User');

api.list = function (req, res) {
		model
		.find({})
		.then(function(users) {
			res.json(users);
		}, function(error) {
			console.log(error);
			res.status(500).json(error);
		})
}

api.readUser = function (req, res) {
		model
		.findOne({"email": req.usuario.email})
		.then(function(user) {
			res.json(user);
		}, function(error) {
			console.log(error);
			res.status(500).json(error);
		})
}

api.add = function (req, res) {
	var user = req.body;
	console.log(user);
	model
		.create(user)
		.then(function(user) {
			res.json(user);
		}, function(error) {
			console.log(error);
			res.status(500).json(error);
		})
}

api.updateUser = function (req, res) {
	if (req.usuario.email == req.body.email) {
		model
			.findByIdAndUpdate(req.usuario._id, req.body)
			.then(function (user) {
				res.json(user);
				}, function(error) {
					console.log(error);
					res.status(500).json(error);
				}) 
	} else {
		res.send({messagem: "Usuário sem permissão para realizar esta operação"})
		return res.status(401);
	}
	
}

api.deleteUser = function (req, res) {
		model
		.remove({email: req.usuario.email})
		.then(function() {
			res.sendStatus(204);
		}, function(error) {
			console.log(error);
			res.status(500).json(error);
		})

}

module.exports = api;