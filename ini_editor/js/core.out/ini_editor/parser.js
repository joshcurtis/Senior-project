// Compiled by ClojureScript 1.7.170 {:static-fns true, :optimize-constants true}
goog.provide('ini_editor.parser');
goog.require('cljs.core');
goog.require('clojure.string');
goog.require('cljs.reader');
goog.require('ini_editor.utils');
cljs.core.enable_console_print_BANG_();
/**
 * Returns [category result], where category is a key that represents the line
 *   this can be any of (:metadata :comment :section :key-value :blank nil). The
 *   category is nil if it was not in a valid format. Result is the parsed value.
 *   It is a string in the case of (:comment :section :blank). It is a clojure
 *   object in the case of :metadata and [key-string val-string] in the case of
 *   :key-value.
 */
ini_editor.parser.categorize_ini_line = (function ini_editor$parser$categorize_ini_line(s){
var s__$1 = clojure.string.trim(s);
if(cljs.core.truth_(s__$1.startsWith("##!"))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$metadata,cljs.reader.read_string(clojure.string.trim(cljs.core.subs.cljs$core$IFn$_invoke$arity$2(s__$1,(3))))], null);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2("#",cljs.core.first(s__$1))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$comment,clojure.string.trim(cljs.core.subs.cljs$core$IFn$_invoke$arity$2(s__$1,(1)))], null);
} else {
if((cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2("[",cljs.core.first(s__$1))) && (cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2("]",cljs.core.last(s__$1)))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$section,clojure.string.trim(cljs.core.subs.cljs$core$IFn$_invoke$arity$3(s__$1,(1),(cljs.core.count(s__$1) - (1))))], null);
} else {
if(cljs.core.truth_(s__$1.includes("="))){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$key_DASH_value,(function (){var vec__11453 = clojure.string.split.cljs$core$IFn$_invoke$arity$3(s__$1,/=/,(2));
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11453,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11453,(1),null);
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [clojure.string.trim(k),clojure.string.trim(v)], null);
})()], null);
} else {
if(clojure.string.blank_QMARK_(s__$1)){
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$blank,""], null);
} else {
return console.warn("Unparsed INI line:",s__$1);

}
}
}
}
}
});
/**
 * Notes - Doesn't handle duplicate sections.
 *       - If there is a duplicate key, only the first occurence counts.
 *   
 */
ini_editor.parser.order_line_BANG_ = (function ini_editor$parser$order_line_BANG_(s,order_atom,section_atom,found_atom){
var vec__11457 = ini_editor.parser.categorize_ini_line(s);
var type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11457,(0),null);
var result = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11457,(1),null);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,cljs.core.cst$kw$section)){
if(!(cljs.core.contains_QMARK_((cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(found_atom) : cljs.core.deref.call(null,found_atom)),result))){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(order_atom,cljs.core.conj,cljs.core._conj(cljs.core.List.EMPTY,result));

(cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(section_atom,result) : cljs.core.reset_BANG_.call(null,section_atom,result));

return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(found_atom,cljs.core.assoc,result,cljs.core.PersistentArrayMap.EMPTY);
} else {
return null;
}
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,cljs.core.cst$kw$key_DASH_value)){
var vec__11458 = result;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11458,(0),null);
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11458,(1),null);
if(!(cljs.core.contains_QMARK_(cljs.core.get.cljs$core$IFn$_invoke$arity$2((cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(found_atom) : cljs.core.deref.call(null,found_atom)),(cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(section_atom) : cljs.core.deref.call(null,section_atom))),k))){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(order_atom,ini_editor.utils.update_list_head,((function (vec__11458,k,_,vec__11457,type,result){
return (function (p1__11454_SHARP_){
return cljs.core.conj.cljs$core$IFn$_invoke$arity$2(p1__11454_SHARP_,k);
});})(vec__11458,k,_,vec__11457,type,result))
);

return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(found_atom,cljs.core.assoc_in,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [(cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(section_atom) : cljs.core.deref.call(null,section_atom)),k], null),true);
} else {
return null;
}
} else {
return null;
}
}
});
/**
 * Returns the order of sections and keys in an INI string. It is returned as a
 *   list of sections. A section is a list, in which the first element is the name
 *   of the section, and the rest are the names of the keys.
 */
