
var mongoose = require('mongoose');
var fileUpload = require('express-fileupload');
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

api.upload = function (req,res) {
	if (!req.files) {
		return res.status(400).send('No files were uploaded.');
	}
	// The name of the input field (i.e. "sampleFile") is used to retrieve the uploaded file 
	let sampleFile = req.files.sampleFile;
  	// Use the mv() method to place the file somewhere on your server 
	sampleFile.mv('../../public/test.jpg', function(err) {
		if (err) {
			return res.status(500).send(err);
		}
		res.send('File uploaded!');
	})
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
