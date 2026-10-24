# Player Radar — Minecraft 1.21.4

Klientowski mod Fabric z GUI pod klawiszem **8**. Projekt jest przygotowany dla Minecrafta 1.21.4 i powinien współpracować z Sodium używanym razem z Fabric.

## Funkcje
- ciemne GUI z fioletowym akcentem,
- lista innych graczy widocznych dla klienta w promieniu 40 bloków,
- odległość w blokach i sortowanie od najbliższego,
- wyszukiwarka oraz przewijanie listy,
- otwieranie/zamykanie klawiszem 8 i zamykanie Escape.

## Budowanie moda
Wymagane: **Java 21** i **Gradle** (lub wrapper Gradle).

1. Rozpakuj archiwum.
2. Otwórz terminal w folderze `PlayerRadar-1.21.4`.
3. Uruchom:
   - Windows: `gradle build`
   - Linux/macOS: `gradle build`
4. Gotowy JAR będzie w `build/libs/player-radar-1.0.0.jar`.
5. Włóż JAR do folderu `.minecraft/mods`. Zainstaluj Fabric Loader 1.21.4 i zgodną wersję Fabric API. Sodium pozostaje osobnym modem.

## Ograniczenia
Radar pokazuje wyłącznie graczy obecnych na liście graczy świata przekazywanej klientowi. Nie potrafi odkrywać osób, których serwer nie wysyła klientowi, ani obchodzić mechanizmów ukrywania graczy. Nie jest to gotowy skompilowany JAR; archiwum zawiera projekt źródłowy do zbudowania.

## Jeśli klawisz 8 jest zajęty
W Minecraft: Opcje → Sterowanie → Key Binds → Miscellaneous → Open Player Radar.