ini_editor.parser.parse_ini_order = (function ini_editor$parser$parse_ini_order(s){
if(typeof s === 'string'){
} else {
throw (new Error([cljs.core.str("Assert failed: "),cljs.core.str("s is not a string."),cljs.core.str("\n"),cljs.core.str(cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.array_seq([cljs.core.list(cljs.core.cst$sym$string_QMARK_,cljs.core.cst$sym$s)], 0)))].join('')));
}

var lines = clojure.string.split_lines(s);
var order_atom = (function (){var G__11466 = cljs.core.List.EMPTY;
return (cljs.core.atom.cljs$core$IFn$_invoke$arity$1 ? cljs.core.atom.cljs$core$IFn$_invoke$arity$1(G__11466) : cljs.core.atom.call(null,G__11466));
})();
var section_atom = (cljs.core.atom.cljs$core$IFn$_invoke$arity$1 ? cljs.core.atom.cljs$core$IFn$_invoke$arity$1("") : cljs.core.atom.call(null,""));
var found_atom = (function (){var G__11467 = cljs.core.PersistentArrayMap.EMPTY;
return (cljs.core.atom.cljs$core$IFn$_invoke$arity$1 ? cljs.core.atom.cljs$core$IFn$_invoke$arity$1(G__11467) : cljs.core.atom.call(null,G__11467));
})();
var seq__11468_11472 = cljs.core.seq(lines);
var chunk__11469_11473 = null;
var count__11470_11474 = (0);
var i__11471_11475 = (0);
while(true){
if((i__11471_11475 < count__11470_11474)){
var line_11476 = chunk__11469_11473.cljs$core$IIndexed$_nth$arity$2(null,i__11471_11475);
ini_editor.parser.order_line_BANG_(line_11476,order_atom,section_atom,found_atom);

var G__11477 = seq__11468_11472;
var G__11478 = chunk__11469_11473;
var G__11479 = count__11470_11474;
var G__11480 = (i__11471_11475 + (1));
seq__11468_11472 = G__11477;
chunk__11469_11473 = G__11478;
count__11470_11474 = G__11479;
i__11471_11475 = G__11480;
continue;
} else {
var temp__4425__auto___11481 = cljs.core.seq(seq__11468_11472);
if(temp__4425__auto___11481){
var seq__11468_11482__$1 = temp__4425__auto___11481;
if(cljs.core.chunked_seq_QMARK_(seq__11468_11482__$1)){
var c__5791__auto___11483 = cljs.core.chunk_first(seq__11468_11482__$1);
var G__11484 = cljs.core.chunk_rest(seq__11468_11482__$1);
var G__11485 = c__5791__auto___11483;
var G__11486 = cljs.core.count(c__5791__auto___11483);
var G__11487 = (0);
seq__11468_11472 = G__11484;
chunk__11469_11473 = G__11485;
count__11470_11474 = G__11486;
i__11471_11475 = G__11487;
continue;
} else {
var line_11488 = cljs.core.first(seq__11468_11482__$1);
ini_editor.parser.order_line_BANG_(line_11488,order_atom,section_atom,found_atom);

var G__11489 = cljs.core.next(seq__11468_11482__$1);
var G__11490 = null;
var G__11491 = (0);
var G__11492 = (0);
seq__11468_11472 = G__11489;
chunk__11469_11473 = G__11490;
count__11470_11474 = G__11491;
i__11471_11475 = G__11492;
continue;
}
} else {
}
}
break;
}

return cljs.core.reverse(((function (lines,order_atom,section_atom,found_atom){
return (function (p1__11459_SHARP_){
return cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.reverse,p1__11459_SHARP_);
});})(lines,order_atom,section_atom,found_atom))
.call(null,(cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(order_atom) : cljs.core.deref.call(null,order_atom))));
});
ini_editor.parser.template_line_BANG_ = (function ini_editor$parser$template_line_BANG_(s,template_atom,section_atom,metadata_atom){
var vec__11499 = ini_editor.parser.categorize_ini_line(s);
var type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11499,(0),null);
var result = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11499,(1),null);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,cljs.core.cst$kw$metadata)){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(metadata_atom,cljs.core.merge,result);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,cljs.core.cst$kw$section)){
var metadata = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.array_seq([new cljs.core.PersistentArrayMap(null, 2, [cljs.core.cst$kw$keys,cljs.core.PersistentArrayMap.EMPTY,cljs.core.cst$kw$important,true], null),(cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(metadata_atom) : cljs.core.deref.call(null,metadata_atom))], 0));
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(template_atom,cljs.core.assoc,result,metadata);

(cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(section_atom,result) : cljs.core.reset_BANG_.call(null,section_atom,result));

var G__11500 = metadata_atom;
var G__11501 = cljs.core.PersistentArrayMap.EMPTY;
return (cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(G__11500,G__11501) : cljs.core.reset_BANG_.call(null,G__11500,G__11501));
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,cljs.core.cst$kw$key_DASH_value)){
var vec__11502 = result;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11502,(0),null);
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11502,(1),null);
var section = (cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(section_atom) : cljs.core.deref.call(null,section_atom));
var metadata = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.array_seq([new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$type,"text"], null),(cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(metadata_atom) : cljs.core.deref.call(null,metadata_atom))], 0));
var exists_QMARK_ = cljs.core.some_QMARK_(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2((cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(template_atom) : cljs.core.deref.call(null,template_atom)),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [section,cljs.core.cst$kw$keys,k], null)));
if(!(exists_QMARK_)){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(template_atom,cljs.core.assoc_in,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [section,cljs.core.cst$kw$keys,k], null),metadata);
} else {
}

var G__11503 = metadata_atom;
var G__11504 = cljs.core.PersistentArrayMap.EMPTY;
return (cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(G__11503,G__11504) : cljs.core.reset_BANG_.call(null,G__11503,G__11504));
} else {
return null;
}
}
}
});
/**
 * Generates an ini template from an INI string
 */
ini_editor.parser.parse_ini_template = (function ini_editor$parser$parse_ini_template(s){
var lines = clojure.string.split_lines(s);
var template_atom = (function (){var G__11511 = cljs.core.PersistentArrayMap.EMPTY;
return (cljs.core.atom.cljs$core$IFn$_invoke$arity$1 ? cljs.core.atom.cljs$core$IFn$_invoke$arity$1(G__11511) : cljs.core.atom.call(null,G__11511));
})();
var section_atom = (cljs.core.atom.cljs$core$IFn$_invoke$arity$1 ? cljs.core.atom.cljs$core$IFn$_invoke$arity$1("") : cljs.core.atom.call(null,""));
var metadata_atom = (function (){var G__11512 = cljs.core.PersistentArrayMap.EMPTY;
return (cljs.core.atom.cljs$core$IFn$_invoke$arity$1 ? cljs.core.atom.cljs$core$IFn$_invoke$arity$1(G__11512) : cljs.core.atom.call(null,G__11512));
})();
var seq__11513_11517 = cljs.core.seq(lines);
var chunk__11514_11518 = null;
var count__11515_11519 = (0);
var i__11516_11520 = (0);
while(true){
if((i__11516_11520 < count__11515_11519)){
var line_11521 = chunk__11514_11518.cljs$core$IIndexed$_nth$arity$2(null,i__11516_11520);
ini_editor.parser.template_line_BANG_(line_11521,template_atom,section_atom,metadata_atom);

var G__11522 = seq__11513_11517;
var G__11523 = chunk__11514_11518;
var G__11524 = count__11515_11519;
var G__11525 = (i__11516_11520 + (1));
seq__11513_11517 = G__11522;
chunk__11514_11518 = G__11523;
count__11515_11519 = G__11524;
i__11516_11520 = G__11525;
continue;
} else {
var temp__4425__auto___11526 = cljs.core.seq(seq__11513_11517);
if(temp__4425__auto___11526){
var seq__11513_11527__$1 = temp__4425__auto___11526;
if(cljs.core.chunked_seq_QMARK_(seq__11513_11527__$1)){
var c__5791__auto___11528 = cljs.core.chunk_first(seq__11513_11527__$1);
var G__11529 = cljs.core.chunk_rest(seq__11513_11527__$1);
var G__11530 = c__5791__auto___11528;
var G__11531 = cljs.core.count(c__5791__auto___11528);
var G__11532 = (0);
seq__11513_11517 = G__11529;
chunk__11514_11518 = G__11530;
count__11515_11519 = G__11531;
i__11516_11520 = G__11532;
continue;
} else {
var line_11533 = cljs.core.first(seq__11513_11527__$1);
ini_editor.parser.template_line_BANG_(line_11533,template_atom,section_atom,metadata_atom);

var G__11534 = cljs.core.next(seq__11513_11527__$1);
var G__11535 = null;
var G__11536 = (0);
var G__11537 = (0);
seq__11513_11517 = G__11534;
chunk__11514_11518 = G__11535;
count__11515_11519 = G__11536;
i__11516_11520 = G__11537;
continue;
}
} else {
}
}
break;
}

return (cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(template_atom) : cljs.core.deref.call(null,template_atom));
});
ini_editor.parser.ini_arbitrary_order = (function ini_editor$parser$ini_arbitrary_order(ini){
return cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p__11540){
var vec__11541 = p__11540;
var sec = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11541,(0),null);
var data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11541,(1),null);
return cljs.core.conj.cljs$core$IFn$_invoke$arity$2(cljs.core.keys(cljs.core.cst$kw$keys.cljs$core$IFn$_invoke$arity$1(data)),sec);
}),ini);
});
/**
 * Formats the ini string value to conform to the template. For most cases, the
 *   value is the same. Here are some common cases.
 *   multiline - vector of strings, one for each line
 */
