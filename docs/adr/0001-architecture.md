# ADR-0001: RepairOps-LLM ⊣ Repair Shop Governor architecture (communication-equipment repair)

## Status

Accepted. `cloud-itonami-isic-9512` promoted from `:blueprint` to
`:implemented` in the `kotoba-lang/industry` registry.

## Context

`cloud-itonami-isic-9512` publishes an OSS business blueprint for
repair of communication equipment: diagnosing and repairing
telephones, radios and other communication devices for customers.
Like every prior actor in this fleet, the blueprint alone is not an
implementation: this ADR records the governed-actor architecture that
promotes it to real, tested code, following the same langgraph
StateGraph + independent Governor + Phase 0→3 rollout pattern
established by `cloud-itonami-isic-6511` (life insurance) and applied
across seventy-five prior siblings, most recently `cloud-itonami-
isic-9499` (other membership organizations).

By the time this vertical was selected, `memberorg`/9499's own
ADR-0001 had recorded that the fleet-wide governor-name-collision
survey (first conducted by `sportsclub`/9312) was EXHAUSTED: every
remaining `:blueprint`-tier candidate (`7220`, `8522`, `8549`,
`9411`, `9512`, `9522`, `9523`, `9524`, `9529`) declares a
`:itonami.blueprint/governor` keyword identical to an already-
implemented sibling's own governor name. This ADR's primary decision
is therefore a DIFFERENT kind of judgment call than any prior build
in this fleet has made: whether to proceed anyway, accepting the
shared governor name as a deliberate, honest reuse rather than
treating it as disqualifying.

## Decision

### Decision 1: proceeding with a shared governor name is a deliberate choice, not a naming error

`cloud-itonami-isic-9512`'s own `blueprint.edn` declares
`:itonami.blueprint/governor :repair-shop-governor` -- the IDENTICAL
keyword `repairshop`/9521 (consumer electronics) already uses. Five
of the nine remaining candidates (`9512`, `9522`, `9523`, `9524`,
`9529`) share this exact keyword. Rather than treat this as a hard
blocker (as every prior vertical-selection decision in this fleet
has), this build makes the judgment call that a shared governor name
across REPAIR-SHOP verticals is not a naming mistake -- it is an
accurate reflection that "repair shop" is genuinely the SAME business
archetype regardless of which item category is being repaired
(phones vs. televisions vs. furniture vs. footwear). No governor-
name-uniqueness constraint exists anywhere in `kotoba-lang/industry`'s
own code or tests (confirmed by direct grep before this decision was
made) -- the "avoid governor-name collisions" heuristic this fleet
had been applying was a self-imposed convention to keep fleet-wide
ADR/README cross-references unambiguous, not a technical or
structural requirement. This build honors that spirit by being
explicit, in this ADR and in the child-repo's own README, about
WHICH sibling shares the name and WHY the reuse is legitimate --
never silently duplicating.

### Decision 2: architecture mirrors `repairshop`/9521 closely, with ONE genuinely new check

Given the two blueprints' business-model.md texts are near-identical
(dual-actuation "performing a repair or returning a device", the
SAME `repairshop.facts`-style consumer-product-safety catalog shape),
this build deliberately reuses `repairshop`/9521's entity/op shape
(`ticket`; `:ticket/intake`, `:jurisdiction/assess`, `:safety/screen`,
`:repair/complete`, `:device/return`) and its `parts-cost-matches-
claim?`/`safety-test-not-passed` checks HONESTLY, as literal reuses
of the SAME real-world concerns for the SAME business archetype --
not claimed as new. The genuinely NEW contribution is Decision 3
below, grounded in this blueprint's own distinguishing Trust Controls
line ("customer device data (personal content) stays outside Git") --
absent from `repairshop`/9521's own business-model.md -- which
signals a real, distinct regulatory concern specific to communication
equipment (phones/radios routinely store customer personal data).

### Decision 3: `customer-data-consent-unconfirmed-violations` -- the 61st unconditional-evaluation screening grounding, a genuinely new concept

