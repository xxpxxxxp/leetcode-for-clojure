# Leetcode judgement for clojure

I'm currently learning Clojure and Lua, really do want to practice with Leetcode questions, sadly those languages are not supported.

The final goal of this project is providing the exactly same questions and judgements with Leetcode, for Clojure and other unsupported languages.

## Sub-projects and goals:
* A common library to login Leetcode, retrieve question / submit solution / etc
* A sniffer to retrieve all test cases for each question.  
The sniffer uses the fact that Leetcode tells you the case you failed. It will start with an incorrect solution, retrieve the first (failed) case; then hardcode the case, retrieve the second case; etc...
* A website serving retrieved questions and test cases, in both protobuf and JSON format.
* A local application judge your Clojure code with test cases
* A normalized online judge application host for performance compare

## No goal
* Leetcode Premium questions

## Steps
- [ ] Common library for Leetcode login and data structures
- [ ] Sniffer for Leetcode (exit criteria: successfully get 100 questions and their test cases)
- [ ] Website serving questions and test cases
- [ ] Judge application for Clojure
- [ ] Judge application for Lua
- [ ] Normalized online judge application host on Azure
- [ ] Auto judge-submit plugin for Intellij platform

As test cases are the key assets of Leetcode, the sniffer and website will be designed to limit traffic (RPS < 1/min).  
Personally, I'm intending to judge my Clojure code, not stealing Leetcode's assets.