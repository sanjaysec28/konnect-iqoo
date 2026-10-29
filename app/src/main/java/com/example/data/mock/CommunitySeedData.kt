package com.example.data.mock

import com.example.data.model.AISummaryData
import com.example.data.model.BuilderProfile
import com.example.data.model.Discussion
import com.example.data.model.DiscussionReply
import com.example.data.model.MemoryItem
import com.example.data.model.NotificationItem
import com.example.data.model.Question
import com.example.data.model.QuestionLifecycleStage

object CommunitySeedData {

  val builders: List<BuilderProfile> = listOf(
    BuilderProfile(
      id = "b-1",
      name = "Arjun Menon",
      title = "Senior Android Architect",
      bio = "10+ years optimizing Android runtimes, Jetpack Compose performance, and reactive client architectures.",
      role = "Senior Android Architect",
      domains = listOf("Android", "Kotlin", "Compose", "Architecture"),
      skills = listOf("Kotlin", "Jetpack Compose", "Coroutines", "Flow", "CameraX"),
      contributionSummary = "Frequently helps with Android architecture and Compose recomposition debugging.",
      solvedQuestionsCount = 54,
      reputationScore = 985,
      isOnline = true,
      relevanceScore = 98
    ),
    BuilderProfile(
      id = "b-2",
      name = "Elena Rostova",
      title = "On-Device AI & NPU Specialist",
      bio = "Optimizing mobile model quantization (INT4/INT8), Snapdragon NPU runtimes, and low-latency audio pipelines.",
      role = "On-Device AI Specialist",
      domains = listOf("AI", "On-Device AI", "Quantization", "C++"),
      skills = listOf("Gemini API", "TensorFlow Lite", "NDK", "Quantization", "PyTorch"),
      contributionSummary = "Specializes in NPU acceleration, tensor buffers, and offline inference on mobile silicon.",
      solvedQuestionsCount = 41,
      reputationScore = 940,
      isOnline = true,
      relevanceScore = 94
    ),
    BuilderProfile(
      id = "b-3",
      name = "David Thorne",
      title = "Distributed Systems & Offline Sync Lead",
      bio = "Author of distributed local-first synchronization engines, consensus protocols, and offline-first mobile databases.",
      role = "Distributed Systems Lead",
      domains = listOf("Backend", "Distributed", "Kotlin", "Firebase"),
      skills = listOf("Firebase", "Offline Sync", "CRDT", "gRPC", "WebSockets"),
      contributionSummary = "Expert on conflict resolution, distributed queues, and resilient offline state engines.",
      solvedQuestionsCount = 49,
      reputationScore = 970,
      isOnline = true,
      relevanceScore = 96
    ),
    BuilderProfile(
      id = "b-4",
      name = "Maya Patel",
      title = "Compiler & Low-Level Performance Engineer",
      bio = "Memory profiling, ART runtime internals, NDK direct memory mapping, and cache locality optimization.",
      role = "Compiler Engineer",
      domains = listOf("DevTools", "NDK", "Performance", "Systems"),
      skills = listOf("C++", "Rust", "JNI", "Memory Profiling", "Assembly"),
      contributionSummary = "Helps builders avoid JNI overhead, GC pauses, and memory footprint spikes.",
      solvedQuestionsCount = 33,
      reputationScore = 890,
      isOnline = false,
      relevanceScore = 91
    ),
    BuilderProfile(
      id = "b-5",
      name = "Siddharth Rao",
      title = "Security & Applied Cryptography",
      bio = "Hardware-backed Android KeyStore, biometric authentication, and zero-knowledge proofs for mobile clients.",
      role = "Security Engineer",
      domains = listOf("Backend", "Security", "Crypto", "Enclave"),
      skills = listOf("KeyStore", "OAuth2", "Cryptography", "Biometrics"),
      contributionSummary = "Advises on secure storage, hardware attestation, and token refresh safety.",
      solvedQuestionsCount = 29,
      reputationScore = 860,
      isOnline = true,
      relevanceScore = 88
    ),
    BuilderProfile(
      id = "b-6",
      name = "Kenji Sato",
      title = "Cross-Platform & React Native Specialist",
      bio = "Bridging native Android TurboModules to React Native, fabric renderer optimization, and permission flows.",
      role = "Cross-Platform Lead",
      domains = listOf("Android", "Web", "React Native", "TurboModules"),
      skills = listOf("React Native", "TypeScript", "JNI", "Android SDK"),
      contributionSummary = "Deep knowledge on Android 14/15 permission migrations across hybrid frameworks.",
      solvedQuestionsCount = 38,
      reputationScore = 915,
      isOnline = true,
      relevanceScore = 95
    ),
    BuilderProfile(
      id = "b-7",
      name = "Sophia Lin",
      title = "Cloud Infrastructure & Realtime Data",
      bio = "High-throughput streaming, Redis PubSub, WebSockets multiplexing, and serverless compute scalability.",
      role = "Cloud Architect",
      domains = listOf("Backend", "Data", "Cloud", "gRPC"),
      skills = listOf("gRPC", "Go", "Kubernetes", "Redis", "Kafka"),
      contributionSummary = "Specializes in low-latency bidirectional socket protocols and cloud edge runtimes.",
      solvedQuestionsCount = 27,
      reputationScore = 840,
      isOnline = false,
      relevanceScore = 85
    ),
    BuilderProfile(
      id = "b-8",
      name = "Chloe Martin",
      title = "Jetpack Compose Performance Specialist",
      bio = "Eliminating micro-stutter, optimizing composition bounds, and profiling complex Canvas graphics in Compose.",
      role = "UI/UX Engineer",
      domains = listOf("Android", "Compose", "Performance"),
      skills = listOf("Jetpack Compose", "Compose Multiplatform", "Canvas", "Animations"),
      contributionSummary = "Known for solving subtle recomposition loops and custom layout performance.",
      solvedQuestionsCount = 36,
      reputationScore = 905,
      isOnline = true,
      relevanceScore = 92
    ),
    BuilderProfile(
      id = "b-9",
      name = "Liam O'Connor",
      title = "Database Internals & Storage Engineer",
      bio = "SQLite B-Tree internals, Room ORM, SQLite WAL mode, and vector similarity indexing directly on client devices.",
      role = "Storage Architect",
      domains = listOf("Data", "SQLite", "Room", "Storage"),
      skills = listOf("SQLite", "Room", "sqlite-vec", "SQL", "Storage Engine"),
      contributionSummary = "Regular contributor on SQLite locking mechanics and vector search caching.",
      solvedQuestionsCount = 31,
      reputationScore = 875,
      isOnline = true,
      relevanceScore = 89
    ),
    BuilderProfile(
      id = "b-10",
      name = "Priya Sharma",
      title = "DevOps & Gradle Tooling Lead",
      bio = "Automating build times with Gradle configuration cache, isolated projects, and CI/CD pipelines.",
      role = "DevOps Lead",
      domains = listOf("DevTools", "Gradle", "CI/CD", "Build Systems"),
      skills = listOf("Gradle Kotlin DSL", "GitHub Actions", "Build Cache", "Docker"),
      contributionSummary = "Guides teams cutting clean build times by 50% using modern cache APIs.",
      solvedQuestionsCount = 24,
      reputationScore = 830,
      isOnline = false,
      relevanceScore = 84
    )
  )

