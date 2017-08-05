var mongoose = require('mongoose');
var api = {};
var model = mongoose.model('Suspect');

api.list = function (req, res) {
	//TODO: Verificar o compartamento quando o campo _id não for fornecido na requisição
	if (req.usuario.permision=3) {
		model
		.find({})
		.then(function(suspects) {
			res.json(suspects);
			}, function(error) {
				console.log(error);
				res.status(500).json(error);
			})	
	} else {
		res.send({messagem: "Usuário sem permissão para realizar esta operação"})
		return res.status(401);
	}
	
}

api.add = function (req, res) {
	var suspect = req.body;
	model
		.create(suspect)
		.then(function(suspect) {
			res.json(suspect);
			}, function(error) {
				console.log(error);
				res.status(500).json(error);
			})
}

api.updateSuspect = function (req, res) {
	model
		.findByIdAndUpdate(req.body.id, req.body)
		.then(function (suspect) {
			res.json(suspect);
			}, function(error) {
				console.log(error);
				res.status(500).json(error);
			})
}

api.deleteSuspect = function (req, res) {
	model
		.remove({_id: req.body.id})
		.then(function() {
			res.sendStatus(204);
			}, function(error) {
				console.log(error);
				res.status(500).json(error);
			})	
}

module.exports = api;