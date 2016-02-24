// Compiled by ClojureScript 1.7.170 {:static-fns true, :optimize-constants true}
goog.provide('reagent.dom');
goog.require('cljs.core');
goog.require('reagent.impl.util');
goog.require('reagent.impl.template');
goog.require('reagent.debug');
goog.require('reagent.interop');
if(typeof reagent.dom.dom !== 'undefined'){
} else {
reagent.dom.dom = (function (){var or__4988__auto__ = (function (){var and__4976__auto__ = typeof ReactDOM !== 'undefined';
if(and__4976__auto__){
return ReactDOM;
} else {
return and__4976__auto__;
}
})();
if(cljs.core.truth_(or__4988__auto__)){
return or__4988__auto__;
} else {
var and__4976__auto__ = typeof require !== 'undefined';
if(and__4976__auto__){
return require("react-dom");
} else {
return and__4976__auto__;
}
}
})();
}
if(cljs.core.truth_(reagent.dom.dom)){
} else {
throw (new Error([cljs.core.str("Assert failed: "),cljs.core.str("Could not find ReactDOM"),cljs.core.str("\n"),cljs.core.str(cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.array_seq([cljs.core.cst$sym$dom], 0)))].join('')));
}
if(typeof reagent.dom.roots !== 'undefined'){
} else {
reagent.dom.roots = (function (){var G__11161 = cljs.core.PersistentArrayMap.EMPTY;
return (cljs.core.atom.cljs$core$IFn$_invoke$arity$1 ? cljs.core.atom.cljs$core$IFn$_invoke$arity$1(G__11161) : cljs.core.atom.call(null,G__11161));
})();
}
reagent.dom.unmount_comp = (function reagent$dom$unmount_comp(container){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(reagent.dom.roots,cljs.core.dissoc,container);

return (reagent.dom.dom["unmountComponentAtNode"])(container);
});
reagent.dom.render_comp = (function reagent$dom$render_comp(comp,container,callback){
var _STAR_always_update_STAR_11164 = reagent.impl.util._STAR_always_update_STAR_;
reagent.impl.util._STAR_always_update_STAR_ = true;

try{return (reagent.dom.dom["render"])((comp.cljs$core$IFn$_invoke$arity$0 ? comp.cljs$core$IFn$_invoke$arity$0() : comp.call(null)),container,((function (_STAR_always_update_STAR_11164){
return (function (){
var _STAR_always_update_STAR_11165 = reagent.impl.util._STAR_always_update_STAR_;
reagent.impl.util._STAR_always_update_STAR_ = false;

try{cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(reagent.dom.roots,cljs.core.assoc,container,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [comp,container], null));

if(cljs.core.some_QMARK_(callback)){
return (callback.cljs$core$IFn$_invoke$arity$0 ? callback.cljs$core$IFn$_invoke$arity$0() : callback.call(null));
} else {
return null;
}
}finally {reagent.impl.util._STAR_always_update_STAR_ = _STAR_always_update_STAR_11165;
}});})(_STAR_always_update_STAR_11164))
);
}finally {reagent.impl.util._STAR_always_update_STAR_ = _STAR_always_update_STAR_11164;
}});
reagent.dom.re_render_component = (function reagent$dom$re_render_component(comp,container){
return reagent.dom.render_comp(comp,container,null);
});
/**
 * Render a Reagent component into the DOM. The first argument may be
 *   either a vector (using Reagent's Hiccup syntax), or a React element. The second argument should be a DOM node.
 * 
 *   Optionally takes a callback that is called when the component is in place.
 * 
 *   Returns the mounted component instance.
 */
reagent.dom.render = (function reagent$dom$render(var_args){
var args11166 = [];
var len__6046__auto___11169 = arguments.length;
var i__6047__auto___11170 = (0);
while(true){
if((i__6047__auto___11170 < len__6046__auto___11169)){
args11166.push((arguments[i__6047__auto___11170]));

var G__11171 = (i__6047__auto___11170 + (1));
i__6047__auto___11170 = G__11171;
continue;
} else {
}
break;
}

var G__11168 = args11166.length;
switch (G__11168) {
case 2:
return reagent.dom.render.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
case 3:
return reagent.dom.render.cljs$core$IFn$_invoke$arity$3((arguments[(0)]),(arguments[(1)]),(arguments[(2)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args11166.length)].join('')));

}
});