ini_editor.parser.ini_template_fix_value = (function ini_editor$parser$ini_template_fix_value(old_value,template){
if(typeof old_value === 'string'){
} else {
throw (new Error([cljs.core.str("Assert failed: "),cljs.core.str("old-value is not a string"),cljs.core.str("\n"),cljs.core.str(cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.array_seq([cljs.core.list(cljs.core.cst$sym$string_QMARK_,cljs.core.cst$sym$old_DASH_value)], 0)))].join('')));
}

var map__11544 = template;
var map__11544__$1 = ((((!((map__11544 == null)))?((((map__11544.cljs$lang$protocol_mask$partition0$ & (64))) || (map__11544.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__11544):map__11544);
var type = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11544__$1,cljs.core.cst$kw$type);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,"multiline")){
return clojure.string.split_lines(old_value);
} else {
return old_value;

}
});
/**
 * Formats the ini hashmap to conform to the template. For most cases, things
 *   remain the same. For changes to values, see `ini-template-fix-value`
 */
ini_editor.parser.ini_apply_template = (function ini_editor$parser$ini_apply_template(ini,template){
var ini_atom = (cljs.core.atom.cljs$core$IFn$_invoke$arity$1 ? cljs.core.atom.cljs$core$IFn$_invoke$arity$1(ini) : cljs.core.atom.call(null,ini));
var secs = ini_editor.parser.ini_arbitrary_order(ini);
var seq__11560_11574 = cljs.core.seq(secs);
var chunk__11565_11575 = null;
var count__11566_11576 = (0);
var i__11567_11577 = (0);
while(true){
if((i__11567_11577 < count__11566_11576)){
var vec__11572_11578 = chunk__11565_11575.cljs$core$IIndexed$_nth$arity$2(null,i__11567_11577);
var sec_11579 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11572_11578,(0),null);
var keys_11580 = cljs.core.nthnext(vec__11572_11578,(1));
var seq__11568_11581 = cljs.core.seq(keys_11580);
var chunk__11569_11582 = null;
var count__11570_11583 = (0);
var i__11571_11584 = (0);
while(true){
if((i__11571_11584 < count__11570_11583)){
var key_11585 = chunk__11569_11582.cljs$core$IIndexed$_nth$arity$2(null,i__11571_11584);
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(ini_atom,cljs.core.update_in,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [sec_11579,cljs.core.cst$kw$keys,key_11585,cljs.core.cst$kw$value], null),ini_editor.parser.ini_template_fix_value,cljs.core.array_seq([cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(template,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sec_11579,cljs.core.cst$kw$keys,key_11585], null))], 0));

var G__11586 = seq__11568_11581;
var G__11587 = chunk__11569_11582;
var G__11588 = count__11570_11583;
var G__11589 = (i__11571_11584 + (1));
seq__11568_11581 = G__11586;
chunk__11569_11582 = G__11587;
count__11570_11583 = G__11588;
i__11571_11584 = G__11589;
continue;
} else {
var temp__4425__auto___11590 = cljs.core.seq(seq__11568_11581);
if(temp__4425__auto___11590){
var seq__11568_11591__$1 = temp__4425__auto___11590;
if(cljs.core.chunked_seq_QMARK_(seq__11568_11591__$1)){
var c__5791__auto___11592 = cljs.core.chunk_first(seq__11568_11591__$1);
var G__11593 = cljs.core.chunk_rest(seq__11568_11591__$1);
var G__11594 = c__5791__auto___11592;
var G__11595 = cljs.core.count(c__5791__auto___11592);
var G__11596 = (0);
seq__11568_11581 = G__11593;
chunk__11569_11582 = G__11594;
count__11570_11583 = G__11595;
i__11571_11584 = G__11596;
continue;
} else {
var key_11597 = cljs.core.first(seq__11568_11591__$1);
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(ini_atom,cljs.core.update_in,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [sec_11579,cljs.core.cst$kw$keys,key_11597,cljs.core.cst$kw$value], null),ini_editor.parser.ini_template_fix_value,cljs.core.array_seq([cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(template,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sec_11579,cljs.core.cst$kw$keys,key_11597], null))], 0));

var G__11598 = cljs.core.next(seq__11568_11591__$1);
var G__11599 = null;
var G__11600 = (0);
var G__11601 = (0);
seq__11568_11581 = G__11598;
chunk__11569_11582 = G__11599;
count__11570_11583 = G__11600;
i__11571_11584 = G__11601;
continue;
}
} else {
}
}
break;
}

