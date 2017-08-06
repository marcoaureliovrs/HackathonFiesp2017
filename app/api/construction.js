
var mongoose = require('mongoose');
var api = {};
var model = mongoose.model('Construction');

api.list = function (req, res) {
	//TODO: Verificar o compartamento quando o campo _id não for fornecido na requisição
		model
		.find({})
		.then(function(constructions) {
			res.json(constructions);
			}, function(error) {
				console.log(error);
				res.status(500).json(error);
			})	
}

api.add = function (req, res) {
	var construction = req.body;
	console.log(req.body);
	model
		.create(construction)
		.then(function(construction) {
			console.log('Construção registrada');
			console.log(construction);
			res.json({"result":"Ok"});
			}, function(error) {
				console.log(error);
				res.status(500).json(error);
			})
}

api.updateConstruction = function (req, res) {
	model
		.findByIdAndUpdate(req.body.id, req.body)
		.then(function (construction) {
			res.json(construction);
			}, function(error) {
				console.log(error);
				res.status(500).json(error);
			})
}
        
api.deleteConstruction = function (req, res) {
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
