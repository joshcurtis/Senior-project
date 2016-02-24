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
return new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$key_DASH_value,(function (){var vec__11455 = clojure.string.split.cljs$core$IFn$_invoke$arity$3(s__$1,/=/,(2));
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11455,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11455,(1),null);
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
var vec__11459 = ini_editor.parser.categorize_ini_line(s);
var type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11459,(0),null);
var result = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11459,(1),null);
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
var vec__11460 = result;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11460,(0),null);
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11460,(1),null);
if(!(cljs.core.contains_QMARK_(cljs.core.get.cljs$core$IFn$_invoke$arity$2((cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(found_atom) : cljs.core.deref.call(null,found_atom)),(cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(section_atom) : cljs.core.deref.call(null,section_atom))),k))){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(order_atom,ini_editor.utils.update_list_head,((function (vec__11460,k,_,vec__11459,type,result){
return (function (p1__11456_SHARP_){
return cljs.core.conj.cljs$core$IFn$_invoke$arity$2(p1__11456_SHARP_,k);
});})(vec__11460,k,_,vec__11459,type,result))
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
var order_atom = (function (){var G__11468 = cljs.core.List.EMPTY;
return (cljs.core.atom.cljs$core$IFn$_invoke$arity$1 ? cljs.core.atom.cljs$core$IFn$_invoke$arity$1(G__11468) : cljs.core.atom.call(null,G__11468));
})();
var section_atom = (cljs.core.atom.cljs$core$IFn$_invoke$arity$1 ? cljs.core.atom.cljs$core$IFn$_invoke$arity$1("") : cljs.core.atom.call(null,""));
var found_atom = (function (){var G__11469 = cljs.core.PersistentArrayMap.EMPTY;
return (cljs.core.atom.cljs$core$IFn$_invoke$arity$1 ? cljs.core.atom.cljs$core$IFn$_invoke$arity$1(G__11469) : cljs.core.atom.call(null,G__11469));
})();
var seq__11470_11474 = cljs.core.seq(lines);
var chunk__11471_11475 = null;
var count__11472_11476 = (0);
var i__11473_11477 = (0);
while(true){
if((i__11473_11477 < count__11472_11476)){
var line_11478 = chunk__11471_11475.cljs$core$IIndexed$_nth$arity$2(null,i__11473_11477);
ini_editor.parser.order_line_BANG_(line_11478,order_atom,section_atom,found_atom);

var G__11479 = seq__11470_11474;
var G__11480 = chunk__11471_11475;
var G__11481 = count__11472_11476;
var G__11482 = (i__11473_11477 + (1));
seq__11470_11474 = G__11479;
chunk__11471_11475 = G__11480;
count__11472_11476 = G__11481;
i__11473_11477 = G__11482;
continue;
} else {
var temp__4425__auto___11483 = cljs.core.seq(seq__11470_11474);
if(temp__4425__auto___11483){
var seq__11470_11484__$1 = temp__4425__auto___11483;
if(cljs.core.chunked_seq_QMARK_(seq__11470_11484__$1)){
var c__5791__auto___11485 = cljs.core.chunk_first(seq__11470_11484__$1);
var G__11486 = cljs.core.chunk_rest(seq__11470_11484__$1);
var G__11487 = c__5791__auto___11485;
var G__11488 = cljs.core.count(c__5791__auto___11485);
var G__11489 = (0);
seq__11470_11474 = G__11486;
chunk__11471_11475 = G__11487;
count__11472_11476 = G__11488;
i__11473_11477 = G__11489;
continue;
} else {
var line_11490 = cljs.core.first(seq__11470_11484__$1);
ini_editor.parser.order_line_BANG_(line_11490,order_atom,section_atom,found_atom);

var G__11491 = cljs.core.next(seq__11470_11484__$1);
var G__11492 = null;
var G__11493 = (0);
var G__11494 = (0);
seq__11470_11474 = G__11491;
chunk__11471_11475 = G__11492;
count__11472_11476 = G__11493;
i__11473_11477 = G__11494;
continue;
}
} else {
}
}
break;
}

return cljs.core.reverse(((function (lines,order_atom,section_atom,found_atom){
return (function (p1__11461_SHARP_){
return cljs.core.map.cljs$core$IFn$_invoke$arity$2(cljs.core.reverse,p1__11461_SHARP_);
});})(lines,order_atom,section_atom,found_atom))
.call(null,(cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(order_atom) : cljs.core.deref.call(null,order_atom))));
});
ini_editor.parser.template_line_BANG_ = (function ini_editor$parser$template_line_BANG_(s,template_atom,section_atom,metadata_atom){
var vec__11501 = ini_editor.parser.categorize_ini_line(s);
var type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11501,(0),null);
var result = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11501,(1),null);
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,cljs.core.cst$kw$metadata)){
return cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$3(metadata_atom,cljs.core.merge,result);
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,cljs.core.cst$kw$section)){
var metadata = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.array_seq([new cljs.core.PersistentArrayMap(null, 2, [cljs.core.cst$kw$keys,cljs.core.PersistentArrayMap.EMPTY,cljs.core.cst$kw$important,true], null),(cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(metadata_atom) : cljs.core.deref.call(null,metadata_atom))], 0));
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(template_atom,cljs.core.assoc,result,metadata);

(cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(section_atom,result) : cljs.core.reset_BANG_.call(null,section_atom,result));

var G__11502 = metadata_atom;
var G__11503 = cljs.core.PersistentArrayMap.EMPTY;
return (cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(G__11502,G__11503) : cljs.core.reset_BANG_.call(null,G__11502,G__11503));
} else {
if(cljs.core._EQ_.cljs$core$IFn$_invoke$arity$2(type,cljs.core.cst$kw$key_DASH_value)){
var vec__11504 = result;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11504,(0),null);
var _ = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11504,(1),null);
var section = (cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(section_atom) : cljs.core.deref.call(null,section_atom));
var metadata = cljs.core.merge.cljs$core$IFn$_invoke$arity$variadic(cljs.core.array_seq([new cljs.core.PersistentArrayMap(null, 1, [cljs.core.cst$kw$type,"text"], null),(cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(metadata_atom) : cljs.core.deref.call(null,metadata_atom))], 0));
var exists_QMARK_ = cljs.core.some_QMARK_(cljs.core.get_in.cljs$core$IFn$_invoke$arity$2((cljs.core.deref.cljs$core$IFn$_invoke$arity$1 ? cljs.core.deref.cljs$core$IFn$_invoke$arity$1(template_atom) : cljs.core.deref.call(null,template_atom)),new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [section,cljs.core.cst$kw$keys,k], null)));
if(!(exists_QMARK_)){
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$4(template_atom,cljs.core.assoc_in,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [section,cljs.core.cst$kw$keys,k], null),metadata);
} else {
}

