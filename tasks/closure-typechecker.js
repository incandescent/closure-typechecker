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

  grunt.registerMultiTask("closure-typechecker",
    "Run Closure compiler typechecking", function() {

    var exec = require('child_process').exec,
        // Expand files to full paths
        files = file.expand(this.data.files),
        lib = 'lib',
        command = 'java';

    command = command + ' -classpath ' +
                lib + '/compiler.jar:' +
                lib + '/closure-typechecker.jar' +
                'com.incandescent.closure.CheckTypes';

    if (this.data.includes) {
      command = command + ' --include "' + this.data.includes + '"';
    }

    if (this.data.excludes) {
      command = command + ' --exclude "' + this.data.excludes + '"';
    }

    exec(command, function (error, stdout, stderr) {
        console.log('stdout: ' + stdout);
        console.log('stderr: ' + stderr);
        if (error !== null) {
          console.log('exec error: ' + error);
        }
    });

    // Fail task if errors were logged.
    if (grunt.errors) { return false; }
  });

};
