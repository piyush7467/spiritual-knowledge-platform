# Editorial Standards, Evidence State Machine & Bilingual Rules

---

## 1. The 6 Knowledge Layers (Strict Separation)

Every record and UI block must explicitly declare which layer it belongs to:
1. **Original Source Text** — Preserved verbatim in original script/language; never silently altered.
2. **Published Translation** — Credited to specific translator/edition or labeled clearly if machine-assisted draft.
3. **Commentator / Translator Interpretation** — Labeled as commentary, never as primary scripture text.
4. **Attributed Teacher Statement** — Linked to specific speaker/book/discourse/timestamp; paraphrases never marked as verbatim quotes.
5. **Editorial Research Summary** — Human-reviewed synthesis linking primary evidence records.
6. **AI-Generated Explanation** — Dynamically marked as AI synthesis citing only verified database records.

---

## 2. Verification & Publishing State Machine

```text
DISCOVERED ──▶ REGISTERED ──▶ PROCESSING ──▶ NEEDS_REVIEW ──▶ VERIFIED ──▶ PUBLISHED
                                                   │               │            │
                                                   ▼               ▼            ▼
                                            NEEDS_CORRECTION    REJECTED     ARCHIVED
```

- `VERIFIED` means **checked against the stated edition/source**, NOT a religious truth judgment.
- Only `SUPER_ADMIN` (and future delegated `REVIEWER`/`ADMIN`) can transition records to `VERIFIED` or `PUBLISHED`.
- Every transition writes an immutable `verification_reviews` row, `content_versions` snapshot, and `audit_logs` entry.
- Unapproved records (`!= PUBLISHED`) are **invisible** to public visitors and **excluded** from production RAG retrieval.

---

## 3. Comparative Analysis Categories

When comparing **(1) Original Scripture**, **(2) Other Saints/Commentators**, and **(3) Teachings of Sant Rampal Ji Maharaj**, findings must be classified into exact neutral categories:
- `AGREEMENT`
- `TRANSLATION_DIFFERENCE`
- `INTERPRETATION_DIFFERENCE`
- `CONTRADICTION` (used *only* when claims are genuinely incompatible in full context)
- `MISSING_CONTEXT`
- `INCONCLUSIVE`

---

## 4. Bilingual (Hindi & English) Strategy

- **UI Chrome:** Maintained static JSON resource bundles (`en.json`, `hi.json`), never machine-translated on page load.
- **Domain Glossary (`glossary_terms`):** Consistent Hindi ↔ English spiritual terminology so names, scriptures, and key terms stay uniform.
- **Machine Translation:** Allowed only to generate reviewable drafts (`translation_status = 'MACHINE_DRAFT'`) storing provider + model version; never auto-replaces human-reviewed translations.

---

## 5. Copyright & External Content Policy

- Store official third-party URLs and permitted official embeds only; **never** download or rehost unauthorized third-party books/videos.
- Record `source_rights` (copyright status, permission notes, attribution text, access restrictions) on every source.
- Public **Correction & Copyright Takedown** form enters private moderation queue (`copyright_reports` / `correction_reports`) with full decision audit trail.

