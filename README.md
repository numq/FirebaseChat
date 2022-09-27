## About
Android Jetpack Compose messenger client implementation using Firebase services

![Full architecture diagram](../main/media/firebase_chat_full_scheme.png)

## Architectural concepts and goals:
- Clean Architecture principles
- Screaming Architecture
- Modularity
- Scalability
- MVVM + State Reduce approach

## Features:
- User profile managing
- User searching
- Private chats
- Public channels _(in progress)_
- Text messages
- Voice messages _(in progress)_
- Audio calls _(in progress)_
- Video calls _(in progress)_

### Technologies:

*_Core:_*
- Kotlin
- Coroutines(Flow)
- Hilt DI
- Arrow.kt

*_Network:_*
- Peer-to-Peer  _(in progress)_
- WebRTC  _(in progress)_

*_Data:_*
- Firebase Auth
- Firebase Firestore
- Firebase Storage
- Realm DB

*_Presentation:_*
- Android
- Jetpack Compose
- Navigation Component
- Accompanist

*_Testing:_*
- JUnit
- MockK
