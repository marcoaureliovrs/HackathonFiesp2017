const supertest = require('supertest')
const app = require('../index')
var agent = supertest.agent(app)

describe("Upload", function () {
    
	it("Realizando Upload", function(done) {
		agent
            .post("/upload")
            .attach('sampleFile', '/test.jpg')
			.expect(200)
			.end(function(err) {
    			done()
			})
	})

})