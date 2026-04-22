# FakeIngresso

Aplicativo Android de catálogo de filmes inspirado no [Ingresso.com](https://www.ingresso.com/), desenvolvido com Jetpack Compose e Clean Architecture.

## Arquitetura

O projeto segue **Clean Architecture** dividido em 2 módulos:

```
:app  → UI (Compose Screens) + ViewModels + DI Setup
:core → Domain Models + Use Cases + Repository (Remote + Local)
```

### Fluxo de dados

```
Screen → ViewModel → UseCase → Repository → API / Room DB
```

- A **API** é a fonte remota (Retrofit)
- O **Room** é a fonte local e cache dos dados
- O `Repository` busca da API e salva no Room; as telas observam o Room via `Flow`
- Favoritos são armazenados em tabela separada (apenas `movieId`), economizando espaço

## Tech Stack

| Tecnologia | Versão | Uso |
|---|---|---|
| Kotlin | 2.0.0 | Linguagem |
| AGP | 8.6.0 | Build |
| Jetpack Compose | BOM 2024.04.01 | UI declarativa |
| Navigation Compose | 2.7.7 | Navegação entre telas |
| Koin | 3.5.6 | Injeção de dependência |
| Retrofit + OkHttp | 2.9.0 / 4.12.0 | Chamadas de API |
| Room | 2.6.1 | Banco de dados local |
| Coil | 2.6.0 | Carregamento de imagens |
| Gson | 2.10.1 | Serialização JSON |
| MockK | 1.13.10 | Mocks para testes |
| JUnit 4 | - | Framework de testes |

**Target**: compileSdk 35 / minSdk 24

## Estrutura de Pacotes

### Módulo `:app`

```
com.henry.fakeingresso/
├── application/
│   ├── MainApplication.kt          # Inicializa Koin
│   ├── modules/ConfigureModule.kt  # Agrega todos os módulos Koin
│   └── viewmodel/ViewModelModule.kt
├── main/
│   └── MainActivity.kt             # Entry point (Splash)
├── splash/
│   └── SplashScreen.kt             # Tela de splash com animação
├── home/
│   ├── HomeActivity.kt             # NavHost principal
│   ├── BaseHomeActivity.kt         # Utilitário de conectividade
│   ├── viewmodel/
│   │   ├── HomeViewModel.kt        # Lógica da home (busca, filtro, refresh)
│   │   └── HomeUiState.kt          # Sealed class dos estados da UI
│   ├── components/
│   │   └── HomeScreen.kt           # Tela home (carousel + grid + search)
│   └── extations/
│       └── MovieExtensions.kt      # Extensions para URLs de poster
├── detail/
│   ├── DetailScreen.kt             # Tela de detalhe estilo Netflix
│   └── DetailViewModel.kt          # Carrega filme por ID + favorito
├── favorites/
│   ├── FavoritesScreen.kt          # Grid de filmes favoritos
│   └── FavoritesViewModel.kt       # Observa favoritos via Flow
├── bottomnavigation/
│   ├── BottomNavItem.kt            # Home e Favorites
│   └── BottomNavigationBar.kt      # Componente da bottom bar
├── ux/                              # Componentes reutilizáveis
│   ├── MovieCarousel.kt            # Carousel horizontal de filmes
│   ├── MovieGridCard.kt            # Card do grid com indicador de favorito
│   ├── ImageCarousel.kt            # Carousel genérico de imagens
│   ├── CarouselPageIndicator.kt    # Indicador de página (dots)
│   ├── FavoriteButton.kt           # Botão de coração + ícone
│   ├── GenreChips.kt               # Chips de gênero
│   ├── RatingRow.kt                # Estrelas + nota
│   └── ContentRatingBadge.kt       # Badge de classificação indicativa
└── ui/theme/
    ├── Color.kt                    # Cores do app
    ├── Theme.kt                    # Material 3 theme
    └── Type.kt                     # Tipografia
```

### Módulo `:core`

```
com.henry.fakeingresso/
├── domain/model/
│   ├── Movie.kt                    # Modelo da API
│   ├── MovieDTO.kt                 # Entity do Room + mapper toDTO()
│   ├── MoviesResponse.kt           # Response wrapper da API
│   ├── PremiereDate.kt
│   ├── Image.kt
│   ├── Trailer.kt
│   ├── RatingDetails.kt
│   └── CompleteTag.kt
├── repository/
│   ├── GetMoviesRepository.kt      # Interface
│   ├── GetMoviesRepositoryImpl.kt  # Implementação (API + Room)
│   ├── RepositoryModule.kt         # Koin module
│   ├── remote/
│   │   ├── ApiService.kt           # Interface Retrofit
│   │   └── NetworkModule.kt        # Koin module (Retrofit, OkHttp)
│   └── local/
│       ├── AppDatabase.kt          # Room Database (v3)
│       ├── MovieDao.kt             # Queries de filmes
│       ├── FavoriteDao.kt          # Queries de favoritos
│       ├── FavoriteEntity.kt       # Entity (só movieId)
│       ├── ConvertersGson.kt       # TypeConverters para Room
│       └── LocalModule.kt          # Koin module
└── usecase/
    ├── GetMoviesUseCase.kt         # Interface
    ├── GetMoviesUseCaseImpl.kt     # Implementação (ordena por data)
    └── UseCaseModule.kt            # Koin module
```

## Navegação

```
MainActivity (Splash) → HomeActivity (NavHost)
                              ├── "home"           → HomeScreen
                              ├── "favorites"      → FavoritesScreen
                              └── "detail/{movieId}" → DetailScreen (sem bottom bar)
```

- Clicar em um filme (carousel ou grid) navega para `detail/{movieId}`
- Bottom navigation: **Home** e **Favoritos**
- A bottom bar fica oculta na tela de detalhe

## Banco de Dados (Room)

**Tabelas:**

| Tabela | Campos principais | Descrição |
|---|---|---|
| `movies` | id, title, synopsis, cast, director, rating, images... | Cache de filmes da API |
| `favorites` | movieId (PK) | IDs dos filmes favoritados |

- Favoritos usam apenas o `movieId` como referência, economizando espaço
- Query de favoritos usa subquery: `SELECT * FROM movies WHERE id IN (SELECT movieId FROM favorites)`
- Database version: 3 com `fallbackToDestructiveMigration`

## API

- **Base URL**: `https://api-content.ingresso.com/`
- **Endpoint**: `GET v0/events/coming-soon/partnership/desafio`
- **Response**: `MoviesResponse` contendo lista de `Movie`
- Configurada via `BuildConfig.BASE_URL` no módulo `:core`

## Injeção de Dependência (Koin)

Todos os módulos são agregados em `ConfigureModule.getModules()`:

```
networkModule    → Retrofit, OkHttp, ApiService
LocalModule      → Room Database, MovieDao, FavoriteDao
RepositoryModule → GetMoviesRepositoryImpl
UseCaseModule    → GetMoviesUseCaseImpl
ViewModelModule  → HomeViewModel, DetailViewModel, FavoritesViewModel
```

## Testes

Testes unitários ficam em `core/src/test/`:

```
com.henry.fakeingresso/
├── setup/
│   └── SetupTest.kt                    # Base class (Dispatchers.setMain)
├── usecase/
│   └── GetMoviesUseCaseTest.kt         # Testes do UseCase
└── repository/
    └── GetMoviesRepositoryImplTest.kt  # Testes do Repository (a implementar)
```

**Stack de testes**: JUnit 4 + MockK + kotlinx-coroutines-test

**Padrão**: Arrange → Act → Assert com mocks via MockK (`mockk`, `every`, `coEvery`, `verify`, `coVerify`)

### O que testar por camada

| Camada | O que testar |
|---|---|
| **UseCase** | Ordenação, delegação ao repository, mapeamento |
| **Repository** | Delegação ao DAO, fluxo do refreshMovies (API → delete → insert), tratamento de erro |
| **ViewModel** | Estados da UI, busca/filtro, combinação de Flows, refresh com conectividade |

## Como rodar

```bash
# Build
./gradlew assembleDebug

# Testes unitários
./gradlew :core:test

# Lint
./gradlew lint
```

## Pontos de atenção para devs

- **Versões de dependência**: `core-ktx 1.15.0` (não 1.18.0 — requer AGP 8.9.1+), `lifecycle 2.8.7` e `activity-compose 1.9.3` (versões mais novas requerem AGP mais recente)
- **Compose**: Usa plugin `kotlin.plugin.compose` em vez de `composeOptions` (obrigatório no Kotlin 2.0+)
- **BuildConfig**: AGP 8+ requer `buildFeatures { buildConfig = true }` explicitamente
- **Imagens**: Alguns filmes têm `imageFeatured` vazio — `MovieExtensions.kt` tem fallback chain entre PosterHorizontal e PosterPortrait
- **Favoritos + Filmes**: Usa `combine()` nos ViewModels para evitar race condition entre os dois Flows
