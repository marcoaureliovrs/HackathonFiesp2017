var geolib = require('geolib');
var geocoder = require('geocoder');
var mongoose = require('mongoose');
var api = {};
var model = mongoose.model('User');

var modelContru = mongoose.model('Construction');


var result;

api.search = function(req, res) {
   modelContru
		.find({})
		.then(function(constructs) {
			geocoder.geocode(constructs.street, function (err, data) {
        		result = data.results;
    		});
    		//res.json(constructs);
    		console.log(result);
    		//res.json(result);
		}, function(error) {
			console.log(error);
			res.status(500).json(error);
		})
		
		console.log(req.body.lat);
		console.log(req.body.lng);
   
   result = geolib.getDistance(
    	{latitude: req.body.lat, longitude: req.body.lng},
    	{latitude: "51° 31' N", longitude: "7° 28' E"}
	);
	
    if(result >100) {
    	res.json({"result":"ok", "address":"Av Paulista, 1313"});
    } else {
    	res.json({"result":""});
    }
}

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