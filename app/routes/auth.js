module.exports = function (app) {
	var api=app.api.auth;
	var apiUser = app.api.user;
	var apiWebhook = app.api.webhook;

	app.get('/webhook', apiWebhook.verify);
	app.post('/webhook', apiWebhook.sendRecive);
	app.post('/users', apiUser.add);
	app.post('/auth', api.autentica);
	app.use('/*', api.verificaToken);
};