var G__11602 = seq__11560_11574;
var G__11603 = chunk__11565_11575;
var G__11604 = count__11566_11576;
var G__11605 = (i__11567_11577 + (1));
seq__11560_11574 = G__11602;
chunk__11565_11575 = G__11603;
count__11566_11576 = G__11604;
i__11567_11577 = G__11605;
continue;
} else {
var temp__4425__auto___11606 = cljs.core.seq(seq__11560_11574);
if(temp__4425__auto___11606){
var seq__11560_11607__$1 = temp__4425__auto___11606;
if(cljs.core.chunked_seq_QMARK_(seq__11560_11607__$1)){
var c__5791__auto___11608 = cljs.core.chunk_first(seq__11560_11607__$1);
var G__11609 = cljs.core.chunk_rest(seq__11560_11607__$1);
var G__11610 = c__5791__auto___11608;
var G__11611 = cljs.core.count(c__5791__auto___11608);
var G__11612 = (0);
seq__11560_11574 = G__11609;
chunk__11565_11575 = G__11610;
count__11566_11576 = G__11611;
i__11567_11577 = G__11612;
continue;
} else {
var vec__11573_11613 = cljs.core.first(seq__11560_11607__$1);
var sec_11614 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11573_11613,(0),null);
var keys_11615 = cljs.core.nthnext(vec__11573_11613,(1));
var seq__11561_11616 = cljs.core.seq(keys_11615);
var chunk__11562_11617 = null;
var count__11563_11618 = (0);
var i__11564_11619 = (0);
while(true){
if((i__11564_11619 < count__11563_11618)){
var key_11620 = chunk__11562_11617.cljs$core$IIndexed$_nth$arity$2(null,i__11564_11619);
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(ini_atom,cljs.core.update_in,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [sec_11614,cljs.core.cst$kw$keys,key_11620,cljs.core.cst$kw$value], null),ini_editor.parser.ini_template_fix_value,cljs.core.array_seq([cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(template,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sec_11614,cljs.core.cst$kw$keys,key_11620], null))], 0));

var G__11621 = seq__11561_11616;
var G__11622 = chunk__11562_11617;
var G__11623 = count__11563_11618;
var G__11624 = (i__11564_11619 + (1));
seq__11561_11616 = G__11621;
chunk__11562_11617 = G__11622;
count__11563_11618 = G__11623;
i__11564_11619 = G__11624;
continue;
} else {
var temp__4425__auto___11625__$1 = cljs.core.seq(seq__11561_11616);
if(temp__4425__auto___11625__$1){
var seq__11561_11626__$1 = temp__4425__auto___11625__$1;
if(cljs.core.chunked_seq_QMARK_(seq__11561_11626__$1)){
var c__5791__auto___11627 = cljs.core.chunk_first(seq__11561_11626__$1);
var G__11628 = cljs.core.chunk_rest(seq__11561_11626__$1);
var G__11629 = c__5791__auto___11627;
var G__11630 = cljs.core.count(c__5791__auto___11627);
var G__11631 = (0);
seq__11561_11616 = G__11628;
chunk__11562_11617 = G__11629;
count__11563_11618 = G__11630;
i__11564_11619 = G__11631;
continue;
} else {
var key_11632 = cljs.core.first(seq__11561_11626__$1);
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(ini_atom,cljs.core.update_in,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [sec_11614,cljs.core.cst$kw$keys,key_11632,cljs.core.cst$kw$value], null),ini_editor.parser.ini_template_fix_value,cljs.core.array_seq([cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(template,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sec_11614,cljs.core.cst$kw$keys,key_11632], null))], 0));

