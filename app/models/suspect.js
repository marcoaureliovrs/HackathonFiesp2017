var mongoose = require ('mongoose');
var schema = mongoose.Schema({
    geo_location: [{
        lat: Number,
        lug: Number
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
        archive1: Stirng,
        archive2: Stirng,
        archive3: Stirng,
        archive4: Stirng,
        archive5: Stirng,
        archive6: Stirng,
        archive7: Stirng
    }]

});

module.exports = mongoose.model('Suspect', schema);