module.exports = function (app) {
	var api = app.api.construction;

	app.route('/constructions')
		.get(api.list);

	app.route("/construction")
		.delete(api.deleteConstruction)
		.post(api.add)
		.put(api.updateConstruction);
}