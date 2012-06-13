# TypeMatch

A Clojure type matching library.

## Usage

```clojure
(typematch? :any nil) ;=> true
(typematch? :int 5) ;=> true
(typematch? :pos 42) ;=> true
(typematch? :str "Hello, world.") ;=> true
(typematch? [:int :str] [37 "foo"]) ;=> true
(typematch? [#(> % 5) :str] [10 "bar"]) ;=> true
(typematch? [#(> % 5) :key] [0 :foo]) ;=> false
(typematch? [:any :-> :int] [:bar 1 2 3]) ;=> true
(typematch? [:any :-> :int] [:bar 1 2 \c]) ;=> false
```

## Available Type Keywords

```clojure
{ :any    "Any type"
  :nil    "Nil"
  :symbol "Symbols"
  :bool   "Booleans"
  :key    "Keywords"
  :num    "Numbers"
  :int    "Integers"
  :zero   "Zero"
  :pos    "Positive Numbers"
  :neg    "Negative Numbers"
  :nat    "Natural Numbers"
  :float  "Floats"
  :ratio  "Ratios"
  :str    "Strings"
  :char   "Characters"
  :reg    "Regular Expressions"
  :vec    "Vectors"
  :seq    "Sequences"
  :set    "Sets"
  :map    "Maps"
  :coll   "Any collection"
  :fn     "Functions" }
```

## Todo

- Destructure maps and sets.
- Use sequences for detailed matching (with values).

## License

Copyright (C) 2012 Liam Goodacre

Distributed under the Eclipse Public License, the same as Clojure.