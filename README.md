Projekt: Symulacja problemu czytelników i pisarzy

Opis:
Projekt dotyczy rozwiązania problemu synchronizacji wątków w kontekście biblioteki, w której czytelnicy i pisarze starają się uzyskać dostęp do zasobów w sposób zsynchronizowany.
Algorytm zarządza dostępem do zasobów biblioteki tak, aby czytelnicy mogli czytać równocześnie, ale pisarze muszą mieć wyłączny dostęp do zasobów.

Aplikacja składa się z trzech głównych komponentów:
1. Czytelnicy (Reader) - wątki, które uzyskują dostęp do biblioteki w celu czytania.
2. Pisarze (Writer) - wątki, które wymagają wyłącznego dostępu do biblioteki w celu pisania.
3. Biblioteka (Library) - zarządza dostępem do zasobów, synchronizując dostęp zarówno dla czytelników, jak i pisarzy.

Sposób uruchomienia:
Aby uruchomić program, należy skorzystać z poniższej komendy w terminalu:
java -jar .\main\target\main-1.0.jar
Po zrobieniu tego, program poprosi o podanie:
-maksymalnej liczby czytelników czytających jednocześnie
-liczba pisarzy
-liczba czytelników

Protokół komunikacyjny:
Serwer (rozumiany jako biblioteka) i klienci (czytelnicy i pisarze) komunikują się przy pomocy prostych komunikatów. Oto przykłady komunikatów:

1. Komunikaty wysyłane przez serwer:
    - "Reader X chce czytać" - wysyłane, gdy czytelnik próbuje uzyskać dostęp do biblioteki.
      Wywoływane w funkcji `requestRead(int readerId)` klasy `Library`, gdy czytelnik próbuje uzyskać dostęp do biblioteki.

    - "Writer X chce pisać" - wysyłane, gdy pisarz próbuje uzyskać dostęp do biblioteki.
      Wywoływane w funkcji `requestWrite(int writerId)` klasy `Library`, gdy pisarz próbuje uzyskać dostęp do biblioteki.

    - "Reader X wszedł i czyta" - wysyłane, gdy czytelnik otrzyma dostęp do biblioteki.
      Wywoływane w funkcji `requestRead(int readerId)` klasy `Library`, gdy czytelnik otrzymuje dostęp do biblioteki i zaczyna czytać.

    - "Writer X wszedł i pisze" - wysyłane, gdy pisarz otrzyma dostęp do biblioteki.
      Wywoływane w funkcji `requestWrite(int writerId)` klasy `Library`, gdy pisarz otrzymuje dostęp do biblioteki i zaczyna pisać.

    - "Reader X zakończył czytanie" - wysyłane, gdy czytelnik kończy swoją sesję czytania.
      Wywoływane w funkcji `finishRead(int readerId)` klasy `Library`, gdy czytelnik kończy swoją sesję czytania i wychodzi z biblioteki.

    - "Writer X zakończył pisanie" - wysyłane, gdy pisarz kończy swoją sesję pisania.
      Wywoływane w funkcji `finishWrite(int writerId)` klasy `Library`, gdy pisarz kończy swoją sesję pisania i wychodzi z biblioteki.

2. Komunikaty wysyłane przez klienta:
    - "Chcę czytać" (czytelnik wykonuje requestWrite()) - wysyłane przez czytelnika, gdy próbuje uzyskać dostęp do biblioteki.
      Wywoływane w funkcji `run()` klasy `Reader`, gdy czytelnik próbuje uzyskać dostęp do biblioteki

    - "Chcę pisać" (pisarz wykonuje requestRead()) - wysyłane przez pisarza, gdy próbuje uzyskać dostęp do biblioteki.
      Wywoływane w funkcji `run()` klasy `Writer`, gdy pisarz próbuje uzyskać dostęp do biblioteki

Szczegóły:
- Algorytm zapewnia, że czytelnicy mogą korzystać z zasobów biblioteki równocześnie, ale pisarze muszą mieć wyłączny dostęp.
- Aby zapewnić odpowiednią synchronizację, użyto mechanizmów blokujących i oczekiwania wątków.

Inne istotne informacje:
- Program obsługuje jednoczesne wątki czytelników i pisarzy.
- W trakcie działania programu wątki mogą wykonywać symulowane zadania, takie jak czytanie i pisanie z losowymi opóźnieniami.
- Program synchronizuje dostęp do zasobów za pomocą obiektów AtomicInteger, metod synchronized oraz kolejki FIFO.

---

### Synchronizacja wątków przy pomocy `wait()` i `notify()`:

1. **Metody `wait()` i `notify()`**:
   - **`wait()`**: Metoda ta jest wywoływana przez wątki, które muszą poczekać na spełnienie pewnych warunków, zanim będą mogły kontynuować swoją pracę. W przypadku tego projektu, wątki czytelników i pisarzy wywołują `wait()` w przypadku, gdy nie mogą uzyskać dostępu do biblioteki. Dzieje się tak, jeśli:
     - Czytelnik nie jest na początku kolejki (nie jest pierwszym oczekującym).
     - Liczba czytelników przekroczyła dozwoloną liczbę (`maxReadersInside`).
     - Pisarz nie jest na początku kolejki lub w bibliotece są już inni ludzie (czytelnicy lub pisarz).
   - **`notifyAll()`**: Metoda ta budzi wątki, które oczekują na warunek, gdy ten zostanie spełniony. Po zakończeniu sesji czytania lub pisania, odpowiednia metoda (`finishRead()` lub `finishWrite()`) wywołuje `notify()`, aby obudzić inne wątki oczekujące na dostęp do biblioteki.

2. **Zmienna `maxPeopleInside`**:
   - Zmienna ta reprezentuje maksymalną liczbę czytelników, którzy mogą być jednocześnie w bibliotece. Dzięki tej zmiennej algorytm kontroluje liczbę czytelników, którzy mogą wchodzić do biblioteki w tym samym czasie. Jeśli liczba ludzi w bibliotece osiągnie wartość `maxPeopleInside`, czytelnicy będą musieli poczekać na swoją kolej.

3. **Kolejka `waitingPeople`**:
   - Kolejka ta przechowuje osoby (czytelników i pisarzy) oczekujące na dostęp do biblioteki. Kolejność w kolejce determinuje, kto jako pierwszy uzyska dostęp do biblioteki. Mechanizm ten zapewnia, że wątki są obsługiwane zgodnie z zasadą "pierwszy przyszedł, pierwszy wyszedł". Dzięki użyciu `ConcurrentLinkedQueue`, kolejka jest bezpieczna w użyciu przez wiele wątków jednocześnie.

4. **Zmienna `peopleCurrentlyInside`**:
   - Zmienna ta śledzi aktualną liczbę osób w bibliotece. Używa się jej, aby kontrolować, ile osób jest w bibliotece i upewnić się, że liczba czytelników nie przekroczy limitu (`maxReadersInside`). Zmienna ta jest typu `AtomicInteger`, co pozwala na bezpieczne wykonywanie operacji zwiększania i zmniejszania liczby osób w bibliotece w sposób wielowątkowy.
