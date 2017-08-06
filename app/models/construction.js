var mongoose = require ('mongoose');
var schema = mongoose.Schema({
    name_company: {
        type: String
    },
    street: {
        type: String
    },
    deadline: {
        type: Date
    },
    initial_term: {
        type: Date
    }
});

module.exports = mongoose.model('Construction', schema);