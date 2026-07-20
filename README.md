# "Суперпозиция" мобильная игра

<div align="center">
  <!-- <img src="assets/logo.png" alt="Superposition Game" width="160"> -->
  <img src="https://github.com/user-attachments/assets/a951686b-9964-4ad1-8ac2-0a6d6954d41b" />

  <p>
    Многопользовательская интерактивная игра по тематике квантовых вычислений "Суперпозиция"
  </p>

  <p>
    <img src="https://img.shields.io/badge/Kotlin-2.2.21-7F52FF?logo=kotlin&logoColor=white" alt="Kotlin">
    <img src="https://img.shields.io/badge/Android-API%2021%2B-3DDC84?logo=android&logoColor=white" alt="Android">
    <img src="https://img.shields.io/badge/Jetpack%20Compose-UI-4285F4?logo=jetpackcompose&logoColor=white" alt="Jetpack Compose">
    <img src="https://img.shields.io/badge/libGDX-1.14.0-E74A45" alt="libGDX">
    <img src="https://img.shields.io/badge/Ktor-3.1.3-087CFA?logo=ktor&logoColor=white" alt="Ktor">
    <img src="https://img.shields.io/badge/STOMP-WebSocket-6A5ACD" alt="STOMP WebSocket">
  </p>
</div>

> [!NOTE]
> Backend уже развёрнут и подключается к приложению автоматически. Пользователю достаточно
> установить мобильный клиент и иметь подключение к интернету - дополнительная
> настройка или локальный запуск сервера не требуются.

## Содержание

