namespace = {}

(function(ns) {
  /** @constructor */
  ns.MyType = function() {};

  /** @return {ns.MyType} */
  function typeIsKnown() {
    return new ns.MyType();
  }
})(namespace);

/** @return {ns.MyType} */
function typeNotKnown() {
  return new namespace.MyType();
}


