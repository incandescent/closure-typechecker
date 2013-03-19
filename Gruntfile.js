module.exports = function(grunt) {

  grunt.initConfig({
    exec: {
      javac: {
        command: 'mkdir out && javac -d out -classpath lib/compiler.jar' +
                 ' src/com/incandescent/closure/CheckTypes.java' +
                 ' src/com/incandescent/closure/FilteredPrintStreamErrorManager.java',
        stdout: true
      },
      jar: {
        command: 'jar -cf lib/closure-typechecker.jar -C out .',
        stdout: true
      },
    },
    clean: {
      out: 'out'
    },
    closure_typechecker: {
      test: {
        files: { src: [ "test/vendor/*.js", "test/test.js" ] },
        report_exclude: "vendor/.*\.js"
      },
      type_test: {
        files: [ "test/type.js" ]
      }
    }
  });

  grunt.loadNpmTasks('grunt-contrib-clean');
  grunt.loadNpmTasks('grunt-exec');
  grunt.loadTasks('tasks');
  grunt.registerTask("build", ["clean:out", "exec:javac", "exec:jar"]);
  grunt.registerTask("test", "closure_typechecker:test");
  grunt.registerTask("typetest", "closure_typechecker:type_test");
  grunt.registerTask("default", ["build", "test"]);
};
