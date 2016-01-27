(function(){

	function saveInBackend(){
		console.log("save invoked ...");
	}

   var editor = ace.edit("editor");
   editor.setTheme("ace/theme/twilight");
   editor.getSession().setMode("ace/mode/javascript");

	editor.getSession().on('change', function(e) {
		console.log("Editor Changed!");
	});

	editor.setValue("function foo(items) {\n  var x = \"All this is syntax highlighted\";\n  return x;\n}");

}());
    