// Compiled by ClojureScript 1.7.170 {:static-fns true, :optimize-constants true}
goog.provide('ini_editor.widgets');
goog.require('cljs.core');
goog.require('ini_editor.utils');
goog.require('reagent.core');
ini_editor.widgets.from_file_list_ = (function ini_editor$widgets$from_file_list_(file_list){
var len = file_list.length;
return cljs.core.map.cljs$core$IFn$_invoke$arity$2(((function (len){
return (function (p1__11292_SHARP_){
return file_list.item(p1__11292_SHARP_);
});})(len))
,cljs.core.range.cljs$core$IFn$_invoke$arity$1(len));
});
ini_editor.widgets.file_input = (function ini_editor$widgets$file_input(props){
var map__11297 = props;
var map__11297__$1 = ((((!((map__11297 == null)))?((((map__11297.cljs$lang$protocol_mask$partition0$ & (64))) || (map__11297.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__11297):map__11297);
var file_types = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11297__$1,cljs.core.cst$kw$file_DASH_types);
var on_change = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11297__$1,cljs.core.cst$kw$on_DASH_change);
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$input,new cljs.core.PersistentArrayMap(null, 3, [cljs.core.cst$kw$on_DASH_change,((function (map__11297,map__11297__$1,file_types,on_change){
return (function (p1__11293_SHARP_){
var G__11299 = ini_editor.widgets.from_file_list_(p1__11293_SHARP_.target.files);
return (on_change.cljs$core$IFn$_invoke$arity$1 ? on_change.cljs$core$IFn$_invoke$arity$1(G__11299) : on_change.call(null,G__11299));
});})(map__11297,map__11297__$1,file_types,on_change))
,cljs.core.cst$kw$type,"file",cljs.core.cst$kw$accept,file_types], null)], null);
});
ini_editor.widgets.dropdown_input = (function ini_editor$widgets$dropdown_input(props){
var map__11305 = props;
var map__11305__$1 = ((((!((map__11305 == null)))?((((map__11305.cljs$lang$protocol_mask$partition0$ & (64))) || (map__11305.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__11305):map__11305);
var disabled = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11305__$1,cljs.core.cst$kw$disabled);
var options = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11305__$1,cljs.core.cst$kw$options);
var value = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11305__$1,cljs.core.cst$kw$value);
var on_change = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11305__$1,cljs.core.cst$kw$on_DASH_change);
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$select,new cljs.core.PersistentArrayMap(null, 4, [cljs.core.cst$kw$class,"form-control",cljs.core.cst$kw$disabled,disabled,cljs.core.cst$kw$on_DASH_change,((function (map__11305,map__11305__$1,disabled,options,value,on_change){
return (function (p1__11300_SHARP_){
var G__11307 = p1__11300_SHARP_.target.value;
return (on_change.cljs$core$IFn$_invoke$arity$1 ? on_change.cljs$core$IFn$_invoke$arity$1(G__11307) : on_change.call(null,G__11307));
});})(map__11305,map__11305__$1,disabled,options,value,on_change))
,cljs.core.cst$kw$value,value], null),cljs.core.map.cljs$core$IFn$_invoke$arity$2(((function (map__11305,map__11305__$1,disabled,options,value,on_change){
return (function (o){
var vec__11308 = ((cljs.core.vector_QMARK_(o))?o:new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [o,o], null));
var l = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11308,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11308,(1),null);
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$option,new cljs.core.PersistentArrayMap(null, 2, [cljs.core.cst$kw$key,v,cljs.core.cst$kw$value,v], null),l], null);
});})(map__11305,map__11305__$1,disabled,options,value,on_change))
,options)], null);
});
ini_editor.widgets.list_input = (function ini_editor$widgets$list_input(props){
var map__11315 = props;
var map__11315__$1 = ((((!((map__11315 == null)))?((((map__11315.cljs$lang$protocol_mask$partition0$ & (64))) || (map__11315.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__11315):map__11315);
var disabled = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11315__$1,cljs.core.cst$kw$disabled);
var on_change = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11315__$1,cljs.core.cst$kw$on_DASH_change);
var value = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11315__$1,cljs.core.cst$kw$value);
var new_value = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11315__$1,cljs.core.cst$kw$new_DASH_value);
if(cljs.core.vector_QMARK_(value)){
} else {
throw (new Error([cljs.core.str("Assert failed: "),cljs.core.str("value passed to list-input must be a vector."),cljs.core.str("\n"),cljs.core.str(cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.array_seq([cljs.core.list(cljs.core.cst$sym$vector_QMARK_,cljs.core.cst$sym$value)], 0)))].join('')));
}

return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$span,cljs.core.PersistentArrayMap.EMPTY,cljs.core.map_indexed.cljs$core$IFn$_invoke$arity$2(((function (map__11315,map__11315__$1,disabled,on_change,value,new_value){
return (function (i,v){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div,new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$key,i], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$input,new cljs.core.PersistentArrayMap(null, 5, [cljs.core.cst$kw$disabled,disabled,cljs.core.cst$kw$class,"form-control",cljs.core.cst$kw$style,new cljs.core.PersistentArrayMap(null, 2, [cljs.core.cst$kw$display,"inline",cljs.core.cst$kw$width,"90%"], null),cljs.core.cst$kw$on_DASH_change,((function (map__11315,map__11315__$1,disabled,on_change,value,new_value){
return (function (p1__11309_SHARP_){
var G__11317 = cljs.core.assoc.cljs$core$IFn$_invoke$arity$3(value,i,p1__11309_SHARP_.target.value);
return (on_change.cljs$core$IFn$_invoke$arity$1 ? on_change.cljs$core$IFn$_invoke$arity$1(G__11317) : on_change.call(null,G__11317));
});})(map__11315,map__11315__$1,disabled,on_change,value,new_value))
,cljs.core.cst$kw$value,v], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$button,new cljs.core.PersistentArrayMap(null, 3, [cljs.core.cst$kw$class,"btn btn-default",cljs.core.cst$kw$on_DASH_click,((function (map__11315,map__11315__$1,disabled,on_change,value,new_value){
return (function (){
var G__11318 = ini_editor.utils.remove_idx(value,i);
return (on_change.cljs$core$IFn$_invoke$arity$1 ? on_change.cljs$core$IFn$_invoke$arity$1(G__11318) : on_change.call(null,G__11318));
});})(map__11315,map__11315__$1,disabled,on_change,value,new_value))
,cljs.core.cst$kw$style,new cljs.core.PersistentArrayMap(null, 2, [cljs.core.cst$kw$background_DASH_color,"orangered",cljs.core.cst$kw$border_DASH_color,"crimson"], null)], null),"-"], null)], null);
});})(map__11315,map__11315__$1,disabled,on_change,value,new_value))
,value),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$button,new cljs.core.PersistentArrayMap(null, 2, [cljs.core.cst$kw$class,"btn btn-default",cljs.core.cst$kw$on_DASH_click,((function (map__11315,map__11315__$1,disabled,on_change,value,new_value){
return (function (){
var G__11319 = cljs.core.conj.cljs$core$IFn$_invoke$arity$2(value,new_value);
return (on_change.cljs$core$IFn$_invoke$arity$1 ? on_change.cljs$core$IFn$_invoke$arity$1(G__11319) : on_change.call(null,G__11319));
});})(map__11315,map__11315__$1,disabled,on_change,value,new_value))
], null),"+"], null)], null);
});
ini_editor.widgets.test_file_value = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.List.EMPTY);
ini_editor.widgets.test_dropdown_value = reagent.core.atom.cljs$core$IFn$_invoke$arity$1("a");
ini_editor.widgets.test_widgets = (function ini_editor$widgets$test_widgets(props){
var file_value = ini_editor.widgets.test_file_value;
var dropdown_value = ini_editor.widgets.test_dropdown_value;
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div$test_DASH_widgtets,cljs.core.PersistentArrayMap.EMPTY,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div,cljs.core.PersistentArrayMap.EMPTY,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [ini_editor.widgets.file_input,new cljs.core.PersistentArrayMap(null, 2, [cljs.core.cst$kw$file_DASH_types,"*",cljs.core.cst$kw$on_DASH_change,((function (file_value,dropdown_value){
return (function (p1__11320_SHARP_){
return (cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(file_value,p1__11320_SHARP_) : cljs.core.reset_BANG_.call(null,file_value,p1__11320_SHARP_));
});})(file_value,dropdown_value))
], null)], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div,cljs.core.PersistentArrayMap.EMPTY,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [ini_editor.widgets.dropdown_input,new cljs.core.PersistentArrayMap(null, 3, [cljs.core.cst$kw$options,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, ["a","b","c"], null),cljs.core.cst$kw$value,(cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(dropdown_value) : cljs.core.deref.call(null,dropdown_value)),cljs.core.cst$kw$on_DASH_change,((function (file_value,dropdown_value){
return (function (p1__11321_SHARP_){
return (cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(dropdown_value,p1__11321_SHARP_) : cljs.core.reset_BANG_.call(null,dropdown_value,p1__11321_SHARP_));
});})(file_value,dropdown_value))
], null)], null)], null)], null);
});