- [Обзор](#readme-technical-overview)
- [Основные возможности клиента](#readme-features)
- [Технологический стек](#readme-technology-stack)
- [Модульная структура](#readme-modules)
- [Архитектура Android-клиента](#readme-android-architecture)
- [Интеграция Compose и libGDX в игре](#readme-compose-libgdx)
- [Сетевая архитектура](#readme-network-architecture)
- [REST API](#readme-rest-api)
- [STOMP/WebSocket](#readme-stomp-websocket)
- [Требования](#readme-requirements)
- [Планы развития](#readme-roadmap)
- [Правила игры](#readme-game-rules)
- [Автор](#readme-author)

<a name="readme-technical-overview"></a>

## Обзор

Суперпозиция - сетевой игровой клиент, в котором Android-интерфейс на
Jetpack Compose объединён с игровым полем на libGDX (в проекте используется 
расширение для Kotlin - libKTX). Клиент использует два транспортных механизма:

- REST API для регистрации, авторизации, профиля, гостевого режима и истории матчей;
- WebSocket (STOMP) для лобби, приглашений, запуска матчей, игровых состояний,
  ходов и серверного таймера.

Android-часть организована по слоям `ui → domain → data`. Общая игровая модель,
отрисовка поля, обработка взаимодействий и локальная предварительная проверка правил вынесены в
отдельный модуль `core`.

~~~text
┌──────────────────────────┐
│         UI Layer         │ - Compose / libGDX UI
├──────────────────────────┤
│       Domain Layer       │ - бизнес-логика и Use Case
├──────────────────────────┤
│        Data Layer        │ - репозитории и источники данных
└──────────────────────────┘
~~~

<a name="readme-features"></a>

## Основные возможности клиента

- регистрация и JWT-авторизация
- гостевой режим работы (без авторизации)
- онлайн-лобби
- отправка, принятие и отклонение приглашений
- получение уведомлений о приглашениях
- запуск сетевого матча
- синхронизация игрового состояния через STOMP
- отправка типизированных игровых ходов
- профиль, рейтинг и статистика пользователя
- история матчей
- библиотека игровых карт

<a name="readme-technology-stack"></a>

## Технологический стек

### Язык и сборка

| Компонент | Версия / назначение |
|---|---|
| Kotlin | `2.2.21` |
| JVM | `Java 17` |
| Android Gradle Plugin | `8.9.3` |
| Kotlin Serialization Plugin | сериализация REST- и STOMP-моделей |

### Android UI

| Технология | Назначение |
|---|---|
| Jetpack Compose | экраны, диалоги, состояние UI и тема |
| Navigation Compose | типобезопасные маршруты и back stack |
| AndroidX Lifecycle | ViewModel и lifecycle-aware state |
| AppCompat | размещение Compose и libGDX в `GameActivity` |

### Игровой слой

| Технология | Версия / назначение |
|---|---|
| libGDX | `1.14.0`, рендеринг и игровой цикл |
| libKTX | `1.13.1-rc1`, Kotlin-обёртки для libGDX |
| Scene2D | акторы карт, кубитов и игровых слотов |
| ShapeDrawer | дополнительная 2D-отрисовка |
| AssetManager / Atlas | загрузка текстур, атласов и игровых ресурсов |

### Сеть и асинхронность

| Технология | Версия / назначение |
|---|---|
| Ktor Client | `3.1.3`, REST-запросы |
| kotlinx.serialization | `1.10.0` |
| Kotlin Coroutines | `1.8.1` |
| StateFlow / SharedFlow | состояние сессии, соединения и UI |
| [StompProtocolAndroid](https://github.com/NaikSoftware/StompProtocolAndroid) | `1.6.6`, реализация STOMP над WebSocket для Android |
| OkHttp | `4.11.0`, WebSocket |

### Архитектурные подходы

| Подход | Применение |
|---|---|
| MVVM | Compose-экраны, ViewModel и управление UI state |
| UDF / MVI-like | игровой цикл `Move → server → GameState → UI` |
| Layered Architecture | разделение Android-клиента на `ui`, `domain` и `data` |
| Repository | абстракция REST, STOMP и локальных источников данных |

<a name="readme-modules"></a>

## Модульная структура

### `android`

Android-приложение и платформенная интеграция:

- Compose UI и Navigation;
- Android ViewModel;
- REST- и STOMP-клиенты;
- DTO, mapper и repository;
- пользовательская сессия и SharedPreferences;
- Android launcher;
- `GameActivity`, объединяющая Compose и libGDX;
- Android-ресурсы и нативные библиотеки.

### `core`

Игровая часть мобильного клиента, отделённая от Android UI-слоя:

- игровые модели `Card`, `Dice`, `GameState`, `PlayerState` и `SlotState`;
- sealed-модель `Move`;
- фабрики карт и кубитов;
- карточные описания и квантовые преобразования;
- `RuleEngine` и отдельные правила валидации;
- Scene2D-акторы;
- контроллер действий игрока;
- менеджеры drag-and-drop, double tap, long press и swap;
- загрузка игровых assets;
- основной libGDX-класс `Main`.

<a name="readme-android-architecture"></a>

## Архитектура Android-клиента

Android-интерфейс построен на основе паттерна `MVVM`: Compose-экраны выполняют
роль View, ViewModel управляют состоянием через, а domain-модели,
use case и репозитории формируют Model-слой.

Проект использует многослойную архитектуру:

~~~text
android
├── ui
│   ├── dialogs                      # диалоговые окна приложения и игры
│   ├── nav                          # Navigation Compose, маршруты и bottom bar
│   ├── screen                       # экраны, ViewModel и UI state
│   └── theme                        # тема и дополнительные элементы оформления
│
├── domain
│   ├── model                        # domain модели
│   ├── repository interfaces        # интерфейсы
│   └── use case                     # use case, бизнес логика
│
├── data
│   ├── dto                          # DTO классы (move, rest, socket, state)
│   ├── repository                   # работа с данными
│   ├── util                         # утилиты, мапперы
│   └── source
│       ├── AppModule.kt             # создание и хранение зависимостей
│       ├── local                    # JWT, сессия, настройки и уведомления
│       ├── rest                     # REST API
│       └── socket                   # WebSocket (STOMP)
└── AndroidLauncher.kt               # запуск приложения

core
├── config                           # настройки конфигурации игровой сцены
├── graphics                         # вспомогательные компоненты для отрисовки игровой сцены
├── manager                          # менеджеры для управления ресурсами и действиями игрока
├── model
│   ├── card                         # модели, типы и описания карт
│   ├── dice                         # модели и состояния кубитов
│   └── game                         # состояние матча, игроков, слотов и ходов
├── rule
│   ├── rule                         # механики игрвых правил
│   └── RuleEngine.kt                # проверка игровых правил
├── ui
│   ├── actor                        # Scene2D-акторы карт, кубитов и слотов
│   └── screen                       # игровое поле и его визуальные компоненты
├── Main.kt                          # запуск libGDX приложения
└── PlayerActionController.kt        # обрабтка нажатий пользователя
~~~

<a name="readme-compose-libgdx"></a>

## Интеграция Compose и libGDX в игре

Игровой экран объединяет два UI-стека:

- Compose отображает верхнюю панель игрока, таймер, фон и игровые диалоги;
- libGDX отображает интерактивное игровое поле.

`GameActivity` создаёт `Main` класс libGDX и передаёт ему:

- идентификатор игрока;
- интерфейс для вызова игровых диалогов;
- callback отправки хода;
- провайдер для получения актуального `GameState`.

libGDX размещается внутри Compose через:

~~~text
AndroidView
└── FragmentContainerView
    └── GameFragment
        └── AndroidFragmentApplication
            └── Main (libGDX)
~~~

Игровой цикл использует однонаправленный поток данных, близкий к MVI.
Действие пользователя преобразуется в типизированную команду `Move`, сервер
обрабатывает её и возвращает новое состояние `GameState`, после чего Compose
и libGDX обновляют интерфейс. Сервер выступает единственным источником истины
для состояния матча.

Актуальное состояние приходит в `GameViewModel` по STOMP. После обновления 
Compose вызывает `game.applyNewState(state)`, и игровое поле синхронизируется 
с серверным состоянием.

Действия пользователя и серверные обновления проходят по следующей цепочке:

~~~text
Действие пользователя, libGDX взаимодействие
    │  - работа менеджеров и типизация Move
    ▼
GameViewModel
    │  - передача Move - sendMove(move)
    ▼
GameRepository
    │  - отправка Move на сервер - /app/game/{gameId}/move
    ▼
SERVER (обработка хода и обновление игрового состояния)
    │  - обновлённый GameState - /user/queue/game/{gameId}
    ▼
GameRepository
    │  - получение обновлённого GameState
    ▼
GameViewModel
    │  - применение GameState applyNewState(state)
    ▼
обновление экрана, libGDX отрисовка игрового поля
~~~

<a name="readme-network-architecture"></a>

## Сетевая архитектура

### REST

Ktor `HttpClient(Android)` конфигурируется в `AppModule`:

- JSON через `ContentNegotiation`;
- `ignoreUnknownKeys = true`;
- `isLenient = true`;
- исключение для non-2xx ответа через `expectSuccess = true`;
- автоматический заголовок `Authorization: Bearer <token>`, если токен присутствует
  в `UserSession`.

### WebSocket (STOMP)

Для реализации протокола STOMP используется библиотека
[NaikSoftware/StompProtocolAndroid](https://github.com/NaikSoftware/StompProtocolAndroid).
Она предоставляет Android-клиент для подключения к STOMP-серверу через
WebSocket, подписки на destinations, отправки сообщений и наблюдения за
жизненным циклом соединения.

Соединение создаётся через `StompConnection`:

~~~text
ws://.../ws-android?userId={userId}
~~~

`StompManager` отвечает за:

- открытие и закрытие соединения;
- очередь подписок, созданных до подключения;
- отправку STOMP SEND;
- делегирование подписок в `StompSubscription`.

`StompSubscription` хранит активные подписки в и предотвращает повторную подписку на один топик.

### Жизненный цикл соединения

~~~text
AndroidLauncher.kt
├── проверка сохранённого JWT
│   ├── востановление JWT
│   ├── GET /api/users/me
│   └── востановление сессии
│
└── JWT отсутствует или некорректен
    ├── POST /api/guest/create
    └── или продолжение в гостевом режиме

UserSession.kt
└── currentUserId
    └── Network.connect(userId)
        └── STOMP WebSocket handshake
            └── connectionState = true
~~~

При успешной авторизации, выходе из аккаунта или переходе в гостевой режим
текущее соединение закрывается и создаётся заново с новым `userId`.

<a name="readme-rest-api"></a>

## REST API

API работают по URL:

~~~text
http://...
~~~

Если в `UserSession` есть JWT, Ktor автоматически добавляет Bearer token ко всем
последующим запросам.

| Метод | Endpoint | Request | Response | Назначение |
|---|---|---|---|---|
| `POST` | `/api/auth/register` | `NewUserDTO` | `AuthorizedUserDTO` | регистрация |
| `POST` | `/api/auth/login` | `AuthRequestDTO` | `AuthResponseDTO` | вход и получение JWT |
| `POST` | `/api/guest/create` | без тела | `GuestResponse` | создание гостевой сессии |
| `GET` | `/api/users/{userId}` | — | `AuthorizedUserDTO` | получение пользователя |
| `GET` | `/api/users/me` | — | `AuthorizedUserDTO` | восстановление текущей сессии |
| `PUT` | `/api/users` | `UpdateUserDTO` | `AuthorizedUserDTO` | обновление профиля |
| `DELETE` | `/api/users/{userId}` | — | без тела | удаление аккаунта |
| `GET` | `/api/history/{userId}` | — | `List<GameHistoryDTO>` | история матчей |

### Авторизация

Запрос регистрации:

~~~json
{
  "email": "player@example.com",
  "password": "password"
}
~~~

Ответ авторизации:

~~~json
{
  "token": "<jwt>",
  "user": {
    "id": 1,
    "email": "player@example.com",
    "league": "BRONZE",
    "nickname": "Player",
    "ratingPoints": 0,
    "winsAmount": 0,
    "gamesPlayed": 0,
    "createdAt": "2026-01-01T00:00:00"
  }
}
~~~

После регистрации клиент выполняет отдельный login-запрос, сохраняет JWT и
переподключает STOMP-сессию с идентификатором авторизованного пользователя.

### Гостевой режим

Ответ `POST /api/guest/create`:

~~~json
{
  "guestId": "guest-id"
}
~~~

Если сохранённого JWT нет или восстановить пользователя не удалось,
`AndroidLauncher` пытается создать гостя. При сетевой ошибке запрос повторяется
с интервалом 3 секунды.

<a name="readme-stomp-websocket"></a>

## STOMP/WebSocket

### Подключение

| Параметр | Значение |
|---|---|
| Transport | WebSocket через OkHttp |
| Protocol | STOMP |
| URL | `ws://.../ws-android` |
| Идентификация | query parameter `userId` |
| Формат payload | JSON, кроме пустых ready/init/ping сообщений |

### Destinations

| Направление | Destination | Payload | Назначение |
|---|---|---|---|
| SUBSCRIBE | `/topic/lobby` | `LobbyResponseDTO` | список игроков в лобби |
| SEND | `/app/lobby` | пустая строка | запрос начального состояния лобби |
| SEND | `/app/invite` | `InvitationDTO` | отправка приглашения |
| SUBSCRIBE | `/user/queue/invitations` | `InvitationEventDTO` | персональные события приглашений |
| SEND | `/app/invitations` | пустая строка | запрос текущих приглашений |
| SEND | `/app/invite.accept` | `InvitationDTO` | принятие приглашения |
| SEND | `/app/invite.reject` | `InvitationDTO` | отклонение приглашения |
| SUBSCRIBE | `/user/queue/game.start` | `GameStartEvent` | получение `gameId` созданного матча |
| SUBSCRIBE | `/user/queue/game/{gameId}` | `GameStateDTO` | серверное состояние матча |
| SEND | `/app/game/{gameId}/ready` | пустая строка | подтверждение готовности клиента |
| SEND | `/app/game/{gameId}/move` | полиморфный `MoveDTO` | отправка игрового действия |
| SUBSCRIBE | `/user/queue/game/{gameId}/timer` | `TimerUpdatePacketDTO` | синхронизация таймера |
| SEND | `/app/ping` | пустая строка | измерение RTT |
| SUBSCRIBE | `/user/queue/pong` | служебный payload | ответ на ping |

### Запуск матча

1. Клиент подписывается на `/user/queue/game.start`.
2. После принятия приглашения сервер создаёт матч.
3. Клиент получает `GameStartEvent` с `gameId`.
4. `Navigation` запускает отдельную `GameActivity`.
5. Клиент подписывается на `/user/queue/game/{gameId}`.
6. После клиент отправляет сообщение о готовности `/app/game/{gameId}/ready`.
7. Сервер начинает публиковать `GameStateDTO`.

### Игровые ходы

Ходы сериализуются полиморфно. Поле-дискриминатор:

~~~json
{
  "type": "PLAY_CARD"
}
~~~

Поддерживаемые типы:

| `type` | DTO | Основные поля |
|---|---|---|
| `PLAY_CARD` | `PlayCardDTO` | `playerId`, `cardId`, `targetSlotIndex`, `targetPlayerId` |
| `ROTATE_DICE` | `RotateDiceDTO` | `cardId`, slot, новое состояние, target player |
| `SWAP_DICES` | `SwapDicesDTO` | два slot index и два владельца |
| `DOUBLE_TAP` | `DoubleTapEffectDTO` | `playerId`, `cardId` |
| `RESHUFFLE_CARD` | `ReshuffleCardDTO` | `cardId`, список заменяемых карт |
| `SURRENDER` | `SurrenderDTO` | `playerId` |

<a name="readme-requirements"></a>

## Требования

- Android 5.0 (API 21) или новее;
- подключение к интернету;
- установленный APK клиента.

Приложение самостоятельно подключается к развёрнутому серверу. Устанавливать
backend, базу данных или дополнительное программное обеспечение пользователю
не требуется.

<a name="readme-roadmap"></a>

## Планы развития

Технические направления развития:

- перейти на HTTPS/WSS
- заменить ручной service locator на полноценный DI
- реализовать лидерборд с рейтингом
- добавить интеграцию ИИ-противника

## Правила игры

<a name="readme-game-rules"></a>

Полные правила мобильной версии игры "Суперпозиция".

[Открыть правила игры](PASTE_PUBLISHED_GOOGLE_DOCS_URL)

<a name="readme-author"></a>

## Автор

[winfeo](https://github.com/winfeo)
