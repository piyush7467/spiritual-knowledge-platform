-- Non-secret sample reference seed for local development verification only.
-- Never store real user passwords, API keys, or private credentials here.

INSERT INTO topics (slug, title_en, title_hi, description_en, description_hi) VALUES
('supreme-creator', 'Supreme Creator (Purna Brahm)', 'पूर्ण परमात्मा का स्वरूप',
 'Scriptural references describing the Sovereign Creator above Kshar and Akshar.',
 'क्षर और अक्षर से परे पूर्ण परमात्मा के स्वरूप से संबंधित शास्त्र प्रमाण।'),
('kabir-references', 'Scriptural References to Kabir', 'शास्त्रों में कबीर साहेब के प्रमाण',
 'Original verses across traditions mentioning Kabir / KavirDev.',
 'विभिन्न पवित्र ग्रंथों में कबीर / कविर्देव से संबंधित मूल प्रमाण।')
ON CONFLICT (slug) DO NOTHING;

INSERT INTO teachers (name_en, name_hi, tradition, bio_summary) VALUES
('Sant Rampal Ji Maharaj', 'संत रामपाल जी महाराज', 'Kabir Panth / Satlok Ashram',
 'Spiritual teacher whose discourses reference Vedas, Gita, Kabir Sagar, Guru Granth Sahib, Quran, and Bible.')
ON CONFLICT DO NOTHING;

