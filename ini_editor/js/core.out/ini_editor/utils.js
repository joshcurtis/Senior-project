// Compiled by ClojureScript 1.7.170 {:static-fns true, :optimize-constants true}
goog.provide('ini_editor.utils');
goog.require('cljs.core');
cljs.core.enable_console_print_BANG_();
/**
 * Given a list l, l is returned with f applied to the first element.
 */
ini_editor.utils.update_list_head = (function ini_editor$utils$update_list_head(l,f){
if(cljs.core.list_QMARK_(l)){
} else {
throw (new Error([cljs.core.str("Assert failed: "),cljs.core.str("l is not a list"),cljs.core.str("\n"),cljs.core.str(cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.array_seq([cljs.core.list(cljs.core.cst$sym$list_QMARK_,cljs.core.cst$sym$l)], 0)))].join('')));
}

var vec__11155 = l;
var head = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11155,(0),null);
var tail = cljs.core.nthnext(vec__11155,(1));
return cljs.core.conj.cljs$core$IFn$_invoke$arity$2(tail,(f.cljs$core$IFn$_invoke$arity$1 ? f.cljs$core$IFn$_invoke$arity$1(head) : f.call(null,head)));
});
/**
 * Give a vector v, v is returned with item v removed.
 */
ini_editor.utils.remove_idx = (function ini_editor$utils$remove_idx(v,idx){
if(cljs.core.vector_QMARK_(v)){
} else {
throw (new Error([cljs.core.str("Assert failed: "),cljs.core.str("v is not a vector"),cljs.core.str("\n"),cljs.core.str(cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.array_seq([cljs.core.list(cljs.core.cst$sym$vector_QMARK_,cljs.core.cst$sym$v)], 0)))].join('')));
}

return cljs.core.vec(cljs.core.concat.cljs$core$IFn$_invoke$arity$2(cljs.core.subvec.cljs$core$IFn$_invoke$arity$3(v,(0),idx),cljs.core.subvec.cljs$core$IFn$_invoke$arity$2(v,(idx + (1)))));
});
ini_editor.utils.read_file = (function ini_editor$utils$read_file(js_file_obj,callback){
var reader = (new FileReader());
var cback = ((function (reader){
return (function (p1__11156_SHARP_){
var G__11158 = p1__11156_SHARP_.target.result;
return (callback.cljs$core$IFn$_invoke$arity$1 ? callback.cljs$core$IFn$_invoke$arity$1(G__11158) : callback.call(null,G__11158));
});})(reader))
;
(reader["onload"] = cback);

return reader.readAsText(js_file_obj);
});
ini_editor.utils.click_element = (function ini_editor$utils$click_element(id){
return document.getElementById(id).click();
});
