
;( function( $ ) {
    
$.fn.validate = function( opts ) {
    
    if ( typeof opts == "object" ) {
        this.each( function() {
            var el = $( this ).data( "opts", opts );
            
            $.each( opts, function( name, config ) {
                config = $.extend({
                    autoValidate: true
                }, config );
                if ( !config.autoValidate ) {
                    return;
                }
                var field = el.find( "#"+name );
                if(typeof(field.val())=="undefined"){
                	field=$("input[name='"+name+"']");
                }
                if(field.length>1){
                	if(field.is("input:text, input:password, textarea")){
	                	$.each(field,function(i){
	                		var nowField = $(this);
		                    nowField.unbind("blur").blur( function( e ) {
		                        _validateField( $(this), config, el );
		                    });
	                	});
                	}else if ( field.is( "input:checkbox, input:radio" )){
                		field.unbind("click").click( function( e ) {
	                        _validateField( field, config, el );
	                    });
               		}
                }else{
	                if ( field.is( "input:text, input:password, textarea" )) {
	                    field.unbind("blur").blur( function( e ) {
	                        _validateField( field, config, el );
	                    });
	                } else if ( field.is( "input:checkbox, input:radio" )) {
	                    field.unbind("click").click( function( e ) {
	                        _validateField( field, config, el );
	                    });
	                }
                }
            });
        });
        
    } else if ( typeof opts == "string" && opts == "validate" ) {
	        var el = this.first();
	        var opts = el.data( "opts" );
	        if ( !opts ) {
	            return true;
	        }
	        var valid = true;
	        var name = arguments[1];
	        if ( name ) {
	        	var field = el.find( "#"+name );
	        	if(typeof(field.val())=="undefined"){
	                	field=$("input[name='"+name+"']");
	             }
	            if(field.length>1){	
	            	if(field.is( "input:text, input:password, textarea, select" )){
		            	$.each(field,function(i){
		            		var nowField = $(this);
				            valid = _validateField( nowField, opts[name], el );
		            	});
	            	}else if(field.is("input:checkbox, input:radio")){
			            valid = _validateField( field, opts[name], el );
	            	}
	            }else{
		            valid = _validateField( field, opts[name], el );
	            }
	        } else {
	            el.validate("reset" );
	            $.each( opts, function( name, config ) {
	            	var field = el.find( "#"+name );
	            	 if(typeof(field.val())=="undefined"){
	                 	field=$("input[name='"+name+"']");
	                 }
	                if(field.length>1){
		            	if(field.is( "input:text, input:password, textarea, select" )){
		                	$.each(field,function(i){
			            		var nowField = $(this);
				                if ( !_validateField( nowField, config, el )) {
				                    valid = false;
				                };
			            	});
		            	}else if(field.is("input:checkbox, input:radio")){
			                if ( !_validateField( field, config, el )) {
			                    valid = false;
			                };
		            	}
	                }else{
		                if ( !_validateField( field, config, el )) {
		                    valid = false;
		                };
	                }
	            });
	        }
	        return valid;
    } 
    else if ( typeof opts == "string" && opts == "reset" ) {//移除提示
        var el = this.first();
        if ( $.fn.tips ) {
            $( ".tip-holder" ).remove();
        } else {
            el.find( ".field-error" ).remove();
        }
        
        el.find( ".form-field-error" ).removeClass( "form-field-error" );
    }
    
    return this;
};

function _validateField( field, config, el ) {
	 return  _validateFieldValue( field, config, el );
}
function _validateFieldValue( field, config, el ) {
	var valid = true,
    msg = "";

   if(field.first().attr('disabled')=='disabled'){
   	valid=true;
	}else{
		$.each( config, function( vType, param ) {
	        var result = null;
	        if ( vType == "equalTo" ) {
	            result = {
	                valid: fieldValue( field ) == fieldValue( el.find( "#"+param )),
	                errorMsg: "\u8BE5\u5B57\u6BB5\u4E0E" + param + "\u4E0D\u540C"
	            };
	        } else if ( vType == "remote" ) {
	        	
	        } else if ( $.inArray( vType, ["msg", "position", "autoValidate"] ) < 0 ) {
	            result = $.validate[vType]( fieldValue( field ) || "", param , field );
	        }
	        
	        if ( result && !result.valid ) {
	            valid = false;
	            msg = config.msg || result.errorMsg;
	            return false;
	        }
	    });
	}
   var formField = field.first().closest( ".form-field" );
   if ( !valid ) {
       if ( $.fn.tips ) {
           field.first().tips({
               position: config.position || "right",
               content: msg,
               autoHide: false
           });
       } else {
           var fieldError = formField.find( ".field-error" );
           if ( !fieldError.length ) {
               fieldError = $( "<div/>", {
                   "class": "field-error"
               }).appendTo( formField );
           }
           fieldError.text( msg );
       }
       
       formField.addClass( "form-field-error" );
   } else {
       formField.removeClass( "form-field-error" );
       if ( $.fn.tips ) {
           field.tips( "hide" );
       } else {
           var fieldError = formField.find( ".field-error" );
           fieldError.remove();
       }
   }
   
   return valid;
}
function fieldValue( field ) {
    if ( field.is( "input:checkbox, input:radio" )) {
        var result = [];
        field.filter( ":checked" ).each( function( i, f ) {
            result.push( $( f ).val());
        });
        return result;
    } else {
        return field.val();
    }
}

$.validate = {
    required: function( value, param ) {
        return {
            valid: !param || requiredTrim(value),
            errorMsg: $.validate.errorMsg.required
        };
    },
    
    minLength: function( value, param ) {
        var length = value.length;
        return {
            valid: length ? length >= param : true,
            errorMsg: $.validate.errorMsg.minLength.replace( /#\{min\}/g, param )
        };
    },
    
    maxLength: function( value, param ) {
        var length = value.length;
        return {
            valid: length ? length <= param : true,
            errorMsg: $.validate.errorMsg.maxLength.replace( /#\{max\}/g, param )
        };
    },
    
    rangeLength: function( value, param ) {
        var length = value.length;
        return {
            valid: length ? ( length >= param[0] && length <=param[1] ) : true,
            errorMsg: $.validate.errorMsg.rangeLength.replace( /#\{min\}/g, param[0] ).replace( /#\{max\}/g, param[1] )
        };
    },
    
    min: function( value, param ) {
        return {
            valid: value >= param || value.trim().length==0,
            errorMsg: $.validate.errorMsg.min.replace( /#\{min\}/g, param )
        };
    },
    minrE: function( value, param ) {
        return {
            valid: value > param || value.trim().length==0,
            errorMsg: $.validate.errorMsg.minrE.replace( /#\{minrE\}/g, param )
        };
    },
    
    max: function( value, param ) {
        return {
            valid: value <= param || value.trim().length==0,
            errorMsg: $.validate.errorMsg.max.replace( /#\{max\}/g, param )
        };
    },
    maxrE: function( value, param ) {
        return {
            valid: value < param || value.trim().length==0,
            errorMsg: $.validate.errorMsg.max.replace( /#\{max\}/g, param )
        };
    },
    range: function( value, param ) {
        return {
            valid: ( value >= param[0] && value <= param[1] ),
            errorMsg: $.validate.errorMsg.range.replace( /#\{min\}/g, param[0] ).replace( /#\{max\}/g, param[1] )
        };
    },
    email: function( value, param ) {
        return {
            valid: !param || !value || /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i.test( value ),
            errorMsg: $.validate.errorMsg.email
        };
    },
    
    url: function( value, param ) {
        return {
            valid: !param || !value || /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test( value ),
            errorMsg: $.validate.errorMsg.url
        };
    },
    
    date: function( value, param ) {
        return {
            valid: !param || !/Invalid|NaN/.test( new Date( value ))||value.trim().length==0,
            errorMsg: $.validate.errorMsg.date
        };
    },

    dateISO: function( value, param ) {
        return {
            valid: !param || /^\d{4}[\/-]\d{1,2}[\/-]\d{1,2}$/.test( value )||value.trim().length==0,
            errorMsg: $.validate.errorMsg.dateISO
        };
    },
    dateFull: function( value, param ) {
        return {
            valid: !param || /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/.test( value )||value.trim().length==0,
            errorMsg: $.validate.errorMsg.dateISO
        };
    },
    number: function( value, param ) {
        return {
            valid: !param || /^-?(?:\d+|\d{1,3}(?:,\d{3})+)(?:\.\d+)?$/.test( value.trim().replace(/[(^\s+)(\s+$)]/g,"") ) || /^\d{0}$/.test( value.trim().replace(/[(^\s+)(\s+$)]/g,"") ),
            errorMsg: $.validate.errorMsg.number
        };
    },

    digits: function( value, param ) {
        return {
            valid: !param || /^\d+$/.test( value )||value.trim().length==0,
            errorMsg: $.validate.errorMsg.digits
        };
    },
    
    identitycard: function( value, param ) {
    	
        return {
            valid: !param ||value.trim().length==0|| /^\d{15}$/.test( value.replace(/[(^\s+)(\s+$)]/g,"") ) || /^\d{17}\X{1}$/.test( value.replace(/[(^\s+)(\s+$)]/g,"") ) || /^\d{17}\x{1}$/.test( value.replace(/[(^\s+)(\s+$)]/g,"") ) || /^\d{18}$/.test( value.replace(/[(^\s+)(\s+$)]/g,"") ),
            errorMsg: $.validate.errorMsg.identitycard
        };
    },
    isonlynumber: function( value, param ) {
        return {
            valid: !param || /^[0-9]+$/.test(value.trim())||value.trim().length==0,
            errorMsg: $.validate.errorMsg.isonlynumber
        };
    },
    username: function( value, param ) {
        return {
            valid: !param ||/^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]|[a-zA-Z])*$/.test(value.trim())||value.trim().length==0,
            errorMsg: $.validate.errorMsg.isonlycharacter
        };
    },
    numberPoint: function(value,param){
    	var paramArray= param.split(",");
    	var sizeLength=paramArray[0];
    	var pointSize=paramArray[1];
    	return {
    		valid: !param ||checkNumAndPoint(value,sizeLength,pointSize)||value.trim().length==0,
            errorMsg: $.validate.errorMsg.numberCheck.replace( /#\{min\}/g, sizeLength ).replace( /#\{max\}/g, pointSize )
    		
    	};
    },
    phoneNumber: function(value,param){
    	return {
    		/* valid: !param || /^1[0-9][0-9]\d{8}$/.test(value.trim()) || /^0(([1-9]\d)|([1-9]\d{2}))-?\d{7,8}(-\d{1,4})?$/.test(value.trim()) || value.trim().length==0,
    		errorMsg:$.validate.errorMsg.phoneNumber*/
    		valid: !param || value.trim().length==0 || (/^[0-9]+-?[0-9]+$/.test(value.trim()) && 20>value.trim().length),
    		errorMsg:$.validate.errorMsg.phoneNumber
    	};
    },
    age :function(value,param){
    	return{
    		valid: ( value >= 0 && value <= 200 ),
            errorMsg: $.validate.errorMsg.age
    	};
    },
    dateCheck:function(value,param){
    	return{
    		valid: !((new Date(Date.parse(value.replace("-","/")))) > (new Date())),
    		errorMsg: $.validate.errorMsg.dateCheck
    	};
    },
    dateAfter:function(value,param){
    	var str = "#"+param[0];
    	var paramValue = $(str).val();
    	var msg = param[1];
    	return{
    		valid: value.trim().length==0 ||paramValue.trim().length==0 || new Date(Date.parse(value.replace("-","/"))) >= new Date(Date.parse(paramValue.replace("-","/"))),
    		errorMsg: $.validate.errorMsg.dateAfter.replace( /#\{msg\}/g,  msg)
    	};
    },
    numberLimit:function(value,param){
    	var lgth = value.length;
    	if(lgth==0){
    		lgth=param;
    	}
    	return{
    		valid: !param || lgth==param,
    		errorMsg:$.validate.errorMsg.numberLimit.replace(/#\{length\}/g,  param)
    	};
    },
    uniqueIdCard: function(value,param){//param填原始身份证ID
    	return {
    		valid: !param || uniqueIdCard(value,param)||value.trim().length==0,
            errorMsg: $.validate.errorMsg.uniqueIdCard
    		
    	};
    },
    idCardCheck:function(value,param){
    	return {
    		valid : !param || value.trim().length==0 || checkParseIdCard(value),
    		errorMsg : $.validate.errorMsg.idCardCheck
    	};
    },
    idCardCheckTwo:function(value,param){

    	return {
    		valid : !param || value.trim().length==0 || checkParseIdCardMax(value),
    		errorMsg : $('#idCardType').val()=='CV0100.03_1' ? $.validate.errorMsg.idCardCheck : $.validate.errorMsg.idCardCheckChild
    	};
    },
    idCardCheckSimple:function(value,param){

    	return {
    		valid : !param || value.trim().length==0 || idCardCheckSimple(value),
    		errorMsg : $('#idCardType').val()=='CV0100.03_1' ? $.validate.errorMsg.idCardCheck : $.validate.errorMsg.idCardCheckChild
    	};
    },
    idCardCheckSimpleComm:function(value,param){
    	return {
    		valid : !param || value.trim().length==0 || idCardCheckSimpleComm(value),
    		errorMsg : $.validate.errorMsg.idCardCheck
    	};
    },
    idCardCheckSimpleCommTwo:function(value,param){
    	return {
    		valid : !param || value.trim().length==0 || idCardCheckSimpleCommTwo(value,param),
    		errorMsg : $.validate.errorMsg.idCardCheck
    	};
    },
    idCardCheckChild:function(value,param){
    	return {
    		valid : !param ||/^[A-Za-z0-9]+$/.test(value.trim()),
    		errorMsg : $.validate.errorMsg.idCardCheckChild
    	};
    },
    decimalPointBefore:function(value,param){//小数点前保留位数验证
    	return {
    		valid : !param || value.trim().length==0 || decimalPointBefore(value,param),
    		errorMsg : $.validate.errorMsg.decimalPointBefore.replace( /#\{num\}/g,  param)
    	};
    },
    decimalPointAfter:function(value,param){//小数点后保留位数验证
    	return {
    		valid : !param || value.trim().length==0 || decimalPointAfter(value,param),
    		errorMsg : $.validate.errorMsg.decimalPointAfter.replace( /#\{num\}/g,  param)
    	};
    },
    surplusIsEnough:function(value,param){//实现资料入库中修改印刷份数时，判断剩余量是否足够
    	return {
    		valid : !param || value.trim().length==0 || surplusIsEnough(value,param),
    		errorMsg : $.validate.errorMsg.surplusIsEnough
    	};
    },
    digital: function(value,param){
    	/*/^1[3|5|8][0-9]\d{8}$/.test($.trim(value))*/
    	return{
    		valid: !param || /^([1-9]{1}\d*)$/.test($.trim(value)) || /^(([1-9]{1}\d*)|([0]{1}))(\.(\d){0,2})$/.test($.trim(value)) || $.trim(value).length==0,
    		errorMsg: $.validate.errorMsg.digital
    	};
    },
    valiMaxNum:function(value,param,obj){//健康教育活动记录中发放资料验证
		var basic=$(obj).attr("basic");
		return {
            valid: value.trim().length==0 || basic==undefined || eval(basic) >= eval(value),
            errorMsg: "数量不足"
        };
	},
	//减体重项需填写区间值 2017.4.19 changlin.zeng
    intervals :function(value,param){    
    	var strs=value.split("-"); 	
		return {
            valid:value.trim().length==0||/^\d+(\.\d{0,2})?$/.test($.trim(value))||(/^\d+(\.\d{1,2})?$/.test(strs[0])&&/^\d+(\.\d{1,2})?$/.test(strs[1])&&parseFloat(strs[0])<parseFloat(strs[1])),
            errorMsg: "只能输入数字或者两位小数或者带-区间,请检查输入的内容是否合法"
        };
	}
    
};
	String.prototype.trim=function()
	{
	     return this.replace(/(^\s*)|(\s*$)/g, "");
	}

function requiredTrim(value){
	if(value.length>0){
		var str= new String(value);
		if(str.trim().length==0){
			return false;
		}
		return true;
			
	}
	return false;
}

function checkNumAndPoint(value, size,pointSize){//小数数的数字判断
//	var obj = document.getElementById(id);
	if (value != '') {
		if (isNaN(value)) {
			return false;
		} else {
			var temp = value.match(/[.]/g);
			if (temp == null) {
				if (value.length > size) {
					return false;
				}
			} else {
				if (value.toString().split(".")[0].length>size || value.toString().split(".")[1].length > pointSize) {
					return false;
				}
			}
		}
		return true;
	} else {
		return true;
	}
}
/*小数点前保留位数验证*/
function decimalPointBefore(value,param){
	if (value != '') {
		if (isNaN(value)) {
			return false;
		} else {
			var temp = value.match(/[.]/g);
			if (temp == null) {
				if (value.length > param) {
					return false;
				}
			} else {
				if (value.toString().split(".")[0].length>param) {
					return false;
				}
			}
		}
		return true;
	} else {
		return true;
	}
}
function decimalPointAfter(value,param){
	if (value != '') {
		if (isNaN(value)) {
			return false;
		} else {
			var temp = value.match(/[.]/g);
			if (temp == null) {
				return true;
			} else {
				if (value.toString().split(".")[1].length>param) {
					return false;
				}
			}
		}
		return true;
	} else {
		return true;
	}
}
/*剩余量是否足够*/
function surplusIsEnough(value,param){
	var flag=true;
	value=value.trim();
	var id=$("#"+param).val();
	if(id!="" && id!=null && id!=undefined){
		$.ajax({    
			url: "qryIssueNumById.action?random="+Math.random(),    
			type: 'POST',    
			dataType: 'json',
			data:{"id": id},
			async:false,   
			success: function(msg){ 
				if(msg!="false"){
					if(eval(msg)>value){
						flag=false;
					}
				}else{
					flag=false;
				}
				
			}
		});
	}
	return flag;
}

function uniqueIdCard(value,param){
	value=value.trim();
	var flag=false;
	if($("#"+param).val()==value){
		flag = true;
	}else{
		$.ajax({    
			url: "uniqueIdCard.action?random="+Math.random(),    
			type: 'POST',    
			dataType: 'json',
			data:{"idCard": value},
			async:false,   
			success: function(msg){ 
				if(msg=="false"){
					flag=true;
				}				
			}
		});
	}
	return flag;
	
}


/**  
 * 身份证15位编码规则：dddddd yymmdd xx p   
 * dddddd：地区码   
 * yymmdd: 出生年月日   
 * xx: 顺序类编码，无法确定   
 * p: 性别，奇数为男，偶数为女  
 * <p />  
 * 身份证18位编码规则：dddddd yyyymmdd xxx y   
 * dddddd：地区码   
 * yyyymmdd: 出生年月日   
 * xxx:顺序类编码，无法确定，奇数为男，偶数为女   
 * y: 校验码，该位数值可通过前17位计算获得  
 * <p />  
 * 18位号码加权因子为(从右到左) Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2,1 ]  
 * 验证位 Y = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ]   
 * 校验位计算公式：Y_P = mod( ∑(Ai×Wi),11 )   
 * i为身份证号码从右往左数的 2...18 位; Y_P为脚丫校验码所在校验码数组位置  
 *   
 */

function checkParseIdCard(val) {
	var val = trim(val);
	var result = idCard(val);
	return result;
}
/*
 * 2014/4/22
 * 根据需要因身份证验证过于严格，新增简易身份证验证
 * 只验证满18位及其之中的出生日期准确且必须>=18位
 * 
 * 添加idCardType== null的情况（其他）不用走身份证验证
 */
function idCardCheckSimple(val){
	val=val.trim();//2013-04-09,Hevin修改的HCP-3762去掉空格
	if($("#idCardTypeNew").val()=="CV0100.03_99" 
		|| $("#idCardTypeNew").val()== null 
		|| $("#idCardTypeNew").val()== ""){
		if( /^[0-9a-zA-Z]*$/g.test(val.trim())){
			return true;
		}else{
			return false;
		}
	}else if(val.length>=18){
		var dateStr=val.substring(6,10)+"-"+val.substring(10,12)+"-"+val.substring(12,14);
		 if(isdatetime(dateStr)){
			 return true;
		 }else{
			 return false;
		 }
	}else{
		return false;
	}
}
function idCardCheckSimpleComm(val){
	val=val.trim();//2013-04-09,Hevin修改的HCP-3762去掉空格
	if(val.length==18){
		var dateStr=val.substring(6,10)+"-"+val.substring(10,12)+"-"+val.substring(12,14);
		if(isdatetime(dateStr)){
			return true;
		}else{
			return false;
		}
	}else{
		return false;
	}

}
function idCardCheckSimpleCommTwo(val,param){
	if($("#"+param.a).val()=="CV02.01.101_01"){
		
		if(val.length==18){
			var dateStr=val.substring(6,10)+"-"+val.substring(10,12)+"-"+val.substring(12,14);
			if(isdatetime(dateStr)){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
    }else{
    	
    	return true;
    }
}
function isdatetime(str) 
{ 
	var reg=/((^((1[8-9]\d{2})|([2-9]\d{3}))([-\/\._])(10|12|0?[13578])([-\/\._])(3[01]|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))([-\/\._])(11|0?[469])([-\/\._])(30|[12][0-9]|0?[1-9])$)|(^((1[8-9]\d{2})|([2-9]\d{3}))([-\/\._])(0?2)([-\/\._])(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)([-\/\._])(0?2)([-\/\._])(29)$)|(^([3579][26]00)([-\/\._])(0?2)([-\/\._])(29)$)|(^([1][89][0][48])([-\/\._])(0?2)([-\/\._])(29)$)|(^([2-9][0-9][0][48])([-\/\._])(0?2)([-\/\._])(29)$)|(^([1][89][2468][048])([-\/\._])(0?2)([-\/\._])(29)$)|(^([2-9][0-9][2468][048])([-\/\._])(0?2)([-\/\._])(29)$)|(^([1][89][13579][26])([-\/\._])(0?2)([-\/\._])(29)$)|(^([2-9][0-9][13579][26])([-\/\._])(0?2)([-\/\._])(29)$))/ig;
	if(reg.test(str)){
		return true;
	}else{
		return false;
	}
}

function checkParseIdCardMax(val) {
	val=val.trim();//2013-04-09,Hevin修改的HCP-3762去掉空格
	var vals = "";
	if($("#idCardType").val()=="CV0100.03_99"){
		if( /^[0-9a-zA-Z]*$/g.test(val.trim())){
			return true;
		}else{
			return false;
		}
	}
	if(val.trim().length>18){
		vals=val.substring(0,18);
		//result= idCard(vals);
	}
	if(val.trim().length<18&&val.trim().length>15){
		vals=val.substring(0,15);
		//result= idCardTwo(vals);
		return false;
	}
	if(val.trim().length==18){
		vals=val;
		//result= idCard(vals);
	}
	if(val.trim().length==15){
		vals=val;
		//result= idCardTwo(vals);
	}
	var result= idCard(vals);
	return result;
}

function idCardTwo(value) {
if ((parseInt(value.substr(6, 2)) + 1900) % 4 == 0
		|| ((parseInt(value.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(value
				.substr(6, 2)) + 1900) % 4 == 0)) {
	var ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/; //测试出生日期的合法性 
} else {
	ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/; //测试出生日期的合法性 
}
if (ereg.test(value))
	return true;
else
	return false;

}
function idCard(value) {
    if (value.length == 18 && 18 != value.length) return false;
    var number = value.toLowerCase();
    var d, sum = 0, v = '10x98765432', w = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2], a = '11,12,13,14,15,21,22,23,31,32,33,34,35,36,37,41,42,43,44,45,46,50,51,52,53,54,61,62,63,64,65,71,81,82,91';
    var re = number.match(/^(\d{2})\d{4}(((\d{2})(\d{2})(\d{2})(\d{3}))|((\d{4})(\d{2})(\d{2})(\d{3}[x\d])))$/);
    if (re == null || a.indexOf(re[1]) < 0) return false;
    if (re[2].length == 9) {
        number = number.substr(0, 6) + '19' + number.substr(6);
        d = ['19' + re[4], re[5], re[6]].join('-');
    } else d = [re[9], re[10], re[11]].join('-');
    if (!isDateTime.call(d, 'yyyy-MM-dd')) return false;
    for (var i = 0; i < 17; i++) sum += number.charAt(i) * w[i];
    return (re[2].length == 9 || number.charAt(17) == v.charAt(sum % 11));
}
var isDateTime = function (format, reObj) {
    format = format || 'yyyy-MM-dd';
    var input = this, o = {}, d = new Date();
    var f1 = format.split(/[^a-z]+/gi), f2 = input.split(/\D+/g), f3 = format.split(/[a-z]+/gi), f4 = input.split(/\d+/g);
    var len = f1.length, len1 = f3.length;
    if (len != f2.length || len1 != f4.length) return false;
    for (var i = 0; i < len1; i++) if (f3[i] != f4[i]) return false;
    for (var i = 0; i < len; i++) o[f1[i]] = f2[i];
    o.yyyy = s(o.yyyy, o.yy, d.getFullYear(), 9999, 4);
    o.MM = s(o.MM, o.M, d.getMonth() + 1, 12);
    o.dd = s(o.dd, o.d, d.getDate(), 31);
    o.hh = s(o.hh, o.h, d.getHours(), 24);
    o.mm = s(o.mm, o.m, d.getMinutes());
    o.ss = s(o.ss, o.s, d.getSeconds());
    o.ms = s(o.ms, o.ms, d.getMilliseconds(), 999, 3);
    if (o.yyyy + o.MM + o.dd + o.hh + o.mm + o.ss + o.ms < 0) return false;
    if (o.yyyy < 100) o.yyyy += (o.yyyy > 30 ? 1900 : 2000);
    d = new Date(o.yyyy, o.MM - 1, o.dd, o.hh, o.mm, o.ss, o.ms);
    var reVal = d.getFullYear() == o.yyyy && d.getMonth() + 1 == o.MM && d.getDate() == o.dd && d.getHours() == o.hh && d.getMinutes() == o.mm && d.getSeconds() == o.ss && d.getMilliseconds() == o.ms;
    return reVal && reObj ? d : reVal;
    function s(s1, s2, s3, s4, s5) {
        s4 = s4 || 60, s5 = s5 || 2;
        var reVal = s3;
        if (s1 != undefined && s1 != '' || !isNaN(s1)) reVal = s1 * 1;
        if (s2 != undefined && s2 != '' && !isNaN(s2)) reVal = s2 * 1;
        return (reVal == s1 && s1.length != s5 || reVal > s4) ? -10000 : reVal;
    }
};

/*function checkIdcard(idcard) {
	var area={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",
	  23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",
	  41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",
	  52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",
	  71:"台湾",81:"香港",82:"澳门",91:"国外"} 
	var idcard, Y, JYM;
	var S, M;
	var idcard_array = new Array();
	idcard_array = idcard.split("");
	//地区检验 
	if (area[parseInt(idcard.substr(0, 2))] == null)
		return false;
	//身份号码位数及格式检验 
	switch (idcard.length) {
	case 15:
		if ((parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0
				|| ((parseInt(idcard.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(idcard
						.substr(6, 2)) + 1900) % 4 == 0)) {
			ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/; //测试出生日期的合法性 
		} else {
			ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/; //测试出生日期的合法性 
		}
		if (ereg.test(idcard))
			return false;
		else
			return false;
		break;
	case 18:
		//18位身份号码检测 
		//出生日期的合法性检查  
		//闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9])) 
		//平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8])) 
		if (parseInt(idcard.substr(6, 4)) % 4 == 0
				|| (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard
						.substr(6, 4)) % 4 == 0)) {
			ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/; //闰年出生日期的合法性正则表达式 
		} else {
			ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/; //平年出生日期的合法性正则表达式 
		}
		if (ereg.test(idcard)) {//测试出生日期的合法性 
			//计算校验位 
			S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
					+ (parseInt(idcard_array[1]) + parseInt(idcard_array[11]))* 9
					+ (parseInt(idcard_array[2]) + parseInt(idcard_array[12]))* 10
					+ (parseInt(idcard_array[3]) + parseInt(idcard_array[13]))* 5
					+ (parseInt(idcard_array[4]) + parseInt(idcard_array[14]))* 8
					+ (parseInt(idcard_array[5]) + parseInt(idcard_array[15]))* 4
					+ (parseInt(idcard_array[6]) + parseInt(idcard_array[16]))* 2
					+ parseInt(idcard_array[7]) * 1
					+ parseInt(idcard_array[8]) * 6 
					+ parseInt(idcard_array[9]) * 3;
			Y = S % 11;
			M = "F";
			JYM = "10X98765432";
			M = JYM.substr(Y, 1); //判断校验位 
			if (M == idcard_array[17])
				return true; //检测ID的校验位 
			else
				return false;
		} else
			return false;
		break;
	default:
		return false;
		break;
	}
}*/

//去掉字符串头尾空格   
function trim(str) {   
    return str.replace(/(^\s*)|(\s*$)/g, "");   
}
$.validate.errorMsg = {
    required: "必填项",
    minLength: "此处输入长度不可小于#{min}位",
    maxLength: "此处输入长度不可大于#{max}位",
    rangeLength: "\u8BE5\u5B57\u6BB5\u7684\u957F\u5EA6\u5FC5\u987B\u5728\#{min}\u548C\#{max}\u4E4B\u95F4",
    min: "\u8BE5\u5B57\u6BB5\u7684\u503C\u4E0D\u80FD\u5C0F\u4E8E\#{min}",
    max: "\u8BE5\u5B57\u6BB5\u7684\u503C\u4E0D\u80FD\u5927\u4E8E\#{max}",
    range: "\u8BE5\u5B57\u6BB5\u7684\u503C\u5FC5\u987B\u5728\#{min}\u548C\#{max}\u4E4B\u95F4",
    email: "\u8FD9\u4E0D\u662F\u4E00\u4E2A\u6807\u51C6\u7684Email\u5730\u5740",
    url: "\u8FD9\u4E0D\u662F\u4E00\u4E2A\u6807\u51C6\u7684url\u5730\u5740",
    date: "\u8FD9\u4E0D\u662F\u4E00\u4E2A\u6807\u51C6\u7684\u65E5\u671F\u683C\u5F0F",
    dateISO: "日期格式错误。",
    number: "\u8BE5\u5B57\u6BB5\u53EA\u80FD\u4E3A\u6570\u5B57",
    digits: "\u8BE5\u5B57\u6BB5\u53EA\u80FD\u4E3A\u6570\u5B57",
    identitycard:"\u8fd9\u4e0d\u662f\u4e00\u4e2a\u6807\u51c6\u7684\u8eab\u4efd\u8bc1\u683c\u5f0f",
    isonlynumber:"只能是整数",
    isonlycharacter:"姓名不合法",
    numberCheck:"小数点前请保留#{min}位,小数点后请保留#{max}位",
    phoneNumber:"联系号码格式不合法",
    age:"年龄输入有误",
    dateCheck:"所填日期不能大于当前日期",
    dateAfter:"所填日期不能小于#{msg}",
    numberLimit:"此处必须输入#{length}个数字",
    idCardCheck:"身份证不合法",
    idCardCheckChild:"只能输入数字或字母",
    uniqueIdCard:"该身份证已存在",
    minrE:"此处必须大于#{minrE}",
    decimalPointBefore:"小数点前长度不能超过#{num}位",
    decimalPointAfter:"小数点后长度不能超过#{num}位",
    surplusIsEnough:"剩余量不足",
    digital: "只能为大于0且不超过两位小数的数字"
};
    
})( jQuery);