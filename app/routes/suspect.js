module.exports = function (app) {
	var api = app.api.suspect;

	app.route('/suspects')
		.get(api.list);

	app.route("/suspect")
		.delete(api.deleteSuspect)
		.post(api.add)
		.put(api.updateSuspect);
}