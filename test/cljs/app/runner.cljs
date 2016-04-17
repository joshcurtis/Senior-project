(ns app.runner
    (:require [doo.runner :refer-macros [doo-tests doo-all-tests]]
              [utils.core-test]))

(doo.runner/doo-tests 'utils.core-test)
