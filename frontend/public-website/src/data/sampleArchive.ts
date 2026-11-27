export interface VerifiedPassageRecord {
  publicId: string;
  sourceTitle: { en: string; hi: string };
  referenceHierarchy: { en: string; hi: string };
  canonicalRef: string;
  edition: { en: string; hi: string };
  publisher: { en: string; hi: string };
  printedPage: string;
  pdfPage: number;
  rightsNote: { en: string; hi: string };
  verifiedDate: string;
  originalScript: string;
  originalLanguageLabel: { en: string; hi: string };
  publishedTranslation: { en: string; hi: string };
  attributionContext: { en: string; hi: string };
  contextBefore: { en: string; hi: string };
  contextAfter: { en: string; hi: string };
  topic: { en: string; hi: string };
}

export const SAMPLE_PASSAGES: VerifiedPassageRecord[] = [
  {
    publicId: 'psg-rv-10-90-01',
    sourceTitle: { en: 'Rigveda Samhita', hi: 'ऋग्वेद संहिता' },
    referenceHierarchy: {
      en: 'Mandal 10 · Sukta 90 · Mantra 1',
      hi: 'मण्डल 10 · सूक्त 90 · मंत्र 1',
    },
    canonicalRef: 'RIGVEDA-10-90-1',
    edition: {
      en: 'Sanskrit Mool with Hindi/English Reference Edition (2014 Reprint)',
      hi: 'संस्कृत मूल एवं प्रमाणित संदर्भ संस्करण (2014 पुनर्मुद्रण)',
    },
    publisher: {
      en: 'Archival Print Preservation',
      hi: 'अभिलेखीय मुद्रित संस्करण',
    },
    printedPage: '642',
    pdfPage: 658,
    rightsNote: {
      en: 'Original public-domain Vedic verse; editorial attribution preserved.',
      hi: 'मूल सार्वजनिक-डोमेन वैदिक मंत्र; संपादकीय स्रोत श्रेय सुरक्षित।',
    },
    verifiedDate: '2026-11-20',
    originalScript:
      'सहस्रशीर्षा पुरुषः सहस्राक्षः सहस्रपात् । स भूमिं विश्वतो वृत्वात्यतिष्ठद्दशाङ्गुलम् ॥१॥',
    originalLanguageLabel: { en: 'Sanskrit (Original Text)', hi: 'संस्कृत (मूल पाठ)' },
    publishedTranslation: {
      en: 'The Supreme Purusha has a thousand heads, a thousand eyes, and a thousand feet; pervading the entire earth from all sides, He stands transcending it by ten fingers’ measure.',
      hi: 'सहस्र (अनंत) सिर, सहस्र नेत्र और सहस्र चरणों वाले परम पुरुष ने समस्त भूमि को सब ओर से व्याप्त कर उससे दस अंगुल ऊपर (परे) स्थित हैं।',
    },
    attributionContext: {
      en: 'Cited in discourses of Sant Rampal Ji Maharaj to examine the embodied, sovereign nature of the Supreme Creator (Purna Brahm).',
      hi: 'संत रामपाल जी महाराज के सत्संगों में पूर्ण परमात्मा के साकार एवं सर्वव्यापी स्वरूप के प्रमाण के रूप में उद्धृत।',
    },
    contextBefore: {
      en: 'Opening invocation of the Purusha Sukta in Rigveda Mandal 10.',
      hi: 'ऋग्वेद मण्डल 10 के पुरुष सूक्त का प्रारंभिक मंत्र।',
    },
    contextAfter: {
      en: 'Followed by Mantra 2 describing Purusha as Lord of immortality and all creation.',
      hi: 'इसके पश्चात मंत्र 2 में परम पुरुष को अमरत्व और समस्त सृष्टि का स्वामी कहा गया है।',
    },
    topic: { en: 'Supreme Creator (Purna Brahm)', hi: 'पूर्ण परमात्मा का स्वरूप' },
  },
  {
    publicId: 'psg-bg-15-17',
    sourceTitle: { en: 'Shrimad Bhagavad Gita', hi: 'श्रीमद्भगवद्गीता' },
    referenceHierarchy: {
      en: 'Chapter 15 · Verse 17',
      hi: 'अध्याय 15 · श्लोक 17',
    },
    canonicalRef: 'GITA-15-17',
    edition: {
      en: 'Gita Press Gorakhpur Edition (Code 12)',
      hi: 'गीता प्रेस गोरखपुर संस्करण (कोड 12)',
    },
    publisher: { en: 'Gita Press, Gorakhpur', hi: 'गीता प्रेस, गोरखपुर' },
    printedPage: '214',
    pdfPage: 221,
    rightsNote: {
      en: 'Exact edition and printed page cited for verification; no unauthorized PDF redistribution.',
      hi: 'सत्यापन हेतु सटीक संस्करण एवं मुद्रित पृष्ठ उद्धृत; अनधिकृत पीडीएफ वितरण नहीं।',
    },
    verifiedDate: '2026-11-22',
    originalScript:
      'उत्तमः पुरुषस्त्वन्यः परमात्मेत्युदाहृतः । यो लोकत्रयमाविश्य बिभर्त्यव्यय ईश्वरः ॥१७॥',
    originalLanguageLabel: { en: 'Sanskrit (Original Verse)', hi: 'संस्कृत (मूल श्लोक)' },
    publishedTranslation: {
      en: 'But distinct from both (Kshar and Akshar) is the Supreme Purusha, declared as Paramatma, who enters and sustains the three worlds as the Imperishable Lord.',
      hi: 'परंतु इन दोनों (क्षर और अक्षर पुरुष) से उत्तम पुरुष तो कोई अन्य ही है, जो परमात्मा कहलाता है और तीनों लोकों में प्रवेश करके सबका धारण-पोषण करने वाला अविनाशी ईश्वर है।',
    },
    attributionContext: {
      en: 'Referenced by Sant Rampal Ji Maharaj to distinguish Kshar Purush, Akshar Purush, and Param Akshar Purush (Verse 16–17).',
      hi: 'संत रामपाल जी महाराज द्वारा क्षर पुरुष, अक्षर पुरुष और परम अक्षर पुरुष के भेद को स्पष्ट करने हेतु उद्धृत (श्लोक 16-17)।',
    },
    contextBefore: {
      en: 'Verse 16 defines the two beings in the world: Kshar (perishable) and Akshar (imperishable).',
      hi: 'श्लोक 16 में संसार में दो पुरुषों—क्षर और अक्षर—का वर्णन किया गया है।',
    },
    contextAfter: {
      en: 'Verse 18 concludes with the speaker’s position relative to Kshar and Akshar.',
      hi: 'श्लोक 18 में गीता ज्ञान दाता द्वारा स्वयं की स्थिति का वर्णन है।',
    },
    topic: { en: 'Three Purushas (Tri-Purush)', hi: 'तीन पुरुषों का भेद' },
  },
  {
    publicId: 'psg-ggs-721',
    sourceTitle: { en: 'Sri Guru Granth Sahib Ji', hi: 'श्री गुरु ग्रंथ साहिब जी' },
    referenceHierarchy: {
      en: 'Raag Tilang · Mahalla 1 · Ang 721',
      hi: 'राग तिलंग · महला 1 · अंग 721',
    },
    canonicalRef: 'GGS-721-TILANG',
    edition: {
      en: 'Standard 1430-Ang SGPC Pagination',
      hi: 'मानक 1430-अंग SGPC पृष्ठांकन',
    },
    publisher: { en: 'SGPC Amritsar Pagination Standard', hi: 'SGPC अमृतसर मानक संस्करण' },
    printedPage: '721',
    pdfPage: 721,
    rightsNote: {
      en: 'Original Gurmukhi text preserved alongside Devanagari and translation.',
      hi: 'मूल गुरुमुखी पाठ के साथ देवनागरी लिप्यंतरण एवं अनुवाद सुरक्षित।',
    },
    verifiedDate: '2026-11-23',
    originalScript:
      'यक अरज गुफतम पेश तो दर गोश कुन करतार । हक्का कबीर करीम तू बेऐब परवरदिगार ॥',
    originalLanguageLabel: { en: 'Gurmukhi / Devanagari Transcript', hi: 'वाणी पाठ (देवनागरी लिप्यंतरण)' },
    publishedTranslation: {
      en: 'I make one prayer before You; listen attentively, O Creator! You are truly Kabir, Merciful, Flawless Sustainer.',
      hi: 'हे करतार! मैं तेरे समक्ष एक प्रार्थना करता हूँ, सुनिए। तू ही सत्य कबीर, दयालु, निर्दोष और पालनहार है।',
    },
    attributionContext: {
      en: 'Cited in spiritual discourses by Sant Rampal Ji Maharaj as Guru Nanak Dev Ji’s direct address to Lord Kabir.',
      hi: 'संत रामपाल जी महाराज के प्रवचनों में श्री गुरु नानक देव जी द्वारा परमात्मा कबीर साहेब की स्तुति के प्रमाण स्वरूप उद्धृत।',
    },
    contextBefore: {
      en: 'Opening lines of Guru Nanak Dev Ji’s Shabad in Raag Tilang on Ang 721.',
      hi: 'अंग 721 पर राग तिलंग में श्री गुरु नानक देव जी के शब्द की प्रारंभिक पंक्तियाँ।',
    },
    contextAfter: {
      en: 'Continues with meditation on mortality and the Creator’s sovereignty.',
      hi: 'आगे नश्वर संसार और करतार की प्रभुता का वर्णन है।',
    },
    topic: { en: 'Name & Sovereignty of Kabir', hi: 'कबीर साहेब की महिमा' },
  },
];

