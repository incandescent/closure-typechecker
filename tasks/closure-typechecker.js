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

    var args = [];
        files = file.expand(this.data.files),
        includes = this.data.includes || this.data.include,
        excludes = this.data.excludes || this.data.exclude,
        closure_typechecker = require('../index.js'),
        done = this.async();

    if (includes) {
      args.push('--report-include');
      args.push(includes);
    }

    if (excludes) {
      args.push('--report-exclude');
      args.push(excludes);
    }

    Array.prototype.push.apply(args, files);

    closure_typechecker.run(args);

    // Fail task if errors were logged.
    if (grunt.errors) { return false; }
  });

};
