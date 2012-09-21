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
        closure_typechecker = require('../index.js'),
        done = this.async();

    if (this.data.includes) {
      args.push('--include');
      args.push('"' + this.data.includes + '"');
    }

    if (this.data.excludes) {
      args.push('--excludes');
      args.push('"' + this.data.excludes+ '"');
    }

    args.push(files);

    closure_typechecker.run(args.join(' '));

    // Fail task if errors were logged.
    if (grunt.errors) { return false; }
  });

};
