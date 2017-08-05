module.exports = function (app) {
	var api=app.api.auth;
	var apiUser = app.api.user;
	
	app.post('/users', apiUser.add);
	app.post('/auth', api.autentica);
	app.use('/*', api.verificaToken);
};