  val currentUser: BuilderProfile = BuilderProfile(
    id = "u-primary",
    name = "Alex",
    title = "Client Systems Engineer",
    bio = "Building high-performance mobile client infrastructure, local-first data pipelines, and AI integrations.",
    role = "Client Systems Engineer",
    domains = listOf("Android", "Kotlin", "Architecture", "AI"),
    skills = listOf("Android", "Kotlin", "Jetpack Compose", "Gemini API", "Room"),
    contributionSummary = "Active contributor in Android Architecture and AI-assisted workflows.",
    solvedQuestionsCount = 14,
    reputationScore = 780,
    isOnline = true,
    relevanceScore = 100
  )

  val questions: List<Question> = listOf(
    Question(
      id = "q-001",
      title = "React Native camera permission fails on Android 15",
      description = "After targeting SDK 35, calling camera permissions inside our TurboModule causes SecurityException unless foreground service type is declared. What is the canonical architecture?",
      authorId = "u-101",
      authorName = "Marcus Cole",
      category = "Android",
      technologies = listOf("React Native", "Android 15", "CameraX", "Permissions"),
      replyCount = 8,
      matchedBuilderCount = 3,
      timeAgo = "18m ago"
    ),
    Question(
      id = "q-002",
      title = "Best approach for offline-first Firebase sync?",
      description = "We are designing an app with heavy offline usage. What is the recommended strategy for syncing deep hierarchical document collections with Firestore without burning quota on reconnect?",
      authorId = "u-102",
      authorName = "Nadia Vance",
      category = "Backend",
      technologies = listOf("Firebase", "Offline Sync", "Firestore", "Kotlin"),
      replyCount = 12,
      matchedBuilderCount = 4,
      timeAgo = "45m ago"
    ),
    Question(
      id = "q-003",
      title = "How to optimize Gemini API latency on mobile?",
      description = "We noticed ~800ms time-to-first-token when calling gemini-2.5-flash via REST. What are the best practices for chunked SSE streaming and connection reuse in OkHttpClient?",
      authorId = "u-103",
      authorName = "Leon Zhang",
      category = "AI",
      technologies = listOf("Gemini API", "SSE", "OkHttp", "Latency"),
      replyCount = 6,
      matchedBuilderCount = 2,
      timeAgo = "1h ago"
    ),
    Question(
      id = "q-004",
      title = "Jetpack Compose recomposition bounds with unstable lambda states",
      description = "Our LazyColumn item recomposes entirely whenever a parent hoisted StateFlow updates, even when using rememberUpdatedState. How do we enforce Strong Skipping Mode properly?",
      authorId = "u-104",
      authorName = "Sarah Jenkins",
      category = "Android",
      technologies = listOf("Jetpack Compose", "Performance", "Kotlin"),
      replyCount = 9,
      matchedBuilderCount = 3,
      timeAgo = "2h ago"
    ),
    Question(
      id = "q-005",
      title = "SQLite WAL2 mode vs memory-mapped files on NVMe",
      description = "Benchmarking high-throughput telemetry inserts on local storage. Has anyone measured WAL2 commit latency vs mapped direct byte buffers on modern UFS 4.0 flash storage?",
      authorId = "u-105",
      authorName = "Tariq Mansour",
      category = "Data",
      technologies = listOf("SQLite", "Storage", "Performance", "NDK"),
      replyCount = 5,
      matchedBuilderCount = 3,
      timeAgo = "3h ago"
    ),
    Question(
      id = "q-006",
      title = "Zero-copy tensor sharing in on-device AI pipelines",
      description = "How to pass camera YUV frames directly to Qualcomm Hexagon DSP NPU tensors without intermediate RGB bitmap conversions in Android NDK?",
      authorId = "u-106",
      authorName = "Hannah Becker",
      category = "AI",
      technologies = listOf("On-Device AI", "NDK", "Quantization", "C++"),
      replyCount = 7,
      matchedBuilderCount = 2,
      timeAgo = "4h ago"
    ),
    Question(
      id = "q-007",
      title = "WebSocket vs gRPC streaming battery efficiency on 5G radio",
      description = "Direct power monitor measurements on modem radio state wakeups: Is multiplexed HTTP/2 significantly more battery efficient than long-lived WebSockets with heartbeats?",
      authorId = "u-107",
      authorName = "Devon Gray",
      category = "Backend",
      technologies = listOf("gRPC", "WebSockets", "Battery", "Networking"),
      replyCount = 11,
      matchedBuilderCount = 3,
      timeAgo = "5h ago"
    ),
    Question(
      id = "q-008",
      title = "Handling Android 15 edge-to-edge window insets in modal sheets",
      description = "When keyboard is displayed in a bottom sheet with enableEdgeToEdge(), IME insets cause visual jumping. How to coordinate WindowInsetsCompat properly with Compose ModalBottomSheet?",
      authorId = "u-108",
      authorName = "Lucas Romero",
      category = "Android",
      technologies = listOf("Android 15", "Jetpack Compose", "WindowInsets"),
      replyCount = 14,
      matchedBuilderCount = 4,
      timeAgo = "6h ago"
    ),
    Question(
      id = "q-009",
      title = "Configuring Gradle configuration cache for multi-module Kotlin projects",
      description = "Enabling org.gradle.configuration-cache fails due to custom task accessing project at execution time. How to migrate tasks to ValueSource or Provider API cleanly?",
      authorId = "u-109",
      authorName = "Amina Kalu",
      category = "DevTools",
      technologies = listOf("Gradle", "Kotlin DSL", "Build Cache"),
      replyCount = 4,
      matchedBuilderCount = 2,
      timeAgo = "8h ago"
    ),
    Question(
      id = "q-010",
      title = "Vector search indexing directly on SQLite via sqlite-vec",
      description = "Can we embed 512-dimension semantic vectors generated by on-device embedding models directly inside Room tables and run fast cosine similarity queries?",
      authorId = "u-110",
      authorName = "Viktor Orlov",
      category = "AI",
      technologies = listOf("AI", "SQLite", "Room", "Vector Search"),
      replyCount = 8,
      matchedBuilderCount = 3,
      timeAgo = "10h ago"
    )
  )

