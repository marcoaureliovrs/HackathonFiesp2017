module.exports = function (app) {
	var api = app.api.user;

	app.route('/users')
		.get(api.list);

	app.route("/user")
		.get(api.readUser)
		.delete(api.deleteUser)
		.put(api.updateUser);
}