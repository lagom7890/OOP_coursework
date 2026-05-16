# Project Reflection

**Module Code:** CS1OPNU
**Assignment Report Title:** Project Reflection
**Student Number:** 33810887
**Actual hrs spent for the assignment:** 27
**Which Artificial Intelligence tools used:** Copilot

## 1. Introduction
For this project, I built a Customer Relationship Manager (CRM) system in Java. The system lets users manage customer profiles, log communications (emails, phone calls, and meetings), and assign and track follow-up tasks. It runs in two modes — a Command Line Interface (CLI) and a Swing-based Graphical User Interface (GUI) — so users can pick whichever they prefer.

The code is split into three layers: data models (CustomerINFO, Communications, Task), business logic controllers, and the UI. Three design patterns were used throughout: Singleton, Observer, and Factory. I also added some small human-centred touches — time-based greetings, encouraging messages when tasks are completed, a warm goodbye dialog when you close the app — because I wanted it to feel like something a real person might actually use, not just a coursework submission.

## 2. Analysis of AI Support in Software Development

### How AI Tools Were Used
I used GitHub Copilot throughout. In the early stages it handled the boring stuff — getters and setters in CustomerINFO, the skeleton of the Subject abstract class and Observer interface. When I got to EntityFactory, Copilot suggested using Java's newer switch expression to pick the right Logger based on a type string. It was neater than what I had been thinking of, so I went with it.

For the Reporting class, Copilot suggested chaining `.stream()`, `.filter()`, `.collect()`, and `Collectors.groupingBy()` to group communication logs by customer ID — exactly what I needed, and I would have taken much longer to get there on my own. It also helped with the Swing layout; JSplitPane, JTabbedPane, and GridLayout were not things I had used before, and having Copilot suggest the structure saved me from spending hours in documentation.

### Benefits of Using AI Tools
The obvious one is speed. Things like the MainCLI menu loop and all the JOptionPane dialogs in MainGUI are genuinely tedious to write. Copilot handled the repetitive bits, which meant I could spend more time on the parts that actually required thinking.

It also taught me things I would not have picked up otherwise. The stream-based filtering it suggested for `searchByName` and `filterByAgeRange` was more elegant than what I had planned. I did not just copy it — I made sure I understood what each part was doing — and I now feel comfortable using Java streams in a way I did not before this project.

### Challenges and Limitations
The biggest issue was with test code. Copilot generated assertions using Java's built-in `assert` keyword, which only does anything if you run the JVM with `-ea`. Without that flag, every assertion gets skipped silently — the tests "pass" even when something is broken. I replaced all of them with a custom `assertTrue` method that throws a RuntimeException, so failures are always caught. It is the kind of thing that is easy to miss if you just run the code and see it working.

The other frustration was that Copilot sometimes suggested things that already existed in my codebase — new utility methods that did basically the same thing as something I had written elsewhere. It has no awareness of anything outside what is currently on screen, so I had to get into the habit of checking before accepting any suggestion.

### Overall Impact on My Learning and Development
Copilot made the project feel less intimidating. When I could see a working structure taking shape quickly, it was easier to keep momentum rather than getting stuck staring at a blank file. But it also required more active thinking than I expected — you cannot just accept suggestions and move on, because some of them are wrong and some are right but do not quite fit what you are building. I think the most useful thing I took away is that AI tools work best when you already have a rough idea of what you are trying to do. When I had no idea, the suggestions were harder to evaluate.

## 3. Analysis of Software Patterns in the Project

### How the Patterns Were Used

**Singleton — SessionManager**
SessionManager stores the current user's name and their notification preference. The private constructor and `static synchronized getInstance()` mean there is only ever one instance. Both MainCLI and MainGUI use the same SessionManager, so the username entered at login is accessible everywhere without having to pass it around.

**Observer — Subject, CustomerManagement, TaskManagement**
Subject is an abstract class that keeps a list of Observer objects and calls `update()` on each when something happens. CustomerManagement and TaskManagement both extend it. MainCLI and MainGUI both implement Observer and register themselves at startup — so when a customer is added or a task is completed, the right UI gets notified automatically. In the CLI that is a line in the terminal; in the GUI it is a popup. Notifications can be toggled through SessionManager.

**Factory — EntityFactory**
EntityFactory is where all objects get created: customers, tasks, and loggers. The `createLogger()` method is the most useful part — you pass in "email", "phone", or "meeting" and get back the right Logger implementation. The calling code does not need to care which one it gets.

### Benefits of Using Software Patterns
Using these three patterns together genuinely made the code easier to work with. Without Singleton, I would have been passing SessionManager around everywhere. Without Observer, the UI would have needed to actively poll the managers to check for updates. Without Factory, every place that needed a Logger would have had its own switch statement. Centralising that in one place means if I ever added a new logger type — say, SMSLogger — I would only change EntityFactory and nothing else.

