# GitTest: Просмотр репозиториев GitHub

**GitTest** — это Android-приложение, демонстрирующее современные подходы к разработке на Kotlin. Оно позволяет пользователям авторизоваться с помощью GitHub Personal Access Token и просматривать список своих репозиториев, а также детальную информацию по каждому из них.

Проект разработан с акцентом на чистую, масштабируемую и тестируемую архитектуру.

## 📱 Скриншоты

<p align="center">
  <img src="https://github.com/user-attachments/assets/8b305c8e-b484-414f-838d-ae7ecfeebe84" width="250" alt="Экран авторизации">
  <img src="https://github.com/user-attachments/assets/f198533a-6c3d-4d70-aed6-cc0b0562eb75" width="250" alt="Экран списка репозиториев">
  <img src="https://github.com/user-attachments/assets/e06b672b-4685-45d7-a62a-6cc43545f635" width="250" alt="Экран деталей репозитория">
  <br><br>
  <img src="https://github.com/user-attachments/assets/12c67c45-8126-47a6-ba1e-0ab841f9d222" width="250" alt="Отображение README">
  <img src="https://github.com/user-attachments/assets/e06b672b-4685-45d7-a62a-6cc43545f635" width="250" alt="Состояние ошибки">
</p>

## ✨ Возможности

-   **Авторизация по токену:** Безопасный вход с использованием GitHub Personal Access Token.
-   **Сохранение сессии:** Приложение запоминает токен, и повторный вход при перезапуске не требуется.
-   **Список репозиториев:** Отображение списка первых 10 репозиториев пользователя.
-   **Детальная информация:** Просмотр подробной информации о репозитории, включая:
    -   Статистику (звезды, форки, наблюдатели).
    -   Кликабельную ссылку на веб-страницу репозитория.
    -   Информацию о лицензии с корректной обработкой ее отсутствия.
    -   Отображение `README.md` файла с полноценным рендерингом Markdown (заголовки, списки, изображения, таблицы).
-   **Обработка состояний:** Грамотное отображение всех состояний UI: загрузка, ошибка (сетевая, API), пустой список и контент.

---

## 🏗 Архитектура: Clean Architecture

Проект строго следует принципам **Чистой Архитектуры**, что обеспечивает слабую связанность, независимость от фреймворков и высокую тестируемость. Архитектура разделена на три основных слоя:

###  `:app:domain` (Слой бизнес-логики)
Это ядро приложения. Он написан на чистом Kotlin и **не имеет зависимостей от Android Framework**.
-   **`UseCases`**: Инкапсулируют отдельные бизнес-сценарии (например, `SignInUseCase`, `GetReposUseCase`). Они являются точкой входа в `domain`-слой для `ViewModel`.
-   **`Repository Interface`**: Абстрактный контракт (`AppRepository`), который определяет, как `UseCases` получают данные. `Domain`-слой не знает, откуда эти данные берутся (из сети или базы данных).
-   **`Models`**: Чистые Kotlin `data class`-ы (`Repo`, `RepoDetails`), описывающие бизнес-сущности.
-   **`AppError`**: `sealed class` для представления бизнес-ошибок, не привязанный к конкретным исключениям (`HttpException`).

### `:app:data` (Слой данных)
Этот слой отвечает за предоставление данных `domain`-слою.
-   **`Repository Implementation`**: Реализует `AppRepository` интерфейс. Он является оркестратором, который решает, откуда брать данные (в данном случае — из `RemoteDataSource`). Здесь происходит маппинг из DTO в доменные модели.
-   **`DataSources`**: Абстракции над конкретными источниками данных. `RemoteDataSource` отвечает за работу с сетью.
-   **`Network (Retrofit)`**: Реализация сетевого взаимодействия с GitHub API с помощью `Retrofit` и `OkHttp Interceptor` для автоматического добавления токена.
-   **`DTO (Data Transfer Objects)`**: Модели, точно соответствующие JSON-ответам от API, с аннотациями `kotlinx.serialization`.
-   **`Mappers`**: `extension`-функции, которые преобразуют "грязные" DTO в "чистые" доменные модели. Здесь же происходит декодирование `base64` для `README`.
-   **`Storage (SharedPreferences)`**: `KeyValueStorage` — обертка над `SharedPreferences` для сохранения токена.

### `:app:ui` (Слой представления)
Этот слой отвечает за все, что видит пользователь.
-   **`View (Fragments)`**: Реализованы как "глупые" `View`. Их единственная задача — отображать состояние, полученное от `ViewModel`, и передавать в нее события пользователя.
-   **`ViewModel`**: "Мозг" каждого экрана. Реализует паттерн **MVVM** с элементами **MVI**:
    -   **`StateFlow<State>`**: Единственный источник правды для UI. `Fragment` подписывается на него и перерисовывается при изменении.
    -   **`SharedFlow<Action>`**: Для одноразовых событий (side-effects), таких как навигация или показ `Snackbar`, чтобы избежать их повторения при смене конфигурации.
-   **`Mappers (UI)`**: `ErrorMapper` — специализированный класс, который преобразует `domain`-ошибки (`AppError`) в UI-модели (`UiInfoState`), содержащие ID ресурсов.
-   **`Navigation`**: `Android Navigation Component` используется для навигации между фрагментами в рамках `Single-Activity` подхода.
-   **`ViewBinding`**: Используется для безопасного доступа к `View` из XML.

---

## 🛠 Технологический стек и библиотеки

-   **Язык:** [Kotlin](https://kotlinlang.org/)
-   **Архитектура:** Clean Architecture, MVVM, MVI-like, UDF
-   **Асинхронность:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://developer.android.com/kotlin/flow)
-   **Внедрение зависимостей:** [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
-   **Сеть:** [Retrofit 2](https://square.github.io/retrofit/) & [OkHttp 3](https://square.github.io/okhttp/)
-   **Парсинг JSON:** [Kotlinx.Serialization](https://github.com/Kotlin/kotlinx.serialization)
-   **UI:**
    -   XML & [ViewBinding](https://developer.android.com/topic/libraries/view-binding)
    -   [Material Design Components](https://material.io/develop/android/docs/getting-started)
    -   [ConstraintLayout](https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintLayout)
-   **Навигация:** [Android Navigation Component](https://developer.android.com/guide/navigation)
-   **Рендеринг Markdown:** [Markwon](https://github.com/noties/Markwon) (включая плагины для изображений и таблиц)
-   **Загрузка изображений:** [Coil](https://coil-kt.github.io/coil/) (используется плагином Markwon)

## 🚀 Как запустить

1.  Склонируйте репозиторий:
    ```bash
    git clone https://github.com/KhitrovValera/git-test.git
    ```
2.  Откройте проект в Android Studio (последней стабильной версии).
3.  Для авторизации вам понадобится [Personal Access Token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens) от вашего GitHub-аккаунта с правами на чтение репозиториев (`repo`).
4.  Соберите и запустите приложение.
