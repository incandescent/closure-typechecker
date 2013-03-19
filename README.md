# grunt-closure-typechecker

Grunt multi-task for running closure-compiler for typechecking (non-Closure) javascript source

This task is useful for type-checking (function signatures, classes, structural types, etc.) under certain code organizations that Closure Compiler understands.
Unfortunately, Closure Compiler does not understand the IIFE idiom, which is currently hamstringing use as a general javascript type checker.
Please vote for these issues if you are interested in seeing Closure Compiler typechecking support working in browser-targeted source:

* http://code.google.com/p/closure-compiler/issues/detail?id=822
* http://code.google.com/p/closure-compiler/issues/detail?id=448

## Getting Started

Install this grunt plugin next to your project's grunt.js gruntfile with `npm install grunt-jasmine-runner` or add it to your `package.json`:

```
"dependencies": {
    "grunt": "latest",
    "grunt-closure-typechecker": "latest",
...
```

Then add this line to your project's `grunt.js` gruntfile:

```javascript
grunt.loadNpmTasks('grunt-closure-typechecker');
```

## Config
- closure_typechecker
  - config name
    - files: Grunt-expanded list of file patterns to analyze
    - report_exclude: Grunt-expanded list of file patterns to exclude from report (e.g. vendor and packaged files)

```javascript
'closure_typechecker' : {
  app: {
    files: { src: ['app/js/**/*.js'] },
    report_exclude: ['app/js/vendor', 'app/js/release']
  }
}
```

## Running

After successful configuration, you can run closure typechecking:

```grunt closure_typechecker```

Or define an alias in your `grunt.js`:

```javascript
grunt.registerTask('typecheck', 'closure_typechecker');
```

Output:

```
Excluding from report: .*app/js/vendor.*
Excluding from report: .*app/js/release.*
Excluding from report: .*app/js/templates.js.*
app/js/namespace.js:3: WARNING - dangerous use of the global this object

app/js/example.js:107: ERROR - actual parameter 4 of Example.NS.prototype.resolve does not match formal parameter
found   : (Object|null)
required: Object

app/js/example.js:111: ERROR - actual parameter 1 of Example.NS.prototype.reject does not match formal parameter
found   : (String|null)
required: String

app/js/example.js:111: ERROR - actual parameter 5 of Example.NS.prototype.reject does not match formal parameter
found   : (Object|null)
required: Object

```

## Release History

* v0.0.2: added support for grunt ~0.4
* v0.0.1: initial "release" updated in place since nobody is using this yet ;)

## License
Copyright (c) 2013 Incandescent Software LLC
Licensed under Apache License Version 2.0
See LICENSE

