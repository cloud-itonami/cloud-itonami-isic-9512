(ns commrepair.governor
  "Repair Shop Governor -- the independent compliance layer that earns
  the RepairOps-LLM the right to commit. The LLM has no notion of
  jurisdictional consumer-product-safety/data-protection law, whether
  a claimed parts cost actually equals the ticket's own parts-quantity
  times parts-unit-price, whether a device has actually passed its
  post-repair safety test, whether the customer has actually given
  informed consent for repair-technician access to personal data
  stored on the device, or when an act stops being a draft and becomes
  a real-world repair completion or device return, so this MUST be a
  separate system able to *reject* a proposal and fall back to HOLD --
  the communication-equipment-repair analog of `cloud-itonami-isic-
  9521`'s `repairshop.governor` (itself the electronics-repair analog
  of `cloud-itonami-isic-6512`'s `casualty.governor`).

  Seven checks, in priority order, ALL HARD violations: a human
  approver CANNOT override them (you don't get to approve your way
  past a fabricated jurisdiction spec-basis, incomplete repair
  evidence, a parts-cost claim that doesn't match quantity times unit-
  price, a device returned without a passed safety test, an
  unconfirmed customer-data-access consent, or a double completion/
  return). The confidence/actuation gate is SOFT: it asks a human to
  look (low confidence / actuation), and the human may approve -- but
  see `commrepair.phase`: for `:stake :actuation/complete-repair`/
  `:actuation/return-device` (a real repair completion or device
  return) NO phase ever allows auto-commit either. Two independent
  layers agree that actuation is always a human call.

  This vertical is structurally the closest sibling to `repairshop`/
  9521 (both are ISIC-Rev.5-9500s repair-shop actors sharing the
  IDENTICAL published `:itonami.blueprint/governor` keyword,
  `:repair-shop-governor` -- see this repo's own docs/adr/0001-
  architecture.md Decision 1 for why this is a deliberate, honest
  reuse of the SAME business archetype for a different repair-item
  category, not a naming error). Communication equipment (phones,
  radios) routinely stores customer personal data, a genuinely
  distinct real-world concern `repairshop`/9521's own consumer-
  electronics catalog does not model -- see Decision 3 below.

    1. Spec-basis                  -- did the jurisdiction proposal cite
                                       an OFFICIAL source (`commrepair.
                                       facts`), or invent one? Like
                                       `repairshop.governor`'s own
                                       actuation ops, `:repair/
                                       complete`/`:device/return` act
                                       directly on a pre-seeded ticket
                                       (see `commrepair.store`'s own
                                       docstring) -- there is no
                                       'ticket is missing' failure mode
                                       to guard against here.
    2. Evidence incomplete         -- for `:repair/complete`/`:device/
                                       return`, has the jurisdiction
                                       actually been assessed with a
                                       full repair-evidence checklist
                                       on file?
    3. Parts cost mismatch         -- for `:repair/complete`,
                                       INDEPENDENTLY recompute whether
                                       the ticket's own `:claimed-
                                       parts-cost` equals `parts-
                                       quantity x parts-unit-price`
                                       (`commrepair.registry/parts-
                                       cost-matches-claim?`) -- an
                                       HONEST, literal reuse of
                                       `repairshop.registry`'s own
                                       EXACT-MATCH independent-
                                       recompute check for the SAME
                                       real-world concern, not claimed
                                       as new.
    4. Safety test not passed      -- for `:device/return`, reported by
                                       THIS proposal itself (a `:safety/
                                       screen` that just found a failed
                                       test), or already on file for the
                                       ticket (`:safety/screen`/
                                       `:device/return`). Evaluated
                                       UNCONDITIONALLY (not scoped to a
                                       specific op) -- an HONEST,
                                       literal reuse of `repairshop.
                                       governor`'s own check for the
                                       SAME real-world concern (post-
                                       repair safety testing), not
                                       claimed as new.
    5. Customer-data consent
       unconfirmed                   -- for `:repair/complete`/
                                       `:device/return`, reported by
                                       THIS proposal itself (a
                                       `:dataconsent/screen` that just
                                       found consent unconfirmed), or
                                       already on file for the ticket
                                       (`:dataconsent/screen`/`:repair/
                                       complete`/`:device/return`).
                                       Evaluated UNCONDITIONALLY (not
                                       scoped to a specific op), the
                                       SAME discipline `casualty.
                                       governor/sanctions-violations`'s
                                       original fix establishes --
                                       GENUINELY NEW (grep-verified
                                       absent -- zero hits for
                                       'customer-data-consent'/'data-
                                       wipe'/'personal-data-access'
                                       across every prior sibling,
                                       including `repairshop.facts`
                                       itself), the 61st distinct
                                       application of this discipline
                                       overall (most recently
                                       `memberorg.governor/tax-exempt-
                                       status-risk-unresolved-
                                       violations` at 60th). Grounded
                                       in real repair-shop data-privacy
                                       law: US FTC's 2021 'Nixing the
                                       Fix' report, UK GDPR/Data
                                       Protection Act 2018, Germany's
                                       DSGVO Art. 6/7, Japan's 個人情報
                                       保護法.
    6. Confidence floor / actuation
       gate                          -- LLM confidence below threshold,
                                       OR the op is `:repair/complete`/
                                       `:device/return` (REAL acts) ->
                                       escalate.

  Two more guards, double-completion/double-return prevention, are
  enforced but NOT listed as numbered HARD checks above because they
  need no upstream comparison at all -- `already-completed-violations`/
  `already-returned-violations` refuse to complete/return the SAME
  ticket twice, off dedicated `:repair-completed?`/`:device-
  returned?` facts (never a `:status` value) -- the SAME 'check a
  dedicated boolean, not status' discipline every prior governor's
  guards establish, informed by `cloud-itonami-isic-6492`'s status-
  lifecycle bug (ADR-2607071320)."
  (:require [commrepair.facts :as facts]
            [commrepair.registry :as registry]
            [commrepair.store :as store]))

(def confidence-floor 0.6)

(def high-stakes
  "Stakes grave enough to always require a human, even when clean.
  Completing a real repair and returning a real device are the two
  real-world actuation events this actor performs -- a two-member set,
  matching `repairshop`/9521's own dual-actuation shape (and this
  fleet's broader dual-actuation precedent)."
  #{:actuation/complete-repair :actuation/return-device})

;; ----------------------------- checks -----------------------------

(defn- spec-basis-violations
  "A `:jurisdiction/assess` (or `:repair/complete`/`:device/return`)
  proposal with no spec-basis citation is a HARD violation -- never
  invent a jurisdiction's consumer-product-safety/data-protection
  requirements."
  [{:keys [op]} proposal]
  (when (contains? #{:jurisdiction/assess :repair/complete :device/return} op)
    (let [value (:value proposal)]
      (when (or (empty? (:cites proposal))
                (and (contains? value :spec-basis) (nil? (:spec-basis value))))
        [{:rule :no-spec-basis
          :detail "公式spec-basisの引用が無い提案は法域要件として扱えない"}]))))

(defn- evidence-incomplete-violations
  "For `:repair/complete`/`:device/return`, the jurisdiction's required
  diagnostic/parts-used/safety-test/customer-data-consent evidence
  must actually be satisfied -- do not trust the advisor's self-
  reported confidence alone."
  [{:keys [op subject]} st]
  (when (contains? #{:repair/complete :device/return} op)
    (let [t (store/ticket st subject)
          assessment (store/assessment-of st subject)]
      (when-not (and assessment
                     (facts/required-evidence-satisfied?
                      (:jurisdiction t) (:checklist assessment)))
        [{:rule :evidence-incomplete
          :detail "法域の必要書類(故障診断書/使用部品記録/安全試験記録/顧客データ同意記録等)が充足していない状態での提案"}]))))

(defn- parts-cost-mismatch-violations
  "For `:repair/complete`, INDEPENDENTLY recompute whether the
  ticket's own claimed parts cost equals parts-quantity x parts-unit-
  price via `commrepair.registry/parts-cost-matches-claim?` -- needs
  no proposal inspection or stored-verdict lookup at all, an honest
  reuse of `repairshop.registry`'s own check."
  [{:keys [op subject]} st]
  (when (= op :repair/complete)
    (let [t (store/ticket st subject)]
      (when-not (registry/parts-cost-matches-claim? t)
        [{:rule :parts-cost-mismatch
          :detail (str subject " の申告部品代金(" (:claimed-parts-cost t)
                      ")が独立再計算値(" (registry/compute-parts-cost t) ")と一致しない")}]))))

(defn- safety-test-not-passed-violations
  "A not-passed post-repair safety test -- reported by THIS proposal
  (e.g. a `:safety/screen` that itself just found a failure), or
  already on file in the store for the ticket (`:safety/screen`/
  `:device/return`) -- is a HARD, un-overridable hold. Evaluated
  UNCONDITIONALLY (not scoped to a specific op) so the screening op
  itself can HARD-hold on its own finding."
  [{:keys [op subject]} proposal st]
  (let [hit-in-proposal? (= :failed (get-in proposal [:value :verdict]))
        ticket-id (when (contains? #{:safety/screen :device/return} op) subject)
        hit-on-file? (and ticket-id (= :failed (:verdict (store/safety-screening-of st ticket-id))))]
    (when (or hit-in-proposal? hit-on-file?)
      [{:rule :safety-test-not-passed
        :detail "修理後安全試験に合格していない機器を返却する提案は進められない"}])))

(defn- customer-data-consent-unconfirmed-violations
  "An unconfirmed customer-data-access consent -- reported by THIS
  proposal (e.g. a `:dataconsent/screen` that itself just found
  consent unconfirmed), or already on file in the store for the
  ticket (`:dataconsent/screen`/`:repair/complete`/`:device/return`)
  -- is a HARD, un-overridable hold. Evaluated UNCONDITIONALLY (not
  scoped to a specific op) so the screening op itself can HARD-hold
  on its own finding."
  [{:keys [op subject]} proposal st]
  (let [hit-in-proposal? (false? (get-in proposal [:value :consent-confirmed?]))
        ticket-id (when (contains? #{:dataconsent/screen :repair/complete :device/return} op) subject)
        hit-on-file? (and ticket-id (false? (:customer-data-consent-confirmed? (store/ticket st ticket-id))))]
    (when (or hit-in-proposal? hit-on-file?)
      [{:rule :customer-data-consent-unconfirmed
        :detail "顧客個人データへのアクセスに関する同意が未確認の状態での修理完了/返却提案は進められない"}])))

(defn- already-completed-violations
  "For `:repair/complete`, refuses to complete the SAME ticket's
  repair twice, off a dedicated `:repair-completed?` fact (never a
  `:status` value)."
  [{:keys [op subject]} st]
  (when (= op :repair/complete)
    (when (store/ticket-already-completed? st subject)
      [{:rule :already-completed
        :detail (str subject " は既に修理完了済み")}])))

(defn- already-returned-violations
  "For `:device/return`, refuses to return the SAME ticket's device
  twice, off a dedicated `:device-returned?` fact (never a `:status`
  value)."
  [{:keys [op subject]} st]
  (when (= op :device/return)
    (when (store/ticket-already-returned? st subject)
      [{:rule :already-returned
        :detail (str subject " は既に返却済み")}])))

(defn check
  "Censors a RepairOps-LLM proposal against the governor rules.
  Returns {:ok? bool :violations [..] :confidence c :escalate? bool
  :high-stakes? bool :hard? bool}."
  [request _context proposal st]
  (let [hard (into []
                   (concat (spec-basis-violations request proposal)
                           (evidence-incomplete-violations request st)
                           (parts-cost-mismatch-violations request st)
                           (safety-test-not-passed-violations request proposal st)
                           (customer-data-consent-unconfirmed-violations request proposal st)
                           (already-completed-violations request st)
                           (already-returned-violations request st)))
        conf (:confidence proposal 0.0)
        low? (< conf confidence-floor)
        stakes? (boolean (high-stakes (:stake proposal)))
        hard? (boolean (seq hard))]
    {:ok?          (and (not hard?) (not low?) (not stakes?))
     :violations   hard
     :confidence   conf
     :hard?        hard?
     :escalate?    (and (not hard?) (or low? stakes?))
     :high-stakes? stakes?}))

(defn hold-fact
  "The audit fact written when a proposal is rejected (HOLD)."
  [request context verdict]
  {:t          :governor-hold
   :op         (:op request)
   :actor      (:actor-id context)
   :subject    (:subject request)
   :disposition :hold
   :basis      (mapv :rule (:violations verdict))
   :violations (:violations verdict)
   :confidence (:confidence verdict)})