  val memories: List<MemoryItem> = listOf(
    MemoryItem(
      id = "mem-1",
      questionId = "q-002",
      title = "Handling Firebase offline synchronization",
      summary = "Resolved across 8 community discussions. Use local cache persistence with bounded pagination queries, and listen to metadata.hasPendingWrites to defer sync alerts.",
      problem = "High read billing and sync conflicts in Firestore when reconnecting after extended offline use.",
      solution = "1. Enable persistent disk cache with setPersistenceEnabled(true).\n2. Isolate client-written mutations behind a local Room staging queue.\n3. Listen to SnapshotMetadata.hasPendingWrites to indicate optimistic local state.\n4. Batch remote synchronizations with a debounce window of 1.5 seconds.",
      tags = listOf("Firebase", "Offline Sync", "Firestore"),
      technologies = listOf("Firebase", "Firestore", "Android"),
      solverName = "David Thorne",
      contributors = listOf("David Thorne", "Arjun Menon", "Nadia Vance"),
      discussionCount = 8
    ),
    MemoryItem(
      id = "mem-2",
      questionId = "q-001",
      title = "Android 15 edge-to-edge window insets resolution",
      summary = "Resolved across 6 community discussions. Mandatory enableEdgeToEdge() in SDK 35 requires explicit WindowInsets padding in Scaffold to prevent navigation bar collision.",
      problem = "System bars and IME soft keyboards overlap bottom buttons and sheet content on Android 15.",
      solution = "1. Use enableEdgeToEdge() in Activity.onCreate().\n2. Wrap layouts inside Scaffold(contentWindowInsets = WindowInsets.safeDrawing).\n3. Consume imeNestedScroll() in scrollable lists.\n4. Apply navigationBarsPadding() only to floating docking containers.",
      tags = listOf("Android 15", "WindowInsets", "Compose"),
      technologies = listOf("Jetpack Compose", "Android 15"),
      solverName = "Arjun Menon",
      contributors = listOf("Arjun Menon", "Kenji Sato", "Chloe Martin"),
      discussionCount = 6
    ),
    MemoryItem(
      id = "mem-3",
      questionId = "q-003",
      title = "Optimizing Gemini API latency via streaming SSE chunks",
      summary = "Resolved across 5 community discussions. Stream response tokens via Server-Sent Events to render first words within 180ms instead of waiting for full generation.",
      problem = "Waiting for complete generation causes 1.2s blank UI lag.",
      solution = "1. Use streamGenerateContent?alt=sse endpoint.\n2. Configure OkHttpClient with HTTP/2 and 60-second timeouts.\n3. Parse chunks asynchronously using Kotlin Flow and append to Compose state incrementally.",
      tags = listOf("Gemini API", "SSE", "Latency", "AI"),
      technologies = listOf("Gemini API", "OkHttp", "Coroutines"),
      solverName = "Elena Rostova",
      contributors = listOf("Elena Rostova", "Leon Zhang"),
      discussionCount = 5
    ),
    MemoryItem(
      id = "mem-4",
      questionId = "q-006",
      title = "Zero-copy direct ByteBuffer allocation for NPU inference",
      summary = "Resolved across 4 community discussions. Allocating pinned DirectByteBuffer directly in NDK maps onto hardware DSP DMA memory without CPU copying.",
      problem = "Frame-by-frame image conversion from YUV to RGB consumes 18ms per frame in CPU time.",
      solution = "1. Allocate native memory via AHardwareBuffer.\n2. Pass memory handle directly into Qualcomm Hexagon NPU input tensor.\n3. Avoid Bitmap.createBitmap across JNI boundary.",
      tags = listOf("On-Device AI", "NDK", "Quantization"),
      technologies = listOf("C++", "NDK", "Android"),
      solverName = "Maya Patel",
      contributors = listOf("Maya Patel", "Elena Rostova"),
      discussionCount = 4
    ),
    MemoryItem(
      id = "mem-5",
      questionId = "q-007",
      title = "gRPC multiplexed HTTP/2 streaming vs WebSocket battery consumption",
      summary = "Resolved across 7 community discussions. gRPC channels multiplexed over a single TCP connection reduced radio wakeup transitions by 18% compared to WebSocket heartbeats.",
      problem = "Constant ping/pong heartbeat packets in WebSockets hold cellular modem in high-power DCH state.",
      solution = "1. Migrate background streams to gRPC HTTP/2 multiplexed streams.\n2. Aggregate telemetry packets into 30-second transmission windows.\n3. Let radio fall back to IDLE state between active requests.",
      tags = listOf("gRPC", "Networking", "Battery"),
      technologies = listOf("gRPC", "HTTP/2", "Android"),
      solverName = "David Thorne",
      contributors = listOf("David Thorne", "Sophia Lin"),
      discussionCount = 7
    ),
    MemoryItem(
      id = "mem-6",
      questionId = "q-004",
      title = "Jetpack Compose stability with Strong Skipping Mode",
      summary = "Resolved across 9 community discussions. Enabling strong skipping mode compiler flag stabilizes unstable collection types without manual @Immutable annotations.",
      problem = "LazyColumn lists re-rendering unnecessarily during scroll.",
      solution = "1. Enable strongSkipping in composeCompiler gradle configuration.\n2. Wrap dynamic callbacks in remember { { ... } }.\n3. Use derivedStateOf for high-frequency calculation states.",
      tags = listOf("Jetpack Compose", "Performance", "Kotlin"),
      technologies = listOf("Jetpack Compose", "Compiler"),
      solverName = "Chloe Martin",
      contributors = listOf("Chloe Martin", "Arjun Menon"),
      discussionCount = 9
    )
  )

