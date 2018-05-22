/* Simple JavaScript Inheritance
 * By John Resig http://ejohn.org/
 * MIT Licensed.
 */
// Inspired by base2 and Prototype
(function(){
  var initializing = false, fnTest = /xyz/.test(function(){xyz;}) ? /\b_super\b/ : /.*/;
 
  // The base Class implementation (does nothing)
  this.Class = function(){};
 
  // Create a new Class that inherits from this class
  Class.extend = function(prop) {
    var _super = this.prototype;
   
    // Instantiate a base class (but only create the instance,
    // don't run the init constructor)
    initializing = true;
    var prototype = new this();
    initializing = false;
   
    // Copy the properties over onto the new prototype
    for (var name in prop) {
      // Check if we're overwriting an existing function
      prototype[name] = typeof prop[name] == "function" &&
        typeof _super[name] == "function" && fnTest.test(prop[name]) ?
        (function(name, fn){
          return function() {
            var tmp = this._super;
           
            // Add a new ._super() method that is the same method
            // but on the super-class
            this._super = _super[name];
           
            // The method only need to be bound temporarily, so we
            // remove it when we're done executing
            var ret = fn.apply(this, arguments);        
            this._super = tmp;
           
            return ret;
          };
        })(name, prop[name]) :
        prop[name];
    }
   
    // The dummy class constructor
    function Class() {
      // All construction is actually done in the init method
      if ( !initializing && this.init )
        this.init.apply(this, arguments);
    }
   
    // Populate our constructed prototype object
    Class.prototype = prototype;
   
    // Enforce the constructor to be what we expect
    Class.prototype.constructor = Class;
 
    // And make this class extendable
    Class.extend = arguments.callee;
   
    return Class;
  };
})();

var SwipeableElem = Class.extend({
    init: function ( id ) {
        var elem = document.getElementById( id );
        this.elem = elem;
        this.callback_left = undefined;
        this.callback_right = undefined;
        this.threshold_x = 40;
        this.threshold_y = 35;
        this.x_1;
        this.y_1;
        this.dx;
        this.dy;
        var that = this;
        this.isMoved = false;
        //Trying to get windows phones to work...ha
        if (window.navigator.msPointerEnabled) {
            elem.addEventListener("MSPointerStart", function (e){that.touchStart(e)}, false);
            elem.addEventListener("MSPointerMove", that.touchMove, false);
            elem.addEventListener("MSPointerEnd", that.touchEnd, false);
        } else {
        //Attaching all the normal listeners.
            elem.addEventListener("touchstart", function (e){that.touchStart(e)}, false);
            elem.addEventListener("touchmove", function (e){that.touchMove(e)}, false);
            elem.addEventListener("touchend", function (e){that.touchEnd(e)}, false);
        }
    }, 
    
    isLeft: function( ){
		if(Math.abs(this.dx) > this.threshold_x && this.dx < 0 && Math.abs(this.dy) < this.threshold_y && this.isMoved){
			return true;
		}
		return false;
    },
    
    isRight: function( ){
		if(Math.abs(this.dx) > this.threshold_x && this.dx > 0 && Math.abs(this.dy) < this.threshold_y && this.isMoved){
			if(scrollX !== undefined)
				return scrollX == 0;
			return true;
		}
		return false;
    },
    
    touchStart: function ( e ) {
		var targetEvent;
		if(!e.touches){
			targetEvent = e;
		} else if(e.touches.length > 0){
			targetEvent = e.touches[0];
		}
		this.x_1 = targetEvent.clientX;
		this.y_1 = targetEvent.clientY;
		this.isMoved = false;
    },
    
    touchMove: function ( e ) {
        var targetEvent;
		if(!e.touches){
			targetEvent = e;
		} else if(e.touches.length > 0){
			targetEvent = e.touches[0];
		}
		this.dx = targetEvent.clientX;
		this.dy = targetEvent.clientY;
		this.isMoved = true;
    },
    
    touchEnd: function ( e ) {
		this.dx = this.dx - this.x_1;
		this.dy = this.dy - this.y_1;
		if(this.isRight() && this.callback_right){
            this.callback_right();
		} else if(this.isLeft() && this.callback_left) {
            this.callback_left();
		}
    },
    
    onSwipeRight: function( callback ){
        this.callback_right = callback;
    },
    
    onSwipeLeft: function( callback ){
        this.callback_left = callback;
    }
});