var G__11633 = cljs.core.next(seq__11561_11626__$1);
var G__11634 = null;
var G__11635 = (0);
var G__11636 = (0);
seq__11561_11616 = G__11633;
chunk__11562_11617 = G__11634;
count__11563_11618 = G__11635;
i__11564_11619 = G__11636;
continue;
}
} else {
}
}
break;
}

var G__11637 = cljs.core.next(seq__11560_11607__$1);
var G__11638 = null;
var G__11639 = (0);
var G__11640 = (0);
seq__11560_11574 = G__11637;
chunk__11565_11575 = G__11638;
count__11566_11576 = G__11639;
i__11567_11577 = G__11640;
continue;
}
} else {
}
}
break;
}

return (cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_atom) : cljs.core.deref.call(null,ini_atom));
});
/**
 * Notes - Multiple sections are not yet properly handled.
 *       - If there are duplicate keys, then the values are joined with a
 *         newline. Only the first comment is used.
 *   
 */
ini_editor.parser.ini_line_BANG_ = (function ini_editor$parser$ini_line_BANG_(s,ini_atom,section_atom,comment_atom){
var vec__11643 = ini_editor.parser.categorize_ini_line(s);
var type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11643,(0),null);
var result = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11643,(1),null);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,cljs.core.cst$kw$comment)){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(comment_atom,cljs.core.str,"\n",result);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,cljs.core.cst$kw$metadata)){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(comment_atom,cljs.core.str,"\n#! ",[cljs.core.str(result)].join(''));
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,cljs.core.cst$kw$section)){
var comments = clojure.string.trim((cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(comment_atom) : cljs.core.deref.call(null,comment_atom)));
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(ini_atom,cljs.core.assoc,result,new cljs.core.PersistentArrayMap(null, 2, [cljs.core.cst$kw$comment,comments,cljs.core.cst$kw$keys,cljs.core.PersistentArrayMap.EMPTY], null));

(cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(section_atom,result) : cljs.core.reset_BANG_.call(null,section_atom,result));

return (cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(comment_atom,"") : cljs.core.reset_BANG_.call(null,comment_atom,""));
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,cljs.core.cst$kw$key_DASH_value)){
var vec__11644 = result;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11644,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11644,(1),null);
var section = (cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(section_atom) : cljs.core.deref.call(null,section_atom));
var comments = clojure.string.trim((cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(comment_atom) : cljs.core.deref.call(null,comment_atom)));
var exists_QMARK_ = cljs.core.contains_QMARK_(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2((cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_atom) : cljs.core.deref.call(null,ini_atom)),new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [section,cljs.core.cst$kw$keys], null)),k);
if(exists_QMARK_){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(ini_atom,cljs.core.update_in,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [section,cljs.core.cst$kw$keys,k,cljs.core.cst$kw$value], null),cljs.core.str,cljs.core.array_seq(["\n",v], 0));
} else {
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(ini_atom,cljs.core.assoc_in,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [section,cljs.core.cst$kw$keys,k], null),new cljs.core.PersistentArrayMap(null, 2, [cljs.core.cst$kw$comment,comments,cljs.core.cst$kw$value,v], null));
}

return (cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(comment_atom,"") : cljs.core.reset_BANG_.call(null,comment_atom,""));
} else {
return null;
}
}
}
}
});
/**
 * Converts an INI string into a hashmap. If a template is provided, then the
 *   ini will be formatted to correspond to the template.
 */
ini_editor.parser.parse_ini = (function ini_editor$parser$parse_ini(var_args){
var args11645 = [];
var len__6046__auto___11653 = arguments.length;
var i__6047__auto___11654 = (0);
while(true){
if((i__6047__auto___11654 < len__6046__auto___11653)){
args11645.push((arguments[i__6047__auto___11654]));

var G__11655 = (i__6047__auto___11654 + (1));
i__6047__auto___11654 = G__11655;
continue;
} else {
}
break;
}

var G__11647 = args11645.length;
switch (G__11647) {
case 1:
return ini_editor.parser.parse_ini.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return ini_editor.parser.parse_ini.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args11645.length)].join('')));

}
});

