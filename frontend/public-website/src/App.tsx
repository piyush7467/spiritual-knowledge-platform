import React, { useState } from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Language } from '@/i18n/translations';
import { SiteShell } from '@/components/layout/SiteShell';
import { HomePage } from '@/pages/HomePage';
import { SearchPage } from '@/pages/SearchPage';
import { ScriptureDetailPage } from '@/pages/ScriptureDetailPage';
import { ComparativeResearchPage } from '@/pages/ComparativeResearchPage';
import { AskAiPage } from '@/pages/AskAiPage';

const queryClient = new QueryClient();

export const App: React.FC = () => {
  const [lang, setLang] = useState<Language>('en');

  const toggleLanguage = () => {
    setLang((prev) => (prev === 'en' ? 'hi' : 'en'));
  };

  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <SiteShell lang={lang} onToggleLang={toggleLanguage}>
          <Routes>
            <Route path="/" element={<HomePage lang={lang} />} />
            <Route path="/search" element={<SearchPage lang={lang} />} />
            <Route path="/scriptures/:publicId" element={<ScriptureDetailPage lang={lang} />} />
            <Route path="/compare" element={<ComparativeResearchPage lang={lang} />} />
            <Route path="/ask" element={<AskAiPage lang={lang} />} />
          </Routes>
        </SiteShell>
      </BrowserRouter>
    </QueryClientProvider>
  );
};

export default App;

