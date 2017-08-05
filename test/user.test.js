const supertest = require('supertest')
const app = require('../index')
var agent = supertest.agent(app)
let keyJwt
let detailsUser

var allUserTest = {
	"name": "Usuário de Teste",
	"email": "usuario@exemplo.com.br",
	"passwd": "supertest123",
	"cpf": 321,
	"date_of_birth": null
}


var userTest = {"email":"usuario@exemplo.com.br", "passwd":"supertest123"}

describe("Rotina de testes para usuários do sistema", function () {
    
	it("Cadastro de usuário", function(done) {
		agent
			.post("/users")
			.send(allUserTest)
			.expect(200)
			.end(function(err) {
    			done()
			})
	})

    it("Autenticação e recuperação de token", function (done) {
        agent
            .post("/auth")
            .send(userTest)
            .expect(200)
            .expect(function(res) {
                keyJwt = res.header['x-access-token']
                console.log(keyJwt);
            })
            .end(function(err) {
      			done()
  			})
	})

	it ("Consulta dos próprios dados", function(done) {
		agent
			.get("/user")
			.set('x-access-token', keyJwt)
			.expect(200)
			.expect('Content-Type', /json/)
			.expect(function(res) {
				detailsUser = res.body
				console.log(detailsUser.name)
			})
			.end(function(err) {
      			done()
  			})

	})

	it ("Atualização dos dados cadastrais", function(done) {
		delete detailsUser._id
		delete detailsUser.__v
		detailsUser.name = "Update Ok"
		console.log(detailsUser)

		agent
			.put("/user")
			.set('x-access-token', keyJwt)
			.send(detailsUser)
			.expect(200)
			.expect(function(res) {
				console.log(res.body);
			})
			.end(function(err) {
      			done()
  			})
	})

	it ("Deleção do cadastro", function(done) {
		agent
			.delete("/user")
			.set('x-access-token', keyJwt)
			.expect(204)
			.end(function(err) {
      			done()
  			})
	})


})