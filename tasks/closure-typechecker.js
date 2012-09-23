/*
 * Grunt Task File
 * ---------------
 *
 * Task: closure-typechecker
 * Description: run Closure compiler typechecking
 * Dependencies: child_process
 *
 */

module.exports = function(grunt) {

  var file = grunt.file;
  var log = grunt.log;
  var config = grunt.config;

  var _ = grunt.utils._;

  grunt.registerMultiTask("closure_typechecker",
    "Run Closure compiler typechecking", function() {

    var args = [],
        files = file.expand(this.data.files),
        includes = this.data.report_includes || this.data.report_include,
        excludes = this.data.report_excludes || this.data.report_exclude,
        closure_typechecker = require('../index.js'),
        done = this.async(),
        a;

    if (includes) {
      a = [];
      a.push(includes);
      _.each(_.flatten(a), function(regex) {
        args.push('--report-include');
        args.push(regex);
      });
    }

    if (excludes) {
      a = [];
      a.push(excludes);
      _.each(_.flatten(a), function(regex) {
        args.push('--report-exclude');
        args.push(regex);
      });
    }

    Array.prototype.push.apply(args, files);

    closure_typechecker.run(args);

    // Fail task if errors were logged.
    if (grunt.errors) { return false; }
  });

};
