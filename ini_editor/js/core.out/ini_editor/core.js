// Compiled by ClojureScript 1.7.170 {:static-fns true, :optimize-constants true}
goog.provide('ini_editor.core');
goog.require('cljs.core');
goog.require('ini_editor.view');
goog.require('reagent.core');
cljs.core.enable_console_print_BANG_();
ini_editor.core.start = (function ini_editor$core$start(){
var G__11770 = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [ini_editor.view.view,cljs.core.PersistentArrayMap.EMPTY], null);
var G__11771 = document.getElementById("app");
return (reagent.core.render_component.cljs$core$IFn$_invoke$arity$2 ? reagent.core.render_component.cljs$core$IFn$_invoke$arity$2(G__11770,G__11771) : reagent.core.render_component.call(null,G__11770,G__11771));
});
goog.exportSymbol('ini_editor.core.start', ini_editor.core.start);
