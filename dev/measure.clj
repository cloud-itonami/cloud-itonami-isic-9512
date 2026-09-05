(ns measure
  (:require [commrepair.store :as store]
            [commrepair.operation :as op]
            [langgraph.graph :as g]))

(def operator {:actor-id "op-1" :actor-role :repair-technician :phase 3})
(defn exec! [a tid req & [ctx]] (g/run* a {:request req :context (or ctx operator)} {:thread-id tid}))
(defn approve! [a tid] (g/run* a {:approval {:status :approved :by "tech-hanako"}} {:thread-id tid :resume? true}))
(defn reject! [a tid] (g/run* a {:approval {:status :rejected :by "tech-hanako"}} {:thread-id tid :resume? true}))

(defn -main [& _]
  (let [db (store/seed-db)
        a (op/build db)]
    (exec! a "t1-assess" {:op :jurisdiction/assess :subject "ticket-1"})
    (approve! a "t1-assess")
    (exec! a "t1-safety" {:op :safety/screen :subject "ticket-1"})
    (approve! a "t1-safety")
    (exec! a "t1-dc" {:op :dataconsent/screen :subject "ticket-1"})
    (approve! a "t1-dc")
    (exec! a "t1-complete" {:op :repair/complete :subject "ticket-1"})
    (approve! a "t1-complete")
    (println "ASSESSMENT ticket-1 =>" (pr-str (store/assessment-of db "ticket-1")))
    (println "SAFETY-SCREEN ticket-1 =>" (pr-str (store/safety-screening-of db "ticket-1")))
    (println "DC-SCREEN ticket-1 =>" (pr-str (store/dataconsent-screening-of db "ticket-1")))
    (println "COMPLETION HISTORY =>" (pr-str (store/completion-history db)))
    (println "TICKET-1 =>" (pr-str (store/ticket db "ticket-1")))
    (println "LEDGER LAST =>" (pr-str (last (store/ledger db))))
    ;; --- evidence-incomplete on an unassessed ticket
    (let [r (exec! a "t2-complete" {:op :repair/complete :subject "ticket-2"})]
      (println "t2-complete disposition =>" (:disposition (:state r))
               "verdict =>" (pr-str (:verdict (:state r)))))
    ;; --- safety screen HARD hold, then device/return on the SAME ticket
    (let [r (exec! a "t4-safety" {:op :safety/screen :subject "ticket-4"})]
      (println "t4-safety disposition =>" (:disposition (:state r))))
    (println "SAFETY-SCREEN ticket-4 after hold =>" (pr-str (store/safety-screening-of db "ticket-4")))
    (exec! a "t4-assess" {:op :jurisdiction/assess :subject "ticket-4"})
    (approve! a "t4-assess")
    (let [r (exec! a "t4-return" {:op :device/return :subject "ticket-4"})]
      (println "t4-return disposition =>" (:disposition (:state r))
               "verdict =>" (pr-str (:verdict (:state r)))
               "audit =>" (pr-str (:audit (:state r)))))
    (let [r (reject! a "t4-return")]
      (println "t4-return after REJECT disposition =>" (:disposition (:state r))))
    (println "LEDGER LAST =>" (pr-str (last (store/ledger db))))
    (println "TICKET-4 =>" (pr-str (store/ticket db "ticket-4")))
    ;; --- phase gate
    (let [r (exec! a "p1-assess" {:op :jurisdiction/assess :subject "ticket-3"} {:actor-id "op-1" :actor-role :repair-technician :phase 1})]
      (println "phase1 assess =>" (:disposition (:state r)) (pr-str (last (store/ledger db)))))
    ;; --- consent gate on actuation
    (exec! a "t5-assess" {:op :jurisdiction/assess :subject "ticket-5"})
    (approve! a "t5-assess")
    (let [r (exec! a "t5-complete" {:op :repair/complete :subject "ticket-5"})]
      (println "t5-complete =>" (:disposition (:state r)) (pr-str (:violations (:verdict (:state r))))))
    (println "LEDGER COUNT =>" (count (store/ledger db)))))
