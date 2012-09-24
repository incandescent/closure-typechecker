/**
 * @typedef {Object}
 */
namespace = {}

/** @constructor */
namespace.MyType1 = function() {};

(function() {
  /** @constructor */
  namespace.MyType2 = function() {};
})();

(function(ns) {
  /** @constructor */
  ns.MyType3 = function() {};
})(namespace);


/** @return {namespace.MyType1} */
function returnMyType1() {
  return new namespace.MyType1();
}

// "Bad type annotation"
/** @return {namespace.MyType2} */
function returnMyType2() {
  return new namespace.MyType2();
}

// "Bad type annotation"
/** @return {namespace.MyType3} */
function returnMyType3() {
  return new namespace.MyType3();
}
