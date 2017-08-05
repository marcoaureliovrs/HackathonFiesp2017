var mongoose = require ('mongoose');
var schema = mongoose.Schema({
	name: {
		type: String,
		required: true
	},
	email: {
		type: String,
		required: true,
		unique: true,
		trim: true,
		index: true
	},
	passwd: {
		type: String,
		required:true
	},
	cpf: {
		type: Number,
		required: true,
		unique: false
	},
	permision: {
		type: Number,
		default: 1
	},
	date_of_birth: {
		type: Date,
		required: false
	},
	updated_at: {
		type: Date,
		default: Date.now
	}

});

module.exports = mongoose.model('User', schema);