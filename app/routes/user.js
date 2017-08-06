module.exports = function (app) {
	var api = app.api.user;
	
	app.route('/geolocation').post(api.search);
	
	app.route('/users')
		.get(api.list);

	app.route("/user")
		.get(api.readUser)
		.delete(api.deleteUser)
		.put(api.updateUser);
}