var G__11505 = metadata_atom;
var G__11506 = cljs.core.PersistentArrayMap.EMPTY;
return (cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2 ? cljs.core.reset_BANG_.cljs$core$IFn$_invoke$arity$2(G__11505,G__11506) : cljs.core.reset_BANG_.call(null,G__11505,G__11506));
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
var template_atom = (function (){var G__11513 = cljs.core.PersistentArrayMap.EMPTY;
return (cljs.core.atom.cljs$core$IFn$_invoke$arity$1 ? cljs.core.atom.cljs$core$IFn$_invoke$arity$1(G__11513) : cljs.core.atom.call(null,G__11513));
})();
var section_atom = (cljs.core.atom.cljs$core$IFn$_invoke$arity$1 ? cljs.core.atom.cljs$core$IFn$_invoke$arity$1("") : cljs.core.atom.call(null,""));
var metadata_atom = (function (){var G__11514 = cljs.core.PersistentArrayMap.EMPTY;
return (cljs.core.atom.cljs$core$IFn$_invoke$arity$1 ? cljs.core.atom.cljs$core$IFn$_invoke$arity$1(G__11514) : cljs.core.atom.call(null,G__11514));
})();
var seq__11515_11519 = cljs.core.seq(lines);
var chunk__11516_11520 = null;
var count__11517_11521 = (0);
var i__11518_11522 = (0);
while(true){
if((i__11518_11522 < count__11517_11521)){
var line_11523 = chunk__11516_11520.cljs$core$IIndexed$_nth$arity$2(null,i__11518_11522);
ini_editor.parser.template_line_BANG_(line_11523,template_atom,section_atom,metadata_atom);

var G__11524 = seq__11515_11519;
var G__11525 = chunk__11516_11520;
var G__11526 = count__11517_11521;
var G__11527 = (i__11518_11522 + (1));
seq__11515_11519 = G__11524;
chunk__11516_11520 = G__11525;
count__11517_11521 = G__11526;
i__11518_11522 = G__11527;
continue;
} else {
var temp__4425__auto___11528 = cljs.core.seq(seq__11515_11519);
if(temp__4425__auto___11528){
var seq__11515_11529__$1 = temp__4425__auto___11528;
if(cljs.core.chunked_seq_QMARK_(seq__11515_11529__$1)){
var c__5791__auto___11530 = cljs.core.chunk_first(seq__11515_11529__$1);
var G__11531 = cljs.core.chunk_rest(seq__11515_11529__$1);
var G__11532 = c__5791__auto___11530;
var G__11533 = cljs.core.count(c__5791__auto___11530);
var G__11534 = (0);
seq__11515_11519 = G__11531;
chunk__11516_11520 = G__11532;
count__11517_11521 = G__11533;
i__11518_11522 = G__11534;
continue;
} else {
var line_11535 = cljs.core.first(seq__11515_11529__$1);
ini_editor.parser.template_line_BANG_(line_11535,template_atom,section_atom,metadata_atom);

var G__11536 = cljs.core.next(seq__11515_11529__$1);
var G__11537 = null;
var G__11538 = (0);
var G__11539 = (0);
seq__11515_11519 = G__11536;
chunk__11516_11520 = G__11537;
count__11517_11521 = G__11538;
i__11518_11522 = G__11539;
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
return cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p__11542){
var vec__11543 = p__11542;
var sec = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11543,(0),null);
var data = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11543,(1),null);
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

var map__11546 = template;
var map__11546__$1 = ((((!((map__11546 == null)))?((((map__11546.cljs$lang$protocol_mask$partition0$ & (64))) || (map__11546.cljs$core$ISeq$))?true:false):false))?cljs.core.apply.cljs$core$IFn$_invoke$arity$2(cljs.core.hash_map,map__11546):map__11546);
var type = cljs.core.get.cljs$core$IFn$_invoke$arity$2(map__11546__$1,cljs.core.cst$kw$type);
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
var seq__11562_11576 = cljs.core.seq(secs);
var chunk__11567_11577 = null;
var count__11568_11578 = (0);
var i__11569_11579 = (0);
while(true){
if((i__11569_11579 < count__11568_11578)){
var vec__11574_11580 = chunk__11567_11577.cljs$core$IIndexed$_nth$arity$2(null,i__11569_11579);
var sec_11581 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11574_11580,(0),null);
var keys_11582 = cljs.core.nthnext(vec__11574_11580,(1));
var seq__11570_11583 = cljs.core.seq(keys_11582);
var chunk__11571_11584 = null;
var count__11572_11585 = (0);
var i__11573_11586 = (0);
while(true){
if((i__11573_11586 < count__11572_11585)){
var key_11587 = chunk__11571_11584.cljs$core$IIndexed$_nth$arity$2(null,i__11573_11586);
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(ini_atom,cljs.core.update_in,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [sec_11581,cljs.core.cst$kw$keys,key_11587,cljs.core.cst$kw$value], null),ini_editor.parser.ini_template_fix_value,cljs.core.array_seq([cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(template,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sec_11581,cljs.core.cst$kw$keys,key_11587], null))], 0));

var G__11588 = seq__11570_11583;
var G__11589 = chunk__11571_11584;
var G__11590 = count__11572_11585;
var G__11591 = (i__11573_11586 + (1));
seq__11570_11583 = G__11588;
chunk__11571_11584 = G__11589;
count__11572_11585 = G__11590;
i__11573_11586 = G__11591;
continue;
} else {
var temp__4425__auto___11592 = cljs.core.seq(seq__11570_11583);
if(temp__4425__auto___11592){
var seq__11570_11593__$1 = temp__4425__auto___11592;
if(cljs.core.chunked_seq_QMARK_(seq__11570_11593__$1)){
var c__5791__auto___11594 = cljs.core.chunk_first(seq__11570_11593__$1);
var G__11595 = cljs.core.chunk_rest(seq__11570_11593__$1);
var G__11596 = c__5791__auto___11594;
var G__11597 = cljs.core.count(c__5791__auto___11594);
var G__11598 = (0);
seq__11570_11583 = G__11595;
chunk__11571_11584 = G__11596;
count__11572_11585 = G__11597;
i__11573_11586 = G__11598;
continue;
} else {
var key_11599 = cljs.core.first(seq__11570_11593__$1);
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(ini_atom,cljs.core.update_in,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [sec_11581,cljs.core.cst$kw$keys,key_11599,cljs.core.cst$kw$value], null),ini_editor.parser.ini_template_fix_value,cljs.core.array_seq([cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(template,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sec_11581,cljs.core.cst$kw$keys,key_11599], null))], 0));

var G__11600 = cljs.core.next(seq__11570_11593__$1);
var G__11601 = null;
var G__11602 = (0);
var G__11603 = (0);
seq__11570_11583 = G__11600;
chunk__11571_11584 = G__11601;
count__11572_11585 = G__11602;
i__11573_11586 = G__11603;
continue;
}
} else {
}
}
break;
}

