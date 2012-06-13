(ns TypeMatch.core)
(defmacro defmacro- [& all] `(defmacro ^:private ~@all))
(defmacro- public [& names] `(declare ~@names))
(defmacro- private [& names] `(declare ^:private ~@names))
(defmacro- defpublic [& all] `(defn ~@all))
(defmacro- defprivate [& all] `(defn- ~@all))
(defprivate bool? [v] (contains? #{true false} v))
(defprivate regex? [v] (instance? java.util.regex.Pattern v))

;;  Public declarations
(public   typematch?)

;;  Private declarations
(private  match?
          try-match?
          complex-match?
          list-predicate
          detail-predicate
          set-predicate
          map-predicate
          fn-predicate
          match-list?
          match-detail?
          match-set?
          match-map?
          match-fn?
          match-before
          match-after)


;;;;  Public Functions

;;  Type Match?
(defpublic typematch? [pattern data] (match? pattern data))


;;;;  Private functions

(defprivate match? [pattern data]
  (trampoline try-match? pattern data))

(defprivate try-match? [pattern data]
  " Matching.  Try keyword types, otherwise try a complex match. "
  (if (keyword? pattern)
    (case pattern
      :any    true
      :nil    (nil?     data)
      :symbol (symbol?  data)
      :bool   (bool?    data)
      :key    (keyword? data)
      :num    (number?  data)
      :int    (integer? data)
      :zero   (zero?    data)
      :pos    (and (number? data) (pos? data))
      :neg    (and (number? data) (neg? data))
      :nat    (and (integer? data) (pos? data))
      :float  (float?   data)
      :ratio  (ratio?   data)
      :str    (string?  data)
      :char   (char?    data)
      :reg    (regex?   data)
      :vec    (vector?  data)
      :seq    (seq?     data)
      :set    (set?     data)
      :map    (map?     data)
      :coll   (coll?    data)
      :fn     (fn?      data)
      false)
    #(complex-match? pattern data)))

(defprivate complex-match? [pattern data]
  " Matching with a complex object. "
  (cond
    (list-predicate pattern data)
      #(match-list? pattern data)
    (set-predicate pattern data)
      #(match-set? pattern data)
    (map-predicate pattern data)
      #(match-map? pattern data)
    (fn? pattern)
      #(match-fn? pattern data)
    :otherwise
      false))

(defprivate list-predicate [pattern data]
  (and (vector? pattern)
       (or (vector? data)
           (seq? data))))
(defprivate detail-predicate [pattern data] (seq? pattern))
(defprivate set-predicate [pattern data] (every? set? [pattern data]))
(defprivate map-predicate [pattern data] (every? map? [pattern data]))
(defprivate fn-predicate [pattern data] (fn? pattern))

;;  [:int :str :-> :int] = int, then str, then rest is vector of int
(defprivate match-list? [pattern data]
  " Matches lists, such as:
    pattern = [:int :key :-> :sym]
    data    = [5 :foo] or [42 :bar 'c 'd] "
  (let [pattern-before (take-while (partial not= :->) pattern)
        pattern-after  (drop (inc (count pattern-before)) pattern)
        data-before    (take (count pattern-before) data)
        data-after     (drop (inc (count pattern-before)) data)]
    #(and (match-before pattern-before data-before)
          (match-after pattern-after data-after))))

(defprivate match-before [pattern data]
  " Match the listed parts of a list.
    For example, if pattern were [:listed1 :listed2 :-> :rest]
    :listed1 and :listed2 "
  (every?
    (fn [[type value]] (match? type value))
    (zipmap pattern data)))

(defprivate match-after [pattern data]
  " Match the rest part of a list.
    For example, if pattern were [:listed1 :listed2 :-> :rest]
    Every element in the equivalent rest of data must match with :rest "
  (if (empty? pattern)
      (empty? data)
      (let [[type & _] pattern]
        (every? (partial match? type) data))))

;;  TODO
(defprivate match-detail? [pattern data]
  false)

;;  TODO
(defprivate match-set? [pattern data]
  false)

;;  TODO
(defprivate match-map? [pattern data]
  false)

(defprivate match-fn? [func data]
  " Use a function as a predicate. "
  (try
    (if (func data) true false)
    (catch Exception e false)))



