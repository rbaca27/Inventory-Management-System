# Distributed Inventory Management System

A high-concurrency, crash-resilient inventory management system implemented in Java. This project demonstrates distributed systems architecture, utilizing a custom Write-Ahead Logging (WAL) mechanism to ensure 100% data durability and state recovery.

## 🏗️ System Architecture

The system is designed with a **"Journal-First"** philosophy to ensure that no transaction is acknowledged until it is safely persisted to disk.

- **Client Layer:** Stores or mobile units making real-time inventory requests via TCP Sockets.
- **Service Layer (Gatekeeper):** A ServerSocket-based interface that ensures the system is fully recovered from the journal before accepting incoming traffic.
- **Logic Layer:** Utilizes `AtomicInteger` and thread-safe collections to manage inventory state without the overhead of heavy-weight locking.
- **Persistence Layer (WAL):** A `BufferedWriter` implementation that records every transaction to a `journal.txt` file before updating the in-memory state.

## ✨ Key Features

- **Crash Resilience:** Automated state recovery on startup. The system replays the `journal.txt` to rebuild the in-memory `HashMap`, ensuring zero data loss after power failures.
- **High Concurrency:** Engineered to handle multiple simultaneous "Store" requests using Java Multithreading and Atomic variables.
- **Optimized I/O:** Implements a Singleton `BufferedWriter` with manual `flush()` triggers to balance high-speed throughput with data safety.
- **Gatekeeper Protocol:** Prevents network availability until the local state is verified and synchronized with the persistent log.
