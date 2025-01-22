
# Project: Simulation of the Readers-Writers Problem

## Description
The project addresses the thread synchronization problem in a library context, where readers and writers compete for synchronized access to shared resources.  
The algorithm ensures that readers can read simultaneously, while writers require exclusive access to the resources.

### Main Components:
1. **Readers** - threads that access the library to read.
2. **Writers** - threads that require exclusive access to the library to write.
3. **Library** - manages access to resources, synchronizing access for both readers and writers.

---

## How to Run
To run the program, use the following command in the terminal:
```bash
java -jar .\main	arget\main-1.0.jar
```
After executing this command, the program will prompt you to specify:
- The maximum number of readers allowed to read simultaneously.
- The number of writers.
- The number of readers.

---

## Communication Protocol

### Messages Sent by the Server:
- **"Reader X wants to read"**  
  Triggered in the `requestRead(int readerId)` method of the `Library` class when a reader tries to access the library.

- **"Writer X wants to write"**  
  Triggered in the `requestWrite(int writerId)` method of the `Library` class when a writer tries to access the library.

- **"Reader X has entered and is reading"**  
  Triggered in the `requestRead(int readerId)` method of the `Library` class when a reader starts reading.

- **"Writer X has entered and is writing"**  
  Triggered in the `requestWrite(int writerId)` method of the `Library` class when a writer starts writing.

- **"Reader X has finished reading"**  
  Triggered in the `finishRead(int readerId)` method of the `Library` class when a reader finishes reading.

- **"Writer X has finished writing"**  
  Triggered in the `finishWrite(int writerId)` method of the `Library` class when a writer finishes writing.

### Messages Sent by the Client:
- **"I want to read"**  
  Triggered in the `run()` method of the `Reader` class when a reader tries to access the library.

- **"I want to write"**  
  Triggered in the `run()` method of the `Writer` class when a writer tries to access the library.

---

## Implementation Details
- **Readers** can access the library's resources simultaneously.
- **Writers** require exclusive access to the resources.
- Synchronization relies on blocking mechanisms (`wait()`, `notify()`) and FIFO queues.

---

## Thread Synchronization with `wait()` and `notify()`

### 1. Methods `wait()` and `notify()`
- **`wait()`**  
  Invoked by threads that must wait for certain conditions to be met:
   - The reader is not at the front of the queue.
   - The number of readers exceeds the allowed limit (`maxReadersInside`).
   - The writer is not at the front of the queue or other users are in the library.

- **`notifyAll()`**  
  Wakes up threads waiting for library access after a reading or writing session ends.

### 2. Variable `maxPeopleInside`
Represents the maximum number of readers allowed in the library simultaneously.

### 3. Queue `waitingPeople`
Holds threads waiting for library access. Ensures FIFO processing.

### 4. Variable `peopleCurrentlyInside`
Tracks the current number of people in the library. Uses `AtomicInteger` for safe operations in a multithreaded environment.

---

## Additional Information
- The program supports concurrent threads for readers and writers.
- Threads perform simulated tasks such as reading and writing with random delays.
- Synchronization uses mechanisms like `synchronized`, `AtomicInteger`, and FIFO queues.  
