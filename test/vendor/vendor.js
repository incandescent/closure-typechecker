/** @const */
var NUMBERWANG = "THAT'S NUMBERWANG";

/**
 * @param {number} foo should be a number
 * @return {string} whether number is numberwang
 */
function numberWang(foo) {
  return NUMBERWANG;
}

function staticError() {
  numberWang(undefined);
}

$.Deferred.prototype.resolveWith = function() { };

Vendor = {}

(function(Vendor) {
  /**
   * @constructor
   */
  Vendor.VendorType: function() {};
  Vendor.VendorType.prototype.doSomeThing = function() {};
})(Vendor);