ini_editor.parser.parse_ini.cljs$core$IFn$_invoke$arity$1 = (function (s){
if(typeof s === 'string'){
} else {
throw (new Error([cljs.core.str("Assert failed: "),cljs.core.str("s is not a string"),cljs.core.str("\n"),cljs.core.str(cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.array_seq([cljs.core.list(cljs.core.cst$sym$string_QMARK_,cljs.core.cst$sym$s)], 0)))].join('')));
}

var lines = clojure.string.split_lines(s);
var ini_atom = (function (){var G__11648 = cljs.core.PersistentArrayMap.EMPTY;
return (cljs.core.atom.cljs$core$IFn$_invoke$arity$1 ? cljs.core.atom.cljs$core$IFn$_invoke$arity$1(G__11648) : cljs.core.atom.call(null,G__11648));
})();
var section_atom = (cljs.core.atom.cljs$core$IFn$_invoke$arity$1 ? cljs.core.atom.cljs$core$IFn$_invoke$arity$1("") : cljs.core.atom.call(null,""));
var comment_atom = (cljs.core.atom.cljs$core$IFn$_invoke$arity$1 ? cljs.core.atom.cljs$core$IFn$_invoke$arity$1("") : cljs.core.atom.call(null,""));
var seq__11649_11657 = cljs.core.seq(lines);
var chunk__11650_11658 = null;
var count__11651_11659 = (0);
var i__11652_11660 = (0);
while(true){
if((i__11652_11660 < count__11651_11659)){
var line_11661 = chunk__11650_11658.cljs$core$IIndexed$_nth$arity$2(null,i__11652_11660);
ini_editor.parser.ini_line_BANG_(line_11661,ini_atom,section_atom,comment_atom);

var G__11662 = seq__11649_11657;
var G__11663 = chunk__11650_11658;
var G__11664 = count__11651_11659;
var G__11665 = (i__11652_11660 + (1));
seq__11649_11657 = G__11662;
chunk__11650_11658 = G__11663;
count__11651_11659 = G__11664;
i__11652_11660 = G__11665;
continue;
} else {
var temp__4425__auto___11666 = cljs.core.seq(seq__11649_11657);
if(temp__4425__auto___11666){
var seq__11649_11667__$1 = temp__4425__auto___11666;
if(cljs.core.chunked_seq_QMARK_(seq__11649_11667__$1)){
var c__5791__auto___11668 = cljs.core.chunk_first(seq__11649_11667__$1);
var G__11669 = cljs.core.chunk_rest(seq__11649_11667__$1);
var G__11670 = c__5791__auto___11668;
var G__11671 = cljs.core.count(c__5791__auto___11668);
var G__11672 = (0);
seq__11649_11657 = G__11669;
chunk__11650_11658 = G__11670;
count__11651_11659 = G__11671;
i__11652_11660 = G__11672;
continue;
} else {
var line_11673 = cljs.core.first(seq__11649_11667__$1);
ini_editor.parser.ini_line_BANG_(line_11673,ini_atom,section_atom,comment_atom);

var G__11674 = cljs.core.next(seq__11649_11667__$1);
var G__11675 = null;
var G__11676 = (0);
var G__11677 = (0);
seq__11649_11657 = G__11674;
chunk__11650_11658 = G__11675;
count__11651_11659 = G__11676;
i__11652_11660 = G__11677;
continue;
}
} else {
}
}
break;
}

return (cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(ini_atom) : cljs.core.deref.call(null,ini_atom));
});

ini_editor.parser.parse_ini.cljs$core$IFn$_invoke$arity$2 = (function (s,template){
return ini_editor.parser.ini_apply_template(ini_editor.parser.parse_ini.cljs$core$IFn$_invoke$arity$1(s),template);
});

ini_editor.parser.parse_ini.cljs$lang$maxFixedArity = 2;
ini_editor.parser.comments_to_str = (function ini_editor$parser$comments_to_str(s){
var lines = cljs.core.map.cljs$core$IFn$_invoke$arity$2(clojure.string.trim,clojure.string.split_lines(s));
return clojure.string.join.cljs$core$IFn$_invoke$arity$1(cljs.core.map.cljs$core$IFn$_invoke$arity$2(((function (lines){
return (function (p1__11678_SHARP_){
if(clojure.string.blank_QMARK_(p1__11678_SHARP_)){
return "";
} else {
return [cljs.core.str("#"),cljs.core.str(p1__11678_SHARP_),cljs.core.str("\n")].join('');

}
});})(lines))
,lines));
});
/**
 * This is pretty stright forward for the most part, comments followed by
 *   key=value. For a vector value, a key=value is written for each item.
 */
