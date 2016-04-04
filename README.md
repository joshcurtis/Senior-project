# MachineKit Editor
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
1. `boot dev`
2. Check it out on a web browser at [localhost:3000](localhost:3000)
3. To connect to the browser's REPL, run `boot repl -c`, and issue the command `(start-repl)`

## Enabling Custom Formatting for Chrome/Chromium Console
1. Open DevTools
2. Go to Settings ("three dots" icon in the upper right corner of DevTools > Menu > Settings F1 > General > Console)
3. Check-in "Enable custom formatters"
4. Close DevTools
5 .Open DevTools

## Generating HTML Code Documentation
1. `boot doc`
2. Check out the documentation html page located in the target/doc/ directory

## Development Resources
* `compojure` - [quick guide](https://learnxinyminutes.com/docs/compojure/)
* `cljs` - [modern cljs](https://github.com/magomimmo/modern-cljs)

## Resources
* [bootswatch](https://bootswatch.com) for CSS styles
* [icons](https://design.google.com/icons/), use 2x
