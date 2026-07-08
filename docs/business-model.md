# Business Model: Repair of communication equipment

## Classification

- Repository: `cloud-itonami-isic-9512`
- ISIC Rev.5: `9512`
- Activity: repair of communication equipment -- diagnosing and repairing telephones, radios and other communication devices for customers
- Social impact: community access, data sovereignty, transparent audit

## Customer

- independent electronics-repair shops
- cooperative repair collectives
- community right-to-repair programs

## Offer

- device intake
- diagnostic/quote proposal
- repair-completion proposal
- immutable audit ledger

## Revenue

- self-host setup: one-time implementation fee
- managed hosting: monthly subscription per shop
- support: monthly retainer with SLA
- migration: import from an incumbent repair-shop system
- per-repair fee

## Trust Controls

- no repair is performed and no device is returned without human sign-off
- a fabricated diagnostic forces a hold, not an override
- every repair path is auditable
- customer device data (personal content) stays outside Git
- emergency manual override paths remain outside LLM control
- a claimed parts cost that doesn't match the actual quantity-times-
  unit-price calculation, a failed post-repair safety test, or an
  unconfirmed customer-data-access consent -- each forces a hold, not
  an override
- a ticket's repair cannot be completed or its device returned twice:
  a double-completion/double-return attempt is held off this actor's
  own ticket facts alone, with no upstream comparison needed

## Repair Shop Governor: decision rule

`blueprint.edn` fixes `:itonami.blueprint/governor` to `:repair-shop-
governor` -- the SAME governor keyword `repairshop`/9521 (consumer
electronics) already uses, a deliberate, honest reuse of the same
business archetype for a different repair-item category (see this
repo's own `docs/adr/0001-architecture.md` Decision 1). This is not a
generic "review step," it is the one gate the TWO real-world acts
this business performs (completing a real repair, returning a real
device to the customer) must pass. The governor sits between the
RepairOps-LLM and execution, per the README's Core Contract:

```text
RepairOps-LLM -> Repair Shop Governor -> hold, proceed, or human approval
```

**Approves**: routine communication-equipment-repair actions proposed
against a ticket that already has a consented diagnostic evidence
checklist on file, satisfied required evidence, a matching parts-cost
claim, a passed post-repair safety test, and a confirmed customer-
data-access consent. These proceed straight to the ticket ledger.

**Rejects or escalates**: the governor refuses to let the advisor
complete a repair or return a device on its own authority when any of
the following hold -- a fabricated jurisdiction spec-basis; incomplete
evidence; a parts-cost mismatch; a failed safety test; an unconfirmed
customer-data-access consent; a double-completion/double-return
attempt. A clean completion/return proposal still always routes to a
human -- `:actuation/complete-repair`/`:actuation/return-device` are
never auto-committed, at any rollout phase.
