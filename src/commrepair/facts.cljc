(ns commrepair.facts
  "Per-jurisdiction consumer-product-safety AND data-protection
  regulatory catalog -- the G2-style spec-basis table the Repair Shop
  Governor checks every jurisdiction/assess proposal against ('did the
  advisor cite an OFFICIAL public source for this jurisdiction's
  requirements, or did it invent one?').

  This blueprint's own Trust Controls line ('customer device data
  (personal content) stays outside Git') and its own named activity
  (communication equipment -- telephones, radios) point at a real,
  distinct regulatory concern beyond `repairshop`/9521's own consumer-
  product-safety-only catalog: communication devices routinely store
  customer personal data (contacts, messages, call logs), and repair
  technicians accessing that data without informed consent is a
  documented real-world privacy risk (the US FTC's 2021 'Nixing the
  Fix' report explicitly flags third-party-repair data-privacy risk).
  Each jurisdiction entry below therefore cites BOTH the consumer-
  product-safety law `repairshop.facts` already models AND a data-
  protection law governing personal-data access during device repair.

  Coverage is reported HONESTLY (see `coverage`), the same discipline
  every sibling actor's `facts` namespace uses: a jurisdiction not in
  this table has NO spec-basis, full stop -- the advisor must not
  fabricate one, and the governor holds if it tries.

  Seed values are drawn from each jurisdiction's official product-
  safety AND data-protection regulators (see `:provenance`); they are
  a STARTING catalog, not a from-scratch survey of all ~194
  jurisdictions.")

(def catalog
  "iso3 -> requirement map. `:required-evidence` mirrors the generic
  diagnostic-report/parts-used-documentation/repair-technician-
  certification/post-repair-safety-test-record/customer-data-consent-
  record evidence set submitted in some form; `:legal-basis` /
  `:owner-authority` / `:provenance` are the G2 citation the governor
  requires before any :jurisdiction/assess proposal can commit."
  {"JPN" {:name "Japan"
          :owner-authority "経済産業省 (METI) / 個人情報保護委員会 (Personal Information Protection Commission)"
          :legal-basis "消費生活用製品安全法 (Consumer Product Safety Act); 個人情報保護法 (Act on the Protection of Personal Information) -- 修理受託時の個人情報取扱い"
          :national-spec "PSCマーク制度・長期使用製品安全点検制度; 個人情報保護法ガイドライン(委託先の監督)"
          :provenance "https://www.ppc.go.jp/"
          :required-evidence ["故障診断書 (diagnostic report)"
                              "使用部品記録 (parts-used documentation)"
                              "修理後安全試験記録 (post-repair safety-test record)"
                              "顧客データ同意記録 (customer-data-consent record)"]}
   "USA" {:name "United States"
          :owner-authority "U.S. Consumer Product Safety Commission (CPSC) / Federal Trade Commission (FTC)"
          :legal-basis "Consumer Product Safety Act (15 U.S.C. §§2051 et seq.); FTC Act Section 5 (unfair/deceptive practices -- repair-related data-privacy risk per the FTC's 2021 Nixing the Fix report)"
          :national-spec "CPSC product-safety standards; FTC repair-shop data-handling guidance"
          :provenance "https://www.ftc.gov/reports/nixing-fix-ftc-report-congress-repair-restrictions"
          :required-evidence ["Diagnostic report"
                              "Parts-used documentation"
                              "Post-repair safety-test record"
                              "Customer-data-consent record"]}
   "GBR" {:name "United Kingdom"
          :owner-authority "Office for Product Safety and Standards (OPSS) / Information Commissioner's Office (ICO)"
          :legal-basis "General Product Safety Regulations 2005; UK GDPR / Data Protection Act 2018 -- lawful basis for processing personal data during device servicing"
          :national-spec "OPSS product-safety enforcement standards; ICO guidance on data processing by third-party repairers"
          :provenance "https://ico.org.uk/"
          :required-evidence ["Diagnostic report"
                              "Parts-used documentation"
                              "Post-repair safety-test record"
                              "Customer-data-consent record"]}
   "DEU" {:name "Germany"
          :owner-authority "Marktüberwachungsbehörden der Länder / Landesdatenschutzbehörden"
          :legal-basis "Produktsicherheitsgesetz (ProdSG); Datenschutz-Grundverordnung (DSGVO) Art. 6/7 -- Einwilligung zur Verarbeitung personenbezogener Daten bei Reparaturannahme"
          :national-spec "ProdSG Marktüberwachungsanforderungen; DSGVO Einwilligungsanforderungen"
          :provenance "https://www.datenschutzkonferenz-online.de/"
          :required-evidence ["Diagnosebericht (diagnostic report)"
                              "Ersatzteilnachweis (parts-used documentation)"
                              "Sicherheitsprüfungsprotokoll nach Reparatur (post-repair safety-test record)"
                              "Kundendateneinwilligungsnachweis (customer-data-consent record)"]}})

(defn spec-basis
  "The jurisdiction's requirement map, or nil -- nil means NO spec-basis,
  and the governor must hold any proposal that tries to complete a
  repair or return a device on it."
  [iso3]
  (get catalog iso3))

(defn coverage
  "Honest coverage report: how many of the requested jurisdictions actually
  have a spec-basis entry. Never report a missing jurisdiction as covered."
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-isic-9512 R0: " (count catalog)
                 " jurisdictions seeded with an official spec-basis. "
                 "This is a starting catalog, not a survey of all ~194 "
                 "jurisdictions -- extend `commrepair.facts/catalog`, "
                 "never fabricate a jurisdiction's requirements.")})))

(defn required-evidence-satisfied?
  "Does `submitted` (a set/coll of evidence keywords or strings) satisfy
  every evidence item listed for `iso3`? Missing spec-basis -> never
  satisfied."
  [iso3 submitted]
  (when-let [{:keys [required-evidence]} (spec-basis iso3)]
    (let [need (count required-evidence)
          have (count (filter (set submitted) required-evidence))]
      (= need have))))

(defn evidence-checklist [iso3]
  (:required-evidence (spec-basis iso3) []))
