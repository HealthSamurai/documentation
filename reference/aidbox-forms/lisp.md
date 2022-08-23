# Lisp

### Custom symbols

* `lisp/i` (`lisp/j`)
* `lisp/global`

### Operations

* `lisp/for`
* `lisp/get`
* `lisp/get-in`
* `lisp/vals`
* `lisp/includes?`
* `lisp/+`
* `lisp/-`
* `lisp/*`
* `lisp/divide`
* `lisp/mod`
* `lisp/=`
* `lisp/>`
* `lisp/>=`
* `lisp/<`
* `lisp/<=`
* `lisp/when`
* `lisp/if`
* `lisp/cond`
* `lisp/or`
* `lisp/and`
* `lisp/not`
* `lisp/count`
* `lisp/select-keys`
* `lisp/dissoc`
* `lisp/matcho-filter`
* `lisp/date`
* `lisp/timestamp`
* `lisp/sql`

### `lisp/i` (`lisp/j`)

Placeholder symbol for variable in `lisp/for` loop

### `lisp/global`

Reference to global document & rules , for use in `get`, `get-in`

> See examples for `lisp/for`

### `lisp/for`

For (loop) expression. Takes a vector of one binding-form (example: \[ ]), and returns a sequence of evaluations of expr

Examples:

```
(lisp/for [lisp/i [1 2 3 4]] (+ lisp/i 10)) ;; => [11 12 13 14]
;; -------------------------------
;; ctx is {:foo [{:bar [1 2 3]} {:bar [10 20 30]}]}
(lisp/for [lisp/i (lisp/get :foo)]
  (lisp/for [lisp/j (lisp/get lisp/i :bar)]
    lisp/j)) ;; => [[1 2 3] [10 20 30]]
```

### `lisp/get`

Returns the value mapped to key or nil if key not present in associative collection, set, string, array.

If collection is not given uses local scope. Local scope can be defined by the owner of the scope (subform, grid, document)

Also can be used with variables See `lisp/i`, `lisp/j`, `lisp/global`

Examples:

```
(lisp/get {:foo 10} :foo) ;; => 10
;; -----------------------
(lisp/get [10 11 12] 1) ;; => 11
;; -----------------------
(lisp/get \"hello\" 0) ;; => h
;; -----------------------
;; with ctx = {:foo 10}
(lisp/get :foo) ;; => 10
```

### `lisp/get-in`

Returns the value in a nested associative structure, where ks is a sequence of keys. Returns nil if the key is not present.

If nested associative structure is not given uses local scope. Local scope can be defined by the owner of the scope (subform, grid, document)

Also can be used with variables See `lisp/i`, `lisp/j`, `lisp/global`

Examples:

```
(lisp/get-in {:foo {:bar 10}} [:foo bar]) ;; => 10
;; -----------------------
(lisp/get-in {:foo [10 11 12]} [:foo 0]) ;; => 10
;; -----------------------
;; with ctx = {:foo {:bar 10}}
(lisp/get-in [:foo :bar]) ;; => 10
```

### `lisp/vals`

Return collection of map's values. Order is arbitrary

Also can be used with variables See `lisp/i`, `lisp/j`, `lisp/global`

Examples:

```
(lisp/vals {:a 10 :b 20}) ;; => (10 20)
;; -----------------------------------
;; with lisp/global = {:foo {:a 10} :bar [1 2 3]}
(lisp/vals lisp/global) ;; => ({:a 10} [1 2 3])
```

### `lisp/includes?`

Returns true when colleciton has an element, otherwise false.

Examples:

```
(lisp/includes? [0] 0) ;; => true
;; ----------------
(lisp/includes? [0 2 1] 10) ;; => false
;; ----------------
(lisp/includes? [(lisp/+ 2 1)] 3) ;; => true
```

### `lisp/+`

Returns the sum of nums

Examples:

```
(lisp/+ 10 20) ;; => 30
;; -----------------
(lisp/+ 3 2 1) ;; => 6
;; -----------------
(lisp/+ 1 (lisp/+ 1 1)) ;; => 3
```

### `lisp/-`

