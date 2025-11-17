import {useEffect, useState} from "react";
import {getCurrentLanguage, subscribeLanguageChange} from "../utils/i18n";

const useLanguage = (): string => {
  const [language, setLanguage] = useState<string>(() => getCurrentLanguage());

  useEffect(() => {
    const unsubscribe = subscribeLanguageChange((changedLanguage) => {
      setLanguage(changedLanguage);
    });
    return () => {
      unsubscribe();
    };
  }, []);

  return language;
};

export default useLanguage;