var G__11604 = seq__11562_11576;
var G__11605 = chunk__11567_11577;
var G__11606 = count__11568_11578;
var G__11607 = (i__11569_11579 + (1));
seq__11562_11576 = G__11604;
chunk__11567_11577 = G__11605;
count__11568_11578 = G__11606;
i__11569_11579 = G__11607;
continue;
} else {
var temp__4425__auto___11608 = cljs.core.seq(seq__11562_11576);
if(temp__4425__auto___11608){
var seq__11562_11609__$1 = temp__4425__auto___11608;
if(cljs.core.chunked_seq_QMARK_(seq__11562_11609__$1)){
var c__5791__auto___11610 = cljs.core.chunk_first(seq__11562_11609__$1);
var G__11611 = cljs.core.chunk_rest(seq__11562_11609__$1);
var G__11612 = c__5791__auto___11610;
var G__11613 = cljs.core.count(c__5791__auto___11610);
var G__11614 = (0);
seq__11562_11576 = G__11611;
chunk__11567_11577 = G__11612;
count__11568_11578 = G__11613;
i__11569_11579 = G__11614;
continue;
} else {
var vec__11575_11615 = cljs.core.first(seq__11562_11609__$1);
var sec_11616 = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11575_11615,(0),null);
var keys_11617 = cljs.core.nthnext(vec__11575_11615,(1));
var seq__11563_11618 = cljs.core.seq(keys_11617);
var chunk__11564_11619 = null;
var count__11565_11620 = (0);
var i__11566_11621 = (0);
while(true){
if((i__11566_11621 < count__11565_11620)){
var key_11622 = chunk__11564_11619.cljs$core$IIndexed$_nth$arity$2(null,i__11566_11621);
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(ini_atom,cljs.core.update_in,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [sec_11616,cljs.core.cst$kw$keys,key_11622,cljs.core.cst$kw$value], null),ini_editor.parser.ini_template_fix_value,cljs.core.array_seq([cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(template,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sec_11616,cljs.core.cst$kw$keys,key_11622], null))], 0));

var G__11623 = seq__11563_11618;
var G__11624 = chunk__11564_11619;
var G__11625 = count__11565_11620;
var G__11626 = (i__11566_11621 + (1));
seq__11563_11618 = G__11623;
chunk__11564_11619 = G__11624;
count__11565_11620 = G__11625;
i__11566_11621 = G__11626;
continue;
} else {
var temp__4425__auto___11627__$1 = cljs.core.seq(seq__11563_11618);
if(temp__4425__auto___11627__$1){
var seq__11563_11628__$1 = temp__4425__auto___11627__$1;
if(cljs.core.chunked_seq_QMARK_(seq__11563_11628__$1)){
var c__5791__auto___11629 = cljs.core.chunk_first(seq__11563_11628__$1);
var G__11630 = cljs.core.chunk_rest(seq__11563_11628__$1);
var G__11631 = c__5791__auto___11629;
var G__11632 = cljs.core.count(c__5791__auto___11629);
var G__11633 = (0);
seq__11563_11618 = G__11630;
chunk__11564_11619 = G__11631;
count__11565_11620 = G__11632;
i__11566_11621 = G__11633;
continue;
} else {
var key_11634 = cljs.core.first(seq__11563_11628__$1);
cljs.core.swap_BANG_.cljs$core$IFn$_invoke$arity$variadic(ini_atom,cljs.core.update_in,new cljs.core.PersistentVector(null, 4, 5, cljs.core.PersistentVector.EMPTY_NODE, [sec_11616,cljs.core.cst$kw$keys,key_11634,cljs.core.cst$kw$value], null),ini_editor.parser.ini_template_fix_value,cljs.core.array_seq([cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(template,new cljs.core.PersistentVector(null, 3, 5, cljs.core.PersistentVector.EMPTY_NODE, [sec_11616,cljs.core.cst$kw$keys,key_11634], null))], 0));

var G__11635 = cljs.core.next(seq__11563_11628__$1);
var G__11636 = null;
var G__11637 = (0);
var G__11638 = (0);
seq__11563_11618 = G__11635;
chunk__11564_11619 = G__11636;
count__11565_11620 = G__11637;
i__11566_11621 = G__11638;
continue;
}
} else {
}
}
break;
}

