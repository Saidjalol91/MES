/*
 * This file is generated and updated by Sencha Cmd. You can edit this file as
 * needed for your application, but these edits will have to be merged by
 * Sencha Cmd when upgrading.
 */
Ext.application({
    name: 'LOTUS',

    extend: 'LOTUS.Application',

    requires: [
        'LOTUS.view.main.Main',
        'LOTUS.GlyphManager',
        'LOTUS.SessionChecker',
        'LOTUS.CommFunction',
        'Ext.window.Toast',
        'Ext.form.action.StandardSubmit',
        'LOTUS.*'
    	
    ],

    // The name of the initial view to create. With the classic toolkit this class
    // will gain a "viewport" plugin if it does not extend Ext.Viewport. With the
    // modern toolkit, the main view will be added to the Viewport.
    //
    mainView: 'LOTUS.view.main.Main',
	
	
    //-------------------------------------------------------------------------
    // Most customizations should be made to LOTUS.Application. If you need to
    // customize this file, doing so below this section reduces the likelihood
    // of merge conflicts when upgrading to new versions of Sencha Cmd.
    //-------------------------------------------------------------------------
	launch : function() {
		SessionChecker.init(480);
		//Ajax Request가 완료될때의 이벤트를 리스닝
        Ext.Ajax.on('requestcomplete', function (conn, response, options) {
            //console.log('requestcomplate 이벤트 감지 SessionChecker 시작');
            //Request URL을 파라미터로 넘김
	       	console.log("requestcomplete............");
	       	SessionChecker.start(options.url);
	       	
	       	if (response.responseText!="") {
	            var result = Ext.JSON.decode(response.responseText);
	                
	            if (!result.success) {
	                if (result.message=="session expired") {
	                    console.log('로그인페이지로 이동.');
	                    document.location.href=SessionChecker.SESSION_KILL_REQ;
	                }
	            }
	       	}
        });
        
        Ext.setGlyphFontFamily('FontAwesome');
	     
	    Ext.namespace('Ext.ux', 'Ext.ux.plugins');
	     
	    Ext.ux.comboBoxRenderer = function(combo){
	        return function(value){
	            var idx = combo.store.find(combo.valueField, value);
	            var record = combo.store.getAt(idx);
       
	            return record ? record.get(combo.displayField) : value; //combo.valueNotFoundText;
	        };
	    };
	     
	    Ext.ux.convertStoreToJson = function(store) {
	         var out = store.getProxy().getWriter();
	         var arr = new Array();
	         var idx = 0;
	         store.each(function(record, index){
	             arr[index] = out.getRecordData(record);
	             idx ++;
	         });
	        
	         if (idx==0) return "";
	        
	         return "{ data : " + Ext.encode(arr) + "}";
	    };
       
	    Ext.ux.convertSelectionToJson = function( grid) {
	         var store     = grid.getStore();
	         var selection = grid.getSelectionModel().getSelection();
	         var out       = store.getProxy().getWriter();
	         var arr       = new Array();
	         var idx = 0;
	         
	         Ext.each( selection, function(record){
	             arr.push( out.getRecordData(record));
	             idx ++;
	         });
	         if (idx==0) return "";
	             
	         return "{ data : " + Ext.encode(arr) + "}";
	    };
       
	    Ext.ux.convertStoreToJsonForDirty = function( store) {
	         var out = store.getProxy().getWriter();
	         var arr = new Array();
	         var idx =0;
	        
	         store.each(function(record, index){
	             if (record.dirty ==true) {
	                 arr[idx] =out.getRecordData(record); 
	                 idx++;
	             }
	         });
	         if (idx==0) return "";
	         
	         return "{ data : " + Ext.encode(arr) + "}";
	    };
	     
	    Ext.ux.showToast = function(s) {
	     	var myHtml = '<div style="text-align:center;font-size:24px;font-weight:800;color:#5F6B77"> ' + s + '</div>'
	        Ext.toast({
	            html: myHtml,
	            closable: false,
	            align: 'b',
	            slideInDuration: 400,
	            padding: 0,
	            minWidth: window.innerWidth
	        });
	    };

	    Ext.ux.validator = function(form, errors) {
        	var message = "", ls_comma = "";                	
       	    fields = form.query("field");

       	    errors.each(function (errorObj) {
       		    var idx = 0;
           	    for (var i=0; i<fields.length; i++) {
           	        if (fields[i].getName() == errorObj.getField()) { 
           	    	    message += ls_comma + fields[i].getFieldLabel();
           	    	    ls_comma = ",";
           	    	    idx++;
           	            break;
           	        }
              	}
   		    });
       	    return message;
		};	

	    Ext.ux.submitFailure = function(form, action) {
            var message = "";
            if (action.failureType=='client') message = "입력 Data를 확인하세요.";
            else message = action.result.message;
            
            return message;
		};	
	}    	
});
