var mongoose = require ('mongoose');
var schema = mongoose.Schema({
    lat: {
        type: String
    },
    log: {
        type: String
    },
    description: {
        type: String
    },
    create_on: {
        type: Date, 
    },
    update_on: {
        type: Date,
        default: Date.now
    },
    archive1: {
        type: String
    },
    archive2: {
        type: String
    },
    archive3: {
        type: String
    },
    archive4: {
        type: String
    },
    archive5: {
        type: String
    },
    archive6: {
        type: String
    },
    archive7: {
        type: String
    }
});

module.exports = mongoose.model('Suspect', schema);