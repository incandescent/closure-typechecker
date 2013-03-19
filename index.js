/* Run closure-typechecker */
exports.run = function(args) {
  var java_args = [
        '-classpath',
        __dirname + '/lib/compiler.jar:' + __dirname + '/lib/closure-typechecker.jar',
        'com.incandescent.closure.CheckTypes'
      ],
      child;

  child = require('child_process').spawn('java', java_args.concat(args));

  child.stdout.pipe(process.stdout, { end: false });
  child.stderr.pipe(process.stderr, { end: false });
};