  val sampleDiscussions: Map<String, Discussion> = mapOf(
    "q-001" to Discussion(
      id = "disc-001",
      questionId = "q-001",
      question = questions[0],
      isSolved = true,
      replies = listOf(
        DiscussionReply(
          id = "rep-1",
          authorId = "b-1",
          authorName = "Arjun Menon",
          authorRole = "Senior Android Architect",
          content = "On Android 15 (SDK 35), any background or headless component accessing CameraX must bind to a ForegroundService with type `foregroundServiceType=\"camera\"`. In React Native, the permission check in AndroidManifest must declare FOREGROUND_SERVICE_CAMERA and request both runtime CAMERA and post-notification permissions before opening the camera stream.",
          isHelpful = true,
          timeAgo = "12m ago",
          helpfulVotes = 24
        ),
        DiscussionReply(
          id = "rep-2",
          authorId = "b-6",
          authorName = "Kenji Sato",
          authorRole = "Cross-Platform Specialist",
          content = "Confirmed! We ran into this exact issue when upgrading TurboModules to SDK 35. Make sure you also check `Build.VERSION.SDK_INT >= 35` inside your native module method before attaching the PreviewView lifecycle owner.",
          isHelpful = false,
          timeAgo = "8m ago",
          helpfulVotes = 9
        )
      ),
      summary = AISummaryData(
        problem = "React Native camera permission fails on Android 15 due to missing foreground service declaration.",
        keyInsight = "Android 15 mandates foregroundServiceType='camera' and runtime FOREGROUND_SERVICE_CAMERA permission for background camera bindings.",
        solutionSteps = listOf(
          "Add <uses-permission android:name=\"android.permission.FOREGROUND_SERVICE_CAMERA\" /> to AndroidManifest.",
          "Declare foregroundServiceType=\"camera\" on your hosting service tag.",
          "Verify permission grant at runtime before calling ProcessCameraProvider.bindToLifecycle()."
        ),
        usefulResources = listOf(
          "Android 15 Developer Preview: Behavior changes for foreground services",
          "CameraX Core Lifecycle Binding Best Practices"
        )
      )
    ),
    "q-002" to Discussion(
      id = "disc-002",
      questionId = "q-002",
      question = questions[1],
      isSolved = true,
      replies = listOf(
        DiscussionReply(
          id = "rep-3",
          authorId = "b-3",
          authorName = "David Thorne",
          authorRole = "Distributed Systems Lead",
          content = "The key to scaling offline Firestore without quota blowouts is: do not rely entirely on Firestore's default cache for heavy write bursts. Stage offline mutations in Room first, and trigger batched Firestore transactions only when connectivity is verified. For reads, use `Source.CACHE` first, falling back to `Source.SERVER` only on user pull-to-refresh.",
          isHelpful = true,
          timeAgo = "30m ago",
          helpfulVotes = 38
        )
      )
    )
  )

  val notifications: List<NotificationItem> = listOf(
    NotificationItem(
      id = "notif-1",
      title = "Arjun Menon replied to your question",
      message = "Shared architecture fix for Android 15 CameraX foreground service.",
      timeAgo = "12m ago",
      type = "reply",
      targetId = "q-001"
    ),
    NotificationItem(
      id = "notif-2",
      title = "Your connection request was accepted",
      message = "David Thorne accepted your builder connection request.",
      timeAgo = "1h ago",
      type = "connection",
      targetId = "b-3"
    ),
    NotificationItem(
      id = "notif-3",
      title = "Discussion added to Community Memory",
      message = "Your solution on 'React Native camera permission fails on Android 15' is now archived.",
      timeAgo = "3h ago",
      type = "memory",
      targetId = "mem-2"
    ),
    NotificationItem(
      id = "notif-4",
      title = "Elena Rostova mentioned you",
      message = "Tagged you in 'Zero-copy tensor sharing in on-device AI pipelines'.",
      timeAgo = "5h ago",
      type = "mention",
      targetId = "q-006"
    )
  )
}