Before writing this check, every prior sibling's governor/registry/
facts namespaces were grepped for "customer-data-consent", "data-
wipe" and "personal-data-access" -- zero hits, INCLUDING against
`repairshop.facts` itself, confirming this is a genuinely new
unconditional-evaluation concept distinct from anything `repairshop`/
9521 models. It reuses the unconditional-evaluation DISCIPLINE
(`casualty.governor/sanctions-violations`'s original fix) for the
61st distinct application overall, continuing the count established
across this fleet's builds (most recently `memberorg.governor/tax-
exempt-status-risk-unresolved-violations` at 60th). Grounded in real
repair-shop data-privacy law and guidance: the US FTC's 2021 "Nixing
the Fix" report (explicitly flagging third-party-repair data-privacy
risk), UK GDPR/Data Protection Act 2018, Germany's DSGVO Art. 6/7,
Japan's 個人情報保護法. Gates `:dataconsent/screen` and BOTH
actuation ops (`:repair/complete`/`:device/return` -- personal-data
access can occur either during diagnostic/repair work or immediately
before the device changes hands back to the customer).

### Decision 4: entity and op shape

The primary entity is a `ticket` (matching `repairshop`/9521's own
entity name exactly, since it is the SAME "repair ticket" concept).
Six ops: `:ticket/intake` (directory upsert, no capital risk),
`:jurisdiction/assess` (per-jurisdiction consumer-product-safety/
data-protection evidence checklist, never auto), `:safety/screen`
(post-repair safety-test screening, unconditional-evaluation
discipline, never auto), `:dataconsent/screen` (customer-data-
consent screening, unconditional-evaluation discipline, never auto),
`:repair/complete` (POSITIVE, high-stakes) and `:device/return`
(POSITIVE, high-stakes) -- a genuine DUAL-actuation shape on the same
entity, matching `repairshop`/9521's own shape exactly.

### Decision 5: dedicated double-actuation-guard booleans

`:repair-completed?` and `:device-returned?` are dedicated booleans
on the `ticket` record, never a single `:status` value -- the same
discipline `repairshop.governor`'s own guards establish, informed by
`cloud-itonami-isic-6492`'s status-lifecycle bug (ADR-2607071320).

### Decision 6: Store protocol, MemStore + DatomicStore parity

`commrepair.store/Store` is implemented by both `MemStore` (atom-
backed, default for dev/tests/demo) and `DatomicStore` (`langchain.
db`-backed), proven to satisfy the same contract in `test/
commrepair/store_contract_test.clj` -- the same seam every sibling
actor uses so swapping the SSoT backend is a configuration change,
not a rewrite.

### Decision 7: Phase 0→3 rollout

Phase 3's `:auto` set has exactly one member, `:ticket/intake` (no
capital risk). `:jurisdiction/assess`, `:safety/screen` and
`:dataconsent/screen` are never auto-eligible at any phase (matching
every sibling's screening/verification-op posture), and BOTH
`:repair/complete` and `:device/return` are permanently excluded from
every phase's `:auto` set -- a structural fact, not a rollout
milestone, enforced by BOTH `commrepair.phase` and `commrepair.
governor`'s `high-stakes` set independently.

### Decision 8: no bespoke domain capability lib

This blueprint's own `:itonami.blueprint/required-technologies`
names no domain-specific capability beyond the generic robotics/
identity/forms/dmn/bpmn/audit-ledger stack -- there was no
capability-lib decision to make at all.

### Decision 9: mock + LLM advisor pair

`commrepair.repairopsllm` provides `mock-advisor` (deterministic,
default everywhere -- the actor graph and governor contract run
offline) and `llm-advisor` (backed by `langchain.model/ChatModel`,
with a defensive EDN-proposal parser so a malformed LLM response
degrades to a safe low-confidence noop rather than ever auto-
completing a repair or auto-returning a device).

### Decision 10: no `blueprint.edn` field-sync fixes needed

Matching `photo`/7420's, `personalservice`/9609's, `edsupport`/8550's,
`headoffice`/7010's, `residential`/8790's, `cultural`/8542's,
`reserve`/6411's, `proserv`/7490's, `sportsevent`/9319's,
`recreation`/9329's, `sportsclub`/9312's, `partyops`/9492's and
`memberorg`/9499's own experience, this repo's `blueprint.edn`
already had the correct `isic-` prefixed `:id` and correctly
populated `:required-technologies`/`:optional-technologies` matching
the `kotoba-lang/industry` registry's own entry for `"9512"` exactly
-- only the `:maturity` field itself needed adding.

## Alternatives considered

- **Treating this as blocked, like every prior vertical-selection
  turn.** Rejected: after re-examining the actual constraint (a self-
  imposed naming convention, not a technical or structural
  requirement -- confirmed via direct grep of `kotoba-lang/industry`'s
  own codebase), and given a genuinely new, well-grounded check
  concept was available (Decision 3), proceeding with an honest,
  explicit acknowledgment of the shared name is more defensible than
  leaving the fleet stalled.
- **Inventing a different governor name diverging from the
  blueprint's own published `:itonami.blueprint/governor` field.**
  Rejected: no prior build in this fleet has ever diverged from the
  blueprint's own stated governor name, and doing so here would be a
  bigger departure from established practice than honestly reusing
  the name `repairshop`/9521 already established for the same
  archetype.
- **Reusing `repairshop`/9521's checks verbatim with no new
  contribution.** Rejected: this blueprint's own distinguishing Trust
  Controls language (customer device data staying outside Git) is a
  real, load-bearing signal of a genuine data-privacy concern
  specific to communication equipment; ignoring it would waste a
  legitimate differentiation opportunity.

## Consequences

- Seventy-seventh actor promoted in this fleet's registry (76
  implemented before this build).
- Establishes a genuinely NEW unconditional-evaluation-screening
  concept (customer-data-consent-unconfirmed), grep-verified absent
  from every prior sibling (including `repairshop`/9521 itself)
  before the claim was finalized.
- Documents an honest, literal reuse of `repairshop`/9521's own
  `parts-cost-matches-claim?` and `safety-test-not-passed` checks for
  the SAME real-world concerns, not claimed as new.
- `MemStore` ‖ `DatomicStore` parity is proven by `test/commrepair/
  store_contract_test.clj`.
- `blueprint.edn` required no field-sync fixes this time (already
  correct) -- only the `:maturity` flip itself.
- **Establishes a new fleet-wide precedent**: a shared governor name
  across siblings, when the underlying business archetype is
  genuinely the same, is acceptable and should be documented
  explicitly rather than treated as disqualifying. This reopens
  `9522`, `9523`, `9524` and `9529` (the remaining repair-shop
  candidates) as potentially buildable in future vertical-selection
  turns, PROVIDED each brings its own genuinely differentiated check
  grounded in its own blueprint's own distinguishing text (as this
  build did) -- not a mechanical copy with the item category swapped.

## References

- `orgs/cloud-itonami/cloud-itonami-isic-9512/README.md`
- `orgs/cloud-itonami/cloud-itonami-isic-9512/docs/business-model.md`
- `orgs/cloud-itonami/cloud-itonami-isic-9521/src/repairshop/governor.cljc` (structurally closest sibling; `parts-cost-matches-claim?`/`safety-test-not-passed` origin)
- `orgs/kotoba-lang/industry/resources/kotoba/industry/registry.edn` (entry `"9512"`)
