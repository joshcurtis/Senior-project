(ns app.runner
    (:require [doo.runner :refer-macros [doo-tests doo-all-tests]]
              [utils.core-test]
              [utils.ini-test]
              [view.core-test]))

(doo.runner/doo-tests 'utils.core-test)
(doo.runner/doo-tests 'utils.ini-test)
(doo.runner/doo-tests 'view.core-test)
