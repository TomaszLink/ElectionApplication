# Założenia biznesowe

1. Wybory są aktywne w okresie pomiędzy `startDate` oraz `endDate`.
2. Każdy niezablokowany wyborca może oddać jeden głos w każdych aktywnych wyborach.
3. Podczas dodawania wyborcy sprawdzany jest jego wiek na podstawie numeru PESEL.
4. Wybory można modyfikować wyłącznie przed ich rozpoczęciem. Jeżeli `startDate` jest wcześniejszy niż bieżąca data, modyfikacja nie jest możliwa.
5. Wyniki są dostępne dla wyborów aktywnych oraz zakończonych.

# Możliwości rozwoju

1. Ograniczenie możliwości głosowania w danych wyborach do wskazanych wyborców, np. poprzez przypisanie wyborców do grup, a następnie przypisanie grup do wyborów.  
   Przykładem biznesowym mogą być wybory lokalne, w których głosują wyłącznie mieszkańcy Gdańska, czyli wyborcy przypisani do grupy `Gdańsk`.

   W przypadku rozwoju aplikacji o logowanie użytkowników mechanizm ten można byłoby połączyć z systemem uprawnień. Podczas oddawania głosu system weryfikowałby, czy dany użytkownik posiada uprawnienie do głosowania w konkretnych wyborach.

2. Aktualnie wyniki wyborów są wyliczane każdorazowo na podstawie zapisanych głosów. Tabela `votes` jest traktowana jako źródło prawdy, a wyniki są obliczane za pomocą zapytania agregującego.

   W przypadku problemów wydajnościowych można byłoby wprowadzić dodatkową tabelę z licznikami głosów dla każdej opcji wyboru, aktualizowaną przy każdym oddanym głosie. (lub aktualizować rekordy `election_options`)

   Innym rozwiązaniem mogłoby być cykliczne przeliczanie wyników dla zakończonych wyborów, np. poprzez procedurę bazodanową, która sprawdzałaby zakończone, nieprzetworzone jeszcze wybory i zapisywała ich wyniki.