reagent.dom.render.cljs$core$IFn$_invoke$arity$2 = (function (comp,container){
return reagent.dom.render.cljs$core$IFn$_invoke$arity$3(comp,container,null);
});

reagent.dom.render.cljs$core$IFn$_invoke$arity$3 = (function (comp,container,callback){
var f = (function (){
return reagent.impl.template.as_element(((cljs.core.fn_QMARK_(comp))?(comp.cljs$core$IFn$_invoke$arity$0 ? comp.cljs$core$IFn$_invoke$arity$0() : comp.call(null)):comp));
});
return reagent.dom.render_comp(f,container,callback);
});

reagent.dom.render.cljs$lang$maxFixedArity = 3;
reagent.dom.unmount_component_at_node = (function reagent$dom$unmount_component_at_node(container){
return reagent.dom.unmount_comp(container);
});
/**
 * Returns the root DOM node of a mounted component.
 */
reagent.dom.dom_node = (function reagent$dom$dom_node(this$){
return (reagent.dom.dom["findDOMNode"])(this$);
});
reagent.impl.template.find_dom_node = reagent.dom.dom_node;
/**
 * Force re-rendering of all mounted Reagent components. This is
 *   probably only useful in a development environment, when you want to
 *   update components in response to some dynamic changes to code.
 * 
 *   Note that force-update-all may not update root components. This
 *   happens if a component 'foo' is mounted with `(render [foo])` (since
 *   functions are passed by value, and not by reference, in
 *   ClojureScript). To get around this you'll have to introduce a layer
 *   of indirection, for example by using `(render [#'foo])` instead.
 */
reagent.dom.force_update_all = (function reagent$dom$force_update_all(){
var seq__11177_11181 = cljs.core.seq(cljs.core.vals((cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(reagent.dom.roots) : cljs.core.deref.call(null,reagent.dom.roots))));
var chunk__11178_11182 = null;
var count__11179_11183 = (0);
var i__11180_11184 = (0);
while(true){
if((i__11180_11184 < count__11179_11183)){
var v_11185 = chunk__11178_11182.cljs$core$IIndexed$_nth$arity$2(null,i__11180_11184);
cljs.core.apply.cljs$core$IFn$_invoke$arity$2(reagent.dom.re_render_component,v_11185);

var G__11186 = seq__11177_11181;
var G__11187 = chunk__11178_11182;
var G__11188 = count__11179_11183;
var G__11189 = (i__11180_11184 + (1));
seq__11177_11181 = G__11186;
chunk__11178_11182 = G__11187;
count__11179_11183 = G__11188;
i__11180_11184 = G__11189;
continue;
} else {
var temp__4425__auto___11190 = cljs.core.seq(seq__11177_11181);
if(temp__4425__auto___11190){
var seq__11177_11191__$1 = temp__4425__auto___11190;
if(cljs.core.chunked_seq_QMARK_(seq__11177_11191__$1)){
var c__5791__auto___11192 = cljs.core.chunk_first(seq__11177_11191__$1);
var G__11193 = cljs.core.chunk_rest(seq__11177_11191__$1);
var G__11194 = c__5791__auto___11192;
var G__11195 = cljs.core.count(c__5791__auto___11192);
var G__11196 = (0);
seq__11177_11181 = G__11193;
chunk__11178_11182 = G__11194;
count__11179_11183 = G__11195;
i__11180_11184 = G__11196;
continue;
} else {
var v_11197 = cljs.core.first(seq__11177_11191__$1);
cljs.core.apply.cljs$core$IFn$_invoke$arity$2(reagent.dom.re_render_component,v_11197);

var G__11198 = cljs.core.next(seq__11177_11191__$1);
var G__11199 = null;
var G__11200 = (0);
var G__11201 = (0);
seq__11177_11181 = G__11198;
chunk__11178_11182 = G__11199;
count__11179_11183 = G__11200;
i__11180_11184 = G__11201;
continue;
}
} else {
}
}
break;
}

return "Updated";
});
