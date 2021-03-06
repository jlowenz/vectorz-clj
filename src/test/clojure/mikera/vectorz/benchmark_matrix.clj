(ns mikera.vectorz.benchmark-matrix
  (:use clojure.core.matrix)
  (:use clojure.core.matrix.stats)
  (:require [clojure.core.matrix.operators :refer [+ - *]])
  (:require [clojure.core.matrix.protocols :as mp])
  (:refer-clojure :exclude [+ - * ])
  (:require [criterium.core :as c])
  (:require [mikera.vectorz.matrix-api])
  (:require [mikera.vectorz.core :as v])
  (:require [mikera.vectorz.matrix :as m])
  (:import [mikera.vectorz Vector3 Vectorz]))

(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)

(set-current-implementation :vectorz)

(defn benchmarks []
  ;; direct vectorz add
  (let [^Vector3 a ( v/vec [1 2 3])
        ^Vector3 b ( v/vec [1 2 3])]
    (c/quick-bench (dotimes [i 1000] (.add a b))))
  ;; => 1.5 ns per addition
  
   ;; core.matrix mutable add
  (let [a (v/vec [1 2 3])
        b (v/vec [1 2 3])]
    (c/quick-bench (dotimes [i 1000] (add! a b))))  
  ;; => 7.5 ns per addition
   
  ;; mikera.vectorz.core mutable add
  (let [^Vector3 a (v/vec [1 2 3])
        ^Vector3 b (v/vec [1 2 3])]
    (c/quick-bench (dotimes [i 1000] (v/add! a b))))  
  ;; => 3.9 ns per addition

  
   ;; core.matrix add
  (let [a (v/vec [1 2 3])
        b (v/vec [1 2 3])]
    (c/quick-bench (dotimes [i 1000] (add a b))))  
  ;; => 15.8 ns per addition
    
  ;; direct persistent vector add
  (let [a [1 2 3]
        b [1 2 3]]
    (c/quick-bench (dotimes [i 1000] (mapv + a b))))  
  
  ;; persistent vector core.matrix add
  (let [a [1 2 3]
        b [1 2 3]]
    (c/quick-bench (dotimes [i 1000] (add a b))))  
  
  ;; Adding two regular Clojure vectors with clojure.core/+
  (let [a [1 2 3 4 5 6 7 8 9 10]
        b [1 2 3 4 5 6 7 8 9 10]]
    (c/quick-bench (dotimes [i 1000] (mapv clojure.core/+ a b))))  
  ;; => Execution time mean per addition : 285 ns
  
  ;; Adding two regular Clojure vectors with +
  (let [a [1 2 3 4 5 6 7 8 9 10]
        b [1 2 3 4 5 6 7 8 9 10]]
    (c/quick-bench (dotimes [i 1000] (+ a b))))  
  ;; => Execution time mean per addition : 285 ns
  
  ;; Adding two core.matrix vectors (pure functions, i.e. creating a new vector)
  (let [a (array :vectorz [1 2 3 4 5 6 7 8 9 10])
        b (array :vectorz [1 2 3 4 5 6 7 8 9 10])]
    (c/quick-bench (dotimes [i 1000] (add a b))))
  ;; => Execution time mean per addition: 120 ns
  
  ;; Adding two core.matrix vectors (mutable operation, i.e. adding to the first vector)
  (let [a (array :vectorz [1 2 3 4 5 6 7 8 9 10])
        b (array :vectorz [1 2 3 4 5 6 7 8 9 10])]
    (c/quick-bench (dotimes [i 1000] (add! a b))))
  ;; => Execution time mean per addition: 28 ns
  
  ;; Adding two core.matrix vectors using low level Java interop
  (let [a (Vectorz/create [1 2 3 4 5 6 7 8 9 10])
        b (Vectorz/create [1 2 3 4 5 6 7 8 9 10])]
    (c/quick-bench (dotimes [i 1000] (.add a b))))
  ;; => Execution time mean per addition: 11 ns
  
  ;; Indexed lookup with Clojure vector
  (let [a (Vectorz/create [1 2 3 4 5 6 7 8 9 10])]
    (c/quick-bench (dotimes [i 1000] (mp/get-nd a [1]))))
  ;; => Execution time mean per lookup: 14 ns


) 