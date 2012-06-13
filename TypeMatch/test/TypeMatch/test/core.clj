(ns TypeMatch.test.core
  (:use [TypeMatch.core])
  (:use [clojure.test]))

(defmacro defis [name pattern data]
  `(deftest ~name (is (typematch? ~pattern ~data) (str "No match on " ~pattern " " ~data))))
(defmacro defno [name pattern data]
  `(deftest ~name (is (not (typematch? ~pattern ~data)) (str "No match on " ~pattern " " ~data))))

;;  Single value -> True
(defis ttest-int :int 4)
(defis ttest-str :str "Hello, world.")
(defis ttest-vec :vec [1 2])
(defis ttest-seq :seq (list :a :b))
(defis ttest-set :set #{1 2 3})
(defis ttest-map :map {:a 1, :b 2})

;;  Single value -> False
(defno ftest-int :int "not an int")
(defno ftest-str :str 42)
(defno ftest-vec :vec '(1 2))
(defno ftest-seq :seq [:a :b])
(defno ttest-set :set [1 2 3])
(defno ftest-map :map [:a 1, :b 2])

;;  Destructured Vector/Sequence -> True
(defis ttest-v-1 [:int :str] [42 "Hello, world."])
(defis ttest-v-2 [[:int :int] :str] [[42 37] "Hello, world."])
(defis ttest-v-3 [:-> :int] [1 2 3 4 5])
(defno ttest-v-4 [:-> :int] [1 2 :foo 4 5])

;;  Sequence = Detail


;;  Destructured Set


;;  Destructured Map