ini_editor.parser.ini_kv_to_string = (function ini_editor$parser$ini_kv_to_string(key,data){
var key__$1 = clojure.string.trim(key);
var comment = ini_editor.parser.comments_to_str(cljs.core.cst$kw$comment.cljs$core$IFn$_invoke$arity$1(data));
var value = cljs.core.cst$kw$value.cljs$core$IFn$_invoke$arity$1(data);
if((cljs.core.some_QMARK_(comment)) && (cljs.core.some_QMARK_(value))){
} else {
throw (new Error([cljs.core.str("Assert failed: "),cljs.core.str("data must be a hasmap with the keys :value and :comment"),cljs.core.str("\n"),cljs.core.str(cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.array_seq([cljs.core.list(cljs.core.cst$sym$and,cljs.core.list(cljs.core.cst$sym$some_QMARK_,cljs.core.cst$sym$comment),cljs.core.list(cljs.core.cst$sym$some_QMARK_,cljs.core.cst$sym$value))], 0)))].join('')));
}

return [cljs.core.str(comment),cljs.core.str(((typeof value === 'string')?[cljs.core.str(key__$1),cljs.core.str("="),cljs.core.str(value),cljs.core.str("\n")].join(''):((cljs.core.vector_QMARK_(value))?clojure.string.join.cljs$core$IFn$_invoke$arity$1(cljs.core.map.cljs$core$IFn$_invoke$arity$2(((function (key__$1,comment,value){
return (function (p1__11679_SHARP_){
return [cljs.core.str(key__$1),cljs.core.str("="),cljs.core.str(clojure.string.trim([cljs.core.str(p1__11679_SHARP_)].join(''))),cljs.core.str("\n")].join('');
});})(key__$1,comment,value))
,value)):(function (){
console.warn("Unable to properly convert INI to string!");

return [cljs.core.str(key__$1),cljs.core.str("="),cljs.core.str([cljs.core.str(value)].join(''))].join('');
})()

)))].join('');
});
ini_editor.parser.ini_sec_to_string = (function ini_editor$parser$ini_sec_to_string(ini_sec,section,key_order){
return [cljs.core.str(ini_editor.parser.comments_to_str(cljs.core.cst$kw$comment.cljs$core$IFn$_invoke$arity$1(ini_sec))),cljs.core.str("["),cljs.core.str(section),cljs.core.str("]\n"),cljs.core.str(clojure.string.join.cljs$core$IFn$_invoke$arity$1(cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__11680_SHARP_){
return ini_editor.parser.ini_kv_to_string(p1__11680_SHARP_,cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(ini_sec,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$keys,p1__11680_SHARP_], null)));
}),key_order)))].join('');
});
/**
 * Converts an ini hashmap into a string. Providing an order to display sections
 *   and keys is optional.
 */
ini_editor.parser.ini_to_str = (function ini_editor$parser$ini_to_str(var_args){
var args11681 = [];
var len__6046__auto___11686 = arguments.length;
var i__6047__auto___11687 = (0);
while(true){
if((i__6047__auto___11687 < len__6046__auto___11686)){
args11681.push((arguments[i__6047__auto___11687]));

var G__11688 = (i__6047__auto___11687 + (1));
i__6047__auto___11687 = G__11688;
continue;
} else {
}
break;
}

var G__11683 = args11681.length;
switch (G__11683) {
case 1:
return ini_editor.parser.ini_to_str.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return ini_editor.parser.ini_to_str.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args11681.length)].join('')));

}
});

ini_editor.parser.ini_to_str.cljs$core$IFn$_invoke$arity$1 = (function (ini){
return ini_editor.parser.ini_to_str.cljs$core$IFn$_invoke$arity$2(ini,ini_editor.parser.ini_arbitrary_order(ini));
});

ini_editor.parser.ini_to_str.cljs$core$IFn$_invoke$arity$2 = (function (ini,order){
return clojure.string.join.cljs$core$IFn$_invoke$arity$2("\n",cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p__11684){
var vec__11685 = p__11684;
var sec = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11685,(0),null);
var keys = cljs.core.nthnext(vec__11685,(1));
return ini_editor.parser.ini_sec_to_string(cljs.core.get.cljs$core$IFn$_invoke$arity$2(ini,sec),sec,keys);
}),order));
});

ini_editor.parser.ini_to_str.cljs$lang$maxFixedArity = 2;