var G__11639 = cljs.core.next(seq__11562_11609__$1);
var G__11640 = null;
var G__11641 = (0);
var G__11642 = (0);
seq__11562_11576 = G__11639;
chunk__11567_11577 = G__11640;
count__11568_11578 = G__11641;
i__11569_11579 = G__11642;
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
var vec__11645 = ini_editor.parser.categorize_ini_line(s);
var type = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11645,(0),null);
var result = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11645,(1),null);
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
var vec__11646 = result;
var k = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11646,(0),null);
var v = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11646,(1),null);
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
var args11647 = [];
var len__6046__auto___11655 = arguments.length;
var i__6047__auto___11656 = (0);
while(true){
if((i__6047__auto___11656 < len__6046__auto___11655)){
args11647.push((arguments[i__6047__auto___11656]));

var G__11657 = (i__6047__auto___11656 + (1));
i__6047__auto___11656 = G__11657;
continue;
} else {
}
break;
}

var G__11649 = args11647.length;
switch (G__11649) {
case 1:
return ini_editor.parser.parse_ini.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return ini_editor.parser.parse_ini.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args11647.length)].join('')));

}
});

ini_editor.parser.parse_ini.cljs$core$IFn$_invoke$arity$1 = (function (s){
if(typeof s === 'string'){
} else {
throw (new Error([cljs.core.str("Assert failed: "),cljs.core.str("s is not a string"),cljs.core.str("\n"),cljs.core.str(cljs.core.pr_str.cljs$core$IFn$_invoke$arity$variadic(cljs.core.array_seq([cljs.core.list(cljs.core.cst$sym$string_QMARK_,cljs.core.cst$sym$s)], 0)))].join('')));
}

var lines = clojure.string.split_lines(s);
var ini_atom = (function (){var G__11650 = cljs.core.PersistentArrayMap.EMPTY;
return (cljs.core.atom.cljs$core$IFn$_invoke$arity$1 ? cljs.core.atom.cljs$core$IFn$_invoke$arity$1(G__11650) : cljs.core.atom.call(null,G__11650));
})();
var section_atom = (cljs.core.atom.cljs$core$IFn$_invoke$arity$1 ? cljs.core.atom.cljs$core$IFn$_invoke$arity$1("") : cljs.core.atom.call(null,""));
var comment_atom = (cljs.core.atom.cljs$core$IFn$_invoke$arity$1 ? cljs.core.atom.cljs$core$IFn$_invoke$arity$1("") : cljs.core.atom.call(null,""));
var seq__11651_11659 = cljs.core.seq(lines);
var chunk__11652_11660 = null;
var count__11653_11661 = (0);
var i__11654_11662 = (0);
while(true){
if((i__11654_11662 < count__11653_11661)){
var line_11663 = chunk__11652_11660.cljs$core$IIndexed$_nth$arity$2(null,i__11654_11662);
ini_editor.parser.ini_line_BANG_(line_11663,ini_atom,section_atom,comment_atom);

var G__11664 = seq__11651_11659;
var G__11665 = chunk__11652_11660;
var G__11666 = count__11653_11661;
var G__11667 = (i__11654_11662 + (1));
seq__11651_11659 = G__11664;
chunk__11652_11660 = G__11665;
count__11653_11661 = G__11666;
i__11654_11662 = G__11667;
continue;
} else {
var temp__4425__auto___11668 = cljs.core.seq(seq__11651_11659);
if(temp__4425__auto___11668){
var seq__11651_11669__$1 = temp__4425__auto___11668;
if(cljs.core.chunked_seq_QMARK_(seq__11651_11669__$1)){
var c__5791__auto___11670 = cljs.core.chunk_first(seq__11651_11669__$1);
var G__11671 = cljs.core.chunk_rest(seq__11651_11669__$1);
var G__11672 = c__5791__auto___11670;
var G__11673 = cljs.core.count(c__5791__auto___11670);
var G__11674 = (0);
seq__11651_11659 = G__11671;
chunk__11652_11660 = G__11672;
count__11653_11661 = G__11673;
i__11654_11662 = G__11674;
continue;
} else {
var line_11675 = cljs.core.first(seq__11651_11669__$1);
ini_editor.parser.ini_line_BANG_(line_11675,ini_atom,section_atom,comment_atom);

var G__11676 = cljs.core.next(seq__11651_11669__$1);
var G__11677 = null;
var G__11678 = (0);
var G__11679 = (0);
seq__11651_11659 = G__11676;
chunk__11652_11660 = G__11677;
count__11653_11661 = G__11678;
i__11654_11662 = G__11679;
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
return (function (p1__11680_SHARP_){
if(clojure.string.blank_QMARK_(p1__11680_SHARP_)){
return "";
} else {
return [cljs.core.str("#"),cljs.core.str(p1__11680_SHARP_),cljs.core.str("\n")].join('');

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
return (function (p1__11681_SHARP_){
return [cljs.core.str(key__$1),cljs.core.str("="),cljs.core.str(clojure.string.trim([cljs.core.str(p1__11681_SHARP_)].join(''))),cljs.core.str("\n")].join('');
});})(key__$1,comment,value))
,value)):(function (){
console.warn("Unable to properly convert INI to string!");

return [cljs.core.str(key__$1),cljs.core.str("="),cljs.core.str([cljs.core.str(value)].join(''))].join('');
})()

)))].join('');
});
ini_editor.parser.ini_sec_to_string = (function ini_editor$parser$ini_sec_to_string(ini_sec,section,key_order){
return [cljs.core.str(ini_editor.parser.comments_to_str(cljs.core.cst$kw$comment.cljs$core$IFn$_invoke$arity$1(ini_sec))),cljs.core.str("["),cljs.core.str(section),cljs.core.str("]\n"),cljs.core.str(clojure.string.join.cljs$core$IFn$_invoke$arity$1(cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p1__11682_SHARP_){
return ini_editor.parser.ini_kv_to_string(p1__11682_SHARP_,cljs.core.get_in.cljs$core$IFn$_invoke$arity$2(ini_sec,new cljs.core.PersistentVector(null, 2, 5, cljs.core.PersistentVector.EMPTY_NODE, [cljs.core.cst$kw$keys,p1__11682_SHARP_], null)));
}),key_order)))].join('');
});
/**
 * Converts an ini hashmap into a string. Providing an order to display sections
 *   and keys is optional.
 */