If one arg is supplied, returns the negation of it, else subtracts all the followed expr from previous and returns the result.

Examples:

```
(lisp/- 1) ;; => -1
;; ----------------
(lisp/- 2 1) ;; => 1
;; ----------------
(lisp/- 4 2 1) ;; => 1
;; ----------------
(lisp/- 1 (lisp/+ 1 1)) ;; => -1
```

### `lisp/*`

Returns the product of nums. (\*) returns 1.

Examples:

```
(lisp/* 2 2) ;; => 4
;; ---------------------
(lisp/* 1 10 10) ;; => 100
;; ---------------------
(lisp/* 1 (lisp/* 10 0)) ;; => 0
```

### `lisp/divide`

Returns numerator divided by all of the denominators.

Examples:

```
(lisp/divide 10 2) ;; => 5
;; ----------------
(lisp/divide 1000 10 10) ;; => 10
;; ----------------
(lisp/divide 100 (lisp/divide 10 1)) ;; => 10
```

### `lisp/mod`

Modulus of num and div. Truncates toward negative infinity.

Examples:

```
(lisp/mod 10 3) ;; => 1
;; ----------------
  (lisp/mod 7 (lisp/+ 2 1)) ;; => 1
```

### `lisp/=`

Returns true values are the same, otherwise false.

Examples:

```
(lisp/= 0 0) ;; => true
;; ----------------
(lisp/= 0 0 1) ;; => false
;; ----------------
(lisp/= 1 (lisp/+ 1 0)) ;; => true
```

### `lisp/>`

Returns true if nums are in monotonically decreasing order, otherwise false.

Examples:

```
(lisp/> 1 2) ;; => false
;; ----------------
(lisp/> 3 2 1) ;; => true
;; ----------------
(lisp/> 11 (lisp/+ 10 0)) ;; => true
```

### `lisp/>=`

Returns true if nums are in monotonically non-increasing order, otherwise false.

Examples:

```
(lisp/>= 0 0) ;; => true
(lisp/>= 3 2 1) ;; => true
(lisp/>= 1 (lisp/+ 10 0)) ;; => false
```

### `lisp/<`

Returns true if nums are in monotonically increasing order, otherwise false.

Examples:

```
(lisp/< 0 1) ;; => true
;; ----------------
(lisp/< 1 2 3) ;; => true
;; ----------------
(lisp/< 1 (lisp/+ 10 0)) ;; => true
```

### `lisp/<=`

Returns true if nums are in monotonically non-decreasing order, otherwise false.

Examples:

```
(lisp/<= 0 0) ;; => true
;; ----------------
(lisp/<= 0 0 1) ;; => true
;; ----------------
(lisp/<= 1 (lisp/+ 10 0)) ;; => true
```

### `lisp/when`

Evaluates test. If logical true, evaluates body

Examples:

```
  (lisp/when (lisp/< 0 1) 10) ;; => 10
;; ----------------
(lisp/when (lisp/> 0 10) 10)  ;; => nil
;; ----------------
(lisp/when true (lisp/+ 10 11))  ;; => 21
```

### `lisp/if`

Evaluates test. If not the singular values nil or false, evaluates and yields then, otherwise, evaluates and yields else. If else is not supplied it defaults to nil.

Examples:

```
(lisp/if true 10) ;; => 10
;; ----------------
(lisp/if false 10 11) ;; => 11
;; ----------------
(lisp/if (lisp/<= (lisp/get :foo) 0)
            (lisp/+ 10 11)
            (lisp/- 10 11)) ;; => 21
```

### `lisp/cond`

Takes a set of test/expr pairs. It evaluates each test one at a time. If a test returns logical true, cond evaluates and returns the value of the corresponding expr and doesn't evaluate any of the other tests or exprs. (cond) returns nil.

Examples:

```
(lisp/cond true 10 :else 11) ;; => 10
;; ---------------------------------------
(lisp/cond false 10 :else 11) ;; => 11
;; ---------------------------------------
(lisp/cond
  (lisp/< 10 0)             (lisp/+ 10 11)
  (lisp/> 10 (lisp/+ 1 10)) 10
  :else                     :invalid)  ;; => :invalid
```