export interface ComparisonSample {
  publicId: string;
  title: { en: string; hi: string };
  findingCategory: { en: string; hi: string };
  scripturePerspective: { ref: string; text: { en: string; hi: string } };
  otherCommentators: { source: string; text: { en: string; hi: string } };
  santRampalJiPerspective: { source: string; text: { en: string; hi: string } };
  editorialSummary: { en: string; hi: string };
}

export const SAMPLE_COMPARISONS: ComparisonSample[] = [
  {
    publicId: 'cmp-gita-15-17',
    title: {
      en: 'Identity of the "Uttam Purusha" in Bhagavad Gita 15:16–17',
      hi: 'भगवद्गीता 15:16–17 में "उत्तम पुरुष" की पहचान',
    },
    findingCategory: {
      en: 'Interpretation Difference',
      hi: 'व्याख्यात्मक अंतर (Interpretation Difference)',
    },
    scripturePerspective: {
      ref: 'Bhagavad Gita 15:17 · Printed p. 214',
      text: {
        en: '"Uttamah purushas tv anyah paramatmety udahritah" — literally states that the Supreme Purusha is OTHER (anyah) than Kshar and Akshar.',
        hi: '"उत्तमः पुरुषस्त्वन्यः परमात्मेत्युदाहृतः" — स्पष्ट रूप से कहता है कि उत्तम पुरुष (परमात्मा) क्षर और अक्षर से अन्य है।',
      },
    },
    otherCommentators: {
      source: 'Conventional Advaita / Bhakti Commentaries',
      text: {
        en: 'Several traditional commentaries identify the speaker of the Gita himself directly as the Uttam Purusha of verse 17, treating "anyah" as distinction from jivas and Prakriti.',
        hi: 'कई पारंपरिक टीकाकार श्लोक 17 के उत्तम पुरुष को गीता वक्ता श्रीकृष्ण से अभिन्न मानते हुए "अन्यः" का अर्थ जीव और प्रकृति से भिन्न करते हैं।',
      },
    },
    santRampalJiPerspective: {
      source: 'Gyan Ganga / Gita Tera Gyan Amrit (Verified Discourse)',
      text: {
        en: 'Reads verse 17 alongside verses 16 and 18 literally: three distinct Purushas exist—Kshar Purush (Brahm/speaker), Akshar Purush (Parbrahm), and Param Akshar Purush (Supreme Kabir).',
        hi: 'श्लोक 16, 17 व 18 को क्रमानुसार पढ़ते हुए तीन भिन्न पुरुष बताते हैं—क्षर पुरुष (ब्रह्म), अक्षर पुरुष (परब्रह्म) तथा परम अक्षर पुरुष (पूर्ण परमात्मा कबीर)।',
      },
    },
    editorialSummary: {
      en: 'All editions preserve identical Sanskrit words; the divergence lies in whether "anyah" (other) distinguishes Paramatma from Kshar-Purush as well as from jivas.',
      hi: 'सभी संस्करणों में संस्कृत शब्द समान हैं; अंतर इस व्याख्या में है कि "अन्यः" शब्द परमात्मा को क्षर पुरुष से भी भिन्न दर्शाता है या नहीं।',
    },
  },
];

