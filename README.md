# cloud-itonami-isic-9512

Open Business Blueprint for **ISIC Rev.5 9512**: Repair of
communication equipment.

This repository publishes a communication-equipment-repair actor --
device intake, per-jurisdiction consumer-product-safety/data-
protection regulatory assessment, post-repair safety screening,
customer-data-consent screening, repair completion and device return
-- as an OSS business that any qualified operator can fork, deploy,
run, improve and sell, so a community or independent provider never
surrenders customer/member data and ledgers to a closed SaaS.

Built on this workspace's
[`langgraph`](https://github.com/kotoba-lang/langgraph)
StateGraph runtime (portable `.cljc`, supervised superstep loop,
interrupts, Datomic/in-mem checkpoints) -- the same actor pattern as
every prior actor in this fleet
([`cloud-itonami-isic-6511`](https://github.com/cloud-itonami/cloud-itonami-isic-6511),
[`6512`](https://github.com/cloud-itonami/cloud-itonami-isic-6512),
[`6621`](https://github.com/cloud-itonami/cloud-itonami-isic-6621),
[`6622`](https://github.com/cloud-itonami/cloud-itonami-isic-6622),
[`6629`](https://github.com/cloud-itonami/cloud-itonami-isic-6629),
[`6520`](https://github.com/cloud-itonami/cloud-itonami-isic-6520),
[`6530`](https://github.com/cloud-itonami/cloud-itonami-isic-6530),
[`6820`](https://github.com/cloud-itonami/cloud-itonami-isic-6820),
[`6612`](https://github.com/cloud-itonami/cloud-itonami-isic-6612),
[`6492`](https://github.com/cloud-itonami/cloud-itonami-isic-6492),
[`6920`](https://github.com/cloud-itonami/cloud-itonami-isic-6920),
[`6611`](https://github.com/cloud-itonami/cloud-itonami-isic-6611),
[`7120`](https://github.com/cloud-itonami/cloud-itonami-isic-7120),
[`8620`](https://github.com/cloud-itonami/cloud-itonami-isic-8620),
[`8530`](https://github.com/cloud-itonami/cloud-itonami-isic-8530),
[`9200`](https://github.com/cloud-itonami/cloud-itonami-isic-9200),
[`7500`](https://github.com/cloud-itonami/cloud-itonami-isic-7500),
[`9603`](https://github.com/cloud-itonami/cloud-itonami-isic-9603),
[`9521`](https://github.com/cloud-itonami/cloud-itonami-isic-9521),
[`9321`](https://github.com/cloud-itonami/cloud-itonami-isic-9321),
[`8730`](https://github.com/cloud-itonami/cloud-itonami-isic-8730),
[`9102`](https://github.com/cloud-itonami/cloud-itonami-isic-9102),
[`9103`](https://github.com/cloud-itonami/cloud-itonami-isic-9103),
[`9602`](https://github.com/cloud-itonami/cloud-itonami-isic-9602),
[`9000`](https://github.com/cloud-itonami/cloud-itonami-isic-9000),
[`8890`](https://github.com/cloud-itonami/cloud-itonami-isic-8890),
[`8610`](https://github.com/cloud-itonami/cloud-itonami-isic-8610),
[`9311`](https://github.com/cloud-itonami/cloud-itonami-isic-9311),
[`8510`](https://github.com/cloud-itonami/cloud-itonami-isic-8510),
[`9412`](https://github.com/cloud-itonami/cloud-itonami-isic-9412),
[`6491`](https://github.com/cloud-itonami/cloud-itonami-isic-6491),
[`8720`](https://github.com/cloud-itonami/cloud-itonami-isic-8720),
[`8521`](https://github.com/cloud-itonami/cloud-itonami-isic-8521),
[`6619`](https://github.com/cloud-itonami/cloud-itonami-isic-6619),
[`3600`](https://github.com/cloud-itonami/cloud-itonami-isic-3600),
[`6190`](https://github.com/cloud-itonami/cloud-itonami-isic-6190),
[`3030`](https://github.com/cloud-itonami/cloud-itonami-isic-3030),
[`3830`](https://github.com/cloud-itonami/cloud-itonami-isic-3830),
[`7020`](https://github.com/cloud-itonami/cloud-itonami-isic-7020),
[`9420`](https://github.com/cloud-itonami/cloud-itonami-isic-9420),
[`9491`](https://github.com/cloud-itonami/cloud-itonami-isic-9491),
[`2610`](https://github.com/cloud-itonami/cloud-itonami-isic-2610),
[`3512`](https://github.com/cloud-itonami/cloud-itonami-isic-3512),
[`8810`](https://github.com/cloud-itonami/cloud-itonami-isic-8810),
[`8691`](https://github.com/cloud-itonami/cloud-itonami-isic-8691),
[`8569`](https://github.com/cloud-itonami/cloud-itonami-isic-8569),
[`6419`](https://github.com/cloud-itonami/cloud-itonami-isic-6419),
[`7310`](https://github.com/cloud-itonami/cloud-itonami-isic-7310),
[`7320`](https://github.com/cloud-itonami/cloud-itonami-isic-7320),
[`7210`](https://github.com/cloud-itonami/cloud-itonami-isic-7210),
[`7410`](https://github.com/cloud-itonami/cloud-itonami-isic-7410),
[`8710`](https://github.com/cloud-itonami/cloud-itonami-isic-8710),
[`8541`](https://github.com/cloud-itonami/cloud-itonami-isic-8541),
[`8690`](https://github.com/cloud-itonami/cloud-itonami-isic-8690),
[`9601`](https://github.com/cloud-itonami/cloud-itonami-isic-9601),
[`6420`](https://github.com/cloud-itonami/cloud-itonami-isic-6420),
[`7420`](https://github.com/cloud-itonami/cloud-itonami-isic-7420),
[`9609`](https://github.com/cloud-itonami/cloud-itonami-isic-9609),
[`8550`](https://github.com/cloud-itonami/cloud-itonami-isic-8550),
[`7010`](https://github.com/cloud-itonami/cloud-itonami-isic-7010),
[`8790`](https://github.com/cloud-itonami/cloud-itonami-isic-8790),
[`8542`](https://github.com/cloud-itonami/cloud-itonami-isic-8542),
[`6411`](https://github.com/cloud-itonami/cloud-itonami-isic-6411),
[`7490`](https://github.com/cloud-itonami/cloud-itonami-isic-7490),
[`9319`](https://github.com/cloud-itonami/cloud-itonami-isic-9319),
[`9329`](https://github.com/cloud-itonami/cloud-itonami-isic-9329),
[`9312`](https://github.com/cloud-itonami/cloud-itonami-isic-9312),
[`9492`](https://github.com/cloud-itonami/cloud-itonami-isic-9492),
[`9499`](https://github.com/cloud-itonami/cloud-itonami-isic-9499)) --
here it is **RepairOps-LLM ⊣ Repair Shop Governor** -- the SAME
governor name `repairshop`/9521 (consumer electronics) already uses,
a deliberate, honest reuse of the same repair-shop business archetype
for a different repair-item category (see `docs/adr/0001-
architecture.md` Decision 1 for why this is not a naming error).

> **Why an actor layer at all?** An LLM is great at drafting a
> diagnostic summary, normalizing records, and checking whether a
> claimed parts cost actually equals the ticket's own recorded parts-
> quantity times unit-price -- but it has **no notion of which
> jurisdiction's consumer-product-safety/data-protection law is
> official, no license to complete a real repair or return a real
> device, and no way to know on its own whether the customer has
> actually given informed consent for a technician to access personal
> data stored on their communication device**. Letting it complete a
> repair or return a device directly invites fabricated regulatory
> citations, a result being charged on top of a mismatched parts-cost
> claim, a device reaching a customer without a passed safety test,
> and a technician accessing a customer's contacts/messages/photos
> without confirmed consent -- a documented real-world risk (the US
> FTC's 2021 "Nixing the Fix" report) -- and liability, for whoever
> runs it. This project seals the RepairOps-LLM into a single node and
> wraps it with an independent **Repair Shop Governor**, a human
> **approval workflow**, and an immutable **audit ledger**.

## Scope: what this actor does and does not do

This actor covers device intake through consumer-product-safety/data-
protection regulatory assessment, post-repair safety screening,
customer-data-consent screening, repair completion and device return.
It does **not**, by itself, hold any license required to operate a
communication-equipment-repair shop in a given jurisdiction, and it
does not claim to. It also does not perform the actual repair/
diagnostic work itself, or judge repair quality -- `commrepair.
registry/parts-cost-matches-claim?` is a pure ground-truth recompute
against the ticket's own recorded fields, not a repair-quality
judgment. Whoever deploys and operates a live instance (a qualified
repair technician/shop owner) supplies any jurisdiction-specific
license, the real diagnostic/repair delivery and the real repair-
shop-management-system integrations, and bears that jurisdiction's
liability -- the software supplies the governed, spec-cited, audited
execution scaffold so that operator does not have to build the
compliance layer from scratch.

### Actuation

**Completing a real repair and returning a real device to the
customer are never autonomous, at any phase, by construction.** Two
independent layers enforce this (`commrepair.governor`'s `:actuation/
complete-repair`/`:actuation/return-device` high-stakes gate and
`commrepair.phase`'s phase table, which never puts either op in any
phase's `:auto` set) -- see `commrepair.phase`'s docstring and
`test/commrepair/phase_test.clj`'s `repair-complete-never-auto-at-
any-phase`/`device-return-never-auto-at-any-phase`. The actor may
draft, check and recommend; a human repair technician is always the
one who actually completes a repair or returns a device. Grounded
directly in this blueprint's own README text ("No automated proposal,
by itself, can complete the following without governor approval and
audit evidence: performing a repair or returning a device to the
customer") -- a genuine DUAL-actuation shape (two distinct real-world
acts on the same ticket), matching `marketadmin`/`registrar`/
`wagering`/`repairshop`/`nursing`/`laundry`/`holdco`/`residential`/
`reserve`'s own dual-actuation precedent, structurally identical to
`repairshop`/9521's own `:actuation/complete-repair`/`:actuation/
return-device` shape (the same business archetype, applied to
communication equipment rather than consumer electronics).

## The core contract

```
device intake + jurisdiction facts (commrepair.facts, spec-cited)
        |
        v
   ┌───────────────────────┐   proposal      ┌───────────────────────┐
   │ RepairOps-LLM         │ ─────────────▶ │ Repair Shop                    │  (independent system)
   │ (sealed)              │  + citations    │ Governor:                    │
   └───────────────────────┘                 │ spec-basis · evidence-       │
          │                 commit ◀┼ incomplete · parts-cost-         │
          │                         │ mismatch (honest reuse) ·             │
    record + ledger        escalate ┼ safety-test-not-passed (honest         │
          │              (ALWAYS for│ reuse) · customer-data-consent-          │
          │       :actuation/complete│ unconfirmed (unconditional, NEW) ·        │
          │       -repair/:actuation/│ already-completed · already-returned       │
          ▼       return-device)     └───────────────────────┘
      human approval
```

**The RepairOps-LLM never completes a repair or returns a device the
Repair Shop Governor would reject, and never does so without a human
sign-off.** Hard violations (fabricated regulatory requirements;
unsupported evidence; a parts-cost mismatch; a failed safety test; an
unconfirmed customer-data-access consent; a double completion/return)
force **hold** and *cannot* be approved past; a clean completion/
return proposal still always routes to a human.

## Run

```bash
clojure -M:dev:run     # walk one clean dual-actuation lifecycle + five HARD-hold cases through the actor
clojure -M:dev:test    # governor contract · phase invariants · store parity · registry conformance · facts coverage
clojure -M:lint        # clj-kondo (errors fail; CI mirrors this)
```

## Robotics premise

All cloud-itonami verticals are designed on the premise that a **robot
performs the physical domain work**. Here a diagnostic-bench robot
assists physical device testing and repair, under the actor, gated by
the independent **Repair Shop Governor**. The governor never
dispatches hardware itself; `:high`/`:safety-critical` actions
require human sign-off.

## Open business

This repository is not only source code. It is a public, forkable
business model:

| Layer | What is open |
|---|---|
| OSS core | Actor runtime, Repair Shop Governor, repair-completion/device-return draft records, audit ledger |
| Business blueprint | Customer, offer, pricing, unit economics, sales motion |
| Operator playbook | How to fork, license, deploy and support the service in a jurisdiction |
| Trust controls | Governance, security reporting, actuation invariant, audit requirements |

See [`docs/business-model.md`](docs/business-model.md) and
[`docs/operator-guide.md`](docs/operator-guide.md) to start this as an
open business on itonami.cloud, and
[`docs/adr/0001-architecture.md`](docs/adr/0001-architecture.md) for the
full architecture and decision record.

## Capability layer

This blueprint resolves its technology stack via
[`kotoba-lang/industry`](https://github.com/kotoba-lang/industry) (ISIC
`9512`). This vertical's service/member records are practice-specific
rather than a shared cross-operator data contract, so `commrepair.*`
runs on the generic robotics/identity/forms/dmn/bpmn/audit-ledger
stack only -- no bespoke domain capability lib to reference at all.

## Layout

| File | Role |
|---|---|
| `src/commrepair/store.cljc` | **Store** protocol -- `MemStore` ‖ `DatomicStore` (`langchain.db`) + append-only audit ledger + repair-completion AND device-return history (dual history, mirroring `repairshop`/9521's own shape). The double-actuation guard checks dedicated `:repair-completed?`/`:device-returned?` booleans rather than a `:status` value |
| `src/commrepair/registry.cljc` | Repair-completion/device-return draft records, plus `parts-cost-matches-claim?` -- an HONEST, literal reuse of `repairshop.registry`'s own EXACT-MATCH independent-recompute check for the SAME real-world concern, not claimed as new |
| `src/commrepair/facts.cljc` | Per-jurisdiction consumer-product-safety AND data-protection catalog (a genuine extension beyond `repairshop.facts`'s own product-safety-only catalog) with an official spec-basis citation per entry, honest coverage reporting |
| `src/commrepair/repairopsllm.cljc` | **RepairOps-LLM** -- `mock-advisor` ‖ `llm-advisor`; intake/jurisdiction-assessment/safety-screening/data-consent-screening/repair-completion/device-return proposals |
| `src/commrepair/governor.cljc` | **Repair Shop Governor** -- 7 HARD checks (spec-basis · evidence-incomplete · parts-cost-mismatch, honest reuse of `repairshop`'s own check · safety-test-not-passed, honest reuse of `repairshop`'s own check · customer-data-consent-unconfirmed, unconditional evaluation, GENUINELY NEW, the 61st grounding of this discipline · already-completed guard · already-returned guard) + 1 soft (confidence/actuation gate) |
| `src/commrepair/phase.cljc` | **Phase 0→3** -- read-only → assisted intake → assisted assess → supervised (repair completion/device return always human; ticket intake is the ONLY auto-eligible op, no direct capital risk) |
| `src/commrepair/operation.cljc` | **OperationActor** -- langgraph-clj StateGraph |
| `src/commrepair/sim.cljc` | demo driver |
| `test/commrepair/*_test.clj` | governor contract · phase invariants · store parity · registry conformance · facts coverage |

## Business-process coverage (honest)

This actor covers device intake through consumer-product-safety/data-
protection regulatory assessment, post-repair safety screening,
customer-data-consent screening, repair completion and device return
-- the core governed lifecycle this blueprint's own `docs/business-
model.md` names as its Offer:

| Covered | Not covered (out of scope for this R0) |
|---|---|
| Ticket intake + per-jurisdiction evidence checklisting, HARD-gated on an official spec-basis citation (`:ticket/intake`/`:jurisdiction/assess`) | Real repair-shop-management-system integration, real diagnostic/repair work itself (see `commrepair.facts`'s docstring) |
| Post-repair safety screening + customer-data-consent screening, each evaluated unconditionally so the screening op itself can HARD-hold on its own finding (`:safety/screen`/`:dataconsent/screen`) | Repair-quality judgment itself -- deliberately outside this actor's competence |
| Repair completion, HARD-gated on full evidence and a matching parts-cost claim, plus a double-completion guard (`:actuation/complete-repair`) | |
| Device return, HARD-gated on full evidence, a passed safety test and a confirmed data-access consent, plus a double-return guard (`:actuation/return-device`) | |
| Immutable audit ledger for every intake/assessment/screening/completion/return decision | |

Extending coverage is additive: add the next gate (e.g. a warranty-
coverage-verification check) as its own governed op with its own HARD
checks and tests, following the SAME "an independent governor
re-verifies against the actor's own records before any real-world
act" pattern this repo's flagship ops already establish.

## Jurisdiction coverage (honest)

`commrepair.facts/coverage` reports how many requested jurisdictions
actually have an official spec-basis in `commrepair.facts/catalog` --
currently 4 seeded (JPN, USA, GBR, DEU) out of ~194 jurisdictions
worldwide. This is a starting catalog to prove the governor contract
end-to-end, not a claim of global coverage. Adding a jurisdiction is
additive: one map entry in `commrepair.facts/catalog`, citing a real
official source -- never fabricate a jurisdiction's requirements to
make coverage look bigger.

## Maturity

`:implemented` -- `RepairOps-LLM` + `Repair Shop Governor` run as
real, tested code (see `Run` above), promoted from the originally-
published `:blueprint`-tier scaffold, modeled closely on `repairshop`/
9521's own architecture and the seventy-five other prior actors'
architecture across this fleet. See `docs/adr/0001-architecture.md`
for the history and design.

## License

Code and implementation templates are AGPL-3.0-or-later.