ini_editor.parser.ini_to_str = (function ini_editor$parser$ini_to_str(var_args){
var args11683 = [];
var len__6046__auto___11688 = arguments.length;
var i__6047__auto___11689 = (0);
while(true){
if((i__6047__auto___11689 < len__6046__auto___11688)){
args11683.push((arguments[i__6047__auto___11689]));

var G__11690 = (i__6047__auto___11689 + (1));
i__6047__auto___11689 = G__11690;
continue;
} else {
}
break;
}

var G__11685 = args11683.length;
switch (G__11685) {
case 1:
return ini_editor.parser.ini_to_str.cljs$core$IFn$_invoke$arity$1((arguments[(0)]));

break;
case 2:
return ini_editor.parser.ini_to_str.cljs$core$IFn$_invoke$arity$2((arguments[(0)]),(arguments[(1)]));

break;
default:
throw (new Error([cljs.core.str("Invalid arity: "),cljs.core.str(args11683.length)].join('')));

}
});

ini_editor.parser.ini_to_str.cljs$core$IFn$_invoke$arity$1 = (function (ini){
return ini_editor.parser.ini_to_str.cljs$core$IFn$_invoke$arity$2(ini,ini_editor.parser.ini_arbitrary_order(ini));
});

ini_editor.parser.ini_to_str.cljs$core$IFn$_invoke$arity$2 = (function (ini,order){
return clojure.string.join.cljs$core$IFn$_invoke$arity$2("\n",cljs.core.map.cljs$core$IFn$_invoke$arity$2((function (p__11686){
var vec__11687 = p__11686;
var sec = cljs.core.nth.cljs$core$IFn$_invoke$arity$3(vec__11687,(0),null);
var keys = cljs.core.nthnext(vec__11687,(1));
return ini_editor.parser.ini_sec_to_string(cljs.core.get.cljs$core$IFn$_invoke$arity$2(ini,sec),sec,keys);
}),order));
});

ini_editor.parser.ini_to_str.cljs$lang$maxFixedArity = 2;
