;(function (Utils) {
  Utils.createElement = function (type, props, children) {
    var elem = document.createElement(type);
    for (var prop in props) {
      if (prop === "style") {
        var style = props[prop];
        for (var styleProp in style) {
          elem.style[styleProp] = style[styleProp];
        }
      } else {
        elem[prop] = props[prop];
      }
    }
    return elem;
  };
})(window.Utils || (window.Utils = {}));