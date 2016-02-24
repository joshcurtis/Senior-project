// Compiled by ClojureScript 1.7.170 {:static-fns true, :optimize-constants true}
goog.provide('ini_editor.controller');
goog.require('cljs.core');
goog.require('ini_editor.utils');
goog.require('ini_editor.model');
goog.require('ini_editor.parser');
goog.require('reagent.core');
ini_editor.controller.expand_all_BANG_ = (function ini_editor$controller$expand_all_BANG_(){
var G__11702 = ini_editor.model.expanded;
var G__11703 = cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,cljs.core.map.cljs$core$IFn$_invoke$arity$2(((function (G__11702){
return (function (p__11704){
var vec__11705 = p__11704;
var s = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11705,(0),null);
var ks = cljs.core.nthnext(vec__11705,(1));
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [s,true], null);
});})(G__11702))
,(cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.model.order) : cljs.core.deref.call(null,ini_editor.model.order))));
return (cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(G__11702,G__11703) : cljs.core.reset_BANG_.call(null,G__11702,G__11703));
});
ini_editor.controller.expand_important_BANG_ = (function ini_editor$controller$expand_important_BANG_(template){
if(cljs.core.some_QMARK_(template)){
} else {
throw (new Error([cljs.core.str("Assert failed: "),cljs.core.str("No template provided"),cljs.core.str("\n"),cljs.core.str(cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.array_seq([cljs.core.list(cljs.core.cst$sym$some_QMARK_,cljs.core.cst$sym$template)], 0)))].join('')));
}

var sections = cljs.core.keys(template);
var imps = cljs.core.filter.cljs$core$IFn$_invoke$arity$2(((function (sections){
return (function (s){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(template,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [s,cljs.core.cst$kw$important], null));
});})(sections))
,sections);
var G__11708 = ini_editor.model.expanded;
var G__11709 = cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,cljs.core.map.cljs$core$IFn$_invoke$arity$2(((function (G__11708,sections,imps){
return (function (s){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [s,true], null);
});})(G__11708,sections,imps))
,imps));
return (cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(G__11708,G__11709) : cljs.core.reset_BANG_.call(null,G__11708,G__11709));
});
ini_editor.controller.toggle_expanded_BANG_ = (function ini_editor$controller$toggle_expanded_BANG_(section){
if(typeof section === 'string'){
} else {
throw (new Error([cljs.core.str("Assert failed: "),cljs.core.str(cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.array_seq([cljs.core.list(cljs.core.cst$sym$string_QMARK_,cljs.core.cst$sym$section)], 0)))].join('')));
}

if(cljs.core.truth_(cljs.core.get.cljs$core$IFn$_invoke$arity$2((cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.model.expanded) : cljs.core.deref.call(null,ini_editor.model.expanded)),section))){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(ini_editor.model.expanded,cljs.core.dissoc,section);
} else {
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(ini_editor.model.expanded,cljs.core.assoc,section,true);
}
});
ini_editor.controller.unload_file_BANG_ = (function ini_editor$controller$unload_file_BANG_(){
var G__11718_11726 = ini_editor.model.ini;
var G__11719_11727 = cljs.core.PersistentArrayMap.EMPTY;
(cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(G__11718_11726,G__11719_11727) : cljs.core.reset_BANG_.call(null,G__11718_11726,G__11719_11727));

var G__11720_11728 = ini_editor.model.template;
var G__11721_11729 = cljs.core.PersistentArrayMap.EMPTY;
(cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(G__11720_11728,G__11721_11729) : cljs.core.reset_BANG_.call(null,G__11720_11728,G__11721_11729));

var G__11722_11730 = ini_editor.model.order;
var G__11723_11731 = cljs.core.List.EMPTY;
(cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(G__11722_11730,G__11723_11731) : cljs.core.reset_BANG_.call(null,G__11722_11730,G__11723_11731));

var G__11724 = ini_editor.model.expanded;
var G__11725 = cljs.core.PersistentArrayMap.EMPTY;
return (cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(G__11724,G__11725) : cljs.core.reset_BANG_.call(null,G__11724,G__11725));
});
ini_editor.controller.upload_file_BANG_ = (function ini_editor$controller$upload_file_BANG_(f){
if(cljs.core.some_QMARK_(f)){
} else {
throw (new Error([cljs.core.str("Assert failed: "),cljs.core.str("No file specified for model/upload-file!"),cljs.core.str("\n"),cljs.core.str(cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.array_seq([cljs.core.list(cljs.core.cst$sym$some_QMARK_,cljs.core.cst$sym$f)], 0)))].join('')));
}

var callback_BANG_ = (function (s){
var order = ini_editor.parser.parse_ini_order(s);
var template = ini_editor.parser.parse_ini_template(s);
var ini = ini_editor.parser.parse_ini.cljs$core$IFn$_invoke$arity$2(s,template);
(cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(ini_editor.model.ini,ini) : cljs.core.reset_BANG_.call(null,ini_editor.model.ini,ini));

(cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(ini_editor.model.template,template) : cljs.core.reset_BANG_.call(null,ini_editor.model.template,template));

(cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(ini_editor.model.order,order) : cljs.core.reset_BANG_.call(null,ini_editor.model.order,order));

return ini_editor.controller.expand_important_BANG_(template);
});
ini_editor.controller.unload_file_BANG_();

return ini_editor.utils.read_file(f,callback_BANG_);
});
ini_editor.controller.set_ini_key_value_BANG_ = (function ini_editor$controller$set_ini_key_value_BANG_(section,key,value){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(ini_editor.model.ini,cljs.core.assoc_in,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [section,cljs.core.cst$kw$keys,key,cljs.core.cst$kw$value], null),value);
});
