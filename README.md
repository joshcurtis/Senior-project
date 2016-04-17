# MachineKit Editor ![build-status](https://travis-ci.org/joshcurtis/MachineView.svg?branch=master "build status")
Edit specifications for use with MachineKit


## Maintainers
- Josh Curtis
- Will Medrano
- Shubham Gogna

## Requirements
1. Java - Backend for Clojure
2. [boot](boot-clj.com) - Build tooling for Clojure, insatll instructions [here](https://github.com/boot-clj/boot#install)
3. Web Browser - JavaScript capable browser. Chrome, Firefox, or Safari are recommended

## Running Development Environment
1. `lein figwheel` to host the application and provide a REPL
2. Check it out on a web browser at [localhost:3000](localhost:3000)

## Enabling Custom Formatting for Chrome/Chromium Console
1. Open DevTools
2. Go to Settings ("three dots" icon in the upper right corner of DevTools > Menu > Settings F1 > General > Console)
3. Check-in "Enable custom formatters"
4. Close DevTools
5 .Open DevTools

## Development Resources
* `compojure` - [quick guide](https://learnxinyminutes.com/docs/compojure/)
* `cljs` - [modern cljs](https://github.com/magomimmo/modern-cljs)

## Adding Javascript
1. Place files under `resources/public/js/`
2. Add them with the other `js` files in `resources/public/index.html`
3. Add definitions of variables to be used under `resources/public/js/externs.js`

## Resources
* [bootswatch](https://bootswatch.com) for CSS styles
* [icons](https://design.google.com/icons/), use 2x size
