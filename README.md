# KidSphere 🌍
**AI Adventure for Young Minds** — 儿童 AI 元宇宙教育游戏

A native Android educational game where children explore themed worlds, meet AI-powered NPCs, complete quests, learn knowledge, earn rewards, and unlock new areas.

## Game Flow
**Explore World → Meet NPC → AI Interaction → Trigger Quest → Learn → Earn Reward → Unlock New Area**

## Architecture

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| Min SDK | Android 6 (API 23) |
| Architecture | MVVM + Repository |
| Database | Room (SQLite) |
| Networking | Retrofit + OkHttp |
| AI Backend | OpenAI-compatible Chat API (configurable) |
| TTS | Alibaba Cloud NLS TTS (`Aixia` voice, child-friendly) |
| UI | ConstraintLayout + RecyclerView + Material Design |
| Async | Kotlin Coroutines + LiveData |

## Project Structure

```
app/src/main/java/com/kidsphere/game/
├── api/              # Retrofit services: AI chat + Alibaba TTS
├── data/
│   ├── db/           # Room DAOs + AppDatabase
│   └── model/        # World, Npc, Quest, Reward, Player, ChatMessage
├── repository/       # GameRepository, AiRepository, TtsRepository
├── ui/
│   ├── adapter/      # RecyclerView adapters
│   ├── npc/          # NpcListActivity, NpcChatActivity
│   ├── quest/        # QuestListActivity, QuestActivity
│   ├── settings/     # SettingsActivity (API key config)
│   └── world/        # WorldMapActivity
├── viewmodel/        # GameViewModel, NpcViewModel
└── KidSphereApp.kt
```

## Worlds & NPCs

| World | Unlock | NPCs | Subjects |
|-------|--------|------|---------|
| 🌲 Enchanted Forest | Default | Ollie the Owl, Ruby the Rabbit | Math, Language |
| 🌊 Deep Ocean | 5 ⭐ | Delphi the Dolphin, Captain Crab | Science, Geography |
| 🚀 Outer Space | 15 ⭐ | RoboNova, Zara the Alien | Science, Astronomy |
| 🏛️ Ancient Ruins | 30 ⭐ | Pharaoh Pip, Sphinx Sasha | History, Logic |

## Setup

### 1. Clone & Open in Android Studio

```bash
git clone <repo-url>
# Open in Android Studio Hedgehog or later
```

### 2. Configure API Keys

Launch the app → tap ⚙️ **Settings** and enter:

| Setting | Description |
|---------|-------------|
| AI Base URL | OpenAI-compatible endpoint (default: `https://api.openai.com/`) |
| AI API Key | Your API key (`sk-...`) |
| AI Model | Model name, e.g. `gpt-3.5-turbo` or `qwen-turbo` |
| Aliyun App Key | Alibaba Cloud NLS application key |
| Aliyun TTS Token | Short-lived token from Alibaba Cloud NLS |

> **Note**: API keys are stored in private `SharedPreferences` and never hard-coded.

### 3. Build

```bash
./gradlew assembleDebug
```

### 4. Run Unit Tests

```bash
./gradlew test
```

## Compatibility

- **Minimum**: Android 6.0 (API 23)
- **Target**: Android 14 (API 34)
- **Architecture**: arm64-v8a, armeabi-v7a, x86_64

## AI Integration Notes

The AI chat service uses an **OpenAI-compatible REST API**. You can point it at:
- [OpenAI](https://platform.openai.com) (`gpt-3.5-turbo`, `gpt-4o`)
- [Alibaba Cloud Tongyi Qianwen](https://dashscope.aliyun.com) (`qwen-turbo`, `qwen-plus`)
- Any self-hosted OpenAI-compatible server

Each NPC has a subject specialty and personality that shapes the AI system prompt, keeping conversations age-appropriate and educational.