### `lisp/or`

Evaluates exprs one at a time, from left to right. If a form returns a logical true value, or returns that value and doesn't evaluate any of the other expressions, otherwise it returns the value of the last expression. (or) returns nil.

Examples:

```
(lisp/or true) ;; => true
;; -----------------------------------------
(lisp/or false true) ;; => true
;; -----------------------------------------
(lisp/or false false true) ;; => true
;; -----------------------------------------
(lisp/or (lisp/get {:foo 1} :bar)
         (lisp/get {:bar 20} :bar)) ;; => 20
```

### `lisp/and`

Evaluates exprs one at a time, from left to right. If a form returns logical false (nil or false), and returns that value and doesn't evaluate any of the other expressions, otherwise it returns the value of the last expr. (and) returns true.

Examples:

```
(lisp/and true) ;; => true
;; -------------------------------------
(lisp/and false true) ;; => false
;; -------------------------------------
(lisp/and true true true) ;; => true
;; -------------------------------------
(lisp/and (lisp/get :foo)
          (lisp/- 10 11)) ;; => -1
```

### `lisp/not`

Returns true if x is logical false, false otherwise.

Examples:

```
(lisp/not true) ;; => false
;; -----------------------------------
  (lisp/not (lisp/get {:foo 10} :foo)) ;; => false
```

### `lisp/count`

Return the number of items in a collection

Examples:

```
(lisp/count [1 2 3]) ;; => 3
```

### `lisp/matcho-filter`

Filter elements by partial match

Examples:

```
(lisp/matcho-filter [{:name "John" :age 32} {:name "Jack" :age 34}] {:name "John"})
=> [{:name "John" :age 32}]


(lisp/matcho-filter [{:name "John" :age 32} {:name "Jack" :age 34}] {:age 34})
=> [{:name "Jack" :age 32}]
```

### `lisp/select-keys`

Returns a map containing only those entries in map whose key is in keys

Examples:

```
(lisp/select-keys {:a 1 :b 2 :c 3} [:a :b]) ;; => {:a 1 :b 2}
```

### `lisp/dissoc`

Returns a map that does not contain a mapping for key(s)

Examples:

```
(lisp/dissoc {:a 1 :b 2 :c 3} :a) ;; => {:b 2 :c 3}
;; ----------------------------------
(lisp/dissoc {:a 1 :b 2 :c 3} :a :b) ;; => {:c 3}
```

### `lisp/date`

Returns a current datetime in given format

Examples:

```
(lisp/date "yyyy-MM-dd HH:mm") => "2022-01-21 23:51"
;;--------------
(lisp/date "HH:mm") => "23:51"
```

### `lisp/timestamp`

Returns timestamp of current datetime

Examples:

```
(lisp/timestamp) => 1658416557
```

### `lisp/sql`

Run SQL query. Returns first row(as map) of result. When selected only one column - automatically unwraps result and return just value of column. To suppress unwrapping you should add column alias - it returns map with {:column-name value} Support 3 formats of query:

1. Pure sql string - "select \* from Patient"
2. Parametrized sql vector \["select \* from patient where id = ?" 101 ]
3. DSQL DSL-map {:select :\* :from :Patient :where \[:= :id 101]}

Examples:

```
(lisp/sql "select * from patient where id = '1'")
=> {:id "1" :name [{:family "Smith"}]}

(lisp/sql ["select resource#>>'{name,0,family}' from patient where id = ?" 101])
=> "Smith"

(lisp/sql ["select resource#>>'{name,0,family}' as name from patient where id = ?" 101])
=> {:name "Smith"}

(lisp/sql {:select [#>> :resource [:name 0 :family]]
           :from :Patient
           :where [:= :id (lisp/get :patient-id)]})
=> "Smith"

(lisp/sql {:select {:patient-name [#>> :resource [:name 0 :family]]}
           :from :Patient
           :where [:= :id (lisp/get :patient-id)]})
=> {:patient-name "Smith"}

(lisp/sql {:select :*
           :from :Patient
           :where [:= :id (lisp/get :patient-id)]})
=> {:id "1" :name [{:family "Smith" :given ["John"]}]}
```
