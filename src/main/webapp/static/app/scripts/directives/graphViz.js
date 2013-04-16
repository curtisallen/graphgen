'use strict';
angular.module('graphApp').directive('graphView', function() {

	// constants
	var margin = 20,
		width = 960,
		height = 500 - 0.5 - margin,
		color = d3.scale.category20(),
		force = d3.layout.force()
			.charge(-50)
			.linkDistance(30)
			.size([width, height]);

	return {
		restrict: 'E',
		terminal: true,
		scope: {
			val: '='
		},
		link: function(scope, element, attrs) {
			// set up initial svg object
			var vis = d3.select(element[0])
				.append('svg')
				.attr('width', width)
				.attr('height', height + margin + 100);
			scope.$watch('val', function(newVal, oldVal) {
				console.log('Value changed: ' + angular.toJson(newVal));
				// Clear the elements inside of the directive
				vis.selectAll('*').remove();

				// if 'val' is undefined, exit
				if (!newVal) {
					return;
				}
				if ($.isEmptyObject(newVal)) {
					return;
				}

				force.nodes(newVal.nodes)
					.links(newVal.links)
					.start();

				var link = vis.selectAll(".link")
					.data(newVal.links)
					.enter().append("line")
					.attr("class", "link")
					.style("stroke-width", function(d) {
					return Math.sqrt(3);
				});

				var node = vis.selectAll(".node")
					.data(newVal.nodes)
					.enter().append("circle")
					.attr("class", "node")
					.attr("r", 5)
					.style("fill", function(d) {
					return color(5);
				})
					.call(force.drag);

				node.append("title")
					.text(function(d) {
					return d.name;
				});

				force.on("tick", function() {
					link.attr("x1", function(d) {
						return d.source.x;
					})
						.attr("y1", function(d) {
						return d.source.y;
					})
						.attr("x2", function(d) {
						return d.target.x;
					})
						.attr("y2", function(d) {
						return d.target.y;
					});

					node.attr("cx", function(d) {
						return d.x;
					})
						.attr("cy", function(d) {
						return d.y;
					});
				});

			});
		}
	};
})
	.directive('freebaseWidget', function($http, $log, $rootScope) {
	$rootScope.safeApply = function(fn) {
		var phase = this.$root.$$phase;
		if (phase == '$apply' || phase == '$digest') {
			fn();
		} else {
			this.$apply(fn);
		}
	};

	return {
		restrict: 'A',
		// terminal: true,
		link: function(scope, element, attrs) {
			angular.element(element)
				.suggest({
				filter: '(all type:/film/actor)',
				key: 'AIzaSyBQcaeTrGRDWOIbWa3QyNi33jTR2aSM-pw'
			});
			angular.element(element)
				.bind("fb-select", function(e, data) {
					$rootScope.safeApply(function () {
						// alert(data.name + ", " + data.id);
						$http.get('/service/graphgen?id=' + data.id)
							.error(function(err){$log.error('Error creating graph' + angular.toJson(err))})
							.success(function() {
								// refresh the graph now
								scope.getGraph();			
							});		
					});
					
			});
		}

	}
});