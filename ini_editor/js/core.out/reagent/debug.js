// Compiled by ClojureScript 1.7.170 {:static-fns true, :optimize-constants true}
goog.provide('reagent.debug');
goog.require('cljs.core');
reagent.debug.has_console = typeof console !== 'undefined';
reagent.debug.tracking = false;
if(typeof reagent.debug.warnings !== 'undefined'){
} else {
reagent.debug.warnings = (cljs.core.atom.cljs$core$IFn$_invoke$arity$1 ? cljs.core.atom.cljs$core$IFn$_invoke$arity$1(null) : cljs.core.atom.call(null,null));
}
if(typeof reagent.debug.track_console !== 'undefined'){
} else {
reagent.debug.track_console = (function (){var o = {};
o.warn = ((function (o){
return (function() { 
var G__10673__delegate = function (args){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(reagent.debug.warnings,cljs.core.update_in,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$warn], null),cljs.core.conj,cljs.core.array_seq([cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.str,args)], 0));
};
var G__10673 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__10674__i = 0, G__10674__a = new Array(arguments.length -  0);
while (G__10674__i < G__10674__a.length) {G__10674__a[G__10674__i] = arguments[G__10674__i + 0]; ++G__10674__i;}
  args = new cljs.core.IndexedSeq(G__10674__a,0);
} 
return G__10673__delegate.call(this,args);};
G__10673.cljs$lang$maxFixedArity = 0;
G__10673.cljs$lang$applyTo = (function (arglist__10675){
var args = cljs.core.seq(arglist__10675);
return G__10673__delegate(args);
});
G__10673.cljs$core$IFn$_invoke$arity$variadic = G__10673__delegate;
return G__10673;
})()
;})(o))
;

o.error = ((function (o){
return (function() { 
var G__10676__delegate = function (args){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(reagent.debug.warnings,cljs.core.update_in,new cljs.core.PersistentVector(null, 1, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$error], null),cljs.core.conj,cljs.core.array_seq([cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.str,args)], 0));
};
var G__10676 = function (var_args){
var args = null;
if (arguments.length > 0) {
var G__10677__i = 0, G__10677__a = new Array(arguments.length -  0);
while (G__10677__i < G__10677__a.length) {G__10677__a[G__10677__i] = arguments[G__10677__i + 0]; ++G__10677__i;}
  args = new cljs.core.IndexedSeq(G__10677__a,0);
} 
return G__10676__delegate.call(this,args);};
G__10676.cljs$lang$maxFixedArity = 0;
G__10676.cljs$lang$applyTo = (function (arglist__10678){
var args = cljs.core.seq(arglist__10678);
return G__10676__delegate(args);
});
G__10676.cljs$core$IFn$_invoke$arity$variadic = G__10676__delegate;
return G__10676;
})()
;})(o))
;

return o;
})();
}
reagent.debug.track_warnings = (function reagent$debug$track_warnings(f){
reagent.debug.tracking = true;

(cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(reagent.debug.warnings,null) : cljs.core.reset_BANG_.call(null,reagent.debug.warnings,null));

(f.cljs$core$IFn$_invoke$arity$0 ? f.cljs$core$IFn$_invoke$arity$0() : f.call(null));

var warns = (cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(reagent.debug.warnings) : cljs.core.deref.call(null,reagent.debug.warnings));
(cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(reagent.debug.warnings,null) : cljs.core.reset_BANG_.call(null,reagent.debug.warnings,null));

reagent.debug.tracking = false;

return warns;
});
