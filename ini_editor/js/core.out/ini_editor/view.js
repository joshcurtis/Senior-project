// Compiled by ClojureScript 1.7.170 {:static-fns true, :optimize-constants true}
goog.provide('ini_editor.view');
goog.require('cljs.core');
goog.require('ini_editor.widgets');
goog.require('ini_editor.model');
goog.require('reagent.core');
goog.require('ini_editor.controller');
goog.require('ini_editor.parser');
goog.require('clojure.string');
ini_editor.view.render_file_io = (function ini_editor$view$render_file_io(props){
return new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div$render_DASH_file_DASH_io,cljs.core.PersistentArrayMap.EMPTY,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [ini_editor.widgets.file_input,new cljs.core.PersistentArrayMap(null, 3, [cljs.core.cst$kw$id,"upload-file",cljs.core.cst$kw$file_DASH_types,".ini",cljs.core.cst$kw$on_DASH_change,(function (p1__11734_SHARP_){
return ini_editor.controller.upload_file_BANG_(cljs.core.first(p1__11734_SHARP_));
})], null)], null)], null);
});
ini_editor.view.render_ini_key = (function ini_editor$view$render_ini_key(props){
var map__11744 = props;
var map__11744__$1 = ((((!((map__11744 == null)))?((((map__11744.cljs$lang$protocol_mask$partition0$ & (64))) || (map__11744.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__11744):map__11744);
var section = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11744__$1,cljs.core.cst$kw$section);
var key = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11744__$1,cljs.core.cst$kw$key);
var ini = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11744__$1,cljs.core.cst$kw$ini);
var template = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11744__$1,cljs.core.cst$kw$template);
var map__11745 = ini;
var map__11745__$1 = ((((!((map__11745 == null)))?((((map__11745.cljs$lang$protocol_mask$partition0$ & (64))) || (map__11745.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__11745):map__11745);
var comment = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11745__$1,cljs.core.cst$kw$comment);
var value = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11745__$1,cljs.core.cst$kw$value);
var map__11746 = template;
var map__11746__$1 = ((((!((map__11746 == null)))?((((map__11746.cljs$lang$protocol_mask$partition0$ & (64))) || (map__11746.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__11746):map__11746);
var type = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11746__$1,cljs.core.cst$kw$type);
var disabled = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11746__$1,cljs.core.cst$kw$disabled);
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div$render_DASH_ini_DASH_key,new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$class,"form-goup"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$label,new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$class,"control-label"], null),key], null),((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,"text"))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$input,new cljs.core.PersistentArrayMap(null, 5, [cljs.core.cst$kw$class,"form-control",cljs.core.cst$kw$disabled,disabled,cljs.core.cst$kw$on_DASH_change,((function (map__11744,map__11744__$1,section,key,ini,template,map__11745,map__11745__$1,comment,value,map__11746,map__11746__$1,type,disabled){
return (function (p1__11735_SHARP_){
return ini_editor.controller.set_ini_key_value_BANG_(section,key,p1__11735_SHARP_.target.value);
});})(map__11744,map__11744__$1,section,key,ini,template,map__11745,map__11745__$1,comment,value,map__11746,map__11746__$1,type,disabled))
,cljs.core.cst$kw$value,value,cljs.core.cst$kw$type,"text"], null)], null):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,"options"))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [ini_editor.widgets.dropdown_input,new cljs.core.PersistentArrayMap(null, 4, [cljs.core.cst$kw$disabled,disabled,cljs.core.cst$kw$options,cljs.core.cst$kw$options.cljs$core$IFn$_invoke$arity$1(template),cljs.core.cst$kw$on_DASH_change,((function (map__11744,map__11744__$1,section,key,ini,template,map__11745,map__11745__$1,comment,value,map__11746,map__11746__$1,type,disabled){
return (function (p1__11736_SHARP_){
return ini_editor.controller.set_ini_key_value_BANG_(section,key,p1__11736_SHARP_);
});})(map__11744,map__11744__$1,section,key,ini,template,map__11745,map__11745__$1,comment,value,map__11746,map__11746__$1,type,disabled))
,cljs.core.cst$kw$value,value], null)], null):((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,"multiline"))?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [ini_editor.widgets.list_input,new cljs.core.PersistentArrayMap(null, 3, [cljs.core.cst$kw$disabled,disabled,cljs.core.cst$kw$on_DASH_change,((function (map__11744,map__11744__$1,section,key,ini,template,map__11745,map__11745__$1,comment,value,map__11746,map__11746__$1,type,disabled){
return (function (p1__11737_SHARP_){
return ini_editor.controller.set_ini_key_value_BANG_(section,key,p1__11737_SHARP_);
});})(map__11744,map__11744__$1,section,key,ini,template,map__11745,map__11745__$1,comment,value,map__11746,map__11746__$1,type,disabled))
,cljs.core.cst$kw$value,value], null)], null):new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$span,cljs.core.PersistentArrayMap.EMPTY,[cljs.core.str("unknown type: \""),cljs.core.str(type),cljs.core.str("\"\nvalue: "),cljs.core.str(value)].join('')], null)
)))], null);
});
ini_editor.view.render_ini_section = (function ini_editor$view$render_ini_section(props){
var map__11752 = props;
var map__11752__$1 = ((((!((map__11752 == null)))?((((map__11752.cljs$lang$protocol_mask$partition0$ & (64))) || (map__11752.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__11752):map__11752);
var section = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11752__$1,cljs.core.cst$kw$section);
var key_order = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11752__$1,cljs.core.cst$kw$key_DASH_order);
var ini = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11752__$1,cljs.core.cst$kw$ini);
var template = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11752__$1,cljs.core.cst$kw$template);
var expanded = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11752__$1,cljs.core.cst$kw$expanded);
var comment = cljs.core.cst$kw$comment.cljs$core$IFn$_invoke$arity$1(ini);
var key_ini = cljs.core.cst$kw$keys.cljs$core$IFn$_invoke$arity$1(ini);
var important_QMARK_ = cljs.core.cst$kw$important.cljs$core$IFn$_invoke$arity$1(template);
var key_template = cljs.core.cst$kw$keys.cljs$core$IFn$_invoke$arity$1(template);
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div,new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$class,"panel panel-default"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div,new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$class,"panel-heading"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$legend,new cljs.core.PersistentArrayMap(null, 4, [cljs.core.cst$kw$class,"panel-title",cljs.core.cst$kw$style,new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$font_DASH_weight,"bold"], null),cljs.core.cst$kw$title,comment,cljs.core.cst$kw$on_DASH_click,((function (map__11752,map__11752__$1,section,key_order,ini,template,expanded,comment,key_ini,important_QMARK_,key_template){
return (function (){
return ini_editor.controller.toggle_expanded_BANG_(section);
});})(map__11752,map__11752__$1,section,key_order,ini,template,expanded,comment,key_ini,important_QMARK_,key_template))
], null),section], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$fieldset$render_DASH_ini_DASH_section,cljs.core.PersistentArrayMap.EMPTY,(cljs.core.truth_(expanded)?new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div,new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$class,"panel-body form-group"], null),cljs.core.map.cljs$core$IFn$_invoke$arity$2(((function (map__11752,map__11752__$1,section,key_order,ini,template,expanded,comment,key_ini,important_QMARK_,key_template){
return (function (k){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [ini_editor.view.render_ini_key,new cljs.core.PersistentArrayMap(null, 4, [cljs.core.cst$kw$section,section,cljs.core.cst$kw$key,k,cljs.core.cst$kw$ini,cljs.core.get.cljs$core$IFn$_invoke$arity$2(key_ini,k),cljs.core.cst$kw$template,cljs.core.get.cljs$core$IFn$_invoke$arity$2(key_template,k)], null)], null);
});})(map__11752,map__11752__$1,section,key_order,ini,template,expanded,comment,key_ini,important_QMARK_,key_template))
,key_order)], null):null)], null)], null);
});
ini_editor.view.render_ini_edit = (function ini_editor$view$render_ini_edit(props){
var map__11758 = props;
var map__11758__$1 = ((((!((map__11758 == null)))?((((map__11758.cljs$lang$protocol_mask$partition0$ & (64))) || (map__11758.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__11758):map__11758);
var ini = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11758__$1,cljs.core.cst$kw$ini);
var template = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11758__$1,cljs.core.cst$kw$template);
var order = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11758__$1,cljs.core.cst$kw$order);
var expanded = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11758__$1,cljs.core.cst$kw$expanded);
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div$render_DASH_ini_DASH_edit,new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$style,new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$margin,"1rem"], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$h1,cljs.core.PersistentArrayMap.EMPTY,"MachineKit INI Configuration"], null),cljs.core.map.cljs$core$IFn$_invoke$arity$2(((function (map__11758,map__11758__$1,ini,template,order,expanded){
return (function (p__11760){
var vec__11761 = p__11760;
var section = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11761,(0),null);
var keys = cljs.core.nthnext(vec__11761,(1));
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [ini_editor.view.render_ini_section,new cljs.core.PersistentArrayMap(null, 6, [cljs.core.cst$kw$key,section,cljs.core.cst$kw$section,section,cljs.core.cst$kw$key_DASH_order,keys,cljs.core.cst$kw$ini,cljs.core.get.cljs$core$IFn$_invoke$arity$2(ini,section),cljs.core.cst$kw$template,cljs.core.get.cljs$core$IFn$_invoke$arity$2(template,section),cljs.core.cst$kw$expanded,cljs.core.get.cljs$core$IFn$_invoke$arity$2(expanded,section)], null)], null);
});})(map__11758,map__11758__$1,ini,template,order,expanded))
,order)], null);
});
ini_editor.view.render_debug = (function ini_editor$view$render_debug(props){
var ini = (cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.model.ini) : cljs.core.deref.call(null,ini_editor.model.ini));
var template = (cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.model.template) : cljs.core.deref.call(null,ini_editor.model.template));
var order = (cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.model.order) : cljs.core.deref.call(null,ini_editor.model.order));
var expanded = (cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.model.expanded) : cljs.core.deref.call(null,ini_editor.model.expanded));
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div,new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$class,"well"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div,cljs.core.PersistentArrayMap.EMPTY,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$h1,cljs.core.PersistentArrayMap.EMPTY,"Debug"], null)], null),cljs.core.map.cljs$core$IFn$_invoke$arity$2(((function (ini,template,order,expanded){
return (function (p__11764){
var vec__11765 = p__11764;
var title = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11765,(0),null);
var content = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11765,(1),null);
return new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div,new cljs.core.PersistentArrayMap(null, 2, [cljs.core.cst$kw$key,title,cljs.core.cst$kw$class,"panel panel-info"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div,new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$class,"panel-heading"], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$h3,new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$class,"panel-title"], null),title], null)], null),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div,new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$class,"panel-body"], null),content], null)], null);
});})(ini,template,order,expanded))
,new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Debug - INI To Write",new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$span,new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$style,new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$white_DASH_space,"pre"], null)], null),ini_editor.parser.ini_to_str.cljs$core$IFn$_invoke$arity$2(ini,order)], null)], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Debug - model/ini",[cljs.core.str(ini)].join('')], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Debug - model/template",[cljs.core.str(template)].join('')], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Debug - model/order",[cljs.core.str(order)].join('')], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, ["Debug - model/expanded",[cljs.core.str(expanded)].join('')], null)], null))], null);
});
ini_editor.view.view = (function ini_editor$view$view(props){
return new cljs.core.PersistentVector(null, 5, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$div,cljs.core.PersistentArrayMap.EMPTY,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [ini_editor.view.render_file_io,cljs.core.PersistentArrayMap.EMPTY], null),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [ini_editor.view.render_ini_edit,new cljs.core.PersistentArrayMap(null, 4, [cljs.core.cst$kw$ini,(cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.model.ini) : cljs.core.deref.call(null,ini_editor.model.ini)),cljs.core.cst$kw$template,(cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.model.template) : cljs.core.deref.call(null,ini_editor.model.template)),cljs.core.cst$kw$order,(cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.model.order) : cljs.core.deref.call(null,ini_editor.model.order)),cljs.core.cst$kw$expanded,(cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_editor.model.expanded) : cljs.core.deref.call(null,ini_editor.model.expanded))], null)], null),(cljs.core.truth_(ini_editor.model.show_debug_QMARK_)?new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [ini_editor.view.render_debug,cljs.core.PersistentArrayMap.EMPTY], null):new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$span,cljs.core.PersistentArrayMap.EMPTY], null))], null);
});
