# Lisp

Aidbox Forms support a subset of the Lisp language for document definition, data retrieval and initialization, calculation of dynamic (derived) attributes, and data extraction.

Supported Lisp functions are tested and documented using [zen-lang](https://github.com/zen-lang/zen). It is possible to implement your own Lisp interpreter in a programming language other than Clojure by adhering to this specification.

### Custom symbols

* `global`

### Operations

* `for`
* `get`
* `get-in`
* `get-schema`
* `vals`
* `includes?`
* `nil?`
* `+`
* `-`
* `*`
* `divide`
* `mod`
* `=`
* `>`
* `>=`
* `<`
* `<=`
* `when`
* `if`
* `cond`
* `or`
* `and`
* `not`
* `count`
* `select-keys`
* `dissoc`
* `matcho-filter`
* `matcho-remove`
* `matcho-path`
* `date`
* `timestamp`
* `parse-date`
* `format-date`
* `period`
* `dec-date`
* `inc-date`
* `sql`
* `sql*`
* `sdc->fhir-value`


### `global`

Reference to global document & rules , for use in `get`, `get-in`

> See examples for `for`

### `for`

For (loop) expression. Takes a vector of one binding-form (example: \[ ]), and returns a sequence of evaluations of expr

Examples:

```clojure
(for [i [1 2 3 4]] (+ i 10)) ;; => [11 12 13 14]
;; -------------------------------
;; ctx is {:foo [{:bar [1 2 3]} {:bar [10 20 30]}]}
(for [i (get :foo)]
  (for [j (get i :bar)]
    j)) ;; => [[1 2 3] [10 20 30]]
```

### `get`

Returns the value mapped to key or nil if key not present in associative collection, set, string, array.

If collection is not given uses local scope. Local scope can be defined by the owner of the scope (subform, grid, document)

Also can be used with variables See `global`

Examples:

```clojure
(get {:foo 10} :foo) ;; => 10
;; -----------------------
(get [10 11 12] 1) ;; => 11
;; -----------------------
(get \"hello\" 0) ;; => h
;; -----------------------
;; with ctx = {:foo 10}
(get :foo) ;; => 10
```

### `get-in`

Returns the value in a nested associative structure, where ks is a sequence of keys. Returns nil if the key is not present.

If nested associative structure is not given uses local scope. Local scope can be defined by the owner of the scope (subform, grid, document)

Also can be used with variables See `global`

Examples:

```clojure
(get-in {:foo {:bar 10}} [:foo bar]) ;; => 10
;; -----------------------
(get-in {:foo [10 11 12]} [:foo 0]) ;; => 10
;; -----------------------
;; with ctx = {:foo {:bar 10}}
(get-in [:foo :bar]) ;; => 10
```

### get-schema

Returns zen-schema for given value-path.
value-path - is vector of keys in nested associative structure

> Used in Document ctx only  (back-end)

Examples:

```
(get-schema [:foo :bar]) ;; => {:type zen/number}
;; -----------------------
(get-schema [:foo 0]) ;; => {:type zen/map ...}
```

### `vals`

Return collection of map's values. Order is arbitrary

Also can be used with variables See `global`

Examples:

```clojure
(vals {:a 10 :b 20}) ;; => (10 20)
;; -----------------------------------
;; with global = {:foo {:a 10} :bar [1 2 3]}
(vals global) ;; => ({:a 10} [1 2 3])
```

### `includes?`

Returns true when colleciton has an element, otherwise false.

Examples:

```clojure
(includes? [0] 0) ;; => true
;; ----------------
(includes? [0 2 1] 10) ;; => false
;; ----------------
(includes? [(+ 2 1)] 3) ;; => true
```

### `nil?`

Returns true if x is nil, false otherwise.

Examples:

```clojure
(nil? nil) => true
;; -------------------
(nil? 1) ;; => false
;; -------------------
(nil? []) ;; => false
;; -------------------
(nil? \"H\") ;; => false
```

### `+`

Returns the sum of nums

Examples:

```clojure
(+ 10 20) ;; => 30
;; -----------------
(+ 3 2 1) ;; => 6
;; -----------------
(+ 1 (+ 1 1)) ;; => 3
```

### `-`

If one arg is supplied, returns the negation of it, else subtracts all the followed expr from previous and returns the result.

Examples:

```clojure
(- 1) ;; => -1
;; ----------------
(- 2 1) ;; => 1
;; ----------------
(- 4 2 1) ;; => 1
;; ----------------
(- 1 (+ 1 1)) ;; => -1
```

### `*`

Returns the product of nums. (\*) returns 1.

Examples:

```clojure
(* 2 2) ;; => 4
;; ---------------------
(* 1 10 10) ;; => 100
;; ---------------------
(* 1 (* 10 0)) ;; => 0
```

### `divide`

Returns numerator divided by all of the denominators.

Examples:

```clojure
(divide 10 2) ;; => 5
;; ----------------
(divide 1000 10 10) ;; => 10
;; ----------------
(divide 100 (divide 10 1)) ;; => 10
```

### `mod`

Modulus of num and div. Truncates toward negative infinity.

Examples:

```clojure
(mod 10 3) ;; => 1
;; ----------------
(mod 7 (+ 2 1)) ;; => 1
```

### `=`

Returns true values are the same, otherwise false.

Examples:

```clojure
(= 0 0) ;; => true
;; ----------------
(= 0 0 1) ;; => false
;; ----------------
(= 1 (+ 1 0)) ;; => true
```

### `>`

Returns true if nums are in monotonically decreasing order, otherwise false.

Examples:

```clojure
(> 1 2) ;; => false
;; ----------------
(> 3 2 1) ;; => true
;; ----------------
(> 11 (+ 10 0)) ;; => true
```

### `>=`

Returns true if nums are in monotonically non-increasing order, otherwise false.

Examples:

```clojure
(>= 0 0) ;; => true
(>= 3 2 1) ;; => true
(>= 1 (+ 10 0)) ;; => false
```

### `<`

Returns true if nums are in monotonically increasing order, otherwise false.

Examples:

```clojure
(< 0 1) ;; => true
;; ----------------
(< 1 2 3) ;; => true
;; ----------------
(< 1 (+ 10 0)) ;; => true
```

### `<=`

Returns true if nums are in monotonically non-decreasing order, otherwise false.

Examples:

```clojure
(<= 0 0) ;; => true
;; ----------------
(<= 0 0 1) ;; => true
;; ----------------
(<= 1 (+ 10 0)) ;; => true
```

### `when`

Evaluates test. If logical true, evaluates body

Examples:

```clojure
(when (< 0 1) 10) ;; => 10
;; ----------------
(when (> 0 10) 10)  ;; => nil
;; ----------------
(when true (+ 10 11))  ;; => 21
```

### `if`

Evaluates test. If not the singular values nil or false, evaluates and yields then, otherwise, evaluates and yields else. If else is not supplied it defaults to nil.

Examples:

```clojure
(if true 10) ;; => 10
;; ----------------
(if false 10 11) ;; => 11
;; ----------------
(if (<= (get :foo) 0)
        (+ 10 11)
        (- 10 11)) ;; => 21
```

### `cond`

Takes a set of test/expr pairs. It evaluates each test one at a time. If a test returns logical true, cond evaluates and returns the value of the corresponding expr and doesn't evaluate any of the other tests or exprs. (cond) returns nil.

Examples:

```clojure
(cond true 10 :else 11) ;; => 10
;; ---------------------------------------
(cond false 10 :else 11) ;; => 11
;; ---------------------------------------
(cond
  (< 10 0) (+ 10 11)
  (> 10 (+ 1 10)) 10
  :else :invalid)  ;; => :invalid
```

### `or`

Evaluates exprs one at a time, from left to right. If a form returns a logical true value, or returns that value and doesn't evaluate any of the other expressions, otherwise it returns the value of the last expression. (or) returns nil.

Examples:

```clojure
(or true) ;; => true
;; -----------------------------------------
(or false true) ;; => true
;; -----------------------------------------
(or false false true) ;; => true
;; -----------------------------------------
(or (get {:foo 1} :bar)
    (get {:bar 20} :bar)) ;; => 20
```

### `and`

Evaluates exprs one at a time, from left to right. If a form returns logical false (nil or false), and returns that value and doesn't evaluate any of the other expressions, otherwise it returns the value of the last expr. (and) returns true.

Examples:

```clojure
(and true) ;; => true
;; -------------------------------------
(and false true) ;; => false
;; -------------------------------------
(and true true true) ;; => true
;; -------------------------------------
(and (get :foo)
     (- 10 11)) ;; => -1
```

### `not`

Returns true if x is logical false, false otherwise.

Examples:

```clojure
(not true) ;; => false
;; -----------------------------------
(not (get {:foo 10} :foo)) ;; => false
```

### `count`

Return the number of items in a collection

Examples:

```clojure
(count [1 2 3]) ;; => 3
```

### `matcho-filter`

Filter elements by partial match

Examples:

```clojure
(matcho-filter [{:name "John" :age 32} {:name "Jack" :age 34}] {:name "John"})
;; => [{:name "John" :age 32}]


(matcho-filter [{:name "John" :age 32} {:name "Jack" :age 34}] {:age 34})
;; => [{:name "Jack" :age 32}]
```

### `matcho-remove`

Remove elements from vector by partial match

Examples:

```clojure
(matcho-remove [{:name "John" :age 32} {:name "Jack" :age 34}] {:name "John"})
;; => [{:name "Jack" :age 34}]


(matcho-remove [{:name "John" :age 32} {:name "Jack" :age 34}] {:age 34})
;; => [{:name "John" :age 32}]
```

### `matcho-path`

Extract elements by path and filters

Examples:

```clojure
(matcho-path {:foo [1 2]} [:foo]) => [1 2]
;; ---------------------------------------------------------------
(matcho-path {:foo [{:k1 [{:bar 1 :type 'a'}]} {:k1 [{:bar 2 :type 'a'}]}]} [:foo :k1 :bar]) => [1 2]
;; ---------------------------------------------------------------
(matcho-path {:foo [{:k1 [{:bar 1 :type 'a'}]} {:k1 [{:bar 2 :type 'b'}]}]} [:foo :k1 {:type 'b'} :bar]) => [2]
```

### `select-keys`

Returns a map containing only those entries in map whose key is in keys

Examples:

```clojure
(select-keys {:a 1 :b 2 :c 3} [:a :b]) ;; => {:a 1 :b 2}
```

### `dissoc`

Returns a map that does not contain a mapping for key(s)

Examples:

```clojure
(dissoc {:a 1 :b 2 :c 3} :a) ;; => {:b 2 :c 3}
;; ----------------------------------
(dissoc {:a 1 :b 2 :c 3} :a :b) ;; => {:c 3}
```

### `date`

Returns a current datetime in given format

Examples:

```clojure
(date "yyyy-MM-dd HH:mm") => "2022-01-21 23:51"
;;--------------
(date "HH:mm") => "23:51"
```

### `timestamp`

Returns timestamp of current datetime

Examples:

```clojure
(timestamp) => 1658416557
```

### parse-date

Parses string date with given format. Returns date in format 'yyyy-MM-dd'

Examples:

```clojure
(parse-date \"10-30-2022\" \"MM-dd-yyyy\") => 2022-10-30
```

### format-date

Takes date string in 'yyyy-MM-dd' and formats it to given template.

Examples:

```clojure
(format-date \"2022-10-30\" \"MM-dd-yyyy\") => 10-30-2022
```

### `period`

Returns difference between two dates in given units. Arguments order is not matter - returns absolute value. Possible units are: :years :months :weeks :days

Examples:

```clojure
(period "2022-01-01" "2022-12-12" :months) ;; => 11
;; ------------------------------------------------
(period "2022-01-01" "2022-01-10" :weeks) ;; => 1
;; ------------------------------------------------
(period "2022-01-01" "2022-01-10" :days) ;; => 9
;; ------------------------------------------------
(period "2022-01-10" "2022-01-01" :days) ;; => 9

```

### dec-date

Returns date decreased by number of date-units.
Possible units are: :years :months :weeks :days


Examples:

```clojure
(dec-date "2022-10-10" 1 :days) ;; => "2022-10-09"
;; --------------------------------------------------
(dec-date "2022-10-10" 1 :weeks) ;; => "2022-10-03"
;; --------------------------------------------------
(dec-date "2022-10-10" 1 :months) ;; => "2022-09-10"
;; --------------------------------------------------
(dec-date "2022-10-10" 1 :years) ;; => "2021-10-10"
```

### inc-date

Returns date increased by number of date-units.
Possible units are: :years :months :weeks :days

```clojure
(inc-date "2022-10-10" 1 :days) ;; => "2022-10-11"
;; --------------------------------------------------
(inc-date "2022-10-10" 1 :weeks) ;; =>  "2022-10-17"
;; --------------------------------------------------
(inc-date "2022-10-10" 1 :months) ;; => "2022-11-10"
;; --------------------------------------------------
(inc-date "2022-10-10" 1 :years) ;; => "2023-10-10"
```

### `sql`

Run SQL query. Returns first row(as map) of result. When selected only one column - automatically unwraps result and return just value of column. To suppress unwrapping you should add column alias - it returns map with {:column-name value} Support 3 formats of query:

1. Pure sql string - "select \* from Patient"
2. Parametrized sql vector \["select \* from patient where id = ?" 101 ]
3. DSQL DSL-map {:select :\* :from :Patient :where \[:= :id 101]}

Examples:

```clojure
(sql "select * from patient where id = '1'")
;; => {:id "1" :name [{:family "Smith"}]}

(sql ["select resource#>>'{name,0,family}' from patient where id = ?" 101])
;; => "Smith"

(sql ["select resource#>>'{name,0,family}' as name from patient where id = ?" 101])
;; => {:name "Smith"}

(sql {:select [#>> :resource [:name 0 :family]]
      :from :Patient
      :where [:= :id (get :patient-id)]})
;; => "Smith"

(sql {:select {:patient-name [#>> :resource [:name 0 :family]]}
      :from :Patient
      :where [:= :id (get :patient-id)]})
;; => {:patient-name "Smith"}

(sql {:select :*
      :from :Patient
      :where [:= :id (get :patient-id)]})
;; => {:id "1" :name [{:family "Smith" :given ["John"]}]}
```



### `sql*`

Run SQL query. Same as `sql` but returns all rows.
When selected only one column - automatically unwraps result and return just value of column. To suppress unwrapping you should add column alias - it returns map with {:column-name value} Support 3 formats of query:

1. Pure sql string - "select \* from Patient"
2. Parametrized sql vector \["select \* from patient where id = ?" 101 ]
3. DSQL DSL-map {:select :\* :from :Patient :where \[:= :id 101]}

Examples:

```clojure
(sql* \"select * from patient where id in ('1', '2') \")
;; => [{:id \"1\" :name [{:family \"Smith\" } {:id \"2\" :name [{:family \"Jones\"}]}]}]

(sql* [\"select resource#>>'{name,0,family}' from patient where id in ? \" [101 102]]) ;; => [\"Smith\" \"Jones\"]

(sql* [\"select resource#>>'{name,0,family}' as name from patient where id in ? \" [101 102]]) ;; => [{:name \"Smith\"} {:name \"Jones\"}]

(sql* {:select [:#>> :resource [:name 0 :family]]
       :from :Patient
       :where [:= :id (get :patient-id)]}) ;; => [\"Smith\"]

(sql* {:select {:patient-name [:#>> :resource [:name 0 :family]]}
       :from :Patient
       :where [:= :id (get :patient-id)]}) ;; => [{:patient-name \"Smith\"}]

(sql* {:select :*
      :from :Patient
      :where [:= :id (get :patient-id)]})
;; => [{:id \"1\" :name [{:family \"Smith\" :given [\"John\"]}]}]

(sql* "select * from patient where id = '1'")
;; => {:id "1" :name [{:family "Smith"}]}
```

### `sdc->fhir-value`

Try convert sdc-node value into fhir-value (Osvervation values)


```clojure
(sdc->fhir-value {..schema...} 10) ;; => {:value {:integer 10}}
;; ----------------
(sdc->fhir-value {..schema...} {:unit \"kg\" :value 10}) ;; => {:value {:Quantity {:unit \"kg\" :value 10}}}
;; ----------------
(sdc->fhir-value {..schema...} {:code \"one\"}) ;; => {:value {:CodeableConcept {:coding [{:code [{:code \"one\"}]}]}}}
```
