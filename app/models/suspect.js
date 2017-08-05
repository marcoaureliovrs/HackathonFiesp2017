var mongoose = require ('mongoose');
var schema = mongoose.Schema({
    geo_location: [{
        lat: String,
        lug: String
    }],
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
    attach: [{
        archive1: String,
        archive2: String,
        archive3: String,
        archive4: String,
        archive5: String,
        archive6: String,
        archive7: String
    }]

});

module.exports = mongoose.model('Suspect', schema);