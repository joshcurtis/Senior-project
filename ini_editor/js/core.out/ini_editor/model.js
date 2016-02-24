// Compiled by ClojureScript 1.7.170 {:static-fns true, :optimize-constants true}
goog.provide('ini_editor.model');
goog.require('cljs.core');
goog.require('reagent.core');
cljs.core.enable_console_print_BANG_();
ini_editor.model.show_debug_QMARK_ = true;
if(typeof ini_editor.model.ini !== 'undefined'){
} else {
ini_editor.model.ini = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
}
if(typeof ini_editor.model.template !== 'undefined'){
} else {
ini_editor.model.template = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
}
if(typeof ini_editor.model.order !== 'undefined'){
} else {
ini_editor.model.order = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.List.EMPTY);
}
if(typeof ini_editor.model.expanded !== 'undefined'){
} else {
ini_editor.model.expanded = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
}
