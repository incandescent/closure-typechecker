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
        files: [ "test/*.js", "test/vendor/*.js" ],
        exclude: "vendor/.*\.js"
      }
    }
  });

  grunt.loadNpmTasks('grunt-clean');
  grunt.loadNpmTasks('grunt-exec');
  grunt.loadTasks('tasks');
  grunt.registerTask("build", "clean:out exec:javac exec:jar");
  grunt.registerTask("test", "closure_typechecker:test");
  grunt.registerTask("default", "build test");
};