### Challenges and Limitations
The Observer implementation for the GUI was trickier than I expected. Swing requires UI updates to happen on the Event Dispatch Thread, so the `update()` method in MainGUI had to use `SwingUtilities.invokeLater()` to wrap the `JOptionPane` call. Copilot's initial suggestion did not include this, so I had to look it up and add it myself.

The Communications class still bothers me. It has three content fields (`phoneContent`, `emailContent`, `meetingContent`), but only one ever has anything in it depending on the communication type. Subclasses would have been cleaner, but felt like overkill for the scale of this project.

### Overall Impact on Project
The patterns gave the project a structure that would not fall apart if I needed to extend it. Adding a web interface would just mean implementing Observer and registering it — the business logic would not need to change at all. That kind of separation is something I understood in theory before, but this project made it feel real and worth the extra upfront effort.

## 4. Ethical and Legal Considerations

### 4.1 Ethical Concerns Related to AI Use
The thing I worried about most was over-reliance. There were moments — especially with the GUI — where I was tempted to just paste in whatever Copilot gave me without reading it. The silent-assertion bug was a direct result of that, and it is a good example of why you cannot trust AI output at face value. I tried to make sure I could explain every piece of code, though I am not sure I always succeeded.

Academic integrity is the other concern. This coursework explicitly allowed — and encouraged — AI tool use, so I was not doing anything wrong. But the boundary between "I used AI to help" and "AI wrote it for me" is not always obvious, even to myself. My approach was to use Copilot for structural boilerplate and keep the actual design decisions — which patterns to use, how the classes relate, what features to build — as my own choices. Whether I got that balance right is something I am still thinking about. It is also worth noting that AI tools tend to reproduce the most common patterns in their training data, which does not always mean the best or most inclusive option.

### 4.2 Data Handling and Privacy
The system holds names, ages, genders, phone numbers, email addresses, and home addresses. Right now everything lives in memory and disappears when you close the app — no database, no files on disk — which keeps things low-risk for a prototype.

If this ever became a real system with persistent storage, it would need to be built with UK GDPR and the Data Protection Act 2018 in mind from the start: collect only what you need, store it securely, have a clear retention policy, and give users a way to access or delete their data. Even at the prototype stage, some things could be better — phone numbers are currently stored as plain long values, which means they could easily show up in logs or debug output. A simple wrapper class that controls how they are printed would be a small but more responsible approach.

### 4.3 Broader Ethical and Legal Implications
A CRM is easy to misuse. The same features that help a business track customer interactions could just as easily be used to monitor individuals without their knowledge — logging every conversation with a vulnerable person, building up a detailed profile of their behaviour. CRM data has been misused for aggressive marketing and worse, and being a student project does not mean the design choices do not matter — the habits you build now tend to stick.

The current GUI is not accessible: no screen reader support, no keyboard-only navigation, fixed font size. A real product would need to address this under the Equality Act 2010. On licensing, I only used the Java standard library so there are no third-party issues, but Copilot's training on public repositories does raise an open question about whether AI-generated code inherits any licence conditions from that data (Freedman, 2023) — worth thinking about for any commercial work.

## 5. Conclusion
This project ended up teaching me more than I expected. I went in knowing roughly what Singleton, Observer, and Factory were, and came out actually understanding why they exist. Observer in particular made a lot more sense once I had seen what the alternative would have looked like — a UI that polls the business logic for updates, or a tangle of direct references between layers that would be a nightmare to change.

AI tools were genuinely useful, but not in a hands-off way. The silent-assertion mistake was probably the most valuable thing that happened in the whole project, because it forced me to be more careful about reviewing generated code. That is something I want to carry forward: always test AI-generated code, not just run it and see if it does not crash.

If I did this again, I would think harder about data structures before writing any code. The Communications class — three fields, only one ever used — is what happens when you start building without a clear plan. I would also think about privacy earlier, rather than treating things like phone number storage as a detail to deal with later.

## References
- Gamma, E., Helm, R., Johnson, R. and Vlissides, J. (1994). Design Patterns: Elements of Reusable Object-Oriented Software. Addison-Wesley.
- Freedman, M. (2023). The copyright problem with AI-generated code. Built In. Available at: https://builtin.com/artificial-intelligence/ai-copyright
- Information Commissioner's Office (2024). Guide to the UK GDPR. ICO. Available at: https://ico.org.uk/for-organisations/uk-gdpr-guidance-and-resources/
- Oracle (2024). Java Platform, Standard Edition Documentation. Available at: https://docs.oracle.com/en/java/
