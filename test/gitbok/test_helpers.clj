(ns gitbok.test-helpers
  "Helper functions for tests"
  (:require [gitbok.state :as state]))

(defn create-test-context
  "Create a test context with a fresh atom for state.
   Optionally accepts initial state to merge with empty-state."
  ([]
   {:system (atom state/empty-state)})
  ([initial-state]
   {:system (atom (merge state/empty-state initial-state))}))