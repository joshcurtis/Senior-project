// Compiled by ClojureScript 1.7.170 {:static-fns true, :optimize-constants true}
goog.provide('ini_editor.core');
goog.require('cljs.core');
goog.require('ini_editor.widgets');
goog.require('ini_editor.utils');
goog.require('reagent.core');
goog.require('ini_editor.parser');
goog.require('clojure.string');
cljs.core.enable_console_print_BANG_();
ini_editor.core.debug_mode_QMARK_ = true;
if(typeof ini_editor.core.file_atom !== 'undefined'){
} else {
ini_editor.core.file_atom = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(null);
}
if(typeof ini_editor.core.ini_atom !== 'undefined'){
} else {
ini_editor.core.ini_atom = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
}
if(typeof ini_editor.core.template_atom !== 'undefined'){
} else {
ini_editor.core.template_atom = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
}
if(typeof ini_editor.core.order_atom !== 'undefined'){
} else {
ini_editor.core.order_atom = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.List.EMPTY);
}
if(typeof ini_editor.core.expanded_atom !== 'undefined'){
} else {
ini_editor.core.expanded_atom = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(cljs.core.PersistentArrayMap.EMPTY);
}
if(typeof ini_editor.core.show_debug_QMARK_ !== 'undefined'){
} else {
ini_editor.core.show_debug_QMARK_ = reagent.core.atom.cljs$core$IFn$_invoke$arity$1(true);
}
ini_editor.core.expand_all_BANG_ = (function ini_editor$core$expand_all_BANG_(){
var G__11702 = ini_editor.core.expanded_atom;
var G__11703 = cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,cljs.core.map.cljs$core$IFn$_invoke$arity$2(((function (G__11702){
return (function (p__11704){
var vec__11705 = p__11704;
var s = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11705,(0),null);
var ks = cljs.core.nthnext(vec__11705,(1));
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [s,true], null);
});})(G__11702))
,(cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.core.order_atom) : cljs.core.deref.call(null,ini_editor.core.order_atom))));
return (cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(G__11702,G__11703) : cljs.core.reset_BANG_.call(null,G__11702,G__11703));
});
ini_editor.core.expand_important_BANG_ = (function ini_editor$core$expand_important_BANG_(template){
var sections = cljs.core.keys(template);
var imps = cljs.core.filter.cljs$core$IFn$_invoke$arity$2(((function (sections){
return (function (s){
return cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(template,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [s,cljs.core.cst$kw$important], null));
});})(sections))
,sections);
var G__11708 = ini_editor.core.expanded_atom;
var G__11709 = cljs.core.into.cljs$core$IFn$_invoke$arity$2(cljs.core.PersistentArrayMap.EMPTY,cljs.core.map.cljs$core$IFn$_invoke$arity$2(((function (G__11708,sections,imps){
return (function (s){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [s,true], null);
});})(G__11708,sections,imps))
,imps));
return (cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(G__11708,G__11709) : cljs.core.reset_BANG_.call(null,G__11708,G__11709));
});
ini_editor.core.toggle_expanded_BANG_ = (function ini_editor$core$toggle_expanded_BANG_(section){
if(cljs.core.truth_(cljs.core.get.cljs$core$IFn$_invoke$arity$2((cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.core.expanded_atom) : cljs.core.deref.call(null,ini_editor.core.expanded_atom)),section))){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(ini_editor.core.expanded_atom,cljs.core.dissoc,section);
} else {
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(ini_editor.core.expanded_atom,cljs.core.assoc,section,true);
}
});
ini_editor.core.unload_ini_BANG_ = (function ini_editor$core$unload_ini_BANG_(){
var G__11718_11726 = ini_editor.core.ini_atom;
var G__11719_11727 = cljs.core.PersistentArrayMap.EMPTY;
(cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(G__11718_11726,G__11719_11727) : cljs.core.reset_BANG_.call(null,G__11718_11726,G__11719_11727));

var G__11720_11728 = ini_editor.core.template_atom;
var G__11721_11729 = cljs.core.PersistentArrayMap.EMPTY;
(cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(G__11720_11728,G__11721_11729) : cljs.core.reset_BANG_.call(null,G__11720_11728,G__11721_11729));

var G__11722_11730 = ini_editor.core.order_atom;
var G__11723_11731 = cljs.core.List.EMPTY;
(cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(G__11722_11730,G__11723_11731) : cljs.core.reset_BANG_.call(null,G__11722_11730,G__11723_11731));

var G__11724 = ini_editor.core.expanded_atom;
var G__11725 = cljs.core.PersistentArrayMap.EMPTY;
return (cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(G__11724,G__11725) : cljs.core.reset_BANG_.call(null,G__11724,G__11725));
});
ini_editor.core.upload_file_BANG_ = (function ini_editor$core$upload_file_BANG_(){
var callback_BANG_ = (function (s){
var order = ini_editor.parser.parse_ini_order(s);
var template = ini_editor.parser.parse_ini_template(s);
var ini = ini_editor.parser.parse_ini.cljs$core$IFn$_invoke$arity$2(s,template);
(cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(ini_editor.core.ini_atom,ini) : cljs.core.reset_BANG_.call(null,ini_editor.core.ini_atom,ini));

(cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(ini_editor.core.template_atom,template) : cljs.core.reset_BANG_.call(null,ini_editor.core.template_atom,template));

(cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(ini_editor.core.order_atom,order) : cljs.core.reset_BANG_.call(null,ini_editor.core.order_atom,order));

return ini_editor.core.expand_important_BANG_(template);
});
ini_editor.core.unload_ini_BANG_();

return ini_editor.utils.read_file((cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.core.file_atom) : cljs.core.deref.call(null,ini_editor.core.file_atom)),callback_BANG_);
});
ini_editor.core.set_file_BANG_ = (function ini_editor$core$set_file_BANG_(f){
return (cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(ini_editor.core.file_atom,f) : cljs.core.reset_BANG_.call(null,ini_editor.core.file_atom,f));
});
ini_editor.core.set_ini_key_value_BANG_ = (function ini_editor$core$set_ini_key_value_BANG_(section,key,value){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(ini_editor.core.ini_atom,cljs.core.assoc_in,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [section,cljs.core.cst$kw$keys,key,cljs.core.cst$kw$value], null),value);
});
ini_editor.core.render_file_io = (function ini_editor$core$render_file_io(props){
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div$render_DASH_file_DASH_io,cljs.core.PersistentArrayMap.EMPTY,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [ini_editor.widgets.file_input,new cljs.core.PersistentArrayMap(null, 2, [cljs.core.cst$kw$file_DASH_types,".ini",cljs.core.cst$kw$on_DASH_change,(function (p1__11732_SHARP_){
return ini_editor.core.set_file_BANG_(cljs.core.first(p1__11732_SHARP_));
})], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$button,new cljs.core.PersistentArrayMap(null, 3, [cljs.core.cst$kw$class,"btn btn-default",cljs.core.cst$kw$on_DASH_click,ini_editor.core.upload_file_BANG_,cljs.core.cst$kw$type,"submit"], null),"Upload"], null)], null);
});
ini_editor.core.render_ini_key = (function ini_editor$core$render_ini_key(props){
var map__11742 = props;
var map__11742__$1 = ((((!((map__11742 == null)))?((((map__11742.cljs$lang$protocol_mask$partition0$ & (64))) || (map__11742.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__11742):map__11742);
var section = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11742__$1,cljs.core.cst$kw$section);
var key = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11742__$1,cljs.core.cst$kw$key);
var ini = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11742__$1,cljs.core.cst$kw$ini);
var template = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11742__$1,cljs.core.cst$kw$template);
var map__11743 = ini;
var map__11743__$1 = ((((!((map__11743 == null)))?((((map__11743.cljs$lang$protocol_mask$partition0$ & (64))) || (map__11743.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__11743):map__11743);
var comment = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11743__$1,cljs.core.cst$kw$comment);
var value = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11743__$1,cljs.core.cst$kw$value);
var map__11744 = template;
var map__11744__$1 = ((((!((map__11744 == null)))?((((map__11744.cljs$lang$protocol_mask$partition0$ & (64))) || (map__11744.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__11744):map__11744);
var type = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11744__$1,cljs.core.cst$kw$type);
var disabled = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11744__$1,cljs.core.cst$kw$disabled);
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div$render_DASH_ini_DASH_key,cljs.core.PersistentArrayMap.EMPTY,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$label,cljs.core.PersistentArrayMap.EMPTY,key], null),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,"text"))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$input,new cljs.core.PersistentArrayMap(null, 5, [cljs.core.cst$kw$class,"form-control",cljs.core.cst$kw$disabled,disabled,cljs.core.cst$kw$on_DASH_change,((function (map__11742,map__11742__$1,section,key,ini,template,map__11743,map__11743__$1,comment,value,map__11744,map__11744__$1,type,disabled){
return (function (p1__11733_SHARP_){
return ini_editor.core.set_ini_key_value_BANG_(section,key,p1__11733_SHARP_.target.value);
});})(map__11742,map__11742__$1,section,key,ini,template,map__11743,map__11743__$1,comment,value,map__11744,map__11744__$1,type,disabled))
,cljs.core.cst$kw$value,value,cljs.core.cst$kw$type,"text"], null)], null):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,"options"))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [ini_editor.widgets.dropdown_input,new cljs.core.PersistentArrayMap(null, 4, [cljs.core.cst$kw$disabled,disabled,cljs.core.cst$kw$options,cljs.core.cst$kw$options.cljs$core$IFn$_invoke$arity$1(template),cljs.core.cst$kw$on_DASH_change,((function (map__11742,map__11742__$1,section,key,ini,template,map__11743,map__11743__$1,comment,value,map__11744,map__11744__$1,type,disabled){
return (function (p1__11734_SHARP_){
return ini_editor.core.set_ini_key_value_BANG_(section,key,p1__11734_SHARP_);
});})(map__11742,map__11742__$1,section,key,ini,template,map__11743,map__11743__$1,comment,value,map__11744,map__11744__$1,type,disabled))
,cljs.core.cst$kw$value,value], null)], null):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,"multiline"))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [ini_editor.widgets.list_input,new cljs.core.PersistentArrayMap(null, 3, [cljs.core.cst$kw$disabled,disabled,cljs.core.cst$kw$on_DASH_change,((function (map__11742,map__11742__$1,section,key,ini,template,map__11743,map__11743__$1,comment,value,map__11744,map__11744__$1,type,disabled){
return (function (p1__11735_SHARP_){
return ini_editor.core.set_ini_key_value_BANG_(section,key,p1__11735_SHARP_);
});})(map__11742,map__11742__$1,section,key,ini,template,map__11743,map__11743__$1,comment,value,map__11744,map__11744__$1,type,disabled))
,cljs.core.cst$kw$value,value], null)], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$span,cljs.core.PersistentArrayMap.EMPTY,[cljs.core.str("unknown type: \""),cljs.core.str(type),cljs.core.str("\"\nvalue: "),cljs.core.str(value)].join('')], null)
)))], null);
});
ini_editor.core.render_ini_section = (function ini_editor$core$render_ini_section(props){
var map__11750 = props;
var map__11750__$1 = ((((!((map__11750 == null)))?((((map__11750.cljs$lang$protocol_mask$partition0$ & (64))) || (map__11750.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__11750):map__11750);
var section = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11750__$1,cljs.core.cst$kw$section);
var key_order = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11750__$1,cljs.core.cst$kw$key_DASH_order);
var ini = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11750__$1,cljs.core.cst$kw$ini);
var template = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11750__$1,cljs.core.cst$kw$template);
var expanded = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11750__$1,cljs.core.cst$kw$expanded);
var comment = cljs.core.cst$kw$comment.cljs$core$IFn$_invoke$arity$1(ini);
var key_ini = cljs.core.cst$kw$keys.cljs$core$IFn$_invoke$arity$1(ini);
var important_QMARK_ = cljs.core.cst$kw$important.cljs$core$IFn$_invoke$arity$1(template);
var key_template = cljs.core.cst$kw$keys.cljs$core$IFn$_invoke$arity$1(template);
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div$render_DASH_ini_DASH_section,cljs.core.PersistentArrayMap.EMPTY,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$h2,new cljs.core.PersistentArrayMap(null, 2, [cljs.core.cst$kw$title,comment,cljs.core.cst$kw$on_DASH_click,((function (map__11750,map__11750__$1,section,key_order,ini,template,expanded,comment,key_ini,important_QMARK_,key_template){
return (function (){
return ini_editor.core.toggle_expanded_BANG_(section);
});})(map__11750,map__11750__$1,section,key_order,ini,template,expanded,comment,key_ini,important_QMARK_,key_template))
], null),section], null),(cljs.core.truth_(expanded)?cljs.core.map.cljs$core$IFn$_invoke$arity$2(((function (map__11750,map__11750__$1,section,key_order,ini,template,expanded,comment,key_ini,important_QMARK_,key_template){
return (function (k){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [ini_editor.core.render_ini_key,new cljs.core.PersistentArrayMap(null, 4, [cljs.core.cst$kw$section,section,cljs.core.cst$kw$key,k,cljs.core.cst$kw$ini,cljs.core.get.cljs$core$IFn$_invoke$arity$2(key_ini,k),cljs.core.cst$kw$template,cljs.core.get.cljs$core$IFn$_invoke$arity$2(key_template,k)], null)], null);
});})(map__11750,map__11750__$1,section,key_order,ini,template,expanded,comment,key_ini,important_QMARK_,key_template))
,key_order):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$span,cljs.core.PersistentArrayMap.EMPTY,""], null))], null);
});
ini_editor.core.render_ini_edit = (function ini_editor$core$render_ini_edit(props){
var map__11756 = props;
var map__11756__$1 = ((((!((map__11756 == null)))?((((map__11756.cljs$lang$protocol_mask$partition0$ & (64))) || (map__11756.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__11756):map__11756);
var ini = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11756__$1,cljs.core.cst$kw$ini);
var template = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11756__$1,cljs.core.cst$kw$template);
var order = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11756__$1,cljs.core.cst$kw$order);
var expanded = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11756__$1,cljs.core.cst$kw$expanded);
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div$render_DASH_ini_DASH_edit,cljs.core.PersistentArrayMap.EMPTY,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$h1,cljs.core.PersistentArrayMap.EMPTY,"MachineKit INI Configuration"], null),cljs.core.map.cljs$core$IFn$_invoke$arity$2(((function (map__11756,map__11756__$1,ini,template,order,expanded){
return (function (p__11758){
var vec__11759 = p__11758;
var section = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11759,(0),null);
var keys = cljs.core.nthnext(vec__11759,(1));
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [ini_editor.core.render_ini_section,new cljs.core.PersistentArrayMap(null, 6, [cljs.core.cst$kw$key,section,cljs.core.cst$kw$section,section,cljs.core.cst$kw$key_DASH_order,keys,cljs.core.cst$kw$ini,cljs.core.get.cljs$core$IFn$_invoke$arity$2(ini,section),cljs.core.cst$kw$template,cljs.core.get.cljs$core$IFn$_invoke$arity$2(template,section),cljs.core.cst$kw$expanded,cljs.core.get.cljs$core$IFn$_invoke$arity$2(expanded,section)], null)], null);
});})(map__11756,map__11756__$1,ini,template,order,expanded))
,order)], null);
});
ini_editor.core.render_debug = (function ini_editor$core$render_debug(props){
return new cljs.core.PersistentVector(null, 10, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$span,cljs.core.PersistentArrayMap.EMPTY,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$h1,cljs.core.PersistentArrayMap.EMPTY,"INI To Write"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div,cljs.core.PersistentArrayMap.EMPTY,cljs.core.map_indexed.cljs$core$IFn$_invoke$arity$2((function (i,s){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div,new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$key,[cljs.core.str("ini-line-"),cljs.core.str(i)].join('')], null),s], null);
}),clojure.string.split_lines(ini_editor.parser.ini_to_str.cljs$core$IFn$_invoke$arity$2((cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.core.ini_atom) : cljs.core.deref.call(null,ini_editor.core.ini_atom)),(cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.core.order_atom) : cljs.core.deref.call(null,ini_editor.core.order_atom)))))], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$h1,cljs.core.PersistentArrayMap.EMPTY,"ini-atom"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div,cljs.core.PersistentArrayMap.EMPTY,[cljs.core.str((cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.core.ini_atom) : cljs.core.deref.call(null,ini_editor.core.ini_atom)))].join('')], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$h1,cljs.core.PersistentArrayMap.EMPTY,"template-atom"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div,cljs.core.PersistentArrayMap.EMPTY,[cljs.core.str((cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.core.template_atom) : cljs.core.deref.call(null,ini_editor.core.template_atom)))].join('')], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$h1,cljs.core.PersistentArrayMap.EMPTY,"order-atom"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div,cljs.core.PersistentArrayMap.EMPTY,[cljs.core.str((cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.core.order_atom) : cljs.core.deref.call(null,ini_editor.core.order_atom)))].join('')], null)], null);
});
ini_editor.core.view = (function ini_editor$core$view(props){
return new cljs.core.PersistentVector(null, 7, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div,cljs.core.PersistentArrayMap.EMPTY,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [ini_editor.core.render_file_io,cljs.core.PersistentArrayMap.EMPTY], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [ini_editor.core.render_ini_edit,new cljs.core.PersistentArrayMap(null, 4, [cljs.core.cst$kw$ini,(cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.core.ini_atom) : cljs.core.deref.call(null,ini_editor.core.ini_atom)),cljs.core.cst$kw$template,(cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.core.template_atom) : cljs.core.deref.call(null,ini_editor.core.template_atom)),cljs.core.cst$kw$order,(cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.core.order_atom) : cljs.core.deref.call(null,ini_editor.core.order_atom)),cljs.core.cst$kw$expanded,(cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.core.expanded_atom) : cljs.core.deref.call(null,ini_editor.core.expanded_atom))], null)], null),(cljs.core.truth_(ini_editor.core.debug_mode_QMARK_)?new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div,new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$on_DASH_click,(function (){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$2(ini_editor.core.show_debug_QMARK_,cljs.core.not);
})], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$h1,cljs.core.PersistentArrayMap.EMPTY,"Debug"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$span,cljs.core.PersistentArrayMap.EMPTY,"Click to toggle shown"], null)], null):null),(cljs.core.truth_((cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.core.show_debug_QMARK_) : cljs.core.deref.call(null,ini_editor.core.show_debug_QMARK_)))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [ini_editor.core.render_debug,cljs.core.PersistentArrayMap.EMPTY], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$span,cljs.core.PersistentArrayMap.EMPTY], null)),(cljs.core.truth_((function (){var and__4976__auto__ = ini_editor.core.debug_mode_QMARK_;
if(cljs.core.truth_(and__4976__auto__)){
return (cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.core.show_debug_QMARK_) : cljs.core.deref.call(null,ini_editor.core.show_debug_QMARK_));
} else {
return and__4976__auto__;
}
})())?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [ini_editor.core.render_debug,cljs.core.PersistentArrayMap.EMPTY], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$span,cljs.core.PersistentArrayMap.EMPTY], null))], null);
});
ini_editor.core.start = (function ini_editor$core$start(){
var G__11762 = new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [ini_editor.core.view,cljs.core.PersistentArrayMap.EMPTY], null);
var G__11763 = document.getElementById("app");
return (reagent.core.render_component.cljs$core$IFn$_invoke$arity$2 ? reagent.core.render_component.cljs$core$IFn$_invoke$arity$2(G__11762,G__11763) : reagent.core.render_component.call(null,G__11762,G__11763));
});
goog.exportSymbol('ini_editor.core.start', ini_editor.core.start);
