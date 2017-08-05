module.exports = function (app) {
	var api = app.api.suspect;

	app.route('/suspects')
		.get(api.list);

	app.route("/suspect")
		.get(api.readSuspect)
		.delete(api.deleteSuspect)
		.put(api.updateSuspect);
}